package models

import cats.implicits._
import db.{Authorization, InternalSubscription, UsersDao}
import io.apibuilder.api.v0.models.Subscription

import javax.inject.Inject

class SubscriptionModel @Inject()(
  usersDao: UsersDao,
  orgModel: OrganizationsModel,
) {

  def toModel(mr: InternalSubscription): Option[Subscription] = {
    toModels(Seq(mr)).headOption
  }

  def toModels(subscriptions: Seq[InternalSubscription]): Seq[Subscription] = {
    val users = usersDao.findAll(
      guids = Some(subscriptions.map(_.userGuid))
    ).map { u => u.guid -> u }.toMap

    val orgs = orgModel.toModelByGuids(Authorization.All, subscriptions.map(_.organizationGuid))
      .map { o => o.guid -> o }.toMap

    subscriptions.flatMap { s =>
      (users.get(s.userGuid), orgs.get(s.organizationGuid)).mapN { case (user, org) =>
        Subscription(
          guid = s.guid,
          user = user,
          organization = org,
          publication = s.publication,
          audit = s.audit
        )
      }
    }
  }
}
