package models

case class Clarification(
                          clarId: Long,
                          problemTitle: String,
                          problemUrl: Option[String],
                          userId: String,
                          userUrl: Option[String],
                          clarText: String,
                          responseText: String,
                          isPublic: String,
                          replyUrl: Option[String]) {

  def slackQuestionFormat(): String = {
    val problem = problemUrl.fold(problemTitle)(url => s"<${url}|${problemTitle}>")
    val user = userUrl.fold(userId)(url => s"<${url}|${userId}>")
    val reply = replyUrl.fold("")(url => s"<${url}|質問に回答する>")
    s"""|[Clar No.${clarId}]
        |問題名：${problem}
        |ユーザ名：${user}
        |質問：${clarText}
        |${reply}""".stripMargin
  }

  def slackResponseFormat(): String = {
    val problem = problemUrl.fold(problemTitle)(url => s"<${url}|${problemTitle}>")
    val user = userUrl.fold(userId)(url => s"<${url}|${userId}>")
    val reply = replyUrl.fold("")(url => s"<${url}|回答を修正する>")
    s"""|[Clar No.${clarId} に回答しました]
        |問題名：${problem}
        |ユーザ名：${user}
        |質問：${clarText}
        |回答：${responseText}
        |全体公開：${isPublic}
        |${reply}""".stripMargin
  }
}
