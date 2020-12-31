package controllers.auth.dto

import play.api.libs.json.{Json, Reads}

case class LoginRequestDTO(nickname: String, password: String) {

}

object LoginRequestDTO {
  implicit val reads: Reads[LoginRequestDTO] = Json.reads[LoginRequestDTO]
}
