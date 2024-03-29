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
import com.palantir.conjure.java.lib.Bytes;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.java.util.Primitives;
import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.visitor.TypeVisitor;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.BearerToken;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import java.nio.ByteBuffer;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;

/**
 * Maps the conjure type into the 'standard' java type i.e. the type one would use in beans/normal variables (as opposed
 * to e.g. service definitions).
 */
public final class DefaultClassNameVisitor implements ClassNameVisitor {

    private final Set<com.palantir.conjure.spec.TypeName> typeNames;
    private final Options options;

    public DefaultClassNameVisitor(Set<com.palantir.conjure.spec.TypeName> typeNames, Options options) {
        this.typeNames = typeNames;
        this.options = options;
    }

    @Override
    public TypeName visitList(ListType type) {
        TypeName itemType = Primitives.box(type.getItemType().accept(this));
        return ParameterizedTypeName.get(ClassName.get(List.class), itemType);
    }

    @Override
    public TypeName visitSet(SetType type) {
        TypeName itemType = Primitives.box(type.getItemType().accept(this));
        return ParameterizedTypeName.get(ClassName.get(Set.class), itemType);
    }

    @Override
    public TypeName visitMap(MapType type) {
        return ParameterizedTypeName.get(
                ClassName.get(Map.class),
                Primitives.box(type.getKeyType().accept(this)),
                Primitives.box(type.getValueType().accept(this)));
    }

    @Override
    @SuppressWarnings("CyclomaticComplexity")
    public TypeName visitOptional(OptionalType type) {
        if (type.getItemType().accept(TypeVisitor.IS_PRIMITIVE)) {
            PrimitiveType primitiveType = type.getItemType().accept(TypeVisitor.PRIMITIVE);
            // special handling for primitive optionals with Java 8
            switch (primitiveType.get()) {
                case UNKNOWN:
                    throw new IllegalStateException("Unknown type " + primitiveType);
                case DOUBLE:
                    return ClassName.get(OptionalDouble.class);
                case INTEGER:
                    return ClassName.get(OptionalInt.class);
                case BOOLEAN:
                    // no OptionalBoolean type
                case SAFELONG:
                case STRING:
                case RID:
                case BEARERTOKEN:
                case UUID:
                case DATETIME:
                case BINARY:
                case ANY:
                    // treat normally
            }
        }

        TypeName itemType = type.getItemType().accept(this);
        if (Primitives.isPrimitive(itemType)) {
            // Safe for primitives (e.g. Booleans with Java 8)
            itemType = Primitives.box(itemType);
        }
        return ParameterizedTypeName.get(ClassName.get(Optional.class), itemType);
    }

    @Override
    @SuppressWarnings("checkstyle:cyclomaticcomplexity")
    public TypeName visitPrimitive(PrimitiveType type) {
        switch (type.get()) {
            case STRING:
                return ClassName.get(String.class);
            case DATETIME:
                return ClassName.get(OffsetDateTime.class);
            case INTEGER:
                return TypeName.INT;
            case DOUBLE:
                return TypeName.DOUBLE;
            case SAFELONG:
                return ClassName.get(SafeLong.class);
            case BINARY:
                return options.useImmutableBytes() ? ClassName.get(Bytes.class) : ClassName.get(ByteBuffer.class);
            case ANY:
                return ClassName.get(Object.class);
            case BOOLEAN:
                return TypeName.BOOLEAN;
            case UUID:
                return ClassName.get(UUID.class);
            case RID:
                return ClassName.get(ResourceIdentifier.class);
            case BEARERTOKEN:
                return ClassName.get(BearerToken.class);
            case UNKNOWN:
                // fall through
        }
        throw new IllegalStateException("Unknown primitive type: " + type);
    }

    @Override
    public TypeName visitReference(com.palantir.conjure.spec.TypeName type) {
        // Types without namespace are either defined locally in this conjure definition, or raw imports.
        if (typeNames.contains(type)) {
            return ClassName.get(
                    Packages.getPrefixedPackage(type.getPackage(), options.packagePrefix()), type.getName());
        } else {
            throw new IllegalStateException("Unknown LocalReferenceType type: " + type);
        }
    }

    @Override
    public TypeName visitExternal(ExternalReference externalType) {
        String conjurePackage = externalType.getExternalReference().getPackage();
        ClassName typeName = ClassName.get(
                conjurePackage, externalType.getExternalReference().getName());
        return Primitives.isBoxedPrimitive(typeName) ? Primitives.unbox(typeName) : typeName;
    }

    @Override
    public TypeName visitUnknown(String unknownType) {
        throw new IllegalStateException("Unknown type: " + unknownType);
    }
}
