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
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.types.ClassNameVisitor;
import com.palantir.conjure.java.types.DefaultClassNameVisitor;
import com.palantir.conjure.java.types.SpecializeBinaryClassNameVisitor;
import com.palantir.conjure.java.types.TypeMapper;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.util.TypeFunctions;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.TypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class UndertowServiceGenerator extends ServiceGenerator {

    private final Options options;

    public UndertowServiceGenerator(Options options) {
        this.options = options;
    }

    @Override
    public List<JavaFile> generate(ConjureDefinition conjureDefinition) {
        Map<TypeName, TypeDefinition> types = TypeFunctions.toTypesMap(conjureDefinition);
        ClassNameVisitor defaultVisitor = new DefaultClassNameVisitor(types.keySet(), options);
        ClassNameVisitor argumentVisitor =
                new SpecializeBinaryClassNameVisitor(defaultVisitor, types, ClassName.get(InputStream.class));
        ClassNameVisitor returnVisitor =
                new SpecializeBinaryClassNameVisitor(defaultVisitor, types, ClassName.get(BinaryResponseBody.class));
        return conjureDefinition.getServices().stream()
                .flatMap(serviceDef -> generateService(
                        serviceDef, types, new TypeMapper(types, argumentVisitor), new TypeMapper(types, returnVisitor))
                        .stream())
                .collect(Collectors.toList());
    }

    private List<JavaFile> generateService(
            ServiceDefinition serviceDefinition,
            Map<TypeName, TypeDefinition> typeDefinitions,
            TypeMapper typeMapper,
            TypeMapper returnTypeMapper) {
        return ImmutableList.of(
                new UndertowServiceInterfaceGenerator(options)
                        .generateServiceInterface(serviceDefinition, typeMapper, returnTypeMapper),
                new UndertowServiceHandlerGenerator(options)
                        .generateServiceHandler(serviceDefinition, typeDefinitions, typeMapper, returnTypeMapper));
    }
}
