package models

import java.util.Date
import javax.inject.{Inject, Singleton}

import anorm.SqlParser.get
import anorm.{SQL, ~}
import play.api.db.DBApi
import play.api.libs.json.Json

import scala.beans.BeanProperty
import scala.concurrent.Future

/**
  * Created by sheep3 on 2017/10/22.
  */
case class Card(
                 @BeanProperty id: Option[Long] = None,
                 @BeanProperty topic_id: Long,
                 @BeanProperty user_id: Long,
                 @BeanProperty content: String,
                 @BeanProperty update_time: Option[Date] = None,
                 @BeanProperty create_time: Option[Date] = None
               )

object Card {
  implicit val format = Json.format[Card]
}

case class CardWithUser(
                         @BeanProperty id: Option[Long] = None,
                         @BeanProperty topic_name: String,
                         @BeanProperty user_name: String,
                         @BeanProperty content: String,
                         @BeanProperty update_time: Option[Date] = None,
                         @BeanProperty create_time: Option[Date] = None
                       )

object CardWithUser {
  implicit val format = Json.format[CardWithUser]
}


@Singleton
class CardRepository @Inject()(dBApi: DBApi)(implicit ec: DatabaseExecutionContext) {

  private val db = dBApi.database("default")

  private val TABLE_NAME = "t_card"

  private val simple = {
    get[Option[Long]](s"$TABLE_NAME.c_id") ~
      get[Long](s"$TABLE_NAME.c_topic_id") ~
      get[Long](s"$TABLE_NAME.c_user_id") ~
      get[String](s"$TABLE_NAME.c_content") ~
      get[Option[Date]](s"$TABLE_NAME.c_update_time") ~
      get[Option[Date]](s"$TABLE_NAME.c_create_time") map {
      case id ~ topic_id ~ user_id ~ content ~ update_time ~ create_time =>
        Card(id, topic_id, user_id, content, update_time, create_time)
    }
  }

  private val cardWithName = {
    get[Option[Long]](s"$TABLE_NAME.c_id") ~
      get[String](s"c_topic_name") ~
      get[String](s"c_user_name") ~
      get[String](s"$TABLE_NAME.c_content") ~
      get[Option[Date]](s"$TABLE_NAME.c_update_time") ~
      get[Option[Date]](s"$TABLE_NAME.c_create_time") map {
      case id ~ topic_name ~ user_name ~ content ~ update_time ~ create_time =>
        CardWithUser(id, topic_name, user_name, content, update_time, create_time)
    }
  }

  def save(card: Card) = Future {
    db.withConnection { implicit connection =>
      SQL(
        s"""
          insert into $TABLE_NAME (
            c_topic_id, c_user_id, c_content
          ) values (
            {topic_id}, {user_id}, {content}
          )
        """)
        .on(
          'topic_id -> card.topic_id,
          'user_id -> card.user_id,
          'content -> card.content
        ).executeUpdate()
    }
  }
  // SELECT c.c_id, t.c_name as c_topic_name, u.c_name as c_user_name, c.c_content, c.c_update_time, c.c_create_time FROM t_card as c left join t_user as u on c.c_user_id = u.c_id left join t_topic as t on t.c_id = c.c_topic_id
  def list(page: Int = 0, pageSize: Int = 10, filter: String = "%") = Future {
    val offset = pageSize * page
    db.withConnection { implicit connection =>
      SQL(
        s"""
           SELECT c.c_id, t.c_name as c_topic_name, u.c_name as c_user_name, c.c_content, c.c_update_time, c.c_create_time FROM $TABLE_NAME as c
           left join t_user as u on c.c_user_id = u.c_id
           left join t_topic as t on t.c_id = c.c_topic_id
           and c.c_content like {filter}
           limit {pageSize} offset {offset}
         """
      ).on(
        'pageSize -> pageSize,
        'offset -> offset,
        'filter -> filter
      ).as(cardWithName *)
    }
  }

  def findByTopicAndUser(topic_id: Long, user_id: Long) = Future {
    db.withConnection { implicit connection =>
      SQL(s"select * from $TABLE_NAME where c_topic_id = {topic_id} and c_user_id = {user_id} order by c_create_time desc")
        .on(
          'topic_id -> topic_id,
          'user_id -> user_id
        ).as(simple *)
    }
  }

  def findFirstByTopicAndUser(topic_id: Long, user_id: Long) = Future {
    db.withConnection { implicit connection =>
      SQL(s"select * from $TABLE_NAME where c_topic_id = {topic_id} and c_user_id = {user_id} order by c_create_time desc limit 1")
        .on(
          'topic_id -> topic_id,
          'user_id -> user_id
        ).as(simple.singleOpt)
    }
  }

}
