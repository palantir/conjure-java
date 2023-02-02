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

import com.palantir.conjure.java.visitor.MoreVisitors;
import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.ArgumentDefinition;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.LogSafety;
import java.util.Optional;

public final class SafetyUtils {

    private SafetyUtils() {}

    public static Optional<LogSafety> getMaybeExternalSafety(AliasDefinition alias) {
        if (alias.getAlias().accept(MoreVisitors.IS_EXTERNAL)) {
            return alias.getAlias().accept(MoreVisitors.EXTERNAL).getSafety();
        }
        return alias.getSafety();
    }

    public static Optional<LogSafety> getMaybeExternalSafety(FieldDefinition field) {
        if (field.getType().accept(MoreVisitors.IS_EXTERNAL)) {
            return field.getType().accept(MoreVisitors.EXTERNAL).getSafety();
        }
        return field.getSafety();
    }

    public static Optional<LogSafety> getMaybeExternalSafety(ArgumentDefinition argument) {
        if (argument.getType().accept(MoreVisitors.IS_EXTERNAL)) {
            return argument.getType().accept(MoreVisitors.EXTERNAL).getSafety();
        }
        return argument.getSafety();
    }
}
