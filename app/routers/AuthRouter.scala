package routers

import controllers.auth.AuthController
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

import javax.inject.{Inject, Singleton}

@Singleton
class AuthRouter @Inject()(authController: AuthController)
    extends SimpleRouter {
  override def routes: Routes = {
    case POST(p"/login")    => authController.login
    case POST(p"/register") => authController.register
    case DELETE(p"")        => authController.deleteUser
  }
}
