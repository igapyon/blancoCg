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

import java.util.List;

/**
 * インタフェースを表現するためのバリューオブジェクト。
 *
 * インタフェースを作成したい場合に利用されます。
 * ※ポイント：クラス名の名前変形や文字列のエスケープ処理などは、blancoCgに与える前に実施されている必要があります。
 */
public class BlancoCgInterface {
    /**
     * このインタフェースの名前です。
     *
     * フィールド: [name]。
     */
    private String fName;

    /**
     * このインタフェースの説明です。
     *
     * フィールド: [description]。
     */
    private String fDescription;

    /**
     * ジェネリクスを指定します。
     *
     * フィールド: [generics]。
     */
    private String fGenerics;

    /**
     * 継承元クラスのリストです。
     *
     * Java言語では多重継承が禁止されているため、ひとつだけ指定する必要があります。
     * フィールド: [extendClassList]。
     * デフォルト: [new java.util.ArrayList<blanco.cg.valueobject.BlancoCgType>()]。
     */
    private List<blanco.cg.valueobject.BlancoCgType> fExtendClassList = new java.util.ArrayList<blanco.cg.valueobject.BlancoCgType>();

    /**
     * このインタフェースのアクセスコントロールを指定します。
     *
     * public/protected/privateなどを指定します。
     * フィールド: [access]。
     * デフォルト: ["public"]。
     */
    private String fAccess = "public";

    /**
     * このインタフェースに付与されているアノテーションのリストです。(java.lang.String)
     *
     * フィールド: [annotationList]。
     * デフォルト: [new java.util.ArrayList<java.lang.String>()]。
     */
    private List<java.lang.String> fAnnotationList = new java.util.ArrayList<java.lang.String>();

    /**
     * このインタフェースに含まれるフィールドのリストです。
     *
     * フィールド: [fieldList]。
     * デフォルト: [new java.util.ArrayList<blanco.cg.valueobject.BlancoCgField>()]。
     */
    private List<blanco.cg.valueobject.BlancoCgField> fFieldList = new java.util.ArrayList<blanco.cg.valueobject.BlancoCgField>();

    /**
     * このインタフェースに含まれるメソッドのリストです。
     *
     * フィールド: [methodList]。
     * デフォルト: [new java.util.ArrayList<blanco.cg.valueobject.BlancoCgMethod>()]。
     */
    private List<blanco.cg.valueobject.BlancoCgMethod> fMethodList = new java.util.ArrayList<blanco.cg.valueobject.BlancoCgMethod>();

    /**
     * 言語ドキュメントを蓄えます。デフォルト以上の表現を追加する場合には、インスタンスを生成して値をセットしてから自動生成します。
     *
     * フィールド: [langDoc]。
     */
    private BlancoCgLangDoc fLangDoc;

    /**
     * フィールド [name] の値を設定します。
     *
     * フィールドの説明: [このインタフェースの名前です。]。
     *
     * @param argName フィールド[name]に設定する値。
     */
    public void setName(final String argName) {
        fName = argName;
    }

    /**
     * フィールド [name] の値を取得します。
     *
     * フィールドの説明: [このインタフェースの名前です。]。
     *
     * @return フィールド[name]から取得した値。
     */
    public String getName() {
        return fName;
    }

    /**
     * フィールド [description] の値を設定します。
     *
     * フィールドの説明: [このインタフェースの説明です。]。
     *
     * @param argDescription フィールド[description]に設定する値。
     */
    public void setDescription(final String argDescription) {
        fDescription = argDescription;
    }

    /**
     * フィールド [description] の値を取得します。
     *
     * フィールドの説明: [このインタフェースの説明です。]。
     *
     * @return フィールド[description]から取得した値。
     */
    public String getDescription() {
        return fDescription;
    }

    /**
     * フィールド [generics] の値を設定します。
     *
     * フィールドの説明: [ジェネリクスを指定します。]。
     *
     * @param argGenerics フィールド[generics]に設定する値。
     */
    public void setGenerics(final String argGenerics) {
        fGenerics = argGenerics;
    }

    /**
     * フィールド [generics] の値を取得します。
     *
     * フィールドの説明: [ジェネリクスを指定します。]。
     *
     * @return フィールド[generics]から取得した値。
     */
    public String getGenerics() {
        return fGenerics;
    }

    /**
     * フィールド [extendClassList] の値を設定します。
     *
     * フィールドの説明: [継承元クラスのリストです。]。
     * Java言語では多重継承が禁止されているため、ひとつだけ指定する必要があります。
     *
     * @param argExtendClassList フィールド[extendClassList]に設定する値。
     */
    public void setExtendClassList(final List<blanco.cg.valueobject.BlancoCgType> argExtendClassList) {
        fExtendClassList = argExtendClassList;
    }

    /**
     * フィールド [extendClassList] の値を取得します。
     *
     * フィールドの説明: [継承元クラスのリストです。]。
     * Java言語では多重継承が禁止されているため、ひとつだけ指定する必要があります。
     * デフォルト: [new java.util.ArrayList<blanco.cg.valueobject.BlancoCgType>()]。
     *
     * @return フィールド[extendClassList]から取得した値。
     */
    public List<blanco.cg.valueobject.BlancoCgType> getExtendClassList() {
        return fExtendClassList;
    }

    /**
     * フィールド [access] の値を設定します。
     *
     * フィールドの説明: [このインタフェースのアクセスコントロールを指定します。]。
     * public/protected/privateなどを指定します。
     *
     * @param argAccess フィールド[access]に設定する値。
     */
    public void setAccess(final String argAccess) {
        fAccess = argAccess;
    }

    /**
     * フィールド [access] の値を取得します。
     *
     * フィールドの説明: [このインタフェースのアクセスコントロールを指定します。]。
     * public/protected/privateなどを指定します。
     * デフォルト: ["public"]。
     *
     * @return フィールド[access]から取得した値。
     */
    public String getAccess() {
        return fAccess;
    }

    /**
     * フィールド [annotationList] の値を設定します。
     *
     * フィールドの説明: [このインタフェースに付与されているアノテーションのリストです。(java.lang.String)]。
     *
     * @param argAnnotationList フィールド[annotationList]に設定する値。
     */
    public void setAnnotationList(final List<java.lang.String> argAnnotationList) {
        fAnnotationList = argAnnotationList;
    }

    /**
     * フィールド [annotationList] の値を取得します。
     *
     * フィールドの説明: [このインタフェースに付与されているアノテーションのリストです。(java.lang.String)]。
     * デフォルト: [new java.util.ArrayList<java.lang.String>()]。
     *
     * @return フィールド[annotationList]から取得した値。
     */
    public List<java.lang.String> getAnnotationList() {
        return fAnnotationList;
    }

    /**
     * フィールド [fieldList] の値を設定します。
     *
     * フィールドの説明: [このインタフェースに含まれるフィールドのリストです。]。
     *
     * @param argFieldList フィールド[fieldList]に設定する値。
     */
    public void setFieldList(final List<blanco.cg.valueobject.BlancoCgField> argFieldList) {
        fFieldList = argFieldList;
    }

    /**
     * フィールド [fieldList] の値を取得します。
     *
     * フィールドの説明: [このインタフェースに含まれるフィールドのリストです。]。
     * デフォルト: [new java.util.ArrayList<blanco.cg.valueobject.BlancoCgField>()]。
     *
     * @return フィールド[fieldList]から取得した値。
     */
    public List<blanco.cg.valueobject.BlancoCgField> getFieldList() {
        return fFieldList;
    }

    /**
     * フィールド [methodList] の値を設定します。
     *
     * フィールドの説明: [このインタフェースに含まれるメソッドのリストです。]。
     *
     * @param argMethodList フィールド[methodList]に設定する値。
     */
    public void setMethodList(final List<blanco.cg.valueobject.BlancoCgMethod> argMethodList) {
        fMethodList = argMethodList;
    }

    /**
     * フィールド [methodList] の値を取得します。
     *
     * フィールドの説明: [このインタフェースに含まれるメソッドのリストです。]。
     * デフォルト: [new java.util.ArrayList<blanco.cg.valueobject.BlancoCgMethod>()]。
     *
     * @return フィールド[methodList]から取得した値。
     */
    public List<blanco.cg.valueobject.BlancoCgMethod> getMethodList() {
        return fMethodList;
    }

    /**
     * フィールド [langDoc] の値を設定します。
     *
     * フィールドの説明: [言語ドキュメントを蓄えます。デフォルト以上の表現を追加する場合には、インスタンスを生成して値をセットしてから自動生成します。]。
     *
     * @param argLangDoc フィールド[langDoc]に設定する値。
     */
    public void setLangDoc(final BlancoCgLangDoc argLangDoc) {
        fLangDoc = argLangDoc;
    }

    /**
     * フィールド [langDoc] の値を取得します。
     *
     * フィールドの説明: [言語ドキュメントを蓄えます。デフォルト以上の表現を追加する場合には、インスタンスを生成して値をセットしてから自動生成します。]。
     *
     * @return フィールド[langDoc]から取得した値。
     */
    public BlancoCgLangDoc getLangDoc() {
        return fLangDoc;
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
        buf.append("blanco.cg.valueobject.BlancoCgInterface[");
        buf.append("name=" + fName);
        buf.append(",description=" + fDescription);
        buf.append(",generics=" + fGenerics);
        buf.append(",extendClassList=" + fExtendClassList);
        buf.append(",access=" + fAccess);
        buf.append(",annotationList=" + fAnnotationList);
        buf.append(",fieldList=" + fFieldList);
        buf.append(",methodList=" + fMethodList);
        buf.append(",langDoc=" + fLangDoc);
        buf.append("]");
        return buf.toString();
    }
}
