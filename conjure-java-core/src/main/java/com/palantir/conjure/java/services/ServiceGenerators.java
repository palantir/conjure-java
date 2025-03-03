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

import com.google.common.base.Strings;
import com.palantir.conjure.java.util.ErrorGenerationUtils;
import com.palantir.conjure.java.util.Javadoc;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ServiceGenerators {
    public enum RequestLineJavaDoc {
        INCLUDE,
        EXCLUDE
    }

    public enum EndpointErrorsJavaDoc {
        INCLUDE,
        EXCLUDE
    }

    public record EndpointJavaDocGenerationOptions(
            RequestLineJavaDoc requestLineJavaDoc, EndpointErrorsJavaDoc endpointErrorsJavaDoc) {}

    public static void addJavaDocForEndpointDefinition(
            MethodSpec.Builder methodBuilder,
            Optional<String> maybePackagePrefix,
            EndpointDefinition endpointDefinition,
            EndpointJavaDocGenerationOptions options) {
        addJavaDocForEndpointDefinitionInternal(methodBuilder, maybePackagePrefix, endpointDefinition, options);
    }

    private static void addJavaDocForEndpointDefinitionInternal(
            MethodSpec.Builder methodBuilder,
            Optional<String> maybePackagePrefix,
            EndpointDefinition endpointDefinition,
            EndpointJavaDocGenerationOptions options) {
        endpointDefinition.getDocs().map(Javadoc::render).ifPresent(doc -> methodBuilder.addJavadoc("$L", doc));
        if (options.requestLineJavaDoc() == RequestLineJavaDoc.INCLUDE) {
            methodBuilder.addJavadoc(
                    "$L", Javadoc.getRequestLine(endpointDefinition.getHttpMethod(), endpointDefinition.getHttpPath()));
        }
        Optional.ofNullable(Strings.emptyToNull(endpointDefinition.getArgs().stream()
                        .flatMap(argument -> Javadoc.getParameterJavadoc(argument, endpointDefinition).stream())
                        .collect(Collectors.joining("\n"))))
                .ifPresent(params -> methodBuilder.addJavadoc("$L", params));
        if (options.endpointErrorsJavaDoc() == EndpointErrorsJavaDoc.INCLUDE) {
            methodBuilder.addJavadoc(endpointDefinition.getErrors().stream()
                    .map(endpointError -> CodeBlock.of(
                            "@throws $T $L",
                            ClassName.get(
                                    Packages.getPrefixedPackage(
                                            endpointError.getError().getPackage(), maybePackagePrefix),
                                    ErrorGenerationUtils.serverErrorsClassName(
                                            endpointError.getError().getNamespace()),
                                    endpointError.getError().getName()),
                            endpointError
                                    .getDocs()
                                    .map(endpointErrorDocs -> " " + Javadoc.render(endpointErrorDocs))
                                    .orElse("")))
                    .collect(CodeBlock.joining("\n")));
        }
        endpointDefinition
                .getDeprecated()
                .map(Javadoc::getDeprecatedJavadoc)
                .ifPresent(d -> methodBuilder.addJavadoc("$L", d));
        Javadoc.getIncubatingJavadoc(endpointDefinition.getTags())
                .ifPresent(ind -> methodBuilder.addJavadoc("$L", ind));
    }

    private ServiceGenerators() {}
}
