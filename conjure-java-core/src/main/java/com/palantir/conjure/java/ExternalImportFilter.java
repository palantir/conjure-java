/*
 * (c) Copyright 2024 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.ArgumentDefinition;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.EnumDefinition;
import com.palantir.conjure.spec.ErrorDefinition;
import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.LogSafety;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.ObjectDefinition;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.Type.Visitor;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.TypeName;
import com.palantir.conjure.spec.UnionDefinition;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import java.util.Optional;

/** Utility to filter a {@link ConjureDefinition} into an equivalent definition with no external type imports. */
final class ExternalImportFilter {

    private final Options options;

    ExternalImportFilter(Options options) {
        this.options = options;
    }

    ConjureDefinition filter(ConjureDefinition definition) {
        if (options.externalFallbackTypes()) {
            return ConjureDefinition.builder()
                    .from(definition)
                    .errors(Lists.transform(definition.getErrors(), this::filter))
                    .types(Lists.transform(definition.getTypes(), this::filter))
                    .services(Lists.transform(definition.getServices(), this::filter))
                    .build();
        } else {
            return definition;
        }
    }

    private ErrorDefinition filter(ErrorDefinition definition) {
        return ErrorDefinition.builder()
                .from(definition)
                .safeArgs(Lists.transform(definition.getSafeArgs(), this::filter))
                .unsafeArgs(Lists.transform(definition.getUnsafeArgs(), this::filter))
                .build();
    }

    private TypeDefinition filter(TypeDefinition definition) {
        return definition.accept(new TypeDefinition.Visitor<>() {
            @Override
            public TypeDefinition visitAlias(AliasDefinition value) {
                TypeWithSafety typeWithSafety = filter(value.getAlias());
                return TypeDefinition.alias(AliasDefinition.builder()
                        .from(value)
                        .alias(typeWithSafety.type())
                        .safety(typeWithSafety.safety().or(value::getSafety))
                        .build());
            }

            @Override
            public TypeDefinition visitEnum(EnumDefinition value) {
                return TypeDefinition.enum_(value);
            }

            @Override
            public TypeDefinition visitObject(ObjectDefinition value) {
                return TypeDefinition.object(ObjectDefinition.builder()
                        .from(value)
                        .fields(Lists.transform(value.getFields(), ExternalImportFilter.this::filter))
                        .build());
            }

            @Override
            public TypeDefinition visitUnion(UnionDefinition value) {
                return TypeDefinition.union(UnionDefinition.builder()
                        .from(value)
                        .union(Lists.transform(value.getUnion(), ExternalImportFilter.this::filter))
                        .build());
            }

            @Override
            public TypeDefinition visitUnknown(@Safe String unknownType) {
                throw new SafeIllegalStateException("Unknown type", SafeArg.of("unknownType", unknownType));
            }
        });
    }

    private ServiceDefinition filter(ServiceDefinition definition) {
        return ServiceDefinition.builder()
                .from(definition)
                .endpoints(Lists.transform(definition.getEndpoints(), this::filter))
                .build();
    }

    private EndpointDefinition filter(EndpointDefinition definition) {
        return EndpointDefinition.builder()
                .from(definition)
                // Remove markers, which are required to be external type imports
                .markers(ImmutableList.of())
                .args(Lists.transform(definition.getArgs(), this::filter))
                .returns(definition.getReturns().map(this::filter).map(TypeWithSafety::type))
                .build();
    }

    private ArgumentDefinition filter(ArgumentDefinition definition) {
        TypeWithSafety typeWithSafety = filter(definition.getType());
        return ArgumentDefinition.builder()
                .from(definition)
                // Remove markers, which are required to be external type imports
                .markers(ImmutableList.of())
                .type(typeWithSafety.type())
                .safety(typeWithSafety.safety().or(definition::getSafety))
                .build();
    }

    private FieldDefinition filter(FieldDefinition definition) {
        TypeWithSafety typeWithSafety = filter(definition.getType());
        return FieldDefinition.builder()
                .from(definition)
                .type(typeWithSafety.type())
                .safety(typeWithSafety.safety().or(definition::getSafety))
                .build();
    }

    private TypeWithSafety filter(Type definition) {
        return definition.accept(TypeFilter.INSTANCE).orElseGet(() -> new TypeWithSafety(definition, Optional.empty()));
    }

    private enum TypeFilter implements Visitor<Optional<TypeWithSafety>> {
        INSTANCE;

        @Override
        public Optional<TypeWithSafety> visitPrimitive(PrimitiveType _value) {
            return Optional.empty();
        }

        @Override
        public Optional<TypeWithSafety> visitOptional(OptionalType value) {
            return value.getItemType()
                    .accept(this)
                    .map(result -> new TypeWithSafety(Type.optional(OptionalType.of(result.type())), result.safety()));
        }

        @Override
        public Optional<TypeWithSafety> visitList(ListType value) {
            return value.getItemType()
                    .accept(this)
                    .map(result -> new TypeWithSafety(Type.list(ListType.of(result.type())), result.safety()));
        }

        @Override
        public Optional<TypeWithSafety> visitSet(SetType value) {
            return value.getItemType()
                    .accept(this)
                    .map(result -> new TypeWithSafety(Type.set(SetType.of(result.type())), result.safety()));
        }

        @Override
        public Optional<TypeWithSafety> visitMap(MapType value) {
            Optional<TypeWithSafety> keyType = value.getKeyType().accept(this);
            Optional<TypeWithSafety> valueType = value.getValueType().accept(this);
            if (keyType.isEmpty() && valueType.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(new TypeWithSafety(
                    Type.map(MapType.of(
                            keyType.map(TypeWithSafety::type).orElseGet(value::getKeyType),
                            valueType.map(TypeWithSafety::type).orElseGet(value::getValueType))),
                    // Safety on maps is tricky due to the rules we apply around what may and may not be marked
                    Optional.empty()));
        }

        @Override
        public Optional<TypeWithSafety> visitReference(TypeName _value) {
            return Optional.empty();
        }

        @Override
        public Optional<TypeWithSafety> visitExternal(ExternalReference value) {
            return Optional.of(new TypeWithSafety(value.getFallback(), value.getSafety()));
        }

        @Override
        public Optional<TypeWithSafety> visitUnknown(@Safe String _unknownType) {
            return Optional.empty();
        }
    }

    private static final class TypeWithSafety {
        private final Type type;
        private final Optional<LogSafety> safety;

        TypeWithSafety(Type type, Optional<LogSafety> safety) {
            this.type = type;
            this.safety = safety;
        }

        Type type() {
            return type;
        }

        Optional<LogSafety> safety() {
            return safety;
        }
    }
}
