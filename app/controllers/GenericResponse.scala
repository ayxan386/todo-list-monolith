package controllers

import play.api.libs.json.{JsObject, JsString, Json, Writes}

case class GenericResponse[T](message: String, data: T) {}

object GenericResponse {

  implicit def writes[T](implicit fmt: Writes[T]): Writes[GenericResponse[T]] =
    new Writes[GenericResponse[T]] {
      def writes(ts: GenericResponse[T]) =
        JsObject(
          Seq(
            "data" -> Json.toJson(ts.data),
            "message" -> JsString(ts.message)
          ))
    }
}
