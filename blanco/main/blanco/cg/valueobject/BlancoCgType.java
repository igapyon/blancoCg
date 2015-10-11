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
 * 型を表現するためのバリューオブジェクト。既存のクラスを参照する場合に利用されます。
 *
 * このバリューオブジェクトでは、対象となる型がクラスなのかインタフェースなのかは表現しません。クラスかインタフェースなのかについては特定しないのです。
 * このクラスの存在意義は genericsやarrayといったフィールドを持っている点です。これらを表現するために、型は単なるjava.lang.Stringではなくバリューオブジェクトである必要があり、また BlancoCgClassといったソースコードを生成するための型とは一線を画する必要が出てくるのです。
 */
public class BlancoCgType {
    /**
     * この型の名前です。java.lang.Stringなどパッケージ名付きで指定します。[]は含むことは出来ません。配列を表す場合には arrayフィールドを利用します。
     *
     * フィールド: [name]。
     */
    private String fName;

    /**
     * この型の説明です。
     *
     * フィールド: [description]。
     */
    private String fDescription;

    /**
     * この型に追加されるジェネリクスを指定します。
     *
     * フィールド: [generics]。
     */
    private String fGenerics;

    /**
     * 型が配列なのかどうかを示します。
     *
     * フィールド: [array]。
     * デフォルト: [false]。
     */
    private boolean fArray = false;

    /**
     * 型の配列の次元数を指定します。配列の場合にのみ利用されます。※Java, C#.NET で対応。それ以外の言語では未対応。
     *
     * フィールド: [arrayDimension]。
     * デフォルト: [1]。
     */
    private int fArrayDimension = 1;

    /**
     * フィールド [name] の値を設定します。
     *
     * フィールドの説明: [この型の名前です。java.lang.Stringなどパッケージ名付きで指定します。[]は含むことは出来ません。配列を表す場合には arrayフィールドを利用します。]。
     *
     * @param argName フィールド[name]に設定する値。
     */
    public void setName(final String argName) {
        fName = argName;
    }

    /**
     * フィールド [name] の値を取得します。
     *
     * フィールドの説明: [この型の名前です。java.lang.Stringなどパッケージ名付きで指定します。[]は含むことは出来ません。配列を表す場合には arrayフィールドを利用します。]。
     *
     * @return フィールド[name]から取得した値。
     */
    public String getName() {
        return fName;
    }

    /**
     * フィールド [description] の値を設定します。
     *
     * フィールドの説明: [この型の説明です。]。
     *
     * @param argDescription フィールド[description]に設定する値。
     */
    public void setDescription(final String argDescription) {
        fDescription = argDescription;
    }

    /**
     * フィールド [description] の値を取得します。
     *
     * フィールドの説明: [この型の説明です。]。
     *
     * @return フィールド[description]から取得した値。
     */
    public String getDescription() {
        return fDescription;
    }

    /**
     * フィールド [generics] の値を設定します。
     *
     * フィールドの説明: [この型に追加されるジェネリクスを指定します。]。
     *
     * @param argGenerics フィールド[generics]に設定する値。
     */
    public void setGenerics(final String argGenerics) {
        fGenerics = argGenerics;
    }

    /**
     * フィールド [generics] の値を取得します。
     *
     * フィールドの説明: [この型に追加されるジェネリクスを指定します。]。
     *
     * @return フィールド[generics]から取得した値。
     */
    public String getGenerics() {
        return fGenerics;
    }

    /**
     * フィールド [array] の値を設定します。
     *
     * フィールドの説明: [型が配列なのかどうかを示します。]。
     *
     * @param argArray フィールド[array]に設定する値。
     */
    public void setArray(final boolean argArray) {
        fArray = argArray;
    }

    /**
     * フィールド [array] の値を取得します。
     *
     * フィールドの説明: [型が配列なのかどうかを示します。]。
     * デフォルト: [false]。
     *
     * @return フィールド[array]から取得した値。
     */
    public boolean getArray() {
        return fArray;
    }

    /**
     * フィールド [arrayDimension] の値を設定します。
     *
     * フィールドの説明: [型の配列の次元数を指定します。配列の場合にのみ利用されます。※Java, C#.NET で対応。それ以外の言語では未対応。]。
     *
     * @param argArrayDimension フィールド[arrayDimension]に設定する値。
     */
    public void setArrayDimension(final int argArrayDimension) {
        fArrayDimension = argArrayDimension;
    }

    /**
     * フィールド [arrayDimension] の値を取得します。
     *
     * フィールドの説明: [型の配列の次元数を指定します。配列の場合にのみ利用されます。※Java, C#.NET で対応。それ以外の言語では未対応。]。
     * デフォルト: [1]。
     *
     * @return フィールド[arrayDimension]から取得した値。
     */
    public int getArrayDimension() {
        return fArrayDimension;
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
        buf.append("blanco.cg.valueobject.BlancoCgType[");
        buf.append("name=" + fName);
        buf.append(",description=" + fDescription);
        buf.append(",generics=" + fGenerics);
        buf.append(",array=" + fArray);
        buf.append(",arrayDimension=" + fArrayDimension);
        buf.append("]");
        return buf.toString();
    }
}
