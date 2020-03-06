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

package com.palantir.conjure.java.visitor;

import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.types.ClassNameVisitor;
import com.palantir.conjure.java.types.DefaultClassNameVisitor;
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
import com.palantir.dialogue.BinaryRequestBody;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class DialogueClassVisitor implements ClassNameVisitor {

    public enum Mode {
        RETURN_VALUE,
        PARAMETER
    }

    private final DefaultClassNameVisitor delegate;
    private final Mode mode;
    private final Map<com.palantir.conjure.spec.TypeName, TypeDefinition> types;

    public DialogueClassVisitor(List<TypeDefinition> types, Options options, Mode mode) {
        this.delegate = new DefaultClassNameVisitor(types, options);
        this.mode = mode;
        this.types = types.stream()
                .collect(Collectors.toMap(t -> t.accept(TypeDefinitionVisitor.TYPE_NAME), Function.identity()));
    }

    @Override
    public TypeName visitPrimitive(PrimitiveType value) {
        if (!value.equals(PrimitiveType.BINARY)) {
            return delegate.visitPrimitive(value);
        }

        if (mode == Mode.RETURN_VALUE) {
            return TypeName.get(InputStream.class);
        } else {
            return TypeName.get(BinaryRequestBody.class);
        }
    }

    @Override
    public TypeName visitOptional(OptionalType value) {
        Type itemType = value.getItemType();
        if (itemType.accept(TypeVisitor.IS_BINARY) || itemType.accept(TypeVisitor.IS_REFERENCE)) {
            return ParameterizedTypeName.get(ClassName.get(Optional.class), box(itemType.accept(this)));
        }
        return delegate.visitOptional(value);
    }

    private static TypeName box(TypeName input) {
        return input.isPrimitive() ? input.box() : input;
    }

    @Override
    public TypeName visitList(ListType value) {
        return delegate.visitList(value);
    }

    @Override
    public TypeName visitSet(SetType value) {
        return delegate.visitSet(value);
    }

    @Override
    public TypeName visitMap(MapType value) {
        return delegate.visitMap(value);
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
            if (aliasTypeName.equals(visitPrimitive(PrimitiveType.BINARY))) {
                return aliasTypeName;
            }
        }

        return delegate.visitReference(type);
    }

    @Override
    public TypeName visitExternal(ExternalReference value) {
        return delegate.visitExternal(value);
    }
}
