package repository

import dtos.itemlist.ItemListResponseDTO
import io.getquill.{PostgresJdbcContext, SnakeCase}
import models.{Item, ItemList}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ListRepository @Inject()(implicit ex: ExecutionContext) {

  lazy val ctx = new PostgresJdbcContext[SnakeCase](SnakeCase, "ctx")

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
    Future(ctx.run(q)).map(_ => list)
  }

  def insertItem(item: Item): Future[Item] = {
    val q = quote {
      baseItemModel.insert(lift(item))
    }
    Future(ctx.run(q)).map(_ => item)
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
    Future(
      ctx
        .run(q)
        .groupMap(_._1)(_._2)
        .map(tup => toReponseDTO(tup))
        .toList)
  }

  def checkUserAndDeleteItem(nickname: String,
                             itemId: String): Future[String] = {
    val q = quote {
      listJoinedItems
        .filter(tup => tup._1.username == lift(nickname))
        .filter(tup => tup._2.exists(item => item.id.toString == lift(itemId)))
    }
    Future(
      ctx
        .run(q)
    ).map(_ => "deleted")
  }
}
