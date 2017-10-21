/*
 * blanco Framework
 * Copyright (C) 2004-2017 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
/*
 * Copyright 2017 Toshiki Iga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package blanco.cg.transformer.php;

import java.util.List;

import blanco.cg.BlancoCgSupportedLang;
import blanco.cg.util.BlancoCgLineUtil;
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
class BlancoCgLangDocPhpSourceExpander {
    /**
     * このクラスが処理対象とするプログラミング言語。
     */
    protected static final int TARGET_LANG = BlancoCgSupportedLang.PHP;

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
        boolean isLangDocTitleStarted = false;

        if (BlancoStringUtil.null2Blank(langDoc.getTitle()).length() > 0) {
            isLangDocTitleStarted = true;
            argSourceLines.add("* "
                    + BlancoCgSourceUtil.escapeStringAsLangDoc(TARGET_LANG,
                            langDoc.getTitle()));
        }

        // 空行が挿入済みかどうかをチェックするためのフラグ。
        boolean isLangDocDescriptionStarted = false;

        for (int indexDescription = 0; indexDescription < langDoc
                .getDescriptionList().size(); indexDescription++) {
            final String strDescrption = langDoc.getDescriptionList().get(
                    indexDescription);

            // 空行挿入。
            if (isLangDocDescriptionStarted == false) {
                isLangDocDescriptionStarted = true;
                if (isLangDocTitleStarted) {
                    argSourceLines.add("*");
                }
            }

            argSourceLines.add("* " + strDescrption);
        }

        // 空行が挿入済みかどうかをチェックするためのフラグ。
        boolean isLangDocTagStarted = false;

        // authorなど付加情報を展開。
        if (langDoc.getTagList() != null) {
            for (int index = 0; index < langDoc.getTagList().size(); index++) {
                final BlancoCgLangDocTag langDocTag = langDoc.getTagList().get(
                        index);

                // 空行挿入。
                if (isLangDocTagStarted == false) {
                    isLangDocTagStarted = true;
                    argSourceLines.add("*");
                }

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

            // 空行挿入。
            if (isLangDocTagStarted == false) {
                isLangDocTagStarted = true;
                argSourceLines.add("*");
            }

            final StringBuffer bufParameter = new StringBuffer();
            // 最近の PHPLint実装では、@param 型 $変数 のような記載へと変更されています。
            bufParameter.append("* @param "
                    + BlancoNameUtil.trimJavaPackage(cgParameter.getType()
                            .getName()) + " "
                    + BlancoCgLineUtil.getVariablePrefix(TARGET_LANG)
                    + cgParameter.getName());
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

            // 空行挿入。
            if (isLangDocTagStarted == false) {
                isLangDocTagStarted = true;
                argSourceLines.add("*");
            }

            final StringBuffer bufReturn = new StringBuffer();
            bufReturn.append("* @return "
                    + BlancoNameUtil.trimJavaPackage(langDoc.getReturn()
                            .getType().getName()));
            if (BlancoStringUtil.null2Blank(
                    langDoc.getReturn().getDescription()).length() > 0) {
                bufReturn.append(" "
                        + BlancoCgSourceUtil.escapeStringAsLangDoc(TARGET_LANG,
                                langDoc.getReturn().getDescription()));
            }
            argSourceLines.add(bufReturn.toString());
        }

        // throwsリストを展開。
        for (int indexThrow = 0; indexThrow < langDoc.getThrowList().size(); indexThrow++) {
            final BlancoCgException cgException = langDoc.getThrowList().get(
                    indexThrow);

            // 空行挿入。
            if (isLangDocTagStarted == false) {
                isLangDocTagStarted = true;
                argSourceLines.add("*");
            }

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
    }
}
