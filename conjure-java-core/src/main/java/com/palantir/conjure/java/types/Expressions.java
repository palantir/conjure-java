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

import com.google.common.base.Joiner;
import com.palantir.logsafe.Preconditions;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Expressions {
    private Expressions() {}

    public static CodeBlock requireNonNull(String fieldName, String message) {
        return CodeBlock.of("$1T.checkNotNull($2N, $3S)", Preconditions.class, fieldName, message);
    }

    public static CodeBlock constructorCall(ClassName clazz, Collection<?> args) {
        return CodeBlock.of("$1T(" + indexParams(2, args.size() + 2) + ")", append(clazz, args));
    }

    public static CodeBlock localMethodCall(String method, Collection<?> args) {
        return CodeBlock.of("$1N(" + indexParams(2, args.size() + 2) + ")", append(method, args));
    }

    public static CodeBlock objectArray(Collection<?> args) {
        return CodeBlock.of("new Object[]{" + indexParams(1, args.size() + 1) + "}", args.toArray());
    }

    private static Object[] append(Object one, Collection<?> rest) {
        return Stream.concat(Stream.of(one), rest.stream()).toArray();
    }

    private static String indexParams(int lower, int upper) {
        return Joiner.on(", ").join(indexStringInRange("$%dN", lower, upper));
    }

    private static Iterator<String> indexStringInRange(String format, int lower, int upper) {
        return IntStream.range(lower, upper).mapToObj(i -> String.format(format, i)).iterator();
    }
}
