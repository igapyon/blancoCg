/*
 * blanco Framework
 * Copyright (C) 2004-2006 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.cg.transformer.php;

import java.util.ArrayList;
import java.util.List;

import blanco.cg.BlancoCgSupportedLang;
import blanco.cg.util.BlancoCgLineUtil;
import blanco.cg.valueobject.BlancoCgException;
import blanco.cg.valueobject.BlancoCgLangDoc;
import blanco.cg.valueobject.BlancoCgMethod;
import blanco.cg.valueobject.BlancoCgParameter;
import blanco.cg.valueobject.BlancoCgSourceFile;
import blanco.commons.util.BlancoNameUtil;
import blanco.commons.util.BlancoStringUtil;

/**
 * BlancoCgMethodをソースコードに展開します。
 * 
 * このクラスはblancoCgのバリューオブジェクトからソースコードを自動生成するトランスフォーマーの個別の展開機能です。
 * 
 * @author IGA Tosiki
 */
class BlancoCgMethodPhpSourceExpander {
    /**
     * このクラスが処理対象とするプログラミング言語。
     */
    protected static final int TARGET_LANG = BlancoCgSupportedLang.PHP;

    /**
     * ここでメソッドを展開します。
     * 
     * @param strClassName
     *            クラス名。
     * @param cgMethod
     *            処理対象となるメソッド。
     * @param argSourceFile
     *            ソースファイル。
     * @param argSourceLines
     *            出力先行リスト。
     * @param argIsInterface
     *            インタフェースかどうか。クラスの場合にはfalse。インタフェースの場合にはtrue。
     */
    public void transformMethod(final String strClassName,
            final BlancoCgMethod cgMethod,
            final BlancoCgSourceFile argSourceFile,
            final List<java.lang.String> argSourceLines,
            final boolean argIsInterface) {
        if (BlancoStringUtil.null2Blank(cgMethod.getName()).length() == 0) {
            throw new IllegalArgumentException("メソッドの名前に適切な値が設定されていません。");
        }
        if (cgMethod.getReturn() == null) {
            // それはありえます。voidの場合にはnullが指定されるのです。
        }

        // 改行を付与。
        argSourceLines.add("");

        prepareExpand(cgMethod, argSourceFile);

        // 情報が一式そろったので、ソースコードの実際の展開を行います。

        // 次に LangDocをソースコード形式に展開。
        new BlancoCgLangDocPhpSourceExpander().transformLangDoc(cgMethod
                .getLangDoc(), argSourceLines);

        // アノテーションを展開。
        expandAnnotationList(cgMethod, argSourceLines);

        // メソッドの本体部分を展開。
        expandMethodBody(strClassName, cgMethod, argSourceFile, argSourceLines,
                argIsInterface);
    }

    /**
     * ソースコード展開に先立ち、必要な情報の収集を行います。
     * 
     * @param cgMethod
     *            メソッドオブジェクト。
     * @param argSourceFile
     *            ソースファイル。
     */
    private void prepareExpand(final BlancoCgMethod cgMethod,
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
     * @param strClassName
     *            クラス名。
     * @param cgMethod
     *            メソッドオブジェクト。
     * @param argSourceLines
     *            ソースコード。
     * @param argIsInterface
     *            インタフェースとして展開するかどうか。
     */
    private void expandMethodBody(final String strClassName,
            final BlancoCgMethod cgMethod,
            final BlancoCgSourceFile argSourceFile,
            final List<java.lang.String> argSourceLines,
            final boolean argIsInterface) {
        final StringBuffer buf = new StringBuffer();

        if (BlancoStringUtil.null2Blank(cgMethod.getAccess()).length() > 0) {
            if (argIsInterface && cgMethod.getAccess().equals("public")) {
                // インタフェース且つpublicの場合には出力を抑制します。
                // これはCheckstyle対策となります。
            } else {
                buf.append(cgMethod.getAccess() + " ");
            }
        }

        if (cgMethod.getAbstract() && argIsInterface == false) {
            // ※インタフェースの場合には abstractは付与しません。
            buf.append("abstract ");
        }
        if (cgMethod.getStatic()) {
            buf.append("static ");
        }
        if (cgMethod.getFinal() && argIsInterface == false) {
            // ※インタフェースの場合には finalは付与しません。
            buf.append("final ");
        }

        // PHPの場合には、コンストラクタの場合にも、戻り値を出力させます。
        // これは PHPLintの現状の仕様に対応させるためのものです。
        if (cgMethod.getReturn() != null
                && cgMethod.getReturn().getType() != null) {
            buf.append("/*."
                    + BlancoCgTypePhpSourceExpander
                            .toPhpLintType(BlancoCgTypePhpSourceExpander
                                    .toTypeString(cgMethod.getReturn()
                                            .getType())) + ".*/ ");
        } else {
            buf.append("/*.void.*/ ");
        }

        buf.append("function ");

        if (cgMethod.getConstructor()) {
            // コンストラクタの場合、メソッド名は利用されません。
            buf.append("__construct(");
        } else {
            buf.append(cgMethod.getName() + "(");
        }

        for (int index = 0; index < cgMethod.getParameterList().size(); index++) {
            final BlancoCgParameter cgParameter = cgMethod.getParameterList()
                    .get(index);
            if (cgParameter.getType() == null) {
                throw new IllegalArgumentException("メソッド[" + cgMethod.getName()
                        + "]のパラメータ[" + cgParameter.getName()
                        + "]の型にnullが与えられました。");
            }

            if (index != 0) {
                buf.append(", ");
            }

            if (cgParameter.getFinal()) {
                // PHP5ではパラメータのfinalはありません。
                // buf.append("final ");
            }
            buf.append("/*.");
            buf.append(BlancoCgTypePhpSourceExpander
                    .toPhpLintType(BlancoCgTypePhpSourceExpander
                            .toTypeString(cgParameter.getType())));
            buf.append(".*/ ");
            buf.append("$" + cgParameter.getName());
        }
        buf.append(")");

        // 例外スローを展開。
        expandThrowList(cgMethod, buf);

        if (cgMethod.getAbstract() || argIsInterface) {
            // 抽象メソッドまたはインタフェースの場合には、メソッドの本体を展開しません。
            buf.append(BlancoCgLineUtil.getTerminator(TARGET_LANG));
            argSourceLines.add(buf.toString());
        } else {
            // メソッドブロックの開始。
            buf.append(" {");

            // ここでいったん、行を確定。
            argSourceLines.add(buf.toString());

            argSourceLines.add("/* パラメータの数、型チェックを行います。 */");
            argSourceLines.add(BlancoCgLineUtil
                    .getIfBegin(TARGET_LANG, "func_num_args() !== "
                            + cgMethod.getParameterList().size()));

            // standardをimport
            argSourceFile.getImportList().add("standard.Exception");
            argSourceLines.add("throw new \\Exception("
                    + BlancoCgLineUtil.getStringLiteralEnclosure(TARGET_LANG)
                    + "[ArgumentException]: " + strClassName + "."
                    + cgMethod.getName() + " のパラメータは["
                    + cgMethod.getParameterList().size()
                    + "]個である必要があります。しかし実際には["
                    + BlancoCgLineUtil.getStringLiteralEnclosure(TARGET_LANG)
                    + " . func_num_args() .  "
                    + BlancoCgLineUtil.getStringLiteralEnclosure(TARGET_LANG)
                    + "]個のパラメータを伴って呼び出されました。"
                    + BlancoCgLineUtil.getStringLiteralEnclosure(TARGET_LANG)
                    + ");");
            argSourceLines.add(BlancoCgLineUtil.getIfEnd(TARGET_LANG));

            for (int indexParameter = 0; indexParameter < cgMethod
                    .getParameterList().size(); indexParameter++) {
                final BlancoCgParameter cgParameter = cgMethod
                        .getParameterList().get(indexParameter);
                if (BlancoCgTypePhpSourceExpander
                        .isLanguageReservedKeyword(BlancoStringUtil
                                .null2Blank(cgParameter.getType().getName()))) {
                    String typeName = cgParameter.getType().getName();
                    if (typeName.equals("float")) {
                        // PHPは歴史的な理由により floatはdoubleを返すとのこと。
                        // 参考: http://www.php.net/manual/ja/function.gettype.php
                        typeName = "double";
                    }
                    argSourceLines
                            .add(BlancoCgLineUtil
                                    .getIfBegin(
                                            TARGET_LANG,
                                            "gettype($"
                                                    + cgParameter.getName()
                                                    + ") !== "
                                                    + BlancoCgLineUtil
                                                            .getStringLiteralEnclosure(TARGET_LANG)
                                                    + typeName
                                                    + BlancoCgLineUtil
                                                            .getStringLiteralEnclosure(TARGET_LANG)
                                                    + " && gettype($"
                                                    + cgParameter.getName()
                                                    + ") !== "
                                                    + BlancoCgLineUtil
                                                            .getStringLiteralEnclosure(TARGET_LANG)
                                                    + "NULL"
                                                    + BlancoCgLineUtil
                                                            .getStringLiteralEnclosure(TARGET_LANG)));
                } else {
                    argSourceLines.add(BlancoCgLineUtil.getIfBegin(TARGET_LANG,
                            "$" + cgParameter.getName() + " instanceof "
                                    + cgParameter.getType().getName()
                                    + " === FALSE"));
                }
                argSourceLines.add("throw new \\Exception("
                        + BlancoCgLineUtil
                                .getStringLiteralEnclosure(TARGET_LANG)
                        + "[ArgumentException]: "
                        + strClassName
                        + "."
                        + cgMethod.getName()
                        + " の"
                        + (indexParameter + 1)
                        + "番目のパラメータは["
                        + cgParameter.getType().getName()
                        + "]型でなくてはなりません。しかし実際には["
                        + BlancoCgLineUtil
                                .getStringLiteralEnclosure(TARGET_LANG)
                        + " "
                        + BlancoCgLineUtil
                                .getStringConcatenationOperator(TARGET_LANG)
                        + " "
                        + (BlancoCgTypePhpSourceExpander
                                .isLanguageReservedKeyword(BlancoStringUtil
                                        .null2Blank(cgParameter.getType()
                                                .getName())) ? "gettype("
                                : "get_class(")
                        + "$"
                        + cgParameter.getName()
                        + ") "
                        + BlancoCgLineUtil
                                .getStringConcatenationOperator(TARGET_LANG)
                        + " "
                        + BlancoCgLineUtil
                                .getStringLiteralEnclosure(TARGET_LANG)
                        + "]型が与えられました。"
                        + BlancoCgLineUtil
                                .getStringLiteralEnclosure(TARGET_LANG) + ");");
                argSourceLines.add(BlancoCgLineUtil.getIfEnd(TARGET_LANG));
            }

            argSourceLines.add("");

            // 親クラスメソッド実行機能の展開。
            // パラメータチェックより後に展開。
            if (BlancoStringUtil.null2Blank(cgMethod.getSuperclassInvocation())
                    .length() > 0) {
                // super(引数) などが含まれます。
                argSourceLines.add(cgMethod.getSuperclassInvocation()
                        + BlancoCgLineUtil.getTerminator(TARGET_LANG));
            }

            argSourceLines.add("");

            // パラメータの非null制約の展開。
            expandParameterCheck(cgMethod, argSourceLines);

            // 行を展開します。
            expandLineList(cgMethod, argSourceLines);

            // メソッドブロックの終了。
            argSourceLines.add("}");
        }
    }

    /**
     * 例外スローを展開します。
     * 
     * @param cgMethod
     *            メソッド。
     * @param buf
     *            出力バッファ。
     */
    private void expandThrowList(final BlancoCgMethod cgMethod,
            final StringBuffer buf) {
        for (int index = 0; index < cgMethod.getThrowList().size(); index++) {
            final BlancoCgException cgException = cgMethod.getThrowList().get(
                    index);

            if (index == 0) {
                buf.append(" throws ");
            } else {
                buf.append(", ");
            }
            // 言語ドキュメント処理においては、blancoCgのTypeに関する共通処理を利用することはできません。
            // 個別に記述を行います。
            buf.append(BlancoNameUtil.trimJavaPackage(cgException.getType()
                    .getName()));
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
            // Java言語では overrideはアノテーションで表現します。
            argSourceLines.add("@Override");
        }

        for (int index = 0; index < cgMethod.getAnnotationList().size(); index++) {
            final String strAnnotation = cgMethod.getAnnotationList()
                    .get(index);

            // Java言語のAnnotationは @ から記述します。
            argSourceLines.add("@" + strAnnotation);
        }
    }

    /**
     * パラメータの非null制約の展開。
     * 
     * @param cgMethod
     *            メソッド。
     * @param argSourceLines
     *            ソースコード。
     */
    private void expandParameterCheck(final BlancoCgMethod cgMethod,
            final List<java.lang.String> argSourceLines) {
        boolean isProcessed = false;
        for (int index = 0; index < cgMethod.getParameterList().size(); index++) {
            final BlancoCgParameter cgParameter = cgMethod.getParameterList()
                    .get(index);
            if (cgParameter.getNotnull()) {
                isProcessed = true;

                argSourceLines.add(BlancoCgLineUtil.getIfBegin(TARGET_LANG, "$"
                        + cgParameter.getName() + " === null"));
                argSourceLines.add("throw new \\Exception('メソッド["
                        + cgMethod.getName() + "]のパラメータ["
                        + cgParameter.getName()
                        + "]にnullが与えられました。しかし、このパラメータにnullを与えることはできません。');");
                argSourceLines.add(BlancoCgLineUtil.getIfEnd(TARGET_LANG));
            }
        }

        if (isProcessed) {
            // パラメータチェックが展開された場合には空行を挿入します。
            argSourceLines.add("");
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
