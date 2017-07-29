package db

import io.apibuilder.api.v0.models._
import io.apibuilder.api.v0.models.json._
import io.apibuilder.common.v0.models.{Audit, ReferenceGuid}
import io.apibuilder.common.v0.models.json._
import io.flow.postgresql.Query
import lib.{Misc, Role, Validation, UrlKey}
import anorm._
import javax.inject.{Inject, Singleton}
import play.api.db._
import play.api.Play.current
import play.api.libs.json._
import java.util.UUID
import org.joda.time.DateTime

@Singleton
class OrganizationsDao @Inject() (
  organizationDomainsDao: OrganizationDomainsDao,
  organizationLogsDao: OrganizationLogsDao
) {
  // TODO: resolve cicrular dependency
  private[this] def membershipsDao = play.api.Play.current.injector.instanceOf[MembershipsDao]

  private[this] val DefaultVisibility = Visibility.Organization

  private[this] val MinNameLength = 3

  private[db] val BaseQuery = Query(s"""
    select organizations.guid,
           organizations.name,
           organizations.key,
           organizations.visibility,
           organizations.namespace,
           ${AuditsParserDao.query("organizations")},
           coalesce(
             (select array_to_string(array_agg(domain), ' ') 
                from organization_domains
               where deleted_at is null
                 and organization_guid = organizations.guid),
             '[]'
           ) as domains
      from organizations
  """)

  private[this] val InsertQuery = """
    insert into organizations
    (guid, name, key, namespace, visibility, created_by_guid, updated_by_guid)
    values
    ({guid}::uuid, {name}, {key}, {namespace}, {visibility}, {user_guid}::uuid, {user_guid}::uuid)
  """

  private[this] val UpdateQuery = """
    update organizations
       set name = {name},
           key = {key},
           namespace = {namespace},
           visibility = {visibility},
           updated_by_guid = {user_guid}::uuid
     where guid = {guid}::uuid
  """

  def validate(
    form: OrganizationForm,
    existing: Option[Organization] = None
  ): Seq[io.apibuilder.api.v0.models.Error] = {

    val nameErrors = if (form.name.length < MinNameLength) {
      Seq(s"name must be at least $MinNameLength characters")
    } else {
      findAll(Authorization.All, name = Some(form.name), limit = 1).headOption match {
        case None => Nil
        case Some(org: Organization) => {
          if (existing.map(_.guid) == Some(org.guid)) {
            Nil
          } else {
            Seq("Org with this name already exists")
          }
        }
      }
    }

    val keyErrors = form.key match {
      case None => {
        Nil
      }

      case Some(key) => {
        UrlKey.validate(key) match {
          case Nil => {
            findByKey(Authorization.All, key) match {
              case None => Nil
              case Some(found) => {
                if (existing.map(_.guid) == Some(found.guid)) {
                  Nil
                } else {
                  Seq("Org with this key already exists")
                }
              }
            }
          }
          case errors => errors
        }
      }
    }

    val namespaceErrors = findAll(Authorization.All, namespace = Some(form.namespace.trim), limit = 1).headOption match {
      case None => {
        isDomainValid(form.namespace.trim) match {
          case true => Nil
          case false => Seq("Namespace is not valid. Expected a name like io.apibuilder")
        }
      }
      case Some(org: Organization) => {
        Nil
      }
    }

    val visibilityErrors =  Visibility.fromString(form.visibility.toString) match {
      case Some(_) => Nil
      case None => Seq(s"Invalid visibility[${form.visibility.toString}]")
    }

    val domainErrors = form.domains.getOrElse(Nil).filter(!isDomainValid(_)).map(d => s"Domain $d is not valid. Expected a domain name like apibuilder.io")

    Validation.errors(nameErrors ++ keyErrors ++ namespaceErrors ++ visibilityErrors ++ domainErrors)
  }


  // We just care that the domain does not have a space in it
  private[this] val DomainRx = """^[^\s]+$""".r
  private[db] def isDomainValid(domain: String): Boolean = {
    domain match {
      case DomainRx() => true
      case _ => false
    }
  }

  /**
   * Creates the org and assigns the user as its administrator.
   */
  def createWithAdministrator(user: User, form: OrganizationForm): Organization = {
    DB.withTransaction { implicit c =>
      val org = create(c, user, form)
      membershipsDao.create(c, user, org, user, Role.Admin)
      organizationLogsDao.create(c, user, org, s"Created organization and joined as ${Role.Admin.name}")
      org
    }
  }

  def findByEmailDomain(email: String): Option[Organization] = {
    Misc.emailDomain(email).flatMap { domain =>
      organizationDomainsDao.findAll(domain = Some(domain)).headOption.flatMap { domain =>
        findByGuid(Authorization.All, domain.organizationGuid)
      }
    }
  }

  def update(user: User, existing: Organization, form: OrganizationForm): Organization = {
    val errors = validate(form, Some(existing))
    assert(errors.isEmpty, errors.map(_.message).mkString("\n"))

    DB.withConnection { implicit c =>
      SQL(UpdateQuery).on(
        'guid -> existing.guid,
        'name -> form.name.trim,
        'key -> form.key.getOrElse(UrlKey.generate(form.name)).trim,
        'namespace -> form.namespace.trim,
        'visibility -> form.visibility.toString,
        'user_guid -> user.guid
      ).execute()
    }

    // TODO: Figure out how we want to handle domains. Best option
    // might be to remove domains from organization_form

    findByGuid(Authorization.All, existing.guid).getOrElse {
      sys.error("Failed to update org")
    }
  }

  private[this] def create(implicit c: java.sql.Connection, user: User, form: OrganizationForm): Organization = {
    val errors = validate(form)
    assert(errors.isEmpty, errors.map(_.message).mkString("\n"))

    val org = Organization(
      guid = UUID.randomUUID,
      key = form.key.getOrElse(UrlKey.generate(form.name)).trim,
      name = form.name.trim,
      namespace = form.namespace.trim,
      visibility = form.visibility,
      domains = form.domains.getOrElse(Nil).map(Domain(_)),
      audit = Audit(
        createdAt = DateTime.now,
        createdBy = ReferenceGuid(user.guid),
        updatedAt = DateTime.now,
        updatedBy = ReferenceGuid(user.guid)
      )
    )

    SQL(InsertQuery).on(
      'guid -> org.guid,
      'name -> org.name,
      'namespace -> org.namespace,
      'visibility -> org.visibility.toString,
      'key -> org.key,
      'user_guid -> user.guid
    ).execute()

    org.domains.foreach { domain =>
      organizationDomainsDao.create(c, user, org, domain.name)
    }

    org
  }

  def softDelete(deletedBy: User, org: Organization) {
    SoftDelete.delete("organizations", deletedBy, org.guid)
  }

  def findByGuid(authorization: Authorization, guid: UUID): Option[Organization] = {
    findAll(authorization, guid = Some(guid), limit = 1).headOption
  }

  def findByUserAndGuid(user: User, guid: UUID): Option[Organization] = {
    findByGuid(Authorization.User(user.guid), guid)
  }

  def findByUserAndKey(user: User, orgKey: String): Option[Organization] = {
    findByKey(Authorization.User(user.guid), orgKey)
  }

  def findByKey(authorization: Authorization, orgKey: String): Option[Organization] = {
    findAll(authorization, key = Some(orgKey), limit = 1).headOption
  }

  def findAll(
    authorization: Authorization,
    guid: Option[UUID] = None,
    userGuid: Option[UUID] = None,
    application: Option[Application] = None,
    key: Option[String] = None,
    name: Option[String] = None,
    namespace: Option[String] = None,
    isDeleted: Option[Boolean] = Some(false),
    limit: Long = 25,
    offset: Long = 0
  ): Seq[Organization] = {
    DB.withConnection { implicit c =>
      Authorization2(authorization).organizationFilter(BaseQuery).
        equals("organizations.guid", guid).
        equals("organizations.key", key).
        and(
          userGuid.map { _ =>
            "organizations.guid in (select organization_guid from memberships where deleted_at is null and user_guid = {user_guid}::uuid)"
          }
        ).bind("user_guid", userGuid).
        and(
          application.map { _ =>
            "organizations.guid in (select organization_guid from applications where deleted_at is null and guid = {application_guid}::uuid)"
          }
        ).bind("application_guid", application.map(_.guid)).
        and(
          name.map { _ =>
            "lower(trim(organizations.name)) = lower(trim({name}))"
          }
        ).bind("name", name).
        and(
          name.map { _ =>
            "organizations.namespace = lower(trim({namespace}))"
          }
        ).bind("namespace", namespace).
        and(isDeleted.map(Filters2.isDeleted("organizations", _))).
        orderBy("lower(organizations.name), organizations.created_at").
        limit(limit).
        offset(offset).
        anormSql().as(
          io.apibuilder.api.v0.anorm.parsers.Organization.parser().*
        )
    }
  }

  private[db] def fromRow(
    row: anorm.Row
  ): Organization = {
    summaryFromRow(row).copy(
      domains = row[Option[String]]("domains").fold(Nil[String])(_.split(" ")).sorted.map(Domain(_))
    )
  }

  private[db] def summaryFromRow(
    row: anorm.Row,
    prefix: Option[String] = None
  ): Organization = {
    val p = prefix.map( _ + "_").getOrElse("")

    Organization(
      guid = row[UUID](s"${p}guid"),
      key = row[String](s"${p}key"),
      name = row[String](s"${p}name"),
      namespace = row[String](s"${p}namespace"),
      visibility = Visibility(row[String](s"${p}visibility")),
      audit = AuditsDao.fromRow(row, prefix)
    )
  }

}
