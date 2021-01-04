package repository

import io.getquill.{PostgresAsyncContext, SnakeCase}
import models.ItemList

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ListRepository @Inject()(implicit ex: ExecutionContext) {

  lazy val ctx = new PostgresAsyncContext[SnakeCase](SnakeCase, "ctx")

  import ctx._

  val baseListModel = quote {
    querySchema[ItemList]("item_list")
  }

  def insert(list: ItemList): Future[ItemList] = {
    val q = quote {
      baseListModel.insert(lift(list))
    }
    ctx.run(q).map(_ => list)
  }

  def getItemListsByNickname(nickname: String): Future[List[ItemList]] = {
    val q = quote {
      baseListModel.filter(_.username == lift(nickname))
    }
    ctx.run(q)
  }
}
