package main

import lib.SlackSupport
import lib.AtcoderSupport
import models.Clarification

object Main extends SlackSupport {
  def main(args: Array[String]): Unit = {
    val atcoder = AtcoderSupport.apply(Consts.atcoderUrl, Consts.atcoderUserId, Consts.atcoderPass)
    val clars = atcoder.getClars()
    var notifiedQuestion = clars.map(clar => clar.clarId)
    var notifiedResponse = clars.collect { case clar: Clarification if !clar.responseText.isEmpty => clar.clarId }

    while (true) {
      Thread.sleep(Consts.clarSleepTime)
      println("check")
      val clars = atcoder.getClars()
      clars.withFilter(clar => !notifiedQuestion.contains(clar.clarId)).foreach(clar => {
        notifiedQuestion = notifiedQuestion :+ clar.clarId
        slackPostMessage(clar.slackQuestionFormat(), Consts.slackWebhookUrl)
      })
      clars.withFilter(clar => !clar.responseText.isEmpty && !notifiedResponse.contains(clar.clarId)).foreach(clar => {
        notifiedResponse = notifiedResponse :+ clar.clarId
        slackPostMessage(clar.slackResponseFormat(), Consts.slackWebhookUrl)
      })
    }
  }
}
