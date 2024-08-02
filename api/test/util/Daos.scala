package util

import db._
import db.generated.SessionsDao
import db.generators.{GeneratorsDao, ServicesDao}
import lib.{DatabaseServiceFetcher, Emails}
import models.VersionsModel
import play.api.Application
import play.api.inject.Injector

trait Daos {
  def app: Application
  def injector: Injector = app.injector

  def applicationsDao: InternalApplicationsDao = injector.instanceOf[db.InternalApplicationsDao]
  def attributesDao: InternalAttributesDao = injector.instanceOf[db.InternalAttributesDao]
  def changesDao: InternalChangesDao = injector.instanceOf[db.InternalChangesDao]
  def databaseServiceFetcher: DatabaseServiceFetcher = injector.instanceOf[DatabaseServiceFetcher]
  def emailVerificationsDao: InternalEmailVerificationsDao = injector.instanceOf[db.InternalEmailVerificationsDao]
  def itemsDao: ItemsDao = injector.instanceOf[db.ItemsDao]
  def membershipRequestsDao: InternalMembershipRequestsDao = injector.instanceOf[db.InternalMembershipRequestsDao]
  def membershipsDao: MembershipsDao = injector.instanceOf[db.MembershipsDao]
  def usersDao: InternalUsersDao = injector.instanceOf[db.InternalUsersDao]

  def organizationAttributeValuesDao: InternalOrganizationAttributeValuesDao = injector.instanceOf[db.InternalOrganizationAttributeValuesDao]
  def organizationDomainsDao: InternalOrganizationDomainsDao = injector.instanceOf[db.InternalOrganizationDomainsDao]
  def organizationLogsDao: OrganizationLogsDao = injector.instanceOf[db.OrganizationLogsDao]
  def organizationsDao: InternalOrganizationsDao = injector.instanceOf[db.InternalOrganizationsDao]
  def originalsDao: OriginalsDao = injector.instanceOf[db.OriginalsDao]
  def passwordResetRequestsDao: PasswordResetRequestsDao = injector.instanceOf[db.PasswordResetRequestsDao]
  def sessionsDao: SessionsDao =  injector.instanceOf[SessionsDao]

  def subscriptionsDao: InternalSubscriptionsDao = injector.instanceOf[db.InternalSubscriptionsDao]
  def tokensDao: InternalTokensDao = injector.instanceOf[db.InternalTokensDao]
  def userPasswordsDao: InternalUserPasswordsDao = injector.instanceOf[db.InternalUserPasswordsDao]
  def versionsDao: VersionsDao = injector.instanceOf[db.VersionsDao]

  def servicesDao: ServicesDao = injector.instanceOf[db.generators.ServicesDao]
  def generatorsDao: GeneratorsDao = injector.instanceOf[db.generators.GeneratorsDao]

  def emails: Emails = injector.instanceOf[Emails]

  def sessionHelper: SessionHelper = injector.instanceOf[SessionHelper]
}
