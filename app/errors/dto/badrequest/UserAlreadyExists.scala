package errors.dto.badrequest

import errors.dto.AbstractHttpError

case class UserAlreadyExists()
  extends AbstractHttpError(
    message = "User with that nickname already exists",
    status = 400) {}
