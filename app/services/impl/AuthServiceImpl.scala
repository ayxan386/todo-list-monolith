package services.impl

import errors.dto.badrequest.PasswordsMatchingError
import errors.dto.notfound.NotFoundError
import models.User
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory
import repository.UserRepository
import services.AuthService
import util.JwtUtils

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthServiceImpl @Inject()(
    jwtUtils: JwtUtils,
    userRepository: UserRepository)(implicit ex: ExecutionContext)
    extends AuthService {

  val log = LoggerFactory.getLogger(classOf[AuthServiceImpl])

  override def login(nickname: String, password: String): Future[String] = {
    log.info(s"Logging user with nickname $nickname")
    userRepository
      .findByNickname(nickname)
      .map(_.getOrElse(throw NotFoundError(s"User with $nickname not found")))
      .filter(checkPasswordEquality(_, password))
      .map(user => jwtUtils.buildToken(user.nickname))
  }

  private def checkPasswordEquality(user: User, password: String): Boolean =
    if (BCrypt.checkpw(password, user.password)) true
    else throw PasswordsMatchingError()
}
