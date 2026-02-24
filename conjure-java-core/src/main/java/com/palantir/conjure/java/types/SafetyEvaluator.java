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
import com.palantir.conjure.spec.ArgumentDefinition;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public final class SafetyEvaluator {

    // Make a constant to avoid allocating a new Optional every time we process a type definition for safety
    private static final Optional<LogSafety> OPTIONAL_OF_SAFE = Optional.of(LogSafety.SAFE);

    /**
     * Enums contain an unknown variant, however we assume that the unknown variant is only used
     * for past and future values which are known at compile-time in that version.
     */
    public static final Optional<LogSafety> ENUM_SAFETY = OPTIONAL_OF_SAFE;
    /**
     * Unknown variant should be considered unsafe because we don't know what kind of data it may contain,
     * however this makes rollout much more challenging, so we will ratchet unknown safety once we have
     * better tooling to extract safe components.
     */
    public static final Optional<LogSafety> UNKNOWN_UNION_VARINT_SAFETY = Optional.empty();

    private final Map<TypeName, TypeDefinition> definitionMap;

    // Memoization cache shared across all evaluate() calls on this instance. Avoids redundant recursive
    // traversals of the type graph, which otherwise dominate generation time for large definitions.
    private final Map<TypeName, LogSafety.Value> cache = new HashMap<>();

    public SafetyEvaluator(ConjureDefinition definition) {
        this(TypeFunctions.toTypesMap(definition));
    }

    public SafetyEvaluator(Map<TypeName, TypeDefinition> definitionMap) {
        this.definitionMap = definitionMap;
    }

    public Optional<LogSafety> evaluate(TypeDefinition def) {
        return fromValue(Preconditions.checkNotNull(def, "TypeDefinition is required")
                .accept(new TypeDefinitionSafetyVisitor(definitionMap, cache, new HashSet<>())));
    }

    public Optional<LogSafety> evaluate(Type type) {
        return fromValue(Preconditions.checkNotNull(type, "TypeDefinition is required")
                .accept(new TypeDefinitionSafetyVisitor(definitionMap, cache, new HashSet<>()).fieldVisitor));
    }

    public Optional<LogSafety> evaluate(Type type, Optional<LogSafety> declaredSafety) {
        return declaredSafety.or(() -> evaluate(type));
    }

    /**
     * Certain types (e.g. primitives and external imports) must declare safety at usage time, while others (e.g.
     * conjure objects) must do so themselves at declaration time. This is a utility method to extract usage time safety
     * declarations if any are needed.
     * */
    public Optional<LogSafety> getUsageTimeSafety(ArgumentDefinition argument) {
        if (argument.getType().accept(RequiresSafetyAtUsageTime.INSTANCE)) {
            return evaluate(argument.getType(), argument.getSafety());
        }
        return argument.getSafety();
    }

    /**
     * Certain types (e.g. primitives and external imports) must declare safety at usage time, while others (e.g.
     * conjure objects) must do so themselves at declaration time. This is a utility method to extract usage time safety
     * declarations if any are needed.
     * */
    public Optional<LogSafety> getUsageTimeSafety(AliasDefinition alias) {
        if (alias.getAlias().accept(RequiresSafetyAtUsageTime.INSTANCE)) {
            return evaluate(alias.getAlias(), alias.getSafety());
        }
        return alias.getSafety();
    }

    /**
     * Certain types (e.g. primitives and external imports) must declare safety at usage time, while others (e.g.
     * conjure objects) must do so themselves at declaration time. This is a utility method to extract usage time safety
     * declarations if any are needed.
     * */
    public Optional<LogSafety> getUsageTimeSafety(FieldDefinition field) {
        if (field.getType().accept(RequiresSafetyAtUsageTime.INSTANCE)) {
            return evaluate(field.getType(), field.getSafety());
        }
        return field.getSafety();
    }

    private static Optional<LogSafety> fromValue(LogSafety.Value value) {
        return switch (value) {
            case SAFE -> Optional.of(LogSafety.SAFE);
            case UNSAFE -> Optional.of(LogSafety.UNSAFE);
            case DO_NOT_LOG -> Optional.of(LogSafety.DO_NOT_LOG);
            case UNKNOWN -> Optional.empty();
        };
    }

    private static LogSafety.Value value(Optional<LogSafety> safety) {
        return safety.map(LogSafety::get).orElse(LogSafety.Value.UNKNOWN);
    }

    private static final class TypeDefinitionSafetyVisitor implements TypeDefinition.Visitor<LogSafety.Value> {
        private final Map<TypeName, LogSafety.Value> cache;
        private final Set<TypeName> inProgress;
        private final Type.Visitor<LogSafety.Value> fieldVisitor;

        // Tracks whether cycle-breaking (the SAFE fallback for back-edges) was used anywhere
        // in the current evaluation subtree. Used to decide whether a result is safe to cache.
        private boolean encounteredCycle;

        private TypeDefinitionSafetyVisitor(
                Map<TypeName, TypeDefinition> definitionMap,
                Map<TypeName, LogSafety.Value> cache,
                Set<TypeName> inProgress) {
            this.cache = cache;
            this.inProgress = inProgress;
            this.fieldVisitor = new FieldSafetyVisitor(definitionMap, this);
        }

        @Override
        public LogSafety.Value visitAlias(AliasDefinition value) {
            return with(value.getTypeName(), () -> getSafety(value.getAlias(), value.getSafety()));
        }

        @Override
        public LogSafety.Value visitEnum(EnumDefinition _value) {
            return ENUM_SAFETY.get().get();
        }

        @Override
        public LogSafety.Value visitObject(ObjectDefinition value) {
            return with(value.getTypeName(), () -> {
                LogSafety.Value safety = LogSafety.SAFE.get();
                for (FieldDefinition field : value.getFields()) {
                    safety = combine(safety, getSafety(field.getType(), field.getSafety()));
                }
                return safety;
            });
        }

        @Override
        public LogSafety.Value visitUnion(UnionDefinition value) {
            return with(value.getTypeName(), () -> {
                LogSafety.Value safety = LogSafety.Value.UNKNOWN;
                for (FieldDefinition variant : value.getUnion()) {
                    safety = combine(safety, getSafety(variant.getType(), variant.getSafety()));
                }
                return safety;
            });
        }

        @Override
        public LogSafety.Value visitUnknown(String unknownType) {
            throw new IllegalStateException("Unknown type: " + unknownType);
        }

        private LogSafety.Value with(TypeName typeName, Supplier<LogSafety.Value> task) {
            // Return memoized result if this type has already been fully evaluated.
            // Note: cache values are Optional<LogSafety> which may be Optional.empty(),
            // so we check for null (absent key) rather than emptiness.
            LogSafety.Value cached = cache.get(typeName);
            if (cached != null) {
                return cached;
            }
            if (!inProgress.add(typeName)) {
                // Given recursive evaluation, we return the least restrictive type: SAFE.
                // Mark that this subtree's result depends on cycle-breaking.
                encounteredCycle = true;
                return LogSafety.Value.SAFE;
            }

            // Save and reset cycle state so we can detect cycles within this type's subtree only.
            boolean previousCycleState = encounteredCycle;
            encounteredCycle = false;

            LogSafety.Value result = task.get();

            boolean subtreeHadCycle = encounteredCycle;
            // Propagate cycle detection upward: if this subtree had a cycle, callers should know.
            encounteredCycle = previousCycleState || subtreeHadCycle;

            if (!inProgress.remove(typeName)) {
                throw new IllegalStateException(
                        "Failed to remove " + typeName + " from in-progress, something is very wrong!");
            }

            // Only cache results where no cycle was encountered in the subtree.
            // When a cycle is broken with the SAFE heuristic, the result depends on which type
            // was the entry point, so caching it would produce incorrect results if the same
            // type is later evaluated from a different starting point.
            if (!subtreeHadCycle) {
                cache.put(typeName, result);
            }
            return result;
        }

        private LogSafety.Value getSafety(Type type, Optional<LogSafety> safety) {
            return safety.map(LogSafety::get).orElseGet(() -> type.accept(fieldVisitor));
        }
    }

    private static final class FieldSafetyVisitor implements Type.Visitor<LogSafety.Value> {
        private final Map<TypeName, TypeDefinition> definitionMap;
        private final TypeDefinition.Visitor<LogSafety.Value> typeDefVisitor;

        FieldSafetyVisitor(
                Map<TypeName, TypeDefinition> definitionMap, TypeDefinition.Visitor<LogSafety.Value> typeDefVisitor) {
            this.definitionMap = definitionMap;
            this.typeDefVisitor = typeDefVisitor;
        }

        @Override
        public LogSafety.Value visitPrimitive(PrimitiveType value) {
            return value.accept(PrimitiveTypeSafetyVisitor.INSTANCE);
        }

        @Override
        public LogSafety.Value visitOptional(OptionalType value) {
            return value.getItemType().accept(this);
        }

        @Override
        public LogSafety.Value visitList(ListType value) {
            return value.getItemType().accept(this);
        }

        @Override
        public LogSafety.Value visitSet(SetType value) {
            return value.getItemType().accept(this);
        }

        @Override
        public LogSafety.Value visitMap(MapType value) {
            LogSafety.Value keySafety = value.getKeyType().accept(this);
            LogSafety.Value valueSafety = value.getValueType().accept(this);
            return combine(keySafety, valueSafety);
        }

        @Override
        public LogSafety.Value visitReference(TypeName value) {
            // inProgress is handled by TypeDefinitionSafetyVisitor
            return Optional.ofNullable(definitionMap.get(value))
                    .map(item -> item.accept(typeDefVisitor))
                    .orElse(LogSafety.Value.UNKNOWN);
        }

        @Override
        public LogSafety.Value visitExternal(ExternalReference value) {
            return value(value.getSafety());
        }

        @Override
        public LogSafety.Value visitUnknown(String unknownType) {
            throw new IllegalStateException("Unknown type: " + unknownType);
        }
    }

    private enum PrimitiveTypeSafetyVisitor implements PrimitiveType.Visitor<LogSafety.Value> {
        INSTANCE;

        @Override
        public LogSafety.Value visitString() {
            return LogSafety.Value.UNKNOWN;
        }

        @Override
        public LogSafety.Value visitDatetime() {
            return LogSafety.Value.UNKNOWN;
        }

        @Override
        public LogSafety.Value visitInteger() {
            return LogSafety.Value.UNKNOWN;
        }

        @Override
        public LogSafety.Value visitDouble() {
            return LogSafety.Value.UNKNOWN;
        }

        @Override
        public LogSafety.Value visitSafelong() {
            return LogSafety.Value.UNKNOWN;
        }

        @Override
        public LogSafety.Value visitBinary() {
            return LogSafety.Value.UNKNOWN;
        }

        @Override
        public LogSafety.Value visitAny() {
            return LogSafety.Value.UNKNOWN;
        }

        @Override
        public LogSafety.Value visitBoolean() {
            return LogSafety.Value.UNKNOWN;
        }

        @Override
        public LogSafety.Value visitUuid() {
            return LogSafety.Value.UNKNOWN;
        }

        @Override
        public LogSafety.Value visitRid() {
            return LogSafety.Value.UNKNOWN;
        }

        @Override
        public LogSafety.Value visitBearertoken() {
            return LogSafety.Value.DO_NOT_LOG;
        }

        @Override
        public LogSafety.Value visitUnknown(String unknownValue) {
            throw new IllegalStateException("Unknown primitive type: " + unknownValue);
        }
    }

    private static final int SAFETY_VALUE_COUNT = LogSafety.Value.values().length;
    private static final LogSafety.Value[] LOG_SAFETY_TABLE = computeLogSafetyTable();

    private static LogSafety.Value[] computeLogSafetyTable() {
        LogSafety.Value[] table = new LogSafety.Value[SAFETY_VALUE_COUNT * SAFETY_VALUE_COUNT];
        for (LogSafety.Value left : LogSafety.Value.values()) {
            for (LogSafety.Value right : LogSafety.Value.values()) {
                table[index(left, right)] = combineInternal(left, right);
            }
        }
        return table;
    }

    private static LogSafety.Value combineInternal(LogSafety.Value left, LogSafety.Value right) {
        if (left == LogSafety.Value.DO_NOT_LOG || right == LogSafety.Value.DO_NOT_LOG) {
            return LogSafety.Value.DO_NOT_LOG;
        }
        if (left == LogSafety.Value.UNSAFE || right == LogSafety.Value.UNSAFE) {
            return LogSafety.Value.UNSAFE;
        }
        if (left == LogSafety.Value.UNKNOWN || right == LogSafety.Value.UNKNOWN) {
            return LogSafety.Value.UNKNOWN;
        }
        return LogSafety.Value.SAFE;
    }

    @SuppressWarnings("EnumOrdinal")
    private static int index(LogSafety.Value left, LogSafety.Value right) {
        return left.ordinal() * SAFETY_VALUE_COUNT + right.ordinal();
    }

    // TODO: remove this
    public static Optional<LogSafety> combine(Optional<LogSafety> one, Optional<LogSafety> two) {
        LogSafety.Value combined = combine(value(one), value(two));

        return fromValue(combined);
    }

    private static LogSafety.Value combine(LogSafety.Value one, LogSafety.Value two) {
        return Preconditions.checkNotNull(LOG_SAFETY_TABLE[index(one, two)], "Missing an entry in the combine table");
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
    // primitive and external types (and types that wrap them) must declare their safety at usage time
    // for all other types, assume the generated class declares safety at definition time
    private enum RequiresSafetyAtUsageTime implements Type.Visitor<Boolean> {
        INSTANCE;

        @Override
        public java.lang.Boolean visitPrimitive(PrimitiveType value) {
            return !value.equals(PrimitiveType.BEARERTOKEN);
        }

        @Override
        public Boolean visitOptional(OptionalType value) {
            return value.getItemType().accept(INSTANCE);
        }

        @Override
        public Boolean visitList(ListType value) {
            return value.getItemType().accept(INSTANCE);
        }

        @Override
        public Boolean visitSet(SetType value) {
            return value.getItemType().accept(INSTANCE);
        }

        @Override
        public Boolean visitMap(MapType _value) {
            return false;
        }

        @Override
        public Boolean visitReference(TypeName _value) {
            return false;
        }

        @Override
        public Boolean visitExternal(ExternalReference _value) {
            return true;
        }

        @Override
        public Boolean visitUnknown(String _unknownType) {
            return false;
        }
    }
}
