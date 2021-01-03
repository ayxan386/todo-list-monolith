package controllers.lists

import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._
import routers.ListController

import javax.inject.{Inject, Singleton}

@Singleton
class ListRouter @Inject()(listController: ListController) extends SimpleRouter {
  override def routes: Routes = {
    case POST(p"/$list_name") => listController.createList(list_name)
  }
}
