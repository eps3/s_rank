package models

import java.util.Date
import javax.inject.{Inject, Singleton}

import anorm.SqlParser.get
import anorm.{SQL, ~}
import play.api.db.DBApi

import scala.beans.BeanProperty
import scala.concurrent.Future

/**
  * Created by sheep3 on 2017/10/21.
  */

case class User(
                 @BeanProperty id: Option[Long] = None,
                 @BeanProperty name: String,
                 @BeanProperty email: String,
                 @BeanProperty desc: String,
                 @BeanProperty img_url: String,
                 @BeanProperty role: String,
                 @BeanProperty password: String,
                 @BeanProperty salt: String,
                 @BeanProperty update_time: Option[Date] = None,
                 @BeanProperty create_time: Option[Date] = None
               )

@Singleton
class UserRepository @Inject()(dBApi: DBApi)(implicit ec: DatabaseExecutionContext) {
  private val db = dBApi.database("default")

  private val TABLE_NAME = "t_user"

  private val simple = {
    get[Option[Long]](s"$TABLE_NAME.c_id") ~
      get[String](s"$TABLE_NAME.c_name") ~
      get[String](s"$TABLE_NAME.c_email") ~
      get[String](s"$TABLE_NAME.c_desc") ~
      get[String](s"$TABLE_NAME.c_img_url") ~
      get[String](s"$TABLE_NAME.c_role") ~
      get[String](s"$TABLE_NAME.c_password") ~
      get[String](s"$TABLE_NAME.c_salt") ~
      get[Option[Date]](s"$TABLE_NAME.c_update_time") ~
      get[Option[Date]](s"$TABLE_NAME.c_create_time") map {
      case id ~ name ~ email ~ desc ~ img_url ~ role ~ password ~ salt ~ update_time ~ create_time =>
        User(id, name, email, desc, img_url, role, password, salt, update_time, create_time)
    }
  }

  def findByName(name: String): Future[Option[User]] = Future {
    db.withConnection { implicit connection =>
      SQL(s"select * from $TABLE_NAME where c_name = {name}")
        .on('name -> name).as(simple.singleOpt)
    }
  }

  def findByEmail(email: String): Future[Option[User]] = Future {
    db.withConnection { implicit connection =>
      SQL(s"select * from $TABLE_NAME where c_email = {email}")
        .on('email -> email).as(simple.singleOpt)
    }
  }

  def save(user: User) = Future {
    db.withConnection { implicit connection =>
      SQL(
        s"""
          insert into $TABLE_NAME (
            c_name, c_email, c_desc, c_img_url, c_role, c_password, c_salt
          ) values (
            {name}, {email}, {desc}, {img_url}, {role}, {password}, {salt}
          )
        """)
        .on(
          'name -> user.name,
          'email -> user.email,
          'desc -> user.desc,
          'img_url -> user.img_url,
          'role -> user.role,
          'password -> user.password,
          'salt -> user.salt
        ).executeUpdate()
    }
  }

  def update(user: User) = Future {
    db.withConnection { implicit connection =>
      SQL(
        s"""
          c_update $TABLE_NAME set
          c_name={name},
          c_email={email},
          c_desc={desc},
          c_img_url={img_url}
          c_role={role}
          c_password={password}
          where c_id={id}
        """)
        .on(
          'id -> user.id,
          'name -> user.name,
          'email -> user.email,
          'desc -> user.desc,
          'img_url -> user.img_url,
          'role -> user.role,
          'password -> user.password,
          'salt -> user.salt
        ).executeUpdate()
    }
  }

}
