/*
 * blanco Framework
 * Copyright (C) 2004-2006 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.cg.transformer.cs;

import java.util.ArrayList;
import java.util.List;

import blanco.cg.BlancoCgSupportedLang;
import blanco.cg.util.BlancoCgLineUtil;
import blanco.cg.valueobject.BlancoCgException;
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
class BlancoCgMethodCsSourceExpander {
    /**
     * このクラスが処理対象とするプログラミング言語。
     */
    protected static final int TARGET_LANG = BlancoCgSupportedLang.CS;

    /**
     * ここでメソッドを展開します。
     * 
     * @param cgMethod
     *            処理対象となるメソッド。
     * @param argSourceFile
     *            ソースファイル。
     * @param argSourceLines
     *            出力先行リスト。
     * @param argIsInterface
     *            インタフェースかどうか。クラスの場合にはfalse。インタフェースの場合にはtrue。
     */
    public void transformMethod(final BlancoCgMethod cgMethod,
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
        new BlancoCgLangDocCsSourceExpander().transformLangDoc(cgMethod
                .getLangDoc(), argSourceLines);

        // アノテーションを展開。
        expandAnnotationList(cgMethod, argSourceLines);

        // メソッドの本体部分を展開。
        expandMethodBody(cgMethod, argSourceFile, argSourceLines,
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
     * @param cgMethod
     *            メソッドオブジェクト。
     * @param argSourceLines
     *            ソースコード。
     * @param argIsInterface
     *            インタフェースとして展開するかどうか。
     */
    private void expandMethodBody(final BlancoCgMethod cgMethod,
            final BlancoCgSourceFile argSourceFile,
            final List<java.lang.String> argSourceLines,
            final boolean argIsInterface) {
        final StringBuffer buf = new StringBuffer();

        if (BlancoStringUtil.null2Blank(cgMethod.getAccess()).length() > 0) {
            if (argIsInterface && cgMethod.getAccess().equals("public")) {
                // インタフェース且つpublicの場合には出力を抑制します。
                // Javaと同様に C#でも出力は抑制します。
            } else {
                buf.append(cgMethod.getAccess() + " ");
            }
        }

        if (cgMethod.getAbstract() && argIsInterface == false) {
            // ※インタフェースの場合には abstractは付与しません。
            buf.append("abstract ");
        }
        if (cgMethod.getOverride()) {
            // C#.NETには override 修飾が存在します。
            buf.append("override ");
        }
        if (isVirtual(cgMethod, argIsInterface)) {
            // ※koyak さんの貢献箇所。
            // C#.NET では、継承クラスでメソッドをオーバーライドするには必ず基底クラスのメソッドが virtual
            // 修飾されている必要があります。
            // このため、メソッドが override でなければ virtual とします。
            buf.append("virtual ");
        }
        if (cgMethod.getStatic()) {
            buf.append("static ");
        }
        if (cgMethod.getFinal() && argIsInterface == false) {
            // ※インタフェースの場合には finalは付与しません。
            buf.append("final ");
        }

        if (cgMethod.getConstructor()) {
            // コンストラクタの場合には、戻り値は存在しません。
            // このため、ここでは何も出力しません。
        } else {
            if (cgMethod.getReturn() != null
                    && cgMethod.getReturn().getType() != null) {
                buf.append(BlancoCgTypeCsSourceExpander.toTypeString(cgMethod
                        .getReturn().getType())
                        + " ");
            } else {
                buf.append("void ");
            }
        }

        buf.append(cgMethod.getName() + "(");
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

            // パラメータのアノテーションを展開。
            if (cgParameter.getAnnotationList() != null) {
                for (int indexAnnotation = 0; indexAnnotation < cgParameter
                        .getAnnotationList().size(); indexAnnotation++) {
                    // C#.NET言語のAnnotationは []で記述します。
                    final String strAnnotation = cgParameter
                            .getAnnotationList().get(indexAnnotation);

                    // C#.NET言語のAnnotationは []で記述します。
                    buf.append("[" + strAnnotation + "] ");
                }
            }

            if (cgParameter.getFinal()) {
                // C#.NETにおけるfinalはreadonly表現となります。ただし限定的なので、現時点では展開を抑制します。
                // buf.append("readonly ");
            }
            buf.append(BlancoCgTypeCsSourceExpander.toTypeString(cgParameter
                    .getType()));
            buf.append(" ");
            buf.append(cgParameter.getName());
        }
        buf.append(")");

        // C#.NETには base()記述が存在します。
        if (BlancoStringUtil.null2Blank(cgMethod.getSuperclassInvocation())
                .length() > 0) {
            // getSuperclassInvocationには base(message)などのような記載がおこなわれます。
            // TODO C#.NETでこの記載が可能なのはコンストラクタだけである模様です。
            buf.append(" : " + cgMethod.getSuperclassInvocation());
        }

        // C#.NETには例外スローのメソッド修飾はありません。
        // TODO 例外スロー情報を 言語ドキュメントに出力することには意義があると考えます。

        if (cgMethod.getAbstract() || argIsInterface) {
            // 抽象メソッドまたはインタフェースの場合には、メソッドの本体を展開しません。
            buf.append(BlancoCgLineUtil.getTerminator(TARGET_LANG));
            argSourceLines.add(buf.toString());
        } else {
            // ここでいったん、行を確定。
            argSourceLines.add(buf.toString());

            // メソッドブロックの開始。
            argSourceLines.add("{");

            // パラメータの非null制約の展開。
            expandParameterCheck(cgMethod, argSourceFile, argSourceLines);

            // 行を展開します。
            expandLineList(cgMethod, argSourceLines);

            // メソッドブロックの終了。
            argSourceLines.add("}");
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
        for (int index = 0; index < cgMethod.getAnnotationList().size(); index++) {
            final String strAnnotation = cgMethod.getAnnotationList()
                    .get(index);

            // C#.NET言語のAnnotationは []で記述します。
            argSourceLines.add("[" + strAnnotation + "]");
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
            final BlancoCgSourceFile argSourceFile,
            final List<java.lang.String> argSourceLines) {
        boolean isProcessed = false;
        for (int index = 0; index < cgMethod.getParameterList().size(); index++) {
            final BlancoCgParameter cgParameter = cgMethod.getParameterList()
                    .get(index);
            if (cgParameter.getNotnull()) {
                isProcessed = true;
                argSourceFile.getImportList().add("System.ArgumentException");

                argSourceLines.add(BlancoCgLineUtil.getIfBegin(TARGET_LANG,
                        cgParameter.getName() + " == null"));
                argSourceLines.add("throw new ArgumentException(\"メソッド["
                        + cgMethod.getName() + "]のパラメータ["
                        + cgParameter.getName()
                        + "]にnullが与えられました。しかし、このパラメータにnullを与えることはできません。\");");
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

    /**
     * メソッドを virtual 修飾するかどうかを判断する。
     * 
     * @param cgMethod
     *            メソッド情報。
     * @param argIsInterface
     *            インタフェースかどうか。
     * @return trueの場合には virtual 修飾をおこなう。
     */
    private boolean isVirtual(final BlancoCgMethod cgMethod,
            final boolean argIsInterface) {
        if (cgMethod.getAbstract() == false && cgMethod.getOverride() == false
                && cgMethod.getFinal() == false
                && cgMethod.getConstructor() == false
                && cgMethod.getStatic() == false && argIsInterface == false) {
            return true;
        }
        return false;
    }
}
