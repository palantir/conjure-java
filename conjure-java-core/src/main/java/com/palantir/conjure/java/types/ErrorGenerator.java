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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.Generator;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.api.errors.AbstractSerializableError;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.conjure.java.util.ErrorGenerationUtils;
import com.palantir.conjure.java.util.ErrorGenerationUtils.DeclaredEndpointErrors;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.java.util.TypeFunctions;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.ErrorDefinition;
import com.palantir.conjure.spec.ErrorNamespace;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;
import com.palantir.javapoet.WildcardTypeName;
import com.palantir.logsafe.Arg;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.UnsafeArg;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.lang.model.element.Modifier;

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
        DeclaredEndpointErrors endpointErrors = DeclaredEndpointErrors.from(definition);
        return ErrorGenerationUtils.getNamespacedErrorsFromDefinitions(definition.getErrors()).stream()
                .flatMap(namespacedErrors -> Stream.of(generateErrorTypesForNamespace(
                        typeMapper,
                        safetyEvaluator,
                        endpointErrors,
                        Packages.getPrefixedPackage(namespacedErrors.javaPackage(), options.packagePrefix()),
                        namespacedErrors.namespace(),
                        namespacedErrors.errors())));
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
            DeclaredEndpointErrors endpointErrors,
            String conjurePackage,
            ErrorNamespace namespace,
            List<ErrorDefinition> errorTypeDefinitions) {
        // Generate ServiceException factory methods
        List<MethodSpec> methodSpecs = errorTypeDefinitions.stream()
                // Skip ServiceFactory method creation for errors defined in endpoints. Users should throw the checked
                // service exception.
                .filter(errorDefinition -> !endpointErrors.contains(errorDefinition))
                .flatMap(entry -> {
                    MethodSpec withoutCause = generateExceptionFactory(typeMapper, entry, false);
                    MethodSpec withCause = generateExceptionFactory(typeMapper, entry, true);
                    return Stream.of(withoutCause, withCause);
                })
                .collect(Collectors.toList());

        // Generate ServiceException factory check methods
        List<MethodSpec> checkMethodSpecs = errorTypeDefinitions.stream()
                .filter(errorDefinition -> !endpointErrors.contains(errorDefinition))
                .map(entry -> ErrorGenerationUtils.conditionalStaticFactoryMethodBuilder(
                                typeMapper,
                                safetyEvaluator,
                                entry,
                                options,
                                ClassName.get(ServiceException.class),
                                Optional.of(entry.getErrorName().getName()))
                        .build())
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

        ClassName errorTypesClassName = errorTypesClassName(conjurePackage, namespace);
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(errorTypesClassName)
                .addMethod(ErrorGenerationUtils.privateConstructor())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addFields(generateErrorTypeFields(namespace, errorTypeDefinitions))
                .addMethods(methodSpecs)
                .addMethods(checkMethodSpecs)
                .addMethods(isRemoteExceptionDefinitions)
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(ErrorGenerator.class));

        // if (options.generateDialogueEndpointErrorResultTypes()) {
        // Always generate the error parameter records.
        typeBuilder.addTypes(generateErrorParameterRecords(errorTypeDefinitions, typeMapper));
        generateSerializableErrors(errorTypeDefinitions, errorTypesClassName);
        generateErrorExceptions(errorTypeDefinitions, errorTypesClassName);
        // }

        return JavaFile.builder(conjurePackage, typeBuilder.build())
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }

    private static List<TypeSpec> generateErrorParameterRecords(
            List<ErrorDefinition> errorTypeDefinitions, TypeMapper typeMapper) {
        return errorTypeDefinitions.stream()
                .map(errorDefinition -> generateErrorParameterRecord(errorDefinition, typeMapper))
                .toList();
    }

    private static List<TypeSpec> generateSerializableErrors(
            List<ErrorDefinition> errorDefinitions, ClassName errorTypesClassName) {
        return errorDefinitions.stream()
                .map(errorDef -> generateSerializableError(errorDef, errorTypesClassName))
                .toList();
    }

    private static List<TypeSpec> generateErrorExceptions(
            List<ErrorDefinition> errorDefinitions, ClassName errorTypesClassName) {
        return errorDefinitions.stream()
                .map(errorDef -> generateErrorException(errorDef, errorTypesClassName))
                .toList();
    }

    private static TypeSpec generateErrorException(ErrorDefinition errorDefinition, ClassName errorTypesClassName) {
        String errorName = errorDefinition.getErrorName().getName();
        String exceptionClassName = errorName + "Exception";
        String serializableErrorClassName = ErrorGenerator.serializableErrorClassName(errorName);
        ClassName errorType = errorTypesClassName.nestedClass(serializableErrorClassName);

        // Constructor
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(errorType, "error")
                .addParameter(TypeName.INT, "status")
                .addStatement("this.$1N = $1N", "error")
                .addStatement("this.$1N = $1N", "status")
                .build();

        // getError
        MethodSpec getError = MethodSpec.methodBuilder("getError")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(errorType)
                .addStatement("return $N", "error")
                .build();

        // getStatus
        MethodSpec getStatus = MethodSpec.methodBuilder("getStatus")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.INT)
                .addStatement("return $N", "status")
                .build();

        // getLogMessage
        MethodSpec getLogMessage = MethodSpec.methodBuilder("getLogMessage")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addStatement(
                        """
                        return error.errorCode().equals(error.errorName())
                               ? $S + error.errorCode()
                               : $S + error.errorCode() + " (" + error.errorName() + ")"
                        """,
                        exceptionClassName + ":",
                        exceptionClassName + ":")
                .build();

        // getArgs
        CodeBlock.Builder argsBlock = CodeBlock.builder().add("return $T.of(", List.class);
        for (FieldDefinition param : errorDefinition.getSafeArgs()) {
            argsBlock.add(
                    "$T.of($S, error.parameters().$L())",
                    SafeArg.class,
                    param.getFieldName().get(),
                    param.getFieldName().get());
        }
        for (FieldDefinition param : errorDefinition.getUnsafeArgs()) {
            argsBlock.add(
                    "$T.of($S, error.parameters().$L())",
                    UnsafeArg.class,
                    param.getFieldName().get(),
                    param.getFieldName().get());
        }
        argsBlock.add(");");

        MethodSpec getArgs = MethodSpec.methodBuilder("getArgs")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(
                        List.class,
                        ParameterizedTypeName.get(ClassName.get(Arg.class), WildcardTypeName.subtypeOf(Object.class))
                                .getClass()))
                .addCode(argsBlock.build())
                .build();

        return TypeSpec.classBuilder(exceptionClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .superclass(RemoteException.class)
                .addField(
                        FieldSpec.builder(errorType, "error", Modifier.PRIVATE).build())
                .addField(FieldSpec.builder(TypeName.INT, "status", Modifier.PRIVATE)
                        .build())
                .addMethod(constructor)
                .addMethod(getError)
                .addMethod(getStatus)
                .addMethod(getLogMessage)
                .addMethod(getArgs)
                .build();
    }

    private static TypeSpec generateSerializableError(ErrorDefinition errorDefinition, ClassName errorTypesClassName) {
        ClassName parametersClassName = errorTypesClassName.nestedClass(ErrorGenerationUtils.errorParametersClassName(
                errorDefinition.getErrorName().getName()));
        MethodSpec.Builder ctorBuilder = MethodSpec.constructorBuilder()
                .addAnnotation(AnnotationSpec.builder(JsonCreator.class)
                        .addMember("mode", "$T.$L", JsonCreator.Mode.class, JsonCreator.Mode.PROPERTIES)
                        .build())
                .addParameter(ParameterSpec.builder(ClassName.get(String.class), "errorCode")
                        .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                                .addMember("value", "$S", "errorCode")
                                .build())
                        .addAnnotation(Safe.class)
                        .build())
                .addParameter(ParameterSpec.builder(ClassName.get(String.class), "errorInstanceId")
                        .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                                .addMember("value", "$S", "errorInstanceId")
                                .build())
                        .addAnnotation(Safe.class)
                        .build());

        // This could be cleaner with streams. This is more efficient.
        List<FieldDefinition> allParams =
                new ArrayList<>(errorDefinition.getUnsafeArgs().size()
                        + errorDefinition.getSafeArgs().size());
        allParams.addAll(errorDefinition.getSafeArgs());
        allParams.addAll(errorDefinition.getUnsafeArgs());

        if (!allParams.isEmpty()) {
            ctorBuilder.addParameter(ParameterSpec.builder(parametersClassName, "parameters")
                    .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                            .addMember("value", "$S", "parameters")
                            .build())
                    .build());
        }

        MethodSpec.Builder legacyParams = MethodSpec.methodBuilder("legacyParameters")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(Map.class, String.class, String.class))
                .addStatement(
                        "return $T.of($L)",
                        Map.class,
                        allParams.stream()
                                .map(param -> String.format(
                                        "\"%s\", Objects.toString(parameters().%s())",
                                        param.getFieldName().get(),
                                        param.getFieldName().get()))
                                .collect(Collectors.joining(", ")));

        return TypeSpec.classBuilder(serializableErrorClassName(
                        errorDefinition.getErrorName().getName()))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .superclass(
                        ParameterizedTypeName.get(ClassName.get(AbstractSerializableError.class), parametersClassName))
                .addMethod(ctorBuilder.build())
                .addMethod(legacyParams.build())
                .build();
    }

    private static TypeSpec generateErrorParameterRecord(ErrorDefinition errorDefinition, TypeMapper typeMapper) {
        TypeSpec.Builder parametersRecordBuilder = TypeSpec.recordBuilder(ErrorGenerationUtils.errorParametersClassName(
                        errorDefinition.getErrorName().getName()))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        MethodSpec.Builder ctorBuilder = MethodSpec.constructorBuilder();
        for (FieldDefinition fieldDef : errorDefinition.getSafeArgs()) {
            ctorBuilder.addParameter(
                    ErrorGenerationUtils.buildParameterWithSafetyAnnotationAndJsonProperty(typeMapper, fieldDef, true));
        }
        for (FieldDefinition fieldDef : errorDefinition.getUnsafeArgs()) {
            ctorBuilder.addParameter(ErrorGenerationUtils.buildParameterWithSafetyAnnotationAndJsonProperty(
                    typeMapper, fieldDef, false));
        }
        return parametersRecordBuilder.recordConstructor(ctorBuilder.build()).build();
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
            methodBuilder.addCode(", ");
            ErrorGenerationUtils.addNullableThrowableCauseParameterToMethodBuilder(methodBuilder);
        }

        ErrorGenerationUtils.addAllLogSafeArgumentsToMethodBuilder(typeMapper, entry, methodBuilder);

        methodBuilder.addCode(");");

        return methodBuilder.build();
    }

    static ClassName errorTypesClassName(String conjurePackage, ErrorNamespace namespace) {
        return ClassName.get(conjurePackage, ErrorGenerationUtils.errorTypesClassName(namespace));
    }

    static String serializableErrorClassName(String errorName) {
        return errorName + "Error";
    }
}
