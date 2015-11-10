package models

case class Clarification(
    clarId: Long,
    problemTitle: String,
    problemUrl: Option[String],
    userId: String,
    userUrl: Option[String],
    clarText: String,
    replyUrl: Option[String]) {

  def slackMsgFormat(): String = {
    val problem = problemUrl match {
      case Some(url) => s"<${url}|${problemTitle}>"
      case None      => problemTitle
    }
    val user = userUrl match {
      case Some(url) => s"<${url}|${userId}>"
      case None      => userId
    }
    val reply = replyUrl match {
      case Some(url) => s"<${url}|質問に回答する>"
      case None      => ""
    }
    val text = s"""|[Clar No.${clarId}]
                   |問題名：${problem}
                   |ユーザ名：${user}
                   |質問：${clarText}
                   |${reply}""".stripMargin

    text
  }
}
