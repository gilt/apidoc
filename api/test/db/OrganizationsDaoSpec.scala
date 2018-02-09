package db

import io.apibuilder.api.v0.models.{OrganizationForm, Visibility}
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import org.junit.Assert._
import java.util.UUID

class OrganizationsDaoSpec extends PlaySpec with OneAppPerSuite with db.Helpers {

  "create" in {
    gilt.name must be("Gilt Test Org")
    gilt.key must be("gilt-test-org")
  }

  "create w/ explicit key" in {
    val name = UUID.randomUUID.toString
    val key = "key-" + UUID.randomUUID.toString
    val org = createOrganization(createdBy, name = Some(name), key = Some(key))
    org.name must be(name)
    org.key must be(key)
  }

  "update" must {

    lazy val org = createOrganization()
    lazy val form = OrganizationForm(
      name = org.name,
      key = Some(org.key),
      visibility = org.visibility,
      namespace = org.namespace
    )

    "name" in {
      val updated = organizationsDao.update(createdBy, org, form.copy(name = org.name + "2"))
      updated.name must be(org.name + "2")
    }

    "key" in {
      val updated = organizationsDao.update(createdBy, org, form.copy(key = Some(org.key + "2")))
      updated.key must be(org.key + "2")
    }

    "namespace" in {
      val updated = organizationsDao.update(createdBy, org, form.copy(namespace = org.namespace + "2"))
      updated.namespace must be(org.namespace + "2")
    }
  
    "visibility" in {
      val updated = organizationsDao.update(createdBy, org, form.copy(visibility = Visibility.Public))
      updated.visibility must be(Visibility.Public)

      val updated2 = organizationsDao.update(createdBy, org, form.copy(visibility = Visibility.Organization))
      updated2.visibility must be(Visibility.Organization)
    }

  }

  "user that creates org must be an admin" in {
    val user = upsertUser(UUID.randomUUID.toString + "@test.apibuilder.io")
    val name = UUID.randomUUID.toString
    val org = organizationsDao.createWithAdministrator(user, createOrganizationForm(name = name))
    org.name must be(name)

    membershipsDao.isUserAdmin(user, org) must be(true)
  }

  "domain" must {

    val domainName = UUID.randomUUID.toString
    val domains = Seq(domainName + ".com", UUID.randomUUID.toString + ".org")
    val org = organizationsDao.createWithAdministrator(
      createdBy,
      OrganizationForm(
        name = "Test Org " + UUID.randomUUID.toString,
        domains = Some(domains),
        namespace = "test." +UUID.randomUUID.toString
      )
    )

    "creates with domains" in {
      org.domains.map(_.name).mkString(" ") must be(domains.mkString(" "))
      val fetched = organizationsDao.findByGuid(Authorization.All, org.guid).get
      fetched.domains.map(_.name).sorted.mkString(" ") must be(domains.sorted.mkString(" "))
    }

    "defaults visibility to organization" in {
      val fetched = organizationsDao.findByGuid(Authorization.All, org.guid).get
      fetched.visibility must be(Visibility.Organization)
    }

  }

  "find by guid" in {
    organizationsDao.findByGuid(Authorization.All, gilt.guid).get.guid must equal(gilt.guid)
  }

  "findAll" must {

    val user1 = createRandomUser()
    val org1 = createOrganization(user1)

    val user2 = createRandomUser()
    val org2 = createOrganization(user2)

    "by key" in {
      organizationsDao.findAll(Authorization.All, key = Some(org1.key)).map(_.guid) must be(Seq(org1.guid))
      organizationsDao.findAll(Authorization.All, key = Some(org2.key)).map(_.guid) must be(Seq(org2.guid))
      organizationsDao.findAll(Authorization.All, key = Some(UUID.randomUUID.toString)) must be(Nil)
    }

    "by name" in {
      organizationsDao.findAll(Authorization.All, name = Some(org1.name)).map(_.guid) must be(Seq(org1.guid))
      organizationsDao.findAll(Authorization.All, name = Some(org1.name.toUpperCase)).map(_.guid) must be(Seq(org1.guid))
      organizationsDao.findAll(Authorization.All, name = Some(org1.name.toLowerCase)).map(_.guid) must be(Seq(org1.guid))
      organizationsDao.findAll(Authorization.All, name = Some(org2.name)).map(_.guid) must be(Seq(org2.guid))
      organizationsDao.findAll(Authorization.All, name = Some(UUID.randomUUID.toString)) must be(Nil)
    }

    "by namespace" in {
      organizationsDao.findAll(Authorization.All, namespace = Some(org1.namespace)).map(_.guid) must be(Seq(org1.guid))
      organizationsDao.findAll(Authorization.All, namespace = Some(org1.namespace.toUpperCase)).map(_.guid) must be(Seq(org1.guid))
      organizationsDao.findAll(Authorization.All, namespace = Some(org1.namespace.toLowerCase)).map(_.guid) must be(Seq(org1.guid))
      organizationsDao.findAll(Authorization.All, namespace = Some(org2.namespace)).map(_.guid) must be(Seq(org2.guid))
      organizationsDao.findAll(Authorization.All, namespace = Some(UUID.randomUUID.toString)) must be(Nil)
    }

    "by guid" in {
      organizationsDao.findAll(Authorization.All, guid = Some(org1.guid)).map(_.guid) must be(Seq(org1.guid))
      organizationsDao.findAll(Authorization.All, guid = Some(org2.guid)).map(_.guid) must be(Seq(org2.guid))
      organizationsDao.findAll(Authorization.All, guid = Some(UUID.randomUUID)) must be(Nil)
    }

    "by userGuid" in {
      organizationsDao.findAll(Authorization.All, userGuid = Some(user1.guid)).map(_.guid) must be(Seq(org1.guid))
      organizationsDao.findAll(Authorization.All, userGuid = Some(user2.guid)).map(_.guid) must be(Seq(org2.guid))
      organizationsDao.findAll(Authorization.All, userGuid = Some(UUID.randomUUID)) must be(Nil)
    }
  }

  "validation" must {

    "validates name" in {
      organizationsDao.validate(createOrganizationForm(name = "this is a long name")) must be(Nil)
      organizationsDao.validate(createOrganizationForm(name = "a")).head.message must be("name must be at least 3 characters")
      organizationsDao.validate(createOrganizationForm(name = gilt.name)).head.message must be("Org with this name already exists")

      organizationsDao.validate(createOrganizationForm(name = gilt.name), Some(gilt)) must be(Nil)
    }

    "validates key" in {
      organizationsDao.validate(createOrganizationForm(name = UUID.randomUUID.toString, key = Some("a"))).head.message must be("Key must be at least 3 characters")
      organizationsDao.validate(createOrganizationForm(name = UUID.randomUUID.toString, key = Some(gilt.key))).head.message must be("Org with this key already exists")
      organizationsDao.validate(createOrganizationForm(name = UUID.randomUUID.toString, key = Some(gilt.key)), Some(gilt)) must be(Nil)
    }

    "raises error if you try to create an org with a short name" in {
      intercept[java.lang.AssertionError] {
        organizationsDao.createWithAdministrator(createdBy, createOrganizationForm("a"))
      }.getMessage must be("assertion failed: name must be at least 3 characters")
    }

    "isDomainValid" in {
      organizationsDao.isDomainValid("bryzek.com") must be(true)
      organizationsDao.isDomainValid("gilt.org") must be(true)
      organizationsDao.isDomainValid("www.bryzek.com") must be(true)
      organizationsDao.isDomainValid("WWW.GILT.COM") must be(true)
      organizationsDao.isDomainValid("www gilt com") must be(false)
    }

    "validates domains" in {
      val name = UUID.randomUUID.toString
      organizationsDao.validate(createOrganizationForm(name = name, domains = None)) must be(Nil)
      organizationsDao.validate(createOrganizationForm(name = name, domains = Some(Seq("bad name")))).head.message must be("Domain bad name is not valid. Expected a domain name like apibuilder.io")
    }
  }

  "Authorization" must {

    val publicUser = createRandomUser()
    val publicOrg = createOrganization(publicUser, Some("A Public " + UUID.randomUUID().toString), visibility = Visibility.Public)

    val privateUser = createRandomUser()
    val privateOrg = createOrganization(privateUser, Some("A Private " + UUID.randomUUID().toString))

    "All" must {

      "sees both orgs" in {
        organizationsDao.findAll(Authorization.All, guid = Some(publicOrg.guid)).map(_.guid) must be(Seq(publicOrg.guid))
        organizationsDao.findAll(Authorization.All, guid = Some(privateOrg.guid)).map(_.guid) must be(Seq(privateOrg.guid))
      }

    }

    "PublicOnly" must {

      "sees only the public org" in {
        organizationsDao.findAll(Authorization.PublicOnly, guid = Some(publicOrg.guid)).map(_.guid) must be(Seq(publicOrg.guid))
        organizationsDao.findAll(Authorization.PublicOnly, guid = Some(privateOrg.guid)).map(_.guid) must be(Nil)
      }

    }

    "User" must {

      "user can see own org" in {
        organizationsDao.findAll(Authorization.User(privateUser.guid), guid = Some(publicOrg.guid)).map(_.guid) must be(Seq(publicOrg.guid))
        organizationsDao.findAll(Authorization.User(privateUser.guid), guid = Some(privateOrg.guid)).map(_.guid) must be(Seq(privateOrg.guid))
      }

      "other user cannot see private org" in {
        organizationsDao.findAll(Authorization.User(publicUser.guid), guid = Some(publicOrg.guid)).map(_.guid) must be(Seq(publicOrg.guid))
        organizationsDao.findAll(Authorization.User(publicUser.guid), guid = Some(privateOrg.guid)).map(_.guid) must be(Nil)
      }

    }

  }

}
