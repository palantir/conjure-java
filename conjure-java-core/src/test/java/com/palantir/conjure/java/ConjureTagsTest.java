/*
 * (c) Copyright 2020 Palantir Technologies Inc. All rights reserved.
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.common.collect.ImmutableMap;
import com.palantir.conjure.java.types.SafetyEvaluator;
import com.palantir.conjure.spec.ArgumentDefinition;
import com.palantir.conjure.spec.ArgumentName;
import com.palantir.conjure.spec.BodyParameterType;
import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.LogSafety;
import com.palantir.conjure.spec.ParameterType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeName;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.Unsafe;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class ConjureTagsTest {

    @Test
    void testEmpty() {
        assertThat(ConjureTags.safety(tags())).isEmpty();
    }

    @Test
    void testUnknown() {
        assertThat(ConjureTags.safety(tags("unknown tag"))).isEmpty();
    }

    @Test
    void testSafe() {
        assertThat(ConjureTags.safety(tags("safe"))).hasValue(LogSafety.SAFE);
    }

    @Test
    void testUnsafe() {
        assertThat(ConjureTags.safety(tags("unsafe"))).hasValue(LogSafety.UNSAFE);
    }

    @Test
    void testSafeAndUnsafe() {
        assertThatThrownBy(() -> ConjureTags.safety(tags("safe", "unsafe")))
                .isInstanceOf(SafeIllegalStateException.class)
                .hasMessageContaining("Tags cannot include both safe and unsafe");
    }

    @Test
    void testUnexpectedCase() {
        assertThatThrownBy(() -> ConjureTags.safety(tags("Safe")))
                .isInstanceOf(SafeIllegalStateException.class)
                .hasMessageContaining("Unexpected capitalization");
    }

    @Test
    void testSafeMarkerAndTag() {
        assertThatThrownBy(() -> ConjureTags.safety(ArgumentDefinition.builder()
                        .from(tags("safe"))
                        .markers(Type.external(ExternalReference.builder()
                                .externalReference(TypeName.of(
                                        Safe.class.getSimpleName(),
                                        Safe.class.getPackage().getName()))
                                .fallback(Type.primitive(PrimitiveType.ANY))
                                .build()))
                        .build()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Unexpected com.palantir.logsafe.Safe marker in addition to a 'safe' tag");
    }

    @Test
    void testSafetyAndMarker() {
        assertThatThrownBy(() -> ConjureTags.safety(ArgumentDefinition.builder()
                        .from(tags())
                        .safety(LogSafety.SAFE)
                        .markers(Type.external(ExternalReference.builder()
                                .externalReference(TypeName.of(
                                        Safe.class.getSimpleName(),
                                        Safe.class.getPackage().getName()))
                                .fallback(Type.primitive(PrimitiveType.ANY))
                                .build()))
                        .build()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("safety: safe' value in addition to a "
                        + "'com.palantir.logsafe.Safe' marker on argument 'name'");
    }

    @Test
    void testSafetyAndTag() {
        assertThatThrownBy(() -> ConjureTags.safety(ArgumentDefinition.builder()
                        .from(tags("safe"))
                        .safety(LogSafety.SAFE)
                        .build()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("safety: safe' value in addition to a 'safe' tag on argument 'name'");
    }

    @Test
    void testUnsafeMarkerAndSafeTag() {
        assertThatThrownBy(() -> ConjureTags.safety(ArgumentDefinition.builder()
                        .from(tags("safe"))
                        .markers(Type.external(ExternalReference.builder()
                                .externalReference(TypeName.of(
                                        Unsafe.class.getSimpleName(),
                                        Unsafe.class.getPackage().getName()))
                                .fallback(Type.primitive(PrimitiveType.ANY))
                                .build()))
                        .build()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Unexpected com.palantir.logsafe.Unsafe marker in addition to a 'safe' tag");
    }

    @Test
    void testSafeMarkerAndUnsafeTag() {
        assertThatThrownBy(() -> ConjureTags.safety(ArgumentDefinition.builder()
                        .from(tags("unsafe"))
                        .markers(Type.external(ExternalReference.builder()
                                .externalReference(TypeName.of(
                                        Safe.class.getSimpleName(),
                                        Safe.class.getPackage().getName()))
                                .fallback(Type.primitive(PrimitiveType.ANY))
                                .build()))
                        .build()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Unexpected com.palantir.logsafe.Safe marker in addition to a 'unsafe' tag");
    }

    @Test
    void testUnsafeMarkerAndTag() {
        assertThatThrownBy(() -> ConjureTags.safety(ArgumentDefinition.builder()
                        .from(tags("unsafe"))
                        .markers(Type.external(ExternalReference.builder()
                                .externalReference(TypeName.of(
                                        Unsafe.class.getSimpleName(),
                                        Unsafe.class.getPackage().getName()))
                                .fallback(Type.primitive(PrimitiveType.ANY))
                                .build()))
                        .build()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Unexpected com.palantir.logsafe.Unsafe marker in addition to a 'unsafe' tag");
    }

    @Test
    void testMarkerAndTypeSafetyDisagreement() {
        SafetyEvaluator safetyEvaluator = new SafetyEvaluator(ImmutableMap.of());
        ArgumentDefinition argument = ArgumentDefinition.builder()
                .argName(ArgumentName.of("name"))
                .markers(Type.external(ExternalReference.builder()
                        .externalReference(TypeName.of(
                                Unsafe.class.getSimpleName(),
                                Unsafe.class.getPackage().getName()))
                        .fallback(Type.primitive(PrimitiveType.ANY))
                        .build()))
                .type(Type.primitive(PrimitiveType.BEARERTOKEN))
                .paramType(ParameterType.body(BodyParameterType.of()))
                .build();
        assertThatThrownBy(() -> ConjureTags.validateArgument(argument, safetyEvaluator))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Declared argument safety is incompatible with the provided type");
    }

    @Test
    void testTagAndTypeSafetyDisagreement() {
        SafetyEvaluator safetyEvaluator = new SafetyEvaluator(ImmutableMap.of());
        ArgumentDefinition argument = ArgumentDefinition.builder()
                .argName(ArgumentName.of("name"))
                .tags("safe")
                .type(Type.primitive(PrimitiveType.BEARERTOKEN))
                .paramType(ParameterType.body(BodyParameterType.of()))
                .build();
        assertThatThrownBy(() -> ConjureTags.validateArgument(argument, safetyEvaluator))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Declared argument safety is incompatible with the provided type");
    }

    private static ArgumentDefinition tags(String... tags) {
        return ArgumentDefinition.builder()
                .argName(ArgumentName.of("name"))
                .tags(Arrays.asList(tags))
                .type(Type.primitive(PrimitiveType.STRING))
                .paramType(ParameterType.body(BodyParameterType.of()))
                .build();
    }
}
