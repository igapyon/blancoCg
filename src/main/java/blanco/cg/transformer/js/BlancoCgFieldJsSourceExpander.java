/*
 * blanco Framework
 * Copyright (C) 2004-2006 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.cg.transformer.js;

import java.util.List;

import blanco.cg.BlancoCgObjectFactory;
import blanco.cg.BlancoCgSupportedLang;
import blanco.cg.util.BlancoCgLineUtil;
import blanco.cg.valueobject.BlancoCgClass;
import blanco.cg.valueobject.BlancoCgField;
import blanco.cg.valueobject.BlancoCgLangDoc;
import blanco.cg.valueobject.BlancoCgSourceFile;
import blanco.commons.util.BlancoStringUtil;

/**
 * BlancoCgFieldをソースコードへと展開します。
 * 
 * このクラスはblancoCgのバリューオブジェクトからソースコードを自動生成するトランスフォーマーの個別の展開機能です。
 * 
 * @author IGA Tosiki
 */
class BlancoCgFieldJsSourceExpander {
    /**
     * このクラスが処理対象とするプログラミング言語。
     */
    protected static final int TARGET_LANG = BlancoCgSupportedLang.JS;

    /**
     * ここでフィールドを展開します。
     * 
     * @param cgClass
     *            処理対象となるクラス。
     * @param cgField
     *            処理対象となるフィールド。
     * @param argSourceFile
     *            ソースファイル。
     * @param argSourceLines
     *            出力先行リスト。
     */
    public void transformField(final BlancoCgClass cgClass,
            final BlancoCgField cgField,
            final BlancoCgSourceFile argSourceFile,
            final List<java.lang.String> argSourceLines) {
        if (BlancoStringUtil.null2Blank(cgField.getName()).length() == 0) {
            throw new IllegalArgumentException("フィールドの名前に適切な値が設定されていません。");
        }
        if (BlancoStringUtil.null2Blank(cgField.getType().getName()).length() == 0) {
            throw new IllegalArgumentException("フィールド[" + cgField.getName()
                    + "]の型が適切な値が設定されていません。");
        }

        // 有無をいわさず改行を付与します。
        argSourceLines.add("");

        // 最初にフィールド情報をLangDocに展開。
        if (cgField.getLangDoc() == null) {
            // LangDoc未指定の場合にはこちら側でインスタンスを生成。
            cgField.setLangDoc(new BlancoCgLangDoc());
        }
        if (cgField.getLangDoc().getTitle() == null) {
            cgField.getLangDoc().setTitle(cgField.getDescription());
        }

        if (BlancoStringUtil.null2Blank(cgField.getAccess()).equals("private")
                || BlancoStringUtil.null2Blank(cgField.getAccess()).equals(
                        "protected")) {
            // protected または private の場合にのみスコープ表現を展開します。
            cgField.getLangDoc().getTagList().add(
                    BlancoCgObjectFactory.getInstance().createLangDocTag(
                            cgField.getAccess(), null, ""));
        }

        if (BlancoStringUtil.null2Blank(cgField.getType().getName()).length() > 0
                || BlancoStringUtil.null2Blank(cgField.getType().getName())
                        .equals("void") == false) {
            cgField.getLangDoc().getTagList().add(
                    BlancoCgObjectFactory.getInstance().createLangDocTag(
                            "type", null, cgField.getType().getName()));
        }

        // 次に LangDocをソースコード形式に展開。
        new BlancoCgLangDocJsSourceExpander().transformLangDoc(cgField
                .getLangDoc(), argSourceLines);

        final StringBuffer buf = new StringBuffer();

        if (cgField.getStatic()) {
            // クラスフィールド (staticなフィールド)は、下記のようにクラス名.フィールド名で直接展開されます。
            buf.append(cgClass.getName() + ".");
        } else {
            // 通常のフィールド変数は this.フィールド名 のように展開します。
            buf.append("this.");
        }

        // フィールド生成の本体部分を展開します。
        buf.append(cgField.getName());

        // デフォルト値の指定がある場合にはこれを展開します。
        if (BlancoStringUtil.null2Blank(cgField.getDefault()).length() > 0) {
            buf.append(" = " + cgField.getDefault()
                    + BlancoCgLineUtil.getTerminator(TARGET_LANG));
        } else {
            buf.append(" = null" + BlancoCgLineUtil.getTerminator(TARGET_LANG));
        }

        argSourceLines.add(buf.toString());

        // import文に型を追加。
        argSourceFile.getImportList().add(cgField.getType().getName());
    }
}
