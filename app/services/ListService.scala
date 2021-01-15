package services

import com.google.inject.ImplementedBy
import dtos.itemlist.{ItemListResponseDTO, ItemRequestDTO, UpdateItemRequest}
import models.{Item, ItemList}
import services.impl.ListServiceImpl

import scala.concurrent.Future

@ImplementedBy(classOf[ListServiceImpl])
trait ListService {
  def updateItem(req: UpdateItemRequest): Future[Item]

  def addItem(req: ItemRequestDTO): Future[Item]

  def getListsByNickname(nickname: String): Future[List[ItemListResponseDTO]]

  def createList(listName: String, nickname: String): Future[ItemList]

  def deleteItem(nickname: String, itemId: String): Future[String]

  def deleteList(nickname: String, listId: String): Future[String]

}
