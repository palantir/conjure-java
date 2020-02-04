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

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.spec.Documentation;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import java.util.Optional;

public final class ConjureAnnotations {

    public static AnnotationSpec getConjureGeneratedAnnotation(Class<?> clazz) {
        return AnnotationSpec.builder(ClassName.get("javax.annotation", "Generated"))
                .addMember("value", "$S", clazz.getCanonicalName())
                .build();
    }

    public static ImmutableList<AnnotationSpec> deprecation(Optional<Documentation> deprecation) {
        return deprecation.isPresent()
                ? ImmutableList.of(AnnotationSpec.builder(Deprecated.class).build())
                : ImmutableList.of();
    }

    private ConjureAnnotations() {}
}
