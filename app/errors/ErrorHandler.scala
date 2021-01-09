package errors

import errors.dto.{AbstractHttpError, ErrorResponseDTO}
import org.slf4j.LoggerFactory
import play.api.http.HttpErrorHandler
import play.api.libs.json.Json
import play.api.mvc.Results.Status
import play.api.mvc.{RequestHeader, Result}

import java.time.LocalDateTime
import javax.inject.Singleton
import scala.concurrent.Future

@Singleton
class ErrorHandler extends HttpErrorHandler {

  val log = LoggerFactory.getLogger(classOf[ErrorHandler])

  override def onClientError(request: RequestHeader,
                             statusCode: Int,
                             message: String): Future[Result] = {
    log.error(s"Status code <$statusCode>; $message")
    Future.successful(buildResult(statusCode, message))
  }

  override def onServerError(request: RequestHeader,
                             ex: Throwable): Future[Result] = {
    val result = ex match {
      case httpError: AbstractHttpError =>
        buildResult(statusCode = httpError.status, httpError.message)
      case _ => buildResult(500, ex.getMessage)
    }
    log.error(s"${ex.getMessage}")
    Future.successful(result)
  }

  def buildErrorResponse(message: String, status: Int): ErrorResponseDTO =
    ErrorResponseDTO(message = message,
                     time = LocalDateTime.now(),
                     status = status)

  private def buildResult(statusCode: Int, message: String) = {
    Status(statusCode)(Json.toJson(buildErrorResponse(message, statusCode)))
  }
}
