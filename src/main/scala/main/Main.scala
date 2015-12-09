package main

import lib.SlackSupport
import lib.AtcoderSupport

object Main extends SlackSupport with AtcoderSupport {
  def main(args: Array[String]): Unit = {
    implicit val cookie: String = getAtcoderCookie(Const.atcoderUrl, Const.atcoderUserId, Const.atcoderPass)

    val clars = getClars(Const.atcoderUrl)
    var notified = clars.map(clar => clar.clarId)

    while (true) {
      Thread.sleep(Const.sleepTime)
      println("check")
      val clars = getClars(Const.atcoderUrl)
      clars.withFilter(clar => !notified.contains(clar.clarId)).foreach(clar => {
        notified = notified :+ clar.clarId
        slackPostMessage(clar.slackMsgFormat(), Const.webhookUrl)
      })
    }
  }
}
