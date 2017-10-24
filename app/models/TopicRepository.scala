package models

import java.util.Date
import javax.inject.{Inject, Singleton}

import anorm.SqlParser.get
import anorm.{SQL, SqlParser, ~}
import exceptions.KnownSystemException
import play.api.Logger
import play.api.db.DBApi
import play.api.libs.json.Json

import scala.beans.BeanProperty
import scala.concurrent.Future

/**
  * Created by sheep3 on 2017/10/21.
  */

case class Topic(
                  @BeanProperty id: Option[Long] = None,
                  @BeanProperty name: String,
                  @BeanProperty desc: String,
                  @BeanProperty user_id: Long,
                  @BeanProperty update_time: Option[Date] = None,
                  @BeanProperty create_time: Option[Date] = None
                )

object Topic {
  implicit val format = Json.format[Topic]
}

case class TopicWithFollow(
                            @BeanProperty id: Option[Long] = None,
                            @BeanProperty name: String,
                            @BeanProperty desc: String,
                            @BeanProperty user_id: Long,
                            @BeanProperty follow: Boolean,
                            @BeanProperty update_time: Option[Date] = None,
                            @BeanProperty create_time: Option[Date] = None
                          )

object TopicWithFollow {
  implicit val format = Json.format[TopicWithFollow]
}

@Singleton
class TopicRepository @Inject()(dBApi: DBApi)(implicit ec: DatabaseExecutionContext) {

  private val db = dBApi.database("default")

  private val TABLE_NAME = "t_topic"

  private val FollowTableName = "t_follow"

  private val simple = {
    get[Option[Long]](s"$TABLE_NAME.c_id") ~
      get[String](s"$TABLE_NAME.c_name") ~
      get[String](s"$TABLE_NAME.c_desc") ~
      get[Long](s"$TABLE_NAME.c_user_id") ~
      get[Option[Date]](s"$TABLE_NAME.c_update_time") ~
      get[Option[Date]](s"$TABLE_NAME.c_create_time") map {
      case id ~ name ~ desc ~ user_id ~ update_time ~ create_time =>
        Topic(id, name, desc, user_id, update_time, create_time)
    }
  }

  private val topicWithFollow = {
    get[Option[Long]](s"$TABLE_NAME.c_id") ~
      get[String](s"$TABLE_NAME.c_name") ~
      get[String](s"$TABLE_NAME.c_desc") ~
      get[Long](s"$TABLE_NAME.c_user_id") ~
      get[Long](s"c_follow") ~
      get[Option[Date]](s"$TABLE_NAME.c_update_time") ~
      get[Option[Date]](s"$TABLE_NAME.c_create_time") map {
      case id ~ name ~ desc ~ user_id ~ follow ~ update_time ~ create_time =>
        TopicWithFollow(id, name, desc, user_id, follow != 0, update_time, create_time)
    }
  }

  def list(page: Int = 0, pageSize: Int = 10, filter: String = "%"): Future[List[Topic]] = Future {
    val offset = pageSize * page
    db.withConnection { implicit connection =>
      SQL(s"select * from $TABLE_NAME where c_name like {filter} limit {pageSize} offset {offset}").on(
        'pageSize -> pageSize,
        'offset -> offset,
        'filter -> filter
      ).as(simple *)
    }
  }

  def listWithUser(user_id: Long = -1, page: Int = 0, pageSize: Int = 10, filter: String = "%"): Future[List[TopicWithFollow]] = Future {
    val offset = pageSize * page
    db.withConnection { implicit connection =>
      SQL(
        s"""
           SELECT t.c_id, t.c_name, t.c_desc, if(f.c_user_id is null,0,1) as c_follow,
           t.c_user_id, t.c_update_time, t.c_create_time FROM $TABLE_NAME as t
           left JOIN t_follow as f on
           f.c_user_id={user_id} and
           f.c_topic_id=t.c_id where
           t.c_name like {filter}
           limit {pageSize} offset {offset}
         """
      ).on(
        'pageSize -> pageSize,
        'offset -> offset,
        'user_id -> user_id,
        'filter -> filter
      ).as(topicWithFollow *)
    }
  }

  def totalCount(filter: String = "%") = Future {
    db.withConnection { implicit connection =>
      SQL(s"SELECT COUNT(*) FROM $TABLE_NAME WHERE c_name like {filter}").on(
        'filter -> filter
      ).as(SqlParser.scalar[Long].singleOpt).get
    }
  }

  def findByName(name: String): Future[Option[Topic]] = Future {
    db.withConnection { implicit connection =>
      SQL(s"select * from $TABLE_NAME where c_name = {name}")
        .on('name -> name).as(simple.singleOpt)
    }
  }

  def findByTopicId(id: Long): Future[Option[Topic]] = Future {
    db.withConnection { implicit connection =>
      SQL(s"select * from $TABLE_NAME where c_id = {id}")
        .on('id -> id).as(simple.singleOpt)
    }
  }

  def findByUserId(user_id: Long): Future[Option[Topic]] = Future {
    db.withConnection { implicit connection =>
      SQL(s"select * from $TABLE_NAME where c_user_id = {user_id}")
        .on('user_id -> user_id).as(simple.singleOpt)
    }
  }

  def save(topic: Topic) = Future {
    db.withConnection { implicit connection =>
      SQL(
        s"""
          insert into $TABLE_NAME (
            c_name, c_desc, c_user_id
          ) values (
            {name}, {desc}, {user_id}
          )
        """)
        .on(
          'name -> topic.name,
          'desc -> topic.desc,
          'user_id -> topic.user_id
        ).executeUpdate()
    }
  }

  def update(topic: Topic) = Future {
    db.withConnection { implicit connection =>
      SQL(
        s"""
          c_update $TABLE_NAME set
          c_name={name},
          c_desc={desc}
          where c_id={id}
        """)
        .on(
          'id -> topic.id,
          'name -> topic.name,
          'desc -> topic.desc
        ).executeUpdate()
    }
  }

  def follow(topic_id: Long, user_id: Long) = Future {
    db.withConnection { implicit connection =>
      if (SQL(
        s"""
          select count(*) from $FollowTableName  where c_user_id = {user_id} and c_topic_id = {topic_id}
        """)
        .on(
          'topic_id -> topic_id,
          'user_id -> user_id
        ).executeQuery().as(SqlParser.scalar[Long].singleOpt).get == 0
      ) {
        SQL(
          s"""
          insert into $FollowTableName (
            c_topic_id, c_user_id
          ) values (
            {topic_id}, {user_id}
          )
        """)
          .on(
            'topic_id -> topic_id,
            'user_id -> user_id
          ).executeUpdate()
      } else {
        throw new KnownSystemException("你已经关注过该主题")
      }
    }
  }

  def unFollow(topic_id: Long, user_id: Long) = Future {
    db.withConnection { implicit connection =>
      if (SQL(
        s"""
          select count(*) from $FollowTableName  where c_user_id = {user_id} and c_topic_id = {topic_id}
        """)
        .on(
          'topic_id -> topic_id,
          'user_id -> user_id
        ).executeQuery().as(SqlParser.scalar[Long].singleOpt).get != 0
      ) {
        SQL(
          s"""
          delete from $FollowTableName where c_user_id = {user_id} and c_topic_id = {topic_id}
        """)
          .on(
            'topic_id -> topic_id,
            'user_id -> user_id
          ).executeUpdate()
      } else {
        throw new KnownSystemException("你没有关注过该主题")
      }
    }
  }

  def followList(user_id: Long) = Future {
    db.withConnection { implicit connection =>
      SQL(s"select * from $TABLE_NAME as t where t.c_id in (select f.c_topic_id from $FollowTableName as f where f.c_user_id = {user_id})")
        .on('user_id -> user_id).as(simple *)
    }
  }
}
