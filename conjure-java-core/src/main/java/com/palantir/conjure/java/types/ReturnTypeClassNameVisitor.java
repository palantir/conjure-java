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

import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.util.TypeFunctions;
import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.visitor.TypeDefinitionVisitor;
import com.palantir.conjure.visitor.TypeVisitor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ReturnTypeClassNameVisitor implements ClassNameVisitor {

    private final DefaultClassNameVisitor delegate;
    private final Map<com.palantir.conjure.spec.TypeName, TypeDefinition> types;
    private final ClassName binaryClassName;
    private final TypeName optionalBinaryTypeName;

    public ReturnTypeClassNameVisitor(
            List<TypeDefinition> types,
            ClassName binaryClassName,
            TypeName optionalBinaryTypeName,
            Options featureFlags) {
        this.delegate = new DefaultClassNameVisitor(types, featureFlags);
        this.types = types.stream()
                .collect(Collectors.toMap(t -> t.accept(TypeDefinitionVisitor.TYPE_NAME), Function.identity()));
        this.binaryClassName = binaryClassName;
        this.optionalBinaryTypeName = optionalBinaryTypeName;
    }

    @Override
    public TypeName visitList(ListType type) {
        return delegate.visitList(type);
    }

    @Override
    public TypeName visitMap(MapType type) {
        return delegate.visitMap(type);
    }

    @Override
    public TypeName visitOptional(OptionalType type) {
        Type itemType = type.getItemType();
        Type dealiased = TypeFunctions.toConjureTypeWithoutAliases(itemType, types.values());
        if (dealiased.accept(TypeVisitor.IS_BINARY)) {
            return optionalBinaryTypeName;
        }
        return delegate.visitOptional(type);
    }

    @Override
    public TypeName visitPrimitive(PrimitiveType type) {
        if (type.get() == PrimitiveType.Value.BINARY) {
            return binaryClassName;
        } else {
            return delegate.visitPrimitive(type);
        }
    }

    @Override
    public TypeName visitReference(com.palantir.conjure.spec.TypeName typeName) {
        if (!types.containsKey(typeName)) {
            throw new IllegalStateException("Unknown LocalReferenceType type: " + typeName);
        }

        Optional<Type> type = TypeFunctions.getAliasedType(typeName, types.values());
        if (type.isPresent()) {
            Type dealiased = dealiasBinary(type.get());
            if (TypeFunctions.isBinaryOrOptionalBinary(dealiased)) {
                return dealiased.accept(this);
            }
        }

        return delegate.visitReference(typeName);
    }

    @Override
    public TypeName visitExternal(ExternalReference type) {
        return delegate.visitExternal(type);
    }

    @Override
    public TypeName visitSet(SetType type) {
        return delegate.visitSet(type);
    }

    private Type dealiasBinary(Type input) {
        Type dealiased = TypeFunctions.toConjureTypeWithoutAliases(input, types.values());
        if (TypeFunctions.isBinaryOrOptionalBinary(dealiased)) {
            return dealiased;
        }
        return input;
    }
}
