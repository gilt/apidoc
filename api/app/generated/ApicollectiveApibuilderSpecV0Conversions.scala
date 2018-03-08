/**
 * Generated by API Builder - https://www.apibuilder.io
 * Service version: 0.14.9-5-gc82bf05b
 * apibuilder 0.14.3 app.apibuilder.io/apicollective/apibuilder-spec/0.14.9-5-gc82bf05b/anorm_2_6_parsers
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

    private[this] def parseJson[T](f: play.api.libs.json.JsValue => T, columnName: String, value: String) = {
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
    implicit val columnToMapApibuilderSpecMethod: Column[Map[String, _root_.io.apibuilder.spec.v0.models.Method]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.Method]] }
    implicit val columnToSeqApibuilderSpecParameterLocation: Column[Seq[_root_.io.apibuilder.spec.v0.models.ParameterLocation]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.ParameterLocation]] }
    implicit val columnToMapApibuilderSpecParameterLocation: Column[Map[String, _root_.io.apibuilder.spec.v0.models.ParameterLocation]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.ParameterLocation]] }
    implicit val columnToSeqApibuilderSpecResponseCodeOption: Column[Seq[_root_.io.apibuilder.spec.v0.models.ResponseCodeOption]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.ResponseCodeOption]] }
    implicit val columnToMapApibuilderSpecResponseCodeOption: Column[Map[String, _root_.io.apibuilder.spec.v0.models.ResponseCodeOption]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.ResponseCodeOption]] }
    implicit val columnToSeqApibuilderSpecAnnotation: Column[Seq[_root_.io.apibuilder.spec.v0.models.Annotation]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Annotation]] }
    implicit val columnToMapApibuilderSpecAnnotation: Column[Map[String, _root_.io.apibuilder.spec.v0.models.Annotation]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.Annotation]] }
    implicit val columnToSeqApibuilderSpecApidoc: Column[Seq[_root_.io.apibuilder.spec.v0.models.Apidoc]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Apidoc]] }
    implicit val columnToMapApibuilderSpecApidoc: Column[Map[String, _root_.io.apibuilder.spec.v0.models.Apidoc]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.Apidoc]] }
    implicit val columnToSeqApibuilderSpecApplication: Column[Seq[_root_.io.apibuilder.spec.v0.models.Application]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Application]] }
    implicit val columnToMapApibuilderSpecApplication: Column[Map[String, _root_.io.apibuilder.spec.v0.models.Application]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.Application]] }
    implicit val columnToSeqApibuilderSpecAttribute: Column[Seq[_root_.io.apibuilder.spec.v0.models.Attribute]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Attribute]] }
    implicit val columnToMapApibuilderSpecAttribute: Column[Map[String, _root_.io.apibuilder.spec.v0.models.Attribute]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.Attribute]] }
    implicit val columnToSeqApibuilderSpecBody: Column[Seq[_root_.io.apibuilder.spec.v0.models.Body]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Body]] }
    implicit val columnToMapApibuilderSpecBody: Column[Map[String, _root_.io.apibuilder.spec.v0.models.Body]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.Body]] }
    implicit val columnToSeqApibuilderSpecContact: Column[Seq[_root_.io.apibuilder.spec.v0.models.Contact]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Contact]] }
    implicit val columnToMapApibuilderSpecContact: Column[Map[String, _root_.io.apibuilder.spec.v0.models.Contact]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.Contact]] }
    implicit val columnToSeqApibuilderSpecDeprecation: Column[Seq[_root_.io.apibuilder.spec.v0.models.Deprecation]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Deprecation]] }
    implicit val columnToMapApibuilderSpecDeprecation: Column[Map[String, _root_.io.apibuilder.spec.v0.models.Deprecation]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.Deprecation]] }
    implicit val columnToSeqApibuilderSpecEnum: Column[Seq[_root_.io.apibuilder.spec.v0.models.Enum]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Enum]] }
    implicit val columnToMapApibuilderSpecEnum: Column[Map[String, _root_.io.apibuilder.spec.v0.models.Enum]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.Enum]] }
    implicit val columnToSeqApibuilderSpecEnumValue: Column[Seq[_root_.io.apibuilder.spec.v0.models.EnumValue]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.EnumValue]] }
    implicit val columnToMapApibuilderSpecEnumValue: Column[Map[String, _root_.io.apibuilder.spec.v0.models.EnumValue]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.EnumValue]] }
    implicit val columnToSeqApibuilderSpecField: Column[Seq[_root_.io.apibuilder.spec.v0.models.Field]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Field]] }
    implicit val columnToMapApibuilderSpecField: Column[Map[String, _root_.io.apibuilder.spec.v0.models.Field]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.Field]] }
    implicit val columnToSeqApibuilderSpecHeader: Column[Seq[_root_.io.apibuilder.spec.v0.models.Header]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Header]] }
    implicit val columnToMapApibuilderSpecHeader: Column[Map[String, _root_.io.apibuilder.spec.v0.models.Header]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.Header]] }
    implicit val columnToSeqApibuilderSpecImport: Column[Seq[_root_.io.apibuilder.spec.v0.models.Import]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Import]] }
    implicit val columnToMapApibuilderSpecImport: Column[Map[String, _root_.io.apibuilder.spec.v0.models.Import]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.Import]] }
    implicit val columnToSeqApibuilderSpecInfo: Column[Seq[_root_.io.apibuilder.spec.v0.models.Info]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Info]] }
    implicit val columnToMapApibuilderSpecInfo: Column[Map[String, _root_.io.apibuilder.spec.v0.models.Info]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.Info]] }
    implicit val columnToSeqApibuilderSpecLicense: Column[Seq[_root_.io.apibuilder.spec.v0.models.License]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.License]] }
    implicit val columnToMapApibuilderSpecLicense: Column[Map[String, _root_.io.apibuilder.spec.v0.models.License]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.License]] }
    implicit val columnToSeqApibuilderSpecModel: Column[Seq[_root_.io.apibuilder.spec.v0.models.Model]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Model]] }
    implicit val columnToMapApibuilderSpecModel: Column[Map[String, _root_.io.apibuilder.spec.v0.models.Model]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.Model]] }
    implicit val columnToSeqApibuilderSpecOperation: Column[Seq[_root_.io.apibuilder.spec.v0.models.Operation]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Operation]] }
    implicit val columnToMapApibuilderSpecOperation: Column[Map[String, _root_.io.apibuilder.spec.v0.models.Operation]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.Operation]] }
    implicit val columnToSeqApibuilderSpecOrganization: Column[Seq[_root_.io.apibuilder.spec.v0.models.Organization]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Organization]] }
    implicit val columnToMapApibuilderSpecOrganization: Column[Map[String, _root_.io.apibuilder.spec.v0.models.Organization]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.Organization]] }
    implicit val columnToSeqApibuilderSpecParameter: Column[Seq[_root_.io.apibuilder.spec.v0.models.Parameter]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Parameter]] }
    implicit val columnToMapApibuilderSpecParameter: Column[Map[String, _root_.io.apibuilder.spec.v0.models.Parameter]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.Parameter]] }
    implicit val columnToSeqApibuilderSpecResource: Column[Seq[_root_.io.apibuilder.spec.v0.models.Resource]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Resource]] }
    implicit val columnToMapApibuilderSpecResource: Column[Map[String, _root_.io.apibuilder.spec.v0.models.Resource]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.Resource]] }
    implicit val columnToSeqApibuilderSpecResponse: Column[Seq[_root_.io.apibuilder.spec.v0.models.Response]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Response]] }
    implicit val columnToMapApibuilderSpecResponse: Column[Map[String, _root_.io.apibuilder.spec.v0.models.Response]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.Response]] }
    implicit val columnToSeqApibuilderSpecService: Column[Seq[_root_.io.apibuilder.spec.v0.models.Service]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Service]] }
    implicit val columnToMapApibuilderSpecService: Column[Map[String, _root_.io.apibuilder.spec.v0.models.Service]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.Service]] }
    implicit val columnToSeqApibuilderSpecUnion: Column[Seq[_root_.io.apibuilder.spec.v0.models.Union]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.Union]] }
    implicit val columnToMapApibuilderSpecUnion: Column[Map[String, _root_.io.apibuilder.spec.v0.models.Union]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.Union]] }
    implicit val columnToSeqApibuilderSpecUnionType: Column[Seq[_root_.io.apibuilder.spec.v0.models.UnionType]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.UnionType]] }
    implicit val columnToMapApibuilderSpecUnionType: Column[Map[String, _root_.io.apibuilder.spec.v0.models.UnionType]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.UnionType]] }
    implicit val columnToSeqApibuilderSpecResponseCode: Column[Seq[_root_.io.apibuilder.spec.v0.models.ResponseCode]] = Util.parser { _.as[Seq[_root_.io.apibuilder.spec.v0.models.ResponseCode]] }
    implicit val columnToMapApibuilderSpecResponseCode: Column[Map[String, _root_.io.apibuilder.spec.v0.models.ResponseCode]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.spec.v0.models.ResponseCode]] }
  }

  object Standard {
    implicit val columnToJsObject: Column[play.api.libs.json.JsObject] = Util.parser { _.as[play.api.libs.json.JsObject] }
    implicit val columnToSeqBoolean: Column[Seq[Boolean]] = Util.parser { _.as[Seq[Boolean]] }
    implicit val columnToMapBoolean: Column[Map[String, Boolean]] = Util.parser { _.as[Map[String, Boolean]] }
    implicit val columnToSeqDouble: Column[Seq[Double]] = Util.parser { _.as[Seq[Double]] }
    implicit val columnToMapDouble: Column[Map[String, Double]] = Util.parser { _.as[Map[String, Double]] }
    implicit val columnToSeqInt: Column[Seq[Int]] = Util.parser { _.as[Seq[Int]] }
    implicit val columnToMapInt: Column[Map[String, Int]] = Util.parser { _.as[Map[String, Int]] }
    implicit val columnToSeqLong: Column[Seq[Long]] = Util.parser { _.as[Seq[Long]] }
    implicit val columnToMapLong: Column[Map[String, Long]] = Util.parser { _.as[Map[String, Long]] }
    implicit val columnToSeqLocalDate: Column[Seq[_root_.org.joda.time.LocalDate]] = Util.parser { _.as[Seq[_root_.org.joda.time.LocalDate]] }
    implicit val columnToMapLocalDate: Column[Map[String, _root_.org.joda.time.LocalDate]] = Util.parser { _.as[Map[String, _root_.org.joda.time.LocalDate]] }
    implicit val columnToSeqDateTime: Column[Seq[_root_.org.joda.time.DateTime]] = Util.parser { _.as[Seq[_root_.org.joda.time.DateTime]] }
    implicit val columnToMapDateTime: Column[Map[String, _root_.org.joda.time.DateTime]] = Util.parser { _.as[Map[String, _root_.org.joda.time.DateTime]] }
    implicit val columnToSeqBigDecimal: Column[Seq[BigDecimal]] = Util.parser { _.as[Seq[BigDecimal]] }
    implicit val columnToMapBigDecimal: Column[Map[String, BigDecimal]] = Util.parser { _.as[Map[String, BigDecimal]] }
    implicit val columnToSeqJsObject: Column[Seq[_root_.play.api.libs.json.JsObject]] = Util.parser { _.as[Seq[_root_.play.api.libs.json.JsObject]] }
    implicit val columnToMapJsObject: Column[Map[String, _root_.play.api.libs.json.JsObject]] = Util.parser { _.as[Map[String, _root_.play.api.libs.json.JsObject]] }
    implicit val columnToSeqJsValue: Column[Seq[_root_.play.api.libs.json.JsValue]] = Util.parser { _.as[Seq[_root_.play.api.libs.json.JsValue]] }
    implicit val columnToMapJsValue: Column[Map[String, _root_.play.api.libs.json.JsValue]] = Util.parser { _.as[Map[String, _root_.play.api.libs.json.JsValue]] }
    implicit val columnToSeqString: Column[Seq[String]] = Util.parser { _.as[Seq[String]] }
    implicit val columnToMapString: Column[Map[String, String]] = Util.parser { _.as[Map[String, String]] }
    implicit val columnToSeqUUID: Column[Seq[_root_.java.util.UUID]] = Util.parser { _.as[Seq[_root_.java.util.UUID]] }
    implicit val columnToMapUUID: Column[Map[String, _root_.java.util.UUID]] = Util.parser { _.as[Map[String, _root_.java.util.UUID]] }
  }

}