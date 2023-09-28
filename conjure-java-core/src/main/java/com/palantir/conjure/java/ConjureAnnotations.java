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
import com.google.common.collect.Iterables;
import com.palantir.conjure.java.lib.internal.ClientEndpoint;
import com.palantir.conjure.java.lib.internal.Incubating;
import com.palantir.conjure.spec.Documentation;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.LogSafety;
import com.palantir.logsafe.DoNotLog;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.Unsafe;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import java.util.Optional;

public final class ConjureAnnotations {

    private static final ImmutableList<AnnotationSpec> DEPRECATED =
            ImmutableList.of(AnnotationSpec.builder(Deprecated.class).build());
    private static final ImmutableList<AnnotationSpec> OVERRIDE_LIST =
            ImmutableList.of(AnnotationSpec.builder(Override.class).build());
    private static final AnnotationSpec INCUBATING_SPEC =
            AnnotationSpec.builder(Incubating.class).build();
    private static final ImmutableList<AnnotationSpec> INCUBATING = ImmutableList.of(INCUBATING_SPEC);
    private static final AnnotationSpec DELEGATING_JSON_CREATOR = AnnotationSpec.builder(JsonCreator.class)
            .addMember("mode", "$T.DELEGATING", JsonCreator.Mode.class)
            .build();
    private static final AnnotationSpec PROPERTIES_JSON_CREATOR = AnnotationSpec.builder(JsonCreator.class)
            .addMember("mode", "$T.PROPERTIES", JsonCreator.Mode.class)
            .build();

    public static AnnotationSpec getConjureGeneratedAnnotation(Class<?> clazz) {
        return AnnotationSpec.builder(ClassName.get("javax.annotation.processing", "Generated"))
                .addMember("value", "$S", clazz.getCanonicalName())
                .build();
    }

    public static ImmutableList<AnnotationSpec> deprecation(Optional<Documentation> deprecation) {
        return deprecation.isPresent() ? DEPRECATED : ImmutableList.of();
    }

    public static ImmutableList<AnnotationSpec> override(boolean override) {
        return override ? OVERRIDE_LIST : ImmutableList.of();
    }

    public static ImmutableList<AnnotationSpec> getClientEndpointAnnotations(EndpointDefinition definition) {
        ImmutableList.Builder<AnnotationSpec> result = ImmutableList.builder();
        if (definition.getTags().contains("incubating")) {
            result.add(INCUBATING_SPEC);
        }
        result.add(clientEndpoint(definition));
        return result.build();
    }

    private static AnnotationSpec clientEndpoint(EndpointDefinition definition) {
        return AnnotationSpec.builder(ClientEndpoint.class)
                .addMember("method", "$S", definition.getHttpMethod().get())
                .addMember("path", "$S", definition.getHttpPath().get())
                .build();
    }

    public static ImmutableList<AnnotationSpec> safety(Optional<LogSafety> value) {
        return value.map(safety -> {
                    switch (safety.get()) {
                        case SAFE:
                            return Safe.class;
                        case UNSAFE:
                            return Unsafe.class;
                        case DO_NOT_LOG:
                            return DoNotLog.class;
                        case UNKNOWN:
                            // fall through
                        default:
                            throw new IllegalStateException("Unknown safety: " + safety);
                    }
                })
                .map(clazz -> ImmutableList.of(AnnotationSpec.builder(clazz).build()))
                .orElseGet(ImmutableList::of);
    }

    public static TypeName withSafety(TypeName typeName, Optional<LogSafety> maybeSafety) {
        if (maybeSafety.isPresent()) {
            if (typeName instanceof ParameterizedTypeName) {
                ParameterizedTypeName param = (ParameterizedTypeName) typeName;
                // Handle List/Set/Optional wrappers, however Map has not been implemented yet.
                if (param.typeArguments.size() == 1) {
                    TypeName typeArgument = Iterables.getOnlyElement(param.typeArguments);
                    if (typeArgument instanceof ClassName) {
                        ImmutableList<AnnotationSpec> annotations = safety(maybeSafety);
                        return ParameterizedTypeName.get(param.rawType, typeArgument.annotated(annotations));
                    } else if (typeArgument instanceof ParameterizedTypeName) {
                        return ParameterizedTypeName.get(param.rawType, withSafety(typeArgument, maybeSafety));
                    }
                }
            }
            return typeName.annotated(safety(maybeSafety));
        }
        return typeName;
    }

    public static AnnotationSpec delegatingJsonCreator() {
        return DELEGATING_JSON_CREATOR;
    }

    public static AnnotationSpec propertiesJsonCreator() {
        return PROPERTIES_JSON_CREATOR;
    }

    private ConjureAnnotations() {}
}
