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
import com.palantir.conjure.java.types.ErrorMapper;
import com.palantir.conjure.java.types.TypeMapper;
import com.palantir.conjure.java.util.JavaNameSanitizer;
import com.palantir.conjure.java.util.Javadoc;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.java.util.ParameterOrder;
import com.palantir.conjure.spec.ArgumentDefinition;
import com.palantir.conjure.spec.AuthType;
import com.palantir.conjure.spec.CookieAuthType;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.HeaderAuthType;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.spec.TypeName;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.lang.model.element.Modifier;

final class UndertowServiceInterfaceGenerator {

    private final Options options;

    UndertowServiceInterfaceGenerator(Options options) {
        this.options = options;
    }

    public JavaFile generateServiceInterface(
            ServiceDefinition serviceDefinition,
            TypeMapper typeMapper,
            TypeMapper returnTypeMapper,
            ErrorMapper errorMapper) {
        TypeSpec.Builder serviceBuilder = TypeSpec.interfaceBuilder((options.undertowServicePrefix() ? "Undertow" : "")
                        + serviceDefinition.getServiceName().getName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(
                        ConjureAnnotations.getConjureGeneratedAnnotation(UndertowServiceInterfaceGenerator.class));

        serviceDefinition.getDocs().ifPresent(docs -> serviceBuilder.addJavadoc("$L", Javadoc.render(docs)));

        serviceBuilder.addMethods(serviceDefinition.getEndpoints().stream()
                .map(endpoint -> generateServiceInterfaceMethod(endpoint, typeMapper, returnTypeMapper, errorMapper))
                .collect(Collectors.toList()));

        return JavaFile.builder(
                        Packages.getPrefixedPackage(
                                serviceDefinition.getServiceName().getPackage(), options.packagePrefix()),
                        serviceBuilder.build())
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }

    private MethodSpec generateServiceInterfaceMethod(
            EndpointDefinition endpointDef,
            TypeMapper typeMapper,
            TypeMapper returnTypeMapper,
            ErrorMapper errorMapper) {
        String methodName =
                JavaNameSanitizer.sanitize(endpointDef.getEndpointName().get());
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameters(createServiceMethodParameters(endpointDef, typeMapper))
                .addAnnotations(ConjureAnnotations.incubating(endpointDef));

        for (TypeName errorName : endpointDef.getErrors()) {
            ClassName errorClass = errorMapper
                    .getClassNameForError(errorName)
                    .orElseThrow(() -> new IllegalStateException("No error found with name " + errorName));
            methodBuilder.addException(errorClass);
        }

        endpointDef.getDeprecated().ifPresent(deprecatedDocsValue -> methodBuilder.addAnnotation(Deprecated.class));

        methodBuilder.addJavadoc("$L", ServiceGenerators.getJavaDocWithRequestLine(endpointDef));

        if (UndertowTypeFunctions.isAsync(endpointDef, options)) {
            methodBuilder.returns(UndertowTypeFunctions.getAsyncReturnType(endpointDef, returnTypeMapper, options));
        } else {
            endpointDef.getReturns().ifPresent(type -> methodBuilder.returns(returnTypeMapper.getClassName(type)));
        }

        return methodBuilder.build();
    }

    private List<ParameterSpec> createServiceMethodParameters(EndpointDefinition endpointDef, TypeMapper typeMapper) {
        List<ParameterSpec> parameterSpecs = new ArrayList<>();

        endpointDef
                .getAuth()
                .ifPresent(authType -> parameterSpecs.add(authType.accept(new AuthType.Visitor<ParameterSpec>() {
                    @Override
                    public ParameterSpec visitHeader(HeaderAuthType _value) {
                        return ParameterSpec.builder(ClassName.get(AuthHeader.class), "authHeader")
                                .build();
                    }

                    @Override
                    public ParameterSpec visitCookie(CookieAuthType _value) {
                        return ParameterSpec.builder(ClassName.get(BearerToken.class), "cookieToken")
                                .build();
                    }

                    @Override
                    public ParameterSpec visitUnknown(String unknownType) {
                        throw new IllegalStateException("unknown auth type: " + unknownType);
                    }
                })));
        List<ArgumentDefinition> sortedArgList = ParameterOrder.sorted(endpointDef.getArgs());
        sortedArgList.forEach(def -> parameterSpecs.add(createServiceMethodParameterArg(typeMapper, def, endpointDef)));

        return ImmutableList.copyOf(parameterSpecs);
    }

    private ParameterSpec createServiceMethodParameterArg(
            TypeMapper typeMapper, ArgumentDefinition def, EndpointDefinition endpoint) {
        return ParameterSpec.builder(
                        typeMapper.getClassName(def.getType()),
                        JavaNameSanitizer.sanitizeParameterName(def.getArgName().get(), endpoint))
                .build();
    }
}
