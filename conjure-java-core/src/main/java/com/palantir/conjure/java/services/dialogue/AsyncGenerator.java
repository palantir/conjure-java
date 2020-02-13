/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.services.dialogue;

import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import com.palantir.conjure.java.services.Auth;
import com.palantir.conjure.spec.ArgumentDefinition;
import com.palantir.conjure.spec.AuthType;
import com.palantir.conjure.spec.BodyParameterType;
import com.palantir.conjure.spec.CookieAuthType;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.EndpointName;
import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.HeaderAuthType;
import com.palantir.conjure.spec.HeaderParameterType;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.ParameterType;
import com.palantir.conjure.spec.PathParameterType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.QueryParameterType;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.visitor.ParameterTypeVisitor;
import com.palantir.conjure.visitor.TypeDefinitionVisitor;
import com.palantir.conjure.visitor.TypeVisitor;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.Deserializer;
import com.palantir.dialogue.PlainSerDe;
import com.palantir.dialogue.Request;
import com.palantir.dialogue.Serializer;
import com.palantir.dialogue.TypeMarker;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import javax.lang.model.element.Modifier;

public final class AsyncGenerator {

    private final Function<com.palantir.conjure.spec.TypeName, TypeDefinition> typeNameResolver;
    private final ParameterTypeMapper parameterTypes;
    private final ReturnTypeMapper returnTypes;

    public AsyncGenerator(
            Function<com.palantir.conjure.spec.TypeName, TypeDefinition> typeNameResolver,
            ParameterTypeMapper parameterTypes,
            ReturnTypeMapper returnTypes) {
        this.typeNameResolver = typeNameResolver;
        this.parameterTypes = parameterTypes;
        this.returnTypes = returnTypes;
    }

    public MethodSpec generate(ClassName serviceClassName, ServiceDefinition def) {
        TypeSpec.Builder impl = TypeSpec.anonymousClassBuilder("").addSuperinterface(Names.asyncClassName(def));

        impl.addField(FieldSpec.builder(PlainSerDe.class, "plainSerDe")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer("runtime.plainSerDe()")
                .build());

        def.getEndpoints().forEach(endpoint -> {
            endpoint.getArgs().stream()
                    .filter(arg -> arg.getParamType().accept(ParameterTypeVisitor.IS_BODY))
                    .findAny()
                    .flatMap(body -> serializer(endpoint.getEndpointName(), body.getType()))
                    .ifPresent(impl::addField);

            deserializer(endpoint.getEndpointName(), endpoint.getReturns()).ifPresent(impl::addField);
            impl.addMethod(asyncClientImpl(serviceClassName, endpoint));
        });

        MethodSpec asyncImpl = MethodSpec.methodBuilder("async")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .addJavadoc(
                        "Creates an asynchronous/non-blocking client for a $L service.",
                        def.getServiceName().getName())
                .returns(Names.asyncClassName(def))
                .addParameter(Channel.class, "channel")
                .addParameter(ConjureRuntime.class, "runtime")
                .addCode(CodeBlock.builder().add("return $L;", impl.build()).build())
                .build();
        return asyncImpl;
    }

    private Optional<FieldSpec> serializer(EndpointName endpointName, Type type) {
        if (type.accept(TypeVisitor.IS_BINARY)) {
            return Optional.empty();
        }
        TypeName className = returnTypes.baseType(type).box();
        ParameterizedTypeName deserializerType = ParameterizedTypeName.get(ClassName.get(Serializer.class), className);
        return Optional.of(FieldSpec.builder(deserializerType, endpointName + "Serializer")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer("runtime.bodySerDe().serializer(new $T<$T>() {})", TypeMarker.class, className)
                .build());
    }

    private Optional<FieldSpec> deserializer(EndpointName endpointName, Optional<Type> type) {
        // TODO(forozco): handle optional binary ??
        if (type.map(t -> t.accept(TypeVisitor.IS_BINARY)).orElse(false)) {
            return Optional.empty();
        }

        TypeName className = returnTypes.baseType(type).box();
        ParameterizedTypeName deserializerType =
                ParameterizedTypeName.get(ClassName.get(Deserializer.class), className);

        CodeBlock realDeserializer = CodeBlock.of(".deserializer(new $T<$T>() {})", TypeMarker.class, className);
        CodeBlock voidDeserializer = CodeBlock.of(".emptyBodyDeserializer()");
        CodeBlock initializer = CodeBlock.builder()
                .add("runtime.bodySerDe()")
                .add(type.isPresent() ? realDeserializer : voidDeserializer)
                .build();

        return Optional.of(FieldSpec.builder(deserializerType, endpointName + "Deserializer")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer(initializer)
                .build());
    }

    private MethodSpec asyncClientImpl(ClassName serviceClassName, EndpointDefinition def) {
        List<ParameterSpec> params = parameterTypes.methodParams(def);
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(
                        def.getEndpointName().get())
                .addModifiers(Modifier.PUBLIC)
                .addParameters(params)
                .addAnnotation(Override.class);

        methodBuilder.returns(returnTypes.async(def.getReturns()));

        CodeBlock.Builder requestParams = CodeBlock.builder();
        def.getAuth().map(AsyncGenerator::generateAuthHeader).ifPresent(requestParams::add);

        def.getArgs().stream()
                .map(param -> generateParam(def.getEndpointName().get(), param))
                .forEach(requestParams::add);

        CodeBlock request = CodeBlock.builder()
                .add("$T $L = $T.builder();", Request.Builder.class, "_request", Request.class)
                .add(requestParams.build())
                .build();
        CodeBlock execute = CodeBlock.builder()
                .add(
                        "channel.execute($T.$L, $L.build())",
                        serviceClassName,
                        def.getEndpointName().get(),
                        "_request")
                .build();
        CodeBlock transformed = CodeBlock.builder()
                .add(
                        "return $T.transform($L, $L, $T.directExecutor());",
                        Futures.class,
                        execute,
                        def.getReturns()
                                .filter(type -> type.accept(TypeVisitor.IS_BINARY))
                                .map(type -> "runtime" + ".bodySerDe()::deserializeInputStream")
                                .orElseGet(() -> def.getEndpointName().get() + "Deserializer::deserialize"),
                        MoreExecutors.class)
                .build();

        MethodSpec asyncClient =
                methodBuilder.addCode(request).addCode(transformed).build();
        return asyncClient;
    }

    private CodeBlock generateParam(String endpointName, ArgumentDefinition param) {
        return param.getParamType().accept(new ParameterType.Visitor<CodeBlock>() {
            @Override
            public CodeBlock visitBody(BodyParameterType value) {
                if (param.getType().accept(TypeVisitor.IS_BINARY)) {
                    return CodeBlock.of("$L.body(runtime.bodySerDe().serialize($L));", "_request", param.getArgName());
                }
                return CodeBlock.of(
                        "$L.body($LSerializer.serialize($L));", "_request", endpointName, param.getArgName());
            }

            @Override
            public CodeBlock visitHeader(HeaderParameterType value) {
                return generateHeaderParam(param, value);
            }

            @Override
            public CodeBlock visitPath(PathParameterType value) {
                return generatePathParam(param);
            }

            @Override
            public CodeBlock visitQuery(QueryParameterType value) {
                return generateQueryParam(param, value);
            }

            @Override
            public CodeBlock visitUnknown(String unknownType) {
                throw new UnsupportedOperationException("Unknown parameter type: " + unknownType);
            }
        });
    }

    private CodeBlock generateHeaderParam(ArgumentDefinition param, HeaderParameterType value) {
        return generatePlainSerializer(
                "putHeaderParams", value.getParamId().get(), param.getArgName().get(), param.getType());
    }

    private CodeBlock generatePathParam(ArgumentDefinition param) {
        return generatePlainSerializer(
                "putPathParams", param.getArgName().get(), param.getArgName().get(), param.getType());
    }

    private CodeBlock generateQueryParam(ArgumentDefinition param, QueryParameterType value) {
        return generatePlainSerializer(
                "putQueryParams", value.getParamId().get(), param.getArgName().get(), param.getType());
    }

    private CodeBlock generatePlainSerializer(String method, String key, String argName, Type type) {
        return type.accept(new Type.Visitor<CodeBlock>() {
            @Override
            public CodeBlock visitPrimitive(PrimitiveType primitiveType) {
                return CodeBlock.of(
                        "$L.$L($S, plainSerDe.serialize$L($L));",
                        "_request",
                        method,
                        key,
                        primitiveTypeName(primitiveType),
                        argName);
            }

            @Override
            public CodeBlock visitOptional(OptionalType optionalType) {
                return CodeBlock.builder()
                        .add("$L.ifPresent(v -> {\n", argName)
                        .add(generatePlainSerializer(method, key, "v", optionalType.getItemType()))
                        .add("\n});")
                        .build();
            }

            @Override
            public CodeBlock visitList(ListType value) {
                return visitCollection(value.getItemType());
            }

            @Override
            public CodeBlock visitSet(SetType value) {
                return visitCollection(value.getItemType());
            }

            @Override
            public CodeBlock visitMap(MapType value) {
                throw new SafeIllegalStateException("Maps can not be query parameters");
            }

            @Override
            public CodeBlock visitReference(com.palantir.conjure.spec.TypeName typeName) {
                TypeDefinition typeDef = typeNameResolver.apply(typeName);
                if (typeDef.accept(TypeDefinitionVisitor.IS_ALIAS)) {
                    return generatePlainSerializer(
                            method,
                            key,
                            argName + ".get()",
                            typeDef.accept(TypeDefinitionVisitor.ALIAS).getAlias());
                } else if (typeDef.accept(TypeDefinitionVisitor.IS_ENUM)) {
                    return CodeBlock.of("$L.$L($S, $T.toString($L));", "_request", method, key, Objects.class, argName);
                }
                throw new IllegalStateException("Plain serialization can only be aliases and enums");
            }

            @Override
            public CodeBlock visitExternal(ExternalReference value) {
                // TODO(forozco): we could probably do something smarter than just calling toString
                return CodeBlock.of("$L.$L($S, $T.toString($L));", "_request", method, key, Objects.class, argName);
            }

            @Override
            public CodeBlock visitUnknown(String unknownType) {
                throw new SafeIllegalStateException("Unknown param type", SafeArg.of("type", unknownType));
            }

            private CodeBlock visitCollection(Type itemType) {
                return CodeBlock.builder()
                        .beginControlFlow("for ($T v : $L)", parameterTypes.baseType(itemType), argName)
                        .add(generatePlainSerializer(method, key, "v", itemType))
                        .endControlFlow()
                        .build();
            }
        });
    }

    private static CodeBlock generateAuthHeader(AuthType auth) {
        return auth.accept(new AuthType.Visitor<CodeBlock>() {
            @Override
            public CodeBlock visitHeader(HeaderAuthType value) {
                return CodeBlock.of(
                        "$L.putHeaderParams($S, plainSerDe.serializeBearerToken($L.getBearerToken()));",
                        "_request",
                        Auth.AUTH_HEADER_NAME,
                        Auth.AUTH_HEADER_PARAM_NAME);
            }

            @Override
            public CodeBlock visitCookie(CookieAuthType value) {
                return CodeBlock.of(
                        "$L.putHeaderParam($S, \"$L=\" + planSerDe.serializeBearerToken($L));",
                        "_request",
                        "Cookie",
                        value.getCookieName(),
                        Auth.COOKIE_AUTH_PARAM_NAME);
            }

            @Override
            public CodeBlock visitUnknown(String unknownType) {
                throw new SafeIllegalStateException("unknown auth type", SafeArg.of("type", unknownType));
            }
        });
    }

    private static final ImmutableMap<PrimitiveType.Value, String> PRIMITIVE_TO_TYPE_NAME = new ImmutableMap.Builder<
                    PrimitiveType.Value, String>()
            .put(PrimitiveType.Value.BEARERTOKEN, "BearerToken")
            .put(PrimitiveType.Value.BOOLEAN, "Boolean")
            .put(PrimitiveType.Value.DATETIME, "DateTime")
            .put(PrimitiveType.Value.DOUBLE, "Double")
            .put(PrimitiveType.Value.INTEGER, "Integer")
            .put(PrimitiveType.Value.RID, "Rid")
            .put(PrimitiveType.Value.SAFELONG, "SafeLong")
            .put(PrimitiveType.Value.STRING, "String")
            .put(PrimitiveType.Value.UUID, "Uuid")
            .build();

    private static String primitiveTypeName(PrimitiveType in) {
        String typeName = PRIMITIVE_TO_TYPE_NAME.get(in.get());
        if (typeName == null) {
            throw new IllegalStateException("unrecognized primitive type: " + in);
        }
        return typeName;
    }
}
