package errors.dto.unauthorizthed

import errors.dto.AbstractHttpError
import io.jsonwebtoken.JwtException

case class InvalidTokenSignatureError(ex: JwtException)
    extends AbstractHttpError(
      message = s"Provided token signature is invalid: ${ex.getMessage}",
      status = 401) {}
