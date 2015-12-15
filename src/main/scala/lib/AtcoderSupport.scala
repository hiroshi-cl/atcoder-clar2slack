package lib

import models.Clarification
import scalaj.http.Http
import scalaj.http.HttpResponse
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import collection.JavaConversions._

class AtcoderSupport private(atcoderUrl: String, userId: String, password: String) {
  private lazy val cookies = {
    val url = atcoderUrl + AtcoderSupport.loginPath
    val response: HttpResponse[String] = Http(url)
      .timeout(connTimeoutMs = AtcoderSupport.httpConnTimeout, readTimeoutMs = AtcoderSupport.httpReadTimeout)
      .postForm(Seq("name" -> userId, "password" -> password)).asString
    response.cookies
  }

  def isOwner(): Boolean = {
    cookies.exists(cookie => cookie.getName == "__privilege" && cookie.getValue == "owner")
  }

  def getClars(): List[Clarification] = {
    val url = atcoderUrl + AtcoderSupport.clarPath
    val response: HttpResponse[String] = Http(url)
      .timeout(connTimeoutMs = AtcoderSupport.httpConnTimeout, readTimeoutMs = AtcoderSupport.httpReadTimeout)
      .cookies(cookies).asString
    val doc = Jsoup.parse(response.body)
    val tbody = doc.select("tbody")
    val trs = if (tbody.isEmpty) new Elements() else tbody.head.select("tr")
    val clars = trs.map(tr => {
      val fields = tr.select("td")

      val problem = fields.get(0).select("a")
      val user = fields.get(1).select("a")
      val question = fields.get(2)
      val response = fields.get(3)
      val isPublic = fields.get(4)

      val reply = fields.get(7).select("a")

      val pattern = """([0-9]+)""".r
      val clarId = pattern.findFirstIn(reply.attr("href")).getOrElse("-1").toLong

      Clarification(
        clarId = clarId,
        problemTitle = problem.text,
        problemUrl = Some(atcoderUrl + problem.attr("href")),
        userId = user.text,
        userUrl = Some(atcoderUrl + user.attr("href")),
        clarText = question.text,
        responseText = response.text,
        isPublic = isPublic.text,
        replyUrl = Some(atcoderUrl + reply.attr("href")))
    }).toList

    clars
  }
}

object AtcoderSupport {
  private val loginPath = "/login"
  private val clarPath = "/clarifications"
  private val httpConnTimeout = 5000
  private val httpReadTimeout = 5000

  def apply(atcoderUrl: String, userId: String, password: String) = new AtcoderSupport(atcoderUrl, userId, password)
}