import scalaj.http._
import spray.json._
import org.jsoup._
import collection.JavaConversions._

object Main {
  var notified = Set[String]()

  def main(args: Array[String]): Unit = {
    implicit val cookie: String = atcoderLogin()
    getClars()

    while (true) {
      Thread.sleep(Const.sleepTime)
      println("check")
      val clars = getClars()
      for (text <- clars) {
        slackPostMessage(text)
      }
    }
  }

  def atcoderLogin(): String = {
    val url = s"${Const.atcoderUrl}/login"
    val response: HttpResponse[String] = Http(url).postForm(Seq("name"->Const.atcoderName, "password"->Const.atcoderPass)).asString
    response.headers("Set-Cookie").toString.replace(",", ";")
  }

  def getClars()(implicit cookie: String): List[String] = {
    var clars = List.empty[String]
    val url = s"${Const.atcoderUrl}/clarifications"
    val response: HttpResponse[String] = Http(url).headers(Seq("Cookie"->cookie)).asString
    val doc = Jsoup.parse(response.body)
    val trs = doc.select("tbody").head.select("tr")

    for (tr <- trs) {
      val fields = tr.select("td")

      val problem = fields.get(0).select("a")
      val user = fields.get(1).select("a")
      val question = fields.get(2)
      val reply = fields.get(7).select("a")

      val pattern = """([0-9]+)""".r
      val clarId = pattern.findFirstIn(reply.attr("href").toString).getOrElse("-1")
      val text = s"""|[Clar通知]
                     |問題名：<${Const.atcoderUrl}${problem.attr("href")}|${problem.text}>
                     |ユーザ名：<${Const.atcoderUrl}${user.attr("href")}|${user.text}>
                     |質問：${question.text}
                     |<${Const.atcoderUrl}${reply.attr("href")}|質問に回答する>""".stripMargin

      if (!notified.contains(clarId)) {
        notified = notified + clarId
        clars = text :: clars
      }
    }

    clars
  }

  def slackPostMessage(text: String): Unit = {
    val payload: String = JsObject("text"->JsString(text)).toString
    Http(Const.webhookUrl).postForm(Seq("payload"->payload)).asString
    println("post: " + payload)
  }
}
