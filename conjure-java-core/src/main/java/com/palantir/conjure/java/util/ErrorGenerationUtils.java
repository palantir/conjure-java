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

package com.palantir.conjure.java.util;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.types.Expressions;
import com.palantir.conjure.java.types.SafetyEvaluator;
import com.palantir.conjure.java.types.TypeMapper;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.EndpointError;
import com.palantir.conjure.spec.ErrorDefinition;
import com.palantir.conjure.spec.ErrorNamespace;
import com.palantir.conjure.spec.ErrorTypeName;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.LogSafety;
import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeName;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.UnsafeArg;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

public final class ErrorGenerationUtils {
    public static MethodSpec privateConstructor() {
        return MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build();
    }

    public static String errorExceptionsClassName(ErrorNamespace namespace) {
        return "Server" + namespace.get() + "Errors";
    }

    public record DeclaredEndpointErrors(Set<ErrorTypeName> errors) {
        public static DeclaredEndpointErrors from(ConjureDefinition definition) {
            return new DeclaredEndpointErrors(definition.getServices().stream()
                    .flatMap(service -> service.getEndpoints().stream())
                    .flatMap(endpoint -> endpoint.getErrors().stream())
                    .map(EndpointError::getError)
                    .collect(Collectors.toSet()));
        }

        public boolean contains(ErrorDefinition errorDefinition) {
            com.palantir.conjure.spec.TypeName errorName = errorDefinition.getErrorName();
            ErrorTypeName errorTypeName = ErrorTypeName.builder()
                    .name(errorName.getName())
                    .package_(errorName.getPackage())
                    .namespace(errorDefinition.getNamespace())
                    .build();
            return errors.contains(errorTypeName);
        }
    }

    public record NamespacedErrors(String javaPackage, ErrorNamespace namespace, List<ErrorDefinition> errors) {}

    public static List<NamespacedErrors> getNamespacedErrorsFromDefinitions(List<ErrorDefinition> errorTypeNameToDef) {
        record ErrorGroup(String javaPackage, ErrorNamespace namespace) {}
        Map<ErrorGroup, List<ErrorDefinition>> errorsByGroup = new HashMap<>();
        errorTypeNameToDef.forEach(errorDef -> {
            ErrorGroup errorGroup = new ErrorGroup(errorDef.getErrorName().getPackage(), errorDef.getNamespace());
            errorsByGroup.computeIfAbsent(errorGroup, key -> new ArrayList<>()).add(errorDef);
        });
        return errorsByGroup.entrySet().stream()
                .map(entry -> {
                    ErrorGroup errorGroup = entry.getKey();
                    return new NamespacedErrors(errorGroup.javaPackage(), errorGroup.namespace(), entry.getValue());
                })
                .collect(ImmutableList.toImmutableList());
    }

    public static void addNullableThrowableCauseParameterToMethodBuilder(MethodSpec.Builder methodBuilder) {
        ParameterSpec causeParameter = ParameterSpec.builder(Throwable.class, "cause")
                .addAnnotation(Nullable.class)
                .build();
        methodBuilder.addParameter(causeParameter);
        methodBuilder.addCode("cause");
    }

    public static void addAllLogSafeArgumentsToMethodBuilder(
            TypeMapper typeMapper, ErrorDefinition errorDefinition, MethodSpec.Builder methodBuilder) {
        errorDefinition
                .getSafeArgs()
                .forEach(arg ->
                        ErrorGenerationUtils.addLogSafeArgumentToMethodBuilder(typeMapper, methodBuilder, arg, true));
        errorDefinition
                .getUnsafeArgs()
                .forEach(arg ->
                        ErrorGenerationUtils.addLogSafeArgumentToMethodBuilder(typeMapper, methodBuilder, arg, false));
    }

    private static void addLogSafeArgumentToMethodBuilder(
            TypeMapper typeMapper, MethodSpec.Builder methodBuilder, FieldDefinition argDefinition, boolean isSafe) {
        String argName = argDefinition.getFieldName().get();
        methodBuilder.addParameter(
                ErrorGenerationUtils.buildParameterWithSafetyAnnotation(typeMapper, argDefinition, isSafe));
        Class<?> clazz = isSafe ? SafeArg.class : UnsafeArg.class;
        methodBuilder.addCode(",\n    $T.of($S, $L)", clazz, argName, argName);
    }

    public static void addAllParametersWithSafetyAnnotationsToMethodBuilder(
            TypeMapper typeMapper, MethodSpec.Builder methodBuilder, ErrorDefinition errorDefinition) {
        errorDefinition
                .getSafeArgs()
                .forEach(arg -> methodBuilder.addParameter(
                        ErrorGenerationUtils.buildParameterWithSafetyAnnotation(typeMapper, arg, true)));
        errorDefinition
                .getUnsafeArgs()
                .forEach(arg -> methodBuilder.addParameter(
                        ErrorGenerationUtils.buildParameterWithSafetyAnnotation(typeMapper, arg, false)));
    }

    public static ParameterSpec buildParameterWithSafetyAnnotation(
            TypeMapper typeMapper, FieldDefinition argDefinition, boolean isSafe) {
        Optional<LogSafety> safety = Optional.of(isSafe ? LogSafety.SAFE : LogSafety.UNSAFE);
        String argName = argDefinition.getFieldName().get();
        TypeName argType = ConjureAnnotations.withSafety(typeMapper.getClassName(argDefinition.getType()), safety);
        ParameterSpec.Builder parameterBuilder = ParameterSpec.builder(argType, argName);
        argDefinition
                .getDocs()
                .ifPresent(docs ->
                        parameterBuilder.addJavadoc("$L", StringUtils.appendIfMissing(Javadoc.render(docs), "\n")));
        return parameterBuilder.build();
    }

    // Conditional factory method
    public static MethodSpec.Builder conditionalStaticFactoryMethodBuilder(
            TypeMapper typeMapper,
            SafetyEvaluator safetyEvaluator,
            ErrorDefinition errorDefinition,
            Options options,
            ClassName exceptionClassThrown,
            Optional<String> errorType) {
        String exceptionMethodName = CaseFormat.UPPER_CAMEL.to(
                CaseFormat.LOWER_CAMEL, errorDefinition.getErrorName().getName());
        String methodName = "throwIf" + errorDefinition.getErrorName().getName();
        String shouldThrowVar = "shouldThrow";

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(TypeName.BOOLEAN, shouldThrowVar)
                        .addJavadoc("Cause the method to throw when true\n")
                        .build())
                .addParameters(ErrorGenerationUtils.createParametersForConditionalStaticFactory(
                        typeMapper, safetyEvaluator, errorDefinition));

        if (options.jetbrainsContractAnnotations()) {
            methodBuilder.addAnnotation(
                    ErrorGenerationUtils.createContractAnnotationForConditionalFactory(errorDefinition));
        }

        methodBuilder
                .beginControlFlow("if ($N)", shouldThrowVar)
                .addCode(
                        "throw $L;",
                        Expressions.localMethodCall(
                                exceptionMethodName,
                                Stream.concat(
                                                errorDefinition.getSafeArgs().stream(),
                                                errorDefinition.getUnsafeArgs().stream())
                                        .map(arg -> arg.getFieldName().get())
                                        .collect(Collectors.toList())));
        if (errorType.isPresent()) {
            methodBuilder.addJavadoc(
                    "Throws a {@link $T} of type $L when {@code $L} is true.\n",
                    exceptionClassThrown,
                    errorType.get(),
                    shouldThrowVar);
        } else {
            methodBuilder.addJavadoc(
                    "Throws a {@link $T} when {@code $L} is true.\n", exceptionClassThrown, shouldThrowVar);
        }
        return methodBuilder.endControlFlow();
    }

    private static List<ParameterSpec> createParametersForConditionalStaticFactory(
            TypeMapper typeMapper, SafetyEvaluator safetyEvaluator, ErrorDefinition errorDefinition) {
        return Stream.concat(
                        errorDefinition.getSafeArgs().stream().map(field -> FieldDefinition.builder()
                                .from(field)
                                .safety(LogSafety.SAFE)
                                .build()),
                        errorDefinition.getUnsafeArgs().stream().map(field -> FieldDefinition.builder()
                                .from(field)
                                .safety(LogSafety.UNSAFE)
                                .build()))
                .map(arg -> {
                    TypeName argumentTypeName = typeMapper.getClassName(arg.getType());
                    Optional<LogSafety> underlyingTypeSafety = safetyEvaluator.getUsageTimeSafety(arg);
                    Optional<LogSafety> typeSafety = safetyEvaluator.evaluate(arg.getType());
                    if (!SafetyEvaluator.allows(underlyingTypeSafety, typeSafety)) {
                        throw new IllegalStateException(String.format(
                                "Cannot use %s type %s as a %s parameter in error %s -> %s",
                                typeSafety.map(Object::toString).orElse("unknown"),
                                argumentTypeName,
                                underlyingTypeSafety.map(Object::toString).orElse("unknown"),
                                errorDefinition.getErrorName().getName(),
                                arg.getFieldName()));
                    }
                    return ParameterSpec.builder(
                                    argumentTypeName, arg.getFieldName().get())
                            .addAnnotations(ConjureAnnotations.safety(underlyingTypeSafety))
                            .addJavadoc(
                                    "$L",
                                    StringUtils.appendIfMissing(
                                            arg.getDocs().map(Javadoc::render).orElse(""), "\n"))
                            .build();
                })
                .collect(ImmutableList.toImmutableList());
    }

    private static AnnotationSpec createContractAnnotationForConditionalFactory(ErrorDefinition errorDefinition) {
        String contract = String.format(
                "true%s -> fail",
                ", _"
                        .repeat(errorDefinition.getSafeArgs().size()
                                + errorDefinition.getUnsafeArgs().size()));
        return AnnotationSpec.builder(ClassName.get("org.jetbrains.annotations", "Contract"))
                .addMember("value", "$S", contract)
                .build();
    }

    private ErrorGenerationUtils() {}
}
