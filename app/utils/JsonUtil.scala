package utils

import play.api.libs.json._
import play.api.mvc.Codec

import scala.beans.BeanProperty

/**
  * Created by admin on 2017/10/12.
  */
object JsonUtil {

  implicit val myCustomCharset: Codec = Codec.javaSupported("UTF-8")

  def returnMsg(msg: JsValue = JsString(""), status_code: Int = 0, status_reason: String = ""): JsValue = {
    Json.obj(
      "status" -> Json.obj(
        "status_code" -> status_code,
        "status_reason" -> status_reason
      ),
      "result" -> msg
    )
  }
}
