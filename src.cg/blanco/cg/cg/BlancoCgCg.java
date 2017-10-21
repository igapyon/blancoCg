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
