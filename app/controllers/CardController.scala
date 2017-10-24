package controllers

import javax.inject.Inject

import play.api.libs.json.{JsNumber, JsString, Json, __}
import play.api.libs.functional.syntax._
import play.api.mvc._
import services.CardService
import utils.{JsonUtil, SubmitValidate}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps


/**
  * Created by sheep3 on 2017/10/22.
  */
class CardController @Inject()(cc: ControllerComponents, cardService: CardService) extends AbstractController(cc) {

  val postCard = (
    (__ \ 'topic_id).read[Long] and
      (__ \ 'content).read[String]
    ) tupled


  def card(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    request.session.get("id") match {
      case Some(user_id) =>
        request.body.asJson.map { json =>
          json.validate[(Long, String)](postCard).map {
            case (topic_id, content) =>
              SubmitValidate checkCard content
              cardService.save(topic_id, user_id.toLong, content).map { c =>
                Ok(JsonUtil.returnMsg(JsString("打卡成功!")))
              }
          }.recoverTotal {
            _ => Future.successful(Ok(JsonUtil.returnMsg(status_code = 1, status_reason = "参数缺少或类型不匹配")))
          }
        }.getOrElse {
          Future.successful(Ok(JsonUtil.returnMsg(status_code = 1, status_reason = "不合法的Json请求")))
        }
      case _ =>
        Future.successful(Ok(JsonUtil.returnMsg(status_code = 1, status_reason = "未登录")))
    }
  }

  def list(filter: String, page: Int, size: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    cardService.list(page, size, s"%$filter%").map { s =>
      Ok(JsonUtil.returnMsg(Json.toJson(s)))
    }
  }
}
