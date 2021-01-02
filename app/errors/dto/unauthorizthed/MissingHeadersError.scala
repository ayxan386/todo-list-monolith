package errors.dto.unauthorizthed

import errors.dto.AbstractHttpError

case class MissingHeadersError()
  extends AbstractHttpError(message = "Required headers are missing",
    status = 401) {}
