/*
 * (c) Copyright 2022 Palantir Technologies Inc. All rights reserved.
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

import com.palantir.conjure.java.util.TypeFunctions;
import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.EnumDefinition;
import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.LogSafety;
import com.palantir.conjure.spec.LogSafety.Visitor;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.ObjectDefinition;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.TypeName;
import com.palantir.conjure.spec.UnionDefinition;
import com.palantir.logsafe.Preconditions;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public final class SafetyEvaluator {
    /**
     * Enums contain an unknown variant, however we assume that the unknown variant is only used
     * for past and future values which are known at compile-time in that version.
     */
    public static final Optional<LogSafety> ENUM_SAFETY = Optional.of(LogSafety.SAFE);
    /** Unknown variant is considered unsafe because we don't know what kind of data it may contain. */
    public static final Optional<LogSafety> UNKNOWN_UNION_VARINT_SAFETY = Optional.of(LogSafety.UNSAFE);

    private final Map<TypeName, TypeDefinition> definitionMap;

    public SafetyEvaluator(ConjureDefinition definition) {
        this(TypeFunctions.toTypesMap(definition));
    }

    public SafetyEvaluator(Map<TypeName, TypeDefinition> definitionMap) {
        this.definitionMap = definitionMap;
    }

    public Optional<LogSafety> evaluate(TypeDefinition def) {
        return Preconditions.checkNotNull(def, "TypeDefinition is required")
                .accept(new TypeDefinitionSafetyVisitor(definitionMap, new HashSet<>()));
    }

    public Optional<LogSafety> evaluate(Type type) {
        return Preconditions.checkNotNull(type, "TypeDefinition is required")
                .accept(new TypeDefinitionSafetyVisitor(definitionMap, new HashSet<>()).fieldVisitor);
    }

    public Optional<LogSafety> evaluate(Type type, Optional<LogSafety> declaredSafety) {
        return declaredSafety.or(() -> evaluate(type));
    }

    private static final class TypeDefinitionSafetyVisitor implements TypeDefinition.Visitor<Optional<LogSafety>> {
        private final Set<TypeName> inProgress;
        private final Type.Visitor<Optional<LogSafety>> fieldVisitor;

        private TypeDefinitionSafetyVisitor(Map<TypeName, TypeDefinition> definitionMap, Set<TypeName> inProgress) {
            this.inProgress = inProgress;
            this.fieldVisitor = new FieldSafetyVisitor(definitionMap, this);
        }

        @Override
        public Optional<LogSafety> visitAlias(AliasDefinition value) {
            return with(value.getTypeName(), () -> getSafety(value.getAlias(), value.getSafety()));
        }

        @Override
        public Optional<LogSafety> visitEnum(EnumDefinition _value) {
            return ENUM_SAFETY;
        }

        @Override
        public Optional<LogSafety> visitObject(ObjectDefinition value) {
            return with(value.getTypeName(), () -> {
                Optional<LogSafety> safety = Optional.of(LogSafety.SAFE);
                for (FieldDefinition field : value.getFields()) {
                    safety = combine(safety, getSafety(field.getType(), field.getSafety()));
                }
                return safety;
            });
        }

        @Override
        public Optional<LogSafety> visitUnion(UnionDefinition value) {
            return with(value.getTypeName(), () -> {
                Optional<LogSafety> safety = UNKNOWN_UNION_VARINT_SAFETY;
                for (FieldDefinition variant : value.getUnion()) {
                    safety = combine(safety, getSafety(variant.getType(), variant.getSafety()));
                }
                return safety;
            });
        }

        @Override
        public Optional<LogSafety> visitUnknown(String unknownType) {
            throw new IllegalStateException("Unknown type: " + unknownType);
        }

        private Optional<LogSafety> with(TypeName typeName, Supplier<Optional<LogSafety>> task) {
            if (!inProgress.add(typeName)) {
                // Given recursive evaluation, we return the least restrictive type: SAFE.
                return Optional.of(LogSafety.SAFE);
            }
            Optional<LogSafety> result = task.get();
            if (!inProgress.remove(typeName)) {
                throw new IllegalStateException(
                        "Failed to remove " + typeName + " from in-progress, something is very wrong!");
            }
            return result;
        }

        private Optional<LogSafety> getSafety(Type type, Optional<LogSafety> safety) {
            return safety.or(() -> type.accept(fieldVisitor));
        }
    }

    private static final class FieldSafetyVisitor implements Type.Visitor<Optional<LogSafety>> {
        private final Map<TypeName, TypeDefinition> definitionMap;
        private final TypeDefinition.Visitor<Optional<LogSafety>> typeDefVisitor;

        FieldSafetyVisitor(
                Map<TypeName, TypeDefinition> definitionMap,
                TypeDefinition.Visitor<Optional<LogSafety>> typeDefVisitor) {
            this.definitionMap = definitionMap;
            this.typeDefVisitor = typeDefVisitor;
        }

        @Override
        public Optional<LogSafety> visitPrimitive(PrimitiveType value) {
            return value.accept(PrimitiveTypeSafetyVisitor.INSTANCE);
        }

        @Override
        public Optional<LogSafety> visitOptional(OptionalType value) {
            return value.getItemType().accept(this);
        }

        @Override
        public Optional<LogSafety> visitList(ListType value) {
            return value.getItemType().accept(this);
        }

        @Override
        public Optional<LogSafety> visitSet(SetType value) {
            return value.getItemType().accept(this);
        }

        @Override
        public Optional<LogSafety> visitMap(MapType value) {
            Optional<LogSafety> keySafety = value.getKeyType().accept(this);
            Optional<LogSafety> valueSafety = value.getValueType().accept(this);
            return combine(keySafety, valueSafety);
        }

        @Override
        public Optional<LogSafety> visitReference(TypeName value) {
            // inProgress is handled by TypeDefinitionSafetyVisitor
            return Optional.ofNullable(definitionMap.get(value)).flatMap(item -> item.accept(typeDefVisitor));
        }

        @Override
        public Optional<LogSafety> visitExternal(ExternalReference _value) {
            // External types have unknown safety for now
            return Optional.empty();
        }

        @Override
        public Optional<LogSafety> visitUnknown(String unknownType) {
            throw new IllegalStateException("Unknown type: " + unknownType);
        }
    }

    private enum PrimitiveTypeSafetyVisitor implements PrimitiveType.Visitor<Optional<LogSafety>> {
        INSTANCE;

        @Override
        public Optional<LogSafety> visitString() {
            return Optional.empty();
        }

        @Override
        public Optional<LogSafety> visitDatetime() {
            return Optional.empty();
        }

        @Override
        public Optional<LogSafety> visitInteger() {
            return Optional.empty();
        }

        @Override
        public Optional<LogSafety> visitDouble() {
            return Optional.empty();
        }

        @Override
        public Optional<LogSafety> visitSafelong() {
            return Optional.empty();
        }

        @Override
        public Optional<LogSafety> visitBinary() {
            return Optional.empty();
        }

        @Override
        public Optional<LogSafety> visitAny() {
            return Optional.empty();
        }

        @Override
        public Optional<LogSafety> visitBoolean() {
            return Optional.empty();
        }

        @Override
        public Optional<LogSafety> visitUuid() {
            return Optional.empty();
        }

        @Override
        public Optional<LogSafety> visitRid() {
            return Optional.empty();
        }

        @Override
        public Optional<LogSafety> visitBearertoken() {
            return Optional.of(LogSafety.DO_NOT_LOG);
        }

        @Override
        public Optional<LogSafety> visitUnknown(String unknownValue) {
            throw new IllegalStateException("Unknown primitive type: " + unknownValue);
        }
    }

    public static Optional<LogSafety> combine(Optional<LogSafety> one, Optional<LogSafety> two) {
        if (one.isPresent() && two.isPresent()) {
            return Optional.of(combine(one.get(), two.get()));
        }
        return one.or(() -> two)
                // When one value is unknown, we cannot assume the other is safe
                .filter(value -> !LogSafety.SAFE.equals(value));
    }

    public static LogSafety combine(LogSafety one, LogSafety two) {
        LogSafety.Value first = one.get();
        LogSafety.Value second = two.get();
        if (first == LogSafety.Value.UNKNOWN || second == LogSafety.Value.UNKNOWN) {
            throw new IllegalStateException("Unable to compare LogSafety values: " + one + " and " + two);
        }
        if (first == LogSafety.Value.DO_NOT_LOG || second == LogSafety.Value.DO_NOT_LOG) {
            return LogSafety.DO_NOT_LOG;
        }
        if (first == LogSafety.Value.UNSAFE || second == LogSafety.Value.UNSAFE) {
            return LogSafety.UNSAFE;
        }
        return one;
    }

    public static boolean allows(Optional<LogSafety> required, Optional<LogSafety> given) {
        if (required.isEmpty() || given.isEmpty()) {
            // If there is no requirement, all inputs are allowed.
            // If there is a requirement but the input is unknown,
            // this serves as the initial determination.
            return true;
        }
        return allows(required.get(), given.get());
    }

    public static boolean allows(LogSafety required, LogSafety given) {
        return required.accept(new Visitor<>() {
            @Override
            public Boolean visitSafe() {
                return LogSafety.SAFE.equals(given);
            }

            @Override
            public Boolean visitUnsafe() {
                return !LogSafety.DO_NOT_LOG.equals(given);
            }

            @Override
            public Boolean visitDoNotLog() {
                return true;
            }

            @Override
            public Boolean visitUnknown(String unknownValue) {
                throw new IllegalStateException("Unknown LogSafety value: " + unknownValue);
            }
        });
    }
}
