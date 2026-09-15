/*
 * (c) Copyright 2026 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.services.dialogue;

import com.palantir.dialogue.TypeMarker;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Produces {@link CodeBlock}s which create {@link TypeMarker} instances. Simple types and one level of
 * parameterization over simple types use {@link TypeMarker} factory methods; anything more complex falls back to
 * anonymous {@link TypeMarker} subclasses.
 */
final class TypeMarkers {

    private static final Map<ClassName, String> TYPE_MARKER_FACTORY_METHODS = Map.of(
            ClassName.get(List.class), "listOf",
            ClassName.get(Set.class), "setOf",
            ClassName.get(Optional.class), "optionalOf",
            ClassName.get(Map.class), "mapOf");

    static CodeBlock typeMarker(TypeName type) {
        if (type instanceof ClassName className) {
            return CodeBlock.of("$T.of($T.class)", TypeMarker.class, className);
        }
        if (type instanceof ParameterizedTypeName parameterized
                && parameterized.typeArguments().stream().allMatch(ClassName.class::isInstance)) {
            String factoryMethod = TYPE_MARKER_FACTORY_METHODS.get(parameterized.rawType());
            if (factoryMethod != null) {
                CodeBlock arguments = parameterized.typeArguments().stream()
                        .map(argument -> CodeBlock.of("$T.class", argument))
                        .collect(CodeBlock.joining(", "));
                return CodeBlock.of("$T.$L($L)", TypeMarker.class, factoryMethod, arguments);
            }
        }
        return CodeBlock.of("new $T<$T>() {}", TypeMarker.class, type);
    }

    private TypeMarkers() {}
}
