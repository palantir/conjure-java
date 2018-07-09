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

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.FeatureFlags;
import com.palantir.conjure.java.types.JerseyMethodTypeClassNameVisitor;
import com.palantir.conjure.java.types.JerseyReturnTypeClassNameVisitor;
import com.palantir.conjure.java.types.TypeMapper;
import com.palantir.conjure.spec.ArgumentDefinition;
import com.palantir.conjure.spec.AuthType;
import com.palantir.conjure.spec.BodyParameterType;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.HeaderParameterType;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.ParameterId;
import com.palantir.conjure.spec.ParameterType;
import com.palantir.conjure.spec.PathParameterType;
import com.palantir.conjure.spec.QueryParameterType;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.visitor.AuthTypeVisitor;
import com.palantir.conjure.visitor.ParameterTypeVisitor;
import com.palantir.conjure.visitor.TypeVisitor;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

public final class JerseyServiceGenerator implements ServiceGenerator {
    private static final ClassName NOT_NULL = ClassName.get("javax.validation.constraints", "NotNull");

    private final Set<FeatureFlags> featureFlags;

    public JerseyServiceGenerator(Set<FeatureFlags> experimentalFeatures) {
        this.featureFlags = ImmutableSet.copyOf(experimentalFeatures);
    }

    @Override
    public Set<JavaFile> generate(ConjureDefinition conjureDefinition) {
        TypeMapper returnTypeMapper = new TypeMapper(
                conjureDefinition.getTypes(),
                types -> new JerseyReturnTypeClassNameVisitor(types));
        TypeMapper methodTypeMapper = new TypeMapper(
                conjureDefinition.getTypes(), JerseyMethodTypeClassNameVisitor::new);
        return conjureDefinition.getServices().stream()
                .map(serviceDef -> generateService(serviceDef, returnTypeMapper, methodTypeMapper))
                .collect(Collectors.toSet());
    }

    private JavaFile generateService(ServiceDefinition serviceDefinition,
            TypeMapper returnTypeMapper, TypeMapper methodTypeMapper) {
        TypeSpec.Builder serviceBuilder = TypeSpec.interfaceBuilder(serviceDefinition.getServiceName().getName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(ClassName.get("javax.ws.rs", "Consumes"))
                        .addMember("value", "$T.APPLICATION_JSON", ClassName.get("javax.ws.rs.core", "MediaType"))
                        .build())
                .addAnnotation(AnnotationSpec.builder(ClassName.get("javax.ws.rs", "Produces"))
                        .addMember("value", "$T.APPLICATION_JSON", ClassName.get("javax.ws.rs.core", "MediaType"))
                        .build())
                .addAnnotation(AnnotationSpec.builder(ClassName.get("javax.ws.rs", "Path"))
                        .addMember("value", "$S", "/")
                        .build())
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(JerseyServiceGenerator.class));

        serviceDefinition.getDocs().ifPresent(docs ->
                serviceBuilder.addJavadoc("$L", StringUtils.appendIfMissing(docs.get(), "\n")));

        serviceBuilder.addMethods(serviceDefinition.getEndpoints().stream()
                .map(endpoint -> generateServiceMethod(endpoint, returnTypeMapper, methodTypeMapper))
                .collect(Collectors.toList()));

        serviceBuilder.addMethods(serviceDefinition.getEndpoints().stream()
                .map(endpoint -> generateCompatibilityBackfillServiceMethods(endpoint, returnTypeMapper,
                        methodTypeMapper))
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));

        return JavaFile.builder(serviceDefinition.getServiceName().getPackage(), serviceBuilder.build())
                .indent("    ")
                .build();
    }

    private MethodSpec generateServiceMethod(
            EndpointDefinition endpointDef,
            TypeMapper returnTypeMapper,
            TypeMapper methodTypeMapper) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(endpointDef.getEndpointName().get())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(httpMethodToClassName(endpointDef.getHttpMethod().get().name()))
                .addParameters(createServiceMethodParameters(endpointDef, methodTypeMapper, true));

        // @Path("") is invalid in Feign JaxRs and equivalent to absent on an endpoint method
        String rawHttpPath = endpointDef.getHttpPath().get();
        String httpPath = rawHttpPath.startsWith("/") ? rawHttpPath.substring(1) : rawHttpPath;
        if (!httpPath.isEmpty()) {
            methodBuilder.addAnnotation(AnnotationSpec.builder(ClassName.get("javax.ws.rs", "Path"))
                        .addMember("value", "$S", httpPath)
                        .build());
        }

        if (endpointDef.getReturns().map(type -> type.accept(TypeVisitor.IS_BINARY)).orElse(false)) {
            methodBuilder.addAnnotation(AnnotationSpec.builder(ClassName.get("javax.ws.rs", "Produces"))
                    .addMember("value", "$T.APPLICATION_OCTET_STREAM", ClassName.get("javax.ws.rs.core", "MediaType"))
                    .build());
        }

        boolean consumesTypeIsBinary = endpointDef.getArgs().stream()
                .anyMatch(arg -> arg.getType().accept(TypeVisitor.IS_BINARY)
                        && arg.getParamType().accept(ParameterTypeVisitor.IS_BODY));

        if (consumesTypeIsBinary) {
            methodBuilder.addAnnotation(AnnotationSpec.builder(ClassName.get("javax.ws.rs", "Consumes"))
                    .addMember("value", "$T.APPLICATION_OCTET_STREAM", ClassName.get("javax.ws.rs.core", "MediaType"))
                    .build());
        }

        endpointDef.getDeprecated().ifPresent(deprecatedDocsValue -> methodBuilder.addAnnotation(
                ClassName.get("java.lang", "Deprecated")));

        ServiceGenerator.getJavaDoc(endpointDef).ifPresent(
                content -> methodBuilder.addJavadoc("$L", content));

        endpointDef.getReturns().ifPresent(type -> methodBuilder.returns(returnTypeMapper.getClassName(type)));

        return methodBuilder.build();
    }

    /** Provides a linear expansion of optional query arguments to improve Java back-compat. */
    private List<MethodSpec> generateCompatibilityBackfillServiceMethods(
            EndpointDefinition endpointDef,
            TypeMapper returnTypeMapper,
            TypeMapper methodTypeMapper) {

        List<ArgumentDefinition> args = Lists.newArrayList();
        List<ArgumentDefinition> queryArgs = Lists.newArrayList();

        for (ArgumentDefinition arg : endpointDef.getArgs()) {
            if (!arg.getParamType().accept(ParameterTypeVisitor.IS_QUERY)
                    || !arg.getType().accept(TYPE_DEFAULTABLE_PREDICATE)) {
                args.add(arg);
            } else {
                queryArgs.add(arg);
            }
        }

        List<MethodSpec> alternateMethods = Lists.newArrayList();
        for (int i = 0; i < queryArgs.size(); i++) {
            alternateMethods.add(createCompatibilityBackfillMethod(
                    endpointDef,
                    returnTypeMapper,
                    methodTypeMapper,
                    queryArgs.subList(i, queryArgs.size())));
        }

        return alternateMethods;
    }

    private MethodSpec createCompatibilityBackfillMethod(
            EndpointDefinition endpointDef,
            TypeMapper returnTypeMapper,
            TypeMapper methodTypeMapper,
            List<ArgumentDefinition> extraArgs) {
        // ensure the correct ordering of parameters by creating the complete sorted parameter list
        List<ParameterSpec> sortedParams = createServiceMethodParameters(endpointDef, methodTypeMapper, false);
        List<Optional<ArgumentDefinition>> sortedMaybeExtraArgs = sortedParams.stream().map(param ->
                extraArgs.stream()
                        .filter(arg -> arg.getArgName().get().equals(param.name))
                        .findFirst())
                .collect(Collectors.toList());

        // omit extraArgs from the back fill method signature
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(endpointDef.getEndpointName().get())
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .addAnnotation(Deprecated.class)
                .addParameters(IntStream.range(0, sortedParams.size())
                        .filter(i -> !sortedMaybeExtraArgs.get(i).isPresent())
                        .mapToObj(sortedParams::get)
                        .collect(Collectors.toList()));

        endpointDef.getReturns().ifPresent(type -> methodBuilder.returns(returnTypeMapper.getClassName(type)));

        // replace extraArgs with default values when invoking the complete method
        StringBuilder sb = new StringBuilder("return $N(");
        List<Object> values = IntStream.range(0, sortedParams.size()).mapToObj(i -> {
            Optional<ArgumentDefinition> maybeArgDef = sortedMaybeExtraArgs.get(i);
            if (maybeArgDef.isPresent()) {
                sb.append("$L, ");
                return maybeArgDef.get().getType().accept(TYPE_DEFAULT_VALUE);
            } else {
                sb.append("$N, ");
                return sortedParams.get(i);
            }
        }).collect(Collectors.toList());
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
            TypeMapper typeMapper,
            boolean withAnnotations) {
        List<ParameterSpec> parameterSpecs = new ArrayList<>();

        Optional<AuthType> auth = endpointDef.getAuth();
        createAuthParameter(auth, withAnnotations).ifPresent(parameterSpecs::add);

        List<ArgumentDefinition> sortedArgList = new ArrayList<>(endpointDef.getArgs());
        sortedArgList.sort(Comparator.comparing(o ->
                o.getParamType().accept(PARAM_SORT_ORDER) + o.getType().accept(TYPE_SORT_ORDER)));

        sortedArgList.forEach(def -> {
            parameterSpecs.add(createServiceMethodParameterArg(typeMapper, def, withAnnotations));
        });
        return ImmutableList.copyOf(parameterSpecs);
    }

    private ParameterSpec createServiceMethodParameterArg(TypeMapper typeMapper, ArgumentDefinition def,
            boolean withAnnotations) {
        ParameterSpec.Builder param = ParameterSpec.builder(
                typeMapper.getClassName(def.getType()), def.getArgName().get());
        if (withAnnotations) {
            getParamTypeAnnotation(def).ifPresent(param::addAnnotation);
            param.addAnnotations(createMarkers(typeMapper, def.getMarkers()));
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
        String tokenName;

        if (auth.get().accept(AuthTypeVisitor.IS_HEADER)) {
            annotationClassName = ClassName.get("javax.ws.rs", "HeaderParam");
            tokenClassName = ClassName.get("com.palantir.tokens.auth", "AuthHeader");
            paramName = "authHeader";
            tokenName = "Authorization";
        } else if (auth.get().accept(AuthTypeVisitor.IS_COOKIE)) {
            annotationClassName = ClassName.get("javax.ws.rs", "CookieParam");
            tokenClassName = ClassName.get("com.palantir.tokens.auth", "BearerToken");
            paramName = "token";
            tokenName = auth.get().accept(AuthTypeVisitor.COOKIE).getCookieName();
        } else {
            throw new IllegalStateException("Unrecognized auth type: " + auth.get());
        }

        ParameterSpec.Builder paramSpec = ParameterSpec.builder(tokenClassName, paramName);
        if (withAnnotations) {
            paramSpec.addAnnotation(AnnotationSpec.builder(annotationClassName)
                    .addMember("value", "$S", tokenName).build());
            if (featureFlags.contains(FeatureFlags.RequireNotNullAuthAndBodyParams)) {
                paramSpec.addAnnotation(AnnotationSpec.builder(NOT_NULL).build());
            }
        }
        return Optional.of(paramSpec.build());
    }

    private Optional<AnnotationSpec> getParamTypeAnnotation(ArgumentDefinition def) {
        AnnotationSpec.Builder annotationSpecBuilder;
        ParameterType paramType = def.getParamType();
        if (paramType.accept(ParameterTypeVisitor.IS_PATH)) {
            annotationSpecBuilder = AnnotationSpec.builder(ClassName.get("javax.ws.rs", "PathParam"))
                    .addMember("value", "$S", def.getArgName().get());
        } else if (paramType.accept(ParameterTypeVisitor.IS_QUERY)) {
            ParameterId paramId = paramType.accept(ParameterTypeVisitor.QUERY).getParamId();
            annotationSpecBuilder = AnnotationSpec.builder(ClassName.get("javax.ws.rs", "QueryParam"))
                    .addMember("value", "$S", paramId.get());
        } else if (paramType.accept(ParameterTypeVisitor.IS_HEADER)) {
            ParameterId paramId = paramType.accept(ParameterTypeVisitor.HEADER).getParamId();
            annotationSpecBuilder = AnnotationSpec.builder(ClassName.get("javax.ws.rs", "HeaderParam"))
                    .addMember("value", "$S", paramId.get());
        } else if (paramType.accept(ParameterTypeVisitor.IS_BODY)) {
            /* no annotations for body parameters */
            if (def.getType().accept(TypeVisitor.IS_OPTIONAL)
                    || !featureFlags.contains(FeatureFlags.RequireNotNullAuthAndBodyParams)) {
                return Optional.empty();
            }
            annotationSpecBuilder = AnnotationSpec
                    .builder(ClassName.get("javax.validation.constraints", "NotNull"));
            // Add test for optional
        } else {
            throw new IllegalStateException("Unrecognized argument type: " + def.getParamType());
        }

        return Optional.of(annotationSpecBuilder.build());
    }

    private static Set<AnnotationSpec> createMarkers(TypeMapper typeMapper, List<Type> markers) {
        checkArgument(markers.stream().allMatch(type -> type.accept(TypeVisitor.IS_REFERENCE)),
                "Markers must refer to reference types.");
        return markers.stream()
                .map(typeMapper::getClassName)
                .map(ClassName.class::cast)
                .map(AnnotationSpec::builder)
                .map(AnnotationSpec.Builder::build)
                .collect(Collectors.toSet());
    }

    private static ClassName httpMethodToClassName(String method) {
        switch (method) {
            case "DELETE":
                return ClassName.get("javax.ws.rs", "DELETE");
            case "GET":
                return ClassName.get("javax.ws.rs", "GET");
            case "PUT":
                return ClassName.get("javax.ws.rs", "PUT");
            case "POST":
                return ClassName.get("javax.ws.rs", "POST");
            default:
                throw new IllegalArgumentException("Unrecognized HTTP method: " + method);
        }
    }

    /** Produces an ordering for ParamaterType of Header, Path, Query, Body. */
    private static final ParameterType.Visitor<Integer> PARAM_SORT_ORDER = new ParameterType.Visitor<Integer>() {
        @Override
        public Integer visitBody(BodyParameterType value) {
            return 30;
        }

        @Override
        public Integer visitHeader(HeaderParameterType value) {
            return 0;
        }

        @Override
        public Integer visitPath(PathParameterType value) {
            return 10;
        }

        @Override
        public Integer visitQuery(QueryParameterType value) {
            return 20;
        }

        @Override
        public Integer visitUnknown(String unknownType) {
            return -1;
        }
    };

    /**
     * Produces a type sort ordering for use with {@link #PARAM_SORT_ORDER} such that types with known defaults come
     * after types without known defaults.
     */
    private static final Type.Visitor<Integer> TYPE_SORT_ORDER = new TypeVisitor.Default<Integer>() {
        @Override
        public Integer visitOptional(OptionalType value) {
            return 1;
        }

        @Override
        public Integer visitList(ListType value) {
            return 1;
        }

        @Override
        public Integer visitSet(SetType value) {
            return 1;
        }

        @Override
        public Integer visitMap(MapType value) {
            return 1;
        }

        @Override
        public Integer visitDefault() {
            return 0;
        }
    };

    /** Indicates whether a particular type has a defaultable value. */
    private static final Type.Visitor<Boolean> TYPE_DEFAULTABLE_PREDICATE = new TypeVisitor.Default<Boolean>() {
        @Override
        public Boolean visitOptional(OptionalType value) {
            return true;
        }

        @Override
        public Boolean visitList(ListType value) {
            return true;
        }

        @Override
        public Boolean visitSet(SetType value) {
            return true;
        }

        @Override
        public Boolean visitMap(MapType value) {
            return true;
        }

        @Override
        public Boolean visitDefault() {
            return false;
        }
    };

    private static final Type.Visitor<CodeBlock> TYPE_DEFAULT_VALUE = new TypeVisitor.Default<CodeBlock>() {
        @Override
        public CodeBlock visitOptional(OptionalType value) {
            return CodeBlock.of("$T.empty()", Optional.class);
        }

        @Override
        public CodeBlock visitList(ListType value) {
            return CodeBlock.of("$T.emptyList()", Collections.class);
        }

        @Override
        public CodeBlock visitSet(SetType value) {
            return CodeBlock.of("$T.emptySet()", Collections.class);
        }

        @Override
        public CodeBlock visitMap(MapType value) {
            return CodeBlock.of("$T.emptyMap()", Collections.class);
        }

        @Override
        public CodeBlock visitDefault() {
            throw new IllegalArgumentException("Cannot backfill non-defaultable parameter type.");
        }
    };
}
