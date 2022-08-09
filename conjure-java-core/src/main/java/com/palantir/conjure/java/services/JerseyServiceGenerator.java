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

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.ConjureMarkers;
import com.palantir.conjure.java.ConjureTags;
import com.palantir.conjure.java.Generator;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.types.ClassNameVisitor;
import com.palantir.conjure.java.types.DefaultClassNameVisitor;
import com.palantir.conjure.java.types.SafetyEvaluator;
import com.palantir.conjure.java.types.SpecializeBinaryClassNameVisitor;
import com.palantir.conjure.java.types.TypeMapper;
import com.palantir.conjure.java.util.Javadoc;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.java.util.ParameterOrder;
import com.palantir.conjure.java.util.TypeFunctions;
import com.palantir.conjure.java.visitor.DefaultableTypeVisitor;
import com.palantir.conjure.spec.ArgumentDefinition;
import com.palantir.conjure.spec.AuthType;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.LogSafety;
import com.palantir.conjure.spec.ParameterId;
import com.palantir.conjure.spec.ParameterType;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.visitor.AuthTypeVisitor;
import com.palantir.conjure.visitor.ParameterTypeVisitor;
import com.palantir.conjure.visitor.TypeVisitor;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.lang.model.element.Modifier;

public final class JerseyServiceGenerator implements Generator {

    private static final ClassName NOT_NULL = ClassName.get("javax.validation.constraints", "NotNull");

    private static final ClassName BINARY_ARGUMENT_TYPE = ClassName.get(InputStream.class);

    private final ClassName binaryReturnTypeResponse;
    private final ClassName binaryReturnTypeOutput;

    private final TypeName optionalBinaryReturnType;

    private final Options options;

    public JerseyServiceGenerator(Options options) {
        this.options = options;
        this.binaryReturnTypeResponse = ClassName.get(jaxrsPackage("core"), "Response");
        this.binaryReturnTypeOutput = ClassName.get(jaxrsPackage("core"), "StreamingOutput");
        this.optionalBinaryReturnType =
                ParameterizedTypeName.get(ClassName.get(Optional.class), binaryReturnTypeOutput);
    }

    @Override
    public Stream<JavaFile> generate(ConjureDefinition conjureDefinition) {
        ClassName binaryReturnType =
                options.jerseyBinaryAsResponse() ? binaryReturnTypeResponse : binaryReturnTypeOutput;

        TypeName optionalBinaryReturnTypeGenerated =
                options.jerseyBinaryAsResponse() ? binaryReturnTypeResponse : optionalBinaryReturnType;

        Map<com.palantir.conjure.spec.TypeName, TypeDefinition> types = TypeFunctions.toTypesMap(conjureDefinition);
        ClassNameVisitor defaultVisitor = new DefaultClassNameVisitor(types.keySet(), options);
        SafetyEvaluator safetyEvaluator = new SafetyEvaluator(types);
        TypeMapper returnTypeMapper = new TypeMapper(
                types,
                new SpecializeBinaryClassNameVisitor(
                        defaultVisitor, types, binaryReturnType, optionalBinaryReturnTypeGenerated));

        TypeMapper argumentTypeMapper = new TypeMapper(
                types, new SpecializeBinaryClassNameVisitor(defaultVisitor, types, BINARY_ARGUMENT_TYPE));

        return conjureDefinition.getServices().stream()
                .map(serviceDef -> generateService(serviceDef, safetyEvaluator, returnTypeMapper, argumentTypeMapper));
    }

    private JavaFile generateService(
            ServiceDefinition serviceDefinition,
            SafetyEvaluator safetyEvaluator,
            TypeMapper returnTypeMapper,
            TypeMapper argumentTypeMapper) {
        com.palantir.conjure.spec.TypeName prefixedName =
                Packages.getPrefixedName(serviceDefinition.getServiceName(), options.packagePrefix());
        TypeSpec.Builder serviceBuilder = TypeSpec.interfaceBuilder(prefixedName.getName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(ClassName.get(jaxrsPackage(), "Consumes"))
                        .addMember("value", "$T.APPLICATION_JSON", ClassName.get(jaxrsPackage("core"), "MediaType"))
                        .build())
                .addAnnotation(AnnotationSpec.builder(ClassName.get(jaxrsPackage(), "Produces"))
                        .addMember("value", "$T.APPLICATION_JSON", ClassName.get(jaxrsPackage("core"), "MediaType"))
                        .build())
                .addAnnotation(AnnotationSpec.builder(ClassName.get(jaxrsPackage(), "Path"))
                        .addMember("value", "$S", "/")
                        .build())
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(JerseyServiceGenerator.class));

        serviceDefinition.getDocs().ifPresent(docs -> serviceBuilder.addJavadoc("$L", Javadoc.render(docs)));

        serviceBuilder.addMethods(serviceDefinition.getEndpoints().stream()
                .map(endpoint -> generateServiceMethod(endpoint, safetyEvaluator, returnTypeMapper, argumentTypeMapper))
                .collect(Collectors.toList()));

        serviceBuilder.addMethods(serviceDefinition.getEndpoints().stream()
                .map(endpoint -> generateCompatibilityBackfillServiceMethods(
                        endpoint, safetyEvaluator, returnTypeMapper, argumentTypeMapper))
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));

        return JavaFile.builder(prefixedName.getPackage(), serviceBuilder.build())
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }

    private MethodSpec generateServiceMethod(
            EndpointDefinition endpointDef,
            SafetyEvaluator safetyEvaluator,
            TypeMapper returnTypeMapper,
            TypeMapper argumentTypeMapper) {
        TypeName returnType =
                endpointDef.getReturns().map(returnTypeMapper::getClassName).orElse(ClassName.VOID);

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(
                        endpointDef.getEndpointName().get())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(
                        httpMethodToClassName(endpointDef.getHttpMethod().get().name()))
                .addParameters(createServiceMethodParameters(endpointDef, safetyEvaluator, argumentTypeMapper, true))
                .returns(returnType);

        // @Path("") is invalid in Feign JaxRs and equivalent to absent on an endpoint method
        String rawHttpPath = endpointDef.getHttpPath().get();
        String httpPath = rawHttpPath.startsWith("/") ? rawHttpPath.substring(1) : rawHttpPath;
        if (!httpPath.isEmpty()) {
            methodBuilder.addAnnotation(AnnotationSpec.builder(ClassName.get(jaxrsPackage(), "Path"))
                    .addMember("value", "$S", httpPath)
                    .build());
        }

        if (returnType.equals(binaryReturnTypeOutput)
                || returnType.equals(binaryReturnTypeResponse)
                || returnType.equals(optionalBinaryReturnType)) {
            methodBuilder.addAnnotation(AnnotationSpec.builder(ClassName.get(jaxrsPackage(), "Produces"))
                    .addMember("value", "$T.APPLICATION_OCTET_STREAM", ClassName.get(jaxrsPackage("core"), "MediaType"))
                    .build());
        }

        boolean consumesTypeIsBinary = endpointDef.getArgs().stream()
                .map(ArgumentDefinition::getType)
                .map(argumentTypeMapper::getClassName)
                .anyMatch(BINARY_ARGUMENT_TYPE::equals);

        if (consumesTypeIsBinary) {
            methodBuilder.addAnnotation(AnnotationSpec.builder(ClassName.get(jaxrsPackage(), "Consumes"))
                    .addMember("value", "$T.APPLICATION_OCTET_STREAM", ClassName.get(jaxrsPackage("core"), "MediaType"))
                    .build());
        }

        endpointDef
                .getDeprecated()
                .ifPresent(
                        deprecatedDocsValue -> methodBuilder.addAnnotation(ClassName.get("java.lang", "Deprecated")));

        methodBuilder.addAnnotations(ConjureAnnotations.getClientEndpointAnnotations(endpointDef));

        ServiceGenerators.getJavaDoc(endpointDef).ifPresent(content -> methodBuilder.addJavadoc("$L", content));

        return methodBuilder.build();
    }

    /** Provides a linear expansion of optional query arguments to improve Java back-compat. */
    private List<MethodSpec> generateCompatibilityBackfillServiceMethods(
            EndpointDefinition endpointDef,
            SafetyEvaluator safetyEvaluator,
            TypeMapper returnTypeMapper,
            TypeMapper argumentTypeMapper) {
        List<ArgumentDefinition> queryArgs = new ArrayList<>();

        for (ArgumentDefinition arg : endpointDef.getArgs()) {
            if (arg.getParamType().accept(ParameterTypeVisitor.IS_QUERY)
                    && arg.getType().accept(DefaultableTypeVisitor.INSTANCE)) {
                queryArgs.add(arg);
            }
        }

        List<MethodSpec> alternateMethods = new ArrayList<>();
        for (int i = 0; i < queryArgs.size(); i++) {
            alternateMethods.add(createCompatibilityBackfillMethod(
                    endpointDef,
                    safetyEvaluator,
                    returnTypeMapper,
                    argumentTypeMapper,
                    queryArgs.subList(i, queryArgs.size())));
        }

        return alternateMethods;
    }

    private MethodSpec createCompatibilityBackfillMethod(
            EndpointDefinition endpointDef,
            SafetyEvaluator safetyEvaluator,
            TypeMapper returnTypeMapper,
            TypeMapper argumentTypeMapper,
            List<ArgumentDefinition> extraArgs) {
        // ensure the correct ordering of parameters by creating the complete sorted parameter list
        List<ParameterSpec> sortedParams =
                createServiceMethodParameters(endpointDef, safetyEvaluator, argumentTypeMapper, false);
        List<Optional<ArgumentDefinition>> sortedMaybeExtraArgs = sortedParams.stream()
                .map(param -> extraArgs.stream()
                        .filter(arg -> arg.getArgName().get().equals(param.name))
                        .findFirst())
                .collect(Collectors.toList());

        // omit extraArgs from the back fill method signature
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(
                        endpointDef.getEndpointName().get())
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .addAnnotation(Deprecated.class)
                .addAnnotations(ConjureAnnotations.getClientEndpointAnnotations(endpointDef))
                .addParameters(IntStream.range(0, sortedParams.size())
                        .filter(i -> !sortedMaybeExtraArgs.get(i).isPresent())
                        .mapToObj(sortedParams::get)
                        .collect(Collectors.toList()));

        endpointDef.getReturns().ifPresent(type -> methodBuilder.returns(returnTypeMapper.getClassName(type)));

        // replace extraArgs with default values when invoking the complete method
        StringBuilder sb = new StringBuilder(endpointDef.getReturns().isPresent() ? "return $N(" : "$N(");
        List<Object> values = IntStream.range(0, sortedParams.size())
                .mapToObj(i -> {
                    Optional<ArgumentDefinition> maybeArgDef = sortedMaybeExtraArgs.get(i);
                    if (maybeArgDef.isPresent()) {
                        sb.append("$L, ");
                        return maybeArgDef.get().getType().accept(DefaultTypeValueVisitor.INSTANCE);
                    } else {
                        sb.append("$N, ");
                        return sortedParams.get(i);
                    }
                })
                .collect(Collectors.toList());
        // trim the end
        sb.setLength(sb.length() - 2);
        sb.append(")");

        ImmutableList<Object> methodCallArgs = ImmutableList.builder()
                .add(endpointDef.getEndpointName().get())
                .addAll(values)
                .build();

        methodBuilder.addStatement(sb.toString(), methodCallArgs.toArray(new Object[0]));

        return methodBuilder.build();
    }

    private List<ParameterSpec> createServiceMethodParameters(
            EndpointDefinition endpointDef,
            SafetyEvaluator safetyEvaluator,
            TypeMapper typeMapper,
            boolean withAnnotations) {
        List<ParameterSpec> parameterSpecs = new ArrayList<>();

        Optional<AuthType> auth = endpointDef.getAuth();
        createAuthParameter(auth, withAnnotations).ifPresent(parameterSpecs::add);

        ParameterOrder.sorted(endpointDef.getArgs())
                .forEach(def -> parameterSpecs.add(
                        createServiceMethodParameterArg(typeMapper, safetyEvaluator, def, withAnnotations)));
        return ImmutableList.copyOf(parameterSpecs);
    }

    private ParameterSpec createServiceMethodParameterArg(
            TypeMapper typeMapper, SafetyEvaluator safetyEvaluator, ArgumentDefinition def, boolean withAnnotations) {
        Optional<LogSafety> declaredSafety = ConjureTags.validateArgument(def, safetyEvaluator);
        ParameterSpec.Builder param = ParameterSpec.builder(
                        typeMapper.getClassName(def.getType()), def.getArgName().get())
                .addAnnotations(ConjureAnnotations.safety(declaredSafety));
        if (withAnnotations) {
            getParamTypeAnnotation(def).ifPresent(param::addAnnotation);
            param.addAnnotations(ConjureMarkers.annotations(typeMapper, def.getMarkers()));
        }
        return param.build();
    }

    private Optional<ParameterSpec> createAuthParameter(Optional<AuthType> auth, boolean withAnnotations) {
        if (!auth.isPresent()) {
            return Optional.empty();
        }

        ClassName annotationClassName;
        ClassName tokenClassName;
        String paramName;
        String value;

        if (auth.get().accept(AuthTypeVisitor.IS_HEADER)) {
            annotationClassName = ClassName.get(jaxrsPackage(), "HeaderParam");
            tokenClassName = ClassName.get("com.palantir.tokens.auth", "AuthHeader");
            paramName = Auth.AUTH_HEADER_PARAM_NAME;
            value = Auth.AUTH_HEADER_NAME;
        } else if (auth.get().accept(AuthTypeVisitor.IS_COOKIE)) {
            annotationClassName = ClassName.get(jaxrsPackage(), "CookieParam");
            tokenClassName = ClassName.get("com.palantir.tokens.auth", "BearerToken");
            paramName = Auth.COOKIE_AUTH_PARAM_NAME;
            value = auth.get().accept(AuthTypeVisitor.COOKIE).getCookieName();
        } else {
            throw new IllegalStateException("Unrecognized auth type: " + auth.get());
        }

        ParameterSpec.Builder paramSpec = ParameterSpec.builder(tokenClassName, paramName);
        if (withAnnotations) {
            paramSpec.addAnnotation(AnnotationSpec.builder(annotationClassName)
                    .addMember("value", "$S", value)
                    .build());
            if (options.requireNotNullAuthAndBodyParams()) {
                paramSpec.addAnnotation(AnnotationSpec.builder(NOT_NULL).build());
            }
        }
        return Optional.of(paramSpec.build());
    }

    private Optional<AnnotationSpec> getParamTypeAnnotation(ArgumentDefinition def) {
        AnnotationSpec.Builder annotationSpecBuilder;
        ParameterType paramType = def.getParamType();
        if (paramType.accept(ParameterTypeVisitor.IS_PATH)) {
            annotationSpecBuilder = AnnotationSpec.builder(ClassName.get(jaxrsPackage(), "PathParam"))
                    .addMember("value", "$S", def.getArgName().get());
        } else if (paramType.accept(ParameterTypeVisitor.IS_QUERY)) {
            ParameterId paramId = paramType.accept(ParameterTypeVisitor.QUERY).getParamId();
            annotationSpecBuilder = AnnotationSpec.builder(ClassName.get(jaxrsPackage(), "QueryParam"))
                    .addMember("value", "$S", paramId.get());
        } else if (paramType.accept(ParameterTypeVisitor.IS_HEADER)) {
            ParameterId paramId = paramType.accept(ParameterTypeVisitor.HEADER).getParamId();
            annotationSpecBuilder = AnnotationSpec.builder(ClassName.get(jaxrsPackage(), "HeaderParam"))
                    .addMember("value", "$S", paramId.get());
        } else if (paramType.accept(ParameterTypeVisitor.IS_BODY)) {
            if (def.getType().accept(TypeVisitor.IS_OPTIONAL) || !options.requireNotNullAuthAndBodyParams()) {
                return Optional.empty();
            }
            annotationSpecBuilder = AnnotationSpec.builder(NOT_NULL);
        } else {
            throw new IllegalStateException("Unrecognized argument type: " + def.getParamType());
        }

        return Optional.of(annotationSpecBuilder.build());
    }

    private ClassName httpMethodToClassName(String method) {
        switch (method) {
            case "DELETE":
                return ClassName.get(jaxrsPackage(), "DELETE");
            case "GET":
                return ClassName.get(jaxrsPackage(), "GET");
            case "PUT":
                return ClassName.get(jaxrsPackage(), "PUT");
            case "POST":
                return ClassName.get(jaxrsPackage(), "POST");
        }
        throw new IllegalArgumentException("Unrecognized HTTP method: " + method);
    }

    private String jaxrsPackage() {
        return options.jakartaPackages() ? "jakarta.ws.rs" : "javax.ws.rs";
    }

    private String jaxrsPackage(String rest) {
        return jaxrsPackage() + "." + rest;
    }
}
