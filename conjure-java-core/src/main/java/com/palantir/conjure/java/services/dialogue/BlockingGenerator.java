/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.services.dialogue;

import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.RemoteExceptions;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import java.util.List;
import javax.lang.model.element.Modifier;

public final class BlockingGenerator {

    private final ParameterTypeMapper parameterTypes;
    private final ReturnTypeMapper returnTypes;

    public BlockingGenerator(ParameterTypeMapper parameterTypes, ReturnTypeMapper returnTypes) {
        this.parameterTypes = parameterTypes;
        this.returnTypes = returnTypes;
    }

    public MethodSpec generate(ServiceDefinition def) {
        TypeSpec.Builder impl = TypeSpec.anonymousClassBuilder("").addSuperinterface(Names.blockingClassName(def));
        def.getEndpoints().forEach(endpoint -> impl.addMethod(blockingClientImpl(endpoint)));

        MethodSpec blockingImpl = MethodSpec.methodBuilder("blocking")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .addJavadoc(
                        "Creates a synchronous/blocking client for a $L service.",
                        def.getServiceName().getName())
                .returns(Names.blockingClassName(def))
                .addParameter(Channel.class, "channel")
                .addParameter(ConjureRuntime.class, "runtime")
                .addCode("$T delegate = async(channel, runtime);", Names.asyncClassName(def))
                .addCode(CodeBlock.builder().add("return $L;", impl.build()).build())
                .build();
        return blockingImpl;
    }

    private MethodSpec blockingClientImpl(EndpointDefinition def) {
        List<ParameterSpec> params = parameterTypes.methodParams(def);
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(
                        def.getEndpointName().get())
                .addModifiers(Modifier.PUBLIC)
                .addParameters(params)
                .addAnnotation(Override.class);
        methodBuilder.returns(returnTypes.baseType(def.getReturns()));

        CodeBlock argList =
                params.stream().map(argDef -> CodeBlock.of("$L", argDef.name)).collect(CodeBlock.joining(", "));

        methodBuilder
                .addCode(def.getReturns().isPresent() ? "return " : "")
                .addCode(
                        "$T.getUnchecked(delegate.$L($L));",
                        RemoteExceptions.class,
                        def.getEndpointName().get(),
                        argList);

        return methodBuilder.build();
    }
}
