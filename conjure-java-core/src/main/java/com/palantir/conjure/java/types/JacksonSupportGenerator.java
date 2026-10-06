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

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.NameAllocator;
import com.palantir.javapoet.TypeSpec;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/** Allocates and emits only the Jackson helpers used by models in each generated package. */
final class JacksonSupportGenerator {
    enum Helper {
        MAP_DESERIALIZER("ConjureMapDeserializer"),
        SET_DESERIALIZER("ConjureSetDeserializer"),
        UNION_DESERIALIZER("ConjureUnionDeserializer"),
        UNION_SERIALIZER("ConjureUnionSerializer");

        private final String simpleName;

        Helper(String simpleName) {
            this.simpleName = simpleName;
        }

        TypeSpec generate(ClassName className) {
            return switch (this) {
                case MAP_DESERIALIZER -> JacksonCollectionDeserializerGenerator.mapDeserializer(className);
                case SET_DESERIALIZER -> JacksonCollectionDeserializerGenerator.setDeserializer(className);
                case UNION_DESERIALIZER -> JacksonUnionSerdeGenerator.unionDeserializer(className);
                case UNION_SERIALIZER -> JacksonUnionSerdeGenerator.unionSerializer(className);
            };
        }
    }

    private final Map<String, NameAllocator> names = new LinkedHashMap<>();
    private final Map<ClassName, Helper> requested = new LinkedHashMap<>();

    JacksonSupportGenerator(List<ClassName> modelClasses) {
        modelClasses.forEach(model -> names.computeIfAbsent(model.packageName(), _package -> new NameAllocator())
                .newName(model.simpleName()));
        names.values().forEach(allocator -> {
            for (Helper helper : Helper.values()) {
                allocator.newName(helper.simpleName, helper);
            }
        });
    }

    ClassName get(ClassName model, Helper helper) {
        NameAllocator allocator = names.get(model.packageName());
        ClassName result = ClassName.get(model.packageName(), allocator.get(helper));
        requested.put(result, helper);
        return result;
    }

    Stream<JavaFile> generate() {
        return requested.entrySet().stream()
                .map(entry -> JavaFile.builder(
                                entry.getKey().packageName(), entry.getValue().generate(entry.getKey()))
                        .skipJavaLangImports(true)
                        .indent("    ")
                        .build());
    }
}
