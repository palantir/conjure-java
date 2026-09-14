/*
 * (c) Copyright 2026 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.undertow.lib;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

final class TypeMarkerTest {
    @Test
    void simpleTypeFactoryMatchesAnonymousClass() {
        TypeMarker<String> factory = TypeMarker.of(String.class);
        TypeMarker<String> anonymous = new TypeMarker<String>() {};

        assertThat(factory.getType()).isEqualTo(anonymous.getType());
        assertThat(factory).isEqualTo(anonymous);
        assertThat(anonymous).isEqualTo(factory);
        assertThat(factory.hashCode()).isEqualTo(anonymous.hashCode());
    }

    @Test
    void listFactoryMatchesAnonymousClass() {
        TypeMarker<List<String>> factory = TypeMarker.listOf(String.class);
        TypeMarker<List<String>> anonymous = new TypeMarker<List<String>>() {};

        assertThat(factory.getType()).isEqualTo(anonymous.getType());
        assertThat(anonymous.getType()).isEqualTo(factory.getType());
        assertThat(factory).isEqualTo(anonymous);
        assertThat(anonymous).isEqualTo(factory);
        assertThat(factory.hashCode()).isEqualTo(anonymous.hashCode());
        assertThat(factory.getType().getTypeName())
                .isEqualTo(anonymous.getType().getTypeName());
    }

    @Test
    void mapFactoryMatchesAnonymousClass() {
        TypeMarker<Map<String, Integer>> factory = TypeMarker.mapOf(String.class, Integer.class);
        TypeMarker<Map<String, Integer>> anonymous = new TypeMarker<Map<String, Integer>>() {};

        assertThat(factory.getType()).isEqualTo(anonymous.getType());
        assertThat(anonymous.getType()).isEqualTo(factory.getType());
        assertThat(factory).isEqualTo(anonymous);
        assertThat(anonymous).isEqualTo(factory);
        assertThat(factory.hashCode()).isEqualTo(anonymous.hashCode());
        assertThat(factory.getType().getTypeName())
                .isEqualTo(anonymous.getType().getTypeName());
    }

    @Test
    void differentTypesAreNotEqual() {
        assertThat(TypeMarker.of(String.class)).isNotEqualTo(TypeMarker.of(Integer.class));
        assertThat(TypeMarker.listOf(String.class)).isNotEqualTo(TypeMarker.listOf(Integer.class));
        assertThat(TypeMarker.listOf(String.class)).isNotEqualTo(TypeMarker.setOf(String.class));
        assertThat(TypeMarker.mapOf(String.class, Integer.class))
                .isNotEqualTo(TypeMarker.mapOf(Integer.class, String.class));
    }
}
