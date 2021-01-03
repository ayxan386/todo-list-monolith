package models

import play.api.libs.json.Json

import java.time.LocalDateTime
import java.util.UUID

case class ItemList(
    id: UUID,
    name: String,
    userName: String,
    createDate: Option[LocalDateTime],
    updateTime: Option[LocalDateTime]
)

object ItemList {
  implicit val writes = Json.writes[ItemList]
}
