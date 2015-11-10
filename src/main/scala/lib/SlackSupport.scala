package lib

import scalaj.http.Http
import spray.json.JsObject
import spray.json.JsString

trait SlackSupport {
  def slackPostMessage(text: String, webhookUrl: String): Unit = {
    val payload: String = JsObject("text" -> JsString(text)).toString
    Http(webhookUrl).postForm(Seq("payload" -> payload)).asString
    println("post: " + payload)
  }
}