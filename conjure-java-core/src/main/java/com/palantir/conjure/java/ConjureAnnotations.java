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

package com.palantir.conjure.java;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.lib.internal.Incubating;
import com.palantir.conjure.spec.Documentation;
import com.palantir.conjure.spec.EndpointDefinition;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import java.util.Optional;

public final class ConjureAnnotations {

    private static final ImmutableList<AnnotationSpec> DEPRECATED =
            ImmutableList.of(AnnotationSpec.builder(Deprecated.class).build());
    private static final ImmutableList<AnnotationSpec> INCUBATING =
            ImmutableList.of(AnnotationSpec.builder(Incubating.class).build());
    private static final AnnotationSpec DELEGATING_JSON_CREATOR = AnnotationSpec.builder(JsonCreator.class)
            .addMember("mode", "$T.DELEGATING", JsonCreator.Mode.class)
            .build();
    private static final AnnotationSpec PROPERTIES_JSON_CREATOR = AnnotationSpec.builder(JsonCreator.class)
            .addMember("mode", "$T.PROPERTIES", JsonCreator.Mode.class)
            .build();

    public static AnnotationSpec getConjureGeneratedAnnotation(Class<?> clazz) {
        return AnnotationSpec.builder(ClassName.get("javax.annotation", "Generated"))
                .addMember("value", "$S", clazz.getCanonicalName())
                .build();
    }

    public static ImmutableList<AnnotationSpec> deprecation(Optional<Documentation> deprecation) {
        return deprecation.isPresent() ? DEPRECATED : ImmutableList.of();
    }

    public static ImmutableList<AnnotationSpec> incubating(EndpointDefinition definition) {
        if (definition.getTags().contains("incubating")) {
            return INCUBATING;
        }
        return ImmutableList.of();
    }

    public static AnnotationSpec delegatingJsonCreator() {
        return DELEGATING_JSON_CREATOR;
    }

    public static AnnotationSpec propertiesJsonCreator() {
        return PROPERTIES_JSON_CREATOR;
    }

    private ConjureAnnotations() {}
}
