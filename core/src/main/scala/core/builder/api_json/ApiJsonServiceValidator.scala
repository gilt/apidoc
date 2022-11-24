package builder.api_json

import builder.JsonUtil
import builder.api_json.templates.JsMerge
import cats.implicits._
import cats.data.ValidatedNec
import cats.data.Validated.{Invalid, Valid}
import core.{DuplicateJsonParser, Importer, ServiceFetcher, Util, VersionMigration}
import lib.{ServiceConfiguration, ServiceValidator, UrlKey, ValidatedHelpers}
import io.apibuilder.spec.v0.models.Service
import play.api.libs.json.{JsObject, Json}
import com.fasterxml.jackson.core.{JsonParseException, JsonProcessingException}

import scala.util.{Failure, Success, Try}

case class ApiJsonServiceValidator(
  config: ServiceConfiguration,
  apiJson: String,
  fetcher: ServiceFetcher,
  migration: VersionMigration
) extends ServiceValidator[Service] with ValidatedHelpers {

  override def validate(): ValidatedNec[String, Service] = {
    internalService match {
      case None => {
        if (apiJson.trim == "") {
          "No Data".invalidNec
        } else {
          parseError.getOrElse(Nil).toList match {
            case Nil => "Invalid JSON".invalidNec
            case errs => errs.mkString(", ").invalidNec
          }
        }
      }

      case Some(form: InternalServiceForm) => {
        validateStructure().andThen { _ =>
          // TODO: Pass in arguments to all validate methods
          sequenceUnique(Seq(
            validateInfo(form.info),
            validateKey(),
            validateImports(),
            validateAttributes("Service", form.attributes),
            validateHeaders(),
            validateResources(form.resources),
            validateInterfaces(),
            validateUnions(),
            validateModels(form.models),
            validateEnums(),
            validateAnnotations(),
            DuplicateJsonParser.validateDuplicates(apiJson)
          ))
        }.map { _ =>
          ServiceBuilder(migration = migration).apply(config, form)
        }
      }
    }
  }

  private var parseError: Option[Seq[String]] = None

  lazy val serviceForm: Option[JsObject] = {
    Try(Json.parse(apiJson)) match {
      case Success(v) => {
        v.asOpt[JsObject] match {
          case Some(o) => {
            JsMerge.merge(o) match {
              case Invalid(errors) => {
                parseError = Some(errors.toNonEmptyList.toList)
                None
              }
              case Valid(js) => Some(js)
            }
          }
          case None => {
            parseError = Some(Seq("Must upload a Json Object"))
            None
          }
        }
      }
      case Failure(ex) => ex match {
        case e: JsonParseException => {
          parseError = Some(Seq(e.getMessage))
          None
        }
        case e: JsonProcessingException => {
          parseError = Some(Seq(e.getMessage))
          None
        }
      }
    }
  }

  // TODO: Remove the variables internalService and errors
  private lazy val internalService: Option[InternalServiceForm] = serviceForm.map(InternalServiceForm(_, fetcher))

  private def validateInfo(info: Option[InternalInfoForm]): ValidatedNec[String, Unit] = {
    info match {
      case None => ().validNec
      case Some(info) => {
        info.license match {
          case None => ().validNec
          case Some(l) => {
            l.name match {
              case None => "License must have a name".invalidNec
              case Some(_) => ().validNec
            }
          }
        }
      }
    }
  }

  private def validateKey(): ValidatedNec[String, Unit] = {
    internalService.get.key match {
      case None => ().validNec
      case Some(key) => {
        val generated = UrlKey.generate(key)
        if (generated == key) {
          ().validNec
        } else {
          s"Invalid url key. A valid key would be $generated".invalidNec
        }
      }
    }
  }

  private def validateStructure(): ValidatedNec[String, Unit] = {
    JsonUtil.validate(
      internalService.get.json,
      strings = Seq("name"),
      optionalStrings = Seq("base_url", "description", "namespace", "$schema"),
      optionalArraysOfObjects = Seq("imports", "headers", "attributes"),
      optionalObjects = Seq("info", "enums", "interfaces", "models", "unions", "resources", "annotations", "templates")
    )
  }

  private def validateImports(): ValidatedNec[String, Unit] = {
    sequenceUnique(
      internalService.get.imports.map(_.warnings) ++
      internalService.get.imports.flatMap { imp =>
        imp.uri match {
          case None => Seq(().validNec)
          case Some(uri) => {
            Util.validateUri(uri) match {
              case Nil => Seq(Importer(fetcher, uri).validate)  // TODO. need to cache somewhere to avoid a second lookup when parsing later
              case errs => errs.map(_.invalidNec)
            }
          }
        }
      }
    )
  }

  private def validateUnions(): ValidatedNec[String, Unit] = {
    sequenceUnique(
      internalService.get.unions.map(_.warnings) ++ internalService.get.unions.map { union =>
        validateAttributes(s"Union[${union.name}]", union.attributes)
      } ++ internalService.get.unions.map(validateUnionTypes)
    )
  }

  private def validateUnionTypes(union: InternalUnionForm): ValidatedNec[String, Unit] = {
    sequenceUnique(
      union.types.map(_.warnings) ++ union.types.map { typ =>
        typ.datatype match {
          case Invalid(errors) => (s"Union[${union.name}] type[] " + errors.toNonEmptyList.toList.mkString(", ")).invalidNec
          case Valid(dt) => validateAttributes(s"Union[${union.name}] type[${dt.name}]", typ.attributes)
        }
      }
    )
  }

  private def validateAnnotations(): ValidatedNec[String, Unit] = {
    val annotations = internalService.get.annotations
    sequenceUnique(
      annotations.map(_.warnings) ++ annotations.filter(_.name.isEmpty).map(_ =>
        "Annotations must have a name".invalidNec
      )
    )
  }

  private def validateEnums(): ValidatedNec[String, Unit] = {
    val enums = internalService.get.enums
    sequenceUnique(
      enums.map(_.warnings) ++ enums.map { enum =>
        validateAttributes(s"Enum[${enum.name}]", enum.attributes)
      } ++ enums.map(validateEnumValues)
    )
  }

  private def validateEnumValues(`enum`: InternalEnumForm): ValidatedNec[String, Unit] = {
    sequenceUnique(
      `enum`.values.map(_.warnings) ++ `enum`.values.zipWithIndex.map { case (value, i) =>
        value.name match {
          case None => s"Enum[${`enum`.name}] value[$i]: Missing name".invalidNec
          case Some(name) => validateAttributes(s"Enum[${`enum`.name}] value[$name]", value.attributes)
        }
      }
    )
  }

  private def validateInterfaces(): ValidatedNec[String, Unit] = {
    val interfaces = internalService.get.interfaces
    sequenceUnique(
      interfaces.map(_.warnings) ++ interfaces.map { interface =>
        validateAttributes(s"Interface[${interface.name}]", interface.attributes)
      }
    )
  }

  private def validateModels(models: Seq[InternalModelForm]): ValidatedNec[String, Unit] = {
    sequenceUnique(
      models.map(_.warnings) ++ models.map { model =>
        validateAttributes(s"Model[${model.name}]", model.attributes)
      } ++ Seq(validateFields(models))
    )
  }

  private def validateHeaders(): ValidatedNec[String, Unit] = {
    val headers = internalService.get.headers
    sequenceUnique(
      headers.map(_.warnings) ++ headers.filter(_.name.isDefined).map { header =>
        validateAttributes(s"Header[${header.name}]", header.attributes)
      }
    )
  }

  private def validateFields(models: Seq[InternalModelForm]): ValidatedNec[String, Unit] = {
    val missingNames = models.flatMap { model =>
      model.fields.filter(_.name.isEmpty).zipWithIndex.map { case (_, i) =>
        modelLabel(model, s"field[$i] must have a name").invalidNec
      }
    }

    val missingTypes = models.flatMap { model =>
      model.fields.filter(_.name.isDefined).map { f =>
        f.datatype match {
          case Invalid(errors) => {
            modelLabel(model, s"field[${f.name.get}] type ${errors.toNonEmptyList.toList.mkString(", ")}").invalidNec
          }
          case Valid(_) => ().validNec
        }
      }
    }

    val attributeErrors = internalService.get.models.flatMap { model =>
      model.fields.filter(_.name.isDefined).map { f =>
        validateAttributes(s"Model[${model.name}] field[${f.name.get}]", f.attributes)
      }
    }

    val warnings = internalService.get.models.flatMap { model =>
      model.fields.filter(f => f.warnings.isInvalid && f.name.isDefined).map { f =>
        (s"Model[${model.name}] field[${f.name.get}]: " + formatErrors(f.warnings)).invalidNec
      }
    }

    sequenceUnique(missingTypes ++ missingNames ++ attributeErrors ++ warnings)
  }

  private def validateAttributes(prefix: String, attributes: Seq[InternalAttributeForm]): ValidatedNec[String, Unit] = {
    sequenceUnique(
      attributes.zipWithIndex.map { case (attr, i) =>
        val fieldErrors = attr.name match {
          case None => s"$prefix: Attribute $i must have a name".invalidNec
          case Some(name) => {
            attr.value match {
              case None => s"$prefix: Attribute $name must have a value".invalidNec
              case Some(_) => ().validNec
            }
          }
        }

        sequenceUnique(Seq(fieldErrors, addPrefixToError(prefix, attr.warnings)))
      }
    )
  }

  private def validateResponses(resources: Seq[InternalResourceForm]): ValidatedNec[String, Unit] = {
    val codeErrors = resources.flatMap { resource =>
      resource.operations.flatMap { op =>
        op.declaredResponses.filter(r => r.warnings.isInvalid).map { r =>
          opLabel(resource, op, s"${r.code}: " + formatErrors(r.warnings)).invalidNec
        }
      }
    }

    val typeErrors = resources.flatMap { resource =>
      resource.operations.flatMap { op =>
        op.declaredResponses.map { r =>
          r.datatype match {
            case Invalid(errors) => opLabel(resource, op, s"${r.code} type: " + errors.toNonEmptyList.toList.mkString(", ")).invalidNec
            case Valid(_) => ().validNec
          }
        }
      }
    }

    val attributeErrors = internalService.get.resources.map { resource =>
      validateAttributes(s"Resource[${resource.datatype.label}]", resource.attributes)
    }

    sequenceUnique(codeErrors ++ typeErrors ++ attributeErrors)
  }

  private def validateResourceBodies(resources: Seq[InternalResourceForm]): ValidatedNec[String, Unit] = {
    sequenceUnique(
      resources.flatMap { resource =>
        resource.operations.filter(_.body.isDefined).map { op =>
          op.body.get.datatype match {
            case Invalid(errs) => opLabel(resource, op, s"Body ${errs.toNonEmptyList.toList.mkString(", ")}").invalidNec
            case Valid(_) => ().validNec
          }
        }
      }
    )
  }

  private def validateResources(resources: Seq[InternalResourceForm]): ValidatedNec[String, Unit] = {
    val resourceWarnings = sequenceUnique(resources.filter(_.warnings.isInvalid).map { resource =>
      (s"Resource[${resource.datatype.label}]" + formatErrors(resource.warnings)).invalidNec
    })

    sequenceUnique(Seq(
      resourceWarnings,
      validateOperations(resources),
      validateResourceBodies(resources),
      validateParameters(resources),
      validateResponses(resources)
    ))
  }

  private def validateOperations(resources: Seq[InternalResourceForm]): ValidatedNec[String, Unit] = {
    val warnings = resources.flatMap { resource =>
      resource.operations.filter(_.warnings.isInvalid).map { op =>
        opLabel(resource, op, formatErrors(op.warnings)).invalidNec
      }
    }

    val attributeErrors = resources.flatMap { resource =>
      resource.operations.map { op =>
        validateAttributes(opLabel(resource, op, ""), op.attributes)
      }
    }

    val bodyAttributeErrors = resources.flatMap { resource =>
      resource.operations.filter(_.body.isDefined).map { op =>
        validateAttributes(opLabel(resource, op, "body"), op.body.get.attributes)
      }
    }

    sequenceUnique(warnings ++ attributeErrors ++ bodyAttributeErrors)
  }

  private def validateParameters(resources: Seq[InternalResourceForm]): ValidatedNec[String, Unit] = {
    sequenceUnique(
      resources.flatMap { resource =>
        resource.operations.flatMap { op =>
          op.parameters.filter(_.warnings.isInvalid).filter(_.name.nonEmpty).map { p =>
            opLabel(resource, op, s"Parameter[${p.name.get}] ${formatErrors(p.warnings)}").invalidNec
          }
        }
      }
    )
  }

  private def opLabel(
    resource: InternalResourceForm,
    op: InternalOperationForm,
    message: String
  ): String = {
    Seq(
      s"Resource[${resource.datatype.label}]",
      op.method.getOrElse(""),
      Seq(
        resource.path,
        op.path
      ).flatten.mkString(""),
      message
    ).map(_.trim).filter(_.nonEmpty).mkString(" ")
  }

  private def modelLabel(model: InternalModelForm, message: String): String = {
    s"Model[${model.name}] $message"
  }
}
