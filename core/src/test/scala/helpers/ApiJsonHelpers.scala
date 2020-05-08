package helpers

import java.util.UUID
import io.apibuilder.api.json.v0.models._

trait ApiJsonHelpers {

  def randomString(): String = {
    UUID.randomUUID.toString
  }

  def makeInterface(
    fields: Option[Seq[Field]] = None,
  ): Interface = {
    Interface(
      fields = fields,
    )
  }

  def makeField(
    name: String = randomString(),
    `type`: String = "string",
    required: Boolean = true,
  ): Field = {
    Field(
      name = name,
      `type` = `type`,
      required = required,
    )
  }

  def makeModel(
    fields: Seq[Field] = Nil,
    interfaces: Option[Seq[Interface]] = None,
  ): Model = {
    Model(
      fields = fields,
      interfaces = interfaces,
    )
  }

  def makeApiJson(
    name: String = randomString(),
    interfaces: Map[String, Interface] = Map.empty,
    models: Map[String, Model] = Map.empty,
  ): ApiJson = {
    ApiJson(
      name = name,
      interfaces = interfaces,
      models = models,
    )
  }
}
