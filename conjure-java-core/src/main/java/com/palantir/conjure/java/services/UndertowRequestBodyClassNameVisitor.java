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

package com.palantir.conjure.java.services;

import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.types.ClassNameVisitor;
import com.palantir.conjure.java.types.DefaultClassNameVisitor;
import com.palantir.conjure.java.util.TypeFunctions;
import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.visitor.TypeVisitor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public final class UndertowRequestBodyClassNameVisitor implements ClassNameVisitor {

    private final ClassNameVisitor delegate;
    private final List<TypeDefinition> types;

    public UndertowRequestBodyClassNameVisitor(List<TypeDefinition> types, Options options) {
        this.types = types;
        delegate = new DefaultClassNameVisitor(types, options);
    }

    @Override
    public TypeName visitUnknown(String unknownType) {
        return delegate.visitUnknown(unknownType);
    }

    @Override
    public TypeName visitPrimitive(PrimitiveType primitiveType) {
        if (PrimitiveType.BINARY.equals(primitiveType)) {
            return ClassName.get(InputStream.class);
        }
        return delegate.visitPrimitive(primitiveType);
    }

    @Override
    public TypeName visitOptional(OptionalType optionalType) {
        Type dealiased = dealiasBinary(optionalType.getItemType());
        if (dealiased.accept(TypeVisitor.IS_BINARY)) {
            return ParameterizedTypeName.get(ClassName.get(Optional.class), dealiased.accept(this));
        }
        return delegate.visitOptional(optionalType);
    }

    @Override
    public TypeName visitList(ListType listType) {
        return delegate.visitList(listType);
    }

    @Override
    public TypeName visitSet(SetType setType) {
        return delegate.visitSet(setType);
    }

    @Override
    public TypeName visitMap(MapType mapType) {
        return delegate.visitMap(mapType);
    }

    @Override
    public TypeName visitReference(com.palantir.conjure.spec.TypeName typeName) {
        Optional<Type> type = TypeFunctions.getAliasedType(typeName, types);
        if (type.isPresent()) {
            Type dealiased = dealiasBinary(type.get());
            if (TypeFunctions.isBinaryOrOptionalBinary(dealiased)) {
                return dealiased.accept(this);
            }
        }
        return delegate.visitReference(typeName);
    }

    @Override
    public TypeName visitExternal(ExternalReference externalReference) {
        return delegate.visitExternal(externalReference);
    }

    private Type dealiasBinary(Type input) {
        Type dealiased = TypeFunctions.toConjureTypeWithoutAliases(input, types);
        if (TypeFunctions.isBinaryOrOptionalBinary(dealiased)) {
            return dealiased;
        }
        return input;
    }
}
