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
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.spec.TypeDefinition;
import com.squareup.javapoet.JavaFile;
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
                .flatMap(serviceDef -> generateService(
                        serviceDef,
                        conjureDefinition.getTypes(),
                        new TypeMapper(
                                conjureDefinition.getTypes(),
                                new UndertowRequestBodyClassNameVisitor(
                                        conjureDefinition.getTypes(), experimentalFeatures)),
                        new TypeMapper(
                                conjureDefinition.getTypes(),
                                new UndertowReturnValueClassNameVisitor(
                                        conjureDefinition.getTypes(), experimentalFeatures)))
                        .stream())
                .collect(Collectors.toSet());
    }

    private List<JavaFile> generateService(
            ServiceDefinition serviceDefinition,
            List<TypeDefinition> typeDefinitions,
            TypeMapper typeMapper,
            TypeMapper returnTypeMapper) {
        return ImmutableList.of(
                new UndertowServiceInterfaceGenerator(experimentalFeatures)
                        .generateServiceInterface(serviceDefinition, typeMapper, returnTypeMapper),
                new UndertowServiceHandlerGenerator(experimentalFeatures)
                        .generateServiceHandler(serviceDefinition, typeDefinitions, typeMapper, returnTypeMapper));
    }
}
