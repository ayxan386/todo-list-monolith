package errors.dto.unauthorizthed

import errors.dto.AbstractHttpError

case class InvalidTokenSignatureError()
  extends AbstractHttpError(message = "Provided token signature is invalid",
    status = 401) {}
