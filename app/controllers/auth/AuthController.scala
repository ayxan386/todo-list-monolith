package controllers.auth

import controllers.auth.dto.{LoginRequestDTO, TokenResponseDTO}
import errors.dto.badrequest.BodyParsingException
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AbstractController, ControllerComponents}
import services.AuthService

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthController @Inject()(
    cc: ControllerComponents,
    authService: AuthService)(implicit ex: ExecutionContext)
    extends AbstractController(cc) {

  def login = Action.async { implicit request =>
    request.body.asJson match {
      case Some(jsonBody: JsValue) =>
        jsonBody
          .asOpt[LoginRequestDTO]
          .map(dto =>
            authService.login(nickname = dto.nickname, password = dto.password))
          .getOrElse(throw new IllegalStateException("something went wrong"))
          .map(token => Ok(Json.toJson(TokenResponseDTO(token = token))))
      case None => throw BodyParsingException()
    }
  }

  def register = Action.async { implicit request =>
    request.body.asJson match {
      case Some(jsonBody) =>
        jsonBody
          .asOpt[LoginRequestDTO]
          .map(dto => authService.register(dto.nickname, dto.password))
          .getOrElse(throw new IllegalStateException("something went wrong"))
          .map(token => Ok(Json.toJson(TokenResponseDTO(token))))
      case None => throw BodyParsingException()
    }
  }

}
