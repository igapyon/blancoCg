*blancoCg

blancoCgは非常にシンプルなソースコード自動生成ライブラリです。
blancoCgを利用すると、とても簡単にソースコードの自動生成を行うことができます。
blancoCgはblanco Frameworkのソースコード自動生成の基盤でもあります。

[ステータス]
 1.Javaソースコード自動生成         : 安定版
 2.C#.NETソースコード自動生成       : 安定版
 3.JavaScriptソースコード自動生成   : 安定版
 4.VB.NETソースコード自動生成       : 開発中
 5.PHPソースコード自動生成 (PHP5)   : 開発中
 6.Rubyソースコード自動生成 (1.8)   : 開発中
 7.Pythonソースコード自動生成 (2.3) : 開発中

[特徴]
  * 低機能であり続けることを目標としています。
    ソースコード自動生成に必要な最低限のインフラとして機能することをゴールとします。
  * 自動生成に必要な情報はバリューオブジェクト(POJO)に蓄えるようになっています。
    blancoCgを利用する側のソースコード共通化が実施しやすくなります。
  * 複数のプログラミング言語 (Java, C#.NET, JavaScript, VB.NET, PHP, Ruby, Python) に対応します。
    複数のプログラミング言語で統一的なソースコード自動生成を実現することができます。
    ※現在のバージョンは Java, C#.NET, JavaScript, VB.NET, PHP, Ruby, Python言語をサポートします。

[開発者]
 1.伊賀敏樹 (Tosiki Iga / いがぴょん): 開発および維持メンテ担当
 2.山本耕司 (Y-moto) : 仕様決定およびリリース判定および開発担当
 3.koyak (http://d.hatena.ne.jp/koyak/): 列挙体および多次元配列で貢献。

[参考リソース]
 1.Java
   http://java.sun.com/docs/books/jls/
   http://java.sun.com/docs/books/jls/second_edition/html/jTOC.doc.html
 3.JavaScript
   http://www.jslint.com/
 4.PHP
   http://www.php.net/manual/ja/
   http://www.icosaedro.it/phplint/phplint-on-line.html
 5.Ruby
   http://www.ruby-lang.org/ja/man/
 6.Python
   http://www.python.jp/doc/2.4/index.html

[ライセンス]
 1.ライセンス として GNU Lesser General Public License と Apache License 2.0 のデュアルライセンスを採用しています。

[依存するライブラリ]
下記のライブラリを利用しています。
※各オープンソース・プロダクトの提供者に感謝します。
 1.blancoCommons
   概要: blanco Framework の構成要素のひとつ。共通ライブラリを担います。
   ライセンス: GNU Lesser General Public License

Mavenの直接依存は`blanco-commons:2.1.1`と`reload4j:1.2.26`です。旧Log4jは推移依存から除外しています。テストではJUnit `4.13.2`とJUnit Vintage Engine `5.14.4`を使用します。


## ビルド

Java 17 以降、Maven 3.6.3 以降、Ant 1.10.18 を使います。Maven で作るclassは `--release 8` でコンパイルし、Java 8 のAPIと互換にします。

```sh
mvn clean package
ant build
```

Ant の `build` は `build-support/ant-dependencies/pom.xml` の固定依存を `target/ant-deps/` に解決してから、メタデータ生成・コンパイル・jar作成を実行します。旧バージョンの依存jarを `lib/` に置く必要はありません。

### Javaの互換性

ビルドにはJDK 17以上を使用し、本体のclassはPOMの`release=8`によりJava 8形式（class major 52）で出力します。

### GitHub Releasesの配布JAR

POMのバージョンと一致する`v<version>`タグをpushすると、GitHub ActionsがJDK 17でテスト・ビルドし、GitHub Releaseへ次のJARを添付します。ソースコードのclass出力はJava 8形式です。

- ライブラリJAR: `blanco-cg-<version>.jar`
- ソースJAR: `blanco-cg-sources-<version>.jar`

例: バージョン`2.2.0`ではタグ`v2.2.0`で`blanco-cg-2.2.0.jar`と`blanco-cg-sources-2.2.0.jar`を作成します。

通常のCIは`devel`へのpush・Pull Requestと手動実行で動作します。ビルド・Release・作業領域の扱いは[運用手順](docs/OPERATIONS.md)を参照してください。
