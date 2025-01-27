/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.conjure.java.types.TypeMapper;
import com.palantir.conjure.java.util.Primitives;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.Type;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import java.util.Optional;

public final class ReturnTypeMapper {
    private static final ClassName LISTENABLE_FUTURE = ClassName.get(ListenableFuture.class);
    private final TypeMapper returnTypes;

    public ReturnTypeMapper(TypeMapper returnTypes) {
        this.returnTypes = returnTypes;
    }

    public TypeName baseType(Type type) {
        return returnTypes.getClassName(type);
    }

    public TypeName baseType(Optional<Type> type) {
        return type.map(this::baseType).orElse(TypeName.VOID);
    }

    public TypeName async(Optional<Type> type) {
        return ParameterizedTypeName.get(LISTENABLE_FUTURE, Primitives.box(baseType(type)));
    }

    public boolean isBinaryOrOptionalBinary(TypeName returnType) {
        return isBinary(returnType) || isOptionalBinary(returnType);
    }

    public boolean isOptionalBinary(TypeName returnType) {
        return returnType.equals(baseType(Type.optional(OptionalType.of(Type.primitive(PrimitiveType.BINARY)))));
    }

    public boolean isBinary(TypeName returnType) {
        return returnType.equals(baseType(Type.primitive(PrimitiveType.BINARY)));
    }
}
