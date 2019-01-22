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
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ReturnTypeClassNameVisitor implements ClassNameVisitor {

    private final DefaultClassNameVisitor delegate;
    private final Map<com.palantir.conjure.spec.TypeName, TypeDefinition> types;
    private final ClassName binaryClassName;

    public ReturnTypeClassNameVisitor(List<TypeDefinition> types, ClassName binaryClassName) {
        this.delegate = new DefaultClassNameVisitor(types);
        this.types = types.stream().collect(Collectors.toMap(
                t -> t.accept(TypeDefinitionVisitor.TYPE_NAME),
                Function.identity()));
        this.binaryClassName = binaryClassName;
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
        // see #201: The implicit tri-state is implemented using null as empty for jersey and retrofit.
        // Furthermore, jersey cannot support Optional<StreamingOutput>.
        if (type.getItemType().accept(TypeVisitor.IS_PRIMITIVE)
                && type.getItemType().accept(TypeVisitor.PRIMITIVE).equals(PrimitiveType.BINARY)) {
            return this.visitPrimitive(PrimitiveType.BINARY);
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
    public TypeName visitReference(com.palantir.conjure.spec.TypeName type) {
        if (!types.containsKey(type)) {
            throw new IllegalStateException("Unknown LocalReferenceType type: " + type);
        }

        TypeDefinition def = types.get(type);
        if (def.accept(TypeDefinitionVisitor.IS_ALIAS)) {
            Type aliasType = def.accept(TypeDefinitionVisitor.ALIAS).getAlias();
            TypeName aliasTypeName = aliasType.accept(this);
            if (aliasTypeName.equals(binaryClassName)) {
                return aliasTypeName;
            }
        }

        return delegate.visitReference(type);
    }

    @Override
    public TypeName visitExternal(ExternalReference type) {
        return delegate.visitExternal(type);
    }

    @Override
    public TypeName visitSet(SetType type) {
        return delegate.visitSet(type);
    }
}
