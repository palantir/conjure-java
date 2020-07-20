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

import com.google.errorprone.annotations.MustBeClosed;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.EndpointChannelFactory;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.InputStream;
import java.util.List;
import javax.lang.model.element.Modifier;

public final class BlockingGenerator implements StaticFactoryMethodGenerator {

    private final Options options;
    private final ParameterTypeMapper parameterTypes;
    private final ReturnTypeMapper returnTypes;

    public BlockingGenerator(Options options, ParameterTypeMapper parameterTypes, ReturnTypeMapper returnTypes) {
        this.options = options;
        this.parameterTypes = parameterTypes;
        this.returnTypes = returnTypes;
    }

    @Override
    public MethodSpec generate(ServiceDefinition def) {
        TypeSpec.Builder impl =
                TypeSpec.anonymousClassBuilder("").addSuperinterface(Names.blockingClassName(def, options));
        def.getEndpoints().forEach(endpoint -> impl.addMethod(blockingClientImpl(endpoint)));

        impl.addMethod(toStringMethod(Names.blockingClassName(def, options)));

        MethodSpec blockingImpl = MethodSpec.methodBuilder("of")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .addJavadoc(
                        "Creates a synchronous/blocking client for a $L service.",
                        def.getServiceName().getName())
                .returns(Names.blockingClassName(def, options))
                .addParameter(EndpointChannelFactory.class, ENDPOINT_CHANNEL_FACTORY)
                .addParameter(ConjureRuntime.class, RUNTIME)
                .addCode(
                        "$T delegate = $T.of($L, $L);",
                        Names.asyncClassName(def, options),
                        Names.asyncClassName(def, options),
                        ENDPOINT_CHANNEL_FACTORY,
                        RUNTIME)
                .addCode(CodeBlock.builder().add("return $L;", impl.build()).build())
                .build();
        return blockingImpl;
    }

    static MethodSpec toStringMethod(ClassName className) {
        return MethodSpec.methodBuilder("toString")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addAnnotation(Override.class)
                .addCode(
                        "return \"$L{$L=\" + $L + \", runtime=\" + _runtime + '}';",
                        className.simpleName(),
                        StaticFactoryMethodGenerator.ENDPOINT_CHANNEL_FACTORY,
                        StaticFactoryMethodGenerator.ENDPOINT_CHANNEL_FACTORY)
                .build();
    }

    private MethodSpec blockingClientImpl(EndpointDefinition def) {
        List<ParameterSpec> params = parameterTypes.methodParams(def);
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(
                        def.getEndpointName().get())
                .addModifiers(Modifier.PUBLIC)
                .addParameters(params)
                .addAnnotation(Override.class);

        if (def.getDeprecated().isPresent()) {
            methodBuilder.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
                    .addMember("value", "$S", "deprecation")
                    .build());
        }

        TypeName returnType = returnTypes.baseType(def.getReturns());
        methodBuilder.returns(returnType);

        if (TypeName.get(InputStream.class).equals(returnType)) {
            methodBuilder.addAnnotation(MustBeClosed.class);
        }

        CodeBlock argList =
                params.stream().map(argDef -> CodeBlock.of("$L", argDef.name)).collect(CodeBlock.joining(", "));

        methodBuilder
                .addCode(def.getReturns().isPresent() ? "return " : "")
                .addCode(
                        "$L.clients().block(delegate.$L($L));",
                        RUNTIME,
                        def.getEndpointName().get(),
                        argList);

        return methodBuilder.build();
    }
}
