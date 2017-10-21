package controllers

import javax.inject.Inject

import play.api.Logger
import play.api.mvc._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

/**
  * Created by xuxin02 on 2017/10/11.
  */
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  val logger = Logger


  def index(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    Future {
      Ok("成功")
    }
  }
}
