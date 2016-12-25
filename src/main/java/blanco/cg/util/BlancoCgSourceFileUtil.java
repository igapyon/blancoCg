/*
 * blanco Framework
 * Copyright (C) 2004-2009 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.cg.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import blanco.cg.resourcebundle.BlancoCgResourceBundle;

/**
 * blancoCg のソースファイル用のユーティリティ・クラス。
 * 
 * @author IGA Tosiki
 */
public class BlancoCgSourceFileUtil {
    /**
     * リソースバンドル・メッセージを扱うためのクラス。
     */
    protected static final BlancoCgResourceBundle fBundle = new BlancoCgResourceBundle();

    /**
     * デフォルトのファイル・コメントの取得。
     * 
     * <UL>
     * <LI>相対パス meta/program/fileheader.txt があれば、これを優先利用します。(UTF-8
     * で記載されている必要があります)
     * <LI>リソースバンドルの指定を利用します。
     * </UL>
     * 
     * @return デフォルトのファイル・コメントの配列。
     */
    public static List<String> getDefaultFileComment() {
        final List<String> result = new ArrayList<String>();
        try {
            final File file = new File(fBundle.getFileHeaderPath());
            if (file.exists() == true && file.isFile() && file.canRead()) {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(
                            new FileInputStream(file), "UTF-8"));
                    for (;;) {
                        final String line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        result.add(line);
                    }
                } finally {
                    if (reader != null) {
                        reader.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result.size() == 0) {
            result.add(fBundle.getDefaultFileComment());
        }

        return result;
    }
}
