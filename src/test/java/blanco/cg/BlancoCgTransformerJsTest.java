/*
 * blanco Framework
 * Copyright (C) 2004-2006 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.cg;

import java.io.File;

import junit.framework.TestCase;
import blanco.cg.transformer.BlancoCgTransformerFactory;
import blanco.cg.valueobject.BlancoCgClass;
import blanco.cg.valueobject.BlancoCgField;
import blanco.cg.valueobject.BlancoCgMethod;
import blanco.cg.valueobject.BlancoCgSourceFile;

/**
 * C#.NET言語用の生成試験。
 * 
 * @author IGA Tosiki
 */
public class BlancoCgTransformerJsTest extends TestCase {
    /**
     * C#.NETの試験。
     * 
     * @throws Exception
     */
    public void testTransformerJs() throws Exception {
        final BlancoCgObjectFactory cgFactory = BlancoCgObjectFactory
                .getInstance();

        // ソースファイルを生成します。
        final BlancoCgSourceFile cgSourceFile = cgFactory.createSourceFile(
                "Myprog", "テスト用のクラス");
        cgSourceFile.getLangDoc().getDescriptionList().add(
                "このクラスは blanco Frameworkによって自動的に生成されました。");

        // クラスを生成します。
        final BlancoCgClass cgClass = cgFactory.createClass("MyClass",
                "このクラスは、テストのためのクラスです。");
        cgSourceFile.getClassList().add(cgClass);
        cgClass.getLangDoc().getTagList().add(
                cgFactory.createLangDocTag("author", null, "blanco Framework"));
        cgClass.getExtendClassList().add(
                cgFactory.createType("java.lang.Thread"));

        // フィールドを生成します。
        final BlancoCgField cgField = cgFactory.createField("myField",
                "number", "数値フィールドの試験です。");
        cgClass.getFieldList().add(cgField);
        cgField.setDefault("Number(1)");

        // フィールドを生成します。
        final BlancoCgField cgField2 = cgFactory.createField("myField2",
                "number", "数値フィールドの試験です。");
        cgClass.getFieldList().add(cgField2);
        cgField2.setAccess("public");
        cgField2.setDefault("Number(3)");

        // メソッドを生成します。
        final BlancoCgMethod cgMethod = cgFactory.createMethod("myMethod",
                "メソッドの試験です。");
        cgClass.getMethodList().add(cgMethod);

        cgMethod.setAccess("private");

        // パラメータを追加します。
        cgMethod.getParameterList().add(
                cgFactory.createParameter("argString", "string", "文字列引数。"));
        cgMethod.getParameterList().add(
                cgFactory.createParameter("argDate", "Date", "日付引数。"));
        // 戻り値を設定します。
        cgMethod.setReturn(cgFactory.createReturn("string", "結合後の文字列。"));

        cgMethod.getThrowList().add(
                cgFactory.createException("System.IO.IOException",
                        "入出力例外が発生した場合。"));

        // メソッドの内容を追加します。
        cgMethod.getLineList().add("// 結合の試験です。");
        cgMethod.getLineList().add("return argString + \", \" + argDate;");

        final BlancoCgTransformer cgTransformerJs = BlancoCgTransformerFactory
                .getJsSourceTransformer();
        cgTransformerJs.transform(cgSourceFile, new File("./tmp/blanco"));
    }
}
