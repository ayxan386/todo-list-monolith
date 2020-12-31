package services.impl

import io.jsonwebtoken.{Jwts, SignatureAlgorithm}
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
    Jwts
      .builder()
      .setSubject(nickname)
      .setExpiration(getExpiration())
      .signWith(SignatureAlgorithm.HS256, secret)
      .compact()
  )

  def getExpiration() = new Date(Instant.now().plus(3, ChronoUnit.HOURS).getNano)

}
