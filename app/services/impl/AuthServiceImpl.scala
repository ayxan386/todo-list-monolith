package services.impl

import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.impl.DefaultJwtBuilder
import services.AuthService

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date
import javax.inject.Singleton
import scala.concurrent.Future

@Singleton
class AuthServiceImpl extends AuthService {

  val secret = "hello_world"

  override def login(nickname: String, password: String): Future[String]
  = Future.successful(
    new DefaultJwtBuilder()
      .setExpiration(getExpiration())
      .setSubject(nickname)
      .signWith(SignatureAlgorithm.ES256, secret.getBytes)
      .compact())

  def getExpiration() = new Date(Instant.now().plus(3, ChronoUnit.HOURS).getNano)

}
