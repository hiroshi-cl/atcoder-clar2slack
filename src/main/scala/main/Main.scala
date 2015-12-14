package main

import lib.SlackSupport
import lib.AtcoderSupport

object Main extends SlackSupport {
  def main(args: Array[String]): Unit = {
    val atcoder = AtcoderSupport.apply(Consts.atcoderUrl, Consts.atcoderUserId, Consts.atcoderPass)
    val clars = atcoder.getClars()
    var notified = clars.map(clar => clar.clarId)

    while (true) {
      Thread.sleep(Consts.clarSleepTime)
      println("check")
      val clars = atcoder.getClars()
      clars.withFilter(clar => !notified.contains(clar.clarId)).foreach(clar => {
        notified = notified :+ clar.clarId
        slackPostMessage(clar.slackMsgFormat(), Consts.slackWebhookUrl)
      })
    }
  }
}
