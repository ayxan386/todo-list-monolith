package services.impl

import errors.dto.NotFoundError
import models.User
import org.mindrot.jbcrypt.BCrypt
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

  override def login(nickname: String, password: String): Future[String] = {
    userRepository
      .findByNickname(nickname)
      .map(o =>
        o.getOrElse(throw NotFoundError(s"User with $nickname not found")))
      .filter(checkPasswordEquality(_, password))
      .recover { case _ => throw PasswordsMatchingError()}
    Future.successful(jwtUtils.buildToken(nickname))
  }

  private def checkPasswordEquality(user: User, password: String): Boolean =
    BCrypt.checkpw(password, user.password)
}
