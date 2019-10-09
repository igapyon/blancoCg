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
 * ソースファイルを表現するためのバリューオブジェクト。
 */
public class BlancoCgSourceFile {
    /**
     * このファイルのファイル名です。
     *
     * なお、この値は明示的に指定しなくとも、多くの場合はクラス名・インタフェース名から名称が導出されるようになっています。
     * フィールド: [name]。
     */
    private String fName;

    /**
     * このファイルの説明です。
     *
     * フィールド: [description]。
     */
    private String fDescription;

    /**
     * このファイルが所属するパッケージです。
     *
     * フィールド: [package]。
     */
    private String fPackage;

    /**
     * このファイルの文字エンコーディングです。
     *
     * フィールド: [encoding]。
     */
    private String fEncoding;

    /**
     * このファイルが参照する他のパッケージのリストです。java.lang.Stringのリスト。
     *
     * フィールド: [importList]。
     * デフォルト: [new java.util.ArrayList&lt;java.lang.String&gt;()]。
     */
    private List<java.lang.String> fImportList = new java.util.ArrayList<java.lang.String>();

    /**
     * このファイルに含まれる列挙体のリストです。
     *
     * フィールド: [enumList]。
     * デフォルト: [new java.util.ArrayList&lt;blanco.cg.valueobject.BlancoCgEnum&gt;()]。
     */
    private List<blanco.cg.valueobject.BlancoCgEnum> fEnumList = new java.util.ArrayList<blanco.cg.valueobject.BlancoCgEnum>();

    /**
     * このファイルに含まれるインタフェースのリストです。
     *
     * フィールド: [interfaceList]。
     * デフォルト: [new java.util.ArrayList&lt;blanco.cg.valueobject.BlancoCgInterface&gt;()]。
     */
    private List<blanco.cg.valueobject.BlancoCgInterface> fInterfaceList = new java.util.ArrayList<blanco.cg.valueobject.BlancoCgInterface>();

    /**
     * このファイルに含まれるクラスのリストです。
     *
     * フィールド: [classList]。
     * デフォルト: [new java.util.ArrayList&lt;blanco.cg.valueobject.BlancoCgClass&gt;()]。
     */
    private List<blanco.cg.valueobject.BlancoCgClass> fClassList = new java.util.ArrayList<blanco.cg.valueobject.BlancoCgClass>();

    /**
     * 言語ドキュメントを蓄えます。デフォルト以上の表現を追加する場合には、インスタンスを生成して値をセットしてから自動生成します。
     *
     * なお、このバリューオブジェクトはソースファイルの言語ドキュメントに該当するため、ここでセットした値が実際のソースコードに反映される場合の影響範囲は限定されてしまう場合があります。
     * フィールド: [langDoc]。
     */
    private BlancoCgLangDoc fLangDoc;

    /**
     * import文を生成するかどうかのフラグです
     *
     * フィールド: [isImport]。
     * デフォルト: [true]。
     */
    private boolean fIsImport = true;

    /**
     * 継承、フィールド、メソッドの引数・戻り値から自動import編成を実施するかどうかのフラグです。(初期はJava言語にのみ対応)。自力で import 文を制御したい場合はこれを false に指定してください。
     *
     * フィールド: [isAutoImport]。
     * デフォルト: [true]。
     */
    private boolean fIsAutoImport = true;

    /**
     * フィールド [name] の値を設定します。
     *
     * フィールドの説明: [このファイルのファイル名です。]。
     * なお、この値は明示的に指定しなくとも、多くの場合はクラス名・インタフェース名から名称が導出されるようになっています。
     *
     * @param argName フィールド[name]に設定する値。
     */
    public void setName(final String argName) {
        fName = argName;
    }

    /**
     * フィールド [name] の値を取得します。
     *
     * フィールドの説明: [このファイルのファイル名です。]。
     * なお、この値は明示的に指定しなくとも、多くの場合はクラス名・インタフェース名から名称が導出されるようになっています。
     *
     * @return フィールド[name]から取得した値。
     */
    public String getName() {
        return fName;
    }

    /**
     * フィールド [description] の値を設定します。
     *
     * フィールドの説明: [このファイルの説明です。]。
     *
     * @param argDescription フィールド[description]に設定する値。
     */
    public void setDescription(final String argDescription) {
        fDescription = argDescription;
    }

    /**
     * フィールド [description] の値を取得します。
     *
     * フィールドの説明: [このファイルの説明です。]。
     *
     * @return フィールド[description]から取得した値。
     */
    public String getDescription() {
        return fDescription;
    }

    /**
     * フィールド [package] の値を設定します。
     *
     * フィールドの説明: [このファイルが所属するパッケージです。]。
     *
     * @param argPackage フィールド[package]に設定する値。
     */
    public void setPackage(final String argPackage) {
        fPackage = argPackage;
    }

    /**
     * フィールド [package] の値を取得します。
     *
     * フィールドの説明: [このファイルが所属するパッケージです。]。
     *
     * @return フィールド[package]から取得した値。
     */
    public String getPackage() {
        return fPackage;
    }

    /**
     * フィールド [encoding] の値を設定します。
     *
     * フィールドの説明: [このファイルの文字エンコーディングです。]。
     *
     * @param argEncoding フィールド[encoding]に設定する値。
     */
    public void setEncoding(final String argEncoding) {
        fEncoding = argEncoding;
    }

    /**
     * フィールド [encoding] の値を取得します。
     *
     * フィールドの説明: [このファイルの文字エンコーディングです。]。
     *
     * @return フィールド[encoding]から取得した値。
     */
    public String getEncoding() {
        return fEncoding;
    }

    /**
     * フィールド [importList] の値を設定します。
     *
     * フィールドの説明: [このファイルが参照する他のパッケージのリストです。java.lang.Stringのリスト。]。
     *
     * @param argImportList フィールド[importList]に設定する値。
     */
    public void setImportList(final List<java.lang.String> argImportList) {
        fImportList = argImportList;
    }

    /**
     * フィールド [importList] の値を取得します。
     *
     * フィールドの説明: [このファイルが参照する他のパッケージのリストです。java.lang.Stringのリスト。]。
     * デフォルト: [new java.util.ArrayList&lt;java.lang.String&gt;()]。
     *
     * @return フィールド[importList]から取得した値。
     */
    public List<java.lang.String> getImportList() {
        return fImportList;
    }

    /**
     * フィールド [enumList] の値を設定します。
     *
     * フィールドの説明: [このファイルに含まれる列挙体のリストです。]。
     *
     * @param argEnumList フィールド[enumList]に設定する値。
     */
    public void setEnumList(final List<blanco.cg.valueobject.BlancoCgEnum> argEnumList) {
        fEnumList = argEnumList;
    }

    /**
     * フィールド [enumList] の値を取得します。
     *
     * フィールドの説明: [このファイルに含まれる列挙体のリストです。]。
     * デフォルト: [new java.util.ArrayList&lt;blanco.cg.valueobject.BlancoCgEnum&gt;()]。
     *
     * @return フィールド[enumList]から取得した値。
     */
    public List<blanco.cg.valueobject.BlancoCgEnum> getEnumList() {
        return fEnumList;
    }

    /**
     * フィールド [interfaceList] の値を設定します。
     *
     * フィールドの説明: [このファイルに含まれるインタフェースのリストです。]。
     *
     * @param argInterfaceList フィールド[interfaceList]に設定する値。
     */
    public void setInterfaceList(final List<blanco.cg.valueobject.BlancoCgInterface> argInterfaceList) {
        fInterfaceList = argInterfaceList;
    }

    /**
     * フィールド [interfaceList] の値を取得します。
     *
     * フィールドの説明: [このファイルに含まれるインタフェースのリストです。]。
     * デフォルト: [new java.util.ArrayList&lt;blanco.cg.valueobject.BlancoCgInterface&gt;()]。
     *
     * @return フィールド[interfaceList]から取得した値。
     */
    public List<blanco.cg.valueobject.BlancoCgInterface> getInterfaceList() {
        return fInterfaceList;
    }

    /**
     * フィールド [classList] の値を設定します。
     *
     * フィールドの説明: [このファイルに含まれるクラスのリストです。]。
     *
     * @param argClassList フィールド[classList]に設定する値。
     */
    public void setClassList(final List<blanco.cg.valueobject.BlancoCgClass> argClassList) {
        fClassList = argClassList;
    }

    /**
     * フィールド [classList] の値を取得します。
     *
     * フィールドの説明: [このファイルに含まれるクラスのリストです。]。
     * デフォルト: [new java.util.ArrayList&lt;blanco.cg.valueobject.BlancoCgClass&gt;()]。
     *
     * @return フィールド[classList]から取得した値。
     */
    public List<blanco.cg.valueobject.BlancoCgClass> getClassList() {
        return fClassList;
    }

    /**
     * フィールド [langDoc] の値を設定します。
     *
     * フィールドの説明: [言語ドキュメントを蓄えます。デフォルト以上の表現を追加する場合には、インスタンスを生成して値をセットしてから自動生成します。]。
     * なお、このバリューオブジェクトはソースファイルの言語ドキュメントに該当するため、ここでセットした値が実際のソースコードに反映される場合の影響範囲は限定されてしまう場合があります。
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
     * なお、このバリューオブジェクトはソースファイルの言語ドキュメントに該当するため、ここでセットした値が実際のソースコードに反映される場合の影響範囲は限定されてしまう場合があります。
     *
     * @return フィールド[langDoc]から取得した値。
     */
    public BlancoCgLangDoc getLangDoc() {
        return fLangDoc;
    }

    /**
     * フィールド [isImport] の値を設定します。
     *
     * フィールドの説明: [import文を生成するかどうかのフラグです]。
     *
     * @param argIsImport フィールド[isImport]に設定する値。
     */
    public void setIsImport(final boolean argIsImport) {
        fIsImport = argIsImport;
    }

    /**
     * フィールド [isImport] の値を取得します。
     *
     * フィールドの説明: [import文を生成するかどうかのフラグです]。
     * デフォルト: [true]。
     *
     * @return フィールド[isImport]から取得した値。
     */
    public boolean getIsImport() {
        return fIsImport;
    }

    /**
     * フィールド [isAutoImport] の値を設定します。
     *
     * フィールドの説明: [継承、フィールド、メソッドの引数・戻り値から自動import編成を実施するかどうかのフラグです。(初期はJava言語にのみ対応)。自力で import 文を制御したい場合はこれを false に指定してください。]。
     *
     * @param argIsAutoImport フィールド[isAutoImport]に設定する値。
     */
    public void setIsAutoImport(final boolean argIsAutoImport) {
        fIsAutoImport = argIsAutoImport;
    }

    /**
     * フィールド [isAutoImport] の値を取得します。
     *
     * フィールドの説明: [継承、フィールド、メソッドの引数・戻り値から自動import編成を実施するかどうかのフラグです。(初期はJava言語にのみ対応)。自力で import 文を制御したい場合はこれを false に指定してください。]。
     * デフォルト: [true]。
     *
     * @return フィールド[isAutoImport]から取得した値。
     */
    public boolean getIsAutoImport() {
        return fIsAutoImport;
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
        buf.append("blanco.cg.valueobject.BlancoCgSourceFile[");
        buf.append("name=" + fName);
        buf.append(",description=" + fDescription);
        buf.append(",package=" + fPackage);
        buf.append(",encoding=" + fEncoding);
        buf.append(",importList=" + fImportList);
        buf.append(",enumList=" + fEnumList);
        buf.append(",interfaceList=" + fInterfaceList);
        buf.append(",classList=" + fClassList);
        buf.append(",langDoc=" + fLangDoc);
        buf.append(",isImport=" + fIsImport);
        buf.append(",isAutoImport=" + fIsAutoImport);
        buf.append("]");
        return buf.toString();
    }

    /**
     * このバリューオブジェクトを指定のターゲットに複写します。
     *
     * <P>使用上の注意</P>
     * <UL>
     * <LI>オブジェクトのシャロー範囲のみ複写処理対象となります。
     * <LI>オブジェクトが循環参照している場合には、このメソッドは使わないでください。
     * </UL>
     *
     * @param target target value object.
     */
    public void copyTo(final BlancoCgSourceFile target) {
        if (target == null) {
            throw new IllegalArgumentException("Bug: BlancoCgSourceFile#copyTo(target): argument 'target' is null");
        }

        // No needs to copy parent class.

        // Name: fName
        // Type: java.lang.String
        target.fName = this.fName;
        // Name: fDescription
        // Type: java.lang.String
        target.fDescription = this.fDescription;
        // Name: fPackage
        // Type: java.lang.String
        target.fPackage = this.fPackage;
        // Name: fEncoding
        // Type: java.lang.String
        target.fEncoding = this.fEncoding;
        // Name: fImportList
        // Type: java.util.List
        // フィールド[fImportList]はサポート外の型[java.util.List]です。
        // Name: fEnumList
        // Type: java.util.List
        // フィールド[fEnumList]はサポート外の型[java.util.List]です。
        // Name: fInterfaceList
        // Type: java.util.List
        // フィールド[fInterfaceList]はサポート外の型[java.util.List]です。
        // Name: fClassList
        // Type: java.util.List
        // フィールド[fClassList]はサポート外の型[java.util.List]です。
        // Name: fLangDoc
        // Type: blanco.cg.valueobject.BlancoCgLangDoc
        // フィールド[fLangDoc]はサポート外の型[blanco.cg.valueobject.BlancoCgLangDoc]です。
        // Name: fIsImport
        // Type: boolean
        target.fIsImport = this.fIsImport;
        // Name: fIsAutoImport
        // Type: boolean
        target.fIsAutoImport = this.fIsAutoImport;
    }
}
