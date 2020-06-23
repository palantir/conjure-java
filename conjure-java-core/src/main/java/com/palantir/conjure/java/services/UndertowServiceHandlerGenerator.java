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

import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.spec.ServiceDefinition;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.List;
import javax.lang.model.element.Modifier;

final class UndertowServiceHandlerGenerator {

    private static final String DELEGATE_VAR_NAME = "delegate";
    private static final String RUNTIME_VAR_NAME = "runtime";

    private final Options options;

    UndertowServiceHandlerGenerator(Options options) {
        this.options = options;
    }

    public JavaFile generateServiceHandler(ServiceDefinition serviceDefinition) {
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

        TypeSpec endpoints = TypeSpec.classBuilder(serviceType.simpleName())
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(UndertowServiceHandlerGenerator.class))
                .addSuperinterface(UndertowService.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(FieldSpec.builder(serviceClass, DELEGATE_VAR_NAME, Modifier.PRIVATE, Modifier.FINAL)
                        .build())
                .addMethod(MethodSpec.methodBuilder("of")
                        .addJavadoc(
                                "@Deprecated: You can now use {@link $1T} directly as it implements {@link $2T}.",
                                serviceClass,
                                UndertowService.class)
                        .addAnnotation(Deprecated.class)
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
                        .addStatement("return $1N.endpoints($2N)", DELEGATE_VAR_NAME, RUNTIME_VAR_NAME)
                        .build())
                .build();

        return JavaFile.builder(
                        Packages.getPrefixedPackage(
                                serviceDefinition.getServiceName().getPackage(), options.packagePrefix()),
                        endpoints)
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }
}
