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
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.conjure.java.util.ErrorGenerationUtils;
import com.palantir.conjure.java.util.ErrorGenerationUtils.DeclaredEndpointErrors;
import com.palantir.conjure.java.util.JavaNameSanitizer;
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
import com.palantir.logsafe.Safe;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.lang.model.element.Modifier;
import org.jspecify.annotations.Nullable;

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

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(errorTypesClassName(conjurePackage, namespace))
                .addMethod(ErrorGenerationUtils.privateConstructor())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addFields(generateErrorTypeFields(namespace, errorTypeDefinitions))
                .addMethods(methodSpecs)
                .addMethods(checkMethodSpecs)
                .addMethods(isRemoteExceptionDefinitions)
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(ErrorGenerator.class))
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(ErrorGenerator.class))
                .addTypes(generateErrorParameterRecords(errorTypeDefinitions, typeMapper))
                .addTypes(generateSerializableErrors(errorTypeDefinitions))
                .addTypes(generateRemoteExceptionTypes(errorTypeDefinitions));
        return JavaFile.builder(conjurePackage, typeBuilder.build())
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }

    private static List<TypeSpec> generateRemoteExceptionTypes(List<ErrorDefinition> errorDefinitions) {
        return errorDefinitions.stream()
                .map(ErrorGenerator::generateRemoteExceptionType)
                .toList();
    }

    private static TypeSpec generateRemoteExceptionType(ErrorDefinition errorDefinition) {
        String remoteExceptionClassName = errorDefinition.getErrorName().getName() + "Exception";
        ClassName serializableErrorClassName =
                ClassName.get("", errorDefinition.getErrorName().getName() + "SerializableError");

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(remoteExceptionClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .superclass(RemoteException.class)
                .addField(FieldSpec.builder(serializableErrorClassName, "error")
                        .addModifiers(Modifier.PRIVATE)
                        .build())
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(serializableErrorClassName, "error")
                        .addParameter(int.class, "status")
                        .addStatement("super(error.toSerializableError(), status)")
                        .addStatement("this.error = error")
                        .build())
                .addMethod(MethodSpec.methodBuilder("error")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(serializableErrorClassName)
                        .addStatement("return error")
                        .build());

        return classBuilder.build();
    }

    private static List<TypeSpec> generateSerializableErrors(List<ErrorDefinition> errorDefinitions) {
        return errorDefinitions.stream()
                .map(ErrorGenerator::generateSerializableError)
                .toList();
    }

    private static TypeSpec generateSerializableError(ErrorDefinition errorDefinition) {
        String serializableErrorClassName = errorDefinition.getErrorName().getName() + "SerializableError";
        String parameterClassNameString = ErrorGenerationUtils.errorParametersClassName(
                errorDefinition.getErrorName().getName());
        ClassName parametersClassName = ClassName.get("", parameterClassNameString);

        // Create constructor
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addAnnotation(AnnotationSpec.builder(JsonCreator.class)
                        .addMember("mode", "$T.$L", JsonCreator.Mode.class, JsonCreator.Mode.PROPERTIES)
                        .build())
                .addParameter(ParameterSpec.builder(ClassName.get(String.class), "errorCode")
                        .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                                .addMember("value", "$S", "errorCode")
                                .build())
                        .addAnnotation(Safe.class)
                        .build())
                .addParameter(ParameterSpec.builder(ClassName.get(String.class), "errorName")
                        .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                                .addMember("value", "$S", "errorName")
                                .build())
                        .addAnnotation(Safe.class)
                        .build())
                .addParameter(ParameterSpec.builder(ClassName.get(String.class), "errorInstanceId")
                        .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                                .addMember("value", "$S", "errorInstanceId")
                                .build())
                        .addAnnotation(Safe.class)
                        .build())
                .addParameter(ParameterSpec.builder(parametersClassName, "parameters")
                        .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                                .addMember("value", "$S", "parameters")
                                .build())
                        .build())
                .addParameter(ParameterSpec.builder(
                                ParameterizedTypeName.get(Map.class, String.class, String.class), "legacyParameters")
                        .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                                .addMember("value", "$S", "legacyParameters")
                                .build())
                        .addAnnotation(Nullable.class)
                        .build())
                .addStatement("super(errorCode, errorName, errorInstanceId, parameters)")
                .addStatement("this.legacyParameters = legacyParameters")
                .build();

        // Create the toSerializableError method
        MethodSpec.Builder toSerializableErrorBuilder = MethodSpec.methodBuilder("toSerializableError")
                .addModifiers(Modifier.PUBLIC)
                .returns(SerializableError.class);
        CodeBlock.Builder builder = CodeBlock.builder()
                .add("$T builder = $T.builder();", SerializableError.Builder.class, SerializableError.class);

        // If therea are legacy parameters, just use those.

        builder.beginControlFlow("if (legacyParameters != null)")
                .addStatement("builder.putAllParameters(legacyParameters)")
                .nextControlFlow("else");

        // Add all parameters
        List<FieldDefinition> allArgs = new ArrayList<>();
        allArgs.addAll(errorDefinition.getSafeArgs());
        allArgs.addAll(errorDefinition.getUnsafeArgs());

        for (int i = 0; i < allArgs.size(); i++) {
            FieldDefinition field = allArgs.get(i);
            String fieldName = field.getFieldName().get();
            builder.add(
                    (i == 0 ? "builder" : "") + ".putParameters($S, $T.toString(parameters().$L()))"
                            + (i == allArgs.size() - 1 ? ";" : ""),
                    fieldName,
                    Objects.class,
                    JavaNameSanitizer.sanitizeErrorParameterName(fieldName));
        }
        builder.endControlFlow();
        builder.add("builder.errorCode(errorCode())")
                .add(".errorName(errorName())")
                .add(".errorInstanceId(errorInstanceId());");
        toSerializableErrorBuilder.addCode(builder.build());
        toSerializableErrorBuilder.addStatement("return builder.build()");

        // Create the class
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(serializableErrorClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .superclass(
                        ParameterizedTypeName.get(ClassName.get(AbstractSerializableError.class), parametersClassName))
                .addField(FieldSpec.builder(
                                ParameterizedTypeName.get(Map.class, String.class, String.class), "legacyParameters")
                        .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                        .addAnnotation(Nullable.class)
                        .build())
                .addMethod(constructor)
                .addMethod(toSerializableErrorBuilder.build());

        return classBuilder.build();
    }

    private static List<TypeSpec> generateErrorParameterRecords(
            List<ErrorDefinition> errorTypeDefinitions, TypeMapper typeMapper) {
        return errorTypeDefinitions.stream()
                .map(errorDefinition -> generateErrorParameterRecord(errorDefinition, typeMapper))
                .toList();
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
}
