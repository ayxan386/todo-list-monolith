package repository

import dtos.itemlist.ItemListResponseDTO
import io.getquill.{PostgresAsyncContext, SnakeCase}
import models.{Item, ItemList}

import javax.inject.{Inject, Singleton}
import scala.collection.compat.toTraversableLikeExtensionMethods
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

  def toReponseDTO(tup: (ItemList, List[Option[Item]])): ItemListResponseDTO = {
    val (itemList, itemsOption) = tup
    val items = itemsOption.filter(_.isDefined).map(_.get)
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
            .groupMap(_._1)(_._2)
            .map(tup => toReponseDTO(tup))
            .toList)
  }
}
