import scalaj.http._
import spray.json._
import org.jsoup._
import collection.JavaConversions._

object Main {
  val atcoderName = ""
  val atcoderPass = ""
  val atcoderUrl = ""
  val webhookUrl = ""
  val sleepTime = 10000

  var notified = Set[String]()

  def main(args: Array[String]): Unit = {
    val cookie: String = atcoderLogin()
    getClars(cookie)

    while (true) {
      Thread.sleep(sleepTime)
      println("check")
      val clars = getClars(cookie)
      for (text <- clars) {
        slackPostMessage(text)
      }
    }
  }

  def atcoderLogin(): String = {
    val url = s"${atcoderUrl}/login"
    val response: HttpResponse[String] = Http(url).postForm(Seq("name"->atcoderName, "password"->atcoderPass)).asString
    response.headers("Set-Cookie").toString.replace(",", ";")
  }

  def getClars(cookie: String): List[String] = {
    var clars = List.empty[String]
    val url = s"${atcoderUrl}/clarifications"
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
                     |問題名：<${atcoderUrl}${problem.attr("href")}|${problem.text}>
                     |ユーザ名：<${atcoderUrl}${user.attr("href")}|${user.text}>
                     |質問：${question.text}
                     |<${atcoderUrl}${reply.attr("href")}|質問に回答する>""".stripMargin

      if (!notified.contains(clarId)) {
        notified = notified + clarId
        clars = text :: clars
      }
    }

    clars
  }

  def slackPostMessage(text: String): Unit = {
    val payload: String = JsObject("text"->JsString(text)).toString
    val url: String = webhookUrl
    Http(url).postForm(Seq("payload"->payload)).asString
    println("post: " + payload)
  }
}
