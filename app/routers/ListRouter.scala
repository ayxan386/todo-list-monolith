package routers

import controllers.lists.ListController
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

import javax.inject.{Inject, Singleton}

@Singleton
class ListRouter @Inject()(listController: ListController)
    extends SimpleRouter {
  override def routes: Routes = {
    case POST(p"/$list_name") => listController.createList(list_name)
    case GET(p"/mine")        => listController.getMyLists
    case PUT(p"/addItem")     => listController.addItem
    case DELETE(p"/item"?q"id=$itemId") => listController.deleteItem(itemId)
    case DELETE(p"/list"?q"id=$listId") => listController.deleteList(listId)
    case PUT(p"/update-item") => listController.updateItem()
  }
}
