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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.palantir.conjure.defs.SafetyDeclarationRequirements;
import com.palantir.conjure.defs.validator.ConjureDefinitionValidator;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.FieldName;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.TypeName;
import com.palantir.conjure.spec.UnionDefinition;
import com.palantir.javapoet.JavaFile;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

final class UnionGeneratorTests {
    @TempDir
    Path output;

    @ParameterizedTest
    @CsvSource({"false,false", "true,false", "true,true"})
    void jacksonHelperNamesDoNotCollide(boolean sealed, boolean visitors) throws Exception {
        ConjureDefinition.Builder definition = ConjureDefinition.builder().version(1);
        for (String name : List.of("Serializer", "Deserializer", "Serializer_", "Deserializer_", "Named")) {
            UnionDefinition.Builder union = UnionDefinition.builder().typeName(TypeName.of(name, "collision"));
            for (String field : name.equals("Named")
                    ? List.of("serializer", "deserializer", "serializer_", "deserializer_")
                    : List.of("value")) {
                union.union(FieldDefinition.builder()
                        .fieldName(FieldName.of(field))
                        .type(Type.primitive(PrimitiveType.STRING))
                        .build());
            }
            definition.types(TypeDefinition.union(union.build()));
        }
        ConjureDefinition conjure = definition.build();
        ConjureDefinitionValidator.validateAll(conjure, SafetyDeclarationRequirements.ALLOWED);
        List<JavaFileObject> sources = new ObjectGenerator(Options.builder()
                        .sealedUnions(sealed)
                        .sealedUnionVisitors(visitors)
                        .build())
                .generate(conjure)
                .map(JavaFile::toJavaFileObject)
                .toList();
        compile(sources);
        try (URLClassLoader loader = new URLClassLoader(
                new URL[] {output.toUri().toURL()}, getClass().getClassLoader())) {
            ObjectMapper mapper = new ObjectMapper();
            for (String name : List.of("Serializer", "Deserializer", "Serializer_", "Deserializer_", "Named")) {
                Class<?> type = loader.loadClass("collision." + name);
                for (String field : name.equals("Named")
                        ? List.of("serializer", "deserializer", "serializer_", "deserializer_")
                        : List.of("value")) {
                    String json = "{\"type\":\"" + field + "\",\"" + field + "\":\"test\"}";
                    Object value = type.getMethod(field, String.class).invoke(null, "test");
                    assertThat(mapper.writerFor(type).writeValueAsString(value)).isEqualTo(json);
                    assertThat(mapper.readValue(json, type)).isEqualTo(value);
                    if (sealed) {
                        assertThat(value.getClass().getSimpleName())
                                .isEqualTo(Character.toUpperCase(field.charAt(0)) + field.substring(1));
                    }
                }
            }
        }
    }

    @ParameterizedTest
    @CsvSource({
        "false,false,false", "false,false,true", "false,true,false", "false,true,true",
        "true,false,false", "true,false,true", "true,true,false", "true,true,true"
    })
    void mapOwnershipAndWireSemantics(boolean sealed, boolean defensive, boolean nonNull) throws Exception {
        Type string = Type.primitive(PrimitiveType.STRING);
        Type map = Type.map(MapType.of(string, string));
        TypeName alias = TypeName.of("MapAlias", "ownership");
        ConjureDefinition definition = ConjureDefinition.builder()
                .version(1)
                .types(TypeDefinition.alias(
                        AliasDefinition.builder().typeName(alias).alias(map).build()))
                .types(TypeDefinition.union(UnionDefinition.builder()
                        .typeName(TypeName.of("MapUnion", "ownership"))
                        .union(FieldDefinition.builder()
                                .fieldName(FieldName.of("fromJson"))
                                .type(map)
                                .build())
                        .union(FieldDefinition.builder()
                                .fieldName(FieldName.of("fromJson_"))
                                .type(map)
                                .build())
                        .union(FieldDefinition.builder()
                                .fieldName(FieldName.of("map"))
                                .type(map)
                                .build())
                        .union(FieldDefinition.builder()
                                .fieldName(FieldName.of("mapOptional"))
                                .type(Type.map(MapType.of(string, Type.optional(OptionalType.of(string)))))
                                .build())
                        .union(FieldDefinition.builder()
                                .fieldName(FieldName.of("alias"))
                                .type(Type.reference(alias))
                                .build())
                        .build()))
                .build();
        compile(new ObjectGenerator(Options.builder()
                        .sealedUnions(sealed)
                        .defensiveCollections(defensive)
                        .nonNullCollections(nonNull)
                        .build())
                .generate(definition)
                .map(JavaFile::toJavaFileObject)
                .toList());
        try (URLClassLoader loader = new URLClassLoader(
                new URL[] {output.toUri().toURL()}, getClass().getClassLoader())) {
            Class<?> type = loader.loadClass("ownership.MapUnion");
            ObjectMapper mapper = ObjectMappers.newServerObjectMapper();
            for (String json : List.of(
                    "{\"type\":\"map\",\"map\":{\"z\":\"last\",\"a\":\"first\"}}",
                    "{\"map\":{\"z\":\"last\",\"a\":\"first\"},\"type\":\"map\"}")) {
                Object union = mapper.readValue(json, type);
                Map<?, ?> value = mapValue(union, sealed);
                assertThat(new ArrayList<>(value.keySet())).isEqualTo(List.of("z", "a"));
                assertThat(mapper.readTree(mapper.writeValueAsString(union))).isEqualTo(mapper.readTree(json));
                if (defensive) {
                    assertThatThrownBy(value::clear).isInstanceOf(UnsupportedOperationException.class);
                }
            }
            for (String field : List.of("map", "mapOptional", "alias", "fromJson", "fromJson_")) {
                for (String json : List.of(
                        "{\"type\":\"" + field + "\"}", "{\"type\":\"" + field + "\",\"" + field + "\":null}")) {
                    Object union = mapper.readValue(json, type);
                    assertThat(mapper.readTree(mapper.writeValueAsBytes(union))
                                    .get(field)
                                    .isEmpty())
                            .isTrue();
                }
            }
            Object optional = mapper.readValue("{\"type\":\"mapOptional\",\"mapOptional\":{\"empty\":null}}", type);
            assertThat(mapper.readTree(mapper.writeValueAsBytes(optional))
                            .get("mapOptional")
                            .get("empty")
                            .isNull())
                    .isTrue();
            String nullElement = "{\"type\":\"map\",\"map\":{\"empty\":null}}";
            if (defensive && nonNull) {
                assertThatThrownBy(() -> mapper.readValue(nullElement, type)).isInstanceOf(IOException.class);
            } else {
                assertThat(mapper.readTree(mapper.writeValueAsBytes(mapper.readValue(nullElement, type))))
                        .isEqualTo(mapper.readTree(nullElement));
            }
            Map<String, String> callerMap = new LinkedHashMap<>(Map.of("original", "value"));
            Object publicValue = type.getMethod("map", Map.class).invoke(null, callerMap);
            callerMap.put("added", "value");
            assertThat(mapValue(publicValue, sealed).containsKey("added")).isEqualTo(!defensive);

            if (defensive) {
                Map<String, String> shared = new LinkedHashMap<>(Map.of("shared", "original"));
                ObjectMapper customMapper = ObjectMappers.newServerObjectMapper()
                        .registerModule(new SimpleModule().addDeserializer(Map.class, new JsonDeserializer<>() {
                            @Override
                            public Map<?, ?> deserialize(JsonParser parser, DeserializationContext _context)
                                    throws IOException {
                                parser.skipChildren();
                                return shared;
                            }

                            @Override
                            public Map<?, ?> getEmptyValue(DeserializationContext _context) {
                                return shared;
                            }
                        }));
                ObjectMapper customConstructorMapper = ObjectMappers.newServerObjectMapper()
                        .registerModule(new SimpleModule()
                                .addValueInstantiator(
                                        LinkedHashMap.class, new ValueInstantiator.Base(LinkedHashMap.class) {
                                            @Override
                                            public boolean canCreateUsingDefault() {
                                                return true;
                                            }

                                            @Override
                                            public Object createUsingDefault(DeserializationContext _context) {
                                                return shared;
                                            }
                                        }));
                for (ObjectMapper custom : List.of(customMapper, customConstructorMapper)) {
                    for (String json : List.of(
                            "{\"type\":\"map\",\"map\":{}}", "{\"type\":\"map\",\"map\":null}", "{\"type\":\"map\"}")) {
                        shared.put("shared", "original");
                        Object union = custom.readValue(json, type);
                        shared.put("shared", "modified");
                        assertThat(mapValue(union, sealed).get("shared")).isEqualTo("original");
                        assertThatThrownBy(mapValue(union, sealed)::clear)
                                .isInstanceOf(UnsupportedOperationException.class);
                    }
                }
            }
        }
    }

    private static Map<?, ?> mapValue(Object union, boolean sealed) throws Exception {
        Object wrapper = union;
        if (!sealed) {
            Method getValue = union.getClass().getDeclaredMethod("getValue");
            getValue.setAccessible(true);
            wrapper = getValue.invoke(union);
        }
        Method accessor = wrapper.getClass().getDeclaredMethod(sealed ? "value" : "getValue");
        accessor.setAccessible(true);
        return (Map<?, ?>) accessor.invoke(wrapper);
    }

    private void compile(List<JavaFileObject> sources) throws IOException {
        List<String> classpath = new ArrayList<>();
        classpath.add(System.getProperty("java.class.path"));
        for (ClassLoader loader = getClass().getClassLoader(); loader != null; loader = loader.getParent()) {
            if (loader instanceof URLClassLoader urlLoader) {
                Arrays.stream(urlLoader.getURLs()).map(URL::getPath).forEach(classpath::add);
            }
        }
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        try (StandardJavaFileManager fileManager =
                compiler.getStandardFileManager(diagnostics, Locale.ROOT, StandardCharsets.UTF_8)) {
            boolean succeeded = compiler.getTask(
                            null,
                            fileManager,
                            diagnostics,
                            List.of(
                                    "--release",
                                    "17",
                                    "-proc:none",
                                    "-d",
                                    output.toString(),
                                    "-classpath",
                                    String.join(File.pathSeparator, classpath)),
                            null,
                            sources)
                    .call();
            assertThat(succeeded)
                    .describedAs("Generated sources must compile: %s", diagnostics.getDiagnostics())
                    .isTrue();
        }
    }
}
