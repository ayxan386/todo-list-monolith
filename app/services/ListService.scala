package services

import models.ItemList

import scala.concurrent.Future

trait ListService {

  def createList(listName: String, nickname: String): Future[ItemList]

}
