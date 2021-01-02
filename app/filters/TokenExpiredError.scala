package filters

import errors.dto.AbstractHttpError

case class TokenExpiredError()
    extends AbstractHttpError(message = "Token has expired", status = 401) {}
