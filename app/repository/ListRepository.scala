package repository

import dtos.itemlist.ItemListResponseDTO
import io.getquill.{PostgresAsyncContext, SnakeCase}
import models.{Item, ItemList}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ListRepository @Inject()(implicit ex: ExecutionContext) {

  lazy val ctx = new PostgresAsyncContext[SnakeCase](SnakeCase, "ctx")

  import ctx._

  val baseListModel = quote {
    querySchema[ItemList]("item_list")
  }

  val baseItemModel = quote {
    querySchema[Item]("items")
  }

  val listJoinedItems = quote {
    baseListModel.leftJoin(baseItemModel).on((l, i) => l.id == i.itemListId)
  }

  def insertItemList(list: ItemList): Future[ItemList] = {
    val q = quote {
      baseListModel.insert(lift(list))
    }
    ctx.run(q).map(_ => list)
  }

  def insertItem(item: Item): Future[Item] = {
    val q = quote {
      baseItemModel.insert(lift(item))
    }
    ctx.run(q).map(_ => item)
  }

  def toReponseDTO(tup: (ItemList, List[Item])): ItemListResponseDTO = {
    val (itemList, items) = tup
    ItemListResponseDTO(
      id = itemList.id,
      name = itemList.name,
      username = itemList.username,
      createDate = itemList.createDate,
      updateDate = itemList.updateDate,
      items = items
    )
  }

  def getItemListsByNickname(
      nickname: String): Future[List[ItemListResponseDTO]] = {
    val q = quote {
      listJoinedItems.filter(_._1.username == lift(nickname))
    }
    ctx
      .run(q)
      .map(
        list =>
          list
            .filter(tup => tup._2.isDefined)
            .groupMap(_._1)(_._2.get)
            .map(tup => toReponseDTO(tup))
            .toList)
  }
}
