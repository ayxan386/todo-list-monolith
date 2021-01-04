package services

import com.google.inject.ImplementedBy
import dtos.itemlist.{ItemListResponseDTO, ItemRequestDTO}
import models.{Item, ItemList}
import services.impl.ListServiceImpl

import scala.concurrent.Future

@ImplementedBy(classOf[ListServiceImpl])
trait ListService {

  def addItem(req: ItemRequestDTO): Future[Item]

  def getListsByNickname(nickname: String): Future[List[ItemListResponseDTO]]

  def createList(listName: String, nickname: String): Future[ItemList]

}
