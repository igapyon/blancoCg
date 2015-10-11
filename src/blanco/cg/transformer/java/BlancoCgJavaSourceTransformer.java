/*
 * blanco Framework
 * Copyright (C) 2004-2006 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.cg.transformer.java;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import blanco.cg.transformer.AbstractBlancoCgJavaStyleTransformer;
import blanco.cg.valueobject.BlancoCgSourceFile;

/**
 * blancoCgのバリューオブジェクトからソースコードを自動生成するトランスフォーマーのエントリポイントです。
 * 
 * BlancoCgTransformerFactoryを経由して生成することを推奨します。<br>
 * このトランスフォーマーではバリューオブジェクトをJavaソースコードへと変換します。
 * 
 * @author IGA Tosiki
 */
public class BlancoCgJavaSourceTransformer extends
        AbstractBlancoCgJavaStyleTransformer {

    /**
     * ソースファイル・バリューオブジェクトをJavaソースコードに変換してライターに出力します。
     * 
     * このAPIではパッケージ構造をディレクトリ構造とは考慮しません。この処理の中ではライターに向けて出力するだけです。
     * 
     * @param argSourceFile
     *            ソースファイル・バリューオブジェクト。
     * @param argWriter
     *            出力先のライター。
     * @throws 入出力例外が発生した場合
     *             。
     */
    public void transform(final BlancoCgSourceFile argSourceFile,
            final BufferedWriter argWriter) throws IOException {
        if (argSourceFile == null) {
            throw new IllegalArgumentException("ソースファイルにnullが与えられました。処理中断します。");
        }
        if (argWriter == null) {
            throw new IllegalArgumentException("出力先ライターにnullが与えられました。処理中断します。");
        }

        final List<java.lang.String> sourceLines = new BlancoCgSourceFileJavaSourceExpander()
                .transformSourceFile(argSourceFile);

        // ソースコードを整形します。
        formatSource(sourceLines);

        // ソースコードをライタへと出力します。
        source2Writer(sourceLines, argWriter);

        // 念のためフラッシュを実施。
        argWriter.flush();
    }

    /**
     * ソースコードに付けられる拡張子を取得します。
     * 
     * @return ソースコードに付けられる拡張子。
     */
    protected String getSourceFileExt() {
        return ".java";
    }
}
