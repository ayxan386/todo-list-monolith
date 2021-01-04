package models

import play.api.libs.json.Json

import java.time.LocalDateTime
import java.util.UUID

case class ItemList(
    id: UUID,
    name: String,
    username: String,
    createDate: Option[LocalDateTime],
    updateDate: Option[LocalDateTime]
) {
  override def hashCode(): Int = id.hashCode()
}

object ItemList {
  implicit val writes = Json.writes[ItemList]
}
