package services

import javax.inject.{Inject, Singleton}

import exceptions.KnownSystemException
import models.{User, UserRepository}
import utils.PassWordUtil

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps


/**
  * Created by sheep3 on 2017/10/21.
  */
@Singleton
class UserService @Inject()(userRepository: UserRepository) {


  def register(name: String, email: String, password: String) = {
    userRepository.findByName(name).flatMap {
      case Some(_) =>
        throw new KnownSystemException("该用户名已存在")
      case _ =>
        userRepository.findByEmail(email).flatMap {
          case Some(_) =>
            throw new KnownSystemException("该邮箱已存在")
          case _ =>
            val salt = PassWordUtil makeSalt
            val user = User(
              name = name,
              email = email,
              desc = "",
              img_url = "",
              role = "",
              password = PassWordUtil makeHash s"$password$salt",
              salt = salt
            )
            userRepository.save(user).map { _ =>
              user
            }
        }
    }
  }

  def login(name: String, password: String) = {
    userRepository.findByName(name).map {
      case Some(u) =>
        if (!u.password.equals(PassWordUtil makeHash s"$password${u.salt}")) {
          throw new KnownSystemException("密码错误")
        }
        u
      case _ =>
        throw new KnownSystemException("该用户名不存在")
    }
  }


}
