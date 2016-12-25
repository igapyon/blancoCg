/*
 * blanco Framework
 * Copyright (C) 2004-2006 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.cg.transformer;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import blanco.cg.valueobject.BlancoCgClass;
import blanco.cg.valueobject.BlancoCgInterface;
import blanco.cg.valueobject.BlancoCgSourceFile;
import blanco.commons.util.BlancoFileUtil;
import blanco.commons.util.BlancoStringUtil;

/**
 * Delphi スタイルの抽象トランスフォーマーです。
 * 
 * @author YAMAMOTO Koji
 */
public abstract class AbstractBlancoCgDelphiStyleTransformer extends
        AbstractBlancoCgTransformer {
    /**
     * デバッグモードで動作させるかどうか。
     */
    private static final boolean IS_DEBUG = true;

    /**
     * ソースファイル・バリューオブジェクトをDelphiソースコードに変換して出力先ディレクトリに出力します。
     * 
     * このAPIではパッケージ構造をディレクトリ構造として考慮します。
     * 
     * @param argSourceFile
     *            ソースファイル・バリューオブジェクト。
     * @param outputDirectory
     *            出力先ルートディレクトリ。
     */
    public void transform(final BlancoCgSourceFile argSourceFile,
            final File outputDirectory) {
        if (argSourceFile == null) {
            throw new IllegalArgumentException("ソースファイルにnullが与えられました。処理中断します。");
        }
        if (outputDirectory == null) {
            throw new IllegalArgumentException(
                    "出力先ルートディレクトリにnullが与えられました。処理中断します。");
        }

        if (outputDirectory.exists() == false) {
            if (outputDirectory.mkdirs() == false) {
                throw new IllegalArgumentException("出力先ルートディレクトリ["
                        + outputDirectory.getAbsolutePath()
                        + "]が存在しなかったので作成しようとしましたがディレクトリ作成に失敗しました。処理中断します。");
            }
        }
        if (outputDirectory.isDirectory() == false) {
            throw new IllegalArgumentException("出力先ルートディレクトリにディレクトリではないファイル["
                    + outputDirectory.getAbsolutePath() + "]が与えられました。処理中断します。");
        }

        if (argSourceFile.getName() == null) {
            // ファイル名が確定していないので、クラス名またはインタフェース名から導出します。
            decideFilenameFromClassOrInterfaceName(argSourceFile);
        }

        try {
            // パッケージ名からディレクトリ名へと変換。
            String strSubdirectory = BlancoStringUtil.replaceAll(
                    BlancoStringUtil.null2Blank(argSourceFile.getPackage()),
                    '.', '/');
            if (strSubdirectory.length() > 0) {
                // サブディレクトリが存在する場合にのみスラッシュを追加します。
                strSubdirectory = "/" + strSubdirectory;
            }

            final File targetPackageDirectory = new File(outputDirectory
                    .getAbsolutePath()
                    + strSubdirectory);
            if (targetPackageDirectory.exists() == false) {
                if (targetPackageDirectory.mkdirs() == false) {
                    throw new IllegalArgumentException("出力先のパッケージディレクトリ["
                            + targetPackageDirectory.getAbsolutePath()
                            + "]の生成に失敗しました。");
                }
            }

            // 出力先のファイルを確定します。
            final File fileTarget = new File(targetPackageDirectory
                    .getAbsolutePath()
                    + "/" + argSourceFile.getName() + getSourceFileExt());

            // 実際のソースコード出力処理を行います。
            final ByteArrayOutputStream outStream = new ByteArrayOutputStream();

            // 自動生成するソースコードのエンコーディング指定機能
            OutputStreamWriter streamWriter = null;
            if (BlancoStringUtil.null2Blank(argSourceFile.getEncoding())
                    .length() == 0) {
                streamWriter = new OutputStreamWriter(outStream);
            } else {
                streamWriter = new OutputStreamWriter(outStream, argSourceFile
                        .getEncoding());
            }

            final BufferedWriter writer = new BufferedWriter(streamWriter);
            try {
                transform(argSourceFile, writer);
                writer.flush();
                outStream.flush();

                switch (BlancoFileUtil.bytes2FileIfNecessary(outStream
                        .toByteArray(), fileTarget)) {
                case 0:
                    if (IS_DEBUG) {
                        // デバッグ時のみスキップを標準出力。
                        System.out.println(CMDLINE_PREFIX + "skip  : "
                                + fileTarget.getAbsolutePath());
                    }
                    break;
                case 1:
                    System.out.println(CMDLINE_PREFIX + "create: "
                            + fileTarget.getAbsolutePath());
                    break;
                case 2:
                    System.out.println(CMDLINE_PREFIX + "update: "
                            + fileTarget.getAbsolutePath());
                    break;
                }
            } finally {
                // ByteArrayOutputStreamのインスタンスは writerのクローズによって
                // ストリームチェインの仕組み上 自動的にクローズされます。

                if (writer != null) {
                    writer.close();
                }
            }
        } catch (IOException ex) {
            throw new IllegalArgumentException("ソースコードを出力する過程で例外が発生しました。"
                    + ex.toString());
        }
    }

    /**
     * ソースコードのリストを整形します。
     * 
     * Delphi言語用の整形を行います。
     * 
     * なお、この処理のなかで { や } は特別な意味を持っています。行末コメントなどが入ると期待する動作ができません。<br>
     * TODO 中カッコを文末に付与する、などのフォーマットなどは未実装です。
     * 
     * @param argSourceLines
     *            ソースコード行リスト。
     */
    protected void formatSource(final List<java.lang.String> argSourceLines) {
        int sourceIndent = 0;
        boolean isInterface = false;
        boolean isImplementation = false;

        for (int index = 0; index < argSourceLines.size(); index++) {
            String strLine = argSourceLines.get(index);
            // 前後の空白は、あらかじめ除去します。
            strLine = strLine.trim();
            if (strLine.length() == 0) {
                // 空行です。
            } else {
                boolean isBeginIndent = false;
                boolean isEndIndent = false;

                // まずは開始文字列の判定を行います。
                // ※開始文字列と終了文字列とは別個に判定する必要があります。
                if (strLine.startsWith("if ")) {
                    // ブロック開始と見なして字下げを予約します。
                    isBeginIndent = true;
                } else if (strLine.startsWith("for ")) {
                    // ブロック開始と見なして字下げを予約します。
                    isBeginIndent = true;
                } else if (strLine.startsWith("while ")) {
                    // ブロック開始と見なして字下げを予約します。
                    isBeginIndent = true;
                } else if (strLine.startsWith("begin")) {
                    // ブロック開始と見なして字下げを予約します。
                    isBeginIndent = true;
                } else if (strLine.startsWith("end")) {
                    // ブロック終了と見なして字下げします。
                    isEndIndent = true;
                } else if (strLine.startsWith("else")) {
                    // ブロック終了と見なして字下げします。
                    isEndIndent = true;
                } else if (strLine.equals("Next")
                        || strLine.startsWith("Next ")) {
                    // ブロック終了と見なして字下げします。
                    isEndIndent = true;
                } else if (strLine.indexOf("type") == 0
                        || strLine.indexOf("interface") == 0
                        || strLine.indexOf("implementation") == 0) {
                    // ブロック終了と見なして字下げします。
                    isBeginIndent = true;
                    isEndIndent = true;
                } else if (strLine.indexOf("unit ") >= 0
                        || strLine.indexOf("class(") >= 0
                        || strLine.indexOf("interface ") >= 0
                        || strLine.indexOf("implementation ") >= 0) {
                    // Endより後で判定しているのがポイントです。
                    // ブロック開始と見なして字下げを予約します。
                    isBeginIndent = true;
                } else if (strLine.equals("published")
                        || strLine.equals("public")
                        || strLine.equals("private")){
                    isBeginIndent = true;
                    isEndIndent = true;
                }

                // 途中に挟まるであろうIfを判定します。
                if (strLine.indexOf(" if ") >= 0) {
                    // ブロック開始と見なして字下げを予約します。
                    isBeginIndent = true;
                }

                if (isEndIndent) {
                    // フラグ一回につき、インデント一個を反映します。
                    sourceIndent--;
                }

                // インデントを実施します。
                for (int indexIndent = 0; indexIndent < sourceIndent; indexIndent++) {
                    // 4タブで字下げします。
                    strLine = "    " + strLine;
                }
                if (isBeginIndent) {
                    sourceIndent++;
                }

                // 更新後の行イメージでリストを更新します。
                argSourceLines.set(index, strLine);
            }
        }
    }
}
