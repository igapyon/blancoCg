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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import blanco.cg.BlancoCgSupportedLang;
import blanco.cg.util.BlancoCgLineUtil;
import blanco.cg.valueobject.BlancoCgSourceFile;

/**
 * BlancoCgSourceFileのなかの import情報を展開します。
 * 
 * このクラスはblancoCgのバリューオブジェクトからソースコードを自動生成するトランスフォーマーの個別の展開機能です。<br>
 * import展開は意外にも複雑な処理です。
 * 
 * @author IGA Tosiki
 */
class BlancoCgImportSwiftSourceExpander {
    /**
     * このクラスが処理対象とするプログラミング言語。
     */
    protected static final int TARGET_LANG = BlancoCgSupportedLang.SWIFT;

    /**
     * import文を展開するためのアンカー文字列。
     */
    private static final String REPLACE_IMPORT_HERE = "/*replace import here*/";

    /**
     * 発見されたアンカー文字列のインデックス。
     * 
     * このクラスの処理の過程で import文が編集されますが、その都度 この値も更新されます。
     */
    private int fFindReplaceImport = -1;

    /**
     * importを展開します。
     * 
     * このメソッドはクラス展開・メソッド展開など一式が終了した後に呼び出すようにします。
     * 
     * @param argSourceFile
     *            ソースファイルインスタンス。
     * @param argSourceLines
     *            ソース行イメージ。(java.lang.Stringが格納されます)
     */
    public void transformImport(final BlancoCgSourceFile argSourceFile,
            final List<java.lang.String> argSourceLines) {
        // import対象のクラス名終端に付与されている配列表現を除去します。
        trimArraySuffix(argSourceFile.getImportList());

        // import(using)のリストからクラス名を除去します。
        trimClassName(argSourceFile);

        // 最初にimport文をソートして処理を行いやすくします。
        sortImport(argSourceFile.getImportList());

        // 重複するimport文を除去します。
        trimRepeatedImport(argSourceFile.getImportList());

        // importする必要のないクラスを除去します
        trimUnnecessaryImport(argSourceFile.getImportList());

        // 自クラスが所属するパッケージに対するimportを抑制します。
        trimMyselfImport(argSourceFile, argSourceFile.getImportList());

        // アンカー文字列を検索します。
        fFindReplaceImport = findAnchorString(argSourceLines);
        if (fFindReplaceImport < 0) {
            throw new IllegalArgumentException("import文の置換文字列を発見することができませんでした。");
        }

        // 最初に「System」パッケージを展開します。
        expandImportWithTarget(argSourceFile, "System", argSourceLines);

        // 最後に「System」以外のパッケージを展開します。
        expandImportWithTarget(argSourceFile, null, argSourceLines);

        // アンカー文字列を除去します。
        removeAnchorString(argSourceLines);
    }

    /**
     * import(using)のリストからクラス名を除去します。
     * 
     * これは、C#.NETでは名前空間単位のusing指定を行うからです。<br>
     * まずクラス名を除去して皇族の処理を実行します。
     * 
     * @param argSourceFile
     *            ソースファイルオブジェクト。
     */
    private void trimClassName(final BlancoCgSourceFile argSourceFile) {
        for (int index = 0; index < argSourceFile.getImportList().size(); index++) {
            String strImport = argSourceFile.getImportList().get(index);
            final int findLastDot = strImport.lastIndexOf('.');
            if (findLastDot > 0) {
                strImport = strImport.substring(0, findLastDot);
            }
            argSourceFile.getImportList().set(index, strImport);
        }
    }

    /**
     * 展開対象となるターゲットを意識してインポートを展開します。
     * 
     * @param argSourceFile
     * @param argTarget
     *            java. または javax. または nullを指定します。
     * @param argSourceLines
     *            ソースコード行リスト。
     */
    private void expandImportWithTarget(final BlancoCgSourceFile argSourceFile,
            final String argTarget, final List<java.lang.String> argSourceLines) {
        boolean isProcessed = false;
        for (int index = 0; index < argSourceFile.getImportList().size(); index++) {
            final String strImport = argSourceFile.getImportList().get(index);

            if (argTarget == null) {
                // System. 以外を展開します。
                if (strImport.startsWith("System")) {
                    // 処理対象とするパッケージ以外であるので、処理をスキップします。
                    // ※System. はハードコードされている点に注意してください。
                    continue;
                }
            } else {
                if (strImport.startsWith(argTarget) == false) {
                    // 処理対象とするパッケージ以外であるので、処理をスキップします。
                    continue;
                }
            }

            isProcessed = true;
            argSourceLines.add(fFindReplaceImport++, "import " + strImport
                    + BlancoCgLineUtil.getTerminator(TARGET_LANG));
        }

        if (isProcessed) {
            // import展開処理が存在した場合にのみ空白を付与します。
            argSourceLines.add(fFindReplaceImport++, "");
        }
    }

    /**
     * 置換アンカー文字列の行数(0オリジン)を検索します。
     * 
     * @return 発見したアンカー文字列の位置(0オリジン)。発見できなかった場合には-1。
     * @param argSourceLines
     *            ソースリスト。
     */
    private static final int findAnchorString(
            final List<java.lang.String> argSourceLines) {
        for (int index = 0; index < argSourceLines.size(); index++) {
            final String line = argSourceLines.get(index);
            if (line.equals(REPLACE_IMPORT_HERE)) {
                // 発見しました。
                return index;
            }
        }

        // 発見できませんでした。発見できなかったことを示す -1 を戻します。
        return -1;
    }

    /**
     * アンカー文字列を挿入します。
     * 
     * 処理の後半でインポート文を編成しなおしますが、その際に参照するアンカー文字列を追加しておきます。<br>
     * このメソッドは他のクラスから呼び出されます。
     * 
     * @param argSourceLines
     *            ソースリスト。
     */
    public static final void insertAnchorString(
            final List<java.lang.String> argSourceLines) {
        argSourceLines.add(BlancoCgImportSwiftSourceExpander.REPLACE_IMPORT_HERE);
    }

    /**
     * アンカー文字列を除去します。
     * 
     * @param argSourceLines
     *            ソースリスト。
     */
    private static final void removeAnchorString(
            final List<java.lang.String> argSourceLines) {
        // 最後にアンカー文字列そのものを除去。
        int findReplaceImport2 = findAnchorString(argSourceLines);
        if (findReplaceImport2 < 0) {
            throw new IllegalArgumentException("import文の置換文字列を発見することができませんでした。");
        }
        argSourceLines.remove(findReplaceImport2);
    }

    /**
     * 与えられたimportをソートします。
     * 
     * 想定されるノードの型(java.lang.String)以外が与えられると、例外が発生します。
     * 
     * @param argImport
     *            インポートリスト。
     */
    private static final void sortImport(final List<java.lang.String> argImport) {
        Collections.sort(argImport, new Comparator<java.lang.String>() {
            public int compare(final String arg0, final String arg1) {
                if (arg0 instanceof String == false) {
                    throw new IllegalArgumentException("importのリストの値ですが、["
                            + arg0 + "]ですが java.lang.String以外の型["
                            + arg0.getClass().getName() + "]になっています。");
                }
                if (arg1 instanceof String == false) {
                    throw new IllegalArgumentException("importのリストの値ですが、["
                            + arg1 + "]ですが java.lang.String以外の型["
                            + arg1.getClass().getName() + "]になっています。");
                }
                final String str0 = (String) arg0;
                final String str1 = (String) arg1;
                return str0.compareTo(str1);
            }
        });
    }

    /**
     * import対象のクラス名終端に付与されている配列表現を除去します。
     * 
     * @param argImport
     *            インポートリスト。
     */
    private void trimArraySuffix(final List<java.lang.String> argImport) {
        for (int index = 0; index < argImport.size(); index++) {
            String strImport = argImport.get(index);
            for (;;) {
                // 配列表現で終了している限り繰り返します。
                if (strImport.endsWith("[]")) {
                    strImport = strImport.substring(0, strImport.length() - 2);
                    argImport.set(index, strImport);
                } else {
                    break;
                }
            }
        }
    }

    /**
     * 重複する不要なimportを除去します。
     * 
     * このメソッドは、与えられたListが既にソート済みであることを前提とします。
     * 
     * @param argImport
     *            インポートリスト。
     */
    private void trimRepeatedImport(final List<java.lang.String> argImport) {
        // 重複するimportを除去。
        String pastImport = "";
        for (int index = argImport.size() - 1; index >= 0; index--) {
            final String strImport = argImport.get(index);
            if (pastImport.equals(strImport)) {
                // 既に処理されている重複するimportです。不要なのでこれを除去します。
                argImport.remove(index);
            }
            // 今回のimportを前回分importとして記憶します。
            pastImport = strImport;
        }
    }

    /**
     * importする必要のないクラスを除去します。
     * 
     * 具体的には java.lang や プリミティブ型が不要と判断される対象です。
     * 
     * @param argImport
     *            インポートリスト。
     */
    private void trimUnnecessaryImport(final List<java.lang.String> argImport) {
        // まずはプリミティブ型を除去します。
        for (int index = argImport.size() - 1; index >= 0; index--) {
            // ソート時点で型チェックは実施済みです。
            final String strImport = argImport.get(index);

            if (BlancoCgTypeSwiftSourceExpander
                    .isLanguageReservedKeyword(strImport)) {
                argImport.remove(index);
            }
        }
    }

    /**
     * 自分自身が所属するパッケージのimportを除去します。
     * 
     * @param argSourceFile
     *            ソースファイルインスタンス。
     * @param argImport
     *            インポートリスト。
     */
    private void trimMyselfImport(final BlancoCgSourceFile argSourceFile,
            final List<java.lang.String> argImport) {
        trimSpecificPackage(argSourceFile.getPackage(), argImport);
    }

    /**
     * 特定のパッケージについて、これをリストから除去します。
     * 
     * 自クラスが所属するパッケージの除去に利用されます。
     * 
     * @param argSpecificPackage
     *            処理対象とするパッケージ。
     * @param argImport
     *            インポートのリスト。
     */
    private static void trimSpecificPackage(final String argSpecificPackage,
            final List<java.lang.String> argImport) {
        for (int index = argImport.size() - 1; index >= 0; index--) {
            // ソート時点で型チェックは実施済みです。
            final String strImport = argImport.get(index);

            // C#.NETでは名前空間が格納されています。名前空間同士を直接比較します。
            if (argSpecificPackage.equals(strImport)) {
                argImport.remove(index);
            }
        }
    }
}
