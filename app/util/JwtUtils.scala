package util

import io.jsonwebtoken.{JwtException, Jwts, SignatureAlgorithm}

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date
import javax.inject.Singleton

@Singleton
class JwtUtils {

  val secret = "hello_world"

  def buildToken(nickname: String): String =
    Jwts
      .builder()
      .setSubject(nickname)
      .setExpiration(getExpiration)
      .signWith(SignatureAlgorithm.HS256, secret.getBytes())
      .compact()

  def getClaims(token: String) =
    try {
      Jwts
        .parser()
        .setSigningKey(secret.getBytes())
        .parseClaimsJws(token)
    } catch {
      case ex: JwtException => throw InvalidTokenSignatureError()
    }

  def isValid(token: String) =
    getClaims(token).getBody.getExpiration
      .before(new Date())

  def getExpiration = new Date(Instant.now().plus(3, ChronoUnit.HOURS).getNano)

  def getNickname(token: String) = getClaims(token).getBody.getSubject
}
