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
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.types.DefaultClassNameVisitor;
import com.palantir.conjure.java.types.ReturnTypeClassNameVisitor;
import com.palantir.conjure.java.types.SpecializeBinaryClassNameVisitor;
import com.palantir.conjure.java.types.TypeMapper;
import com.palantir.conjure.java.util.Javadoc;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.java.util.ParameterOrder;
import com.palantir.conjure.spec.ArgumentDefinition;
import com.palantir.conjure.spec.AuthType;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.ParameterId;
import com.palantir.conjure.spec.ParameterType;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.visitor.AuthTypeVisitor;
import com.palantir.conjure.visitor.ParameterTypeVisitor;
import com.palantir.conjure.visitor.TypeVisitor;
import com.palantir.logsafe.Preconditions;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.lang.model.element.Modifier;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

public final class JerseyServiceGenerator implements ServiceGenerator {

    private static final ClassName NOT_NULL = ClassName.get("javax.validation.constraints", "NotNull");

    private static final ClassName BINARY_ARGUMENT_TYPE = ClassName.get(InputStream.class);
    private static final ClassName BINARY_RETURN_TYPE_RESPONSE = ClassName.get(Response.class);
    private static final ClassName BINARY_RETURN_TYPE_OUTPUT = ClassName.get(StreamingOutput.class);
    private static final TypeName OPTIONAL_BINARY_RETURN_TYPE =
            ParameterizedTypeName.get(ClassName.get(Optional.class), BINARY_RETURN_TYPE_OUTPUT);

    private final Options options;

    public JerseyServiceGenerator(Options options) {
        this.options = options;
    }

    @Override
    public Set<JavaFile> generate(ConjureDefinition conjureDefinition) {
        ClassName binaryReturnType =
                options.jerseyBinaryAsResponse() ? BINARY_RETURN_TYPE_RESPONSE : BINARY_RETURN_TYPE_OUTPUT;

        TypeName optionalBinaryReturnType =
                options.jerseyBinaryAsResponse() ? BINARY_RETURN_TYPE_RESPONSE : OPTIONAL_BINARY_RETURN_TYPE;

        TypeMapper returnTypeMapper = new TypeMapper(
                conjureDefinition.getTypes(),
                new ReturnTypeClassNameVisitor(
                        conjureDefinition.getTypes(), binaryReturnType, optionalBinaryReturnType, options));

        TypeMapper argumentTypeMapper = new TypeMapper(
                conjureDefinition.getTypes(),
                new SpecializeBinaryClassNameVisitor(
                        new DefaultClassNameVisitor(conjureDefinition.getTypes(), options), BINARY_ARGUMENT_TYPE));

        return conjureDefinition.getServices().stream()
                .map(serviceDef -> generateService(serviceDef, returnTypeMapper, argumentTypeMapper))
                .collect(Collectors.toSet());
    }

    private JavaFile generateService(
            ServiceDefinition serviceDefinition, TypeMapper returnTypeMapper, TypeMapper argumentTypeMapper) {
        com.palantir.conjure.spec.TypeName prefixedName =
                Packages.getPrefixedName(serviceDefinition.getServiceName(), options.packagePrefix());
        TypeSpec.Builder serviceBuilder = TypeSpec.interfaceBuilder(prefixedName.getName())
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

        serviceDefinition.getDocs().ifPresent(docs -> serviceBuilder.addJavadoc("$L", Javadoc.render(docs)));

        serviceBuilder.addMethods(serviceDefinition.getEndpoints().stream()
                .map(endpoint -> generateServiceMethod(endpoint, returnTypeMapper, argumentTypeMapper))
                .collect(Collectors.toList()));

        serviceBuilder.addMethods(serviceDefinition.getEndpoints().stream()
                .map(endpoint ->
                        generateCompatibilityBackfillServiceMethods(endpoint, returnTypeMapper, argumentTypeMapper))
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));

        return JavaFile.builder(prefixedName.getPackage(), serviceBuilder.build())
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }

    private MethodSpec generateServiceMethod(
            EndpointDefinition endpointDef, TypeMapper returnTypeMapper, TypeMapper argumentTypeMapper) {
        TypeName returnType =
                endpointDef.getReturns().map(returnTypeMapper::getClassName).orElse(ClassName.VOID);

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(
                        endpointDef.getEndpointName().get())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(
                        httpMethodToClassName(endpointDef.getHttpMethod().get().name()))
                .addParameters(createServiceMethodParameters(endpointDef, argumentTypeMapper, true))
                .returns(returnType);

        // @Path("") is invalid in Feign JaxRs and equivalent to absent on an endpoint method
        String rawHttpPath = endpointDef.getHttpPath().get();
        String httpPath = rawHttpPath.startsWith("/") ? rawHttpPath.substring(1) : rawHttpPath;
        if (!httpPath.isEmpty()) {
            methodBuilder.addAnnotation(AnnotationSpec.builder(ClassName.get("javax.ws.rs", "Path"))
                    .addMember("value", "$S", httpPath)
                    .build());
        }

        if (returnType.equals(BINARY_RETURN_TYPE_OUTPUT)
                || returnType.equals(BINARY_RETURN_TYPE_RESPONSE)
                || returnType.equals(OPTIONAL_BINARY_RETURN_TYPE)) {
            methodBuilder.addAnnotation(AnnotationSpec.builder(ClassName.get("javax.ws.rs", "Produces"))
                    .addMember("value", "$T.APPLICATION_OCTET_STREAM", ClassName.get("javax.ws.rs.core", "MediaType"))
                    .build());
        }

        boolean consumesTypeIsBinary = endpointDef.getArgs().stream()
                .map(ArgumentDefinition::getType)
                .map(argumentTypeMapper::getClassName)
                .anyMatch(BINARY_ARGUMENT_TYPE::equals);

        if (consumesTypeIsBinary) {
            methodBuilder.addAnnotation(AnnotationSpec.builder(ClassName.get("javax.ws.rs", "Consumes"))
                    .addMember("value", "$T.APPLICATION_OCTET_STREAM", ClassName.get("javax.ws.rs.core", "MediaType"))
                    .build());
        }

        endpointDef
                .getDeprecated()
                .ifPresent(
                        deprecatedDocsValue -> methodBuilder.addAnnotation(ClassName.get("java.lang", "Deprecated")));

        ServiceGenerator.getJavaDoc(endpointDef).ifPresent(content -> methodBuilder.addJavadoc("$L", content));

        return methodBuilder.build();
    }

    /** Provides a linear expansion of optional query arguments to improve Java back-compat. */
    private List<MethodSpec> generateCompatibilityBackfillServiceMethods(
            EndpointDefinition endpointDef, TypeMapper returnTypeMapper, TypeMapper argumentTypeMapper) {
        List<ArgumentDefinition> headerArgs = new ArrayList<>();
        List<ArgumentDefinition> queryArgs = new ArrayList<>();

        for (ArgumentDefinition arg : endpointDef.getArgs()) {
            if (arg.getParamType().accept(ParameterTypeVisitor.IS_HEADER)
                    && arg.getType().accept(DefaultableTypeVisitor.INSTANCE)) {
                headerArgs.add(arg);
            }
            if (arg.getParamType().accept(ParameterTypeVisitor.IS_QUERY)
                    && arg.getType().accept(DefaultableTypeVisitor.INSTANCE)) {
                queryArgs.add(arg);
            }
        }

        List<MethodSpec> alternateMethods = new ArrayList<>();
        for (int j = 0; j <= headerArgs.size(); j++) {
            for (int i = 0; i <= queryArgs.size(); i++) {
                List<ArgumentDefinition> extraHeaderArgs = headerArgs.subList(j, headerArgs.size());
                List<ArgumentDefinition> extraQueryArgs = queryArgs.subList(i, queryArgs.size());
                List<ArgumentDefinition> extraArgs = new ArrayList<>(extraHeaderArgs);
                extraArgs.addAll(extraQueryArgs);
                if (!extraArgs.isEmpty()) {
                    alternateMethods.add(createCompatibilityBackfillMethod(
                            endpointDef, returnTypeMapper, argumentTypeMapper, extraArgs));
                }
            }
        }

        return alternateMethods;
    }

    private MethodSpec createCompatibilityBackfillMethod(
            EndpointDefinition endpointDef,
            TypeMapper returnTypeMapper,
            TypeMapper argumentTypeMapper,
            List<ArgumentDefinition> extraArgs) {
        // ensure the correct ordering of parameters by creating the complete sorted parameter list
        List<ParameterSpec> sortedParams = createServiceMethodParameters(endpointDef, argumentTypeMapper, false);
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
            EndpointDefinition endpointDef, TypeMapper typeMapper, boolean withAnnotations) {
        List<ParameterSpec> parameterSpecs = new ArrayList<>();

        Optional<AuthType> auth = endpointDef.getAuth();
        createAuthParameter(auth, withAnnotations).ifPresent(parameterSpecs::add);

        ParameterOrder.sorted(endpointDef.getArgs())
                .forEach(def -> parameterSpecs.add(createServiceMethodParameterArg(typeMapper, def, withAnnotations)));
        return ImmutableList.copyOf(parameterSpecs);
    }

    private ParameterSpec createServiceMethodParameterArg(
            TypeMapper typeMapper, ArgumentDefinition def, boolean withAnnotations) {
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
            paramName = Auth.AUTH_HEADER_PARAM_NAME;
            tokenName = "Authorization";
        } else if (auth.get().accept(AuthTypeVisitor.IS_COOKIE)) {
            annotationClassName = ClassName.get("javax.ws.rs", "CookieParam");
            tokenClassName = ClassName.get("com.palantir.tokens.auth", "BearerToken");
            paramName = Auth.COOKIE_AUTH_PARAM_NAME;
            tokenName = auth.get().accept(AuthTypeVisitor.COOKIE).getCookieName();
        } else {
            throw new IllegalStateException("Unrecognized auth type: " + auth.get());
        }

        ParameterSpec.Builder paramSpec = ParameterSpec.builder(tokenClassName, paramName);
        if (withAnnotations) {
            paramSpec.addAnnotation(AnnotationSpec.builder(annotationClassName)
                    .addMember("value", "$S", tokenName)
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
            if (def.getType().accept(TypeVisitor.IS_OPTIONAL) || !options.requireNotNullAuthAndBodyParams()) {
                return Optional.empty();
            }
            annotationSpecBuilder = AnnotationSpec.builder(NOT_NULL);
        } else {
            throw new IllegalStateException("Unrecognized argument type: " + def.getParamType());
        }

        return Optional.of(annotationSpecBuilder.build());
    }

    private static Set<AnnotationSpec> createMarkers(TypeMapper typeMapper, List<Type> markers) {
        Preconditions.checkArgument(
                markers.stream().allMatch(type -> type.accept(TypeVisitor.IS_REFERENCE)),
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
        }
        throw new IllegalArgumentException("Unrecognized HTTP method: " + method);
    }
}
