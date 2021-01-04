package models

import play.api.libs.json.Json

import java.time.LocalDateTime
import java.util.UUID

case class Item(id: UUID,
                title: String,
                content: Option[String],
                itemListID: UUID,
                createDate: Option[LocalDateTime],
                updateDate: Option[LocalDateTime])

object Item {
  implicit val writes = Json.writes[Item]
}
