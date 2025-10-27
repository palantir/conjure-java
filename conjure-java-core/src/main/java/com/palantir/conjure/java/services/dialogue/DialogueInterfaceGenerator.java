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

import com.google.common.base.CaseFormat;
import com.google.errorprone.annotations.MustBeClosed;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.services.IsUndertowAsyncMarkerVisitor;
import com.palantir.conjure.java.services.ServiceGenerators;
import com.palantir.conjure.java.services.ServiceGenerators.EndpointErrorsJavaDoc;
import com.palantir.conjure.java.services.ServiceGenerators.EndpointJavaDocGenerationOptions;
import com.palantir.conjure.java.services.ServiceGenerators.RequestLineJavaDoc;
import com.palantir.conjure.java.util.ErrorGenerationUtils;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.EndpointError;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.visitor.TypeVisitor;
import com.palantir.dialogue.Channel;
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
import com.palantir.logsafe.SafeArg;
import java.io.InputStream;
import java.util.Optional;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

public final class DialogueInterfaceGenerator {

    private final Options options;
    private final ParameterTypeMapper parameterTypes;
    private final ReturnTypeMapper returnTypes;

    public DialogueInterfaceGenerator(
            Options options, ParameterTypeMapper parameterTypes, ReturnTypeMapper returnTypes) {
        this.options = options;
        this.parameterTypes = parameterTypes;
        this.returnTypes = returnTypes;
    }

    public JavaFile generateBlocking(ServiceDefinition def, StaticFactoryMethodGenerator methodGenerator) {
        return generate(def, Names.blockingClassName(def, options), StaticFactoryMethodType.BLOCKING, methodGenerator);
    }

    public JavaFile generateAsync(ServiceDefinition def, StaticFactoryMethodGenerator methodGenerator) {
        return generate(def, Names.asyncClassName(def, options), StaticFactoryMethodType.ASYNC, methodGenerator);
    }

    @SuppressWarnings("for-rollout:deprecation")
    private JavaFile generate(
            ServiceDefinition def,
            ClassName className,
            StaticFactoryMethodType serviceCallType,
            StaticFactoryMethodGenerator methodGenerator) {
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

        for (EndpointDefinition endpoint : def.getEndpoints()) {
            serviceBuilder.addMethod(apiMethod(endpoint, serviceCallType));
            if (options.generateErrorParameterFormatRespectingDialogueInterfaces()) {
                endpointErrorUtilityType(
                                endpoint,
                                Packages.getPrefixedPackage(def.getServiceName().getPackage(), options.packagePrefix()),
                                className)
                        .ifPresent(serviceBuilder::addType);
            }
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

        return JavaFile.builder(
                        Packages.getPrefixedPackage(def.getServiceName().getPackage(), options.packagePrefix()),
                        serviceBuilder.build())
                .build();
    }

    private MethodSpec apiMethod(EndpointDefinition endpointDef, StaticFactoryMethodType serviceCallType) {
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

        TypeName returnType = serviceCallType.switchBy(
                returnTypes.baseType(endpointDef.getReturns()), returnTypes.async(endpointDef.getReturns()));
        methodBuilder.returns(returnType);
        if (TypeName.get(InputStream.class).equals(returnType)) {
            methodBuilder.addAnnotation(MustBeClosed.class);
        }

        return methodBuilder.build();
    }

    private Optional<TypeSpec> endpointErrorUtilityType(
            EndpointDefinition endpointDef, String packageName, ClassName className) {
        ClassName utiltyClassName = ClassName.get(
                packageName,
                className.simpleName(),
                CaseFormat.LOWER_CAMEL.to(
                                CaseFormat.UPPER_CAMEL,
                                endpointDef.getEndpointName().get()) + "Errors");
        if (endpointDef.getErrors().isEmpty()) {
            return Optional.empty();
        }
        TypeSpec.Builder builder = TypeSpec.interfaceBuilder(utiltyClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.SEALED);
        MethodSpec.Builder fromBuilder = MethodSpec.methodBuilder("from")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(RemoteException.class, "e")
                .returns(utiltyClassName);

        boolean first = true;
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        for (EndpointError error : endpointDef.getErrors()) {
            String errorName = error.getError().getName();
            String errorPackage = error.getError().getPackage();
            ClassName exceptionClassName = ClassName.get(
                    Packages.getPrefixedPackage(errorPackage, options.packagePrefix()),
                    ErrorGenerationUtils.errorTypesClassName(error.getError().getNamespace()),
                    ErrorGenerationUtils.errorExceptionClassName(errorName));
            TypeSpec errorRecord = createRecordForEndpointErrorUtility(errorName, exceptionClassName, utiltyClassName);
            // Not strictly required. Seems a bit cleaner to not have it.
            // builder.addPermittedSubclass(ClassName.get(errorPackage, errorName));
            builder.addType(errorRecord);

            // Add to the `from` method
            if (first) {
                first = false;
                codeBlock.beginControlFlow("if (e instanceof $T ex)", exceptionClassName);
            } else {
                codeBlock.nextControlFlow("else if (e instanceof $T ex)", exceptionClassName);
            }
            codeBlock.addStatement("return new $L(ex)", errorName);
        }
        // Add the unknown case
        TypeSpec unknownRecord =
                createRecordForEndpointErrorUtility("Unknown", ClassName.get(RemoteException.class), utiltyClassName);
        builder.addType(unknownRecord);
        codeBlock.nextControlFlow("else").addStatement("return new $L(e)", unknownRecord.name());
        codeBlock.endControlFlow();
        fromBuilder.addCode(codeBlock.build());
        builder.addMethod(fromBuilder.build());
        return Optional.of(builder.build());
    }

    private static TypeSpec createRecordForEndpointErrorUtility(
            String recordName, ClassName errorClassName, ClassName utiltyClassName) {
        return TypeSpec.recordBuilder(recordName)
                .recordConstructor(MethodSpec.constructorBuilder()
                        .addParameter(ParameterSpec.builder(errorClassName, "e").build())
                        .build())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addSuperinterface(utiltyClassName)
                .build();
    }
}
