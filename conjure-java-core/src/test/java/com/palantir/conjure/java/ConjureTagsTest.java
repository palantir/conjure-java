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

import com.palantir.conjure.spec.ArgumentDefinition;
import com.palantir.conjure.spec.ArgumentName;
import com.palantir.conjure.spec.BodyParameterType;
import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.ParameterType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeName;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.Unsafe;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import com.squareup.javapoet.ClassName;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class ConjureTagsTest {

    @Test
    void testEmpty() {
        assertThat(ConjureTags.safetyAnnotations(tags())).isEmpty();
    }

    @Test
    void testUnknown() {
        assertThat(ConjureTags.safetyAnnotations(tags("unknown tag"))).isEmpty();
    }

    @Test
    void testSafe() {
        assertThat(ConjureTags.safetyAnnotations(tags("safe")))
                .hasSize(1)
                .allSatisfy(annotationSpec -> assertThat(annotationSpec.type).isEqualTo(ClassName.get(Safe.class)));
    }

    @Test
    void testUnsafe() {
        assertThat(ConjureTags.safetyAnnotations(tags("unsafe")))
                .hasSize(1)
                .allSatisfy(annotationSpec -> assertThat(annotationSpec.type).isEqualTo(ClassName.get(Unsafe.class)));
    }

    @Test
    void testSafeAndUnsafe() {
        assertThatThrownBy(() -> ConjureTags.safetyAnnotations(tags("safe", "unsafe")))
                .isInstanceOf(SafeIllegalStateException.class)
                .hasMessageContaining("Tags cannot include both safe and unsafe");
    }

    @Test
    void testUnexpectedCase() {
        assertThatThrownBy(() -> ConjureTags.safetyAnnotations(tags("Safe")))
                .isInstanceOf(SafeIllegalStateException.class)
                .hasMessageContaining("Unexpected capitalization");
    }

    @Test
    void testSafeMarkerAndTag() {
        assertThatThrownBy(() -> ConjureTags.safetyAnnotations(ArgumentDefinition.builder()
                        .from(tags("safe"))
                        .markers(Type.external(ExternalReference.builder()
                                .externalReference(TypeName.of(
                                        Safe.class.getSimpleName(),
                                        Safe.class.getPackage().getName()))
                                .fallback(Type.primitive(PrimitiveType.ANY))
                                .build()))
                        .build()))
                .isInstanceOf(SafeIllegalStateException.class)
                .hasMessageContaining("Unexpected com.palantir.logsafe.Safe marker in addition to a 'safe' tag");
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
