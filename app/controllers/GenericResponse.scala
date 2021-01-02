package controllers

import play.api.libs.json.Json

case class GenericResponse(message: String) {}

object GenericResponse {
  implicit val writes = Json.writes[GenericResponse]
}
