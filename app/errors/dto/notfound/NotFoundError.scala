package errors.dto.notfound

import errors.dto.AbstractHttpError

case class NotFoundError(override val message: String)
  extends AbstractHttpError(message = message, status = 404) {}
