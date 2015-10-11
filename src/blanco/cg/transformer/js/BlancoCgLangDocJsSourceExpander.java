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

import blanco.cg.BlancoCgSupportedLang;
import blanco.cg.util.BlancoCgSourceUtil;
import blanco.cg.valueobject.BlancoCgException;
import blanco.cg.valueobject.BlancoCgLangDoc;
import blanco.cg.valueobject.BlancoCgLangDocTag;
import blanco.cg.valueobject.BlancoCgParameter;
import blanco.commons.util.BlancoNameUtil;
import blanco.commons.util.BlancoStringUtil;

/**
 * BlancoCgLangDoc(言語ドキュメント)をソースコードに展開します。
 * 
 * このクラスはblancoCgのバリューオブジェクトからソースコードを自動生成するトランスフォーマーの個別の展開機能です。<br>
 * クラス、メソッド、フィールドなど、各種言語ドキュメントを展開する共通処理です。
 * 
 * @author IGA Tosiki
 */
class BlancoCgLangDocJsSourceExpander {
    /**
     * このクラスが処理対象とするプログラミング言語。
     */
    protected static final int TARGET_LANG = BlancoCgSupportedLang.JS;

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
        // 現在は JSDocのタグを出力しています。
        argSourceLines.add("/**");

        // 開始・終了を除く本体を展開します。
        transformLangDocBody(langDoc, argSourceLines);

        argSourceLines.add("*/");
    }

    /**
     * 言語ドキュメントのうち、本体部分を展開します。
     * 
     * このメソッドはソースファイルのファイルヘッダー展開からも利用されています。
     * 
     * @param langDoc
     * @param argSourceLines
     */
    public void transformLangDocBody(final BlancoCgLangDoc langDoc,
            final List<java.lang.String> argSourceLines) {

        if (BlancoStringUtil.null2Blank(langDoc.getTitle()).length() > 0) {
            argSourceLines.add("* "
                    + BlancoCgSourceUtil.escapeStringAsLangDoc(TARGET_LANG,
                            langDoc.getTitle()));
        }

        // authorなど付加情報を展開。
        if (langDoc.getTagList() != null) {
            for (int index = 0; index < langDoc.getTagList().size(); index++) {
                final BlancoCgLangDocTag langDocTag = langDoc.getTagList().get(
                        index);

                if (langDocTag.getName() == null) {
                    throw new IllegalArgumentException(
                            "BlancoCgLangDocTagのnameにnullが与えられました。"
                                    + langDocTag.toString());
                }
                if (langDocTag.getValue() == null) {
                    throw new IllegalArgumentException(
                            "BlancoCgLangDocTagのvalueにnullが与えられました。"
                                    + langDocTag.toString());
                }

                final StringBuffer buf = new StringBuffer();
                buf.append("* @" + langDocTag.getName() + " ");
                if (BlancoStringUtil.null2Blank(langDocTag.getKey()).length() > 0) {
                    buf.append(langDocTag.getKey() + " ");
                }
                buf.append(BlancoCgSourceUtil.escapeStringAsLangDoc(
                        TARGET_LANG, langDocTag.getValue()));
                argSourceLines.add(buf.toString());
            }
        }

        // メソッドパラメータを展開。
        for (int indexParameter = 0; indexParameter < langDoc
                .getParameterList().size(); indexParameter++) {
            final BlancoCgParameter cgParameter = langDoc.getParameterList()
                    .get(indexParameter);

            final StringBuffer bufParameter = new StringBuffer();
            bufParameter.append("* @param {" + cgParameter.getType().getName()
                    + "} " + cgParameter.getName());
            if (BlancoStringUtil.null2Blank(cgParameter.getDescription())
                    .length() > 0) {
                bufParameter.append(" "
                        + BlancoCgSourceUtil.escapeStringAsLangDoc(TARGET_LANG,
                                cgParameter.getDescription()));
            }
            argSourceLines.add(bufParameter.toString());
        }

        if (langDoc.getReturn() != null
                && langDoc.getReturn().getType().getName().equals("void") == false) {

            final StringBuffer bufReturn = new StringBuffer();
            bufReturn.append("* @return");
            if (BlancoStringUtil.null2Blank(
                    langDoc.getReturn().getDescription()).length() > 0) {
                bufReturn.append(" "
                        + BlancoCgSourceUtil.escapeStringAsLangDoc(TARGET_LANG,
                                langDoc.getReturn().getDescription()));
            }
            argSourceLines.add(bufReturn.toString());

            // JSDocでは @type により型を表現するようになっています。
            argSourceLines.add("* @type "
                    + langDoc.getReturn().getType().getName());
        }

        // throwsリストを展開。
        for (int indexThrow = 0; indexThrow < langDoc.getThrowList().size(); indexThrow++) {
            final BlancoCgException cgException = langDoc.getThrowList().get(
                    indexThrow);

            final StringBuffer bufThrow = new StringBuffer();

            // 言語ドキュメント処理においては、blancoCgのTypeに関する共通処理を利用することはできません。
            // 個別に記述を行います。
            bufThrow.append("* @throws "
                    + BlancoNameUtil.trimJavaPackage(cgException.getType()
                            .getName()));
            if (BlancoStringUtil.null2Blank(cgException.getDescription())
                    .length() > 0) {
                bufThrow.append(" "
                        + BlancoCgSourceUtil.escapeStringAsLangDoc(TARGET_LANG,
                                cgException.getDescription()));
            }
            argSourceLines.add(bufThrow.toString());
        }

        for (int indexDescription = 0; indexDescription < langDoc
                .getDescriptionList().size(); indexDescription++) {
            final String strDescrption = langDoc.getDescriptionList().get(
                    indexDescription);

            argSourceLines.add("* " + strDescrption);
        }
    }
}
