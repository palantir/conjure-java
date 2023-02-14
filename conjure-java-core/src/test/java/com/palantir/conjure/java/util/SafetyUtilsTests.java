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

import static org.assertj.core.api.Assertions.assertThat;

import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.FieldName;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.LogSafety;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeName;
import org.junit.jupiter.api.Test;

public class SafetyUtilsTests {

    private static final Type SAFE_EXTERNAL = Type.external(ExternalReference.builder()
            .externalReference(TypeName.of("Long", "java.lang"))
            .fallback(Type.primitive(PrimitiveType.STRING))
            .safety(LogSafety.SAFE)
            .build());

    @Test
    public void externalField() {
        FieldDefinition field = FieldDefinition.builder()
                .fieldName(FieldName.of("testField"))
                .type(SAFE_EXTERNAL)
                .build();
        assertThat(SafetyUtils.getUsageTimeSafety(field)).isPresent().get().isEqualTo(LogSafety.SAFE);
    }

    @Test
    public void primitiveField() {
        FieldDefinition field = FieldDefinition.builder()
                .fieldName(FieldName.of("testField"))
                .type(Type.primitive(PrimitiveType.STRING))
                .safety(LogSafety.SAFE)
                .build();
        assertThat(SafetyUtils.getUsageTimeSafety(field)).isPresent().get().isEqualTo(LogSafety.SAFE);
    }

    @Test
    public void externalWrapper() {
        FieldDefinition field = FieldDefinition.builder()
                .fieldName(FieldName.of("testField"))
                .type(Type.list(ListType.builder().itemType(SAFE_EXTERNAL).build()))
                .build();
        assertThat(SafetyUtils.getUsageTimeSafety(field)).isPresent().get().isEqualTo(LogSafety.SAFE);
    }

    @Test
    public void primitiveWrapper() {
        FieldDefinition field = FieldDefinition.builder()
                .fieldName(FieldName.of("testField"))
                .type(Type.list(ListType.builder()
                        .itemType(Type.primitive(PrimitiveType.STRING))
                        .build()))
                .safety(LogSafety.SAFE)
                .build();
        assertThat(SafetyUtils.getUsageTimeSafety(field)).isPresent().get().isEqualTo(LogSafety.SAFE);
    }

    @Test
    public void referenceField() {
        FieldDefinition field = FieldDefinition.builder()
                .fieldName(FieldName.of("testField"))
                .type(Type.primitive(PrimitiveType.BEARERTOKEN))
                .build();
        assertThat(SafetyUtils.getUsageTimeSafety(field)).isEmpty();
    }

    @Test
    public void referenceFieldWrapper() {
        FieldDefinition field = FieldDefinition.builder()
                .fieldName(FieldName.of("testField"))
                .type(Type.list(ListType.builder()
                        .itemType(Type.primitive(PrimitiveType.BEARERTOKEN))
                        .build()))
                .build();
        assertThat(SafetyUtils.getUsageTimeSafety(field)).isEmpty();
    }
}
