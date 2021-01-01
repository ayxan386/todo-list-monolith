package errors.dto

import play.api.libs.json.Json

import java.time.LocalDateTime

case class ErrorResponseDTO(message: String, time: LocalDateTime, status: Int)

object ErrorResponseDTO {
  implicit val writes = Json.writes[ErrorResponseDTO]
}
