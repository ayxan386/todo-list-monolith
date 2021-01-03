package services.impl

import models.ItemList
import play.api.Logger
import repository.ListRepository
import services.ListService

import java.time.LocalDateTime
import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ListServiceImpl @Inject()(listRepository: ListRepository)(
    implicit ex: ExecutionContext)
    extends ListService {

  val log = Logger("list-service")

  override def createList(listName: String,
                          nickname: String): Future[ItemList] = {
    log.info(s"Creating list $listName by user $nickname")
    val newItemList = createEmptyList(listName, nickname)
    listRepository.insert(newItemList)
  }

  private def createEmptyList(listName: String, nickname: String) =
    ItemList(id = UUID.randomUUID(),
             name = listName,
             username = nickname,
             createDate = Some(LocalDateTime.now()),
             updateDate = Some(LocalDateTime.now()))
}
