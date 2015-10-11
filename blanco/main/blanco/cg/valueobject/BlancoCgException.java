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
 * 例外を表現するためのバリューオブジェクト。
 *
 * おもにメソッドのthrowsを表現するために利用されます。
 */
public class BlancoCgException {
    /**
     * この例外の型です。java.io.IOExceptionなどは、このVOの中に指定します。
     *
     * フィールド: [type]。
     */
    private BlancoCgType fType;

    /**
     * この例外の説明です。「入出力例外が発生した場合。」などと記載します。
     *
     * フィールド: [description]。
     */
    private String fDescription;

    /**
     * フィールド [type] の値を設定します。
     *
     * フィールドの説明: [この例外の型です。java.io.IOExceptionなどは、このVOの中に指定します。]。
     *
     * @param argType フィールド[type]に設定する値。
     */
    public void setType(final BlancoCgType argType) {
        fType = argType;
    }

    /**
     * フィールド [type] の値を取得します。
     *
     * フィールドの説明: [この例外の型です。java.io.IOExceptionなどは、このVOの中に指定します。]。
     *
     * @return フィールド[type]から取得した値。
     */
    public BlancoCgType getType() {
        return fType;
    }

    /**
     * フィールド [description] の値を設定します。
     *
     * フィールドの説明: [この例外の説明です。「入出力例外が発生した場合。」などと記載します。]。
     *
     * @param argDescription フィールド[description]に設定する値。
     */
    public void setDescription(final String argDescription) {
        fDescription = argDescription;
    }

    /**
     * フィールド [description] の値を取得します。
     *
     * フィールドの説明: [この例外の説明です。「入出力例外が発生した場合。」などと記載します。]。
     *
     * @return フィールド[description]から取得した値。
     */
    public String getDescription() {
        return fDescription;
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
        buf.append("blanco.cg.valueobject.BlancoCgException[");
        buf.append("type=" + fType);
        buf.append(",description=" + fDescription);
        buf.append("]");
        return buf.toString();
    }
}
