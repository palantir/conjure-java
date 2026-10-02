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

package com.palantir.conjure.java.codegen.lib;

import static org.assertj.core.api.Assertions.assertThat;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.ParameterizedTypeName;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;

final class TypeMarkersTest {

    private static final ClassName TYPE_MARKER = ClassName.get("com.example", "TypeMarker");

    @Test
    void simpleTypeUsesOfFactory() {
        assertThat(TypeMarkers.typeMarker(TYPE_MARKER, ClassName.get(String.class))
                        .toString())
                .isEqualTo("com.example.TypeMarker.of(java.lang.String.class)");
    }

    @Test
    void listUsesListOfFactory() {
        ParameterizedTypeName type = ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(String.class));
        assertThat(TypeMarkers.typeMarker(TYPE_MARKER, type).toString())
                .isEqualTo("com.example.TypeMarker.listOf(java.lang.String.class)");
    }

    @Test
    void setUsesSetOfFactory() {
        ParameterizedTypeName type = ParameterizedTypeName.get(ClassName.get(Set.class), ClassName.get(String.class));
        assertThat(TypeMarkers.typeMarker(TYPE_MARKER, type).toString())
                .isEqualTo("com.example.TypeMarker.setOf(java.lang.String.class)");
    }

    @Test
    void optionalUsesOptionalOfFactory() {
        ParameterizedTypeName type =
                ParameterizedTypeName.get(ClassName.get(Optional.class), ClassName.get(String.class));
        assertThat(TypeMarkers.typeMarker(TYPE_MARKER, type).toString())
                .isEqualTo("com.example.TypeMarker.optionalOf(java.lang.String.class)");
    }

    @Test
    void mapUsesMapOfFactory() {
        ParameterizedTypeName type = ParameterizedTypeName.get(
                ClassName.get(Map.class), ClassName.get(String.class), ClassName.get(Integer.class));
        assertThat(TypeMarkers.typeMarker(TYPE_MARKER, type).toString())
                .isEqualTo("com.example.TypeMarker.mapOf(java.lang.String.class, java.lang.Integer.class)");
    }

    @Test
    void nestedListUsesAnonymousClass() {
        ParameterizedTypeName type = ParameterizedTypeName.get(
                ClassName.get(List.class), ParameterizedTypeName.get(Optional.class, String.class));
        assertThat(TypeMarkers.typeMarker(TYPE_MARKER, type).toString())
                .isEqualTo("new com.example.TypeMarker<java.util.List<java.util.Optional<java.lang.String>>>() {}");
    }

    @Test
    void nestedMapValueUsesAnonymousClass() {
        ParameterizedTypeName type = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(List.class, String.class));
        assertThat(TypeMarkers.typeMarker(TYPE_MARKER, type).toString())
                .isEqualTo("new com.example.TypeMarker<java.util.Map<java.lang.String,"
                        + " java.util.List<java.lang.String>>>() {}");
    }

    @Test
    void unknownParameterizedTypeUsesAnonymousClass() {
        ParameterizedTypeName type =
                ParameterizedTypeName.get(ClassName.get("com.example", "Custom"), ClassName.get(String.class));
        assertThat(TypeMarkers.typeMarker(TYPE_MARKER, type).toString())
                .isEqualTo("new com.example.TypeMarker<com.example.Custom<java.lang.String>>() {}");
    }
}
