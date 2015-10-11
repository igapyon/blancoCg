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
 * VB.NET言語用の生成試験。
 * 
 * @author IGA Tosiki
 */
public class BlancoCgTransformerVbTest extends TestCase {
    /**
     * VB.NETの試験。
     * 
     * @throws Exception
     */
    public void testTransformerJs() throws Exception {
        final BlancoCgObjectFactory cgFactory = BlancoCgObjectFactory
                .getInstance();

        // ソースファイルを生成します。
        final BlancoCgSourceFile cgSourceFile = cgFactory.createSourceFile(
                "Myprog", "テスト用のクラス");
        cgSourceFile.getImportList().add("System.Text.DummyText");
        // 同じパッケージのインポート試験。
        cgSourceFile.getImportList().add("Myprog.MyClass2");
        cgSourceFile.getLangDoc().getDescriptionList().add(
                "このクラスは blanco Frameworkによって自動的に生成されました。");

        // クラスを生成します。
        final BlancoCgClass cgClass = cgFactory.createClass("MySampleClass",
                "このクラスは、テストのためのクラスです。");
        cgSourceFile.getClassList().add(cgClass);
        cgClass.getLangDoc().getTagList().add(
                cgFactory.createLangDocTag("author", null, "blanco Framework"));
        cgClass.getExtendClassList().add(
                cgFactory.createType("System.WebException"));
        cgClass.getImplementInterfaceList().add(
                cgFactory.createType("System.WebException"));
        cgClass.getImplementInterfaceList().add(
                cgFactory.createType("System.WebException2"));

        // フィールドを生成します。
        final BlancoCgField cgField = cgFactory.createField("myField",
                "System.DateTime", "日付フィールドの試験です。");
        cgClass.getFieldList().add(cgField);
        cgField.setDefault("New DateTime()");

        // メソッドを生成します。
        final BlancoCgMethod cgMethod = cgFactory.createMethod("MyMethod",
                "メソッドの試験です。");
        cgClass.getMethodList().add(cgMethod);

        cgMethod.setAccess("private");

        // パラメータを追加します。
        cgMethod.getParameterList().add(
                cgFactory.createParameter("argString", "System.String",
                        "文字列引数。"));
        cgMethod.getParameterList()
                .add(
                        cgFactory.createParameter("argDate", "System.DateTime",
                                "日付引数。"));
        // 戻り値を設定します。
        cgMethod.setReturn(cgFactory.createReturn("Boolean", "成功ならtrue。"));

        cgMethod.getThrowList().add(
                cgFactory.createException("System.IO.IOException",
                        "入出力例外が発生した場合。"));

        // メソッドの内容を追加します。
        cgMethod.getLineList().add("' 代入の試験です。");
        cgMethod.getLineList().add("Return True");

        final BlancoCgTransformer cgTransformerVb = BlancoCgTransformerFactory
                .getVbSourceTransformer();
        cgTransformerVb.transform(cgSourceFile, new File("./tmp/blanco"));
    }
}
