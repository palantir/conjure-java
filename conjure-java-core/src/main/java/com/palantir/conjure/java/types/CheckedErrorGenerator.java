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

package com.palantir.conjure.java.types;

import com.google.common.base.CaseFormat;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.Generator;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.undertow.lib.CheckedServiceException;
import com.palantir.conjure.java.util.ErrorGenerationUtils;
import com.palantir.conjure.java.util.ErrorGenerationUtils.DeclaredEndpointErrors;
import com.palantir.conjure.java.util.ErrorGenerationUtils.ErrorDefinitionsByPackageAndNamespace;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.java.util.TypeFunctions;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.ErrorDefinition;
import com.palantir.conjure.spec.ErrorNamespace;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.lang.model.element.Modifier;

public final class CheckedErrorGenerator implements Generator {

    private final Options options;

    public CheckedErrorGenerator(Options options) {
        this.options = options;
    }

    @Override
    public Stream<JavaFile> generate(ConjureDefinition definition) {
        Map<com.palantir.conjure.spec.TypeName, TypeDefinition> types = TypeFunctions.toTypesMap(definition);
        TypeMapper typeMapper = new TypeMapper(types, options);
        DeclaredEndpointErrors endpointErrors = DeclaredEndpointErrors.from(definition);
        return ErrorDefinitionsByPackageAndNamespace.from(definition.getErrors())
                .processErrorDefinitions((pkg, namespace, errorDefinitions) -> {
                    List<ErrorDefinition> filteredErrorDefinitions = errorDefinitions.stream()
                            .filter(endpointErrors::contains)
                            .toList();
                    if (filteredErrorDefinitions.isEmpty()) {
                        return Stream.empty();
                    }
                    return Stream.of(generateErrorExceptionsForNamespace(
                            typeMapper,
                            Packages.getPrefixedPackage(pkg, options.packagePrefix()),
                            namespace,
                            filteredErrorDefinitions));
                });
    }

    private JavaFile generateErrorExceptionsForNamespace(
            TypeMapper typeMapper,
            String conjurePackage,
            ErrorNamespace namespace,
            List<ErrorDefinition> errorDefinitions) {
        List<MethodSpec> constructors = errorDefinitions.stream()
                .flatMap(entry -> {
                    MethodSpec withoutCause = generateExceptionFactory(typeMapper, entry, conjurePackage, false);
                    MethodSpec withCause = generateExceptionFactory(typeMapper, entry, conjurePackage, true);
                    return Stream.of(withoutCause, withCause);
                })
                .toList();

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(errorExceptionsClassName(conjurePackage, namespace))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(ErrorGenerationUtils.privateConstructor())
                .addMethods(constructors)
                .addTypes(errorDefinitions.stream()
                        .map(def -> generateErrorException(typeMapper, conjurePackage, namespace, def))
                        .toList())
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(CheckedErrorGenerator.class));
        return JavaFile.builder(conjurePackage, typeBuilder.build())
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }

    private static MethodSpec generateExceptionFactory(
            TypeMapper typeMapper, ErrorDefinition errorDefinition, String conjurePackage, boolean withCause) {
        String methodName = CaseFormat.UPPER_CAMEL.to(
                CaseFormat.LOWER_CAMEL, errorDefinition.getErrorName().getName());

        ClassName exceptionClass = ClassName.get(
                conjurePackage,
                "Server" + errorDefinition.getNamespace() + "Errors",
                errorDefinition.getErrorName().getName());

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(exceptionClass);

        methodBuilder.addCode("return new $T(", exceptionClass);

        boolean firstArg = true;

        if (withCause) {
            ErrorGenerationUtils.addNullableThrowableCauseParameterToMethodBuilder(methodBuilder);
            firstArg = false;
        }

        for (FieldDefinition arg : Stream.concat(
                        errorDefinition.getSafeArgs().stream(), errorDefinition.getUnsafeArgs().stream())
                .toList()) {
            if (!firstArg) {
                methodBuilder.addCode(", ");
            }
            firstArg = false;
            methodBuilder.addCode("$L", arg.getFieldName().get());
        }

        ErrorGenerationUtils.addAllParametersWithSafetyAnnotationsToMethodBuilder(
                typeMapper, methodBuilder, errorDefinition);

        methodBuilder.addCode(");");

        return methodBuilder.build();
    }

    private TypeSpec generateErrorException(
            TypeMapper typeMapper, String conjurePackage, ErrorNamespace namespace, ErrorDefinition errorDefinition) {
        return TypeSpec.classBuilder(errorDefinition.getErrorName().getName())
                .superclass(CheckedServiceException.class)
                .addMethod(buildExceptionConstructor(typeMapper, conjurePackage, namespace, errorDefinition, false))
                .addMethod(buildExceptionConstructor(typeMapper, conjurePackage, namespace, errorDefinition, true))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .build();
    }

    private MethodSpec buildExceptionConstructor(
            TypeMapper typeMapper,
            String conjurePackage,
            ErrorNamespace namespace,
            ErrorDefinition errorDefinition,
            boolean withCause) {
        MethodSpec.Builder methodBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addCode(
                        "super($L.$L",
                        ErrorGenerator.errorTypesClassName(conjurePackage, namespace),
                        CaseFormat.UPPER_CAMEL.to(
                                CaseFormat.UPPER_UNDERSCORE,
                                errorDefinition.getErrorName().getName()));
        if (withCause) {
            methodBuilder.addParameter(ParameterSpec.builder(Throwable.class, "cause")
                    .addAnnotation(Nullable.class)
                    .build());
            methodBuilder.addCode(", cause");
        }

        ErrorGenerationUtils.addAllLogSafeArgumentsToMethodBuilder(typeMapper, errorDefinition, methodBuilder);

        methodBuilder.addCode(");");
        return methodBuilder.build();
    }

    private static ClassName errorExceptionsClassName(String conjurePackage, ErrorNamespace namespace) {
        return ClassName.get(conjurePackage, "Server" + namespace.get() + "Errors");
    }
}
