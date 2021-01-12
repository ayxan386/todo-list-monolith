package controllers.lists

import controllers.GenericResponse
import dtos.itemlist.ItemRequestDTO
import errors.dto.badrequest.BodyParsingException
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents, Handler}
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

  def getMyLists: Handler = Action.async { implicit request =>
    request.attrs
      .get(TypedKeys.tokenType)
      .map(nickname => listService.getListsByNickname(nickname))
      .get
      .map(li => GenericResponse("success", li))
      .map(Json.toJson(_))
      .map(Ok(_))
  }

  def addItem: Handler = Action.async { implicit request =>
    request.body.asJson match {
      case Some(json) =>
        json
          .asOpt[ItemRequestDTO]
          .map(req => listService.addItem(req))
          .get
          .map(item => GenericResponse("success", item))
          .map(Json.toJson(_))
          .map(Ok(_))
      case None => throw BodyParsingException()
    }
  }

  def deleteItem(itemId: String): Handler = Action.async{implicit request =>
    request.attrs
      .get(TypedKeys.tokenType)
      .map(nickname => listService.deleteItem(nickname = nickname, itemId = itemId))
  }
}
