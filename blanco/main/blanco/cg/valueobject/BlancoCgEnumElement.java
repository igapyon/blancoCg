/*
 * blanco Framework
 * Copyright (C) 2004-2009 IGA Tosiki
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.cg.valueobject;

/**
 * enumの各要素を表現するためのバリューオブジェクト。
 */
public class BlancoCgEnumElement {
    /**
     * この列挙体の要素の名前です。
     *
     * フィールド: [name]。
     */
    private String fName;

    /**
     * この列挙体の要素の説明です。
     *
     * フィールド: [description]。
     */
    private String fDescription;

    /**
     * (C#.NETのみ対応) デフォルト値をあらわします。
     *
     * Stringなら ""、intなら 3 などのように実際の文を指定します。
     * (ダブルクオートなども含んだ形で表現します。)
     * フィールド: [default]。
     */
    private String fDefault;

    /**
     * フィールド [name] の値を設定します。
     *
     * フィールドの説明: [この列挙体の要素の名前です。]。
     *
     * @param argName フィールド[name]に設定する値。
     */
    public void setName(final String argName) {
        fName = argName;
    }

    /**
     * フィールド [name] の値を取得します。
     *
     * フィールドの説明: [この列挙体の要素の名前です。]。
     *
     * @return フィールド[name]から取得した値。
     */
    public String getName() {
        return fName;
    }

    /**
     * フィールド [description] の値を設定します。
     *
     * フィールドの説明: [この列挙体の要素の説明です。]。
     *
     * @param argDescription フィールド[description]に設定する値。
     */
    public void setDescription(final String argDescription) {
        fDescription = argDescription;
    }

    /**
     * フィールド [description] の値を取得します。
     *
     * フィールドの説明: [この列挙体の要素の説明です。]。
     *
     * @return フィールド[description]から取得した値。
     */
    public String getDescription() {
        return fDescription;
    }

    /**
     * フィールド [default] の値を設定します。
     *
     * フィールドの説明: [(C#.NETのみ対応) デフォルト値をあらわします。]。
     * Stringなら ""、intなら 3 などのように実際の文を指定します。
     * (ダブルクオートなども含んだ形で表現します。)
     *
     * @param argDefault フィールド[default]に設定する値。
     */
    public void setDefault(final String argDefault) {
        fDefault = argDefault;
    }

    /**
     * フィールド [default] の値を取得します。
     *
     * フィールドの説明: [(C#.NETのみ対応) デフォルト値をあらわします。]。
     * Stringなら ""、intなら 3 などのように実際の文を指定します。
     * (ダブルクオートなども含んだ形で表現します。)
     *
     * @return フィールド[default]から取得した値。
     */
    public String getDefault() {
        return fDefault;
    }

    /**
     * このバリューオブジェクトの文字列表現を取得します。
     *
     * <P>使用上の注意</P>
     * <UL>
     * <LI>オブジェクトのシャロー範囲のみ文字列化の処理対象となります。
     * <LI>オブジェクトが循環参照している場合には、このメソッドは使わないでください。
     * </UL>
     *
     * @return バリューオブジェクトの文字列表現。
     */
    @Override
    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("blanco.cg.valueobject.BlancoCgEnumElement[");
        buf.append("name=" + fName);
        buf.append(",description=" + fDescription);
        buf.append(",default=" + fDefault);
        buf.append("]");
        return buf.toString();
    }
}
