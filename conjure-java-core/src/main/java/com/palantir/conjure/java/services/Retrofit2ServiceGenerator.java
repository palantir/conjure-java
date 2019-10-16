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

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.FeatureFlags;
import com.palantir.conjure.java.types.DefaultClassNameVisitor;
import com.palantir.conjure.java.types.ReturnTypeClassNameVisitor;
import com.palantir.conjure.java.types.SpecializeBinaryClassNameVisitor;
import com.palantir.conjure.java.types.TypeMapper;
import com.palantir.conjure.java.util.Javadoc;
import com.palantir.conjure.java.util.ParameterOrder;
import com.palantir.conjure.spec.ArgumentDefinition;
import com.palantir.conjure.spec.ArgumentName;
import com.palantir.conjure.spec.AuthType;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.HttpPath;
import com.palantir.conjure.spec.ParameterId;
import com.palantir.conjure.spec.ParameterType;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.visitor.AuthTypeVisitor;
import com.palantir.conjure.visitor.ParameterTypeVisitor;
import com.palantir.logsafe.Preconditions;
import com.palantir.util.syntacticpath.Path;
import com.palantir.util.syntacticpath.Paths;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.lang.model.element.Modifier;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Retrofit2ServiceGenerator implements ServiceGenerator {

    private static final ClassName COMPLETABLE_FUTURE_TYPE = ClassName.get("java.util.concurrent", "CompletableFuture");
    private static final ClassName LISTENABLE_FUTURE_TYPE =
            ClassName.get("com.google.common.util.concurrent", "ListenableFuture");
    private static final ClassName CALL_TYPE = ClassName.get("retrofit2", "Call");
    private static final String AUTH_HEADER_NAME = "Authorization";

    private static final ClassName BINARY_ARGUMENT_TYPE = ClassName.get("okhttp3", "RequestBody");
    private static final ClassName BINARY_RETURN_TYPE = ClassName.get("okhttp3", "ResponseBody");

    private static final Logger log = LoggerFactory.getLogger(Retrofit2ServiceGenerator.class);

    private final Set<FeatureFlags> featureFlags;

    @SuppressWarnings("deprecation")
    public Retrofit2ServiceGenerator(Set<FeatureFlags> experimentalFeatures) {
        this.featureFlags = ImmutableSet.copyOf(experimentalFeatures);
        Preconditions.checkArgument(
                !featureFlags.contains(FeatureFlags.RetrofitListenableFutures)
                        || !featureFlags.contains(FeatureFlags.RetrofitCompletableFutures),
                "Cannot enable both the RetrofitListenableFutures and RetrofitCompletableFutures "
                        + "Conjure experimental features. Please remove one.");
    }

    @Override
    public Set<JavaFile> generate(ConjureDefinition conjureDefinition) {
        TypeMapper returnTypeMapper = new TypeMapper(conjureDefinition.getTypes(), new ReturnTypeClassNameVisitor(
                conjureDefinition.getTypes(), BINARY_RETURN_TYPE, BINARY_RETURN_TYPE, featureFlags));

        TypeMapper argumentTypeMapper = new TypeMapper(
                conjureDefinition.getTypes(), new SpecializeBinaryClassNameVisitor(
                        new DefaultClassNameVisitor(conjureDefinition.getTypes(), featureFlags), BINARY_ARGUMENT_TYPE));

        return conjureDefinition.getServices().stream()
                .map(serviceDef -> generateService(serviceDef, returnTypeMapper, argumentTypeMapper))
                .collect(Collectors.toSet());
    }

    private JavaFile generateService(
            ServiceDefinition serviceDefinition, TypeMapper returnTypeMapper, TypeMapper argumentTypeMapper) {
        TypeSpec.Builder serviceBuilder = TypeSpec.interfaceBuilder(serviceName(serviceDefinition))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(Retrofit2ServiceGenerator.class));

        serviceDefinition.getDocs().ifPresent(docs -> serviceBuilder.addJavadoc("$L", Javadoc.render(docs)));

        serviceBuilder.addMethods(serviceDefinition.getEndpoints().stream()
                .map(endpoint -> generateServiceMethod(endpoint, returnTypeMapper, argumentTypeMapper))
                .collect(Collectors.toList()));

        serviceBuilder.addMethods(serviceDefinition.getEndpoints().stream()
                .map(endpoint -> generateCompatibilityBackfillServiceMethods(
                        endpoint, returnTypeMapper, argumentTypeMapper))
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));

        return JavaFile.builder(serviceDefinition.getServiceName().getPackage(), serviceBuilder.build())
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }

    private String serviceName(ServiceDefinition serviceDefinition) {
        return serviceDefinition.getServiceName().getName() + "Retrofit";
    }

    @SuppressWarnings("deprecation")
    private ClassName getReturnType() {
        if (featureFlags.contains(FeatureFlags.RetrofitCompletableFutures)) {
            return COMPLETABLE_FUTURE_TYPE;
        } else if (featureFlags.contains(FeatureFlags.RetrofitListenableFutures)) {
            return LISTENABLE_FUTURE_TYPE;
        } else {
            return CALL_TYPE;
        }
    }

    private MethodSpec generateServiceMethod(
            EndpointDefinition endpointDef, TypeMapper returnTypeMapper, TypeMapper argumentTypeMapper) {
        TypeName returnType = endpointDef.getReturns().map(returnTypeMapper::getClassName).orElse(ClassName.VOID);

        Set<ArgumentName> encodedPathArgs = extractEncodedPathArgs(endpointDef.getHttpPath());
        HttpPath endpointPathWithoutRegex = replaceEncodedPathArgs(endpointDef.getHttpPath());
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(endpointDef.getEndpointName().get())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(AnnotationSpec.builder(httpMethodToClassName(endpointDef.getHttpMethod().get().name()))
                        .addMember("value", "$S", "." + endpointPathWithoutRegex)
                        .build())
                .addAnnotation(AnnotationSpec.builder(ClassName.get("retrofit2.http", "Headers"))
                        .addMember("value", "$S", "hr-path-template: " + endpointPathWithoutRegex)
                        .addMember("value", "$S", "Accept: " + getReturnMediaType(returnType))
                        .build());

        if (returnType.equals(BINARY_RETURN_TYPE)) {
            methodBuilder.addAnnotation(AnnotationSpec.builder(ClassName.get("retrofit2.http", "Streaming")).build());
        }

        endpointDef.getDeprecated().ifPresent(
                deprecatedDocsValue -> methodBuilder.addAnnotation(ClassName.get("java.lang", "Deprecated")));

        ServiceGenerator.getJavaDoc(endpointDef).ifPresent(content -> methodBuilder.addJavadoc("$L", content));

        methodBuilder.returns(ParameterizedTypeName.get(getReturnType(), returnType.box()));

        methodBuilder.addParameters(createServiceMethodParameters(endpointDef, argumentTypeMapper, encodedPathArgs));

        return methodBuilder.build();
    }

    private Set<ArgumentName> extractEncodedPathArgs(HttpPath path) {
        Pattern pathArg = Pattern.compile("\\{([^\\}]+)\\}");
        Matcher matcher = pathArg.matcher(path.toString());
        ImmutableSet.Builder<ArgumentName> encodedArgs = ImmutableSet.builder();
        while (matcher.find()) {
            String arg = matcher.group(1);
            if (arg.contains(":")) {
                // Strip everything after first colon
                encodedArgs.add(ArgumentName.of(arg.substring(0, arg.indexOf(':'))));
            }
        }
        return encodedArgs.build();
    }

    private HttpPath replaceEncodedPathArgs(HttpPath httpPath) {
        List<String> newSegments = Lists.newArrayList();
        Pattern pattern = Pattern.compile("\\{([^:]+):(.*)}");
        Path path = Paths.get(httpPath.get());
        for (String segment : path.getSegments()) {
            Matcher matcher = pattern.matcher(segment);
            if (matcher.matches()) {
                newSegments.add("{" + matcher.group(1) + "}");
            } else {
                newSegments.add(segment);
            }
        }
        return HttpPath.of("/" + Joiner.on("/").join(newSegments));
    }

    /** Provides a linear expansion of optional query arguments to improve Java back-compat. */
    private List<MethodSpec> generateCompatibilityBackfillServiceMethods(
            EndpointDefinition endpointDef, TypeMapper returnTypeMapper, TypeMapper argumentTypeMapper) {
        Set<ArgumentName> encodedPathArgs = extractEncodedPathArgs(endpointDef.getHttpPath());
        List<ArgumentDefinition> queryArgs = Lists.newArrayList();

        for (ArgumentDefinition arg : endpointDef.getArgs()) {
            if (arg.getParamType().accept(ParameterTypeVisitor.IS_QUERY)
                    && arg.getType().accept(DefaultableTypeVisitor.INSTANCE)) {
                queryArgs.add(arg);
            }
        }

        List<MethodSpec> alternateMethods = Lists.newArrayList();
        for (int i = 0; i < queryArgs.size(); i++) {
            alternateMethods.add(createCompatibilityBackfillMethod(
                    endpointDef, returnTypeMapper, argumentTypeMapper, encodedPathArgs, queryArgs.subList(
                            i, queryArgs.size())));
        }

        return alternateMethods;
    }

    private MethodSpec createCompatibilityBackfillMethod(
            EndpointDefinition endpointDef,
            TypeMapper returnTypeMapper,
            TypeMapper argumentTypeMapper,
            Set<ArgumentName> encodedPathArgs,
            List<ArgumentDefinition> extraArgs) {
        TypeName returnType = endpointDef.getReturns().map(returnTypeMapper::getClassName).orElse(ClassName.VOID);
        // ensure the correct ordering of parameters by creating the complete sorted parameter list
        List<ParameterSpec> sortedParams =
                createServiceMethodParameters(endpointDef, argumentTypeMapper, encodedPathArgs);
        List<Optional<ArgumentDefinition>> sortedMaybeExtraArgs = sortedParams.stream()
                .map(param -> extraArgs.stream().filter(arg -> arg.getArgName().get().equals(param.name)).findFirst())
                .collect(Collectors.toList());

        // omit extraArgs from the back fill method signature
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(endpointDef.getEndpointName().get())
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .addAnnotation(Deprecated.class)
                .addParameters(IntStream.range(0, sortedParams.size())
                        .filter(i -> !sortedMaybeExtraArgs.get(i).isPresent())
                        .mapToObj(sortedParams::get)
                        .collect(Collectors.toList()));

        endpointDef.getReturns().ifPresent(
                type -> methodBuilder.returns(ParameterizedTypeName.get(getReturnType(), returnType.box())));

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

        ImmutableList<Object> methodCallArgs =
                ImmutableList.builder().add(endpointDef.getEndpointName().get()).addAll(values).build();

        methodBuilder.addStatement(sb.toString(), methodCallArgs.toArray(new Object[0]));

        return methodBuilder.build();
    }

    private List<ParameterSpec> createServiceMethodParameters(
            EndpointDefinition endpointDef, TypeMapper typeMapper, Set<ArgumentName> encodedPathArgs) {
        List<ParameterSpec> parameterSpecs = new ArrayList<>();

        Optional<AuthType> auth = endpointDef.getAuth();
        getAuthParameter(auth).ifPresent(parameterSpecs::add);

        ParameterOrder.sorted(endpointDef.getArgs())
                .forEach(def -> parameterSpecs.add(createEndpointParameter(typeMapper, encodedPathArgs, def)));
        return ImmutableList.copyOf(parameterSpecs);
    }

    private ParameterSpec createEndpointParameter(
            TypeMapper argumentTypeMapper, Set<ArgumentName> encodedPathArgs, ArgumentDefinition def) {
        ParameterSpec.Builder param =
                ParameterSpec.builder(argumentTypeMapper.getClassName(def.getType()), def.getArgName().get());
        ParameterType paramType = def.getParamType();
        if (paramType.accept(ParameterTypeVisitor.IS_PATH)) {
            AnnotationSpec.Builder builder = AnnotationSpec.builder(ClassName.get("retrofit2.http", "Path"))
                    .addMember("value", "$S", def.getArgName().get());
            if (encodedPathArgs.contains(def.getArgName())) {
                builder.addMember("encoded", "$L", true);
            }
            param.addAnnotation(builder.build());
        } else if (paramType.accept(ParameterTypeVisitor.IS_QUERY)) {
            ParameterId paramId = paramType.accept(ParameterTypeVisitor.QUERY).getParamId();
            param.addAnnotation(AnnotationSpec.builder(ClassName.get("retrofit2.http", "Query"))
                    .addMember("value", "$S", paramId.get())
                    .build());
        } else if (paramType.accept(ParameterTypeVisitor.IS_HEADER)) {
            ParameterId paramId = paramType.accept(ParameterTypeVisitor.HEADER).getParamId();
            param.addAnnotation(AnnotationSpec.builder(ClassName.get("retrofit2.http", "Header"))
                    .addMember("value", "$S", paramId.get())
                    .build());
        } else if (paramType.accept(ParameterTypeVisitor.IS_BODY)) {
            param.addAnnotation(ClassName.get("retrofit2.http", "Body"));
        } else {
            throw new IllegalStateException("Unrecognized argument type: " + def.getParamType());
        }

        return param.build();
    }

    private Optional<ParameterSpec> getAuthParameter(Optional<AuthType> auth) {
        if (!auth.isPresent()) {
            return Optional.empty();
        }

        AuthType authType = auth.get();
        if (authType.accept(AuthTypeVisitor.IS_HEADER)) {
            return Optional.of(
                    ParameterSpec.builder(ClassName.get("com.palantir.tokens.auth", "AuthHeader"), "authHeader")
                            .addAnnotation(AnnotationSpec.builder(ClassName.get("retrofit2.http", "Header"))
                                    .addMember("value", "$S", AUTH_HEADER_NAME)
                                    .build())
                            .build());
        } else if (authType.accept(AuthTypeVisitor.IS_COOKIE)) {
            // TODO(melliot): generate required retrofit logic to support this
            log.error("Retrofit does not support Cookie arguments");
        }

        throw new IllegalStateException("Unrecognized auth type: " + auth.get());
    }

    private static String getReturnMediaType(TypeName returnType) {
        return returnType.equals(BINARY_RETURN_TYPE) ? MediaType.APPLICATION_OCTET_STREAM : MediaType.APPLICATION_JSON;
    }

    private static ClassName httpMethodToClassName(String method) {
        switch (method) {
            case "DELETE":
                return ClassName.get("retrofit2.http", "DELETE");
            case "GET":
                return ClassName.get("retrofit2.http", "GET");
            case "PUT":
                return ClassName.get("retrofit2.http", "PUT");
            case "POST":
                return ClassName.get("retrofit2.http", "POST");
        }
        throw new IllegalArgumentException("Unrecognized HTTP method: " + method);
    }
}
