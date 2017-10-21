package utils

import java.text.SimpleDateFormat
import java.util.Date

/**
  * Created by sheep3 on 2017/10/22.
  */
object DateUtil {

  def today(time: Long): Boolean = {
    thisTime(time, "yyyy-MM-dd")
  }

  def thisMonth(time: Long): Boolean = {
    thisTime(time, "yyyy-MM")
  }

  def thisTime(time: Long, pattern: String): Boolean = {
    val date = new Date(time)
    val sdf = new SimpleDateFormat(pattern)
    val param = sdf.format(date)
    val now = sdf.format(new Date())
    if (param.equals(now)) {
      true
    } else false
  }
}
