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

import com.google.errorprone.annotations.MustBeClosed;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.services.IsUndertowAsyncMarkerVisitor;
import com.palantir.conjure.java.services.ServiceGenerators;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.visitor.TypeVisitor;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.DialogueService;
import com.palantir.dialogue.DialogueServiceFactory;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.EndpointChannel;
import com.palantir.dialogue.EndpointChannelFactory;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.Function;
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
        return generate(def, Names.blockingClassName(def, options), returnTypes::baseType, methodGenerator);
    }

    public JavaFile generateAsync(ServiceDefinition def, StaticFactoryMethodGenerator methodGenerator) {
        return generate(def, Names.asyncClassName(def, options), returnTypes::async, methodGenerator);
    }

    private JavaFile generate(
            ServiceDefinition def,
            ClassName className,
            Function<Optional<Type>, TypeName> returnTypeMapper,
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

        serviceBuilder.addMethods(def.getEndpoints().stream()
                .map(endpoint -> apiMethod(endpoint, returnTypeMapper))
                .collect(toList()));

        MethodSpec staticFactoryMethod = methodGenerator.generate(def);
        serviceBuilder.addMethod(staticFactoryMethod);

        serviceBuilder.addMethod(MethodSpec.methodBuilder("of")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .addJavadoc(
                        "Creates an asynchronous/non-blocking client for a $L service.",
                        def.getServiceName().getName())
                .returns(staticFactoryMethod.returnType)
                .addParameter(Channel.class, StaticFactoryMethodGenerator.CHANNEL)
                .addParameter(ConjureRuntime.class, StaticFactoryMethodGenerator.RUNTIME)
                .addCode(CodeBlock.builder()
                        .add(
                                "if ($L instanceof $T) { return $L(($T) $L, $L); }\n",
                                StaticFactoryMethodGenerator.CHANNEL,
                                EndpointChannelFactory.class,
                                staticFactoryMethod.name,
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
                                staticFactoryMethod.name,
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

    private MethodSpec apiMethod(EndpointDefinition endpointDef, Function<Optional<Type>, TypeName> returnTypeMapper) {
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
        methodBuilder.addJavadoc("$L", ServiceGenerators.getJavaDocWithRequestLine(endpointDef));

        TypeName returnType = returnTypeMapper.apply(endpointDef.getReturns());
        methodBuilder.returns(returnType);

        if (TypeName.get(InputStream.class).equals(returnType)) {
            methodBuilder.addAnnotation(MustBeClosed.class);
        }

        return methodBuilder.build();
    }
}
