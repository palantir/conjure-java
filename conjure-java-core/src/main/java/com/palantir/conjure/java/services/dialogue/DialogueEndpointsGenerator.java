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
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.Map;
import java.util.Optional;
import javax.lang.model.element.Modifier;
import org.glassfish.jersey.uri.internal.UriTemplateParser;

final class DialogueEndpointsGenerator {
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
        def.getDocs().map(Javadoc::render).ifPresent(builder::addJavadoc);

        return builder.addField(FieldSpec.builder(
                                TypeName.get(PathTemplate.class), "pathTemplate", Modifier.PRIVATE, Modifier.FINAL)
                        .initializer(pathTemplateInitializer(def.getHttpPath()))
                        .build())
                .addMethod(MethodSpec.methodBuilder("renderPath")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(ParameterizedTypeName.get(Map.class, String.class, String.class), "params")
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
                        .build())
                .build();
    }

    // TODO(rfink): Integrate/consolidate with checking code in PathDefinition class
    private static CodeBlock pathTemplateInitializer(HttpPath path) {
        UriTemplateParser uriTemplateParser = new UriTemplateParser(path.get());
        Splitter splitter = Splitter.on('/');
        Iterable<String> rawSegments = splitter.split(uriTemplateParser.getNormalizedTemplate());

        CodeBlock.Builder pathTemplateBuilder = CodeBlock.builder().add("$T.builder()", PathTemplate.class);

        for (String segment : rawSegments) {
            if (segment.isEmpty()) {
                continue; // avoid empty segments; typically the first segment is empty
            }

            if (segment.startsWith("{") && segment.endsWith("}")) {
                pathTemplateBuilder.add(".variable($S)", segment.substring(1, segment.length() - 1));
            } else {
                pathTemplateBuilder.add(".fixed($S)", segment);
            }
        }

        CodeBlock build = pathTemplateBuilder.add(".build()").build();
        return build;
    }
}
