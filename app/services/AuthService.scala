package services

import com.google.inject.ImplementedBy
import services.impl.AuthServiceImpl

import scala.concurrent.Future

@ImplementedBy(classOf[AuthServiceImpl])
trait AuthService {

  def login(nickname: String, password: String): Future[String]

  def register(nickname: String, password: String): Future[String]

}
