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

import com.google.common.collect.ImmutableMap;
import com.palantir.conjure.java.Options;
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
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.Deserializer;
import com.palantir.dialogue.EndpointChannel;
import com.palantir.dialogue.EndpointChannelFactory;
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
import javax.lang.model.element.Modifier;

public final class AsyncGenerator implements StaticFactoryMethodGenerator {
    private static final String REQUEST = "_request";
    private static final String PLAIN_SER_DE = "_plainSerDe";

    private final Options options;
    private final TypeNameResolver typeNameResolver;
    private final ParameterTypeMapper parameterTypes;
    private final ReturnTypeMapper returnTypes;

    public AsyncGenerator(
            Options options,
            TypeNameResolver typeNameResolver,
            ParameterTypeMapper parameterTypes,
            ReturnTypeMapper returnTypes) {
        this.options = options;
        this.typeNameResolver = typeNameResolver;
        this.parameterTypes = parameterTypes;
        this.returnTypes = returnTypes;
    }

    @Override
    public MethodSpec generate(ServiceDefinition def) {
        TypeSpec.Builder impl =
                TypeSpec.anonymousClassBuilder("").addSuperinterface(Names.asyncClassName(def, options));

        impl.addField(FieldSpec.builder(PlainSerDe.class, PLAIN_SER_DE)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer(CodeBlock.of("$L.plainSerDe()", StaticFactoryMethodGenerator.RUNTIME))
                .build());

        def.getEndpoints().forEach(endpoint -> {
            endpoint.getArgs().stream()
                    .filter(arg -> arg.getParamType().accept(ParameterTypeVisitor.IS_BODY))
                    .findAny()
                    .flatMap(body -> serializer(endpoint.getEndpointName(), body.getType()))
                    .ifPresent(impl::addField);

            impl.addField(bindEndpointChannel(def, endpoint));
            deserializer(endpoint.getEndpointName(), endpoint.getReturns()).ifPresent(impl::addField);
            impl.addMethod(asyncClientImpl(def, endpoint));
        });

        impl.addMethod(BlockingGenerator.toStringMethod(Names.blockingClassName(def, options)));

        MethodSpec asyncImpl = MethodSpec.methodBuilder("of")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .addJavadoc(
                        "Creates an asynchronous/non-blocking client for a $L service.",
                        def.getServiceName().getName())
                .returns(Names.asyncClassName(def, options))
                .addParameter(EndpointChannelFactory.class, StaticFactoryMethodGenerator.ENDPOINT_CHANNEL_FACTORY)
                .addParameter(ConjureRuntime.class, StaticFactoryMethodGenerator.RUNTIME)
                .addCode(CodeBlock.builder().add("return $L;", impl.build()).build())
                .build();
        return asyncImpl;
    }

    private FieldSpec bindEndpointChannel(ServiceDefinition def, EndpointDefinition endpoint) {
        return FieldSpec.builder(ClassName.get(EndpointChannel.class), Names.endpointChannel(endpoint))
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer(
                        "$L.endpoint($T.$L)",
                        StaticFactoryMethodGenerator.ENDPOINT_CHANNEL_FACTORY,
                        Names.endpointsClassName(def, options),
                        endpoint.getEndpointName().get())
                .build();
    }

    private Optional<FieldSpec> serializer(EndpointName endpointName, Type type) {
        if (type.accept(TypeVisitor.IS_BINARY)) {
            return Optional.empty();
        }
        TypeName className = returnTypes.baseType(type).box();
        ParameterizedTypeName deserializerType = ParameterizedTypeName.get(ClassName.get(Serializer.class), className);
        return Optional.of(FieldSpec.builder(deserializerType, endpointName + "Serializer")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer(
                        "$L.bodySerDe().serializer(new $T<$T>() {})",
                        StaticFactoryMethodGenerator.RUNTIME,
                        TypeMarker.class,
                        className)
                .build());
    }

    private Optional<FieldSpec> deserializer(EndpointName endpointName, Optional<Type> type) {
        TypeName className = returnTypes.baseType(type).box();
        if (isBinaryOrOptionalBinary(className, returnTypes)) {
            return Optional.empty();
        }
        ParameterizedTypeName deserializerType =
                ParameterizedTypeName.get(ClassName.get(Deserializer.class), className);

        CodeBlock realDeserializer = CodeBlock.of("deserializer(new $T<$T>() {})", TypeMarker.class, className);
        CodeBlock voidDeserializer = CodeBlock.of("emptyBodyDeserializer()");
        CodeBlock initializer = CodeBlock.of(
                "$L.bodySerDe().$L",
                StaticFactoryMethodGenerator.RUNTIME,
                type.isPresent() ? realDeserializer : voidDeserializer);

        return Optional.of(FieldSpec.builder(deserializerType, endpointName + "Deserializer")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer(initializer)
                .build());
    }

    private static boolean isBinaryOrOptionalBinary(TypeName className, ReturnTypeMapper returnTypes) {
        return isBinary(className, returnTypes) || isOptionalBinary(className, returnTypes);
    }

    private static boolean isBinary(TypeName className, ReturnTypeMapper returnTypes) {
        return className.equals(returnTypes.baseType(Type.primitive(PrimitiveType.BINARY)));
    }

    private static boolean isOptionalBinary(TypeName className, ReturnTypeMapper returnTypes) {
        return className.equals(
                returnTypes.baseType(Type.optional(OptionalType.of(Type.primitive(PrimitiveType.BINARY)))));
    }

    private MethodSpec asyncClientImpl(ServiceDefinition serviceDefinition, EndpointDefinition def) {
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
                .add("$T $L = $T.builder();", Request.Builder.class, REQUEST, Request.class)
                .add(requestParams.build())
                .build();
        CodeBlock execute = CodeBlock.of(
                "return $L.clients().call($L, $L.build(), $L);",
                StaticFactoryMethodGenerator.RUNTIME,
                Names.endpointChannel(def),
                REQUEST,
                def.getReturns()
                        .filter(type -> isBinaryOrOptionalBinary(returnTypes.baseType(type), returnTypes))
                        .map(type -> StaticFactoryMethodGenerator.RUNTIME
                                + (isOptionalBinary(returnTypes.baseType(type), returnTypes)
                                        ? ".bodySerDe().optionalInputStreamDeserializer()"
                                        : ".bodySerDe().inputStreamDeserializer()"))
                        .orElseGet(() -> def.getEndpointName().get() + "Deserializer"));

        MethodSpec asyncClient = methodBuilder.addCode(request).addCode(execute).build();
        return asyncClient;
    }

    private CodeBlock generateParam(String endpointName, ArgumentDefinition param) {
        return param.getParamType().accept(new ParameterType.Visitor<CodeBlock>() {
            @Override
            public CodeBlock visitBody(BodyParameterType value) {
                if (parameterTypes
                        .baseType(param.getType())
                        .equals(parameterTypes.baseType(Type.primitive(PrimitiveType.BINARY)))) {
                    return CodeBlock.of(
                            "$L.body($L.bodySerDe().serialize($L));",
                            REQUEST,
                            StaticFactoryMethodGenerator.RUNTIME,
                            param.getArgName());
                }
                return CodeBlock.of("$L.body($LSerializer.serialize($L));", REQUEST, endpointName, param.getArgName());
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
                "putHeaderParams",
                value.getParamId().get(),
                CodeBlock.of(param.getArgName().get()),
                param.getType());
    }

    private CodeBlock generatePathParam(ArgumentDefinition param) {
        return generatePlainSerializer(
                "putPathParams",
                param.getArgName().get(),
                CodeBlock.of("$L", param.getArgName().get()),
                param.getType());
    }

    private CodeBlock generateQueryParam(ArgumentDefinition param, QueryParameterType value) {
        return generatePlainSerializer(
                "putQueryParams",
                value.getParamId().get(),
                CodeBlock.of(param.getArgName().get()),
                param.getType());
    }

    private CodeBlock generatePlainSerializer(String method, String key, CodeBlock argName, Type type) {
        return type.accept(new Type.Visitor<CodeBlock>() {
            @Override
            public CodeBlock visitPrimitive(PrimitiveType primitiveType) {
                return CodeBlock.of(
                        "$L.$L($S, $L.serialize$L($L));",
                        "_request",
                        method,
                        key,
                        PLAIN_SER_DE,
                        primitiveTypeName(primitiveType),
                        argName);
            }

            @Override
            public CodeBlock visitOptional(OptionalType optionalType) {

                return CodeBlock.builder()
                        .beginControlFlow("if ($L.isPresent())", argName)
                        .add(generatePlainSerializer(
                                method,
                                key,
                                CodeBlock.of("$L.$L()", argName, getOptionalAccessor(optionalType.getItemType())),
                                optionalType.getItemType()))
                        .endControlFlow()
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
                TypeDefinition typeDef = typeNameResolver.resolve(typeName);
                if (typeDef.accept(TypeDefinitionVisitor.IS_ALIAS)) {
                    return generatePlainSerializer(
                            method,
                            key,
                            CodeBlock.of("$L.get()", argName),
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
                CodeBlock elementVariable = CodeBlock.of("$LElement", argName);
                return CodeBlock.builder()
                        .beginControlFlow(
                                "for ($T $L : $L)", parameterTypes.baseType(itemType), elementVariable, argName)
                        .add(generatePlainSerializer(method, key, elementVariable, itemType))
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
                        "$L.putHeaderParams($S, $L.toString());",
                        REQUEST,
                        Auth.AUTH_HEADER_NAME,
                        Auth.AUTH_HEADER_PARAM_NAME);
            }

            @Override
            public CodeBlock visitCookie(CookieAuthType value) {
                return CodeBlock.of(
                        "$L.putHeaderParams($S, \"$L=\" + $L.serializeBearerToken($L));",
                        REQUEST,
                        "Cookie",
                        value.getCookieName(),
                        PLAIN_SER_DE,
                        Auth.COOKIE_AUTH_PARAM_NAME);
            }

            @Override
            public CodeBlock visitUnknown(String unknownType) {
                throw new SafeIllegalStateException("unknown auth type", SafeArg.of("type", unknownType));
            }
        });
    }

    private static String getOptionalAccessor(Type type) {
        if (type.accept(TypeVisitor.IS_PRIMITIVE)) {
            PrimitiveType primitive = type.accept(TypeVisitor.PRIMITIVE);
            if (primitive.equals(PrimitiveType.DOUBLE)) {
                return "getAsDouble";
            } else if (primitive.equals(PrimitiveType.INTEGER)) {
                return "getAsInt";
            }
        }
        return "get";
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
