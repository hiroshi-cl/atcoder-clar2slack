# atcoder-clar2slack

## これは何

* AtCoderのクラーが来たらSlackに通知します。
* これは、コンテスト管理者用のツールなので、コンテスタントは使わない方がいいです。

## 使い方

* [sbt](http://www.scala-sbt.org/download.html)のインストール
* `src/main/resources/application.conf` を開いてセッティング
  * `atcoder.url` : コンテストのURL(例 - "http://arc001.contest.atcoder.jp")
  * `atcoder.userId` : コンテストの管理権限をもつユーザのID
  * `atcoder.password` : コンテストの管理権限をもつユーザのパスワード
  * `slack.webhookUrl` : Slackの[Incoming Webhooks](https://my.slack.com/services/new/incoming-webhook/)を登録して、Webhook URLをペースト
  * `clar.sleepTime` : クラーを何ミリ秒おきに参照するかを設定できます
* `sbt run` で実行
