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

    public static Optional<LogSafety> getMaybeExternalSafety(AliasDefinition alias) {
        if (alias.getAlias().accept(WrapsExternalType.INSTANCE)) {
            return alias.getAlias().accept(GetMaybeExternalSafety.INSTANCE);
        }
        return alias.getSafety();
    }

    public static Optional<LogSafety> getMaybeExternalSafety(FieldDefinition field) {
        if (field.getType().accept(WrapsExternalType.INSTANCE)) {
            return field.getType().accept(GetMaybeExternalSafety.INSTANCE);
        }
        return field.getSafety();
    }

    public static Optional<LogSafety> getMaybeExternalSafety(ArgumentDefinition argument) {
        if (argument.getType().accept(WrapsExternalType.INSTANCE)) {
            return argument.getType().accept(GetMaybeExternalSafety.INSTANCE);
        }
        return argument.getSafety();
    }

    private enum WrapsExternalType implements Type.Visitor<Boolean> {
        INSTANCE;

        @Override
        public java.lang.Boolean visitPrimitive(PrimitiveType _value) {
            return false;
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

    private enum GetMaybeExternalSafety implements Type.Visitor<Optional<LogSafety>> {
        INSTANCE;

        @Override
        public Optional<LogSafety> visitPrimitive(PrimitiveType _value) {
            return Optional.empty();
        }

        @Override
        public Optional<LogSafety> visitOptional(OptionalType value) {
            return value.getItemType().accept(INSTANCE);
        }

        @Override
        public Optional<LogSafety> visitList(ListType value) {
            return value.getItemType().accept(INSTANCE);
        }

        @Override
        public Optional<LogSafety> visitSet(SetType value) {
            return value.getItemType().accept(INSTANCE);
        }

        @Override
        public Optional<LogSafety> visitMap(MapType _value) {
            return Optional.empty();
        }

        @Override
        public Optional<LogSafety> visitReference(TypeName _value) {
            return Optional.empty();
        }

        @Override
        public Optional<LogSafety> visitExternal(ExternalReference value) {
            return value.getSafety();
        }

        @Override
        public Optional<LogSafety> visitUnknown(String _unknownType) {
            return Optional.empty();
        }
    }
}
