package main

import scalaj.http._
import spray.json._
import org.jsoup._
import org.jsoup.select.Elements
import collection.JavaConversions._
import lib.SlackSupport
import lib.AtcoderSupport

object Main extends SlackSupport with AtcoderSupport {
  def main(args: Array[String]): Unit = {
    implicit val cookie: String = getAtcoderCookie(Const.atcoderUrl, Const.atcoderUserId, Const.atcoderPass)
    var notified = Set[Long]()
    val clars = getClars(Const.atcoderUrl, cookie)
    for (clar <- clars) {
      notified = notified + clar.clarId
    }

    while (true) {
      Thread.sleep(Const.sleepTime)
      println("check")
      val clars = getClars(Const.atcoderUrl, cookie)
      for (clar <- clars) {
        if (!notified.contains(clar.clarId)) {
          notified = notified + clar.clarId
          slackPostMessage(clar.slackMsgFormat(), Const.webhookUrl)
        }
      }
    }
  }
}
