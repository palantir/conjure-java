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

import com.palantir.conjure.java.Generator;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.types.ClassNameVisitor;
import com.palantir.conjure.java.types.DefaultClassNameVisitor;
import com.palantir.conjure.java.types.SpecializeBinaryClassNameVisitor;
import com.palantir.conjure.java.types.TypeMapper;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.util.TypeFunctions;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.TypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import java.io.InputStream;
import java.util.Map;
import java.util.stream.Stream;

public final class UndertowServiceGenerator implements Generator {

    private final Options options;

    public UndertowServiceGenerator(Options options) {
        this.options = options;
    }

    @Override
    public Stream<JavaFile> generate(ConjureDefinition conjureDefinition) {
        Map<TypeName, TypeDefinition> types = TypeFunctions.toTypesMap(conjureDefinition);
        ClassNameVisitor defaultVisitor = new DefaultClassNameVisitor(types.keySet(), options);
        TypeMapper typeMapper = new TypeMapper(
                types, new SpecializeBinaryClassNameVisitor(defaultVisitor, types, ClassName.get(InputStream.class)));
        TypeMapper returnTypeMapper = new TypeMapper(
                types,
                new SpecializeBinaryClassNameVisitor(defaultVisitor, types, ClassName.get(BinaryResponseBody.class)));

        UndertowServiceInterfaceGenerator interfaceGenerator = new UndertowServiceInterfaceGenerator(options);
        UndertowServiceHandlerGenerator handlerGenerator = new UndertowServiceHandlerGenerator(options);

        return conjureDefinition.getServices().stream()
                .flatMap(serviceDef -> Stream.of(
                        interfaceGenerator.generateServiceInterface(serviceDef, typeMapper, returnTypeMapper),
                        handlerGenerator.generateServiceHandler(serviceDef, types, typeMapper, returnTypeMapper)));
    }
}
