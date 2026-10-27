# blancoCgのビルドとRelease運用

## バージョンとJavaの互換性

本体バージョンは`pom.xml`のproject versionを正とします。Antの`build.xml`もPOMからバージョンを読み取るため、MavenとAntの配布物に同じバージョンを使用します。

ビルドにはJDK 17以上、Maven 3.6.3以上、Antを使う場合は1.10.18を使用します。MavenとAntのコンパイル設定は`release=8`で、本体のclass出力はJava 8形式（class major 52）です。

## ローカルビルド

```sh
mvn --batch-mode --no-transfer-progress clean verify -Dgpg.skip=true
```

テストに続き、`target/`へ通常JAR・sources JAR・Javadoc JARを生成します。sources JARには通常のJavaソースと`blanco/main`の生成済みソースを含めます。実行時のリソースバンドルを読み込めるよう、`blanco/main`のpropertiesも通常JARへ格納します。

POMにはMaven配布用のGPG署名設定があるため、署名鍵を使用しないローカル検証とGitHub Actionsでは`-Dgpg.skip=true`を指定します。

Antの生成・コンパイル・配布物作成を実行する場合は次を使います。

```sh
ant build
```

Antは`build-support/ant-dependencies/pom.xml`の固定依存を`target/ant-deps/`へ解決します。最終的なAnt版JARは`lib.ant/blancocg-<version>.jar`、ソースZIPは`blancoCg-<version>-src.zip`です。GitHub Releaseへの添付にはMaven版JARを使用します。

## 通常のGitHub Actions

[`build.yml`](../.github/workflows/build.yml)は`devel`へのpush、`devel`宛てのPull Request、手動実行で動作します。Temurin JDK 17を使用してテストとビルドを実行します。

## タグ起点のGitHub Release

[`release.yml`](../.github/workflows/release.yml)は`v*`タグのpushで動作します。タグのソースを取得し、タグの`v`以降とPOMのproject versionが完全一致することを確認してから、テストとビルドを実行します。

成功すると、そのタグのGitHub Releaseを作成または更新し、次の2つのJARを添付します。同名の添付ファイルがある場合は置き換えます。

- `blanco-cg-<version>.jar`：依存ライブラリを含まないライブラリJAR
- `blanco-cg-sources-<version>.jar`：ソースJAR

例えばproject versionが`2.2.0`の場合、対応するタグは`v2.2.0`です。workflowを含むコミットに対してタグを作成し、originへpushすると配布を開始します。失敗時はActionsの実行ログを確認してください。

タグとPOMの照合、タグのソースからのビルド、ReleaseへのJAR添付は、[igapyonv3のRelease workflow](https://github.com/igapyon/igapyonv3/blob/bf9e9e509bdfee754975ded625ad320ce3cd837f/.github/workflows/release.yml)を基準にしています。

## ローカル作業領域

`workplace/`はローカル作業用です。`.gitignore`で除外しており、生成ログやPR下書きをGitへ登録しません。既に追跡されているファイルはignore設定だけでは除外されないため、`git ls-files workplace`で確認します。

## 整備時の確認（2026-10-27）

生成物のない作業コピーでMavenの`clean verify -Dgpg.skip=true`が成功し、11テストが成功しました。Release配布用JARには本体133class（すべてmajor 52）と2つのリソースバンドルがあり、sources JARには通常ソースと生成済みソースがあることを確認しました。配布用JARからのリソース読込・Javaソース生成と、生成ソースのコンパイルも成功しました。

Ant 1.10.18による生成・コンパイル・パッケージ作成も成功し、POMから取得した`2.2.0`のJARとソースZIPを確認しました。Javadocプラグインの既存`excludes`設定に対する警告と、Ant生成ツールのLog4j provider未検出ログは残っています。
