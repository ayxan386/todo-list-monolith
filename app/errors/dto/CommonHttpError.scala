package errors.dto

case class CommonHttpError(override val message: String,
                           override val status: Int)
  extends AbstractHttpError(message = message, status = status) {}
