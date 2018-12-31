/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.services;

import com.google.common.collect.Iterables;
import com.google.common.reflect.TypeToken;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.FeatureFlags;
import com.palantir.conjure.java.types.CodeBlocks;
import com.palantir.conjure.java.types.TypeMapper;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.HandlerContext;
import com.palantir.conjure.java.undertow.lib.Routable;
import com.palantir.conjure.java.undertow.lib.RoutingRegistry;
import com.palantir.conjure.java.undertow.lib.SerializerRegistry;
import com.palantir.conjure.java.undertow.lib.internal.Auth;
import com.palantir.conjure.java.undertow.lib.internal.BinarySerializers;
import com.palantir.conjure.java.undertow.lib.internal.StringDeserializers;
import com.palantir.conjure.spec.ArgumentDefinition;
import com.palantir.conjure.spec.AuthType;
import com.palantir.conjure.spec.CookieAuthType;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.EndpointName;
import com.palantir.conjure.spec.HeaderAuthType;
import com.palantir.conjure.spec.ParameterType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.visitor.AuthTypeVisitor;
import com.palantir.conjure.visitor.ParameterTypeVisitor;
import com.palantir.conjure.visitor.TypeVisitor;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

final class UndertowServiceHandlerGenerator {

    private static final String EXCHANGE_VAR_NAME = "exchange";
    private static final String SERIALIZER_REGISTRY_VAR_NAME = "serializers";
    private static final String DELEGATE_VAR_NAME = "delegate";
    private static final String CONTEXT_VAR_NAME = "context";

    private static final String AUTH_HEADER_VAR_NAME = "authHeader";

    private static final String COOKIE_TOKEN_VAR_NAME = "cookieToken";

    private final Set<FeatureFlags> experimentalFeatures;

    UndertowServiceHandlerGenerator(Set<FeatureFlags> experimentalFeatures) {
        this.experimentalFeatures = experimentalFeatures;
    }

    public JavaFile generateServiceHandler(ServiceDefinition serviceDefinition, List<TypeDefinition> typeDefinitions,
            TypeMapper typeMapper, TypeMapper returnTypeMapper) {

        String serviceName = serviceDefinition.getServiceName().getName();
        // class name
        ClassName serviceClass = ClassName.get(serviceDefinition.getServiceName().getPackage(),
                (experimentalFeatures.contains(FeatureFlags.UndertowServicePrefix) ? "Undertow" : "")
                        + serviceDefinition.getServiceName().getName());
        TypeSpec.Builder routableBuilder = TypeSpec.classBuilder(serviceName + "Routable")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .addSuperinterface(Routable.class);

        // addFields
        routableBuilder.addField(serviceClass, DELEGATE_VAR_NAME, Modifier.PRIVATE, Modifier.FINAL);
        routableBuilder.addField(ClassName.get(SerializerRegistry.class), SERIALIZER_REGISTRY_VAR_NAME,
                Modifier.PRIVATE, Modifier.FINAL);
        // addConstructor
        routableBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(HandlerContext.class, CONTEXT_VAR_NAME)
                .addParameter(serviceClass, DELEGATE_VAR_NAME)
                .addStatement("this.$1N = $2N.serializerRegistry()", SERIALIZER_REGISTRY_VAR_NAME, CONTEXT_VAR_NAME)
                .addStatement("this.$1N = $1N", DELEGATE_VAR_NAME)
                .build());

        // implement Routable#register interface
        // TODO(nmiyake): check for path disjointness per https://palantir.quip.com/5VxNAIyYYvnZ. Eventually, this
        // should be enforced at the IR level -- once that is done, the generator will not need to perform any
        // validation as the proper endpoint uniqueness guarantees will be provided by the IR itself.
        CodeBlock routingHandler = CodeBlock.builder()
                .add(CodeBlocks.of(Iterables.transform(serviceDefinition.getEndpoints(),
                        e -> CodeBlock.of(".$1L($2S, $3L)",
                                e.getHttpMethod().toString().toLowerCase(),
                                e.getHttpPath(),
                                CodeBlock.of("new $1T()",
                                        endpointToHandlerType(serviceDefinition.getServiceName(), e.getEndpointName()))
                        ))))
                .build();
        routableBuilder.addMethod(MethodSpec.methodBuilder("register")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(RoutingRegistry.class, "routingRegistry")
                .addStatement("$1L$2L", "routingRegistry", routingHandler)
                .build());

        // addEndpointHandlers
        routableBuilder.addTypes(Iterables.transform(serviceDefinition.getEndpoints(),
                e -> generateEndpointHandler(e, typeDefinitions, typeMapper, returnTypeMapper)));

        TypeSpec routable = routableBuilder.build();

        ClassName routableFactoryType = ClassName.get(serviceDefinition.getServiceName().getPackage(),
                serviceDefinition.getServiceName().getName() + "Endpoint");

        ClassName routableName = ClassName.get(serviceDefinition.getServiceName().getPackage(),
                routableFactoryType.simpleName(), serviceName + "Routable");
        TypeSpec routableFactory = TypeSpec.classBuilder(routableFactoryType.simpleName())
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(UndertowServiceHandlerGenerator.class))
                .addSuperinterface(Endpoint.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(FieldSpec.builder(serviceClass, DELEGATE_VAR_NAME, Modifier.PRIVATE, Modifier.FINAL).build())
                .addMethod(MethodSpec.methodBuilder("of")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .addParameter(serviceClass, DELEGATE_VAR_NAME)
                        .addStatement("return new $T($N)", routableFactoryType, DELEGATE_VAR_NAME)
                        .returns(Endpoint.class)
                        .build())
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .addParameter(serviceClass, DELEGATE_VAR_NAME)
                        .addStatement("this.$1N = $1N", DELEGATE_VAR_NAME)
                        .build())
                .addMethod(MethodSpec.methodBuilder("create")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(HandlerContext.class, CONTEXT_VAR_NAME)
                        .returns(Routable.class)
                        .addStatement("return new $1T($2N, $3N)", routableName, CONTEXT_VAR_NAME, DELEGATE_VAR_NAME)
                        .build())

                .addType(routable)
                .build();

        return JavaFile.builder(serviceDefinition.getServiceName().getPackage(), routableFactory)
                .indent("    ")
                .build();
    }

    private TypeName endpointToHandlerType(com.palantir.conjure.spec.TypeName serviceName, EndpointName name) {
        return ClassName.get(serviceName.getPackage(),
                serviceName.getName() + "Endpoint", serviceName.getName() + "Routable",
                endpointToHandlerClassName(name));
    }

    private String endpointToHandlerClassName(EndpointName name) {
        return StringUtils.capitalize(name.get()) + "Handler";
    }

    private TypeSpec generateEndpointHandler(EndpointDefinition endpointDefinition,
            List<TypeDefinition> typeDefinitions,
            TypeMapper typeMapper,
            TypeMapper returnTypeMapper) {
        return TypeSpec.classBuilder(endpointToHandlerClassName(endpointDefinition.getEndpointName()))
                .addModifiers(Modifier.PRIVATE)
                .addSuperinterface(HttpHandler.class)
                .addFields(endpointDefinition.getArgs().stream()
                        .filter(def -> def.getParamType().accept(ParameterTypeVisitor.IS_BODY))
                        .map(def -> createTypeField(typeMapper, def))
                        .collect(Collectors.toList()))
                .addMethod(MethodSpec.methodBuilder("handleRequest")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(HttpServerExchange.class, EXCHANGE_VAR_NAME)
                        .addException(IOException.class)
                        .addCode(CodeBlock.builder()
                                .add(endpointInvocation(
                                        endpointDefinition, typeDefinitions, typeMapper, returnTypeMapper))
                                .build())
                        .build())
                .build();
    }

    private static FieldSpec createTypeField(TypeMapper typeMapper, ArgumentDefinition argument) {
        String name = argument.getArgName().get() + "Type";
        TypeName type = ParameterizedTypeName.get(
                ClassName.get(TypeToken.class), typeMapper.getClassName(argument.getType()));
        return FieldSpec.builder(type, name, Modifier.PRIVATE, Modifier.FINAL)
                .initializer("new $T() {}", type)
                .build();
    }

    private static final String PATH_PARAMS_VAR_NAME = "pathParams";
    private static final String QUERY_PARAMS_VAR_NAME = "queryParams";
    private static final String HEADER_PARAMS_VAR_NAME = "headerParams";

    private CodeBlock endpointInvocation(EndpointDefinition endpointDefinition, List<TypeDefinition> typeDefinitions,
            TypeMapper typeMapper, TypeMapper returnTypeMapper) {
        CodeBlock.Builder code = CodeBlock.builder();

        // auth code
        Optional<String> authVarName = addAuthCode(code, endpointDefinition);

        // body parameter
        getBodyParamTypeArgument(endpointDefinition.getArgs()).ifPresent(bodyParam -> {
            if (bodyParam.getType().accept(TypeVisitor.IS_BINARY)) {
                // TODO(ckozak): Support aliased and optional binary types
                code.addStatement("$1T $2N = $3T.deserializeInputStream($4N)",
                        InputStream.class, bodyParam.getArgName().get(), BinarySerializers.class, EXCHANGE_VAR_NAME);
            } else {
                code.addStatement("$1T $2N = $3N.deserialize($4N, $5N)",
                        typeMapper.getClassName(bodyParam.getType()).box(),
                        bodyParam.getArgName().get(),
                        SERIALIZER_REGISTRY_VAR_NAME,
                        bodyParam.getArgName().get() + "Type",
                        EXCHANGE_VAR_NAME);
            }
        });

        // path parameters
        addPathParamsCode(code, endpointDefinition, typeDefinitions, typeMapper);

        // header parameters
        addHeaderParamsCode(code, endpointDefinition, typeDefinitions, typeMapper);

        // query parameters
        addQueryParamsCode(code, endpointDefinition, typeDefinitions, typeMapper);

        List<String> methodArgs = new ArrayList<>();
        authVarName.ifPresent(name -> methodArgs.add(name));
        methodArgs.addAll(UndertowServiceGenerator.sortArgumentDefinitions(
                endpointDefinition.getArgs()).stream().map(
                    arg -> arg.getArgName().get()).collect(Collectors.toList()));

        final String resultVarName = "result";
        if (endpointDefinition.getReturns().isPresent()) {
            Type returnType = endpointDefinition.getReturns().get();
            code.addStatement("$1T $2N = $3N.$4L($5L)",
                    returnTypeMapper.getClassName(returnType),
                    resultVarName,
                    DELEGATE_VAR_NAME,
                    endpointDefinition.getEndpointName(),
                    String.join(", ", methodArgs)
            );

            // optional<> handling
            // TODO(ckozak): Support aliased binary types
            if (UndertowTypeFunctions.toConjureTypeWithoutAliases(returnType, typeDefinitions)
                    .accept(TypeVisitor.IS_OPTIONAL)) {
                CodeBlock serializer = UndertowTypeFunctions.isOptionalBinary(returnType)
                        ? CodeBlock.builder().add("$1T.serialize($2N.get(), $3N)",
                                BinarySerializers.class, resultVarName, EXCHANGE_VAR_NAME).build()
                        : CodeBlock.builder().add("$1N.serialize($2N, $3N)",
                                SERIALIZER_REGISTRY_VAR_NAME, resultVarName, EXCHANGE_VAR_NAME).build();
                // For optional<>: set response code to 204/NO_CONTENT if result is absent
                code.add(
                        CodeBlock.builder()
                                .beginControlFlow("if ($1L)",
                                        createIsOptionalPresentCall(returnType, resultVarName, typeDefinitions))
                                .addStatement(serializer)
                                .nextControlFlow("else")
                                .addStatement("$1N.setStatusCode(204)", EXCHANGE_VAR_NAME)
                                .endControlFlow()
                                .build());
            } else {
                if (returnType.accept(TypeVisitor.IS_BINARY)) {
                    code.addStatement("$1T.serialize($2N, $3N)",
                            BinarySerializers.class, resultVarName, EXCHANGE_VAR_NAME);
                } else {
                    code.addStatement("$1N.serialize($2N, $3N)",
                            SERIALIZER_REGISTRY_VAR_NAME, resultVarName, EXCHANGE_VAR_NAME);
                }
            }
        } else {
            code.addStatement("$1N.$2L($3L)",
                    DELEGATE_VAR_NAME,
                    endpointDefinition.getEndpointName(),
                    String.join(", ", methodArgs));
            // Set 204 response code for void methods
            code.addStatement("$1N.setStatusCode(204)", EXCHANGE_VAR_NAME);
        }
        return code.build();
    }

    // Adds code for authorization. Returns an optional that contains the name of the variable that contains the
    // deserialized optional parameter.
    private Optional<String> addAuthCode(
            CodeBlock.Builder code,
            EndpointDefinition endpointDefinition
    ) {
        if (!endpointDefinition.getAuth().isPresent()) {
            return Optional.empty();
        }


        return endpointDefinition.getAuth().get().accept(new AuthType.Visitor<Optional<String>>() {
            @Override
            public Optional<String> visitHeader(HeaderAuthType value) {
                // header auth
                code.addStatement("$1T $2N = $3T.header($4N)",
                        AuthHeader.class, AUTH_HEADER_VAR_NAME, Auth.class, EXCHANGE_VAR_NAME);
                return Optional.of(AUTH_HEADER_VAR_NAME);
            }

            @Override
            public Optional<String> visitCookie(CookieAuthType value) {
                code.addStatement("$1T $2N = $3T.cookie($4N, $5S)",
                        BearerToken.class,
                        COOKIE_TOKEN_VAR_NAME,
                        Auth.class,
                        EXCHANGE_VAR_NAME,
                        endpointDefinition.getAuth().get().accept(AuthTypeVisitor.COOKIE).getCookieName());
                return Optional.of(COOKIE_TOKEN_VAR_NAME);
            }

            @Override
            public Optional<String> visitUnknown(String unknownType) {
                throw new IllegalStateException("unknown auth type: " + unknownType);
            }
        });
    }

    private void addPathParamsCode(
            CodeBlock.Builder code,
            EndpointDefinition endpointDefinition,
            List<TypeDefinition> typeDefinitions,
            TypeMapper typeMapper
    ) {
        if (hasPathArgument(endpointDefinition.getArgs())) {
            code.addStatement("$1T<$2T, $2T> $3N = $4N.getAttachment($5T.ATTACHMENT_KEY).getParameters()",
                    Map.class, String.class, PATH_PARAMS_VAR_NAME, EXCHANGE_VAR_NAME,
                    io.undertow.util.PathTemplateMatch.class);
            code.add(
                    generatePathParameterCodeBlock(endpointDefinition.getArgs().stream(), typeDefinitions, typeMapper));
        }
    }

    private void addHeaderParamsCode(
            CodeBlock.Builder code,
            EndpointDefinition endpointDefinition,
            List<TypeDefinition> typeDefinitions,
            TypeMapper typeMapper
    ) {
        if (hasHeaderArgument(endpointDefinition.getArgs())) {
            code.addStatement("$1T $2N = $3N.getRequestHeaders()", io.undertow.util.HeaderMap.class,
                    HEADER_PARAMS_VAR_NAME,
                    EXCHANGE_VAR_NAME);
            code.add(generateHeaderParameterCodeBlock(endpointDefinition.getArgs().stream(), typeDefinitions,
                    typeMapper));
        }
    }

    private void addQueryParamsCode(
            CodeBlock.Builder code,
            EndpointDefinition endpointDefinition,
            List<TypeDefinition> typeDefinitions,
            TypeMapper typeMapper
    ) {
        if (hasQueryArgument(endpointDefinition.getArgs())) {
            code.addStatement("$1T $2N = $3N.getQueryParameters()",
                    ParameterizedTypeName.get(ClassName.get(Map.class), TypeName.get(String.class),
                            ParameterizedTypeName.get(Deque.class, String.class)), QUERY_PARAMS_VAR_NAME,
                    EXCHANGE_VAR_NAME);
            code.add(generateQueryParameterCodeBlock(endpointDefinition.getArgs().stream(), typeDefinitions,
                    typeMapper));
        }
    }

    private boolean hasPathArgument(List<ArgumentDefinition> args) {
        return Iterables.any(args, arg -> arg.getParamType().accept(ParameterTypeVisitor.IS_PATH));
    }

    private boolean hasQueryArgument(List<ArgumentDefinition> args) {
        return Iterables.any(args, arg -> arg.getParamType().accept(ParameterTypeVisitor.IS_QUERY));
    }

    private boolean hasHeaderArgument(List<ArgumentDefinition> args) {
        return Iterables.any(args, arg -> arg.getParamType().accept(ParameterTypeVisitor.IS_HEADER));
    }

    private CodeBlock generatePathParameterCodeBlock(Stream<ArgumentDefinition> params,
            List<TypeDefinition> typeDefinitions,
            TypeMapper typeMapper) {
        return generateParameterCodeBlock(
                params,
                ParameterTypeVisitor.IS_PATH,
                PATH_PARAMS_VAR_NAME,
                arg -> arg.getArgName().get(),
                typeDefinitions,
                typeMapper);
    }

    private CodeBlock generateQueryParameterCodeBlock(Stream<ArgumentDefinition> params,
            List<TypeDefinition> typeDefinitions,
            TypeMapper typeMapper) {
        return generateParameterCodeBlock(
                params,
                ParameterTypeVisitor.IS_QUERY,
                QUERY_PARAMS_VAR_NAME,
                arg -> arg.getParamType().accept(ParameterTypeVisitor.QUERY).getParamId().get(),
                typeDefinitions,
                typeMapper);
    }

    private CodeBlock generateHeaderParameterCodeBlock(Stream<ArgumentDefinition> params,
            List<TypeDefinition> typeDefinitions, TypeMapper typeMapper) {
        return generateParameterCodeBlock(
                params,
                ParameterTypeVisitor.IS_HEADER,
                HEADER_PARAMS_VAR_NAME,
                arg -> arg.getParamType().accept(ParameterTypeVisitor.HEADER).getParamId().get(), typeDefinitions,
                typeMapper);
    }

    private CodeBlock generateParameterCodeBlock(Stream<ArgumentDefinition> params,
            ParameterType.Visitor<Boolean> paramTypeVisitor,
            String paramsVarName,
            Function<ArgumentDefinition, String> toParamId,
            List<TypeDefinition> typeDefinitions,
            TypeMapper typeMapper) {
        return CodeBlocks.of(params.filter(param -> param.getParamType().accept(paramTypeVisitor)).map(
                arg -> {
                    Type normalizedType = UndertowTypeFunctions.toConjureTypeWithoutAliases(arg.getType(),
                            typeDefinitions);
                    if (normalizedType.equals(arg.getType())) {
                        // type does not contain any aliases
                        return decodePlainParameterCodeBlock(normalizedType, typeMapper, arg.getArgName().get(),
                                paramsVarName,
                                toParamId.apply(arg));
                    } else {
                        // type contains aliases: decode raw value and then construct real value from raw one
                        String rawVarName = arg.getArgName().get() + "Raw";
                        return CodeBlocks.of(
                                decodePlainParameterCodeBlock(normalizedType, typeMapper, rawVarName,
                                        paramsVarName,
                                        toParamId.apply(arg)),
                                CodeBlocks.statement(
                                        "$1T $2N = $3L",
                                        typeMapper.getClassName(arg.getType()),
                                        arg.getArgName().get(),
                                        createConstructorForTypeWithReference(arg.getType(), rawVarName,
                                                typeDefinitions, typeMapper)
                                )
                        );
                    }
                }).collect(Collectors.toList()));
    }

    private CodeBlock decodePlainParameterCodeBlock(Type type, TypeMapper typeMapper, String resultVarName,
            String paramsVarName, String paramId) {
        if (type.accept(TypeVisitor.IS_EXTERNAL)) {
            return CodeBlocks.statement(
                    "$1T $2N = $3T.valueOf($4T.deserializeString($5N.get($6S)))",
                    typeMapper.getClassName(type),
                    resultVarName,
                    typeMapper.getClassName(type),
                    StringDeserializers.class,
                    paramsVarName,
                    paramId
            );
        }
        if (type.accept(TypeVisitor.IS_OPTIONAL)
                && type.accept(TypeVisitor.OPTIONAL).getItemType().accept(TypeVisitor.IS_EXTERNAL)) {
            return CodeBlocks.statement(
                    "$1T $2N = $4T.deserializeOptionalString($5N.get($6S)).map($3T::valueOf)",
                    typeMapper.getClassName(type),
                    resultVarName,
                    typeMapper.getClassName(type.accept(TypeVisitor.OPTIONAL).getItemType()),
                    StringDeserializers.class,
                    paramsVarName,
                    paramId
            );
        }
        return CodeBlocks.statement(
                "$1T $2N = $3T.$4L($5N.get($6S))",
                typeMapper.getClassName(type),
                resultVarName,
                ClassName.get(StringDeserializers.class),
                deserializeFunctionName(type),
                paramsVarName,
                paramId
        );
    }

    /**
     * Returns a CodeBlock that, when run, returns a boolean indicating whether or not the optional represented by the
     * provided type is empty. inType must be a type that is either an optional or an alias that resolves to an
     * optional.
     */
    private static CodeBlock createIsOptionalPresentCall(Type inType, String varName,
            List<TypeDefinition> typeDefinitions) {
        if (inType.accept(TypeVisitor.IS_OPTIONAL)) {
            // current type is optional type: call isPresent
            return CodeBlock.of("$1N.isPresent()", varName);
        } else if (UndertowTypeFunctions.isAliasType(inType)) {
            // current type is an alias type: call "get()" to resolve alias and generate recursively on aliased type
            Type aliasedType = UndertowTypeFunctions.getAliasedType(inType, typeDefinitions);
            return createIsOptionalPresentCall(aliasedType, varName + ".get()", typeDefinitions);
        } else {
            throw new IllegalArgumentException("inType must be either an optional or alias type, was " + inType);
        }
    }

    /*
     * Returns a CodeBlock that constructs the given type given the provided variable that holds the decoded value for
     * the type. inType must be a type that resolves to (primitive|optional<primitive>) and must use an alias type
     * somewhere in its type definition. This means that inType must be one of the following:
     * - optional<alias that resolves to a primitive>
     * - alias of a primitive
     * - alias of an optional<primitive>
     * - alias of an optional<alias that resolves to a primitive>
     * - alias of an alias that follows one of these rules (recursive definition)
     *
     * An "alias that resolves to a primitive" is either an alias of a primitive or an alias of an alias that follows
     * these rules (recursive definition).
     */
    private static CodeBlock createConstructorForTypeWithReference(Type inType, String decodedVarName,
            List<TypeDefinition> typeDefinitions, TypeMapper typeMapper) {
        // "in" must be 1 of 2 types: optional<alias that resolves to a primitive> or alias
        if (inType.accept(TypeVisitor.IS_OPTIONAL)) {
            // optional<alias that resolves to a primitive>
            Type typeOfOptional = inType.accept(TypeVisitor.OPTIONAL).getItemType();
            return CodeBlock.of("$1T.ofNullable($2N.isPresent() ? $3L : null)", Optional.class, decodedVarName,
                    createConstructorForTypeWithReference(typeOfOptional, decodedVarName + ".get()", typeDefinitions,
                            typeMapper));
        } else {
            // alias
            CodeBlock ofContent;

            // alias must resolve to one of:
            //   * primitive
            //   * optional<primitive>
            //   * optional<alias that resolves to a primitive>
            //   * alias that follows one of these rules (recursive definition)
            Type aliasedType = UndertowTypeFunctions.getAliasedType(inType, typeDefinitions);
            if (aliasedType.accept(TypeVisitor.IS_PRIMITIVE)) {
                // primitive
                ofContent = CodeBlock.of("$1N", decodedVarName);
            } else if (aliasedType.accept(TypeVisitor.IS_OPTIONAL)) {
                // optional<primitive> or optional<alias that resolves to primitive>
                Type optionalType = aliasedType.accept(TypeVisitor.OPTIONAL).getItemType();
                if (optionalType.accept(TypeVisitor.IS_PRIMITIVE)) {
                    // optional<primitive>

                    // can use decoded var directly because it will already be the proper type (OptionalInt,
                    // OptionalDouble, or Optional<Primitive>)
                    ofContent = CodeBlock.of("$1N", decodedVarName);
                } else {
                    // optional<alias that resolves to primitive>
                    ofContent = createConstructorForTypeWithReference(aliasedType, decodedVarName, typeDefinitions,
                            typeMapper);
                }
            } else {
                // alias
                ofContent = createConstructorForTypeWithReference(aliasedType, decodedVarName, typeDefinitions,
                        typeMapper);
            }
            return CodeBlock.of("$1T.of($2L)", typeMapper.getClassName(inType), ofContent);
        }
    }

    private static String deserializeFunctionName(Type type) {
        if (type.accept(TypeVisitor.IS_PRIMITIVE)) {
            return "deserialize" + UndertowTypeFunctions.primitiveTypeName(
                    type.accept(UndertowTypeFunctions.PRIMITIVE_VISITOR));
        } else if (type.accept(TypeVisitor.IS_OPTIONAL)
                && type.accept(TypeVisitor.OPTIONAL).getItemType().accept(TypeVisitor.IS_PRIMITIVE)) {
            PrimitiveType innerPrimitiveType = type.accept(UndertowTypeFunctions.OPTIONAL_VISITOR).getItemType().accept(
                    UndertowTypeFunctions.PRIMITIVE_VISITOR);
            return "deserializeOptional" + UndertowTypeFunctions.primitiveTypeName(innerPrimitiveType);
        } else if (type.accept(TypeVisitor.IS_LIST)
                && type.accept(TypeVisitor.LIST).getItemType().accept(TypeVisitor.IS_PRIMITIVE)) {
            Type subtype = type.accept(TypeVisitor.LIST).getItemType();
            return deserializeFunctionName(subtype) + "List";
        } else if (type.accept(TypeVisitor.IS_SET)
                && type.accept(TypeVisitor.SET).getItemType().accept(TypeVisitor.IS_PRIMITIVE)) {
            Type subtype = type.accept(TypeVisitor.SET).getItemType();
            return deserializeFunctionName(subtype) + "Set";
        } else {
            throw new IllegalStateException("unknown type: " + type);
        }
    }

    private Optional<ArgumentDefinition> getBodyParamTypeArgument(List<ArgumentDefinition> args) {
        List<ArgumentDefinition> bodyArgs = args.stream().filter(
                arg -> arg.getParamType().accept(ParameterTypeVisitor.IS_BODY)).collect(Collectors.toList());
        if (bodyArgs.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Iterables.getOnlyElement(bodyArgs));
    }
}
