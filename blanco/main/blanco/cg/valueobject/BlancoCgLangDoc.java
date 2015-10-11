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
 * 言語用のドキュメントを表現するためのバリューオブジェクト。
 *
 * Java言語の場合には JavaDocを表します。自動生成時に他の「説明」フィールドやメソッドのパラメータなどから情報が構築される場合があります。
 * ※ポイント：コメント文字列のエスケープ処理などは、blancoCgに与える前に実施されている必要があります。
 */
public class BlancoCgLangDoc {
    /**
     * この言語ドキュメントのタイトル説明です。他の原料のdescriptionから自動生成されることが多いです。
     *
     * フィールド: [title]。
     */
    private String fTitle;

    /**
     * この言語ドキュメントの詳細説明です。(java.lang.String)のリストです。
     *
     * ここで与えられた文字列がそのままドキュメント説明部に展開されるため、通常は文字参照エンコーディングを実施したあとの値をセットします。(エンコーディング後のものを与えるからこそ、<pre>などを実現することができるのです。)
     * フィールド: [descriptionList]。
     * デフォルト: [new java.util.ArrayList<java.lang.String>()]。
     */
    private List<java.lang.String> fDescriptionList = new java.util.ArrayList<java.lang.String>();

    /**
     * 推奨されない場合に、非推奨の理由が記載されます。
     *
     * フィールド: [deprecated]。
     */
    private String fDeprecated;

    /**
     * パラメータのリストです。メソッドの場合にのみ利用されます。
     *
     * フィールド: [parameterList]。
     * デフォルト: [new java.util.ArrayList<blanco.cg.valueobject.BlancoCgParameter>()]。
     */
    private List<blanco.cg.valueobject.BlancoCgParameter> fParameterList = new java.util.ArrayList<blanco.cg.valueobject.BlancoCgParameter>();

    /**
     * このメソッドの戻り値です。
     *
     * 戻り値が無い (void)の場合には nullをセットします。
     * フィールド: [return]。
     */
    private BlancoCgReturn fReturn;

    /**
     * 発生しうる例外の一覧です。メソッドの場合にのみ利用されます。
     *
     * フィールド: [throwList]。
     * デフォルト: [new java.util.ArrayList<blanco.cg.valueobject.BlancoCgException>()]。
     */
    private List<blanco.cg.valueobject.BlancoCgException> fThrowList = new java.util.ArrayList<blanco.cg.valueobject.BlancoCgException>();

    /**
     * 言語ドキュメントのタグのリスト。BlancoCgLangDocTagがリストに格納されます。
     *
     * フィールド: [tagList]。
     * デフォルト: [new java.util.ArrayList<blanco.cg.valueobject.BlancoCgLangDocTag>()]。
     */
    private List<blanco.cg.valueobject.BlancoCgLangDocTag> fTagList = new java.util.ArrayList<blanco.cg.valueobject.BlancoCgLangDocTag>();

    /**
     * フィールド [title] の値を設定します。
     *
     * フィールドの説明: [この言語ドキュメントのタイトル説明です。他の原料のdescriptionから自動生成されることが多いです。]。
     *
     * @param argTitle フィールド[title]に設定する値。
     */
    public void setTitle(final String argTitle) {
        fTitle = argTitle;
    }

    /**
     * フィールド [title] の値を取得します。
     *
     * フィールドの説明: [この言語ドキュメントのタイトル説明です。他の原料のdescriptionから自動生成されることが多いです。]。
     *
     * @return フィールド[title]から取得した値。
     */
    public String getTitle() {
        return fTitle;
    }

    /**
     * フィールド [descriptionList] の値を設定します。
     *
     * フィールドの説明: [この言語ドキュメントの詳細説明です。(java.lang.String)のリストです。]。
     * ここで与えられた文字列がそのままドキュメント説明部に展開されるため、通常は文字参照エンコーディングを実施したあとの値をセットします。(エンコーディング後のものを与えるからこそ、<pre>などを実現することができるのです。)
     *
     * @param argDescriptionList フィールド[descriptionList]に設定する値。
     */
    public void setDescriptionList(final List<java.lang.String> argDescriptionList) {
        fDescriptionList = argDescriptionList;
    }

    /**
     * フィールド [descriptionList] の値を取得します。
     *
     * フィールドの説明: [この言語ドキュメントの詳細説明です。(java.lang.String)のリストです。]。
     * ここで与えられた文字列がそのままドキュメント説明部に展開されるため、通常は文字参照エンコーディングを実施したあとの値をセットします。(エンコーディング後のものを与えるからこそ、<pre>などを実現することができるのです。)
     * デフォルト: [new java.util.ArrayList<java.lang.String>()]。
     *
     * @return フィールド[descriptionList]から取得した値。
     */
    public List<java.lang.String> getDescriptionList() {
        return fDescriptionList;
    }

    /**
     * フィールド [deprecated] の値を設定します。
     *
     * フィールドの説明: [推奨されない場合に、非推奨の理由が記載されます。]。
     *
     * @param argDeprecated フィールド[deprecated]に設定する値。
     */
    public void setDeprecated(final String argDeprecated) {
        fDeprecated = argDeprecated;
    }

    /**
     * フィールド [deprecated] の値を取得します。
     *
     * フィールドの説明: [推奨されない場合に、非推奨の理由が記載されます。]。
     *
     * @return フィールド[deprecated]から取得した値。
     */
    public String getDeprecated() {
        return fDeprecated;
    }

    /**
     * フィールド [parameterList] の値を設定します。
     *
     * フィールドの説明: [パラメータのリストです。メソッドの場合にのみ利用されます。]。
     *
     * @param argParameterList フィールド[parameterList]に設定する値。
     */
    public void setParameterList(final List<blanco.cg.valueobject.BlancoCgParameter> argParameterList) {
        fParameterList = argParameterList;
    }

    /**
     * フィールド [parameterList] の値を取得します。
     *
     * フィールドの説明: [パラメータのリストです。メソッドの場合にのみ利用されます。]。
     * デフォルト: [new java.util.ArrayList<blanco.cg.valueobject.BlancoCgParameter>()]。
     *
     * @return フィールド[parameterList]から取得した値。
     */
    public List<blanco.cg.valueobject.BlancoCgParameter> getParameterList() {
        return fParameterList;
    }

    /**
     * フィールド [return] の値を設定します。
     *
     * フィールドの説明: [このメソッドの戻り値です。]。
     * 戻り値が無い (void)の場合には nullをセットします。
     *
     * @param argReturn フィールド[return]に設定する値。
     */
    public void setReturn(final BlancoCgReturn argReturn) {
        fReturn = argReturn;
    }

    /**
     * フィールド [return] の値を取得します。
     *
     * フィールドの説明: [このメソッドの戻り値です。]。
     * 戻り値が無い (void)の場合には nullをセットします。
     *
     * @return フィールド[return]から取得した値。
     */
    public BlancoCgReturn getReturn() {
        return fReturn;
    }

    /**
     * フィールド [throwList] の値を設定します。
     *
     * フィールドの説明: [発生しうる例外の一覧です。メソッドの場合にのみ利用されます。]。
     *
     * @param argThrowList フィールド[throwList]に設定する値。
     */
    public void setThrowList(final List<blanco.cg.valueobject.BlancoCgException> argThrowList) {
        fThrowList = argThrowList;
    }

    /**
     * フィールド [throwList] の値を取得します。
     *
     * フィールドの説明: [発生しうる例外の一覧です。メソッドの場合にのみ利用されます。]。
     * デフォルト: [new java.util.ArrayList<blanco.cg.valueobject.BlancoCgException>()]。
     *
     * @return フィールド[throwList]から取得した値。
     */
    public List<blanco.cg.valueobject.BlancoCgException> getThrowList() {
        return fThrowList;
    }

    /**
     * フィールド [tagList] の値を設定します。
     *
     * フィールドの説明: [言語ドキュメントのタグのリスト。BlancoCgLangDocTagがリストに格納されます。]。
     *
     * @param argTagList フィールド[tagList]に設定する値。
     */
    public void setTagList(final List<blanco.cg.valueobject.BlancoCgLangDocTag> argTagList) {
        fTagList = argTagList;
    }

    /**
     * フィールド [tagList] の値を取得します。
     *
     * フィールドの説明: [言語ドキュメントのタグのリスト。BlancoCgLangDocTagがリストに格納されます。]。
     * デフォルト: [new java.util.ArrayList<blanco.cg.valueobject.BlancoCgLangDocTag>()]。
     *
     * @return フィールド[tagList]から取得した値。
     */
    public List<blanco.cg.valueobject.BlancoCgLangDocTag> getTagList() {
        return fTagList;
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
        buf.append("blanco.cg.valueobject.BlancoCgLangDoc[");
        buf.append("title=" + fTitle);
        buf.append(",descriptionList=" + fDescriptionList);
        buf.append(",deprecated=" + fDeprecated);
        buf.append(",parameterList=" + fParameterList);
        buf.append(",return=" + fReturn);
        buf.append(",throwList=" + fThrowList);
        buf.append(",tagList=" + fTagList);
        buf.append("]");
        return buf.toString();
    }
}
