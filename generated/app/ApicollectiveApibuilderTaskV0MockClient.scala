/**
 * Generated by API Builder - https://www.apibuilder.io
 * Service version: 0.16.51
 * User agent: apibuilder localhost 9000/apicollective/apibuilder-task/latest/play_2_8_mock_client
 */
package io.apibuilder.task.v0.mock {

  object Factories {

    def randomString(length: Int = 24): String = {
      _root_.scala.util.Random.alphanumeric.take(length).mkString
    }

    def makeTaskType(): io.apibuilder.task.v0.models.TaskType = io.apibuilder.task.v0.models.TaskType.Email

    def makeDiffVersionData(): io.apibuilder.task.v0.models.DiffVersionData = io.apibuilder.task.v0.models.DiffVersionData(
      oldVersionGuid = _root_.java.util.UUID.randomUUID,
      newVersionGuid = _root_.java.util.UUID.randomUUID
    )

    def makeEmailDataApplicationCreated(): io.apibuilder.task.v0.models.EmailDataApplicationCreated = io.apibuilder.task.v0.models.EmailDataApplicationCreated(
      applicationGuid = _root_.java.util.UUID.randomUUID
    )

    def makeEmailDataEmailVerificationCreated(): io.apibuilder.task.v0.models.EmailDataEmailVerificationCreated = io.apibuilder.task.v0.models.EmailDataEmailVerificationCreated(
      guid = _root_.java.util.UUID.randomUUID
    )

    def makeEmailDataMembershipCreated(): io.apibuilder.task.v0.models.EmailDataMembershipCreated = io.apibuilder.task.v0.models.EmailDataMembershipCreated(
      guid = _root_.java.util.UUID.randomUUID
    )

    def makeEmailDataMembershipRequestAccepted(): io.apibuilder.task.v0.models.EmailDataMembershipRequestAccepted = io.apibuilder.task.v0.models.EmailDataMembershipRequestAccepted(
      organizationGuid = _root_.java.util.UUID.randomUUID,
      userGuid = _root_.java.util.UUID.randomUUID,
      role = io.apibuilder.common.v0.mock.Factories.makeMembershipRole()
    )

    def makeEmailDataMembershipRequestCreated(): io.apibuilder.task.v0.models.EmailDataMembershipRequestCreated = io.apibuilder.task.v0.models.EmailDataMembershipRequestCreated(
      guid = _root_.java.util.UUID.randomUUID
    )

    def makeEmailDataMembershipRequestDeclined(): io.apibuilder.task.v0.models.EmailDataMembershipRequestDeclined = io.apibuilder.task.v0.models.EmailDataMembershipRequestDeclined(
      organizationGuid = _root_.java.util.UUID.randomUUID,
      userGuid = _root_.java.util.UUID.randomUUID
    )

    def makeEmailDataPasswordResetRequestCreated(): io.apibuilder.task.v0.models.EmailDataPasswordResetRequestCreated = io.apibuilder.task.v0.models.EmailDataPasswordResetRequestCreated(
      guid = _root_.java.util.UUID.randomUUID
    )

    def makeEmailData(): io.apibuilder.task.v0.models.EmailData = io.apibuilder.task.v0.mock.Factories.makeEmailDataApplicationCreated()

  }

}