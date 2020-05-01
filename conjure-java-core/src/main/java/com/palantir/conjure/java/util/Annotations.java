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

package com.palantir.conjure.java.util;

import com.google.common.base.Preconditions;
import com.palantir.conjure.java.types.TypeMapper;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.visitor.TypeVisitor;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class Annotations {

    public static Set<AnnotationSpec> createMarkers(TypeMapper typeMapper, List<Type> markers) {
        Preconditions.checkArgument(
                markers.stream().allMatch(type -> type.accept(TypeVisitor.IS_REFERENCE)),
                "Markers must refer to reference types.");
        return markers.stream()
                .map(typeMapper::getClassName)
                .map(ClassName.class::cast)
                .map(AnnotationSpec::builder)
                .map(AnnotationSpec.Builder::build)
                .collect(Collectors.toSet());
    }

    private Annotations() {}
}
