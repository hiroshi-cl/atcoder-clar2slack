package lib

import models.Clarification
import scalaj.http.Http
import scalaj.http.HttpResponse
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import collection.JavaConversions._

trait AtcoderSupport {
  private val loginPath = "/login"
  private val clarPath = "/clarifications"

  private val httpConnTimeout = 5000
  private val httpReadTimeout = 5000

  def getAtcoderCookie(atcoderUrl: String, userId: String, password: String): String = {
    val url = atcoderUrl + loginPath
    val response: HttpResponse[String] = Http(url)
      .timeout(connTimeoutMs = httpConnTimeout, readTimeoutMs = httpReadTimeout)
      .postForm(Seq("name" -> userId, "password" -> password)).asString
    val cookie = response.headers("Set-Cookie").toString.replace(",", ";")
    cookie
  }

  def getClars(atcoderUrl: String)(implicit cookie: String): List[Clarification] = {
    val url = atcoderUrl + clarPath
    val response: HttpResponse[String] = Http(url)
      .timeout(connTimeoutMs = httpConnTimeout, readTimeoutMs = httpReadTimeout)
      .headers(Seq("Cookie" -> cookie)).asString
    val doc = Jsoup.parse(response.body)
    val tbody = doc.select("tbody")
    val trs = if (tbody.isEmpty()) new Elements() else tbody.head.select("tr")
    val clars = trs.map(tr => {
      val fields = tr.select("td")

      val problem = fields.get(0).select("a")
      val user = fields.get(1).select("a")
      val question = fields.get(2)
      val reply = fields.get(7).select("a")

      val pattern = """([0-9]+)""".r
      val clarId = pattern.findFirstIn(reply.attr("href").toString).getOrElse("-1").toLong

      Clarification(
        clarId = clarId,
        problemTitle = problem.text,
        problemUrl = Some(atcoderUrl + problem.attr("href")),
        userId = user.text,
        userUrl = Some(atcoderUrl + user.attr("href")),
        clarText = question.text,
        replyUrl = Some(atcoderUrl + reply.attr("href")))
    }).toList

    clars
  }
}