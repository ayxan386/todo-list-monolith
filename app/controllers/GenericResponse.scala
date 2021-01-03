package controllers

import play.api.libs.json.{JsValue, Json, Writes}

case class GenericResponse[T](message: String, data: T) {}

object GenericResponse {

  implicit def writes[T](implicit writes: Writes[T]) =
    new Writes[GenericResponse[T]] {
      override def writes(data: GenericResponse[T]): JsValue =
        Json.obj("message" -> data.message, "data" -> Json.toJson[T](data.data))
    }
}
