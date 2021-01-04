package dtos.itemlist

import play.api.libs.json.Json

import java.util.UUID

case class ItemRequestDTO(
    itemListId: UUID,
    title: String,
    content: Option[String]
)

object ItemRequestDTO {
  implicit val reads = Json.reads[ItemRequestDTO]
}
