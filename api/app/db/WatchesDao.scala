package db

import anorm._
import io.apibuilder.api.v0.models.{Error, Organization, User, WatchForm}
import io.apibuilder.common.v0.models.{Audit, ReferenceGuid}
import io.flow.postgresql.Query
import lib.Validation
import play.api.db._

import java.util.UUID
import javax.inject.{Inject, Singleton}

case class ValidatedWatchForm(
  org: Organization,
  application: InternalApplication,
  form: WatchForm
)

case class InternalWatch(
                               guid: UUID,
                               audit: Audit,
                               applicationGuid: UUID,
                               userGuid: UUID
                             )
@Singleton
class WatchesDao @Inject() (
  @NamedDatabase("default") db: Database,
  applicationsDao: ApplicationsDao,
  organizationsDao: OrganizationsDao,
  usersDao: UsersDao
) {

  private val dbHelpers = DbHelpers(db, "watches")

  private val BaseQuery = Query(s"""
    select guid, user_guid, application_guid,
           ${AuditsDao.queryCreationDefaultingUpdatedAt("watches")}
      from watches
  """)

  private val InsertQuery = """
    insert into watches
    (guid, user_guid, application_guid, created_by_guid)
    values
    ({guid}::uuid, {user_guid}::uuid, {application_guid}::uuid, {created_by_guid}::uuid)
  """

  def validate(
    auth: Authorization,
    form: WatchForm
  ): Either[Seq[Error], ValidatedWatchForm] = {
    val userErrors = usersDao.findByGuid(form.userGuid) match {
      case None => Seq("User not found")
      case Some(_) => Nil
    }

    val org: Option[Organization] = organizationsDao.findByKey(auth, form.organizationKey)
    val application: Option[InternalApplication] = org.flatMap { o =>
      applicationsDao.findByOrganizationKeyAndApplicationKey(auth, o.key, form.applicationKey)
    }

    val applicationKeyErrors = application match {
      case None => Seq(s"Application[${form.applicationKey}] not found")
      case Some(_) => Nil
    }


    (applicationKeyErrors ++ userErrors).toList match {
      case Nil => {
        Right(
          ValidatedWatchForm(
            org = org.get,
            application = application.get,
            form = form
          )
        )
      }
      case errors => {
        Left(Validation.errors(errors))
      }
    }
  }

  def upsert(createdBy: User, fullForm: ValidatedWatchForm): InternalWatch = {
    val application = fullForm.application
    val guid = UUID.randomUUID

    db.withConnection { implicit c =>
      SQL(InsertQuery).on(
        "guid" -> guid,
        "user_guid" -> fullForm.form.userGuid,
        "application_guid" -> application.guid,
        "created_by_guid" -> createdBy.guid
      ).execute()
    }

    findByGuid(Authorization.All, guid).getOrElse {
      sys.error("Failed to create watch")
    }
  }

  def softDelete(deletedBy: User, watch: InternalWatch): Unit =  {
    dbHelpers.delete(deletedBy, watch.guid)
  }

  def findByGuid(authorization: Authorization, guid: UUID): Option[InternalWatch] = {
    findAll(authorization, guid = Some(guid), limit = 1).headOption
  }

  def findAll(
    authorization: Authorization,
    guid: Option[UUID] = None,
    organizationKey: Option[String] = None,
    applicationGuid: Option[UUID] = None,
    applicationKey: Option[String] = None,
    userGuid: Option[UUID] = None,
    isDeleted: Option[Boolean] = Some(false),
    limit: Long = 25,
    offset: Long = 0
  ): Seq[InternalWatch] = {
    db.withConnection { implicit c =>
      authorization.applicationFilter(BaseQuery, "application_guid").
        equals("watches.guid", guid).
        equals("organizations.key", organizationKey).
        equals("watches.application_guid", applicationGuid).
        equals("applications.key", applicationKey).
        equals("watches.user_guid", userGuid).
        and(isDeleted.map(Filters.isDeleted("watches", _))).
        orderBy("applications.key, watches.created_at").
        limit(limit).
        offset(offset).
        as(parser.*)
    }
  }

  private val parser: RowParser[InternalWatch] = {
    import org.joda.time.DateTime

    SqlParser.get[UUID]("guid") ~
      SqlParser.get[DateTime]("created_at") ~
      SqlParser.get[UUID]("created_by_guid") ~
      SqlParser.get[DateTime]("updated_at") ~
      SqlParser.get[UUID]("updated_by_guid") ~
      SqlParser.get[UUID]("application_guid") ~
      SqlParser.get[UUID]("user_guid") map {
      case guid ~ createdAt ~ createdByGuid ~ updatedAt ~ updatedByGuid ~ applicationGuid ~ userGuid => {
        InternalWatch(
          guid = guid,
          applicationGuid = applicationGuid,
          userGuid = userGuid,
          audit = Audit(
            createdAt = createdAt,
            createdBy = ReferenceGuid(createdByGuid),
            updatedAt = updatedAt,
            updatedBy = ReferenceGuid(updatedByGuid),
          )
        )
      }
    }
  }

}
