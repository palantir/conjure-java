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
import com.palantir.conjure.java.FeatureFlags;
import com.palantir.conjure.java.types.TypeMapper;
import com.palantir.conjure.spec.ArgumentDefinition;
import com.palantir.conjure.spec.AuthType;
import com.palantir.conjure.spec.CookieAuthType;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.HeaderAuthType;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

final class UndertowServiceInterfaceGenerator {

    private final Set<FeatureFlags> experimentalFeatures;

    UndertowServiceInterfaceGenerator(Set<FeatureFlags> experimentalFeatures) {
        this.experimentalFeatures = experimentalFeatures;
    }

    public JavaFile generateServiceInterface(
            ServiceDefinition serviceDefinition, TypeMapper typeMapper, TypeMapper returnTypeMapper) {
        TypeSpec.Builder serviceBuilder = TypeSpec.interfaceBuilder(
                (experimentalFeatures.contains(FeatureFlags.UndertowServicePrefix) ? "Undertow" : "")
                        + serviceDefinition.getServiceName().getName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(
                        ConjureAnnotations.getConjureGeneratedAnnotation(UndertowServiceInterfaceGenerator.class));

        serviceDefinition.getDocs().ifPresent(docs ->
                serviceBuilder.addJavadoc("$L", StringUtils.appendIfMissing(docs.get(), "\n")));

        serviceBuilder.addMethods(serviceDefinition.getEndpoints().stream()
                .map(endpoint -> generateServiceInterfaceMethod(endpoint, typeMapper, returnTypeMapper))
                .collect(Collectors.toList()));

        return JavaFile.builder(serviceDefinition.getServiceName().getPackage(), serviceBuilder.build())
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }

    private MethodSpec generateServiceInterfaceMethod(
            EndpointDefinition endpointDef,
            TypeMapper typeMapper,
            TypeMapper returnTypeMapper) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(endpointDef.getEndpointName().get())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameters(createServiceMethodParameters(endpointDef, typeMapper));

        endpointDef.getDeprecated().ifPresent(deprecatedDocsValue -> methodBuilder.addAnnotation(
                ClassName.get("java.lang", "Deprecated")));

        ServiceGenerator.getJavaDoc(endpointDef).ifPresent(content -> methodBuilder.addJavadoc("$L", content));

        endpointDef.getReturns().ifPresent(type -> methodBuilder.returns(
                UndertowTypeFunctions.unbox(returnTypeMapper.getClassName(type))));

        return methodBuilder.build();
    }

    private List<ParameterSpec> createServiceMethodParameters(EndpointDefinition endpointDef, TypeMapper typeMapper) {
        List<ParameterSpec> parameterSpecs = new ArrayList<>();

        endpointDef.getAuth().ifPresent(
                authType -> parameterSpecs.add(authType.accept(new AuthType.Visitor<ParameterSpec>() {
                    @Override
                    public ParameterSpec visitHeader(HeaderAuthType value) {
                        return ParameterSpec.builder(ClassName.get(AuthHeader.class), "authHeader").build();
                    }

                    @Override
                    public ParameterSpec visitCookie(CookieAuthType value) {
                        return ParameterSpec.builder(ClassName.get(BearerToken.class), "cookieToken").build();
                    }

                    @Override
                    public ParameterSpec visitUnknown(String unknownType) {
                        throw new IllegalStateException("unknown auth type: " + unknownType);
                    }
                })));

        // TODO(nmiyake): export and share this logic properly in conjure-java project instead of relying on copy-pasted
        // code matching the implementation. In order to maintain API compatibility with Conjure-generated Jersey APIs,
        // the following code block must match the code defined in
        // com.palantir.conjure.java.services.JerseyServiceGenerator.createServiceMethodParameters.
        List<ArgumentDefinition> sortedArgList = UndertowServiceGenerator.sortArgumentDefinitions(
                endpointDef.getArgs());
        sortedArgList.forEach(def -> parameterSpecs.add(createServiceMethodParameterArg(typeMapper, def)));
        // end copied block

        return ImmutableList.copyOf(parameterSpecs);
    }

    private ParameterSpec createServiceMethodParameterArg(TypeMapper typeMapper, ArgumentDefinition def) {
        ParameterSpec.Builder param = ParameterSpec.builder(
                UndertowTypeFunctions.unbox(typeMapper.getClassName(def.getType())), def.getArgName().get());
        return param.build();
    }
}
