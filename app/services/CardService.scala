package services

import javax.inject.{Inject, Singleton}

import exceptions.KnownSystemException
import models.{Card, CardRepository, TopicRepository}
import utils.DateUtil

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps

/**
  * Created by sheep3 on 2017/10/22.
  */
@Singleton
class CardService @Inject()(cardRepository: CardRepository, topicRepository: TopicRepository) {

  def list(page: Int = 0, pageSize: Int = 10, filter: String = "%") = {
    if (pageSize > 20) {
      cardRepository.list(page, 10, filter)
    } else {
      cardRepository.list(page, pageSize, filter)
    }
  }

  def save(topic_id: Long, user_id: Long, content: String): Future[Int] = {
    topicRepository.findByTopicId(topic_id) flatMap {
      case Some(_) =>
        cardRepository.findFirstByTopicAndUser(topic_id, user_id).flatMap {
          case Some(c) =>
            if (DateUtil.today(c.create_time.get.getTime)) {
              throw new KnownSystemException("这个主题你今天已经打过卡啦！")
            } else {
              realSave(topic_id, user_id, content)
            }
          case _ =>
            realSave(topic_id, user_id, content)
        }
      case _ =>
        throw new KnownSystemException("该主题不存在！")
    }
  }

  def realSave(topic_id: Long, user_id: Long, content: String) = {
    cardRepository.save(new Card(
      topic_id = topic_id,
      user_id = user_id,
      content = content
    ))
  }

}
