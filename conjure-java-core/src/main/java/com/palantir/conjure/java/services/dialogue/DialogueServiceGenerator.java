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

import static java.util.stream.Collectors.toList;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.services.ServiceGenerator;
import com.palantir.conjure.java.types.TypeMapper;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.java.visitor.ClassVisitor;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.HttpPath;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.visitor.TypeDefinitionVisitor;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.HttpMethod;
import com.palantir.dialogue.PathTemplate;
import com.palantir.dialogue.UrlBuilder;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.lang.model.element.Modifier;
import org.glassfish.jersey.uri.internal.UriTemplateParser;

// TODO(rfink): Add unit tests for misc edge cases, e.g.: docs/no-docs, auth/no-auth, binary return type.
public final class DialogueServiceGenerator implements ServiceGenerator {

    private final Options options;
    private final String apiVersion;

    public DialogueServiceGenerator(Options options, String apiVersion) {
        this.options = options;
        this.apiVersion = apiVersion;
    }

    @Override
    public Set<JavaFile> generate(ConjureDefinition conjureDefinition) {
        TypeMapper parameterTypes = new TypeMapper(
                conjureDefinition.getTypes(),
                new ClassVisitor(conjureDefinition.getTypes(), options, ClassVisitor.Mode.PARAMETER));
        TypeMapper returnTypes = new TypeMapper(
                conjureDefinition.getTypes(),
                new ClassVisitor(conjureDefinition.getTypes(), options, ClassVisitor.Mode.RETURN_VALUE));
        Map<com.palantir.conjure.spec.TypeName, TypeDefinition> typeDefinitionsByName =
                conjureDefinition.getTypes().stream()
                        .collect(Collectors.toMap(
                                type -> type.accept(TypeDefinitionVisitor.TYPE_NAME), Function.identity()));
        TypeAwareGenerator generator = new TypeAwareGenerator(
                options,
                typeName -> Preconditions.checkNotNull(
                        typeDefinitionsByName.get(typeName),
                        "Referenced unknown TypeName",
                        SafeArg.of("typeName", typeName)),
                parameterTypes,
                returnTypes,
                apiVersion);
        return conjureDefinition.getServices().stream()
                .flatMap(serviceDef -> generator.service(serviceDef).stream())
                .collect(Collectors.toSet());
    }

    // TODO(rfink): Split into separate classes: endpoint, interface, impl.
    private static final class TypeAwareGenerator {
        private final Options options;
        private final TypeNameResolver typeNameResolver;
        private final ParameterTypeMapper parameterTypes;
        private final ReturnTypeMapper returnTypes;
        private final String apiVersion;

        private TypeAwareGenerator(
                Options options,
                TypeNameResolver typeNameResolver,
                TypeMapper parameterTypes,
                TypeMapper returnTypes,
                String apiVersion) {
            this.options = options;
            this.typeNameResolver = typeNameResolver;
            this.parameterTypes = new ParameterTypeMapper(parameterTypes);
            this.returnTypes = new ReturnTypeMapper(returnTypes);
            this.apiVersion = apiVersion;
        }

        private Set<JavaFile> service(ServiceDefinition def) {
            JavaFile client = client(def);
            InterfaceGenerator ifaceGenerator = new InterfaceGenerator(options, def, parameterTypes, returnTypes);

            ImmutableSet<JavaFile> of =
                    ImmutableSet.of(ifaceGenerator.generateBlocking(), ifaceGenerator.generateAsync(), client);
            return of;
        }

        private JavaFile client(ServiceDefinition def) {
            ClassName serviceClassName = Names.publicClassName(def, options);
            TypeSpec.Builder serviceBuilder = TypeSpec.classBuilder(serviceClassName)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(DialogueServiceGenerator.class));

            serviceBuilder.addMethod(MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PRIVATE)
                    .build());
            serviceBuilder.addMethod(new BlockingGenerator(options, parameterTypes, returnTypes).generate(def));
            serviceBuilder.addMethod(new AsyncGenerator(options, typeNameResolver, parameterTypes, returnTypes)
                    .generate(serviceClassName, def));
            serviceBuilder.addFields(def.getEndpoints().stream()
                    .map(e -> endpoint(e, def.getServiceName().getName()))
                    .collect(toList()));

            return JavaFile.builder(
                            Packages.getPrefixedPackage(def.getServiceName().getPackage(), options.packagePrefix()),
                            serviceBuilder.build())
                    .build();
        }

        private FieldSpec endpoint(EndpointDefinition def, String serviceName) {
            ClassName endpointType = ClassName.get(Endpoint.class);

            TypeSpec endpointClass = TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(endpointType)
                    .addField(FieldSpec.builder(
                                    TypeName.get(PathTemplate.class), "pathTemplate", Modifier.PRIVATE, Modifier.FINAL)
                            .initializer(pathTemplateInitializer(def.getHttpPath()))
                            .build())
                    // TODO(rfink): These fields cannot be static. Does this matter? Should we make these real
                    // instead of anonymous classes?
                    // TODO(rfink): Decide whether we want to initialize these lazily.
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
                                    .add("return $S;", apiVersion)
                                    .build())
                            .build())
                    .build();

            return FieldSpec.builder(
                            endpointType,
                            def.getEndpointName().get(),
                            Modifier.PRIVATE,
                            Modifier.STATIC,
                            Modifier.FINAL)
                    .initializer(CodeBlock.of("$L", endpointClass))
                    .build();
        }

        // TODO(rfink): Integrate/consolidate with checking code in PathDefinition class
        private CodeBlock pathTemplateInitializer(HttpPath path) {
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
}
