package errors.dto.badrequest

import errors.dto.AbstractHttpError

case class PasswordsMatchingError()
  extends AbstractHttpError(message = "Provided passwords don't match",
    status = 400) {}
