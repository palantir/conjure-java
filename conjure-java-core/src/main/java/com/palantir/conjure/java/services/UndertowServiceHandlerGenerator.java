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

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.MoreCollectors;
import com.google.common.collect.Streams;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.ConjureTags;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.types.CodeBlocks;
import com.palantir.conjure.java.types.TypeMapper;
import com.palantir.conjure.java.undertow.lib.Deserializer;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.ReturnValueWriter;
import com.palantir.conjure.java.undertow.lib.Serializer;
import com.palantir.conjure.java.undertow.lib.TypeMarker;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import com.palantir.conjure.java.util.JavaNameSanitizer;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.java.util.ParameterOrder;
import com.palantir.conjure.java.util.TypeFunctions;
import com.palantir.conjure.java.visitor.DefaultTypeVisitor;
import com.palantir.conjure.java.visitor.MoreVisitors;
import com.palantir.conjure.spec.ArgumentDefinition;
import com.palantir.conjure.spec.AuthType;
import com.palantir.conjure.spec.CookieAuthType;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.EndpointName;
import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.HeaderAuthType;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.ParameterType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.visitor.AuthTypeVisitor;
import com.palantir.conjure.visitor.ParameterTypeVisitor;
import com.palantir.conjure.visitor.TypeVisitor;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import com.squareup.javapoet.AnnotationSpec;
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
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import io.undertow.util.StatusCodes;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

final class UndertowServiceHandlerGenerator {

    private static final String EXCHANGE_VAR_NAME = "exchange";
    private static final String DELEGATE_VAR_NAME = "delegate";
    private static final String RUNTIME_VAR_NAME = "runtime";
    private static final String DESERIALIZER_VAR_NAME = "deserializer";
    private static final String SERIALIZER_VAR_NAME = "serializer";
    private static final String AUTH_HEADER_VAR_NAME = "authHeader";
    private static final String COOKIE_TOKEN_VAR_NAME = "cookieToken";
    private static final String RESULT_VAR_NAME = "result";

    private static final ImmutableSet<String> RESERVED_PARAM_NAMES = ImmutableSet.of(
            EXCHANGE_VAR_NAME, DELEGATE_VAR_NAME, RUNTIME_VAR_NAME, DESERIALIZER_VAR_NAME, SERIALIZER_VAR_NAME);

    private final Options options;

    UndertowServiceHandlerGenerator(Options options) {
        this.options = options;
    }

    public JavaFile generateServiceHandler(
            ServiceDefinition serviceDefinition,
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typeDefinitions,
            TypeMapper typeMapper,
            TypeMapper returnTypeMapper) {
        String serviceName = serviceDefinition.getServiceName().getName();
        // class name
        ClassName serviceClass = ClassName.get(
                Packages.getPrefixedPackage(serviceDefinition.getServiceName().getPackage(), options.packagePrefix()),
                (options.undertowServicePrefix() ? "Undertow" : "")
                        + serviceDefinition.getServiceName().getName());
        TypeSpec.Builder factory = TypeSpec.classBuilder(serviceName + "Factory")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL);

        // addFields
        factory.addField(serviceClass, DELEGATE_VAR_NAME, Modifier.PRIVATE, Modifier.FINAL);
        factory.addField(ClassName.get(UndertowRuntime.class), RUNTIME_VAR_NAME, Modifier.PRIVATE, Modifier.FINAL);
        // addConstructor
        factory.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(UndertowRuntime.class, RUNTIME_VAR_NAME)
                .addParameter(serviceClass, DELEGATE_VAR_NAME)
                .addStatement("this.$1N = $1N", RUNTIME_VAR_NAME)
                .addStatement("this.$1N = $1N", DELEGATE_VAR_NAME)
                .build());

        ClassName serviceType = ClassName.get(
                Packages.getPrefixedPackage(serviceDefinition.getServiceName().getPackage(), options.packagePrefix()),
                serviceDefinition.getServiceName().getName() + "Endpoints");

        CodeBlock endpointBlock = CodeBlock.builder().build();
        for (EndpointDefinition e : serviceDefinition.getEndpoints()) {
            CodeBlock nextBlock = CodeBlock.of(
                    "new $1T($2N, $3N)",
                    endpointToHandlerType(serviceDefinition.getServiceName(), e.getEndpointName()),
                    RUNTIME_VAR_NAME,
                    DELEGATE_VAR_NAME);
            endpointBlock = endpointBlock.isEmpty() ? nextBlock : CodeBlock.of("$1L, $2L", endpointBlock, nextBlock);
        }

        TypeSpec endpoints = TypeSpec.classBuilder(serviceType.simpleName())
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(UndertowServiceHandlerGenerator.class))
                .addSuperinterface(UndertowService.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(FieldSpec.builder(serviceClass, DELEGATE_VAR_NAME, Modifier.PRIVATE, Modifier.FINAL)
                        .build())
                .addMethod(MethodSpec.methodBuilder("of")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .addParameter(serviceClass, DELEGATE_VAR_NAME)
                        .addStatement("return new $T($N)", serviceType, DELEGATE_VAR_NAME)
                        .returns(UndertowService.class)
                        .build())
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .addParameter(serviceClass, DELEGATE_VAR_NAME)
                        .addStatement("this.$1N = $1N", DELEGATE_VAR_NAME)
                        .build())
                .addMethod(MethodSpec.methodBuilder("endpoints")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(UndertowRuntime.class, RUNTIME_VAR_NAME)
                        .returns(ParameterizedTypeName.get(List.class, Endpoint.class))
                        .addStatement("return $1T.of($2L)", ImmutableList.class, endpointBlock)
                        .build())
                .addTypes(Lists.transform(
                        serviceDefinition.getEndpoints(),
                        e -> generateEndpointHandler(
                                e, serviceDefinition, serviceClass, typeDefinitions, typeMapper, returnTypeMapper)))
                .build();

        return JavaFile.builder(
                        Packages.getPrefixedPackage(
                                serviceDefinition.getServiceName().getPackage(), options.packagePrefix()),
                        endpoints)
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }

    private TypeName endpointToHandlerType(com.palantir.conjure.spec.TypeName serviceName, EndpointName name) {
        return ClassName.get(
                Packages.getPrefixedPackage(serviceName.getPackage(), options.packagePrefix()),
                serviceName.getName() + "Endpoints",
                endpointToHandlerClassName(name));
    }

    private String endpointToHandlerClassName(EndpointName name) {
        return StringUtils.capitalize(name.get()) + "Endpoint";
    }

    private TypeSpec generateEndpointHandler(
            EndpointDefinition endpointDefinition,
            ServiceDefinition serviceDefinition,
            ClassName serviceClass,
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typeDefinitions,
            TypeMapper typeMapper,
            TypeMapper returnTypeMapper) {
        MethodSpec.Builder handleMethodBuilder = MethodSpec.methodBuilder("handleRequest")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(HttpServerExchange.class, EXCHANGE_VAR_NAME)
                .addException(IOException.class)
                .addCode(endpointInvocation(endpointDefinition, typeDefinitions, typeMapper, returnTypeMapper));

        endpointDefinition
                .getDeprecated()
                .ifPresent(deprecatedDocsValue ->
                        handleMethodBuilder.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
                                .addMember("value", "$S", "deprecation")
                                .build()));

        MethodSpec.Builder ctorBuilder = MethodSpec.constructorBuilder()
                .addParameter(UndertowRuntime.class, RUNTIME_VAR_NAME)
                .addParameter(serviceClass, DELEGATE_VAR_NAME)
                .addStatement("this.$1N = $1N", RUNTIME_VAR_NAME)
                .addStatement("this.$1N = $1N", DELEGATE_VAR_NAME);

        TypeSpec.Builder endpointBuilder = TypeSpec.classBuilder(
                        endpointToHandlerClassName(endpointDefinition.getEndpointName()))
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .addSuperinterface(HttpHandler.class)
                .addSuperinterface(Endpoint.class)
                .addField(FieldSpec.builder(UndertowRuntime.class, RUNTIME_VAR_NAME, Modifier.PRIVATE, Modifier.FINAL)
                        .build())
                .addField(FieldSpec.builder(serviceClass, DELEGATE_VAR_NAME, Modifier.PRIVATE, Modifier.FINAL)
                        .build());

        addTags(endpointDefinition, endpointBuilder);

        getBodyParamTypeArgument(endpointDefinition.getArgs())
                .map(ArgumentDefinition::getType)
                // Filter out binary data
                .flatMap(type -> {
                    Type dealiased = TypeFunctions.toConjureTypeWithoutAliases(type, typeDefinitions);
                    return TypeFunctions.isBinaryOrOptionalBinary(dealiased) ? Optional.empty() : Optional.of(type);
                })
                .map(typeMapper::getClassName)
                .map(TypeName::box)
                .map(this::immutableCollection)
                .ifPresent(typeName -> {
                    TypeName type = ParameterizedTypeName.get(ClassName.get(Deserializer.class), typeName);
                    endpointBuilder.addField(
                            FieldSpec.builder(type, DESERIALIZER_VAR_NAME, Modifier.PRIVATE, Modifier.FINAL)
                                    .build());
                    ctorBuilder.addStatement(
                            "this.$1N = $2N.bodySerDe().deserializer(new $3T() {})",
                            DESERIALIZER_VAR_NAME,
                            RUNTIME_VAR_NAME,
                            ParameterizedTypeName.get(ClassName.get(TypeMarker.class), typeName));
                });

        endpointDefinition.getReturns().ifPresent(returnType -> {
            Type dealiased = TypeFunctions.toConjureTypeWithoutAliases(returnType, typeDefinitions);
            if (!TypeFunctions.isBinaryOrOptionalBinary(dealiased)) {
                TypeName typeName = returnTypeMapper.getClassName(returnType).box();
                TypeName type = ParameterizedTypeName.get(ClassName.get(Serializer.class), typeName);
                endpointBuilder.addField(FieldSpec.builder(type, SERIALIZER_VAR_NAME, Modifier.PRIVATE, Modifier.FINAL)
                        .build());
                ctorBuilder.addStatement(
                        "this.$1N = $2N.bodySerDe().serializer(new $3T() {})",
                        SERIALIZER_VAR_NAME,
                        RUNTIME_VAR_NAME,
                        ParameterizedTypeName.get(ClassName.get(TypeMarker.class), typeName));
            }
        });

        endpointBuilder.addMethod(ctorBuilder.build()).addMethod(handleMethodBuilder.build());

        if (UndertowTypeFunctions.isAsync(endpointDefinition, options)) {
            ParameterizedTypeName type =
                    UndertowTypeFunctions.getAsyncReturnType(endpointDefinition, returnTypeMapper, options);
            TypeName resultType = Iterables.getOnlyElement(type.typeArguments);
            endpointBuilder.addSuperinterface(
                    ParameterizedTypeName.get(ClassName.get(ReturnValueWriter.class), resultType));

            endpointBuilder.addMethod(MethodSpec.methodBuilder("write")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(resultType, RESULT_VAR_NAME)
                    .addParameter(HttpServerExchange.class, EXCHANGE_VAR_NAME)
                    .addException(IOException.class)
                    .addCode(generateReturnValueCodeBlock(endpointDefinition, typeDefinitions))
                    .build());
        }

        endpointBuilder
                .addMethod(MethodSpec.methodBuilder("method")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .returns(HttpString.class)
                        .addStatement(
                                "return $1T.$2N",
                                Methods.class,
                                endpointDefinition.getHttpMethod().toString())
                        .build())
                .addMethod(MethodSpec.methodBuilder("template")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .returns(String.class)
                        .addStatement("return $1S", endpointDefinition.getHttpPath())
                        .build())
                .addMethod(MethodSpec.methodBuilder("serviceName")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .returns(String.class)
                        // Note that this is the service name as defined in conjure, not the potentially modified
                        // name of the generated service interface. We may generate "UndertowFooService", but we
                        // should still return "FooService" here.
                        .addStatement(
                                "return $1S", serviceDefinition.getServiceName().getName())
                        .build())
                .addMethod(MethodSpec.methodBuilder("name")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .returns(String.class)
                        .addStatement(
                                "return $1S",
                                endpointDefinition.getEndpointName().get())
                        .build())
                .addMethod(MethodSpec.methodBuilder("handler")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .returns(HttpHandler.class)
                        .addStatement("return this")
                        .build());

        endpointDefinition
                .getDeprecated()
                .ifPresent(documentation -> endpointBuilder.addMethod(MethodSpec.methodBuilder("deprecated")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .returns(ParameterizedTypeName.get(ClassName.get(Optional.class), ClassName.get(String.class)))
                        .addStatement("return $1T.of($2S)", Optional.class, documentation)
                        .build()));

        return endpointBuilder.build();
    }

    private static void addTags(EndpointDefinition endpointDefinition, TypeSpec.Builder endpointBuilder) {
        if (!endpointDefinition.getTags().isEmpty()) {
            CodeBlock arrayValues = CodeBlock.join(
                    Collections2.transform(endpointDefinition.getTags(), value -> CodeBlock.of("$S", value)), ", ");
            endpointBuilder.addField(FieldSpec.builder(
                            ParameterizedTypeName.get(ImmutableSet.class, String.class),
                            "TAGS",
                            Modifier.PRIVATE,
                            Modifier.STATIC,
                            Modifier.FINAL)
                    .initializer(CodeBlock.of("$T.of($L)", ImmutableSet.class, arrayValues))
                    .build());
            endpointBuilder.addMethod(MethodSpec.methodBuilder("tags")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(ParameterizedTypeName.get(Set.class, String.class))
                    .addStatement("return TAGS")
                    .build());
        }
    }

    private static final ClassName LIST_NAME = ClassName.get(List.class);
    private static final ClassName IMMUTABLE_LIST_NAME = ClassName.get(ImmutableList.class);
    private static final ClassName SET_NAME = ClassName.get(Set.class);
    private static final ClassName IMMUTABLE_SET_NAME = ClassName.get(ImmutableSet.class);

    private TypeName immutableCollection(TypeName input) {
        // Note that only the outermost collection is considered for replacement to avoid
        // generic type incompatibilities.
        if (options.nonNullTopLevelCollectionValues() && input instanceof ParameterizedTypeName) {
            ParameterizedTypeName parameterized = (ParameterizedTypeName) input;
            if (LIST_NAME.equals(parameterized.rawType)) {
                return ParameterizedTypeName.get(
                        IMMUTABLE_LIST_NAME, parameterized.typeArguments.toArray(new TypeName[0]));
            } else if (SET_NAME.equals(parameterized.rawType)) {
                return ParameterizedTypeName.get(
                        IMMUTABLE_SET_NAME, parameterized.typeArguments.toArray(new TypeName[0]));
            }
        }

        return input;
    }

    private static final String PATH_PARAMS_VAR_NAME = "pathParams";
    private static final String QUERY_PARAMS_VAR_NAME = "queryParams";
    private static final String HEADER_PARAMS_VAR_NAME = "headerParams";

    private CodeBlock endpointInvocation(
            EndpointDefinition endpointDefinition,
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typeDefinitions,
            TypeMapper typeMapper,
            TypeMapper returnTypeMapper) {
        CodeBlock.Builder code = CodeBlock.builder();

        // auth code
        Optional<String> authVarName = addAuthCode(code, endpointDefinition);

        // body parameter
        getBodyParamTypeArgument(endpointDefinition.getArgs()).ifPresent(bodyParam -> {
            String paramName = sanitizeVarName(bodyParam.getArgName().get(), endpointDefinition);
            Type dealiased = TypeFunctions.toConjureTypeWithoutAliases(bodyParam.getType(), typeDefinitions);
            if (TypeFunctions.isBinaryOrOptionalBinary(dealiased)) {
                code.addStatement(
                        "$1T $2N = $3N.bodySerDe().deserializeInputStream($4N)",
                        InputStream.class,
                        paramName,
                        RUNTIME_VAR_NAME,
                        EXCHANGE_VAR_NAME);
            } else {
                code.addStatement(
                        "$1T $2N = $3N.deserialize($4N)",
                        typeMapper.getClassName(bodyParam.getType()).box(),
                        paramName,
                        DESERIALIZER_VAR_NAME,
                        EXCHANGE_VAR_NAME);
            }
            code.add(generateParamMetadata(bodyParam, bodyParam.getArgName().get(), paramName, typeMapper));
        });

        // path parameters
        addPathParamsCode(code, endpointDefinition, typeDefinitions, typeMapper);

        // header parameters
        addHeaderParamsCode(code, endpointDefinition, typeDefinitions, typeMapper);

        // query parameters
        addQueryParamsCode(code, endpointDefinition, typeDefinitions, typeMapper);

        List<String> methodArgs = new ArrayList<>();
        authVarName.ifPresent(methodArgs::add);
        methodArgs.addAll(ParameterOrder.sorted(endpointDefinition.getArgs()).stream()
                .map(arg -> arg.getArgName().get())
                .map(arg -> sanitizeVarName(arg, endpointDefinition))
                .collect(ImmutableList.toImmutableList()));

        boolean async = UndertowTypeFunctions.isAsync(endpointDefinition, options);
        if (async || endpointDefinition.getReturns().isPresent()) {
            code.addStatement(
                    "$1T $2N = $3N.$4L($5L)",
                    async
                            ? UndertowTypeFunctions.getAsyncReturnType(endpointDefinition, returnTypeMapper, options)
                            : returnTypeMapper.getClassName(
                                    endpointDefinition.getReturns().get()),
                    RESULT_VAR_NAME,
                    DELEGATE_VAR_NAME,
                    JavaNameSanitizer.sanitize(
                            endpointDefinition.getEndpointName().get()),
                    String.join(", ", methodArgs));
        } else {
            code.addStatement(
                    "$1N.$2L($3L)",
                    DELEGATE_VAR_NAME,
                    endpointDefinition.getEndpointName(),
                    String.join(", ", methodArgs));
        }
        if (UndertowTypeFunctions.isAsync(endpointDefinition, options)) {
            code.add(CodeBlocks.statement(
                    "$1N.async().register($2N, this, $3N)", RUNTIME_VAR_NAME, RESULT_VAR_NAME, EXCHANGE_VAR_NAME));
        } else {
            code.add(generateReturnValueCodeBlock(endpointDefinition, typeDefinitions));
        }
        return code.build();
    }

    private CodeBlock generateReturnValueCodeBlock(
            EndpointDefinition endpointDefinition,
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typeDefinitions) {
        CodeBlock.Builder code = CodeBlock.builder();
        if (endpointDefinition.getReturns().isPresent()) {
            Type returnType = endpointDefinition.getReturns().get();
            // optional<> handling
            Type dealiased = TypeFunctions.toConjureTypeWithoutAliases(returnType, typeDefinitions);
            if (dealiased.accept(TypeVisitor.IS_OPTIONAL)) {
                CodeBlock serializer;
                if (TypeFunctions.isBinaryOrOptionalBinary(dealiased)) {
                    serializer = CodeBlock.builder()
                            .add(
                                    dealiased.accept(TypeVisitor.IS_BINARY)
                                            ? "$1N.bodySerDe().serialize($2N, $3N)"
                                            : "$1N.bodySerDe().serialize($2N.get(), $3N)",
                                    RUNTIME_VAR_NAME,
                                    RESULT_VAR_NAME,
                                    EXCHANGE_VAR_NAME)
                            .build();
                } else {
                    serializer = CodeBlock.builder()
                            .add("$1N.serialize($2N, $3N)", SERIALIZER_VAR_NAME, RESULT_VAR_NAME, EXCHANGE_VAR_NAME)
                            .build();
                }
                // For optional<>: set response code to 204/NO_CONTENT if result is absent
                code.add(CodeBlock.builder()
                        .beginControlFlow(
                                "if ($1L)",
                                createIsOptionalPresentCall(
                                        TypeFunctions.isBinaryOrOptionalBinary(dealiased) ? dealiased : returnType,
                                        RESULT_VAR_NAME,
                                        typeDefinitions))
                        .addStatement(serializer)
                        .nextControlFlow("else")
                        .addStatement("$1N.setStatusCode($2T.NO_CONTENT)", EXCHANGE_VAR_NAME, StatusCodes.class)
                        .endControlFlow()
                        .build());
            } else {
                if (dealiased.accept(TypeVisitor.IS_BINARY)) {
                    code.addStatement(
                            "$1N.bodySerDe().serialize($2N, $3N)",
                            RUNTIME_VAR_NAME,
                            RESULT_VAR_NAME,
                            EXCHANGE_VAR_NAME);
                } else {
                    code.addStatement(
                            "$1N.serialize($2N, $3N)", SERIALIZER_VAR_NAME, RESULT_VAR_NAME, EXCHANGE_VAR_NAME);
                }
            }
        } else {
            // Set 204 response code for void methods
            // Use the constant from undertow for improved source readability, javac will compile it out.
            code.addStatement("$1N.setStatusCode($2T.NO_CONTENT)", EXCHANGE_VAR_NAME, StatusCodes.class);
        }
        return code.build();
    }

    private static CodeBlock generateParamMetadata(
            ArgumentDefinition argument, String paramName, String variableName, TypeMapper typeMapper) {
        ConjureTags.validateTags(argument);
        Set<String> mergedTags = Streams.concat(
                        argument.getMarkers().stream().map(marker -> typeMapper
                                .getClassName(marker)
                                .box()
                                .withoutAnnotations()
                                .toString()),
                        argument.getTags().stream())
                .collect(ImmutableSet.toImmutableSet());
        return generateParamTags(mergedTags, paramName, variableName);
    }

    private static CodeBlock generateParamTags(
            Collection<String> tags,
            String paramName,
            // Variable may be sanitized and does not necessarily match the paramName.
            // For example paramName may be 'int' where the variable name would be
            // sanitized to 'int_'.
            String variableName) {
        return CodeBlocks.of(tags.stream()
                .map(marker -> CodeBlock.of(
                        "$1N.markers().param($2S, $3S, $4N, $5N);",
                        RUNTIME_VAR_NAME,
                        marker,
                        paramName,
                        variableName,
                        EXCHANGE_VAR_NAME))
                .collect(ImmutableList.toImmutableList()));
    }

    // Adds code for authorization. Returns an optional that contains the name of the variable that contains the
    // deserialized optional parameter.
    private Optional<String> addAuthCode(CodeBlock.Builder code, EndpointDefinition endpointDefinition) {
        if (!endpointDefinition.getAuth().isPresent()) {
            return Optional.empty();
        }

        return endpointDefinition.getAuth().get().accept(new AuthType.Visitor<Optional<String>>() {
            @Override
            public Optional<String> visitHeader(HeaderAuthType _value) {
                // header auth
                code.addStatement(
                        "$1T $2N = $3N.auth().header($4N)",
                        AuthHeader.class,
                        AUTH_HEADER_VAR_NAME,
                        RUNTIME_VAR_NAME,
                        EXCHANGE_VAR_NAME);
                return Optional.of(AUTH_HEADER_VAR_NAME);
            }

            @Override
            public Optional<String> visitCookie(CookieAuthType _value) {
                code.addStatement(
                        "$1T $2N = $3N.auth().cookie($4N, $5S)",
                        BearerToken.class,
                        COOKIE_TOKEN_VAR_NAME,
                        RUNTIME_VAR_NAME,
                        EXCHANGE_VAR_NAME,
                        endpointDefinition
                                .getAuth()
                                .get()
                                .accept(AuthTypeVisitor.COOKIE)
                                .getCookieName());
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
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typeDefinitions,
            TypeMapper typeMapper) {
        if (hasPathArgument(endpointDefinition.getArgs())) {
            code.addStatement(
                    "$1T<$2T, $2T> $3N = $4N.getAttachment($5T.ATTACHMENT_KEY).getParameters()",
                    Map.class,
                    String.class,
                    PATH_PARAMS_VAR_NAME,
                    EXCHANGE_VAR_NAME,
                    io.undertow.util.PathTemplateMatch.class);
            code.add(generatePathParameterCodeBlock(endpointDefinition, typeDefinitions, typeMapper));
        }
    }

    private void addHeaderParamsCode(
            CodeBlock.Builder code,
            EndpointDefinition endpointDefinition,
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typeDefinitions,
            TypeMapper typeMapper) {
        if (hasHeaderArgument(endpointDefinition.getArgs())) {
            code.addStatement(
                    "$1T $2N = $3N.getRequestHeaders()",
                    io.undertow.util.HeaderMap.class,
                    HEADER_PARAMS_VAR_NAME,
                    EXCHANGE_VAR_NAME);
            code.add(generateHeaderParameterCodeBlock(endpointDefinition, typeDefinitions, typeMapper));
        }
    }

    private void addQueryParamsCode(
            CodeBlock.Builder code,
            EndpointDefinition endpointDefinition,
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typeDefinitions,
            TypeMapper typeMapper) {
        if (hasQueryArgument(endpointDefinition.getArgs())) {
            code.addStatement(
                    "$1T $2N = $3N.getQueryParameters()",
                    ParameterizedTypeName.get(
                            ClassName.get(Map.class),
                            TypeName.get(String.class),
                            ParameterizedTypeName.get(Deque.class, String.class)),
                    QUERY_PARAMS_VAR_NAME,
                    EXCHANGE_VAR_NAME);
            code.add(generateQueryParameterCodeBlock(endpointDefinition, typeDefinitions, typeMapper));
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

    private CodeBlock generatePathParameterCodeBlock(
            EndpointDefinition endpoint,
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typeDefinitions,
            TypeMapper typeMapper) {
        return generateParameterCodeBlock(
                endpoint,
                ParameterTypeVisitor.IS_PATH,
                PATH_PARAMS_VAR_NAME,
                arg -> arg.getArgName().get(),
                typeDefinitions,
                typeMapper);
    }

    private CodeBlock generateQueryParameterCodeBlock(
            EndpointDefinition endpoint,
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typeDefinitions,
            TypeMapper typeMapper) {
        return generateParameterCodeBlock(
                endpoint,
                ParameterTypeVisitor.IS_QUERY,
                QUERY_PARAMS_VAR_NAME,
                arg -> arg.getParamType()
                        .accept(ParameterTypeVisitor.QUERY)
                        .getParamId()
                        .get(),
                typeDefinitions,
                typeMapper);
    }

    private CodeBlock generateHeaderParameterCodeBlock(
            EndpointDefinition endpoint,
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typeDefinitions,
            TypeMapper typeMapper) {
        return generateParameterCodeBlock(
                endpoint,
                ParameterTypeVisitor.IS_HEADER,
                HEADER_PARAMS_VAR_NAME,
                arg -> arg.getParamType()
                        .accept(ParameterTypeVisitor.HEADER)
                        .getParamId()
                        .get(),
                typeDefinitions,
                typeMapper);
    }

    private CodeBlock generateParameterCodeBlock(
            EndpointDefinition endpoint,
            ParameterType.Visitor<Boolean> paramTypeVisitor,
            String paramsVarName,
            Function<ArgumentDefinition, String> toParamId,
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typeDefinitions,
            TypeMapper typeMapper) {
        return CodeBlocks.of(endpoint.getArgs().stream()
                .filter(param -> param.getParamType().accept(paramTypeVisitor))
                .map(arg -> {
                    Type normalizedType = TypeFunctions.toConjureTypeWithoutAliases(arg.getType(), typeDefinitions);
                    String paramName = sanitizeVarName(arg.getArgName().get(), endpoint);
                    final CodeBlock retrieveParam;
                    if (normalizedType.equals(arg.getType())
                            // Collections of alias types are handled the same way as external imports
                            || TypeFunctions.isListOrSet(arg.getType())) {
                        // type is not an alias or optional of an alias
                        retrieveParam = decodePlainParameterCodeBlock(
                                arg.getType(), typeMapper, paramName, paramsVarName, toParamId.apply(arg));
                    } else {
                        // type contains aliases: decode raw value and then construct real value from raw one
                        String rawVarName = arg.getArgName().get() + "Raw";
                        retrieveParam = CodeBlocks.of(
                                decodePlainParameterCodeBlock(
                                        normalizedType, typeMapper, rawVarName, paramsVarName, toParamId.apply(arg)),
                                CodeBlocks.statement(
                                        "$1T $2N = $3L",
                                        typeMapper.getClassName(arg.getType()),
                                        paramName,
                                        createConstructorForTypeWithReference(
                                                arg.getType(), rawVarName, typeDefinitions, typeMapper)));
                    }
                    return CodeBlocks.of(
                            retrieveParam,
                            generateParamMetadata(arg, arg.getArgName().get(), paramName, typeMapper));
                })
                .collect(ImmutableList.toImmutableList()));
    }

    private CodeBlock decodePlainParameterCodeBlock(
            Type type, TypeMapper typeMapper, String resultVarName, String paramsVarName, String paramId) {
        if (type.accept(MoreVisitors.IS_EXTERNAL)) {
            return CodeBlocks.statement(
                    "$1T $2N = $3T.valueOf($4N.plainSerDe().deserializeString($5N.get($6S)))",
                    typeMapper.getClassName(type),
                    resultVarName,
                    typeMapper.getClassName(type).box(),
                    RUNTIME_VAR_NAME,
                    paramsVarName,
                    paramId);
        }
        Optional<CodeBlock> complexDeserializer =
                getComplexTypeStringDeserializer(type, typeMapper, resultVarName, paramsVarName, paramId);
        if (complexDeserializer.isPresent()) {
            return complexDeserializer.get();
        }
        return CodeBlocks.statement(
                "$1T $2N = $3N.plainSerDe().$4L($5N.get($6S))",
                typeMapper.getClassName(type),
                resultVarName,
                RUNTIME_VAR_NAME,
                deserializeFunctionName(type),
                paramsVarName,
                paramId);
    }

    /**
     * Generates a deserializer block decoding strings using conjure plain encoding from header, query, and path
     * parameters to complex types.
     *
     * <p>We consider complex types to be anything that is neither a primitive nor contained primitive, For example enum
     * types and external imports.
     */
    private Optional<CodeBlock> getComplexTypeStringDeserializer(
            Type type, TypeMapper typeMapper, String resultVarName, String paramsVarName, String paramId) {
        return type.accept(new DefaultTypeVisitor<Optional<String>>() {
                    @Override
                    public Optional<String> visitExternal(ExternalReference _value) {
                        return Optional.of("deserializeComplex");
                    }

                    @Override
                    public Optional<String> visitReference(com.palantir.conjure.spec.TypeName _value) {
                        return Optional.of("deserializeComplex");
                    }

                    @Override
                    public Optional<String> visitPrimitive(PrimitiveType _value) {
                        return Optional.empty();
                    }

                    @Override
                    public Optional<String> visitList(ListType value) {
                        return value.getItemType().accept(this).map(ignored -> "deserializeComplexList");
                    }

                    @Override
                    public Optional<String> visitSet(SetType value) {
                        return value.getItemType().accept(this).map(ignored -> "deserializeComplexSet");
                    }

                    @Override
                    public Optional<String> visitOptional(OptionalType value) {
                        return value.getItemType().accept(this).map(ignored -> "deserializeOptionalComplex");
                    }

                    @Override
                    public Optional<String> visitDefault() {
                        return Optional.empty();
                    }
                })
                .map(functionName -> CodeBlocks.statement(
                        "$1T $2N = $3N.plainSerDe().$4L($5N.get($6S), $7T::valueOf)",
                        typeMapper.getClassName(type),
                        resultVarName,
                        RUNTIME_VAR_NAME,
                        functionName,
                        paramsVarName,
                        paramId,
                        typeMapper.getClassName(getComplexType(type)).box()));
    }

    /**
     * Gets the complex type either matching, or contained by
     *
     * <pre>type</pre>
     *
     * .
     *
     * <p>We consider complex types to be anything that is neither a primitive nor contained primitive, For example enum
     * types and external imports.
     */
    private Type getComplexType(Type type) {
        // No need to handle the map type because it is not allowed in string
        // values like headers, path, or query parameters.
        return type.accept(new DefaultTypeVisitor<Optional<Type>>() {
                    @Override
                    public Optional<Type> visitList(ListType value) {
                        return Optional.of(value.getItemType());
                    }

                    @Override
                    public Optional<Type> visitSet(SetType value) {
                        return Optional.of(value.getItemType());
                    }

                    @Override
                    public Optional<Type> visitOptional(OptionalType value) {
                        return Optional.of(value.getItemType());
                    }

                    @Override
                    public Optional<Type> visitDefault() {
                        return Optional.empty();
                    }
                })
                .orElse(type);
    }

    /**
     * Returns a CodeBlock that, when run, returns a boolean indicating whether or not the optional represented by the
     * provided type is empty. inType must be a type that is either an optional or an alias that resolves to an
     * optional.
     */
    private static CodeBlock createIsOptionalPresentCall(
            Type inType, String varName, Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typeDefinitions) {
        if (inType.accept(TypeVisitor.IS_OPTIONAL)) {
            // current type is optional type: call isPresent
            return CodeBlock.of("$1N.isPresent()", varName);
        } else if (TypeFunctions.isReferenceType(inType)) {
            // current type is an alias type: call "get()" to resolve alias and generate recursively on aliased type
            Type aliasedType = TypeFunctions.getReferencedType(inType, typeDefinitions);
            return createIsOptionalPresentCall(aliasedType, varName + ".get()", typeDefinitions);
        } else {
            throw new IllegalArgumentException("inType must be either an optional or alias type, was " + inType);
        }
    }

    /**
     * Returns a CodeBlock that constructs the given type given the provided variable that holds the decoded value for
     * the type. inType must be a type that resolves to {@code (primitive|optional<primitive>)} and must use an alias
     * type somewhere in its type definition. This means that inType must be one of the following:
     * - {@code optional<alias that resolves to a primitive>}
     * - alias of a primitive
     * - alias of an {@code optional<primitive>}
     * - alias of an {@code optional<alias that resolves to a primitive>}
     * - alias of an alias that follows one of these rules (recursive definition)
     *
     * An "alias that resolves to a primitive" is either an alias of a primitive or an alias of an alias that follows
     * these rules (recursive definition).
     */
    private static CodeBlock createConstructorForTypeWithReference(
            Type inType,
            String decodedVarName,
            Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typeDefinitions,
            TypeMapper typeMapper) {
        // "in" must be 1 of 2 types: optional<alias that resolves to a primitive> or alias
        if (inType.accept(TypeVisitor.IS_OPTIONAL)) {
            // optional<alias that resolves to a primitive>
            Type typeOfOptional = inType.accept(TypeVisitor.OPTIONAL).getItemType();
            return CodeBlock.of(
                    "$1T.ofNullable($2N.isPresent() ? $3L : null)",
                    Optional.class,
                    decodedVarName,
                    createConstructorForTypeWithReference(
                            typeOfOptional, decodedVarName + ".get()", typeDefinitions, typeMapper));
        } else {
            // alias
            CodeBlock ofContent;

            // alias must resolve to one of:
            //   * primitive
            //   * optional<primitive>
            //   * optional<alias that resolves to a primitive>
            //   * alias that follows one of these rules (recursive definition)
            Type aliasedType = TypeFunctions.getReferencedType(inType, typeDefinitions);
            if (aliasedType.accept(TypeVisitor.IS_PRIMITIVE) || aliasedType.accept(MoreVisitors.IS_EXTERNAL)) {
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
                    ofContent = createConstructorForTypeWithReference(
                            aliasedType, decodedVarName, typeDefinitions, typeMapper);
                }
            } else {
                // alias
                ofContent =
                        createConstructorForTypeWithReference(aliasedType, decodedVarName, typeDefinitions, typeMapper);
            }
            return CodeBlock.of("$1T.of($2L)", typeMapper.getClassName(inType), ofContent);
        }
    }

    private static String deserializeFunctionName(Type type) {
        if (type.accept(TypeVisitor.IS_PRIMITIVE)) {
            return "deserialize" + TypeFunctions.primitiveTypeName(type.accept(TypeFunctions.PRIMITIVE_VISITOR));
        } else if (type.accept(TypeVisitor.IS_OPTIONAL)
                && type.accept(TypeVisitor.OPTIONAL).getItemType().accept(TypeVisitor.IS_PRIMITIVE)) {
            PrimitiveType innerPrimitiveType =
                    type.accept(TypeFunctions.OPTIONAL_VISITOR).getItemType().accept(TypeFunctions.PRIMITIVE_VISITOR);
            return "deserializeOptional" + TypeFunctions.primitiveTypeName(innerPrimitiveType);
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
        return args.stream()
                .filter(arg -> arg.getParamType().accept(ParameterTypeVisitor.IS_BODY))
                .collect(MoreCollectors.toOptional());
    }

    private static String sanitizeVarName(String input, EndpointDefinition endpoint) {
        String value = JavaNameSanitizer.sanitizeParameterName(input, endpoint);
        if (RESERVED_PARAM_NAMES.contains(value)
                || (endpoint.getReturns().isPresent() && RESULT_VAR_NAME.equals(value))) {
            return sanitizeVarName(value + "_", endpoint);
        }
        return value;
    }
}
