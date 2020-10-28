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

import com.google.common.collect.ImmutableList;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.Unsafe;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import com.squareup.javapoet.ClassName;
import org.junit.jupiter.api.Test;

class ConjureTagsTest {

    @Test
    void testEmpy() {
        assertThat(ConjureTags.tagAnnotations(ImmutableList.of())).isEmpty();
    }

    @Test
    void testUnknown() {
        assertThat(ConjureTags.tagAnnotations(ImmutableList.of("unknown tag"))).isEmpty();
    }

    @Test
    void testSafe() {
        assertThat(ConjureTags.tagAnnotations(ImmutableList.of("safe")))
                .hasSize(1)
                .allSatisfy(annotationSpec -> assertThat(annotationSpec.type).isEqualTo(ClassName.get(Safe.class)));
    }

    @Test
    void testUnsafe() {
        assertThat(ConjureTags.tagAnnotations(ImmutableList.of("unsafe")))
                .hasSize(1)
                .allSatisfy(annotationSpec -> assertThat(annotationSpec.type).isEqualTo(ClassName.get(Unsafe.class)));
    }

    @Test
    void testSafeAndUnsafe() {
        assertThatThrownBy(() -> ConjureTags.tagAnnotations(ImmutableList.of("safe", "unsafe")))
                .isInstanceOf(SafeIllegalStateException.class)
                .hasMessageContaining("Tags cannot include both safe and unsafe");
    }

    @Test
    void testUnexpectedCase() {
        assertThatThrownBy(() -> ConjureTags.tagAnnotations(ImmutableList.of("Safe")))
                .isInstanceOf(SafeIllegalStateException.class)
                .hasMessageContaining("Unexpected capitalization");
    }
}
