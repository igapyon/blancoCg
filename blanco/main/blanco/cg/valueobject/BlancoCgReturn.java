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
 * 戻り値を表現するためのバリューオブジェクト。
 *
 * なお、メソッドの戻り値がvoidである場合には、設定する必要はありません。
 */
public class BlancoCgReturn {
    /**
     * この戻り値の説明です。
     *
     * フィールド: [description]。
     */
    private String fDescription;

    /**
     * この戻り値の型です。java.lang.Stringなどを指定します。
     *
     * フィールド: [type]。
     */
    private BlancoCgType fType;

    /**
     * フィールド [description] の値を設定します。
     *
     * フィールドの説明: [この戻り値の説明です。]。
     *
     * @param argDescription フィールド[description]に設定する値。
     */
    public void setDescription(final String argDescription) {
        fDescription = argDescription;
    }

    /**
     * フィールド [description] の値を取得します。
     *
     * フィールドの説明: [この戻り値の説明です。]。
     *
     * @return フィールド[description]から取得した値。
     */
    public String getDescription() {
        return fDescription;
    }

    /**
     * フィールド [type] の値を設定します。
     *
     * フィールドの説明: [この戻り値の型です。java.lang.Stringなどを指定します。]。
     *
     * @param argType フィールド[type]に設定する値。
     */
    public void setType(final BlancoCgType argType) {
        fType = argType;
    }

    /**
     * フィールド [type] の値を取得します。
     *
     * フィールドの説明: [この戻り値の型です。java.lang.Stringなどを指定します。]。
     *
     * @return フィールド[type]から取得した値。
     */
    public BlancoCgType getType() {
        return fType;
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
        buf.append("blanco.cg.valueobject.BlancoCgReturn[");
        buf.append("description=" + fDescription);
        buf.append(",type=" + fType);
        buf.append("]");
        return buf.toString();
    }
}
