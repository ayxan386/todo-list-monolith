package services.impl

import dtos.itemlist.{ItemListResponseDTO, ItemRequestDTO, UpdateItemRequest}
import errors.dto.notfound.ItemNotFoundError
import models.{Item, ItemList}
import org.apache.commons.beanutils.BeanUtils
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
    listRepository.insertItemList(newItemList)
  }

  override def getListsByNickname(
      nickname: String): Future[List[ItemListResponseDTO]] = {
    log.info(s"Getting lists of user $nickname")
    listRepository.getItemListsByNickname(nickname)
  }

  override def addItem(req: ItemRequestDTO): Future[Item] = {
    log.info(s"Added item to list ${req.itemListId}")
    val item = convertRequestToModel(req)
    listRepository.insertItem(item)
  }

  override def deleteItem(nickname: String, itemId: String): Future[String] = {
    log.info(s"Deleting item ${itemId} by ${nickname}")
    listRepository.checkUserAndDeleteItem(nickname, itemId)
  }

  override def deleteList(nickname: String, listId: String): Future[String] = {
    log.info(s"Deleting list ${listId} by $nickname")
    listRepository.checkUserAndDeleteList(nickname = nickname,
                                          listId = UUID.fromString(listId))
  }

  override def updateItem(req: UpdateItemRequest): Future[Item] = {
    log.info(s"Update item with id<${req.id}>")
    listRepository
      .getItemById(req.id)
      .map(op => op.getOrElse(throw ItemNotFoundError(req.id.toString)))
      .map(item => copyExistingProperties(item, req))
      .map(listRepository.updateItem)
      .flatten
  }

  private def copyExistingProperties(item: Item, request: UpdateItemRequest) =
    request.content
      .map(content => item.copy(content = Some(content)))
      .orElse(Some(item))
      .flatMap(
        item =>
          request.status
            .map(status => item.copy())
            .orElse(Some(item)))
      .get

  private def convertRequestToModel(req: ItemRequestDTO): Item =
    Item(
      id = UUID.randomUUID(),
      title = req.title,
      content = req.content,
      itemListId = req.itemListId,
      createDate = Some(LocalDateTime.now()),
      updateDate = Some(LocalDateTime.now())
    )

  private def createEmptyList(listName: String, nickname: String) =
    ItemList(id = UUID.randomUUID(),
             name = listName,
             username = nickname,
             createDate = Some(LocalDateTime.now()),
             updateDate = Some(LocalDateTime.now()))

}
