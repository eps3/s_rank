package exceptions

import javax.inject.Singleton

import play.api.Logger
import play.api.http.HttpErrorHandler
import play.api.mvc.{RequestHeader, Result}
import play.api.mvc.Results._
import play.api.http.ContentTypes._
import utils.JsonUtil

import scala.concurrent.Future

/**
  * Created by admin on 2017/10/12.
  */
@Singleton
class ErrorHandler extends HttpErrorHandler {
  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Future.successful(
      Status(statusCode)(JsonUtil.returnMsg(status_code = 400, status_reason = "client error")).as(JSON)
    )
  }

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    exception match {
      case k: KnownSystemException =>
        Future.successful(
          Ok(JsonUtil.returnMsg(status_code = k.code, status_reason = k.getMessage))
        )
      case _ =>
        Logger.error(s"ServerError -> ${request.host}${request.uri}", exception)
        Future.successful(
          InternalServerError(JsonUtil.returnMsg(status_code = 500, status_reason = "server error")).as(JSON)
        )
    }
  }
}
