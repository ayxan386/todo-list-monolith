package errors.dto.badrequest

import errors.dto.AbstractHttpError

case class BodyParsingException()
  extends AbstractHttpError(message = "Body couldn't be parsed to JSON",
    status = 400) {}
