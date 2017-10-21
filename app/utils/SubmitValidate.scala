package utils

import exceptions.KnownSystemException

/**
  * Created by sheep3 on 2017/10/21.
  */
object SubmitValidate {

  def checkRegister(name :String, email: String, password: String): Unit ={
    if (name.length > 20 || name.length < 1) {
      throw new KnownSystemException("用户名长度必须在1~20以内")
    }
    if (email.length > 60 || password.length < 2) {
      throw new KnownSystemException("邮箱长度必须在2~60以内")
    }
    if ("""^([\w-_]+(?:\.[\w-_]+)*)@((?:[a-z0-9]+(?:-[a-zA-Z0-9]+)*)+\.[a-z]{2,6})$""".r.findFirstIn(email).isEmpty){
      throw new KnownSystemException("邮箱格式不正确")
    }
    if (password.length > 30 || password.length < 6) {
      throw new KnownSystemException("密码长度必须在6~30以内")
    }
  }

  def checkLogin(name :String, password: String): Unit ={
    if (name.length > 20 || name.length < 1) {
      throw new KnownSystemException("用户名长度必须在1~20以内")
    }
    if (password.length > 30 || password.length < 6) {
      throw new KnownSystemException("密码长度必须在6~30以内")
    }
  }

  def checkTopic(name: String, desc: String): Unit ={
    if (name.length > 20 || name.length < 1) {
      throw new KnownSystemException("主题名称长度必须在1~20以内")
    }
    if (desc.length > 140) {
      throw new KnownSystemException("主题描述长度必须在140以内")
    }
  }

  def checkCard(content: String): Unit ={
    if (content.length > 140) {
      throw new KnownSystemException("打卡内容长度必须在140以内")
    }
  }

}
