/*
 * (c) Copyright 2023 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.util;

import com.google.common.collect.ImmutableMap;
import com.palantir.conjure.java.types.SafetyEvaluator;
import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.ArgumentDefinition;
import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.LogSafety;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeName;
import java.util.Optional;

public final class SafetyUtils {
    private SafetyUtils() {}

    // SafetyEvaluator uses its TypeDefinitionMap to resolve the safety of reference types, but these utils
    // only evaluate the safety of external imports and primitives, so it's safe to use a placeholder
    private static final SafetyEvaluator safetyEvaluator = new SafetyEvaluator(ImmutableMap.of());

    public static Optional<LogSafety> getUsageTimeSafety(AliasDefinition alias) {
        if (alias.getAlias().accept(RequiresSafetyAtUsageTime.INSTANCE)) {
            return safetyEvaluator.evaluate(alias.getAlias(), alias.getSafety());
        }
        return alias.getSafety();
    }

    public static Optional<LogSafety> getUsageTimeSafety(FieldDefinition field) {
        if (field.getType().accept(RequiresSafetyAtUsageTime.INSTANCE)) {
            return safetyEvaluator.evaluate(field.getType(), field.getSafety());
        }
        return field.getSafety();
    }

    public static Optional<LogSafety> getUsageTimeSafety(ArgumentDefinition argument) {
        if (argument.getType().accept(RequiresSafetyAtUsageTime.INSTANCE)) {
            return safetyEvaluator.evaluate(argument.getType(), argument.getSafety());
        }
        return argument.getSafety();
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
