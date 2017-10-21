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

import java.util.List;

import blanco.cg.BlancoCgObjectFactory;
import blanco.cg.valueobject.BlancoCgClass;
import blanco.cg.valueobject.BlancoCgLangDoc;
import blanco.cg.valueobject.BlancoCgMethod;
import blanco.cg.valueobject.BlancoCgSourceFile;

/**
 * BlancoCgClassをソースコードへと展開します。
 * 
 * このクラスはblancoCgのバリューオブジェクトからソースコードを自動生成するトランスフォーマーの個別の展開機能です。
 * 
 * @author IGA Tosiki
 */
class BlancoCgClassJsSourceExpander {

    /**
     * ここでClassを展開します。
     * 
     * @param cgClass
     *            処理対象となるクラス。
     * @param argSourceLines
     *            ソースコード。
     */
    public void transformClass(final BlancoCgClass cgClass,
            final BlancoCgSourceFile argSourceFile,
            final List<java.lang.String> argSourceLines) {
        // 最初にクラス情報をLangDocに展開。
        if (cgClass.getLangDoc() == null) {
            // LangDoc未指定の場合にはこちら側でインスタンスを生成。
            cgClass.setLangDoc(new BlancoCgLangDoc());
        }
        if (cgClass.getLangDoc().getTitle() == null) {
            cgClass.getLangDoc().setTitle(cgClass.getDescription());
        }

        // 次に LangDocをソースコード形式に展開。
        // new BlancoCgLangDocJsSourceExpander().transformLangDoc(cgClass
        // .getLangDoc(), argSourceLines);

        // アノテーションを展開。
        expandAnnotationList(cgClass, argSourceLines);

        // コンストラクタが存在するか、あるいはひとつも存在しないかどうかチェックします。
        boolean isConstructorExist = false;
        for (int index = 0; index < cgClass.getMethodList().size(); index++) {
            final BlancoCgMethod cgMethod = cgClass.getMethodList().get(index);
            if (cgMethod.getConstructor()) {
                isConstructorExist = true;
                break;
            }
        }
        if (isConstructorExist == false) {
            // クラスのコンストラクタがひとつも存在しない場合には、blancoCgの責務としてデフォルトコンストラクタを自前で生成する必要が出てきます。
            // これは JavaScriptの言語仕様としてのクラスの構造の特色に由来するものです。
            // 内容が空のメソッドとしてデフォルトコンストラクタを生成します。
            final BlancoCgMethod cgMethod = BlancoCgObjectFactory.getInstance()
                    .createMethod(cgClass.getName(), "デフォルトコンストラクタ");
            cgMethod.setConstructor(true);
            cgClass.getMethodList().add(cgMethod);
        }

        // クラスの情報は、クラスのコンストラクタへと移送します。
        // これは JavaScriptの言語仕様としてのクラスの構造の特色に由来するものです。
        // クラスの宣言部がコンストラクタそのものであるので、コンストラクタにクラスの情報を移送する必要があるからです。
        for (int index = 0; index < cgClass.getMethodList().size(); index++) {
            final BlancoCgMethod cgMethod = cgClass.getMethodList().get(index);
            if (cgMethod.getConstructor()) {
                // クラスの情報をコンストラクタに移送します。
                // ただしタイトルは移送しません。
                for (int indexClassLangDoc = 0; indexClassLangDoc < cgClass
                        .getLangDoc().getDescriptionList().size(); indexClassLangDoc++) {
                    cgMethod.getLangDoc().getDescriptionList().add(
                            cgClass.getLangDoc().getDescriptionList().get(
                                    indexClassLangDoc));
                }
                for (int indexClassLangDoc = 0; indexClassLangDoc < cgClass
                        .getLangDoc().getTagList().size(); indexClassLangDoc++) {
                    cgMethod.getLangDoc().getTagList().add(
                            cgClass.getLangDoc().getTagList().get(
                                    indexClassLangDoc));
                }
            }
        }

        // ここでメソッドを展開。
        expandMethodList(cgClass, argSourceFile, argSourceLines);

        argSourceLines.add("/* クラス[" + cgClass.getName() + "]宣言の終了。 */");
    }

    /**
     * アノテーションを展開します。
     * 
     * @param cgClass
     *            クラス。
     * @param argSourceLines
     *            ソースコード。
     */
    private void expandAnnotationList(final BlancoCgClass cgClass,
            final List<java.lang.String> argSourceLines) {
        for (int index = 0; index < cgClass.getAnnotationList().size(); index++) {
            final String strAnnotation = cgClass.getAnnotationList().get(index);

            throw new IllegalArgumentException(
                    "現バージョンの blancoCgは JavaScript言語の際にはアノテーションをサポートしません。"
                            + strAnnotation);
            // JavaScript言語のAnnotationは不明です。
            // argSourceLines.add("@" + strAnnotation);
        }
    }

    /**
     * クラスに含まれる各々のメソッドを展開します。
     * 
     * @param cgClass
     *            処理中のクラス。
     * @param argSourceFile
     *            ソースファイル。
     * @param argSourceLines
     *            ソースコード行リスト。
     */
    private void expandMethodList(final BlancoCgClass cgClass,
            final BlancoCgSourceFile argSourceFile,
            final List<java.lang.String> argSourceLines) {
        if (cgClass.getMethodList() == null) {
            throw new IllegalArgumentException("メソッドのリストにnullが与えられました。");
        }

        // 最初にコンストラクタを展開。
        for (int index = 0; index < cgClass.getMethodList().size(); index++) {
            final BlancoCgMethod cgMethod = cgClass.getMethodList().get(index);

            if (cgMethod.getConstructor()) {
                // コンストラクタのみを最初に展開します。
                new BlancoCgMethodJsSourceExpander().transformMethod(cgClass,
                        cgMethod, argSourceFile, argSourceLines);
            }
        }

        // クラスフィールド (staticなフィールド)を展開します。
        new BlancoCgMethodJsSourceExpander().transformStaticFieldList(cgClass,
                argSourceFile, argSourceLines);

        // 次に一般のメソッドを展開。
        for (int index = 0; index < cgClass.getMethodList().size(); index++) {
            final BlancoCgMethod cgMethod = cgClass.getMethodList().get(index);

            if (cgMethod.getConstructor() == false) {
                // コンストラクタ以外を展開します。
                new BlancoCgMethodJsSourceExpander().transformMethod(cgClass,
                        cgMethod, argSourceFile, argSourceLines);
            }
        }
    }
}
