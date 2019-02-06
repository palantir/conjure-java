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

package com.palantir.conjure.java.services;

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.FeatureFlags;
import com.palantir.conjure.java.types.TypeMapper;
import com.palantir.conjure.spec.ArgumentDefinition;
import com.palantir.conjure.spec.BodyParameterType;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.HeaderParameterType;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.ParameterType;
import com.palantir.conjure.spec.PathParameterType;
import com.palantir.conjure.spec.QueryParameterType;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.visitor.TypeVisitor;
import com.squareup.javapoet.JavaFile;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class UndertowServiceGenerator implements ServiceGenerator {

    private final Set<FeatureFlags> experimentalFeatures;

    public UndertowServiceGenerator(Set<FeatureFlags> experimentalFeatures) {
        this.experimentalFeatures = experimentalFeatures;
    }

    @Override
    public Set<JavaFile> generate(ConjureDefinition conjureDefinition) {
        return conjureDefinition.getServices().stream()
                .flatMap(serviceDef -> generateService(serviceDef, conjureDefinition.getTypes(),
                        new TypeMapper(
                                conjureDefinition.getTypes(),
                                new UndertowRequestBodyClassNameVisitor(
                                        conjureDefinition.getTypes(), experimentalFeatures)),
                        new TypeMapper(
                                conjureDefinition.getTypes(),
                                new UndertowReturnValueClassNameVisitor(
                                        conjureDefinition.getTypes(), experimentalFeatures)))
                        .stream()).collect(Collectors.toSet());
    }

    private List<JavaFile> generateService(ServiceDefinition serviceDefinition, List<TypeDefinition> typeDefinitions,
            TypeMapper typeMapper, TypeMapper returnTypeMapper) {
        return ImmutableList.of(
                new UndertowServiceInterfaceGenerator(experimentalFeatures)
                        .generateServiceInterface(serviceDefinition, typeMapper, returnTypeMapper),
                new UndertowServiceHandlerGenerator(experimentalFeatures)
                        .generateServiceHandler(serviceDefinition, typeDefinitions, typeMapper, returnTypeMapper)
        );
    }

    static List<ArgumentDefinition> sortArgumentDefinitions(List<ArgumentDefinition> in) {
        List<ArgumentDefinition> sortedArgList = new ArrayList<>(in);
        sortedArgList.sort(Comparator.comparing(o ->
                o.getParamType().accept(PARAM_SORT_ORDER) + o.getType().accept(TYPE_SORT_ORDER)));
        return sortedArgList;
    }

    /** Produces an ordering for ParameterType of Header, Path, Query, Body. */
    private static final ParameterType.Visitor<Integer> PARAM_SORT_ORDER = new ParameterType.Visitor<Integer>() {
        @Override
        public Integer visitBody(BodyParameterType value) {
            return 30;
        }

        @Override
        public Integer visitHeader(HeaderParameterType value) {
            return 0;
        }

        @Override
        public Integer visitPath(PathParameterType value) {
            return 10;
        }

        @Override
        public Integer visitQuery(QueryParameterType value) {
            return 20;
        }

        @Override
        public Integer visitUnknown(String unknownType) {
            return -1;
        }
    };

    /**
     * Produces a type sort ordering for use with {@link #PARAM_SORT_ORDER} such that types with known defaults come
     * after types without known defaults.
     */
    private static final Type.Visitor<Integer> TYPE_SORT_ORDER = new TypeVisitor.Default<Integer>() {
        @Override
        public Integer visitOptional(OptionalType value) {
            return 1;
        }

        @Override
        public Integer visitList(ListType value) {
            return 1;
        }

        @Override
        public Integer visitSet(SetType value) {
            return 1;
        }

        @Override
        public Integer visitMap(MapType value) {
            return 1;
        }

        @Override
        public Integer visitDefault() {
            return 0;
        }
    };
}
