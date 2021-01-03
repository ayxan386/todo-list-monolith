package routers

import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter

import javax.inject.{Inject, Singleton}
import play.api.routing.sird._

@Singleton
class ListRouter @Inject()(listController: ListController) extends SimpleRouter {
  override def routes: Routes = {
    case POST(p"/$list_name") => listController.createList(list_name)
  }
}
