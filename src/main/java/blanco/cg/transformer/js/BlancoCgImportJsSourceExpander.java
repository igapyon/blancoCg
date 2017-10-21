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

import blanco.cg.valueobject.BlancoCgSourceFile;

/**
 * BlancoCgSourceFileのなかの import情報を展開します。
 * 
 * このクラスはblancoCgのバリューオブジェクトからソースコードを自動生成するトランスフォーマーの個別の展開機能です。<br>
 * import展開は意外にも複雑な処理です。
 * 
 * @author IGA Tosiki
 */
class BlancoCgImportJsSourceExpander {
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
        // アンカー文字列を検索します。
        fFindReplaceImport = findAnchorString(argSourceLines);
        if (fFindReplaceImport < 0) {
            throw new IllegalArgumentException("import文の置換文字列を発見することができませんでした。");
        }

        // アンカー文字列を除去します。
        removeAnchorString(argSourceLines);
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
        argSourceLines.add(BlancoCgImportJsSourceExpander.REPLACE_IMPORT_HERE);
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
}
