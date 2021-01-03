package routers

import controllers.GenericResponse
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import services.ListService
import util.TypedKeys

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class ListController @Inject()(
    cc: ControllerComponents,
    listService: ListService)(implicit ex: ExecutionContext)
    extends AbstractController(cc) {

  def createList(listName: String) = Action.async { implicit request =>
    request.attrs
      .get(TypedKeys.tokenType)
      .map(nickname =>
        listService.createList(listName = listName, nickname = nickname))
      .get
      .map(il => GenericResponse("success", il))
      .map(gr => Ok(Json.toJson(gr)))
  }
}
