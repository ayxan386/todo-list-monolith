package dtos.itemlist

import models.Item
import play.api.libs.json.Json

import java.time.LocalDateTime
import java.util.UUID

case class ItemListResponseDTO(
    id: UUID,
    name: String,
    username: String,
    createDate: Option[LocalDateTime],
    updateDate: Option[LocalDateTime],
    items: List[Item]
)

object ItemListResponseDTO {
  implicit val writes = Json.writes[ItemListResponseDTO]
}
