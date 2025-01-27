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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.CaseFormat;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.errorprone.annotations.MustBeClosed;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.services.IsUndertowAsyncMarkerVisitor;
import com.palantir.conjure.java.services.ServiceGenerators;
import com.palantir.conjure.java.services.ServiceGenerators.EndpointErrorsJavaDoc;
import com.palantir.conjure.java.services.ServiceGenerators.EndpointJavaDocGenerationOptions;
import com.palantir.conjure.java.services.ServiceGenerators.RequestLineJavaDoc;
import com.palantir.conjure.java.util.ErrorGenerationUtils;
import com.palantir.conjure.java.util.ErrorGenerationUtils.ErrorNameToParameterExistenceMapping;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.EndpointError;
import com.palantir.conjure.spec.ErrorTypeName;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.visitor.TypeVisitor;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureErrors.BaseEndpointError;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.DialogueService;
import com.palantir.dialogue.DialogueServiceFactory;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.EndpointChannel;
import com.palantir.dialogue.EndpointChannelFactory;
import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

public final class DialogueInterfaceGenerator {

    private final Options options;
    private final ParameterTypeMapper parameterTypes;
    private final ReturnTypeMapper returnTypes;
    private final ErrorNameToParameterExistenceMapping errorNameToParameterExistenceMapping;

    public DialogueInterfaceGenerator(
            Options options,
            ParameterTypeMapper parameterTypes,
            ReturnTypeMapper returnTypes,
            ErrorNameToParameterExistenceMapping errorNameToParameterExistenceMapping) {
        this.options = options;
        this.parameterTypes = parameterTypes;
        this.returnTypes = returnTypes;
        this.errorNameToParameterExistenceMapping = errorNameToParameterExistenceMapping;
    }

    public JavaFile generateBlocking(
            ServiceDefinition def,
            StaticFactoryMethodGenerator methodGenerator,
            boolean generateDialogueEndpointErrorResultTypes) {
        return generate(
                def,
                Names.blockingClassName(def, options),
                StaticFactoryMethodType.BLOCKING,
                methodGenerator,
                generateDialogueEndpointErrorResultTypes);
    }

    public JavaFile generateAsync(
            ServiceDefinition def,
            StaticFactoryMethodGenerator methodGenerator,
            boolean generateDialogueEndpointErrorResultTypes) {
        return generate(
                def,
                Names.asyncClassName(def, options),
                StaticFactoryMethodType.ASYNC,
                methodGenerator,
                generateDialogueEndpointErrorResultTypes);
    }

    private JavaFile generate(
            ServiceDefinition def,
            ClassName className,
            StaticFactoryMethodType serviceCallType,
            StaticFactoryMethodGenerator methodGenerator,
            boolean generateDialogueEndpointErrorResultTypes) {
        String packageName = Packages.getPrefixedPackage(def.getServiceName().getPackage(), options.packagePrefix());
        TypeSpec.Builder serviceBuilder = TypeSpec.interfaceBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(DialogueInterfaceGenerator.class))
                .addAnnotation(AnnotationSpec.builder(DialogueService.class)
                        .addMember("value", "$T.Factory.class", className)
                        .build());

        serviceBuilder.addType(TypeSpec.classBuilder("Factory")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(DialogueServiceFactory.class), className))
                .addMethod(MethodSpec.methodBuilder("create")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(className)
                        .addParameter(EndpointChannelFactory.class, "endpointChannelFactory")
                        .addParameter(ConjureRuntime.class, "runtime")
                        .addStatement("return $T.of($L, $L)", className, "endpointChannelFactory", "runtime")
                        .build())
                .build());

        def.getDocs().ifPresent(docs -> serviceBuilder.addJavadoc("$L", StringUtils.appendIfMissing(docs.get(), "\n")));

        serviceBuilder.addMethods(def.getEndpoints().stream()
                .map(endpoint -> apiMethod(
                        packageName, className, endpoint, generateDialogueEndpointErrorResultTypes, serviceCallType))
                .collect(toList()));

        if (generateDialogueEndpointErrorResultTypes) {
            // Create public sealed interface for the "response" type for each of the endpoints.
            serviceBuilder.addTypes(def.getEndpoints().stream()
                    .map(endpointDef -> responseTypeForEndpoint(packageName, className, endpointDef))
                    .toList());
        }

        MethodSpec staticFactoryMethod = methodGenerator.generate(def);
        serviceBuilder.addMethod(staticFactoryMethod);

        serviceBuilder.addMethod(MethodSpec.methodBuilder("of")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .addJavadoc(
                        "Creates an asynchronous/non-blocking client for a $L service.",
                        def.getServiceName().getName())
                .returns(staticFactoryMethod.returnType())
                .addParameter(Channel.class, StaticFactoryMethodGenerator.CHANNEL)
                .addParameter(ConjureRuntime.class, StaticFactoryMethodGenerator.RUNTIME)
                .addCode(CodeBlock.builder()
                        .add(
                                "if ($L instanceof $T) { return $L(($T) $L, $L); }\n",
                                StaticFactoryMethodGenerator.CHANNEL,
                                EndpointChannelFactory.class,
                                staticFactoryMethod.name(),
                                EndpointChannelFactory.class,
                                StaticFactoryMethodGenerator.CHANNEL,
                                StaticFactoryMethodGenerator.RUNTIME)
                        .add(
                                "return $L(new $T() { "
                                        + "  @$T "
                                        + "  public $T endpoint($T endpoint) { "
                                        + "    return $L.clients().bind($L, endpoint);"
                                        + "  } "
                                        + "}, "
                                        + "$L);",
                                staticFactoryMethod.name(),
                                EndpointChannelFactory.class,
                                Override.class,
                                EndpointChannel.class,
                                Endpoint.class,
                                StaticFactoryMethodGenerator.RUNTIME,
                                StaticFactoryMethodGenerator.CHANNEL,
                                StaticFactoryMethodGenerator.RUNTIME)
                        .build())
                .build());

        return JavaFile.builder(packageName, serviceBuilder.build()).build();
    }

    private TypeSpec responseTypeForEndpoint(String packageName, ClassName className, EndpointDefinition endpointDef) {
        ClassName responseTypeName = ClassName.get(
                packageName,
                className.simpleName(),
                ErrorGenerationUtils.endpointResponseResultTypeName(endpointDef.getEndpointName()));
        TypeSpec successRecord = createSuccessRecord(packageName, className, responseTypeName, endpointDef);
        // Create a record for each of the endpoint's errors
        List<TypeSpec> errorTypes = new ArrayList<>();
        for (EndpointError endpointError : endpointDef.getErrors()) {
            errorTypes.add(constructEndpointErrorType(endpointError, packageName, responseTypeName));
        }

        TypeSpec.Builder builder = TypeSpec.interfaceBuilder(responseTypeName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.SEALED)
                .addPermittedSubclass(
                        ClassName.get(packageName, className.simpleName(), responseTypeName.simpleName(), "Success"))
                .addPermittedSubclasses(errorTypes.stream()
                        .map(type -> ClassName.get(
                                packageName, className.simpleName(), responseTypeName.simpleName(), type.name()))
                        .toList())
                .addType(successRecord)
                .addTypes(errorTypes);

        if (returnTypes.isBinaryOrOptionalBinary(returnTypes.baseType(endpointDef.getReturns()))) {
            builder.addSuperinterface(ClassName.get(Closeable.class))
                    .addMethod(MethodSpec.methodBuilder("close")
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                            .returns(TypeName.VOID)
                            .addException(IOException.class)
                            .build());
        }

        return builder.build();
    }

    private TypeSpec constructEndpointErrorType(
            EndpointError endpointError, String packageName, ClassName responseTypeName) {
        String errorTypeName = CaseFormat.UPPER_CAMEL.to(
                CaseFormat.UPPER_UNDERSCORE, endpointError.getError().getName());
        ClassName errorTypesClassName = ClassName.get(
                endpointError.getError().getPackage(),
                ErrorGenerationUtils.errorTypesClassName(
                        endpointError.getError().getNamespace()),
                errorTypeName);
        TypeSpec.Builder endpointErrorTypeBuilder = TypeSpec.classBuilder(ClassName.get(
                        packageName,
                        responseTypeName.simpleName(),
                        endpointError.getError().getName()))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .addSuperinterface(responseTypeName);
        ClassName parametersClassName = ClassName.get(
                endpointError.getError().getPackage(),
                ErrorGenerationUtils.errorTypesClassName(
                        endpointError.getError().getNamespace()),
                ErrorGenerationUtils.errorParametersClassName(
                        endpointError.getError().getName()));
        endpointErrorTypeBuilder
                .superclass(ParameterizedTypeName.get(ClassName.get(BaseEndpointError.class), parametersClassName))
                .addMethod(errorTypeConstructor(endpointError.getError(), parametersClassName, errorTypesClassName));
        return endpointErrorTypeBuilder.build();
    }

    private TypeSpec createSuccessRecord(
            String packageName, ClassName className, ClassName responseTypeName, EndpointDefinition endpointDef) {
        ClassName successTypeClassName =
                ClassName.get(packageName, className.simpleName(), responseTypeName.simpleName(), "Success");
        TypeSpec.Builder successRecordBuilder = TypeSpec.recordBuilder(successTypeClassName);
        MethodSpec.Builder successCtorBuilder =
                MethodSpec.compactConstructorBuilder().addModifiers(Modifier.PUBLIC);
        TypeName returnType = returnTypes.baseType(endpointDef.getReturns());
        if (!returnType.equals(TypeName.VOID)) {
            successCtorBuilder.addStatement(
                    "$T.checkArgumentNotNull(value, \"value cannot be null\")", Preconditions.class);
            ParameterSpec.Builder parameterBuilder = ParameterSpec.builder(returnType, "value");
            if (TypeName.get(InputStream.class).equals(returnType)) {
                parameterBuilder.addAnnotation(MustBeClosed.class);
            }
            // The @JsonValue annotation ensures that deserialization delegates to the type of "value".
            // https://github.com/FasterXML/jackson-databind/issues/3180
            parameterBuilder.addAnnotation(JsonValue.class);
            successCtorBuilder.addParameter(parameterBuilder.build());
        }

        successRecordBuilder
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .recordConstructor(successCtorBuilder.build())
                .addSuperinterface(responseTypeName);

        if (returnTypes.isBinaryOrOptionalBinary(returnType)) {
            MethodSpec.Builder closeOverride = MethodSpec.methodBuilder("close")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addException(IOException.class);
            if (returnTypes.isBinary(returnType)) {
                closeOverride.addCode(
                        CodeBlock.builder().addStatement("value.close()").build());
            } else {
                closeOverride.addCode(CodeBlock.builder()
                        .beginControlFlow("if (value.isPresent())")
                        .addStatement("value.get().close()")
                        .endControlFlow()
                        .build());
            }
            successRecordBuilder.addMethod(closeOverride.build());
        }
        return successRecordBuilder.build();
    }

    private MethodSpec errorTypeConstructor(
            ErrorTypeName errorTypeName, ClassName parametersClassName, ClassName errorTypesClassName) {
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
        // If the definition for the error does not specify any parameters, we do not need to attempt to deserialize the
        // parameters field.
        if (errorNameToParameterExistenceMapping.hasParameters(errorTypeName)) {
            ctorBuilder.addParameter(ParameterSpec.builder(parametersClassName, "parameters")
                    .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                            .addMember("value", "$S", "parameters")
                            .build())
                    .build());
            ctorBuilder.addStatement("super(errorCode, $T.name(), errorInstanceId, parameters)", errorTypesClassName);
        } else {
            ctorBuilder.addStatement(
                    "super(errorCode, $T.name(), errorInstanceId, new $T())", errorTypesClassName, parametersClassName);
        }
        return ctorBuilder.build();
    }

    private MethodSpec apiMethod(
            String packageName,
            ClassName className,
            EndpointDefinition endpointDef,
            boolean generateDialogueEndpointErrorResultTypes,
            StaticFactoryMethodType serviceCallType) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(
                        endpointDef.getEndpointName().get())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameters(parameterTypes.interfaceMethodParams(endpointDef))
                .addAnnotations(ConjureAnnotations.getClientEndpointAnnotations(endpointDef));
        endpointDef.getMarkers().stream()
                .filter(marker -> !marker.accept(IsUndertowAsyncMarkerVisitor.INSTANCE))
                .map(marker -> {
                    Preconditions.checkState(
                            marker.accept(TypeVisitor.IS_REFERENCE),
                            "Endpoint marker must be a reference type",
                            SafeArg.of("marker", marker));
                    return marker.accept(TypeVisitor.REFERENCE);
                })
                .forEach(referenceType -> methodBuilder.addAnnotation(
                        ClassName.get(referenceType.getPackage(), referenceType.getName())));

        endpointDef.getDeprecated().ifPresent(_deprecatedValue -> {
            if (endpointDef.getTags().contains("deprecated-for-removal")) {
                methodBuilder.addAnnotation(AnnotationSpec.builder(Deprecated.class)
                        .addMember("forRemoval", "true")
                        .build());
            } else {
                methodBuilder.addAnnotation(Deprecated.class);
            }
        });
        ServiceGenerators.addJavaDocForEndpointDefinition(
                methodBuilder,
                options.packagePrefix(),
                endpointDef,
                new EndpointJavaDocGenerationOptions(RequestLineJavaDoc.INCLUDE, EndpointErrorsJavaDoc.EXCLUDE));

        if (generateDialogueEndpointErrorResultTypes) {
            TypeName returnType = ClassName.get(
                    packageName,
                    className.simpleName(),
                    ErrorGenerationUtils.endpointResponseResultTypeName(endpointDef.getEndpointName()));
            methodBuilder.returns(serviceCallType.switchBy(
                    returnType, ParameterizedTypeName.get(ClassName.get(ListenableFuture.class), returnType)));
        } else {
            TypeName returnType = serviceCallType.switchBy(
                    returnTypes.baseType(endpointDef.getReturns()), returnTypes.async(endpointDef.getReturns()));
            methodBuilder.returns(returnType);
            if (TypeName.get(InputStream.class).equals(returnType)) {
                methodBuilder.addAnnotation(MustBeClosed.class);
            }
        }

        return methodBuilder.build();
    }
}
