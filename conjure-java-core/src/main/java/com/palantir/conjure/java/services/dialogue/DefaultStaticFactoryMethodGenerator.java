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
import com.google.common.collect.ImmutableMap;
import com.google.common.hash.Hashing;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.api.errors.ConjureErrorParameterFormats;
import com.palantir.conjure.java.services.Auth;
import com.palantir.conjure.java.util.ErrorGenerationUtils;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.java.util.Primitives;
import com.palantir.conjure.spec.ArgumentDefinition;
import com.palantir.conjure.spec.AuthType;
import com.palantir.conjure.spec.BodyParameterType;
import com.palantir.conjure.spec.CookieAuthType;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.ErrorDefinition;
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
import com.palantir.dialogue.ExceptionDeserializerArgs;
import com.palantir.dialogue.PlainSerDe;
import com.palantir.dialogue.Request;
import com.palantir.dialogue.Serializer;
import com.palantir.dialogue.TypeMarker;
import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;
import com.palantir.javapoet.TypeVariableName;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.lang.model.element.Modifier;

public final class DefaultStaticFactoryMethodGenerator implements StaticFactoryMethodGenerator {
    private static final String REQUEST = "_request";
    private static final String PLAIN_SER_DE = "_plainSerDe";

    private final Options options;
    private final TypeNameResolver typeNameResolver;
    private final ParameterTypeMapper parameterTypes;
    private final ReturnTypeMapper returnTypes;
    private final StaticFactoryMethodType methodType;
    private final List<ErrorDefinition> errorDefinitions;

    public DefaultStaticFactoryMethodGenerator(
            Options options,
            TypeNameResolver typeNameResolver,
            ParameterTypeMapper parameterTypes,
            ReturnTypeMapper returnTypes,
            StaticFactoryMethodType methodType,
            List<ErrorDefinition> errorDefinitions) {
        this.options = options;
        this.typeNameResolver = typeNameResolver;
        this.parameterTypes = parameterTypes;
        this.returnTypes = returnTypes;
        this.methodType = methodType;
        this.errorDefinitions = errorDefinitions;
    }

    @Override
    public MethodSpec generate(ServiceDefinition def) {
        ClassName className = getClassName(def);
        TypeSpec.Builder impl = TypeSpec.anonymousClassBuilder("").addSuperinterface(className);

        impl.addField(FieldSpec.builder(PlainSerDe.class, PLAIN_SER_DE)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer(CodeBlock.of("$L.plainSerDe()", StaticFactoryMethodGenerator.RUNTIME))
                .build());

        boolean isErrorRespecting = options.generateErrorParameterFormatRespectingDialogueInterfaces();

        if (isErrorRespecting) {
            impl.addMethod(createHelperToConstructExceptionDeserializerArgs());
        }

        SerDeFieldsContext context = collectSerDeFields(def, isErrorRespecting);
        generateTypeMarkerFields(impl, context);
        generateExceptionArgsFields(impl, context);
        generateDeserializerFields(impl, context, isErrorRespecting);
        generateSerializerFields(impl, context);
        generateEndpointImplementations(
                impl, def, context.endpointToSerializerField(), context.endpointToDeserializerField());

        impl.addMethod(DefaultStaticFactoryMethodGenerator.toStringMethod(className));

        String javadoc = methodType.switchBy(
                "Creates a synchronous/blocking client for a $L service.",
                "Creates an asynchronous/non-blocking client for a $L service.");
        return MethodSpec.methodBuilder("of")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .addJavadoc(javadoc, def.getServiceName().getName())
                .returns(className)
                .addParameter(EndpointChannelFactory.class, StaticFactoryMethodGenerator.ENDPOINT_CHANNEL_FACTORY)
                .addParameter(ConjureRuntime.class, StaticFactoryMethodGenerator.RUNTIME)
                .addCode(CodeBlock.builder().add("return $L;", impl.build()).build())
                .build();
    }

    @SuppressWarnings("checkstyle:cyclomaticcomplexity")
    private SerDeFieldsContext collectSerDeFields(ServiceDefinition def, boolean isErrorRespecting) {
        Map<TypeName, String> fieldBaseNames = new HashMap<>();
        Map<TypeName, String> typeMarkerFields = new LinkedHashMap<>();
        Map<TypeName, String> exceptionArgsFields = new LinkedHashMap<>();
        Map<TypeName, String> deserializerFieldNames = new LinkedHashMap<>();
        Map<TypeName, DeserializerType> deserializerTypes = new LinkedHashMap<>();
        Map<TypeName, String> serializerFieldNames = new LinkedHashMap<>();
        Map<String, String> endpointToDeserializerField = new HashMap<>();
        Map<String, String> endpointToSerializerField = new HashMap<>();
        Set<String> usedBaseNames = new HashSet<>();

        for (EndpointDefinition endpoint : def.getEndpoints()) {
            TypeName returnClassName = Primitives.box(returnTypes.baseType(endpoint.getReturns()));

            if (returnTypes.isBinaryOrOptionalBinary(returnClassName) && !isErrorRespecting) {
                // Binary types in non-error path use inline deserializers, no field needed
                continue;
            }

            String baseName = fieldBaseNames.computeIfAbsent(
                    returnClassName, typeName -> uniqueFieldBaseName(typeName, usedBaseNames));
            if (!deserializerFieldNames.containsKey(returnClassName)) {
                if (isErrorRespecting || endpoint.getReturns().isPresent()) {
                    typeMarkerFields.put(returnClassName, baseName + "TypeMarker");
                }
                if (isErrorRespecting) {
                    exceptionArgsFields.put(returnClassName, baseName + "ExceptionArgs");
                }
                deserializerFieldNames.put(returnClassName, baseName + "Deserializer");
                DeserializerType deserType = isErrorRespecting
                        ? getDeserializerType(endpoint.getReturns(), returnClassName)
                        : (endpoint.getReturns().isPresent() ? DeserializerType.STANDARD : DeserializerType.EMPTY_BODY);
                deserializerTypes.put(returnClassName, deserType);
            }

            endpointToDeserializerField.put(
                    endpoint.getEndpointName().get(), deserializerFieldNames.get(returnClassName));
        }

        for (EndpointDefinition endpoint : def.getEndpoints()) {
            endpoint.getArgs().stream()
                    .filter(arg -> arg.getParamType().accept(ParameterTypeVisitor.IS_BODY))
                    .findAny()
                    .filter(body -> !body.getType().accept(TypeVisitor.IS_BINARY))
                    .ifPresent(body -> {
                        TypeName bodyClassName = Primitives.box(returnTypes.baseType(body.getType()));
                        String baseName = fieldBaseNames.computeIfAbsent(
                                bodyClassName, typeName -> uniqueFieldBaseName(typeName, usedBaseNames));
                        typeMarkerFields.putIfAbsent(bodyClassName, baseName + "TypeMarker");
                        serializerFieldNames.putIfAbsent(bodyClassName, baseName + "Serializer");
                        endpointToSerializerField.put(
                                endpoint.getEndpointName().get(), serializerFieldNames.get(bodyClassName));
                    });
        }

        return new SerDeFieldsContext(
                typeMarkerFields,
                exceptionArgsFields,
                deserializerFieldNames,
                deserializerTypes,
                serializerFieldNames,
                endpointToDeserializerField,
                endpointToSerializerField);
    }

    private static void generateTypeMarkerFields(TypeSpec.Builder impl, SerDeFieldsContext context) {
        context.typeMarkerFields()
                .forEach((typeName, fieldName) -> impl.addField(FieldSpec.builder(
                                ParameterizedTypeName.get(ClassName.get(TypeMarker.class), typeName), fieldName)
                        .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                        .initializer("new $T<$T>() {}", TypeMarker.class, typeName)
                        .build()));
    }

    private static void generateExceptionArgsFields(TypeSpec.Builder impl, SerDeFieldsContext context) {
        context.exceptionArgsFields().forEach((typeName, fieldName) -> {
            impl.addField(FieldSpec.builder(
                            ParameterizedTypeName.get(ClassName.get(ExceptionDeserializerArgs.class), typeName),
                            fieldName)
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                    .initializer(
                            "createExceptionDeserializerArgs($L)",
                            context.typeMarkerFields().get(typeName))
                    .build());
        });
    }

    private static void generateDeserializerFields(
            TypeSpec.Builder impl, SerDeFieldsContext context, boolean isErrorRespecting) {
        context.deserializerFieldNames().forEach((typeName, fieldName) -> {
            ParameterizedTypeName deserializerType =
                    ParameterizedTypeName.get(ClassName.get(Deserializer.class), typeName);
            DeserializerType deserType = context.deserializerTypes().get(typeName);
            CodeBlock initializer = createDeserializerInitializer(deserType, typeName, context, isErrorRespecting);
            impl.addField(FieldSpec.builder(deserializerType, fieldName)
                    .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                    .initializer(initializer)
                    .build());
        });
    }

    private static void generateSerializerFields(TypeSpec.Builder impl, SerDeFieldsContext context) {
        context.serializerFieldNames()
                .forEach((typeName, fieldName) -> impl.addField(FieldSpec.builder(
                                ParameterizedTypeName.get(ClassName.get(Serializer.class), typeName), fieldName)
                        .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                        .initializer(
                                "$L.bodySerDe().serializer($L)",
                                StaticFactoryMethodGenerator.RUNTIME,
                                context.typeMarkerFields().get(typeName))
                        .build()));
    }

    private static CodeBlock createDeserializerInitializer(
            DeserializerType deserType, TypeName typeName, SerDeFieldsContext context, boolean isErrorRespecting) {
        if (isErrorRespecting) {
            String argsField = context.exceptionArgsFields().get(typeName);
            return switch (deserType) {
                case STANDARD ->
                    CodeBlock.of("$L.bodySerDe().deserializer($L)", StaticFactoryMethodGenerator.RUNTIME, argsField);
                case EMPTY_BODY ->
                    CodeBlock.of(
                            "$L.bodySerDe().emptyBodyDeserializer($L)",
                            StaticFactoryMethodGenerator.RUNTIME,
                            argsField);
                case BINARY ->
                    CodeBlock.of(
                            "$L.bodySerDe().inputStreamDeserializer($L)",
                            StaticFactoryMethodGenerator.RUNTIME,
                            argsField);
                case OPTIONAL_BINARY ->
                    CodeBlock.of(
                            "$L.bodySerDe().optionalInputStreamDeserializer($L)",
                            StaticFactoryMethodGenerator.RUNTIME,
                            argsField);
            };
        } else {
            String tmField = context.typeMarkerFields().get(typeName);
            return switch (deserType) {
                case STANDARD ->
                    CodeBlock.of("$L.bodySerDe().deserializer($L)", StaticFactoryMethodGenerator.RUNTIME, tmField);
                case EMPTY_BODY ->
                    CodeBlock.of("$L.bodySerDe().emptyBodyDeserializer()", StaticFactoryMethodGenerator.RUNTIME);
                default ->
                    throw new SafeIllegalStateException(
                            "Unexpected deserializer type in non-error path", SafeArg.of("type", deserType));
            };
        }
    }

    private void generateEndpointImplementations(
            TypeSpec.Builder impl,
            ServiceDefinition def,
            Map<String, String> endpointToSerializerField,
            Map<String, String> endpointToDeserializerField) {
        def.getEndpoints().forEach(endpoint -> {
            impl.addField(bindEndpointChannel(def, endpoint));
            impl.addMethod(clientImpl(endpoint, endpointToSerializerField, endpointToDeserializerField));
        });
    }

    private ClassName getTypeMarkersClass(ErrorDefinition errorDef) {
        return ClassName.get(
                Packages.getPrefixedPackage(errorDef.getErrorName().getPackage(), options.packagePrefix()),
                ErrorGenerationUtils.errorTypesClassName(errorDef.getNamespace()) + "TypeMarkers");
    }

    private MethodSpec createHelperToConstructExceptionDeserializerArgs() {
        TypeVariableName typeVariableT = TypeVariableName.get("T");
        ParameterizedTypeName builderType =
                ParameterizedTypeName.get(ClassName.get(ExceptionDeserializerArgs.Builder.class), typeVariableT);
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("createExceptionDeserializerArgs")
                .addTypeVariable(typeVariableT)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(ParameterizedTypeName.get(ClassName.get(ExceptionDeserializerArgs.class), typeVariableT))
                .addParameter(ParameterizedTypeName.get(ClassName.get(TypeMarker.class), typeVariableT), "returnType");

        CodeBlock.Builder code = CodeBlock.builder()
                .addStatement(
                        "$T builder = $T.<$T>builder().returnType(returnType)",
                        builderType,
                        ExceptionDeserializerArgs.class,
                        typeVariableT);

        // Deduplicate by TypeMarkers class (one per error namespace)
        errorDefinitions.stream()
                .map(this::getTypeMarkersClass)
                .distinct()
                .sorted(Comparator.comparing(ClassName::toString))
                .forEach(typeMarkersClass -> code.addStatement("$T.registerExceptions(builder)", typeMarkersClass));

        code.addStatement("return builder.build()");
        return methodBuilder.addCode(code.build()).build();
    }

    private ClassName getClassName(ServiceDefinition def) {
        return methodType.switchBy(Names.blockingClassName(def, options), Names.asyncClassName(def, options));
    }

    /**
     * Computes a unique camelCase field base name for a given TypeName. If the derived name collides with an
     * already-used name, a deterministic hash suffix is appended.
     */
    private static String uniqueFieldBaseName(TypeName typeName, Set<String> usedNames) {
        String base = typeNameToFieldBase(typeName);
        if (usedNames.add(base)) {
            return base;
        }
        String candidate = base + deterministicHash(typeName);
        usedNames.add(candidate);
        return candidate;
    }

    private static String deterministicHash(TypeName typeName) {
        // Use deterministic hash to deconflict colliding names
        return Integer.toUnsignedString(
                Hashing.murmur3_32_fixed()
                        .hashUnencodedChars(typeName.toString())
                        .asInt(),
                36);
    }

    private static String typeNameToFieldBase(TypeName typeName) {
        if (typeName instanceof ParameterizedTypeName parameterized) {
            StringBuilder sb = new StringBuilder();
            String rawName = parameterized.rawType().simpleName();
            sb.append(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, rawName));
            for (TypeName arg : parameterized.typeArguments()) {
                String argBase = typeNameToFieldBase(arg);
                sb.append(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, argBase));
            }
            return sb.toString();
        } else if (typeName instanceof ClassName cn) {
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, cn.simpleName());
        } else {
            return "type" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, deterministicHash(typeName));
        }
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

    private MethodSpec clientImpl(
            EndpointDefinition def,
            Map<String, String> endpointToSerializerField,
            Map<String, String> endpointToDeserializerField) {
        List<ParameterSpec> params = parameterTypes.implementationMethodParams(def);
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

        TypeName returnType = getReturnType(def);
        methodBuilder.returns(returnType);

        CodeBlock.Builder requestParams = CodeBlock.builder();
        def.getAuth()
                .map(DefaultStaticFactoryMethodGenerator::generateAuthHeader)
                .ifPresent(requestParams::add);

        def.getArgs().stream()
                .map(param -> generateParam(def.getEndpointName().get(), param, endpointToSerializerField))
                .forEach(requestParams::add);

        CodeBlock request = CodeBlock.builder()
                .add("$T $L = $T.builder();", Request.Builder.class, REQUEST, Request.class)
                .add(requestParams.build())
                .build();
        String codeBlock = methodType.switchBy(
                "$L.clients().callBlocking($L, $L.build(), $L);", "$L.clients().call($L, $L.build" + "(), $L);");
        CodeBlock execute = CodeBlock.of(
                codeBlock,
                StaticFactoryMethodGenerator.RUNTIME,
                Names.endpointChannel(def),
                REQUEST,
                def.getReturns()
                        .filter(type -> !options.generateErrorParameterFormatRespectingDialogueInterfaces()
                                && returnTypes.isBinaryOrOptionalBinary(returnTypes.baseType(type)))
                        .map(type -> StaticFactoryMethodGenerator.RUNTIME
                                + (returnTypes.isOptionalBinary(returnTypes.baseType(type))
                                        ? ".bodySerDe().optionalInputStreamDeserializer()"
                                        : ".bodySerDe().inputStreamDeserializer()"))
                        .orElseGet(() -> endpointToDeserializerField.get(
                                def.getEndpointName().get())));

        methodBuilder.addCode(request);
        if (options.generateErrorParameterFormatRespectingDialogueInterfaces()) {
            // If the BodySerDe.errorParameterFormat field is set, add the header
            methodBuilder.addCode(CodeBlock.builder()
                    .beginControlFlow("if ($L.bodySerDe().errorParameterFormat().isPresent())", RUNTIME)
                    .add(
                            "$L.putHeaderParams($S, $L.bodySerDe().errorParameterFormat().get().toString());",
                            REQUEST,
                            ConjureErrorParameterFormats.ACCEPT_CONJURE_ERROR_PARAMETER_FORMAT_HEADER,
                            RUNTIME)
                    .endControlFlow()
                    .build());
        }
        methodBuilder.addCode(methodType.switchBy(def.getReturns().isPresent() ? "return " : "", "return "));
        methodBuilder.addCode(execute);

        return methodBuilder.build();
    }

    private TypeName getReturnType(EndpointDefinition def) {
        return methodType.switchBy(returnTypes.baseType(def.getReturns()), returnTypes.async(def.getReturns()));
    }

    private CodeBlock generateParam(
            String endpointName, ArgumentDefinition param, Map<String, String> endpointToSerializerField) {
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
                return CodeBlock.of(
                        "$L.body($L.serialize($L));",
                        REQUEST,
                        endpointToSerializerField.get(endpointName),
                        param.getArgName());
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
            .buildOrThrow();

    private static String primitiveTypeName(PrimitiveType in) {
        String typeName = PRIMITIVE_TO_TYPE_NAME.get(in.get());
        if (typeName == null) {
            throw new IllegalStateException("unrecognized primitive type: " + in);
        }
        return typeName;
    }

    private static MethodSpec toStringMethod(ClassName className) {
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

    private enum DeserializerType {
        STANDARD,
        EMPTY_BODY,
        BINARY,
        OPTIONAL_BINARY;
    }

    private record SerDeFieldsContext(
            Map<TypeName, String> typeMarkerFields,
            Map<TypeName, String> exceptionArgsFields,
            Map<TypeName, String> deserializerFieldNames,
            Map<TypeName, DeserializerType> deserializerTypes,
            Map<TypeName, String> serializerFieldNames,
            Map<String, String> endpointToDeserializerField,
            Map<String, String> endpointToSerializerField) {}

    private DeserializerType getDeserializerType(Optional<Type> type, TypeName className) {
        if (type.isEmpty()) {
            return DeserializerType.EMPTY_BODY;
        } else if (returnTypes.isBinary(className)) {
            return DeserializerType.BINARY;
        } else if (returnTypes.isOptionalBinary(className)) {
            return DeserializerType.OPTIONAL_BINARY;
        } else {
            return DeserializerType.STANDARD;
        }
    }
}
