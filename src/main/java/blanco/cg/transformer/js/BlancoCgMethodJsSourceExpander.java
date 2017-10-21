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
package blanco.cg.transformer.js;

import java.util.ArrayList;
import java.util.List;

import blanco.cg.BlancoCgObjectFactory;
import blanco.cg.BlancoCgSupportedLang;
import blanco.cg.util.BlancoCgLineUtil;
import blanco.cg.valueobject.BlancoCgClass;
import blanco.cg.valueobject.BlancoCgException;
import blanco.cg.valueobject.BlancoCgField;
import blanco.cg.valueobject.BlancoCgLangDoc;
import blanco.cg.valueobject.BlancoCgMethod;
import blanco.cg.valueobject.BlancoCgParameter;
import blanco.cg.valueobject.BlancoCgSourceFile;
import blanco.commons.util.BlancoStringUtil;

/**
 * BlancoCgMethodをソースコードに展開します。
 * 
 * このクラスはblancoCgのバリューオブジェクトからソースコードを自動生成するトランスフォーマーの個別の展開機能です。
 * 
 * @author IGA Tosiki
 */
class BlancoCgMethodJsSourceExpander {
    /**
     * このクラスが処理対象とするプログラミング言語。
     */
    protected static final int TARGET_LANG = BlancoCgSupportedLang.JS;

    /**
     * ここでメソッドを展開します。
     * 
     * @param cgMethod
     *            処理対象となるメソッド。
     * @param argSourceFile
     *            ソースファイル。
     * @param argSourceLines
     *            出力先行リスト。
     */
    public void transformMethod(final BlancoCgClass cgClass,
            final BlancoCgMethod cgMethod,
            final BlancoCgSourceFile argSourceFile,
            final List<java.lang.String> argSourceLines) {
        if (BlancoStringUtil.null2Blank(cgMethod.getName()).length() == 0) {
            throw new IllegalArgumentException("メソッドの名前に適切な値が設定されていません。");
        }
        if (cgMethod.getReturn() == null) {
            // それはありえます。voidの場合にはnullが指定されるのです。
        }

        // 改行を付与。
        argSourceLines.add("");

        prepareExpand(cgClass, cgMethod, argSourceFile);

        // 情報が一式そろったので、ソースコードの実際の展開を行います。

        // 次に LangDocをソースコード形式に展開。
        new BlancoCgLangDocJsSourceExpander().transformLangDoc(cgMethod
                .getLangDoc(), argSourceLines);

        // アノテーションを展開。
        expandAnnotationList(cgMethod, argSourceLines);

        // メソッドの本体部分を展開。
        expandMethodBody(cgClass, cgMethod, argSourceFile, argSourceLines);
    }

    /**
     * クラスに含まれる static なフィールドを展開します。
     * 
     * 現在は 登録順でソースコード展開します。
     * 
     * @param cgClass
     *            処理中のクラス。
     * @param argSourceFile
     *            ソースファイル。
     * @param argSourceLines
     *            ソースコード行リスト。
     */
    public void transformStaticFieldList(final BlancoCgClass cgClass,
            final BlancoCgSourceFile argSourceFile,
            final List<java.lang.String> argSourceLines) {
        if (cgClass.getFieldList() == null) {
            // フィールドのリストにnullが与えられました。
            // かならずフィールドのリストにはListをセットしてください。
            throw new IllegalArgumentException("フィールドのリストにnullが与えられました。");
        }

        for (int index = 0; index < cgClass.getFieldList().size(); index++) {
            final BlancoCgField cgField = cgClass.getFieldList().get(index);

            if (cgField.getStatic()) {
                // ここではクラスのフィールド (staticなフィールド) のみを展開します。
                new BlancoCgFieldJsSourceExpander().transformField(cgClass,
                        cgField, argSourceFile, argSourceLines);
            }
        }
    }

    /**
     * ソースコード展開に先立ち、必要な情報の収集を行います。
     * 
     * @param cgMethod
     *            メソッドオブジェクト。
     * @param argSourceFile
     *            ソースファイル。
     */
    private void prepareExpand(final BlancoCgClass cgClass,
            final BlancoCgMethod cgMethod,
            final BlancoCgSourceFile argSourceFile) {
        // 最初にメソッド情報をLangDocに展開。
        if (cgMethod.getLangDoc() == null) {
            // LangDoc未指定の場合にはこちら側でインスタンスを生成。
            cgMethod.setLangDoc(new BlancoCgLangDoc());
        }
        if (cgMethod.getLangDoc().getParameterList() == null) {
            cgMethod.getLangDoc().setParameterList(
                    new ArrayList<blanco.cg.valueobject.BlancoCgParameter>());
        }
        if (cgMethod.getLangDoc().getThrowList() == null) {
            cgMethod.getLangDoc().setThrowList(
                    new ArrayList<blanco.cg.valueobject.BlancoCgException>());
        }
        if (cgMethod.getLangDoc().getTitle() == null) {
            cgMethod.getLangDoc().setTitle(cgMethod.getDescription());
        }

        if (cgMethod.getConstructor()) {
            // ファンクション名そのものをLangDocに展開します。
            cgMethod.getLangDoc().getTagList().add(
                    BlancoCgObjectFactory.getInstance().createLangDocTag(
                            "class", null, cgClass.getDescription()));

            cgMethod.getLangDoc().getTagList().add(
                    BlancoCgObjectFactory.getInstance().createLangDocTag(
                            "constructor", null, ""));
        } else {
            cgMethod.getLangDoc().getTagList().add(
                    BlancoCgObjectFactory.getInstance().createLangDocTag(
                            "addon", null, ""));
        }

        if (BlancoStringUtil.null2Blank(cgMethod.getAccess()).equals("private")) {
            cgMethod.getLangDoc().getTagList().add(
                    BlancoCgObjectFactory.getInstance().createLangDocTag(
                            "private", null, ""));
        }

        for (int indexParameter = 0; indexParameter < cgMethod
                .getParameterList().size(); indexParameter++) {
            final BlancoCgParameter cgParameter = cgMethod.getParameterList()
                    .get(indexParameter);

            // import文に型を追加。
            argSourceFile.getImportList().add(cgParameter.getType().getName());

            // 言語ドキュメントにパラメータを追加。
            cgMethod.getLangDoc().getParameterList().add(cgParameter);
        }

        if (cgMethod.getReturn() != null) {
            // import文に型を追加。
            argSourceFile.getImportList().add(
                    cgMethod.getReturn().getType().getName());

            // 言語ドキュメントにreturnを追加。
            cgMethod.getLangDoc().setReturn(cgMethod.getReturn());
        }

        // 例外についてLangDoc構造体に展開
        for (int index = 0; index < cgMethod.getThrowList().size(); index++) {
            final BlancoCgException cgException = cgMethod.getThrowList().get(
                    index);

            // import文に型を追加。
            argSourceFile.getImportList().add(cgException.getType().getName());

            // 言語ドキュメントに例外を追加。
            cgMethod.getLangDoc().getThrowList().add(cgException);
        }
    }

    /**
     * メソッドの本体部分を展開します。
     * 
     * @param cgClass
     *            クラスオブジェクト。
     * @param cgMethod
     *            メソッドオブジェクト。
     * @param argSourceFile
     *            ソースコード。
     * @param argSourceLines
     *            インタフェースとして展開するかどうか。
     */
    private void expandMethodBody(final BlancoCgClass cgClass,
            final BlancoCgMethod cgMethod,
            final BlancoCgSourceFile argSourceFile,
            final List<java.lang.String> argSourceLines) {
        final StringBuffer buf = new StringBuffer();

        if (cgMethod.getConstructor()) {
            // コンストラクタの場合には functionから始まります。
            buf.append("function " + cgClass.getName());
        } else {
            // 通常のメソッドの場合には、コンストラクタの prototypeへのfunctionの追加となります。
            buf.append(cgClass.getName() + ".prototype." + cgMethod.getName()
                    + " = function");
        }

        // JavaScriptにはアクセスフラグそのものは存在しません。
        // JSDocの記述としてアクセスを表現しています。

        // JavaScriptでは言語としては戻り値は出力しません。JSDocの記述としてのみ表現します。

        buf.append("(");
        for (int index = 0; index < cgMethod.getParameterList().size(); index++) {
            final BlancoCgParameter cgParameter = cgMethod.getParameterList()
                    .get(index);
            if (cgParameter.getType() == null) {
                throw new IllegalArgumentException("メソッド[" + cgMethod.getName()
                        + "]のパラメータ[" + cgParameter.getName()
                        + "]に型がnullが与えられました。");
            }

            if (index != 0) {
                buf.append(", ");
            }

            // JavaScriptではfinal修飾は無効です。

            buf.append("/* "
                    + BlancoCgTypeJsSourceExpander.toTypeString(cgParameter
                            .getType()) + " */");
            buf.append(" ");
            buf.append(cgParameter.getName());
        }
        buf.append(")");

        // 例外スロー展開はJavaScriptには存在しません。

        if (cgMethod.getAbstract()) {
            // 抽象メソッドまたはインタフェースの場合には、メソッドの本体を展開しません。
            buf.append(BlancoCgLineUtil.getTerminator(TARGET_LANG));
            argSourceLines.add(buf.toString());
        } else {
            // メソッドブロックの開始。
            buf.append(" {");

            // ここでいったん、行を確定。
            argSourceLines.add(buf.toString());

            // 引数チェックの自動生成を行います。
            argSourceLines.add("/* パラメータの数、型チェックを行います。 */");
            argSourceLines.add(BlancoCgLineUtil.getIfBegin(TARGET_LANG,
                    "arguments.length !== "
                            + cgMethod.getParameterList().size()));
            argSourceLines
                    .add("throw new Error(\"[ArgumentException]: "
                            + cgClass.getName()
                            + "."
                            + cgMethod.getName()
                            + " のパラメータは["
                            + cgMethod.getParameterList().size()
                            + "]個である必要があります。しかし実際には[\" + arguments.length +  \"]個のパラメータを伴って呼び出されました。\");");
            argSourceLines.add(BlancoCgLineUtil.getIfEnd(TARGET_LANG));

            for (int indexParameter = 0; indexParameter < cgMethod
                    .getParameterList().size(); indexParameter++) {
                final BlancoCgParameter cgParameter = cgMethod
                        .getParameterList().get(indexParameter);
                if (BlancoCgTypeJsSourceExpander
                        .isLanguageReservedKeyword(BlancoStringUtil
                                .null2Blank(cgParameter.getType().getName()))) {
                    argSourceLines.add(BlancoCgLineUtil.getIfBegin(TARGET_LANG,
                            "typeof(" + cgParameter.getName() + ") != \""
                                    + cgParameter.getType().getName() + "\""));
                } else {
                    argSourceLines.add(BlancoCgLineUtil.getIfBegin(TARGET_LANG,
                            cgParameter.getName() + " instanceof "
                                    + cgParameter.getType().getName()
                                    + " == false"));
                }
                argSourceLines.add("throw new Error(\"[ArgumentException]: "
                        + cgClass.getName() + "." + cgMethod.getName() + " の"
                        + (indexParameter + 1) + "番目のパラメータは["
                        + cgParameter.getType().getName()
                        + "]型でなくてはなりません。しかし実際には[\" + typeof("
                        + cgParameter.getName() + ") + \"]型が与えられました。\");");
                argSourceLines.add(BlancoCgLineUtil.getIfEnd(TARGET_LANG));
            }

            argSourceLines.add("");

            if (cgMethod.getConstructor()) {
                // コンストラクタであるのでフィールドを展開します。
                // JavaScriptの場合には、クラスにフィールドを展開するのではなくコンストラクタにフィールド展開が存在します。
                expandFieldList(cgClass, argSourceFile, argSourceLines);
            }

            // 親クラスメソッド実行機能の展開。
            if (BlancoStringUtil.null2Blank(cgMethod.getSuperclassInvocation())
                    .length() > 0) {
                // super(引数) などが含まれます。
                argSourceLines.add(cgMethod.getSuperclassInvocation()
                        + BlancoCgLineUtil.getTerminator(TARGET_LANG));
            }

            // 行を展開します。
            expandLineList(cgMethod, argSourceLines);

            // メソッドブロックの終了。
            if (cgMethod.getConstructor()) {
                // クラス宣言では最後にセミコロンが付与しません。
                argSourceLines.add("}");
            } else {
                // JavaScriptでは最後にセミコロンが付与されます。
                argSourceLines.add("};");
            }
        }
    }

    /**
     * クラスに含まれる各々のフィールドを展開します。
     * 
     * 現在は 登録順でソースコード展開します。
     * 
     * @param cgClass
     *            処理中のクラス。
     * @param argSourceFile
     *            ソースファイル。
     * @param argSourceLines
     *            ソースコード行リスト。
     */
    private void expandFieldList(final BlancoCgClass cgClass,
            final BlancoCgSourceFile argSourceFile,
            final List<java.lang.String> argSourceLines) {
        if (cgClass.getFieldList() == null) {
            // フィールドのリストにnullが与えられました。
            // かならずフィールドのリストにはListをセットしてください。
            throw new IllegalArgumentException("フィールドのリストにnullが与えられました。");
        }

        for (int index = 0; index < cgClass.getFieldList().size(); index++) {
            final BlancoCgField cgField = cgClass.getFieldList().get(index);

            if (cgField.getStatic() == false) {
                // コンストラクタの中で、staticではないフィールドを展開します。
                new BlancoCgFieldJsSourceExpander().transformField(cgClass,
                        cgField, argSourceFile, argSourceLines);
            }
        }
    }

    /**
     * アノテーションを展開します。
     * 
     * @param cgMethod
     *            メソッド。
     * @param argSourceLines
     *            ソースコード。
     */
    private void expandAnnotationList(final BlancoCgMethod cgMethod,
            final List<java.lang.String> argSourceLines) {
        if (cgMethod.getOverride()) {
            // JavaScript言語での override表現は現時点ではサポート外です。
            throw new IllegalArgumentException(
                    "現バージョンの blancoCgは JavaScript言語の際にはオーバーライド表現をサポートしません。");
            // argSourceLines.add("@Override");
        }

        for (int index = 0; index < cgMethod.getAnnotationList().size(); index++) {
            final String strAnnotation = cgMethod.getAnnotationList()
                    .get(index);
            throw new IllegalArgumentException(
                    "現バージョンの blancoCgは JavaScript言語の際にはアノテーションをサポートしません。"
                            + strAnnotation);
            // JavaScript言語のAnnotationは不明です。
            // argSourceLines.add("@" + strAnnotation);
        }
    }

    /**
     * 行を展開します。
     * 
     * @param cgMethod
     *            メソッド情報。
     * @param argSourceLines
     *            出力行リスト。
     */
    private void expandLineList(final BlancoCgMethod cgMethod,
            final List<java.lang.String> argSourceLines) {
        for (int indexLine = 0; indexLine < cgMethod.getLineList().size(); indexLine++) {
            final String strLine = cgMethod.getLineList().get(indexLine);
            argSourceLines.add(strLine);
        }
    }
}
