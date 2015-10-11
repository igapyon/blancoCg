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

import blanco.cg.valueobject.BlancoCgType;
import blanco.commons.util.BlancoNameUtil;
import blanco.commons.util.BlancoStringUtil;

/**
 * BlancoCgTypeをソースコードに展開します。
 * 
 * このクラスはblancoCgのバリューオブジェクトからソースコードを自動生成するトランスフォーマーの個別の展開機能です。
 * 
 * @author IGA Tosiki
 */
class BlancoCgTypeCsSourceExpander {
    /**
     * プログラミング言語の予約語一覧。
     */
    private static final String[] LANGUAGE_RESERVED_KEYWORD = { "void", "byte",
            "short", "int", "long", "char", "float", "double", "decimal",
            "bool", "string" };

    /**
     * blancoCg型を、具体的な文字列へと変換します。
     * 
     * 配列を表す[]やジェネリクスも展開します。<br>
     * TODO 複数パッケージ間での同一クラス名(例:java.util.Dateとjava.sql.Dateなど)は考慮していません。
     * 複数パッケージの同一クラス名を一つのソースファイル内で利用する為の諸機能は未提供です。
     * 
     * @param argType
     *            blancoCg上の型。
     * @return プログラミング言語における型を示す文字列。
     */
    public static String toTypeString(final BlancoCgType argType) {
        final StringBuffer buf = new StringBuffer();
        buf.append(BlancoNameUtil.trimJavaPackage(argType.getName()));

        // 配列を展開します。
        if (argType.getArray()) {
            buf.append("[");
            for (int dimension = 1; dimension < argType.getArrayDimension(); dimension++) {
                buf.append(",");
            }
            buf.append("]");
        }

        // ジェネリクスを展開します。
        if (BlancoStringUtil.null2Blank(argType.getGenerics()).length() > 0) {
            // TODO C#.NETのジェネリクスを再度精査する必要があります。
            buf.append(argType.getGenerics());
        }

        return buf.toString();
    }

    /**
     * 与えられた文字列がプログラミング言語の予約語であるかどうかをチェックします。
     * 
     * @param argCheck
     *            チェックしたい文字列。
     * @return プログラミング言語の予約語に該当したかどうか。
     */
    public static boolean isLanguageReservedKeyword(final String argCheck) {
        for (int index = 0; index < LANGUAGE_RESERVED_KEYWORD.length; index++) {
            if (LANGUAGE_RESERVED_KEYWORD[index].equals(argCheck)) {
                // この文字列はプログラミング言語の予約語です。
                return true;
            }
        }

        // キーワードにヒットしませんでした。この文字列はプログラミング言語の予約語ではありません。
        return false;
    }
}
