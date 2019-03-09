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

package com.palantir.conjure.java.types;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.palantir.conjure.java.FeatureFlags;
import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.visitor.TypeVisitor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Set;

/**
 * Maps the conjure type into the 'standard' java type, applying guava immutable collections where possible.
 * This is helpful as a deserialization target, but should not be used for exposed API.
 */
public final class GuavaImmutableClassNameVisitor implements ClassNameVisitor {

    private final ClassNameVisitor defaultDelegate;

    public GuavaImmutableClassNameVisitor(List<TypeDefinition> types, Set<FeatureFlags> featureFlags) {
        this.defaultDelegate = new DefaultClassNameVisitor(types, featureFlags);
    }

    @Override
    public TypeName visitList(ListType type) {
        TypeName itemType = type.getItemType().accept(this).box();
        return ParameterizedTypeName.get(ClassName.get(ImmutableList.class), itemType);
    }

    @Override
    public TypeName visitMap(MapType type) {
        return ParameterizedTypeName.get(ClassName.get(ImmutableMap.class),
                type.getKeyType().accept(this).box(),
                type.getValueType().accept(this).box());
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
        if (itemType.isPrimitive()) {
            // Safe for primitives (e.g. Booleans with Java 8)
            itemType = itemType.box();
        }
        return ParameterizedTypeName.get(ClassName.get(Optional.class), itemType);
    }

    @Override
    @SuppressWarnings("checkstyle:cyclomaticcomplexity")
    public TypeName visitPrimitive(PrimitiveType type) {
        return defaultDelegate.visitPrimitive(type);
    }

    @Override
    public TypeName visitReference(com.palantir.conjure.spec.TypeName type) {
        return defaultDelegate.visitReference(type);
    }

    @Override
    public TypeName visitExternal(ExternalReference externalType) {
        return defaultDelegate.visitExternal(externalType);
    }

    @Override
    public TypeName visitSet(SetType type) {
        TypeName itemType = type.getItemType().accept(this).box();
        return ParameterizedTypeName.get(ClassName.get(ImmutableSet.class), itemType);
    }

    @Override
    public TypeName visitUnknown(String unknownType) {
        return defaultDelegate.visitUnknown(unknownType);
    }
}
