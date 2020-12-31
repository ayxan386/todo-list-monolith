package controllers.auth.dto

import play.api.libs.json.Json

case class TokenResponseDTO(token: String) {

}

object TokenResponseDTO {
  implicit val writes = Json.writes[TokenResponseDTO]
}
