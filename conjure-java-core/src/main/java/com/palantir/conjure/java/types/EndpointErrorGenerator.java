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
import com.palantir.conjure.java.api.errors.EndpointServiceException;
import com.palantir.conjure.java.util.ErrorGenerationUtils;
import com.palantir.conjure.java.util.ErrorGenerationUtils.DeclaredEndpointErrors;
import com.palantir.conjure.java.util.JavaNameSanitizer;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.java.util.TypeFunctions;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.ErrorDefinition;
import com.palantir.conjure.spec.ErrorNamespace;
import com.palantir.conjure.spec.ErrorTypeName;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.lang.model.element.Modifier;

public final class EndpointErrorGenerator implements Generator {

    private final Options options;

    public EndpointErrorGenerator(Options options) {
        this.options = options;
    }

    @Override
    public Stream<JavaFile> generate(ConjureDefinition definition) {
        DeclaredEndpointErrors endpointErrors = DeclaredEndpointErrors.from(definition);
        if (!options.dangerousDoNotUseEnableEndpointAssociatedErrors()
                && !endpointErrors.errors().isEmpty()) {
            List<String> errorNames =
                    endpointErrors.errors().stream().map(ErrorTypeName::getName).toList();
            throw new SafeIllegalStateException(
                    "Errors are associated with endpoints. This feature is currently not supported.",
                    SafeArg.of("errors", errorNames));
        }
        Map<com.palantir.conjure.spec.TypeName, TypeDefinition> types = TypeFunctions.toTypesMap(definition);
        SafetyEvaluator safetyEvaluator = new SafetyEvaluator(types);
        TypeMapper typeMapper = new TypeMapper(types, options);
        return ErrorGenerationUtils.getNamespacedErrorsFromDefinitions(definition.getErrors()).stream()
                .flatMap(namespacedErrors -> {
                    List<ErrorDefinition> filteredErrorDefinitions = namespacedErrors.errors().stream()
                            .filter(endpointErrors::contains)
                            .toList();
                    if (filteredErrorDefinitions.isEmpty()) {
                        return Stream.empty();
                    }
                    return Stream.of(generateErrorExceptionsForNamespace(
                            typeMapper,
                            safetyEvaluator,
                            Packages.getPrefixedPackage(namespacedErrors.javaPackage(), options.packagePrefix()),
                            namespacedErrors.namespace(),
                            filteredErrorDefinitions));
                });
    }

    private JavaFile generateErrorExceptionsForNamespace(
            TypeMapper typeMapper,
            SafetyEvaluator safetyEvaluator,
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
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(
                        ClassName.get(conjurePackage, ErrorGenerationUtils.serverErrorsClassName(namespace)))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(ErrorGenerationUtils.privateConstructor())
                .addMethods(constructors)
                .addMethods(generateConditionalExceptionFactories(
                        typeMapper, safetyEvaluator, errorDefinitions, conjurePackage, options))
                .addTypes(errorDefinitions.stream()
                        .map(def -> generateErrorException(typeMapper, conjurePackage, namespace, def))
                        .toList())
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(EndpointErrorGenerator.class));
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
                ErrorGenerationUtils.serverErrorsClassName(errorDefinition.getNamespace()),
                errorDefinition.getErrorName().getName());

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(exceptionClass);

        methodBuilder.addCode("return new $T(", exceptionClass);

        List<CodeBlock> args = new ArrayList<>(
                Stream.concat(errorDefinition.getSafeArgs().stream(), errorDefinition.getUnsafeArgs().stream())
                        .map(arg -> CodeBlock.of(
                                "$N",
                                JavaNameSanitizer.sanitizeErrorParameterName(
                                        arg.getFieldName().get())))
                        .toList());
        if (withCause) {
            args.add(CodeBlock.of("$N", "cause"));
        } else {
            args.add(CodeBlock.of("$N", "null"));
        }
        methodBuilder.addCode("$L", args.stream().collect(CodeBlock.joining(",")));

        ErrorGenerationUtils.addAllParametersWithSafetyAnnotationsToMethodBuilder(
                typeMapper, methodBuilder, errorDefinition);
        if (withCause) {
            ParameterSpec causeParameter = ParameterSpec.builder(Throwable.class, "cause")
                    .addAnnotation(Nullable.class)
                    .build();
            methodBuilder.addParameter(causeParameter);
        }

        methodBuilder.addCode(");");

        return methodBuilder.build();
    }

    private static List<MethodSpec> generateConditionalExceptionFactories(
            TypeMapper typeMapper,
            SafetyEvaluator safetyEvaluator,
            List<ErrorDefinition> errorDefinitions,
            String conjurePackage,
            Options options) {
        return errorDefinitions.stream()
                .map(errorDefinition -> {
                    ClassName exceptionClassName = ClassName.get(
                            conjurePackage,
                            ErrorGenerationUtils.serverErrorsClassName(errorDefinition.getNamespace()),
                            errorDefinition.getErrorName().getName());
                    return ErrorGenerationUtils.conditionalStaticFactoryMethodBuilder(
                                    typeMapper,
                                    safetyEvaluator,
                                    errorDefinition,
                                    options,
                                    exceptionClassName,
                                    Optional.empty())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private TypeSpec generateErrorException(
            TypeMapper typeMapper, String conjurePackage, ErrorNamespace namespace, ErrorDefinition errorDefinition) {
        return TypeSpec.classBuilder(errorDefinition.getErrorName().getName())
                .superclass(EndpointServiceException.class)
                .addMethod(buildExceptionConstructor(typeMapper, conjurePackage, namespace, errorDefinition))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .build();
    }

    private MethodSpec buildExceptionConstructor(
            TypeMapper typeMapper, String conjurePackage, ErrorNamespace namespace, ErrorDefinition errorDefinition) {
        MethodSpec.Builder methodBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addCode(
                        "super($T.$L",
                        ErrorGenerator.errorTypesClassName(conjurePackage, namespace),
                        CaseFormat.UPPER_CAMEL.to(
                                CaseFormat.UPPER_UNDERSCORE,
                                errorDefinition.getErrorName().getName()));
        methodBuilder.addCode(", cause");
        ErrorGenerationUtils.addAllLogSafeArgumentsToMethodBuilder(typeMapper, errorDefinition, methodBuilder);
        methodBuilder.addParameter(ParameterSpec.builder(Throwable.class, "cause")
                .addAnnotation(Nullable.class)
                .build());
        methodBuilder.addCode(");");
        return methodBuilder.build();
    }
}
