/**
 * Generated by API Builder - https://www.apibuilder.io
 * Service version: 0.14.78
 * apibuilder 0.14.75 app.apibuilder.io/apicollective/apibuilder-common/0.14.78/anorm_2_6_parsers
 */
package io.apibuilder.common.v0.anorm.conversions {

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
    import io.apibuilder.common.v0.models.json._
    implicit val columnToSeqApibuilderCommonAudit: Column[Seq[_root_.io.apibuilder.common.v0.models.Audit]] = Util.parser { _.as[Seq[_root_.io.apibuilder.common.v0.models.Audit]] }
    implicit val columnToMapApibuilderCommonAudit: Column[Map[String, _root_.io.apibuilder.common.v0.models.Audit]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.common.v0.models.Audit]] }
    implicit val columnToSeqApibuilderCommonHealthcheck: Column[Seq[_root_.io.apibuilder.common.v0.models.Healthcheck]] = Util.parser { _.as[Seq[_root_.io.apibuilder.common.v0.models.Healthcheck]] }
    implicit val columnToMapApibuilderCommonHealthcheck: Column[Map[String, _root_.io.apibuilder.common.v0.models.Healthcheck]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.common.v0.models.Healthcheck]] }
    implicit val columnToSeqApibuilderCommonReference: Column[Seq[_root_.io.apibuilder.common.v0.models.Reference]] = Util.parser { _.as[Seq[_root_.io.apibuilder.common.v0.models.Reference]] }
    implicit val columnToMapApibuilderCommonReference: Column[Map[String, _root_.io.apibuilder.common.v0.models.Reference]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.common.v0.models.Reference]] }
    implicit val columnToSeqApibuilderCommonReferenceGuid: Column[Seq[_root_.io.apibuilder.common.v0.models.ReferenceGuid]] = Util.parser { _.as[Seq[_root_.io.apibuilder.common.v0.models.ReferenceGuid]] }
    implicit val columnToMapApibuilderCommonReferenceGuid: Column[Map[String, _root_.io.apibuilder.common.v0.models.ReferenceGuid]] = Util.parser { _.as[Map[String, _root_.io.apibuilder.common.v0.models.ReferenceGuid]] }
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