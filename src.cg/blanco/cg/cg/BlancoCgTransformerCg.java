/*
 * blanco Framework
 * Copyright (C) 2004-2006 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.cg.cg;

import java.io.File;
import java.util.List;

import blanco.cg.BlancoCgObjectFactory;
import blanco.cg.transformer.BlancoCgTransformerFactory;
import blanco.cg.valueobject.BlancoCgInterface;
import blanco.cg.valueobject.BlancoCgMethod;
import blanco.cg.valueobject.BlancoCgSourceFile;

/**
 * blancoCgのBlancoCgTransformerを自動生成するためのクラスです。
 * 
 * ソースコード自動生成ライブラリ blancoCg そのものの一部を自動生成します。
 * 
 * @author IGA Tosiki
 */
public class BlancoCgTransformerCg {
    /**
     * blancoCg上のオブジェクトを生成するためのファクトリ。
     */
    private final BlancoCgObjectFactory cgFactory = BlancoCgObjectFactory
            .getInstance();

    /**
     * ソースコードの展開を行います。
     * 
     * @param targetDirectory
     *            ソースコード出力先ルートディレクトリ。
     */
    public void process(final File targetDirectory) {
        // ソースファイルヘッダを展開します。
        final BlancoCgSourceFile cgSourceFile = expandSourceFileHeader("blanco.cg");

        // インタフェースを展開します。
        final BlancoCgInterface cgInterface = expandInterface(cgSourceFile);

        // メソッドを展開します。
        cgInterface.getMethodList().add(expandTransformMethod());
        cgInterface.getMethodList().add(expandTransform2Method());

        // バリューオブジェクトを入力として、ソースコードを出力します。
        BlancoCgTransformerFactory.getJavaSourceTransformer().transform(
                cgSourceFile, targetDirectory);
    }

    /**
     * ソースファイルヘッダーを展開します。
     * 
     * @param argPackage
     *            ソースコードが所属するパッケージ。
     * @return ソースファイルオブジェクト。
     */
    public BlancoCgSourceFile expandSourceFileHeader(final String argPackage) {
        final BlancoCgSourceFile cgSourceFile = cgFactory.createSourceFile(
                argPackage, "blanco Framework");
        cgSourceFile.setEncoding("UTF-8");

        final List<java.lang.String> sourceDesc = cgSourceFile.getLangDoc()
                .getDescriptionList();

        sourceDesc.add("Copyright (C) 2004-2006 IGA Tosiki");
        sourceDesc.add("");
        sourceDesc
                .add("This library is free software; you can redistribute it and/or");
        sourceDesc
                .add("modify it under the terms of the GNU Lesser General Public");
        sourceDesc
                .add("License as published by the Free Software Foundation; either");
        sourceDesc
                .add("version 2.1 of the License, or (at your option) any later version.");

        return cgSourceFile;
    }

    /**
     * インタフェースを展開します。
     * 
     * @param cgSourceFile
     *            ソースファイルオブジェクト。
     * @return インタフェースオブジェクト。
     */
    private BlancoCgInterface expandInterface(
            final BlancoCgSourceFile cgSourceFile) {
        final BlancoCgInterface cgInterface = cgFactory.createInterface(
                "BlancoCgTransformer",
                "blancoCgのバリューオブジェクトからソースコードを自動生成するトランスフォーマーのインタフェースです。");
        cgSourceFile.getInterfaceList().add(cgInterface);

        cgInterface.getLangDoc().getTagList().add(
                cgFactory.createLangDocTag("author", null, "IGA Tosiki"));
        cgInterface.getLangDoc().getDescriptionList().add(
                "個別の言語用のソースコード自動生成部分は、このインタフェースを実装して実現されます。");

        return cgInterface;
    }

    /**
     * transformメソッドを展開します。
     * 
     * @return メソッドオブジェクト。
     */
    private BlancoCgMethod expandTransformMethod() {
        final BlancoCgMethod cgMethod = cgFactory.createMethod("transform",
                "ソースファイル・バリューオブジェクトをJavaソースコードに変換して出力先ディレクトリに出力します。");

        cgMethod.getLangDoc().getDescriptionList().add(
                "このAPIではパッケージ構造をディレクトリ構造として考慮します。");
        cgMethod.getParameterList().add(
                cgFactory.createParameter("sourceFile",
                        "blanco.cg.valueobject.BlancoCgSourceFile",
                        "ソースファイル・バリューオブジェクト。"));
        cgMethod.getParameterList().add(
                cgFactory.createParameter("outputDirectory", "java.io.File",
                        "出力先ルートディレクトリ。"));

        return cgMethod;
    }

    /**
     * transformメソッドを展開します。
     * 
     * @return メソッドオブジェクト。
     */
    public BlancoCgMethod expandTransform2Method() {
        final BlancoCgMethod cgMethod = cgFactory.createMethod("transform",
                "ソースファイル・バリューオブジェクトをソースコードに変換してライターに出力します。");

        cgMethod.getLangDoc().getDescriptionList().add(
                "このAPIではパッケージ構造をディレクトリ構造とは考慮しません。");
        cgMethod.getParameterList().add(
                cgFactory.createParameter("sourceFile",
                        "blanco.cg.valueobject.BlancoCgSourceFile",
                        "ソースファイル・バリューオブジェクト。"));
        cgMethod.getParameterList().add(
                cgFactory.createParameter("writer", "java.io.BufferedWriter",
                        "出力先のライター。"));
        cgMethod.getThrowList().add(
                cgFactory.createException("java.io.IOException",
                        "入出力例外が発生した場合。"));

        return cgMethod;
    }
}
