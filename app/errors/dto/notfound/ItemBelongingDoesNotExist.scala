package errors.dto.notfound

import errors.dto.AbstractHttpError

case class ItemBelongingDoesNotExist(nickname: String, itemId: String)
  extends AbstractHttpError(
    status = 404,
    message =
      s"List item with id <${itemId}> belonging to <${nickname}> does not exist") {}
