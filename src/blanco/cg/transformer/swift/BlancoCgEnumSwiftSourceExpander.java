/*
 * blanco Framework
 * Copyright (C) 2004-2006 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.cg.transformer.swift;

import java.util.List;

import blanco.cg.BlancoCgSupportedLang;
import blanco.cg.util.BlancoCgLineUtil;
import blanco.cg.util.BlancoCgSourceUtil;
import blanco.cg.valueobject.BlancoCgEnum;
import blanco.cg.valueobject.BlancoCgEnumElement;
import blanco.cg.valueobject.BlancoCgLangDoc;
import blanco.cg.valueobject.BlancoCgSourceFile;
import blanco.commons.util.BlancoStringUtil;

/**
 * BlancoCgEnumをソースコードへと展開します。
 * 
 * このクラスはblancoCgのバリューオブジェクトからソースコードを自動生成するトランスフォーマーの個別の展開機能です。
 * 
 * @author IGA Tosiki
 */
class BlancoCgEnumSwiftSourceExpander {
    /**
     * このクラスが処理対象とするプログラミング言語。
     */
    protected static final int TARGET_LANG = BlancoCgSupportedLang.JAVA;

    /**
     * ここで列挙体を展開します。
     * 
     * @param cgEnum
     *            処理対象となる列挙体。
     * @param argSourceFile
     *            ソースファイル。
     * @param argSourceLines
     *            出力先行リスト。
     * @param argIsInterface
     *            インタフェースかどうか。クラスの場合にはfalse。インタフェースの場合にはtrue。
     */
    public void transformEnum(final BlancoCgEnum cgEnum,
            final BlancoCgSourceFile argSourceFile,
            final List<java.lang.String> argSourceLines) {
        if (BlancoStringUtil.null2Blank(cgEnum.getName()).length() == 0) {
            throw new IllegalArgumentException("列挙体の名前に適切な値が設定されていません。");
        }

        // 有無をいわさず改行を付与します。
        argSourceLines.add("");

        // 最初にフィールド情報をLangDocに展開。
        if (cgEnum.getLangDoc() == null) {
            // LangDoc未指定の場合にはこちら側でインスタンスを生成。
            cgEnum.setLangDoc(new BlancoCgLangDoc());
        }
        if (cgEnum.getLangDoc().getTitle() == null) {
            cgEnum.getLangDoc().setTitle(cgEnum.getDescription());
        }

        // 次に LangDocをソースコード形式に展開。
        new BlancoCgLangDocSwiftSourceExpander().transformLangDoc(cgEnum
                .getLangDoc(), argSourceLines);

        final StringBuffer buf = new StringBuffer();

        if (BlancoStringUtil.null2Blank(cgEnum.getAccess()).length() > 0) {
            buf.append(cgEnum.getAccess() + " ");
        }

        // 列挙体生成の本体部分を展開します。
        buf.append("enum " + cgEnum.getName());

        // 要素を展開します。
        buf.append("{");
        boolean isFirstElement = true;
        for (BlancoCgEnumElement element : cgEnum.getElementList()) {
            if (isFirstElement) {
                isFirstElement = false;
            } else {
                buf.append(", ");
            }
            buf.append(element.getName());

            // デフォルト値を出力。
            if (BlancoStringUtil.null2Blank(element.getDefault()).length() > 0) {
                buf.append(" = " + element.getDefault());
            }

            if (BlancoStringUtil.null2Blank(element.getDescription()).length() > 0) {
                buf.append(" /* "
                        + BlancoCgSourceUtil.escapeStringAsLangDoc(TARGET_LANG,
                                element.getDescription()) + " */");
            }
        }
        buf.append("}");

        buf.append(BlancoCgLineUtil.getTerminator(TARGET_LANG));
        argSourceLines.add(buf.toString());
    }
}
