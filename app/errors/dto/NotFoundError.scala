package errors.dto

case class NotFoundError(override val message: String)
  extends AbstractHttpError(message = message, status = 404) {}
