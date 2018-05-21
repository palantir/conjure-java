/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.types;

import com.squareup.javapoet.CodeBlock;
import java.util.Arrays;

public final class CodeBlocks {

    private CodeBlocks() {}

    public static CodeBlock statement(String format, Object... args) {
        return CodeBlock.builder().addStatement(format, args).build();
    }

    public static CodeBlock of(CodeBlock... blocks) {
        return of(Arrays.asList(blocks));
    }

    public static CodeBlock of(Iterable<CodeBlock> blocks) {
        CodeBlock.Builder builder = CodeBlock.builder();
        for (CodeBlock block : blocks) {
            builder.add(block);
        }
        return builder.build();
    }

}
