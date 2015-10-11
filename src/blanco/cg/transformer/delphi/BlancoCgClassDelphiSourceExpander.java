/*
 * blanco Framework
 * Copyright (C) 2004-2006 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.cg.transformer.delphi;

import java.util.ArrayList;
import java.util.List;

import blanco.cg.valueobject.BlancoCgClass;
import blanco.cg.valueobject.BlancoCgEnum;
import blanco.cg.valueobject.BlancoCgField;
import blanco.cg.valueobject.BlancoCgLangDoc;
import blanco.cg.valueobject.BlancoCgMethod;
import blanco.cg.valueobject.BlancoCgSourceFile;
import blanco.cg.valueobject.BlancoCgType;

/**
 * BlancoCgClassをソースコードへと展開します。
 * 
 * このクラスはblancoCgのバリューオブジェクトからソースコードを自動生成するトランスフォーマーの個別の展開機能です。
 * 
 * @author IGA Tosiki
 */
class BlancoCgClassDelphiSourceExpander {

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
        new BlancoCgLangDocDelphiSourceExpander().transformLangDoc(cgClass
                .getLangDoc(), argSourceLines);

        // アノテーションを展開。
        expandAnnotationList(cgClass, argSourceLines);

        final StringBuffer buf = new StringBuffer();

        // Delphiではclass自体に可視性を設定しません
        // if (BlancoStringUtil.null2Blank(cgClass.getAccess()).length() > 0) {
        // buf.append(cgClass.getAccess() + " ");
        // }
        // if (cgClass.getAbstract()) {
        // buf.append("abstract ");
        // }
        // if (cgClass.getFinal()) {
        // buf.append("final ");
        // }

        buf.append(cgClass.getName());

        // 親クラスを展開。
        expandExtendClassList(cgClass, argSourceFile, buf);

        // 親インタフェースを展開。
        expandImplementInterfaceList(cgClass, argSourceFile, buf);

        // 行を確定して書き出しを実施。
        argSourceLines.add(buf.toString());

        // クラスのブロックの開始。
        argSourceLines.add("");

        // ここで列挙体を展開。
        // expandEnumList(cgClass, argSourceFile, argSourceLines);

        // ここでフィールドを展開。
        expandFieldList(cgClass, argSourceFile, argSourceLines);

        // ここでメソッド宣言を展開。
        expandMethodDeclarationList(cgClass, argSourceFile, argSourceLines);

        // クラスのブロックの終了。
        argSourceLines.add("end;");

        // implementation
        argSourceLines.add("implementation");

        // ここでメソッドを展開。
        expandMethodList(cgClass, argSourceFile, argSourceLines);

        // end.
        argSourceLines.add("end.");

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
            // C#.NET言語のAnnotationは []で記述します。
            argSourceLines.add("[" + strAnnotation + "]");
        }
    }

    /**
     * 親クラスを展開します。
     * 
     * ※BlancoCgInterface展開の際に、このメソッドを共通処理として呼び出してはなりません。
     * その共通化は、かえって理解を妨げると判断しています。
     * 
     * @param cgClass
     *            クラスのバリューオブジェクト。
     * @param argBuf
     *            出力先文字列バッファ。
     */
    private void expandExtendClassList(final BlancoCgClass cgClass,
            final BlancoCgSourceFile argSourceFile, final StringBuffer argBuf) {
        if (cgClass.getExtendClassList().size() == 0) {
            argBuf.append(" = class(TObject)");
            return;
        }
        for (int index = 0; index < cgClass.getExtendClassList().size(); index++) {
            final BlancoCgType type = cgClass.getExtendClassList().get(index);

            // import文に型を追加。
            // argSourceFile.getImportList().add(type.getName());

            if (index == 0) {
                argBuf.append(" = class("
                        + BlancoCgTypeDelphiSourceExpander.toTypeString(type)
                        + ")");
            } else {
                // TODO C#.NETの継承が一度きりだったかどうか確認を実施すること。
                // throw new
                // IllegalArgumentException("C#.NET言語では継承は一回しか実施できません。");

                // TODO 現時点では多重継承がOKであると想定します。
                argBuf.append(", "
                        + BlancoCgTypeDelphiSourceExpander.toTypeString(type));
            }
        }
    }

    /**
     * 親インタフェースを展開します。
     * 
     * @param cgClass
     *            処理中のクラス。
     * @param argBuf
     *            出力先文字列バッファ。
     */
    private void expandImplementInterfaceList(final BlancoCgClass cgClass,
            final BlancoCgSourceFile argSourceFile, final StringBuffer argBuf) {
        for (int index = 0; index < cgClass.getImplementInterfaceList().size(); index++) {
            final BlancoCgType type = cgClass.getImplementInterfaceList().get(
                    index);

            // import文に型を追加。
            // argSourceFile.getImportList().add(type.getName());

            if (index == 0 && cgClass.getExtendClassList().size() == 0) {
                // 最初のインタフェースで、且つ継承が無い場合に : を出力します。
                argBuf.append(" : ");
            } else {
                argBuf.append(", ");
            }
            argBuf.append(BlancoCgTypeDelphiSourceExpander.toTypeString(type));
        }
    }

    /**
     * クラスに含まれる各々の列挙体を展開します。
     * 
     * @param cgClass
     *            処理中のクラス。
     * @param argSourceFile
     *            ソースファイル。
     * @param argSourceLines
     *            ソースコード行リスト。
     */
    private void expandEnumList(final BlancoCgClass cgClass,
            final BlancoCgSourceFile argSourceFile,
            final List<java.lang.String> argSourceLines) {
        if (cgClass.getEnumList() == null) {
            return;
        }

        for (BlancoCgEnum cgEnum : cgClass.getEnumList()) {
            new BlancoCgEnumDelphiSourceExpander().transformEnum(cgEnum,
                    argSourceFile, argSourceLines);
        }
    }

    /**
     * クラスに含まれる各々のフィールドを展開します。
     * 
     * TODO 定数宣言を優先して展開し、その後変数宣言を展開するなどの工夫が必要です。<br>
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

        List<BlancoCgField> publishedList = new ArrayList<BlancoCgField>();
        List<BlancoCgField> publicList = new ArrayList<BlancoCgField>();
        List<BlancoCgField> privateList = new ArrayList<BlancoCgField>();

        for (int index = 0; index < cgClass.getFieldList().size(); index++) {
            final BlancoCgField cgField = cgClass.getFieldList().get(index);

            String access = cgField.getAccess();

            if (isPublished(access)) {
                publishedList.add(cgField);
            } else if (isPublic(access)) {
                publicList.add(cgField);
            } else if (isPrivate(access)) {
                privateList.add(cgField);
            } else {
                // サポートされない
            }
        }

        // published フィールドの展開
        if (publishedList.size() > 0) {
            argSourceLines.add("published");
        }
        for (int index = 0; index < publishedList.size(); index++) {
            final BlancoCgField cgField = publishedList.get(index);
            // クラスのフィールドとして展開を行います。
            new BlancoCgFieldDelphiSourceExpander().transformField(cgField,
                    argSourceFile, argSourceLines, false);
        }

        // public フィールドの展開
        if (publicList.size() > 0) {
            argSourceLines.add("public");
        }
        for (int index = 0; index < publicList.size(); index++) {
            final BlancoCgField cgField = publicList.get(index);
            // クラスのフィールドとして展開を行います。
            new BlancoCgFieldDelphiSourceExpander().transformField(cgField,
                    argSourceFile, argSourceLines, false);
        }

        // private フィールドの展開
        if (privateList.size() > 0) {
            argSourceLines.add("private");
        }
        for (int index = 0; index < privateList.size(); index++) {
            final BlancoCgField cgField = privateList.get(index);
            // クラスのフィールドとして展開を行います。
            new BlancoCgFieldDelphiSourceExpander().transformField(cgField,
                    argSourceFile, argSourceLines, false);
        }
    }

    /**
     * クラスに含まれる各々のメソッド宣言を展開します。
     * 
     * @param cgClass
     *            処理中のクラス。
     * @param argSourceFile
     *            ソースファイル。
     * @param argSourceLines
     *            ソースコード行リスト。
     */
    private void expandMethodDeclarationList(final BlancoCgClass cgClass,
            final BlancoCgSourceFile argSourceFile,
            final List<java.lang.String> argSourceLines) {
        if (cgClass.getMethodList() == null) {
            throw new IllegalArgumentException("メソッドのリストにnullが与えられました。");
        }

        List<BlancoCgMethod> publishedList = new ArrayList<BlancoCgMethod>();
        List<BlancoCgMethod> publicList = new ArrayList<BlancoCgMethod>();
        List<BlancoCgMethod> privateList = new ArrayList<BlancoCgMethod>();

        for (int index = 0; index < cgClass.getMethodList().size(); index++) {
            final BlancoCgMethod cgMethod = cgClass.getMethodList().get(index);
            String access = cgMethod.getAccess();

            if (isPublished(access)) {
                publishedList.add(cgMethod);
            } else if (isPublic(access)) {
                publicList.add(cgMethod);
            } else if (isPrivate(access)) {
                privateList.add(cgMethod);
            } else {
                // サポートされない
            }
        }

        // published メソッドの展開
        if (publishedList.size() > 0) {
            // 改行を付与。
            argSourceLines.add("");
            argSourceLines.add("published");
        }
        for (int index = 0; index < publishedList.size(); index++) {
            final BlancoCgMethod cgMethod = publishedList.get(index);
            // クラスのフィールドとして展開を行います。
            new BlancoCgMethodDelphiSourceExpander()
                    .transformMethodDeclaration(cgMethod, argSourceFile,
                            argSourceLines, false);
        }

        // public メソッドの展開
        if (publicList.size() > 0) {
            // 改行を付与。
            argSourceLines.add("");
            argSourceLines.add("public");
        }
        for (int index = 0; index < publicList.size(); index++) {
            final BlancoCgMethod cgMethod = publicList.get(index);
            // クラスのフィールドとして展開を行います。
            new BlancoCgMethodDelphiSourceExpander()
                    .transformMethodDeclaration(cgMethod, argSourceFile,
                            argSourceLines, false);
        }

        // private メソッドの展開
        if (privateList.size() > 0) {
            // 改行を付与。
            argSourceLines.add("");
            argSourceLines.add("private");
        }
        for (int index = 0; index < privateList.size(); index++) {
            final BlancoCgMethod cgMethod = privateList.get(index);
            // クラスのフィールドとして展開を行います。
            new BlancoCgMethodDelphiSourceExpander()
                    .transformMethodDeclaration(cgMethod, argSourceFile,
                            argSourceLines, false);
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
        for (int index = 0; index < cgClass.getMethodList().size(); index++) {
            final BlancoCgMethod cgMethod = cgClass.getMethodList().get(index);
            // クラスのメソッドとして展開を行います。
            new BlancoCgMethodDelphiSourceExpander().transformMethod(cgClass
                    .getName(), cgMethod, argSourceFile, argSourceLines, false);
        }
    }

    private boolean isPrivate(String access) {
        return "private".equals(access);
    }

    private boolean isPublic(String access) {
        return "public".equals(access);
    }

    private boolean isPublished(String access) {
        return "".equals(access) || "published".equals(access);
    }
}
