package util

import errors.dto.unauthorizthed.InvalidTokenSignatureError
import io.jsonwebtoken.{JwtException, Jwts, SignatureAlgorithm}

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.{Base64, Date}
import javax.inject.Singleton

@Singleton
class JwtUtils {

  val secret = "hello_world"

  def buildToken(nickname: String): String =
    Jwts
      .builder()
      .setSubject(nickname)
      .setExpiration(getExpiration)
      .signWith(SignatureAlgorithm.HS256, secret)
      .compact()

  def getClaims(token: String) =
    try {
      println(secret)
      Jwts
        .parser()
        .setSigningKey(secret)
        .parseClaimsJws(token)
    } catch {
      case ex: JwtException => throw InvalidTokenSignatureError(ex)
    }

  def isValid(token: String) = {
    println(token)
    val exp = getClaims(token).getBody.getExpiration
    println(exp)
    exp.after(new Date())
  }

  def getExpiration = Date.from(Instant.now().plus(3, ChronoUnit.HOURS))

  def getNickname(token: String) = getClaims(token).getBody.getSubject
}
