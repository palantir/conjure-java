/*
 * (c) Copyright 2024 Palantir Technologies Inc. All rights reserved.
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

import com.google.common.base.CaseFormat;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.EndpointError;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;
import java.util.List;
import javax.lang.model.element.Modifier;

public final class DialogueEndpointResultTypeGenerator {
    private final Options options;
    private final ReturnTypeMapper returnTypeMapper;

    public DialogueEndpointResultTypeGenerator(Options options, ReturnTypeMapper returnTypeMapper) {
        this.options = options;
        this.returnTypeMapper = returnTypeMapper;
    }

    public JavaFile generateEndpointResultTypes(ServiceDefinition serviceDefinition) {
        String packageName =
                Packages.getPrefixedPackage(serviceDefinition.getServiceName().getPackage(), options.packagePrefix());
        ClassName resultsClassName = Names.resultTypesClassName(serviceDefinition, options);
        TypeSpec resultTypesClass = TypeSpec.classBuilder(resultsClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(
                        ConjureAnnotations.getConjureGeneratedAnnotation(DialogueEndpointResultTypeGenerator.class))
                .addTypes(createResultTypes(serviceDefinition, packageName, resultsClassName))
                .build();

        return JavaFile.builder(packageName, resultTypesClass).build();
    }

    private List<TypeSpec> createResultTypes(
            ServiceDefinition serviceDefinition, String packageName, ClassName resultsClassName) {
        return serviceDefinition.getEndpoints().stream()
                .filter(endpoint -> !endpoint.getErrors().isEmpty())
                .map(pt -> createResultTypesForEndpoint(pt, packageName, resultsClassName))
                .toList();
    }

    private TypeSpec createResultTypesForEndpoint(
            EndpointDefinition endpointDefinition, String packageName, ClassName resultsClassName) {
        String endpointResultName = getEndpointResultName(endpointDefinition);
        TypeSpec.Builder typeBuilder = TypeSpec.interfaceBuilder(endpointResultName)
                .addModifiers(Modifier.PUBLIC, Modifier.SEALED)
                .addType(createSuccessRecord(endpointDefinition, packageName, resultsClassName))
                .addPermittedSubclass(
                        ClassName.get(packageName, resultsClassName.simpleName(), endpointResultName, "Success"))
                .addTypes(endpointDefinition.getErrors().stream()
                        .map(error -> createErrorRecord(error, endpointDefinition, packageName, resultsClassName))
                        .toList())
                .addPermittedSubclasses(endpointDefinition.getErrors().stream()
                        .map(error -> ClassName.get(
                                packageName,
                                resultsClassName.simpleName(),
                                getEndpointResultName(endpointDefinition),
                                getErrorName(error)))
                        .toList());
        // TODO(p): add permitted subclasses for all errors.

        return typeBuilder.build();
    }

    private TypeSpec createSuccessRecord(
            EndpointDefinition endpointDefinition, String packageName, ClassName resultsClassName) {
        TypeSpec.Builder builder = TypeSpec.recordBuilder("Success")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addSuperinterface(ClassName.get(
                        packageName, resultsClassName.simpleName(), getEndpointResultName(endpointDefinition)));
        if (endpointDefinition.getReturns().isPresent()) {
            TypeName returnType =
                    returnTypeMapper.baseType(endpointDefinition.getReturns().get());
            builder.recordConstructor(MethodSpec.constructorBuilder()
                    .addParameter(ParameterSpec.builder(returnType, "value").build())
                    .build());
        }
        return builder.build();
    }

    private TypeSpec createErrorRecord(
            EndpointError endpointError,
            EndpointDefinition endpointDefinition,
            String packageName,
            ClassName resultsClassName) {
        TypeSpec.Builder builder = TypeSpec.recordBuilder(getErrorName(endpointError))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addSuperinterface(ClassName.get(
                        packageName, resultsClassName.simpleName(), getEndpointResultName(endpointDefinition)));

        return builder.build();
    }

    private static String getErrorName(EndpointError endpointError) {
        return String.format(
                "%s_%s",
                endpointError.getError().getNamespace(),
                endpointError.getError().getName());
    }

    private static String getEndpointResultName(EndpointDefinition definition) {
        return CaseFormat.LOWER_CAMEL.to(
                        CaseFormat.UPPER_CAMEL, definition.getEndpointName().get()) + "Result";
    }
}
