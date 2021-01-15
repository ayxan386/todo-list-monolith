package errors.dto.notfound

import errors.dto.AbstractHttpError

case class ItemNotFoundError(id: String)
  extends AbstractHttpError(message = s"Item with $id not found",
    status = 404)
