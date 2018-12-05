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

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.FeatureFlags;
import com.palantir.conjure.java.types.MethodTypeClassNameVisitor;
import com.palantir.conjure.java.types.ReturnTypeClassNameVisitor;
import com.palantir.conjure.java.types.TypeMapper;
import com.palantir.conjure.spec.ArgumentDefinition;
import com.palantir.conjure.spec.ArgumentName;
import com.palantir.conjure.spec.AuthType;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.HttpPath;
import com.palantir.conjure.spec.ParameterId;
import com.palantir.conjure.spec.ParameterType;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.visitor.AuthTypeVisitor;
import com.palantir.conjure.visitor.ParameterTypeVisitor;
import com.palantir.conjure.visitor.TypeVisitor;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.lang.model.element.Modifier;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Retrofit2ServiceGenerator implements ServiceGenerator {

    private static final ClassName COMPLETABLE_FUTURE_TYPE = ClassName.get("java.util.concurrent", "CompletableFuture");
    private static final ClassName LISTENABLE_FUTURE_TYPE = ClassName.get(
            "com.google.common.util.concurrent", "ListenableFuture");
    private static final ClassName CALL_TYPE = ClassName.get("retrofit2", "Call");
    private static final String AUTH_HEADER_NAME = "Authorization";

    private static final ClassName BINARY_METHOD_TYPE = ClassName.get("okhttp3", "RequestBody");
    private static final ClassName BINARY_RETURN_TYPE = ClassName.get("okhttp3", "ResponseBody");

    private static final Logger log = LoggerFactory.getLogger(Retrofit2ServiceGenerator.class);

    private final Set<FeatureFlags> featureFlags;

    public Retrofit2ServiceGenerator(Set<FeatureFlags> experimentalFeatures) {
        this.featureFlags = ImmutableSet.copyOf(experimentalFeatures);
        checkArgument(!featureFlags.contains(FeatureFlags.RetrofitListenableFutures)
                        || !featureFlags.contains(FeatureFlags.RetrofitCompletableFutures),
                "Cannot enable both the RetrofitListenableFutures and RetrofitCompletableFutures "
                        + "Conjure experimental features. Please remove one.");
    }

    @Override
    public Set<JavaFile> generate(ConjureDefinition conjureDefinition) {
        TypeMapper returnTypeMapper = new TypeMapper(
                conjureDefinition.getTypes(),
                ReturnTypeClassNameVisitor.createFactory(BINARY_RETURN_TYPE));
        TypeMapper methodTypeMapper = new TypeMapper(
                conjureDefinition.getTypes(),
                MethodTypeClassNameVisitor.createFactory(BINARY_METHOD_TYPE));
        return conjureDefinition.getServices().stream()
                .map(serviceDef -> generateService(serviceDef, returnTypeMapper, methodTypeMapper))
                .collect(Collectors.toSet());
    }

    private JavaFile generateService(ServiceDefinition serviceDefinition,
            TypeMapper returnTypeMapper, TypeMapper methodTypeMapper) {
        TypeSpec.Builder serviceBuilder = TypeSpec.interfaceBuilder(serviceName(serviceDefinition))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(Retrofit2ServiceGenerator.class));

        serviceDefinition.getDocs().ifPresent(docs -> {
            serviceBuilder.addJavadoc("$L", StringUtils.appendIfMissing(docs.get(), "\n"));
        });

        serviceBuilder.addMethods(serviceDefinition.getEndpoints().stream()
                .map(endpoint -> generateServiceMethod(endpoint, returnTypeMapper, methodTypeMapper))
                .collect(Collectors.toList()));

        return JavaFile.builder(serviceDefinition.getServiceName().getPackage(), serviceBuilder.build())
                .indent("    ")
                .build();
    }

    private String serviceName(ServiceDefinition serviceDefinition) {
        return serviceDefinition.getServiceName().getName() + "Retrofit";
    }

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
            EndpointDefinition endpointDef,
            TypeMapper returnTypeMapper,
            TypeMapper methodTypeMapper) {
        TypeName returnType = endpointDef.getReturns()
                .map(returnTypeMapper::getClassName)
                .orElse(ClassName.VOID);

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

        endpointDef.getDeprecated().ifPresent(deprecatedDocsValue -> methodBuilder.addAnnotation(
                ClassName.get("java.lang", "Deprecated")));

        ServiceGenerator.getJavaDoc(endpointDef).ifPresent(
                content -> methodBuilder.addJavadoc("$L", content));

        methodBuilder.returns(ParameterizedTypeName.get(getReturnType(), returnType.box()));

        getAuthParameter(endpointDef.getAuth()).ifPresent(methodBuilder::addParameter);

        methodBuilder.addParameters(endpointDef.getArgs().stream()
                .map(arg -> createEndpointParameter(methodTypeMapper, encodedPathArgs, arg))
                .collect(Collectors.toList()));

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

    private ParameterSpec createEndpointParameter(
            TypeMapper methodTypeMapper,
            Set<ArgumentName> encodedPathArgs,
            ArgumentDefinition def) {
        ParameterSpec.Builder param = ParameterSpec.builder(
                methodTypeMapper.getClassName(def.getType()),
                def.getArgName().get());
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
        return returnType.equals(BINARY_RETURN_TYPE)
                ? MediaType.APPLICATION_OCTET_STREAM
                : MediaType.APPLICATION_JSON;
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
                return ClassName.get("retrofit2.http", "DELETE");
            case "GET":
                return ClassName.get("retrofit2.http", "GET");
            case "PUT":
                return ClassName.get("retrofit2.http", "PUT");
            case "POST":
                return ClassName.get("retrofit2.http", "POST");
            default:
                throw new IllegalArgumentException("Unrecognized HTTP method: " + method);
        }
    }

}
