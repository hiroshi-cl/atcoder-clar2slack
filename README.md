# atcoder-clar2slack

## これは何

* AtCoderのクラーが来たらSlackに通知します。

## 使い方

* sbtのインストール
* `Const.scala` を開いてセッティング
  * `atcoderName` : コンテストの管理権限をもつユーザのID
  * `atcoderPass` : コンテストの管理権限をもつユーザのパスワード
  * `atcoderUrl` : コンテストのURL(例 - "http://arc001.contest.atcoder.jp")
  * `webhookUrl` : Slackの[Incoming Webhooks](https://my.slack.com/services/new/incoming-webhook/)を登録して、Webhook URLをペースト
  * `sleepTime` : クラーを何ミリ秒おきに参照するかを設定できます
* `sbt run` で実行

