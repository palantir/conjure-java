/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.services.dialogue;

import static java.util.stream.Collectors.toList;

import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.services.ServiceGenerator;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.spec.Type;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.Optional;
import java.util.function.Function;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

public final class InterfaceGenerator {

    private final ServiceDefinition def;
    private final ParameterTypeMapper parameterTypes;
    private final ReturnTypeMapper returnTypes;

    public InterfaceGenerator(ServiceDefinition def, ParameterTypeMapper parameterTypes, ReturnTypeMapper returnTypes) {
        this.def = def;
        this.parameterTypes = parameterTypes;
        this.returnTypes = returnTypes;
    }

    public JavaFile generateBlocking() {
        return generate(Names.blockingClassName(def), returnTypes::baseType);
    }

    public JavaFile generateAsync() {
        return generate(Names.asyncClassName(def), returnTypes::async);
    }

    private JavaFile generate(ClassName className, Function<Optional<Type>, TypeName> returnTypeMapper) {
        TypeSpec.Builder serviceBuilder = TypeSpec.interfaceBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(DialogueServiceGenerator.class));

        def.getDocs().ifPresent(docs -> serviceBuilder.addJavadoc("$L", StringUtils.appendIfMissing(docs.get(), "\n")));

        serviceBuilder.addMethods(def.getEndpoints().stream()
                .map(endpoint -> apiMethod(endpoint, returnTypeMapper))
                .collect(toList()));

        return JavaFile.builder(def.getServiceName().getPackage(), serviceBuilder.build())
                .build();
    }

    private MethodSpec apiMethod(EndpointDefinition endpointDef, Function<Optional<Type>, TypeName> returnTypeMapper) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(
                        endpointDef.getEndpointName().get())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameters(parameterTypes.methodParams(endpointDef));

        endpointDef.getDeprecated().ifPresent(deprecatedDocsValue -> methodBuilder.addAnnotation(Deprecated.class));
        ServiceGenerator.getJavaDoc(endpointDef).ifPresent(content -> methodBuilder.addJavadoc("$L", content));

        methodBuilder.returns(returnTypeMapper.apply(endpointDef.getReturns()));

        return methodBuilder.build();
    }
}
