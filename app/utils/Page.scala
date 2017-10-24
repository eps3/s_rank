package utils

import models.{Card, CardWithUser, Topic, TopicWithFollow}
import play.api.libs.json.Json

/**
  * Created by sheep3 on 2017/10/24.
  */
case class Page[T](items: Seq[T], page: Int, pageSize: Long, total: Long)
object Page {
  implicit val topicFormat = Json.format[Page[Topic]]
  implicit val CardFormat = Json.format[Page[Card]]
  implicit val topicWithFollowFormat = Json.format[Page[TopicWithFollow]]
  implicit val cardWithUserFormat = Json.format[Page[CardWithUser]]
}
