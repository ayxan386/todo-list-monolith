package filters

import akka.stream.Materializer
import errors.dto.unauthorizthed.{MissingHeadersError, TokenExpiredError}
import play.api.Logger
import play.api.mvc.{Filter, RequestHeader, Result}
import util.{JwtUtils, TypedKeys}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TokenFilter @Inject()(jwtUtils: JwtUtils)(implicit val mat: Materializer,
                                                ec: ExecutionContext)
    extends Filter {

  private val ignoredPaths = List("login", "register")
  private val ignoredMethods = List("OPTIONS")
  val HEADER_NAME = "Authorization"
  val PREFIX = "Bearer "
  val log = Logger("filters")

  def checkPath(rh: RequestHeader): Option[RequestHeader] =
    Some(rh)
      .filter(r => !ignoredPaths.exists(path => r.path.contains(path)))

  def checkRequestMethod(rh: RequestHeader): Boolean =
    !ignoredMethods.exists(method => method.equalsIgnoreCase(rh.method))

  override def apply(f: RequestHeader => Future[Result])(
      rh: RequestHeader): Future[Result] = {
    if (checkPath(rh).isDefined && checkRequestMethod(rh)) {
      rh.headers
        .get(HEADER_NAME)
        .orElse(throw MissingHeadersError())
        .map(header => header.substring(PREFIX.length))
        .filter(jwtUtils.isValid)
        .orElse(throw TokenExpiredError())
        .map(jwtUtils.getNickname)
        .map(nickname => rh.addAttr(TypedKeys.tokenType, nickname))
        .map(f)
        .get
    } else f(rh)
  }
}
