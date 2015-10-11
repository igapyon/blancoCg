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
import blanco.cg.valueobject.BlancoCgEnum;
import blanco.cg.valueobject.BlancoCgEnumElement;
import blanco.cg.valueobject.BlancoCgField;
import blanco.cg.valueobject.BlancoCgInterface;
import blanco.cg.valueobject.BlancoCgMethod;
import blanco.cg.valueobject.BlancoCgSourceFile;

/**
 * Delphi言語用の生成試験。
 * 
 * @author YAMAMOTO Koji
 */
public class BlancoCgTransformerDelphiTest extends TestCase {
    /**
     * Delphiの試験。
     * 
     * @throws Exception
     */
    public void testTransformerDelphi() throws Exception {
        final BlancoCgObjectFactory cgFactory = BlancoCgObjectFactory
                .getInstance();

        // ソースファイルを生成します。

        final BlancoCgSourceFile cgSourceFile = cgFactory.createSourceFile(
                "Unit1", "テスト用のクラス");
        cgSourceFile.setName("Unit1");

        cgSourceFile.getImportList().add("System.Text.DummyText");
        // 同じパッケージのインポート試験。
        cgSourceFile.getImportList().add("Myprog.MyClass2");

        // クラスを生成します。
        final BlancoCgClass cgClass = cgFactory.createClass("TMyClass",
                "このクラスは、テストのためのクラスです。");
        cgSourceFile.getClassList().add(cgClass);
        // cgClass.getLangDoc().getTagList().add(
        // cgFactory.createLangDocTag("author", null, "blanco Framework"));
        cgClass.getExtendClassList().add(cgFactory.createType("TObject"));
        // cgClass.getImplementInterfaceList().add(
        // cgFactory.createType("System.WebException"));
        // cgClass.getImplementInterfaceList().add(
        // cgFactory.createType("System.WebException2"));

        // 列挙体
        // final BlancoCgEnum cgEnum = cgFactory.createEnum("FavorColor",
        // "列挙体の試験。");
        // cgClass.getEnumList().add(cgEnum);
        // final BlancoCgEnumElement cgEnumElementFirst = cgFactory
        // .createEnumElement("Red", "あか");
        // cgEnumElementFirst.setDefault("1");
        // cgEnum.getElementList().add(cgEnumElementFirst);
        // cgEnum.getElementList().add(
        // cgFactory.createEnumElement("Yerrow", "きいろ"));
        // cgEnum.getElementList().add(cgFactory.createEnumElement("Blue",
        // "あお"));

        // フィールドを生成します。
        final BlancoCgField cgField = cgFactory.createField("MyField",
                "String", "Stringフィールドの試験です。");
        cgClass.getFieldList().add(cgField);
        // cgField.setDefault("new DateTime()");
        //
        // final BlancoCgField cgField2 = cgFactory.createField("myField2",
        // "java.util.Date", "日付フィールドの試験v2です。");
        // cgClass.getFieldList().add(cgField2);
        // cgField2.getType().setArray(true);

        // プロシージャを生成します。
        final BlancoCgMethod cgMethod = cgFactory.createMethod("MyMethod",
                "プロシージャの試験です。");
        cgClass.getMethodList().add(cgMethod);

        // パラメータを追加します。
        cgMethod.getParameterList().add(
                cgFactory.createParameter("argString", "String", "文字列引数。"));
        cgMethod.getParameterList().add(
                cgFactory.createParameter("argInt", "integer", "整数引数。"));

        // ファンクションを生成します。
        final BlancoCgMethod cgFunction = cgFactory.createMethod("MyFunction",
                "メソッドの試験です。");
        cgClass.getMethodList().add(cgFunction);

        // パラメータを追加します。
        cgFunction.getParameterList().add(
                cgFactory.createParameter("argString", "String", "文字列引数。"));
        cgFunction.getParameterList().add(
                cgFactory.createParameter("argInt", "integer", "整数引数。"));

        // 戻り値を設定します。
        cgFunction.setReturn(cgFactory.createReturn("boolean", "成功ならtrue。"));

        // cgMethod.getThrowList().add(
        // cgFactory.createException("System.IO.IOException",
        // "入出力例外が発生した場合。"));

        // アノテーションの追加。
        // cgMethod.getAnnotationList().add(
        // "Copyright(value=\"blanco Framework\")");

        // メソッドの内容を追加します。
        // cgMethod.getLineList().add("// 代入の試験です。");
        // cgMethod.getLineList().add("int a = 0;");

        final BlancoCgTransformer cgTransformerDelphi = BlancoCgTransformerFactory
                .getDelphiSourceTransformer();
        cgTransformerDelphi.transform(cgSourceFile, new File("./tmp/blanco"));
    }

}
