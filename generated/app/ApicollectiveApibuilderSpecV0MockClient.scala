/**
 * Generated by API Builder - https://www.apibuilder.io
 * Service version: 0.15.90
 * apibuilder 0.15.33 app.apibuilder.io/apicollective/apibuilder-spec/latest/play_2_8_mock_client
 */
package io.apibuilder.spec.v0.mock {

  object Factories {

    def randomString(length: Int = 24): String = {
      _root_.scala.util.Random.alphanumeric.take(length).mkString
    }

    def makeMethod(): io.apibuilder.spec.v0.models.Method = io.apibuilder.spec.v0.models.Method.Get

    def makeParameterLocation(): io.apibuilder.spec.v0.models.ParameterLocation = io.apibuilder.spec.v0.models.ParameterLocation.Path

    def makeResponseCodeOption(): io.apibuilder.spec.v0.models.ResponseCodeOption = io.apibuilder.spec.v0.models.ResponseCodeOption.Default

    def makeAnnotation(): io.apibuilder.spec.v0.models.Annotation = io.apibuilder.spec.v0.models.Annotation(
      name = Factories.randomString(24),
      description = None,
      deprecation = None
    )

    def makeApidoc(): io.apibuilder.spec.v0.models.Apidoc = io.apibuilder.spec.v0.models.Apidoc(
      version = Factories.randomString(24)
    )

    def makeApplication(): io.apibuilder.spec.v0.models.Application = io.apibuilder.spec.v0.models.Application(
      key = Factories.randomString(24)
    )

    def makeAttribute(): io.apibuilder.spec.v0.models.Attribute = io.apibuilder.spec.v0.models.Attribute(
      name = Factories.randomString(24),
      value = _root_.play.api.libs.json.Json.obj(),
      description = None,
      deprecation = None
    )

    def makeBody(): io.apibuilder.spec.v0.models.Body = io.apibuilder.spec.v0.models.Body(
      `type` = Factories.randomString(24),
      description = None,
      deprecation = None,
      attributes = Nil
    )

    def makeContact(): io.apibuilder.spec.v0.models.Contact = io.apibuilder.spec.v0.models.Contact(
      name = None,
      url = None,
      email = None
    )

    def makeDeprecation(): io.apibuilder.spec.v0.models.Deprecation = io.apibuilder.spec.v0.models.Deprecation(
      description = None
    )

    def makeEnum(): io.apibuilder.spec.v0.models.Enum = io.apibuilder.spec.v0.models.Enum(
      name = Factories.randomString(24),
      plural = Factories.randomString(24),
      description = None,
      deprecation = None,
      values = Nil,
      attributes = Nil
    )

    def makeEnumValue(): io.apibuilder.spec.v0.models.EnumValue = io.apibuilder.spec.v0.models.EnumValue(
      name = Factories.randomString(24),
      description = None,
      deprecation = None,
      attributes = Nil,
      value = None
    )

    def makeField(): io.apibuilder.spec.v0.models.Field = io.apibuilder.spec.v0.models.Field(
      name = Factories.randomString(24),
      `type` = Factories.randomString(24),
      description = None,
      deprecation = None,
      default = None,
      required = true,
      minimum = None,
      maximum = None,
      example = None,
      attributes = Nil,
      annotations = Nil
    )

    def makeHeader(): io.apibuilder.spec.v0.models.Header = io.apibuilder.spec.v0.models.Header(
      name = Factories.randomString(24),
      `type` = Factories.randomString(24),
      description = None,
      deprecation = None,
      required = true,
      default = None,
      attributes = Nil
    )

    def makeImport(): io.apibuilder.spec.v0.models.Import = io.apibuilder.spec.v0.models.Import(
      uri = Factories.randomString(24),
      namespace = Factories.randomString(24),
      organization = io.apibuilder.spec.v0.mock.Factories.makeOrganization(),
      application = io.apibuilder.spec.v0.mock.Factories.makeApplication(),
      version = Factories.randomString(24),
      enums = Nil,
      interfaces = Nil,
      unions = Nil,
      models = Nil,
      annotations = Nil
    )

    def makeInfo(): io.apibuilder.spec.v0.models.Info = io.apibuilder.spec.v0.models.Info(
      license = None,
      contact = None
    )

    def makeInterface(): io.apibuilder.spec.v0.models.Interface = io.apibuilder.spec.v0.models.Interface(
      name = Factories.randomString(24),
      plural = Factories.randomString(24),
      description = None,
      deprecation = None,
      fields = Nil,
      attributes = Nil
    )

    def makeLicense(): io.apibuilder.spec.v0.models.License = io.apibuilder.spec.v0.models.License(
      name = Factories.randomString(24),
      url = None
    )

    def makeModel(): io.apibuilder.spec.v0.models.Model = io.apibuilder.spec.v0.models.Model(
      name = Factories.randomString(24),
      plural = Factories.randomString(24),
      description = None,
      deprecation = None,
      fields = Nil,
      attributes = Nil,
      interfaces = Nil
    )

    def makeOperation(): io.apibuilder.spec.v0.models.Operation = io.apibuilder.spec.v0.models.Operation(
      method = io.apibuilder.spec.v0.mock.Factories.makeMethod(),
      path = Factories.randomString(24),
      description = None,
      deprecation = None,
      body = None,
      parameters = Nil,
      responses = Nil,
      attributes = Nil
    )

    def makeOrganization(): io.apibuilder.spec.v0.models.Organization = io.apibuilder.spec.v0.models.Organization(
      key = Factories.randomString(24)
    )

    def makeParameter(): io.apibuilder.spec.v0.models.Parameter = io.apibuilder.spec.v0.models.Parameter(
      name = Factories.randomString(24),
      `type` = Factories.randomString(24),
      location = io.apibuilder.spec.v0.mock.Factories.makeParameterLocation(),
      description = None,
      deprecation = None,
      required = true,
      default = None,
      minimum = None,
      maximum = None,
      example = None,
      attributes = None
    )

    def makeResource(): io.apibuilder.spec.v0.models.Resource = io.apibuilder.spec.v0.models.Resource(
      `type` = Factories.randomString(24),
      plural = Factories.randomString(24),
      path = None,
      description = None,
      deprecation = None,
      operations = Nil,
      attributes = Nil
    )

    def makeResponse(): io.apibuilder.spec.v0.models.Response = io.apibuilder.spec.v0.models.Response(
      code = io.apibuilder.spec.v0.mock.Factories.makeResponseCode(),
      `type` = Factories.randomString(24),
      headers = None,
      description = None,
      deprecation = None,
      attributes = None
    )

    def makeService(): io.apibuilder.spec.v0.models.Service = io.apibuilder.spec.v0.models.Service(
      apidoc = None,
      name = Factories.randomString(24),
      organization = io.apibuilder.spec.v0.mock.Factories.makeOrganization(),
      application = io.apibuilder.spec.v0.mock.Factories.makeApplication(),
      namespace = Factories.randomString(24),
      version = Factories.randomString(24),
      baseUrl = None,
      description = None,
      info = io.apibuilder.spec.v0.mock.Factories.makeInfo(),
      headers = Nil,
      imports = Nil,
      enums = Nil,
      interfaces = Nil,
      unions = Nil,
      models = Nil,
      resources = Nil,
      attributes = Nil,
      annotations = Nil
    )

    def makeUnion(): io.apibuilder.spec.v0.models.Union = io.apibuilder.spec.v0.models.Union(
      name = Factories.randomString(24),
      plural = Factories.randomString(24),
      discriminator = None,
      description = None,
      deprecation = None,
      types = Nil,
      attributes = Nil,
      interfaces = Nil
    )

    def makeUnionType(): io.apibuilder.spec.v0.models.UnionType = io.apibuilder.spec.v0.models.UnionType(
      `type` = Factories.randomString(24),
      description = None,
      deprecation = None,
      attributes = Nil,
      default = None,
      discriminatorValue = None
    )

    def makeResponseCode(): io.apibuilder.spec.v0.models.ResponseCode = io.apibuilder.spec.v0.models.ResponseCodeInt(1)

  }

}