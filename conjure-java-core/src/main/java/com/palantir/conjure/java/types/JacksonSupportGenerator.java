/*
 * (c) Copyright 2026 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.types;

import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.NameAllocator;
import com.palantir.javapoet.TypeSpec;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import javax.lang.model.element.Modifier;

/** Allocates and emits only the Jackson helpers used by models in each generated package. */
final class JacksonSupportGenerator {
    enum Helper {
        CONTAINER_DESERIALIZER("ContainerDeserializer"),
        UNION_DESERIALIZER("UnionDeserializer"),
        UNION_SERIALIZER("UnionSerializer");

        private final String simpleName;

        Helper(String simpleName) {
            this.simpleName = simpleName;
        }
    }

    record Union(ClassName className, List<ClassName> variants, String dispatchMethod) {}

    private final Map<String, ClassName> supportClasses = new LinkedHashMap<>();
    private final Map<ClassName, Set<Helper>> requested = new LinkedHashMap<>();
    private final Map<ClassName, Union> unions = new LinkedHashMap<>();

    JacksonSupportGenerator(List<ClassName> modelClasses) {
        Map<String, NameAllocator> names = new LinkedHashMap<>();
        modelClasses.forEach(model -> names.computeIfAbsent(model.packageName(), _package -> new NameAllocator())
                .newName(model.simpleName()));
        names.forEach((packageName, allocator) -> supportClasses.put(
                packageName, ClassName.get(packageName, allocator.newName("ConjureJacksonSupport"))));
    }

    ClassName get(ClassName model, Helper helper) {
        ClassName support = supportClasses.get(model.packageName());
        requested
                .computeIfAbsent(support, _support -> EnumSet.noneOf(Helper.class))
                .add(helper);
        return support.nestedClass(helper.simpleName);
    }

    ClassName registerUnion(ClassName model, List<ClassName> variants, String dispatchMethod) {
        unions.put(model, new Union(model, List.copyOf(variants), dispatchMethod));
        return get(model, Helper.UNION_DESERIALIZER);
    }

    Stream<JavaFile> generate() {
        return requested.entrySet().stream().map(entry -> {
            ClassName support = entry.getKey();
            TypeSpec.Builder builder = TypeSpec.classBuilder(support)
                    .addModifiers(Modifier.FINAL)
                    .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(JacksonSupportGenerator.class))
                    .addMethod(MethodSpec.constructorBuilder()
                            .addModifiers(Modifier.PRIVATE)
                            .build());
            for (Helper helper : entry.getValue()) {
                ClassName helperClass = support.nestedClass(helper.simpleName);
                builder.addType(
                        switch (helper) {
                            case CONTAINER_DESERIALIZER ->
                                JacksonCollectionDeserializerGenerator.containerDeserializer(helperClass);
                            case UNION_DESERIALIZER ->
                                JacksonUnionSerdeGenerator.unionDeserializer(
                                        helperClass,
                                        unions.values().stream()
                                                .filter(union -> union.className()
                                                        .packageName()
                                                        .equals(support.packageName()))
                                                .toList());
                            case UNION_SERIALIZER -> JacksonUnionSerdeGenerator.unionSerializer(helperClass);
                        });
            }
            return JavaFile.builder(support.packageName(), builder.build())
                    .skipJavaLangImports(true)
                    .indent("    ")
                    .build();
        });
    }
}
