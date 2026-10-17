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

import com.google.common.collect.ImmutableList;
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

    private record PackageSupport(ClassName className, Set<Helper> requested, ImmutableList.Builder<Union> unions) {
        PackageSupport(ClassName className) {
            this(className, EnumSet.noneOf(Helper.class), ImmutableList.builder());
        }
    }

    private final Map<String, PackageSupport> packages = new LinkedHashMap<>();

    JacksonSupportGenerator(List<ClassName> reservedClasses) {
        Map<String, NameAllocator> names = new LinkedHashMap<>();
        reservedClasses.forEach(type -> names.computeIfAbsent(type.packageName(), _package -> new NameAllocator())
                .newName(type.simpleName()));
        names.forEach((packageName, allocator) -> packages.put(
                packageName,
                new PackageSupport(ClassName.get(packageName, allocator.newName("ConjureJacksonSupport")))));
    }

    ClassName get(ClassName model, Helper helper) {
        PackageSupport support = packages.get(model.packageName());
        support.requested().add(helper);
        return support.className().nestedClass(helper.simpleName);
    }

    ClassName registerUnion(ClassName model, List<ClassName> variants, String dispatchMethod) {
        packages.get(model.packageName()).unions().add(new Union(model, List.copyOf(variants), dispatchMethod));
        return get(model, Helper.UNION_DESERIALIZER);
    }

    Stream<JavaFile> generate() {
        return packages.values().stream()
                .filter(support -> !support.requested().isEmpty())
                .map(JacksonSupportGenerator::generate);
    }

    private static JavaFile generate(PackageSupport support) {
        TypeSpec.Builder builder = TypeSpec.classBuilder(support.className())
                .addModifiers(Modifier.FINAL)
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(JacksonSupportGenerator.class))
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .build());
        for (Helper helper : support.requested()) {
            ClassName helperClass = support.className().nestedClass(helper.simpleName);
            builder.addType(
                    switch (helper) {
                        case CONTAINER_DESERIALIZER ->
                            JacksonCollectionDeserializerGenerator.containerDeserializer(helperClass);
                        case UNION_DESERIALIZER ->
                            JacksonUnionSerdeGenerator.unionDeserializer(
                                    helperClass, support.unions().build());
                        case UNION_SERIALIZER -> JacksonUnionSerdeGenerator.unionSerializer(helperClass);
                    });
        }
        return JavaFile.builder(support.className().packageName(), builder.build())
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }
}
