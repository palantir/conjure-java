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

package com.palantir.conjure.java.codegen.lib;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Produces CodeBlocks for a given TypeMarker ClassName. These are typically either
 * com.palantir.conjure.java.undertow.lib.TypeMarker or
 * com.palantir.dialogue.TypeMarker
 *
 * Simple types and supported parameterized types with one level of parameterization will use
 * static factories for typemarker generation. All other cases will use anon classes.
 */
public final class TypeMarkers {

    private static final Map<ClassName, String> TYPE_MARKER_FACTORY_METHODS = Map.of(
            ClassName.get(List.class), "listOf",
            ClassName.get(Set.class), "setOf",
            ClassName.get(Optional.class), "optionalOf",
            ClassName.get(Map.class), "mapOf");

    private TypeMarkers() {
        // util class
    }

    public static CodeBlock typeMarker(ClassName typeMarkerClass, TypeName type) {
        if (type instanceof ClassName className) {
            return CodeBlock.of("$T.of($T.class)", typeMarkerClass, className);
        }
        if (type instanceof ParameterizedTypeName parameterized
                && parameterized.typeArguments().stream().allMatch(ClassName.class::isInstance)) {
            String factoryMethod = TYPE_MARKER_FACTORY_METHODS.get(parameterized.rawType());
            if (factoryMethod != null) {
                CodeBlock arguments = parameterized.typeArguments().stream()
                        .map(argument -> CodeBlock.of("$T.class", argument))
                        .collect(CodeBlock.joining(", "));
                return CodeBlock.of("$T.$L($L)", typeMarkerClass, factoryMethod, arguments);
            }
        }
        return CodeBlock.of("new $T<$T>() {}", typeMarkerClass, type);
    }
}
