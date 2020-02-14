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
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import com.palantir.conjure.java.services.Auth;
import com.palantir.conjure.java.visitor.DefaultTypeVisitor;
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
import com.palantir.conjure.visitor.ParameterTypeVisitor;
import com.palantir.conjure.visitor.TypeVisitor;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.Deserializer;
import com.palantir.dialogue.PlainSerDe;
import com.palantir.dialogue.Request;
import com.palantir.dialogue.Serializer;
import com.palantir.dialogue.TypeMarker;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
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

public final class AsyncGenerator {

    public static final String REQUEST = "_request";
    private final ParameterTypeMapper parameterTypes;
    private final ReturnTypeMapper returnTypes;

    public AsyncGenerator(ParameterTypeMapper parameterTypes, ReturnTypeMapper returnTypes) {
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
                .add("$T $L = $T.builder();", Request.Builder.class, REQUEST, Request.class)
                .add(requestParams.build())
                .build();
        CodeBlock execute = CodeBlock.builder()
                .add(
                        "channel.execute($T.$L, $L.build())",
                        serviceClassName,
                        def.getEndpointName().get(),
                        REQUEST)
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
                    return CodeBlock.of("$L.body(runtime.bodySerDe().serialize($L));", REQUEST, param.getArgName());
                }
                return CodeBlock.of("$L.body($LSerializer.serialize($L));", REQUEST, endpointName, param.getArgName());
            }

            @Override
            public CodeBlock visitHeader(HeaderParameterType value) {
                return generateHeaderParam(param, value);
            }

            @Override
            public CodeBlock visitPath(PathParameterType value) {
                return param.getType().accept(new DefaultTypeVisitor<CodeBlock>() {
                    @Override
                    public CodeBlock visitReference(com.palantir.conjure.spec.TypeName unused) {
                        return CodeBlock.of(
                                "$L.putPathParams($S, $L.toString());",
                                REQUEST,
                                param.getArgName(),
                                param.getArgName());
                    }

                    @Override
                    public CodeBlock visitDefault() {
                        return CodeBlock.of(
                                "$L.putPathParams($S, plainSerDe.serialize$L($L));",
                                REQUEST,
                                param.getArgName(),
                                param.getType().accept(PlainSerializer.INSTANCE),
                                param.getArgName());
                    }
                });
            }

            @Override
            public CodeBlock visitQuery(QueryParameterType value) {
                return param.getType().accept(new DefaultTypeVisitor<CodeBlock>() {
                    @Override
                    public CodeBlock visitOptional(OptionalType optionalType) {
                        return CodeBlock.of(
                                "$L.ifPresent(v -> $L.putQueryParams($S, $T.toString(v)));",
                                param.getArgName(),
                                REQUEST,
                                value.getParamId(),
                                Objects.class);
                    }

                    @Override
                    public CodeBlock visitReference(com.palantir.conjure.spec.TypeName unused) {
                        return CodeBlock.of(
                                "$L.putQueryParams($S, $L.toString());",
                                REQUEST,
                                value.getParamId(),
                                param.getArgName());
                    }

                    @Override
                    public CodeBlock visitList(ListType value) {
                        return visitCollection();
                    }

                    @Override
                    public CodeBlock visitSet(SetType value) {
                        return visitCollection();
                    }

                    private CodeBlock visitCollection() {
                        return CodeBlock.of(
                                "$L.putAllQueryParams($S, plainSerDe.serialize$L($L));",
                                REQUEST,
                                value.getParamId(),
                                param.getType().accept(PlainSerializer.INSTANCE),
                                param.getArgName());
                    }

                    @Override
                    public CodeBlock visitDefault() {
                        return CodeBlock.of(
                                "$L.putQueryParams($S, plainSerDe.serialize$L($L));",
                                REQUEST,
                                value.getParamId(),
                                param.getType().accept(PlainSerializer.INSTANCE),
                                param.getArgName());
                    }
                });
            }

            @Override
            public CodeBlock visitUnknown(String unknownType) {
                throw new UnsupportedOperationException("Unknown parameter type: " + unknownType);
            }
        });
    }

    private CodeBlock generateHeaderParam(ArgumentDefinition param, HeaderParameterType value) {
        return param.getType().accept(new DefaultTypeVisitor<CodeBlock>() {
            @Override
            public CodeBlock visitOptional(OptionalType optionalType) {
                return CodeBlock.of(
                        "$L.ifPresent(v -> $L.putHeaderParams($S, $T.toString(v)));",
                        param.getArgName(),
                        REQUEST,
                        value.getParamId(),
                        Objects.class);
            }

            @Override
            public CodeBlock visitReference(com.palantir.conjure.spec.TypeName unused) {
                // TODO(forozco): handle references correctly
                return CodeBlock.of(
                        "$L.putHeaderParams($S, $L.toString());", REQUEST, value.getParamId(), param.getArgName());
            }

            @Override
            public CodeBlock visitList(ListType unused) {
                return visitCollection();
            }

            @Override
            public CodeBlock visitSet(SetType unused) {
                return visitCollection();
            }

            private CodeBlock visitCollection() {
                return CodeBlock.of(
                        "$L.putAllHeaderParams($S, plainSerDe.serialize$L($L));",
                        REQUEST,
                        value.getParamId(),
                        param.getType().accept(PlainSerializer.INSTANCE),
                        param.getArgName());
            }

            @Override
            public CodeBlock visitDefault() {
                return CodeBlock.of(
                        "$L.putHeaderParams($S, plainSerDe.serialize$L($L));",
                        REQUEST,
                        value.getParamId(),
                        param.getType().accept(PlainSerializer.INSTANCE),
                        param.getArgName());
            }
        });
    }

    private static CodeBlock generateAuthHeader(AuthType auth) {
        return auth.accept(new AuthType.Visitor<CodeBlock>() {
            @Override
            public CodeBlock visitHeader(HeaderAuthType value) {
                return CodeBlock.of(
                        "$L.putHeaderParams($S, plainSerDe.serializeBearerToken($L.getBearerToken()));",
                        REQUEST,
                        Auth.AUTH_HEADER_NAME,
                        Auth.AUTH_HEADER_PARAM_NAME);
            }

            @Override
            public CodeBlock visitCookie(CookieAuthType value) {
                return CodeBlock.of(
                        "$L.putHeaderParam($S, \"$L=\" + planSerDe.serializeBearerToken($L));",
                        REQUEST,
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

    private static final class PlainSerializer extends DefaultTypeVisitor<String> {
        private static final PlainSerializer INSTANCE = new PlainSerializer();

        @Override
        public String visitPrimitive(PrimitiveType primitiveType) {
            return primitiveTypeName(primitiveType);
        }

        @Override
        public String visitOptional(OptionalType value) {
            throw new SafeIllegalArgumentException("Cannot serialize optional");
        }

        @Override
        public String visitList(ListType value) {
            return value.getItemType().accept(PlainSerializer.INSTANCE) + "List";
        }

        @Override
        public String visitSet(SetType value) {
            return value.getItemType().accept(PlainSerializer.INSTANCE) + "Set";
        }

        @Override
        public String visitMap(MapType value) {
            throw new SafeIllegalArgumentException("Cannot serialize map");
        }

        @Override
        public String visitReference(com.palantir.conjure.spec.TypeName value) {
            throw new SafeIllegalArgumentException("Cannot serialize reference");
        }

        @Override
        public String visitExternal(ExternalReference value) {
            throw new SafeIllegalArgumentException("Cannot serialize external");
        }

        @Override
        public String visitDefault() {
            throw new SafeIllegalArgumentException("Only primitive types and collections can be serialized");
        }
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
