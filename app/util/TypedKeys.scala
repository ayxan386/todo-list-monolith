package util

import play.api.libs.typedmap.TypedKey

object TypedKeys {

  val tokenType: TypedKey[String] = TypedKey[String]("nickname")

}
