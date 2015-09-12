# atcoder-clar2slack

## これは何

* AtCoderのクラーが来たらSlackに通知します。

## 使い方

* sbtのインストール
* `Main.scala` を開いてセッティング
  * `atcoderName` : Clarを見る権限を持つユーザのID
  * `atcoderPass` : Clarを見る権限を持つユーザのパスワード
  * `atcoderUrl` : コンテストのURL(例 - "http://arc001.contest.atcoder.jp")
  * `webhookUrl` : Slackの[Incoming Webhooks](https://my.slack.com/services/new/incoming-webhook/)を登録して、Webhook URLをペースト
  * `sleepTime` : クラーを何ミリ秒おきに参照するかを設定できます
* `sbt run` で実行

