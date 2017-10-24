package controllers

import javax.inject.Inject

import play.api.Logger
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.mvc._
import services.TopicService
import utils.{JsonUtil, SubmitValidate}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps

/**
  * Created by sheep3 on 2017/10/21.
  */
class TopicController @Inject()(cc: ControllerComponents, topicService: TopicService) extends AbstractController(cc) {

  val addTopic = (
    (__ \ 'name).read[String] and
      (__ \ 'desc).read[String]
    ) tupled

  def topic(filter: String, page: Int, size: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    request.session.get("id") match {
      case Some(user_id) =>
        topicService.listWithUser(user_id.toLong, page, size, s"%$filter%").map { s =>
          Ok(JsonUtil.returnMsg(Json.toJson(s)))
        }
      case _ =>
        topicService.listWithUser(-1, page, size, s"%$filter%").map { s =>
          Ok(JsonUtil.returnMsg(Json.toJson(s)))
        }
    }
  }

  def followTopic(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    request.session.get("id") match {
      case Some(user_id) =>
        topicService.followList(user_id.toLong).map { s =>
          Ok(JsonUtil.returnMsg(Json.toJson(s)))
        }
      case _ =>
        Future.successful(Ok(JsonUtil.returnMsg(status_code = 1, status_reason = "未登录")))
    }
  }

  def follow(topic_id: Long): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val user_id = request.session.get("id")
    if (user_id.nonEmpty) {
      topicService.follow(topic_id, user_id.get.toLong).map { c =>
        Ok(JsonUtil.returnMsg(JsString("关注成功！")))
      }
    } else {
      Future.successful(Ok(JsonUtil.returnMsg(status_code = 1, status_reason = "未登录")))
    }
  }

  def unFollow(topic_id: Long): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val user_id = request.session.get("id")
    if (user_id.nonEmpty) {
      topicService.unFollow(topic_id, user_id.get.toLong).map { c =>
        Ok(JsonUtil.returnMsg(JsString("取消关注成功！")))
      }
    } else {
      Future.successful(Ok(JsonUtil.returnMsg(status_code = 1, status_reason = "未登录")))
    }
  }

  def add(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val user_id = request.session.get("id")
    if (user_id.nonEmpty) {
      request.session.get("role").get match {
        case "ADMIN" =>
          request.body.asJson.map { json =>
            json.validate[(String, String)](addTopic).map {
              case (name, desc) =>
                SubmitValidate.checkTopic(name, desc)
                Logger.info(s"$name, $desc, ${user_id.get.toLong}")
                topicService
                  .addTopic(name, desc, user_id.get.toLong)
                  .map { c =>
                    Ok(JsonUtil.returnMsg(JsNumber(c)))
                  }
            }.recoverTotal {
              _ => Future.successful(Ok(JsonUtil.returnMsg(status_code = 1, status_reason = "参数缺少或类型不匹配")))
            }
          }.getOrElse {
            Future.successful(Ok(JsonUtil.returnMsg(status_code = 1, status_reason = "不合法的Json请求")))
          }
        case _ =>
          Future.successful(Ok(JsonUtil.returnMsg(status_code = 1, status_reason = "你的权限不够!")))
      }
    } else {
      Future.successful(Ok(JsonUtil.returnMsg(status_code = 1, status_reason = "未登录")))
    }
  }
}
