/*
 * blanco Framework
 * Copyright (C) 2004-2006 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.cg.cg;

import java.io.File;

/**
 * blancoCgの各種クラスを自動生成するためのエントリポイント・クラスです。
 * 
 * ソースコード自動生成ライブラリ blancoCg そのものの一部を自動生成します。
 * 
 * @author IGA Tosiki
 */
public class BlancoCgCg {
    /**
     * blancoCgの各種クラスを自動生成するためのエントリポイント。
     * 
     * @param args
     *            起動引数。ただしこの処理では無視されます。
     */
    public static final void main(final String[] args) {
        final File targetDirectory = new File("blanco/main");

        new BlancoCgTransformerCg().process(targetDirectory);
    }
}
