/*
 * (c) Copyright 2023 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.util;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeName;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class PrimitiveHelpers {

    private PrimitiveHelpers() {}

    private static final Map<TypeName, TypeName> PRIMITIVES = Map.ofEntries(
            Map.entry(TypeName.INT, TypeName.INT.box()),
            Map.entry(TypeName.BOOLEAN, TypeName.BOOLEAN.box()),
            Map.entry(TypeName.BYTE, TypeName.BYTE.box()),
            Map.entry(TypeName.CHAR, TypeName.CHAR.box()),
            Map.entry(TypeName.DOUBLE, TypeName.DOUBLE.box()),
            Map.entry(TypeName.FLOAT, TypeName.FLOAT.box()),
            Map.entry(TypeName.LONG, TypeName.LONG.box()),
            Map.entry(TypeName.SHORT, TypeName.SHORT.box()),
            Map.entry(TypeName.VOID, TypeName.VOID.box()));

    public static TypeName box(TypeName type) {
        if (isPrimitive(type)) {
            List<AnnotationSpec> annotations = type.annotations;
            return PRIMITIVES.get(getPrimitiveType(type).get()).annotated(annotations);
        } else {
            return type;
        }
    }

    public static boolean isPrimitive(TypeName type) {
        return getPrimitiveType(type).isPresent();
    }

    public static boolean isBoxedPrimitive(TypeName type) {
        return type.withoutAnnotations().isBoxedPrimitive();
    }

    public static TypeName unbox(TypeName type) {
        List<AnnotationSpec> annotations = type.annotations;
        return type.withoutAnnotations().unbox().annotated(annotations);
    }

    private static Optional<TypeName> getPrimitiveType(TypeName type) {
        TypeName rawType = type.withoutAnnotations();
        return PRIMITIVES.keySet().stream()
                .filter(typeName -> typeName.equals(rawType))
                .findAny();
    }
}
