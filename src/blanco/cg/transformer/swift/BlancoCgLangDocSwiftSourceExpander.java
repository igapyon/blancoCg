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
import blanco.cg.valueobject.BlancoCgLangDoc;
import blanco.cg.valueobject.BlancoCgParameter;
import blanco.commons.util.BlancoStringUtil;

/**
 * BlancoCgLangDoc(言語ドキュメント)をソースコードに展開します。
 * 
 * このクラスはblancoCgのバリューオブジェクトからソースコードを自動生成するトランスフォーマーの個別の展開機能です。<br>
 * クラス、メソッド、フィールドなど、各種言語ドキュメントを展開する共通処理です。
 * 
 * @author IGA Tosiki
 */
class BlancoCgLangDocSwiftSourceExpander {
    /**
     * このクラスが処理対象とするプログラミング言語。
     */
    protected static final int TARGET_LANG = BlancoCgSupportedLang.SWIFT;

    /**
     * 言語ドキュメント情報を元にソースコードを展開します。
     * 
     * @param langDoc
     *            言語ドキュメント情報。
     * @param argSourceLines
     *            ソースコード。
     */
    public void transformLangDoc(final BlancoCgLangDoc langDoc,
            final List<java.lang.String> argSourceLines) {
        // 開始・終了を除く本体を展開します。
        transformLangDocBody(langDoc, argSourceLines, false);
    }

    /**
     * 言語ドキュメントのうち、本体部分を展開します。
     * 
     * このメソッドはソースファイルのファイルヘッダー展開からも利用されています。
     * 
     * @param langDoc
     * @param argSourceLines
     * @param isFileHeader
     *            ファイルヘッダかどうか。
     */
    public void transformLangDocBody(final BlancoCgLangDoc langDoc,
            final List<java.lang.String> argSourceLines,
            final boolean isFileHeader) {
        boolean isLangDocTitleStarted = false;

        String commentString = "/// ";
        if (isFileHeader) {
            commentString = BlancoCgLineUtil
                    .getSingleLineCommentPrefix(TARGET_LANG);
        }

        if (BlancoStringUtil.null2Blank(langDoc.getTitle()).length() > 0) {
            isLangDocTitleStarted = true;
            argSourceLines.add(commentString
                    + "<summary>"
                    + BlancoCgSourceUtil.escapeStringAsLangDoc(TARGET_LANG,
                            langDoc.getTitle()) + "</summary>");
        }

        // 空行が挿入済みかどうかをチェックするためのフラグ。
        boolean isLangDocDescriptionStarted = false;

        for (int indexDescription = 0; indexDescription < langDoc
                .getDescriptionList().size(); indexDescription++) {
            final String strDescrption = langDoc.getDescriptionList().get(
                    indexDescription);

            if (isLangDocDescriptionStarted == false) {
                if (isLangDocTitleStarted == false) {
                    isLangDocTitleStarted = true;
                    argSourceLines.add(commentString
                            + "<summary>"
                            + BlancoCgSourceUtil.escapeStringAsLangDoc(
                                    TARGET_LANG, strDescrption) + "</summary>");
                } else {
                    isLangDocDescriptionStarted = true;
                    argSourceLines.add(commentString + "<remarks>");
                    argSourceLines.add(commentString + "<newpara>"
                            + strDescrption + "</newpara>");
                }
            } else {
                argSourceLines.add(commentString + "<newpara>" + strDescrption
                        + "</newpara>");
            }
        }

        if (isLangDocDescriptionStarted) {
            argSourceLines.add(commentString + "</remarks>");
        }

        // TODO authorを展開。

        // TODO author以外の展開も検討すること。

        // メソッドパラメータを展開。
        for (int indexParameter = 0; indexParameter < langDoc
                .getParameterList().size(); indexParameter++) {
            final BlancoCgParameter cgParameter = langDoc.getParameterList()
                    .get(indexParameter);

            final StringBuffer bufParameter = new StringBuffer();
            bufParameter.append(commentString + "<param name=\""
                    + cgParameter.getName() + "\">");
            if (BlancoStringUtil.null2Blank(cgParameter.getDescription())
                    .length() > 0) {
                bufParameter.append(BlancoCgSourceUtil.escapeStringAsLangDoc(
                        TARGET_LANG, cgParameter.getDescription()));
            }
            bufParameter.append("</param>");
            argSourceLines.add(bufParameter.toString());
        }

        if (langDoc.getReturn() != null
                && langDoc.getReturn().getType().getName().equals("void") == false) {

            final StringBuffer bufReturn = new StringBuffer();
            bufReturn.append(commentString + "<returns>");
            if (BlancoStringUtil.null2Blank(
                    langDoc.getReturn().getDescription()).length() > 0) {
                bufReturn.append(BlancoCgSourceUtil.escapeStringAsLangDoc(
                        TARGET_LANG, langDoc.getReturn().getDescription()));
            }
            bufReturn.append("</returns>");
            argSourceLines.add(bufReturn.toString());
        }

        // ※throwsリスト展開は C#.NETには存在しません。
        // TODO throwリスト展開について、言語ドキュメントの説明部分へと展開すること。
    }
}
