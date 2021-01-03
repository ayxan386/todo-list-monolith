package services

import com.google.inject.ImplementedBy
import models.ItemList
import services.impl.ListServiceImpl

import scala.concurrent.Future

@ImplementedBy(classOf[ListServiceImpl])
trait ListService {

  def createList(listName: String, nickname: String): Future[ItemList]

}
