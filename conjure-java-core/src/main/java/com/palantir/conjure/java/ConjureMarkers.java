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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.palantir.conjure.java.types.TypeMapper;
import com.palantir.conjure.spec.ArgumentDefinition;
import com.palantir.conjure.spec.LogSafety;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeName;
import com.palantir.conjure.visitor.TypeVisitor;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.Unsafe;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** The marker concept is deprecated in favor of tags, however the functionality has not been dropped yet. */
public final class ConjureMarkers {

    private static final ImmutableSet<ClassName> SAFETY_CLASS_NAMES =
            ImmutableSet.of(ClassName.get(Safe.class), ClassName.get(Unsafe.class));

    private static final TypeName SAFE_TYPE_NAME =
            TypeName.of(Safe.class.getSimpleName(), Safe.class.getPackage().getName());
    private static final TypeName UNSAFE_TYPE_NAME =
            TypeName.of(Unsafe.class.getSimpleName(), Unsafe.class.getPackage().getName());

    public static ImmutableList<AnnotationSpec> annotations(TypeMapper typeMapper, List<Type> markers) {
        Preconditions.checkArgument(
                markers.stream().allMatch(type -> type.accept(TypeVisitor.IS_REFERENCE)),
                "Markers must refer to reference types.");
        return markers.stream()
                .map(typeMapper::getClassName)
                .map(ClassName.class::cast)
                // safety is handled separately by markerSafety
                .filter(name -> !SAFETY_CLASS_NAMES.contains(name))
                .map(AnnotationSpec::builder)
                .map(AnnotationSpec.Builder::build)
                .collect(ImmutableList.toImmutableList());
    }

    public static Optional<LogSafety> markerSafety(ArgumentDefinition argument) {
        ImmutableList<LogSafety> markerSafety = argument.getMarkers().stream()
                .map(type -> type.accept(TypeVisitor.IS_REFERENCE) ? type.accept(TypeVisitor.REFERENCE) : null)
                .map(typeName -> {
                    if (SAFE_TYPE_NAME.equals(typeName)) {
                        return LogSafety.SAFE;
                    }
                    if (UNSAFE_TYPE_NAME.equals(typeName)) {
                        return LogSafety.UNSAFE;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(ImmutableList.toImmutableList());
        if (markerSafety.isEmpty()) {
            return Optional.empty();
        }
        if (markerSafety.size() > 1) {
            throw new SafeIllegalStateException(
                    "Markers may declare at most one safety value",
                    SafeArg.of("argName", argument.getArgName()),
                    SafeArg.of("markerSafety", markerSafety));
        }
        return Optional.of(Iterables.getOnlyElement(markerSafety));
    }

    private ConjureMarkers() {}
}
