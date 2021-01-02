package filters

import akka.stream.Materializer
import play.api.mvc.{Filter, RequestHeader, Result}
import util.{JwtUtils, TypedKeys}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TokenFilter @Inject()(jwtUtils: JwtUtils)(implicit val mat: Materializer,
                                                ec: ExecutionContext)
    extends Filter {

  private val ignoredPaths = List("login", "register")
  val HEADER_NAME = "Authorization"
  val PREFIX = "Bearer "

  def checkPath(rh: RequestHeader): Option[RequestHeader] =
    Some(rh)
      .filter(r => !ignoredPaths.exists(path => r.path.contains(path)))

  override def apply(f: RequestHeader => Future[Result])(
      rh: RequestHeader): Future[Result] = {
    if (checkPath(rh).isDefined)
      Some(rh)
        .map(rh => rh.headers.get(HEADER_NAME))
        .getOrElse(throw MissingHeadersError())
        .map(header => header.substring(0, PREFIX.length))
        .filter(jwtUtils.isValid)
        .orElse(throw TokenExpiredError())
        .map(jwtUtils.getNickname)
        .map(nickname => rh.addAttr(TypedKeys.tokenType, nickname))
        .map(f)
        .get
    else f(rh)
  }
}
