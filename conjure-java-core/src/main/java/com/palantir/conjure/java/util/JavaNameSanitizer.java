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

package com.palantir.conjure.java.util;

import com.google.common.collect.ImmutableSet;
import com.palantir.conjure.spec.FieldName;
import javax.lang.model.SourceVersion;

public final class JavaNameSanitizer {

    private static final ImmutableSet<String> RESERVED_FIELD_NAMES = ImmutableSet.of("memoizedHashCode");

    /**
     * Sanitizes the given {@link FieldName} for use as a java specifier.
     */
    public static String sanitize(FieldName fieldName) {
        String identifier = CaseConverter.toCase(fieldName.get(), CaseConverter.Case.LOWER_CAMEL_CASE);
        return sanitize(identifier);
    }

    private static String sanitize(String name) {
        return SourceVersion.isName(name) && !RESERVED_FIELD_NAMES.contains(name) ? name : name + "_";
    }

    private JavaNameSanitizer() {}
}
