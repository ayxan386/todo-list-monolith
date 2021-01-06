package repository

import io.getquill.{PostgresJdbcContext, SnakeCase}
import models.User

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(implicit ex: ExecutionContext) {

  lazy val ctx = new PostgresJdbcContext[SnakeCase](SnakeCase, "ctx")

  import ctx._

  val baseModel = quote { querySchema[User]("users") }

  def findByNickname(nickname: String): Future[Option[User]] = {
    val q = quote(baseModel.filter(_.nickname == lift(nickname)))
    Future.successful(ctx.run(q).headOption)
  }

  def insertUser(user: User): Future[User] = {
    val q = quote(baseModel.insert(lift(user)))
    Future
      .apply(ctx.run(q))
      .map(_ => user)
  }

  def deleteByNickname(nickname: String): Future[String] = {
    val q = quote(baseModel.filter(_.nickname == lift(nickname)).delete)
    Future(ctx.run(q)).map(_ => "success")
  }
}
