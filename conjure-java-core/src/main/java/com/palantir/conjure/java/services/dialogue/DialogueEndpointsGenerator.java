/*
 * (c) Copyright 2020 Palantir Technologies Inc. All rights reserved.
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

import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ListMultimap;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.util.Javadoc;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.HttpPath;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.HttpMethod;
import com.palantir.dialogue.PathTemplate;
import com.palantir.dialogue.UrlBuilder;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import javax.lang.model.element.Modifier;

final class DialogueEndpointsGenerator {
    // matches HttpPathValidator from conjure-core
    private static final String PATTERN = "[a-z][a-z0-9]*([A-Z0-9][a-z0-9]+)*";
    private static final Pattern SEGMENT_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9._-]*$");
    private static final Pattern PARAM_SEGMENT_PATTERN = Pattern.compile("^\\{" + PATTERN + "}$");
    private static final Pattern PARAM_REGEX_SEGMENT_PATTERN =
            Pattern.compile("^\\{" + PATTERN + "(" + Pattern.quote(":.+") + "|" + Pattern.quote(":.*") + ")" + "}$");

    private final Options options;

    DialogueEndpointsGenerator(Options options) {
        this.options = options;
    }

    public JavaFile endpointsClass(ServiceDefinition def) {
        ClassName serviceClassName = Names.endpointsClassName(def, options);

        TypeSpec.Builder enumBuilder = TypeSpec.enumBuilder(serviceClassName)
                .addSuperinterface(ClassName.get(Endpoint.class))
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(DialogueEndpointsGenerator.class));

        def.getEndpoints().forEach(endpoint -> {
            enumBuilder.addEnumConstant(
                    endpoint.getEndpointName().get(),
                    endpointField(endpoint, def.getServiceName().getName(), options.apiVersion()));
        });

        if (!options.apiVersion().isPresent()) {
            enumBuilder.addField(FieldSpec.builder(
                            TypeName.get(String.class), "VERSION", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                    .initializer(
                            "$T.ofNullable($T.class.getPackage().getImplementationVersion()).orElse(\"0.0.0\")",
                            TypeName.get(Optional.class),
                            serviceClassName)
                    .build());
        }

        return JavaFile.builder(
                        Packages.getPrefixedPackage(def.getServiceName().getPackage(), options.packagePrefix()),
                        enumBuilder.build())
                .build();
    }

    private static TypeSpec endpointField(EndpointDefinition def, String serviceName, Optional<String> apiVersion) {
        TypeSpec.Builder builder = TypeSpec.anonymousClassBuilder("");
        def.getDocs().ifPresent(docs -> builder.addJavadoc("$L", Javadoc.render(docs)));

        builder.addField(FieldSpec.builder(
                                TypeName.get(PathTemplate.class), "pathTemplate", Modifier.PRIVATE, Modifier.FINAL)
                        .initializer(pathTemplateInitializer(def.getHttpPath()))
                        .build())
                .addMethod(MethodSpec.methodBuilder("renderPath")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(
                                ParameterizedTypeName.get(ListMultimap.class, String.class, String.class), "params")
                        .addParameter(UrlBuilder.class, "url")
                        .addCode("pathTemplate.fill(params, url);")
                        .build())
                .addMethod(MethodSpec.methodBuilder("httpMethod")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(HttpMethod.class)
                        .addCode(CodeBlock.builder()
                                .add(
                                        "return $T.$L;",
                                        HttpMethod.class,
                                        def.getHttpMethod().get())
                                .build())
                        .build())
                .addMethod(MethodSpec.methodBuilder("serviceName")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addCode(CodeBlock.builder()
                                .add("return $S;", serviceName)
                                .build())
                        .build())
                .addMethod(MethodSpec.methodBuilder("endpointName")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addCode(CodeBlock.builder()
                                .add("return $S;", def.getEndpointName().get())
                                .build())
                        .build())
                .addMethod(MethodSpec.methodBuilder("version")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addCode(CodeBlock.builder()
                                .add(apiVersion
                                        .map(s -> CodeBlock.of("return $S;", s))
                                        .orElseGet(() -> CodeBlock.of("return VERSION;")))
                                .build())
                        .build());
        addTags(def, builder);
        return builder.build();
    }

    private static void addTags(EndpointDefinition def, TypeSpec.Builder destination) {
        if (!def.getTags().isEmpty()) {
            CodeBlock arrayValues =
                    CodeBlock.join(Collections2.transform(def.getTags(), value -> CodeBlock.of("$S", value)), ", ");
            destination.addField(FieldSpec.builder(
                            ParameterizedTypeName.get(ImmutableSet.class, String.class),
                            "tags",
                            Modifier.PRIVATE,
                            Modifier.FINAL)
                    .initializer(CodeBlock.of("$T.of($L)", ImmutableSet.class, arrayValues))
                    .build());
            destination.addMethod(MethodSpec.methodBuilder("tags")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(ParameterizedTypeName.get(Set.class, String.class))
                    .addStatement("return tags")
                    .build());
        }
    }

    private static CodeBlock pathTemplateInitializer(HttpPath path) {
        Splitter splitter = Splitter.on('/');
        CodeBlock.Builder pathTemplateBuilder = CodeBlock.builder().add("$T.builder()", PathTemplate.class);

        Iterable<String> rawSegments = splitter.split(path.get());
        for (String segment : rawSegments) {
            if (segment.isEmpty()) {
                continue;
            }
            if (SEGMENT_PATTERN.matcher(segment).matches()) {
                // fixed
                pathTemplateBuilder.add(".fixed($S)", segment);
            } else if (PARAM_SEGMENT_PATTERN.matcher(segment).matches()) {
                // variable
                pathTemplateBuilder.add(".variable($S)", segment.substring(1, segment.length() - 1));
            } else if (PARAM_REGEX_SEGMENT_PATTERN.matcher(segment).matches()) {
                // variable
                pathTemplateBuilder.add(".variable($S)", segment.substring(1, segment.length() - 4));
            } else {
                throw new SafeIllegalArgumentException(
                        "Invalid path segment", SafeArg.of("segment", segment), SafeArg.of("path", path));
            }
        }

        return pathTemplateBuilder.add(".build()").build();
    }
}
