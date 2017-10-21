package controllers

import javax.inject.Inject

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import services.UserService
import utils.{JsonUtil, SubmitValidate}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

/**
  * Created by sheep3 on 2017/10/21.
  */
class UserController @Inject()(cc: ControllerComponents, userService: UserService) extends AbstractController(cc) {

  val registerUser = (
    (__ \ 'name).read[String] and
      (__ \ 'email).read[String] and
      (__ \ 'password).read[String]
    ) tupled

  val loginUser = (
    (__ \ 'name).read[String] and
      (__ \ 'password).read[String]
    ) tupled

  def register(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    request.body.asJson.map { json =>
      json.validate[(String, String, String)](registerUser).map {
        case (name, email, password) =>
          SubmitValidate.checkRegister(name, email, password)
          userService.register(name, email, password).flatMap(ru => {
            userService.login(ru.name, password).map(u => {
              Ok(JsonUtil.returnMsg(JsString("注册成功")))
                .withSession("username" -> u.name, "id" -> u.id.get.toString, "role" -> u.role)
            })
          })
      }.recoverTotal {
        _ => Future.successful(Ok(JsonUtil.returnMsg(status_code = 1, status_reason = "参数缺少或类型不匹配")))
      }
    }.getOrElse {
      Future.successful(Ok(JsonUtil.returnMsg(status_code = 1, status_reason = "不合法的Json请求")))
    }
  }

  def login(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    request.body.asJson.map { json =>
      json.validate[(String, String)](loginUser).map {
        case (name, password) =>
          SubmitValidate.checkLogin(name, password)
          userService.login(name, password).map(u => {
            Ok(JsonUtil.returnMsg(JsString("登录成功")))
              .withSession("username" -> u.name, "id" -> u.id.get.toString, "role" -> u.role)
          })
      }.recoverTotal {
        _ => Future.successful(Ok(JsonUtil.returnMsg(status_code = 1, status_reason = "参数缺少或类型不匹配")))
      }
    }.getOrElse {
      Future.successful(Ok(JsonUtil.returnMsg(status_code = 1, status_reason = "不合法的Json请求")))
    }
  }

  def logout(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    Future.successful(Ok(JsonUtil.returnMsg(JsString("退出成功"))).withSession())
  }
}
