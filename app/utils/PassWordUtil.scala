package utils

import java.security.MessageDigest
import java.util.UUID

/**
  * Created by sheep3 on 2017/10/21.
  */
object PassWordUtil {

  def makeSalt: String = {
    UUID.randomUUID().toString.substring(0, 5)
  }

  def makeHash(str: String): String = {
    val md5Digest: MessageDigest = MessageDigest.getInstance("MD5")
    val hash = md5Digest.digest(str.getBytes("UTF-8"))
    hash.map("%02x".format(_)).mkString.toUpperCase
  }

}
