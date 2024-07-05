/**
 * Generated by API Builder - https://www.apibuilder.io
 * Service version: 0.16.50
 * User agent: apibuilder localhost 9000/apicollective/apibuilder-spec/latest/anorm_2_8_scala_3_parsers
 */
package io.apibuilder.spec.v0.anorm.conversions {

  import anorm.{Column, MetaDataItem, TypeDoesNotMatch}
  import play.api.libs.json.{JsArray, JsObject, JsValue}
  import scala.util.{Failure, Success, Try}
  import play.api.libs.json.JodaReads._

  /**
    * Conversions to collections of objects using JSON.
    */
  object Util {

    def parser[T](
      f: play.api.libs.json.JsValue => T
    ) = anorm.Column.nonNull { (value, meta) =>
      val MetaDataItem(columnName, nullable, clazz) = meta
      value match {
        case json: org.postgresql.util.PGobject => parseJson(f, columnName.qualified, json.getValue)
        case json: java.lang.String => parseJson(f, columnName.qualified, json)
        case _=> {
          Left(
            TypeDoesNotMatch(
              s"Column[${columnName.qualified}] error converting $value to Json. Expected instance of type[org.postgresql.util.PGobject] and not[${value.asInstanceOf[AnyRef].getClass}]"
            )
          )
        }


      }
    }

    private def parseJson[T](f: play.api.libs.json.JsValue => T, columnName: String, value: String) = {
      Try {
        f(
          play.api.libs.json.Json.parse(value)
        )
      } match {
        case Success(result) => Right(result)
        case Failure(ex) => Left(
          TypeDoesNotMatch(
            s"Column[$columnName] error parsing json $value: $ex"
          )
        )
      }
    }

  }

  object Types {
    import io.apibuilder.spec.v0.models.json._
    implicit val columnToSeqApibuilderSpecMethod: Column[Seq[_root_.io.apibuilder.spec.v0.models.Method]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Method]] }
    implicit val columnToSeqApibuilderSpecParameterLocation: Column[Seq[_root_.io.apibuilder.spec.v0.models.ParameterLocation]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.ParameterLocation]] }
    implicit val columnToSeqApibuilderSpecResponseCodeOption: Column[Seq[_root_.io.apibuilder.spec.v0.models.ResponseCodeOption]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.ResponseCodeOption]] }
    implicit val columnToSeqApibuilderSpecAnnotation: Column[Seq[_root_.io.apibuilder.spec.v0.models.Annotation]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Annotation]] }
    implicit val columnToSeqApibuilderSpecApidoc: Column[Seq[_root_.io.apibuilder.spec.v0.models.Apidoc]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Apidoc]] }
    implicit val columnToSeqApibuilderSpecApplication: Column[Seq[_root_.io.apibuilder.spec.v0.models.Application]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Application]] }
    implicit val columnToSeqApibuilderSpecAttribute: Column[Seq[_root_.io.apibuilder.spec.v0.models.Attribute]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Attribute]] }
    implicit val columnToSeqApibuilderSpecBody: Column[Seq[_root_.io.apibuilder.spec.v0.models.Body]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Body]] }
    implicit val columnToSeqApibuilderSpecContact: Column[Seq[_root_.io.apibuilder.spec.v0.models.Contact]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Contact]] }
    implicit val columnToSeqApibuilderSpecDeprecation: Column[Seq[_root_.io.apibuilder.spec.v0.models.Deprecation]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Deprecation]] }
    implicit val columnToSeqApibuilderSpecEnum: Column[Seq[_root_.io.apibuilder.spec.v0.models.Enum]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Enum]] }
    implicit val columnToSeqApibuilderSpecEnumValue: Column[Seq[_root_.io.apibuilder.spec.v0.models.EnumValue]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.EnumValue]] }
    implicit val columnToSeqApibuilderSpecField: Column[Seq[_root_.io.apibuilder.spec.v0.models.Field]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Field]] }
    implicit val columnToSeqApibuilderSpecHeader: Column[Seq[_root_.io.apibuilder.spec.v0.models.Header]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Header]] }
    implicit val columnToSeqApibuilderSpecImport: Column[Seq[_root_.io.apibuilder.spec.v0.models.Import]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Import]] }
    implicit val columnToSeqApibuilderSpecInfo: Column[Seq[_root_.io.apibuilder.spec.v0.models.Info]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Info]] }
    implicit val columnToSeqApibuilderSpecInterface: Column[Seq[_root_.io.apibuilder.spec.v0.models.Interface]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Interface]] }
    implicit val columnToSeqApibuilderSpecLicense: Column[Seq[_root_.io.apibuilder.spec.v0.models.License]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.License]] }
    implicit val columnToSeqApibuilderSpecModel: Column[Seq[_root_.io.apibuilder.spec.v0.models.Model]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Model]] }
    implicit val columnToSeqApibuilderSpecOperation: Column[Seq[_root_.io.apibuilder.spec.v0.models.Operation]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Operation]] }
    implicit val columnToSeqApibuilderSpecOrganization: Column[Seq[_root_.io.apibuilder.spec.v0.models.Organization]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Organization]] }
    implicit val columnToSeqApibuilderSpecParameter: Column[Seq[_root_.io.apibuilder.spec.v0.models.Parameter]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Parameter]] }
    implicit val columnToSeqApibuilderSpecResource: Column[Seq[_root_.io.apibuilder.spec.v0.models.Resource]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Resource]] }
    implicit val columnToSeqApibuilderSpecResponse: Column[Seq[_root_.io.apibuilder.spec.v0.models.Response]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Response]] }
    implicit val columnToSeqApibuilderSpecService: Column[Seq[_root_.io.apibuilder.spec.v0.models.Service]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Service]] }
    implicit val columnToSeqApibuilderSpecUnion: Column[Seq[_root_.io.apibuilder.spec.v0.models.Union]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Union]] }
    implicit val columnToSeqApibuilderSpecUnionType: Column[Seq[_root_.io.apibuilder.spec.v0.models.UnionType]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.UnionType]] }
    implicit val columnToSeqApibuilderSpecResponseCode: Column[Seq[_root_.io.apibuilder.spec.v0.models.ResponseCode]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.ResponseCode]] }
  }

  object Standard {
    implicit val columnToJsObject: Column[play.api.libs.json.JsObject] = Util.parser { _.as[play.api.libs.json.JsObject] }
    implicit val columnToJsValue: Column[play.api.libs.json.JsValue] = Util.parser { _.as[play.api.libs.json.JsValue] }
    implicit val columnToSeqBoolean: Column[Seq[Boolean]] = Util.parser { _.as[Seq[Boolean]] }
    implicit val columnToSeqDouble: Column[Seq[Double]] = Util.parser { _.as[Seq[Double]] }
    implicit val columnToSeqInt: Column[Seq[Int]] = Util.parser { _.as[Seq[Int]] }
    implicit val columnToSeqLong: Column[Seq[Long]] = Util.parser { _.as[Seq[Long]] }
    implicit val columnToSeqLocalDate: Column[Seq[_root_.org.joda.time.LocalDate]] = Util.parser { _.as[Seq[_root_.org.joda.time.LocalDate]] }
    implicit val columnToSeqDateTime: Column[Seq[_root_.org.joda.time.DateTime]] = Util.parser { _.as[Seq[_root_.org.joda.time.DateTime]] }
    implicit val columnToSeqBigDecimal: Column[Seq[BigDecimal]] = Util.parser { _.as[Seq[BigDecimal]] }
    implicit val columnToSeqJsObject: Column[Seq[_root_.play.api.libs.json.JsObject]] = Util.parser { _.as[Seq[_root_.play.api.libs.json.JsObject]] }
    implicit val columnToSeqJsValue: Column[Seq[_root_.play.api.libs.json.JsValue]] = Util.parser { _.as[Seq[_root_.play.api.libs.json.JsValue]] }
    implicit val columnToSeqString: Column[Seq[String]] = Util.parser { _.as[Seq[String]] }
    implicit val columnToSeqUUID: Column[Seq[_root_.java.util.UUID]] = Util.parser { _.as[Seq[_root_.java.util.UUID]] }
  }

}