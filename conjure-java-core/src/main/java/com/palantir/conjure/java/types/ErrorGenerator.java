/*
 * (c) Copyright 2020 Palantir Technologies Inc. All rights reserved.
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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.Generator;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.conjure.java.util.Javadoc;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.java.util.TypeFunctions;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.ErrorDefinition;
import com.palantir.conjure.spec.ErrorNamespace;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.LogSafety;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.UnsafeArg;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

public final class ErrorGenerator implements Generator {

    private static final String REMOTE_EXCEPTION_VAR = "remoteException";

    private final Options options;

    public ErrorGenerator(Options options) {
        this.options = options;
    }

    @Override
    public Stream<JavaFile> generate(ConjureDefinition definition) {
        Map<com.palantir.conjure.spec.TypeName, TypeDefinition> types = TypeFunctions.toTypesMap(definition);
        TypeMapper typeMapper = new TypeMapper(types, options);
        SafetyEvaluator safetyEvaluator = new SafetyEvaluator(types);
        return splitErrorDefsByNamespace(definition.getErrors()).entrySet().stream()
                .flatMap(entry -> entry.getValue().entrySet().stream()
                        .map(innerEntry -> generateErrorTypesForNamespace(
                                typeMapper,
                                safetyEvaluator,
                                Packages.getPrefixedPackage(entry.getKey(), options.packagePrefix()),
                                innerEntry.getKey(),
                                innerEntry.getValue())));
    }

    private static Map<String, Map<ErrorNamespace, List<ErrorDefinition>>> splitErrorDefsByNamespace(
            List<ErrorDefinition> errorTypeNameToDef) {
        Map<String, Map<ErrorNamespace, List<ErrorDefinition>>> pkgToNamespacedErrorDefs = new HashMap<>();
        errorTypeNameToDef.forEach(errorDef -> {
            String errorPkg = errorDef.getErrorName().getPackage();
            pkgToNamespacedErrorDefs.computeIfAbsent(errorPkg, key -> new HashMap<>());

            Map<ErrorNamespace, List<ErrorDefinition>> namespacedErrorDefs = pkgToNamespacedErrorDefs.get(errorPkg);
            ErrorNamespace namespace = errorDef.getNamespace();
            // TODO(rfink): Use Multimap?
            namespacedErrorDefs.computeIfAbsent(namespace, key -> new ArrayList<>());
            namespacedErrorDefs.get(namespace).add(errorDef);
        });
        return pkgToNamespacedErrorDefs;
    }

    private static ImmutableList<FieldSpec> generateErrorTypeFields(
            ErrorNamespace namespace, List<ErrorDefinition> errorTypeDefinitions) {
        return errorTypeDefinitions.stream()
                .map(errorDef -> {
                    CodeBlock initializer = CodeBlock.of(
                            "ErrorType.create(ErrorType.Code.$L, \"$L:$L\")",
                            errorDef.getCode().get(),
                            namespace.get(),
                            errorDef.getErrorName().getName());
                    FieldSpec.Builder fieldSpecBuilder = FieldSpec.builder(
                                    ClassName.get(ErrorType.class),
                                    CaseFormat.UPPER_CAMEL.to(
                                            CaseFormat.UPPER_UNDERSCORE,
                                            errorDef.getErrorName().getName()),
                                    Modifier.PUBLIC,
                                    Modifier.STATIC,
                                    Modifier.FINAL)
                            .initializer(initializer);
                    errorDef.getDocs().ifPresent(docs -> fieldSpecBuilder.addJavadoc(docs.get()));
                    return fieldSpecBuilder.build();
                })
                .collect(ImmutableList.toImmutableList());
    }

    private JavaFile generateErrorTypesForNamespace(
            TypeMapper typeMapper,
            SafetyEvaluator safetyEvaluator,
            String conjurePackage,
            ErrorNamespace namespace,
            List<ErrorDefinition> errorTypeDefinitions) {
        // Generate ServiceException factory methods
        List<MethodSpec> methodSpecs = errorTypeDefinitions.stream()
                .flatMap(entry -> {
                    MethodSpec withoutCause = generateExceptionFactory(typeMapper, entry, false);
                    MethodSpec withCause = generateExceptionFactory(typeMapper, entry, true);
                    return Stream.of(withoutCause, withCause);
                })
                .collect(Collectors.toList());

        // Generate ServiceException factory check methods
        List<MethodSpec> checkMethodSpecs = errorTypeDefinitions.stream()
                .map(entry -> {
                    String exceptionMethodName = CaseFormat.UPPER_CAMEL.to(
                            CaseFormat.LOWER_CAMEL, entry.getErrorName().getName());
                    String methodName = "throwIf" + entry.getErrorName().getName();

                    String shouldThrowVar = "shouldThrow";

                    MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                            .addJavadoc(
                                    "Throws a {@link $T} of type $L when {@code $L} is true.\n",
                                    ServiceException.class,
                                    entry.getErrorName().getName(),
                                    shouldThrowVar)
                            .addParameter(ParameterSpec.builder(TypeName.BOOLEAN, shouldThrowVar)
                                    .addJavadoc("Cause the method to throw when true\n")
                                    .build())
                            .addParameters(Streams.concat(
                                            entry.getSafeArgs().stream().map(field -> FieldDefinition.builder()
                                                    .from(field)
                                                    .safety(LogSafety.SAFE)
                                                    .build()),
                                            entry.getUnsafeArgs().stream().map(field -> FieldDefinition.builder()
                                                    .from(field)
                                                    .safety(LogSafety.UNSAFE)
                                                    .build()))
                                    .map(arg -> {
                                        TypeName argumentTypeName = typeMapper.getClassName(arg.getType());
                                        Optional<LogSafety> required = arg.getSafety();
                                        Optional<LogSafety> typeSafety = safetyEvaluator.evaluate(arg.getType());
                                        if (!SafetyEvaluator.allows(required, typeSafety)) {
                                            throw new IllegalStateException(String.format(
                                                    "Cannot use %s type %s as a %s parameter in error %s -> %s",
                                                    typeSafety
                                                            .map(Object::toString)
                                                            .orElse("unknown"),
                                                    argumentTypeName,
                                                    required.map(Object::toString)
                                                            .orElse("unknown"),
                                                    entry.getErrorName().getName(),
                                                    arg.getFieldName()));
                                        }
                                        return ParameterSpec.builder(
                                                        argumentTypeName,
                                                        arg.getFieldName().get())
                                                .addAnnotations(ConjureAnnotations.safety(arg.getSafety()))
                                                .addJavadoc(
                                                        "$L",
                                                        StringUtils.appendIfMissing(
                                                                arg.getDocs()
                                                                        .map(Javadoc::render)
                                                                        .orElse(""),
                                                                "\n"))
                                                .build();
                                    })
                                    .collect(ImmutableList.toImmutableList()));
                    if (options.jetbrainsContractAnnotations()) {
                        String contract = String.format(
                                "true%s -> fail",
                                ", _"
                                        .repeat(entry.getSafeArgs().size()
                                                + entry.getUnsafeArgs().size()));
                        methodBuilder.addAnnotation(
                                AnnotationSpec.builder(ClassName.get("org.jetbrains.annotations", "Contract"))
                                        .addMember("value", "$S", contract)
                                        .build());
                    }

                    return methodBuilder
                            .beginControlFlow("if ($N)", shouldThrowVar)
                            .addCode(
                                    "throw $L;",
                                    Expressions.localMethodCall(
                                            exceptionMethodName,
                                            Streams.concat(entry.getSafeArgs().stream(), entry.getUnsafeArgs().stream())
                                                    .map(arg ->
                                                            arg.getFieldName().get())
                                                    .collect(Collectors.toList())))
                            .endControlFlow()
                            .build();
                })
                .collect(Collectors.toList());

        List<MethodSpec> isRemoteExceptionDefinitions = errorTypeDefinitions.stream()
                .map(entry -> {
                    String typeName = CaseFormat.UPPER_CAMEL.to(
                            CaseFormat.UPPER_UNDERSCORE, entry.getErrorName().getName());
                    String methodName = "is" + entry.getErrorName().getName();

                    return MethodSpec.methodBuilder(methodName)
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                            .addParameter(RemoteException.class, REMOTE_EXCEPTION_VAR)
                            .returns(TypeName.BOOLEAN)
                            .addStatement(Expressions.requireNonNull(
                                    REMOTE_EXCEPTION_VAR, "remote exception must not be null"))
                            .addStatement(
                                    "return $N.name().equals($N.getError().errorName())",
                                    typeName,
                                    REMOTE_EXCEPTION_VAR)
                            .addJavadoc(
                                    "Returns true if the {@link $T} is named $L:$L",
                                    RemoteException.class,
                                    entry.getNamespace(),
                                    entry.getErrorName().getName())
                            .build();
                })
                .collect(Collectors.toList());

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(errorTypesClassName(conjurePackage, namespace))
                .addMethod(privateConstructor())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addFields(generateErrorTypeFields(namespace, errorTypeDefinitions))
                .addMethods(methodSpecs)
                .addMethods(checkMethodSpecs)
                .addMethods(isRemoteExceptionDefinitions)
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(ErrorGenerator.class));

        return JavaFile.builder(conjurePackage, typeBuilder.build())
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }

    private static MethodSpec generateExceptionFactory(
            TypeMapper typeMapper, ErrorDefinition entry, boolean withCause) {
        String methodName = CaseFormat.UPPER_CAMEL.to(
                CaseFormat.LOWER_CAMEL, entry.getErrorName().getName());
        String typeName = CaseFormat.UPPER_CAMEL.to(
                CaseFormat.UPPER_UNDERSCORE, entry.getErrorName().getName());

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ClassName.get(ServiceException.class));

        methodBuilder.addCode("return new $T($L", ServiceException.class, typeName);

        if (withCause) {
            methodBuilder.addParameter(Throwable.class, "cause");
            methodBuilder.addCode(", cause");
        }

        entry.getSafeArgs().forEach(arg -> processArg(typeMapper, methodBuilder, arg, true));

        entry.getUnsafeArgs().forEach(arg -> processArg(typeMapper, methodBuilder, arg, false));
        methodBuilder.addCode(");");

        return methodBuilder.build();
    }

    private static void processArg(
            TypeMapper typeMapper, MethodSpec.Builder methodBuilder, FieldDefinition argDefinition, boolean isSafe) {
        Optional<LogSafety> safety = Optional.of(isSafe ? LogSafety.SAFE : LogSafety.UNSAFE);
        String argName = argDefinition.getFieldName().get();
        TypeName argType = ConjureAnnotations.withSafety(typeMapper.getClassName(argDefinition.getType()), safety);
        ParameterSpec.Builder parameterBuilder =
                ParameterSpec.builder(argType, argName).addAnnotations(ConjureAnnotations.safety(safety));
        argDefinition
                .getDocs()
                .ifPresent(docs ->
                        parameterBuilder.addJavadoc("$L", StringUtils.appendIfMissing(Javadoc.render(docs), "\n")));
        methodBuilder.addParameter(parameterBuilder.build());
        Class<?> clazz = isSafe ? SafeArg.class : UnsafeArg.class;
        methodBuilder.addCode(",\n    $T.of($S, $L)", clazz, argName, argName);
    }

    private static ClassName errorTypesClassName(String conjurePackage, ErrorNamespace namespace) {
        return ClassName.get(conjurePackage, namespace.get() + "Errors");
    }

    private static MethodSpec privateConstructor() {
        return MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build();
    }
}
