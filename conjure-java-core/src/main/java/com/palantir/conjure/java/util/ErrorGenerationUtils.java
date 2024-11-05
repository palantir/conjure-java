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

import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.types.TypeMapper;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.EndpointError;
import com.palantir.conjure.spec.ErrorDefinition;
import com.palantir.conjure.spec.ErrorNamespace;
import com.palantir.conjure.spec.ErrorTypeName;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.LogSafety;
import com.palantir.javapoet.JavaFile;
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
import org.apache.commons.lang3.function.TriFunction;

public final class ErrorGenerationUtils {
    public static MethodSpec privateConstructor() {
        return MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build();
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

    public record ErrorDefinitionsByPackageAndNamespace(
            Map<String, Map<ErrorNamespace, List<ErrorDefinition>>> packageToNamespacedErrorDefs) {
        public static ErrorDefinitionsByPackageAndNamespace from(List<ErrorDefinition> errorTypeNameToDef) {
            Map<String, Map<ErrorNamespace, List<ErrorDefinition>>> pkgToNamespacedErrorDefs = new HashMap<>();
            errorTypeNameToDef.forEach(errorDef -> {
                String errorPkg = errorDef.getErrorName().getPackage();
                pkgToNamespacedErrorDefs.computeIfAbsent(errorPkg, key -> new HashMap<>());
                Map<ErrorNamespace, List<ErrorDefinition>> namespacedErrorDefs = pkgToNamespacedErrorDefs.get(errorPkg);
                ErrorNamespace namespace = errorDef.getNamespace();
                namespacedErrorDefs.computeIfAbsent(namespace, key -> new ArrayList<>());
                namespacedErrorDefs.get(namespace).add(errorDef);
            });
            return new ErrorDefinitionsByPackageAndNamespace(pkgToNamespacedErrorDefs);
        }

        public Stream<JavaFile> processErrorDefinitions(
                TriFunction<String, ErrorNamespace, List<ErrorDefinition>, Stream<JavaFile>> function) {
            return packageToNamespacedErrorDefs.entrySet().stream()
                    .flatMap(entry -> entry.getValue().entrySet().stream()
                            .flatMap(innerEntry ->
                                    function.apply(entry.getKey(), innerEntry.getKey(), innerEntry.getValue())));
        }
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

    private ErrorGenerationUtils() {}
}
