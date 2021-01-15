package dtos.itemlist

import play.api.libs.json.Json

import java.util.UUID

case class UpdateItemRequest(id: UUID,
                             status: Option[String],
                             content: Option[String])

object UpdateItemRequest {
  implicit val reads = Json.reads[UpdateItemRequest]
}
