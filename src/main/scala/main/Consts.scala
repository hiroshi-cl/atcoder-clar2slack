package main

import com.typesafe.config.ConfigFactory

object Consts {
  private val conf = ConfigFactory.load()
  val atcoderUrl = conf.getString("atcoder.url")
  val atcoderUserId = conf.getString("atcoder.userId")
  val atcoderPass = conf.getString("atcoder.password")
  val slackWebhookUrl = conf.getString("slack.webhookUrl")
  val clarSleepTime = conf.getInt("clar.sleepTime")
}
