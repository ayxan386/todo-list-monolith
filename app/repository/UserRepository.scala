package repository

import io.getquill.{EntityQuery, PostgresAsyncContext, SnakeCase}
import models.User

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(implicit ex: ExecutionContext) {
  lazy val ctx = new PostgresAsyncContext[SnakeCase](SnakeCase, "ctx")

  import ctx._

  val baseModel = quote { querySchema[User]("users") }

  def findByNickname(nickname: String): Future[Option[User]] = {
    val q = quote(baseModel.filter(_.nickname == lift(nickname)))
    ctx.run(q).map(ru => ru.headOption)
  }

}
