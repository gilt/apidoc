/**
 * Generated by API Builder - https://www.apibuilder.io
 * Service version: 0.16.50
 * User agent: apibuilder app.apibuilder.io/apicollective/apibuilder-generator/latest/anorm_2_8_parsers
 */
package io.apibuilder.generator.v0.anorm.conversions {

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
    import io.apibuilder.generator.v0.models.json._
    implicit val columnToSeqApibuilderGeneratorFileFlag: Column[Seq[_root_.io.apibuilder.generator.v0.models.FileFlag]] = Util.parser { _.as[Seq[_root_.io.apibuilder.generator.v0.models.FileFlag]] }
    implicit val columnToMapApibuilderGeneratorFileFlag: Column[Map[String, _root_.io.apibuilder.generator.v0.models.FileFlag]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.generator.v0.models.FileFlag]] }
    implicit val columnToSeqApibuilderGeneratorAttribute: Column[Seq[_root_.io.apibuilder.generator.v0.models.Attribute]] = Util.parser { _.as[Seq[_root_.io.apibuilder.generator.v0.models.Attribute]] }
    implicit val columnToMapApibuilderGeneratorAttribute: Column[Map[String, _root_.io.apibuilder.generator.v0.models.Attribute]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.generator.v0.models.Attribute]] }
    implicit val columnToSeqApibuilderGeneratorError: Column[Seq[_root_.io.apibuilder.generator.v0.models.Error]] = Util.parser { _.as[Seq[_root_.io.apibuilder.generator.v0.models.Error]] }
    implicit val columnToMapApibuilderGeneratorError: Column[Map[String, _root_.io.apibuilder.generator.v0.models.Error]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.generator.v0.models.Error]] }
    implicit val columnToSeqApibuilderGeneratorFile: Column[Seq[_root_.io.apibuilder.generator.v0.models.File]] = Util.parser { _.as[Seq[_root_.io.apibuilder.generator.v0.models.File]] }
    implicit val columnToMapApibuilderGeneratorFile: Column[Map[String, _root_.io.apibuilder.generator.v0.models.File]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.generator.v0.models.File]] }
    implicit val columnToSeqApibuilderGeneratorGenerator: Column[Seq[_root_.io.apibuilder.generator.v0.models.Generator]] = Util.parser { _.as[Seq[_root_.io.apibuilder.generator.v0.models.Generator]] }
    implicit val columnToMapApibuilderGeneratorGenerator: Column[Map[String, _root_.io.apibuilder.generator.v0.models.Generator]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.generator.v0.models.Generator]] }
    implicit val columnToSeqApibuilderGeneratorHealthcheck: Column[Seq[_root_.io.apibuilder.generator.v0.models.Healthcheck]] = Util.parser { _.as[Seq[_root_.io.apibuilder.generator.v0.models.Healthcheck]] }
    implicit val columnToMapApibuilderGeneratorHealthcheck: Column[Map[String, _root_.io.apibuilder.generator.v0.models.Healthcheck]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.generator.v0.models.Healthcheck]] }
    implicit val columnToSeqApibuilderGeneratorInvocation: Column[Seq[_root_.io.apibuilder.generator.v0.models.Invocation]] = Util.parser { _.as[Seq[_root_.io.apibuilder.generator.v0.models.Invocation]] }
    implicit val columnToMapApibuilderGeneratorInvocation: Column[Map[String, _root_.io.apibuilder.generator.v0.models.Invocation]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.generator.v0.models.Invocation]] }
    implicit val columnToSeqApibuilderGeneratorInvocationForm: Column[Seq[_root_.io.apibuilder.generator.v0.models.InvocationForm]] = Util.parser { _.as[Seq[_root_.io.apibuilder.generator.v0.models.InvocationForm]] }
    implicit val columnToMapApibuilderGeneratorInvocationForm: Column[Map[String, _root_.io.apibuilder.generator.v0.models.InvocationForm]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.generator.v0.models.InvocationForm]] }
  }

  object Standard {
    implicit val columnToJsObject: Column[play.api.libs.json.JsObject] = Util.parser { _.as[play.api.libs.json.JsObject] }
    implicit val columnToJsValue: Column[play.api.libs.json.JsValue] = Util.parser { _.as[play.api.libs.json.JsValue] }
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