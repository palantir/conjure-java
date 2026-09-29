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

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.palantir.conjure.defs.SafetyDeclarationRequirements;
import com.palantir.conjure.defs.validator.ConjureDefinitionValidator;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.spec.FieldName;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.SetType;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

final class UnionGeneratorTests {
    private static final ObjectMapper JSON_MAPPER = ObjectMappers.newServerObjectMapper();

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
                union.union(field(field, Type.primitive(PrimitiveType.STRING)));
            }
            definition.types(TypeDefinition.union(union.build()));
        }
        ConjureDefinition conjure = definition.build();
        ConjureDefinitionValidator.validateAll(conjure, SafetyDeclarationRequirements.ALLOWED);
        try (URLClassLoader loader = generate(
                conjure,
                Options.builder()
                        .sealedUnions(sealed)
                        .sealedUnionVisitors(visitors)
                        .build())) {
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
        try (URLClassLoader loader = generateCollections(sealed, defensive, nonNull)) {
            Class<?> type = loader.loadClass("ownership.MapUnion");
            ObjectMapper mapper = ObjectMappers.newServerObjectMapper();
            for (String json : List.of(
                    "{\"type\":\"map\",\"map\":{\"z\":\"last\",\"a\":\"first\"}}",
                    "{\"map\":{\"z\":\"last\",\"a\":\"first\"},\"type\":\"map\"}")) {
                Object union = mapper.readValue(json, type);
                Map<?, ?> value = mapValue(union, sealed);
                assertThat(new ArrayList<>(value.keySet())).isEqualTo(List.of("z", "a"));
                assertThat(wireTree(mapper, union)).isEqualTo(mapper.readTree(json));
                if (defensive) {
                    assertThatThrownBy(value::clear).isInstanceOf(UnsupportedOperationException.class);
                }
            }
            assertEmptyVariants(mapper, type, "map", "mapOptional", "alias", "fromJson", "fromJson_");
            Object optional = mapper.readValue("{\"type\":\"mapOptional\",\"mapOptional\":{\"empty\":null}}", type);
            assertThat(wireTree(mapper, optional).at("/mapOptional/empty").isNull())
                    .isTrue();
            String nullElement = "{\"type\":\"map\",\"map\":{\"empty\":null}}";
            if (defensive && nonNull) {
                assertThatThrownBy(() -> mapper.readValue(nullElement, type)).isInstanceOf(IOException.class);
            } else {
                assertThat(wireTree(mapper, mapper.readValue(nullElement, type)))
                        .isEqualTo(mapper.readTree(nullElement));
            }
            Map<String, String> callerMap = new LinkedHashMap<>(Map.of("original", "value"));
            Object publicValue = type.getMethod("map", Map.class).invoke(null, callerMap);
            callerMap.put("added", "value");
            assertThat(mapValue(publicValue, sealed).containsKey("added")).isEqualTo(!defensive);

            if (defensive) {
                Map<String, String> shared = new LinkedHashMap<>(Map.of("shared", "original"));
                for (ObjectMapper custom : customMappers(Map.class, shared)) {
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
        return (Map<?, ?>) unionValue(union, sealed);
    }

    private static Object unionValue(Object union, boolean sealed) throws Exception {
        Object wrapper = union;
        if (!sealed) {
            Method getValue = union.getClass().getDeclaredMethod("getValue");
            getValue.setAccessible(true);
            wrapper = getValue.invoke(union);
        }
        Method accessor = wrapper.getClass().getDeclaredMethod(sealed ? "value" : "getValue");
        accessor.setAccessible(true);
        return accessor.invoke(wrapper);
    }

    @ParameterizedTest
    @CsvSource({
        "false,false,false", "false,false,true", "false,true,false", "false,true,true",
        "true,false,false", "true,false,true", "true,true,false", "true,true,true"
    })
    void setOwnershipAndAliasWireSemantics(boolean sealed, boolean defensive, boolean nonNull) throws Exception {
        try (URLClassLoader loader = generateCollections(sealed, defensive, nonNull)) {
            Class<?> type = loader.loadClass("ownership.SetUnion");
            Class<?> aliasType = loader.loadClass("ownership.SetAlias");
            for (ObjectMapper mapper : List.of(
                    ObjectMappers.newServerObjectMapper(),
                    ObjectMappers.newCborServerObjectMapper(),
                    ObjectMappers.newSmileServerObjectMapper())) {
                assertSetWireSemantics(mapper, type, sealed, defensive, nonNull);
                assertAliasWireSemantics(mapper, loader);
            }
            Set<String> callerSet = new LinkedHashSet<>(List.of("original"));
            Object publicValue = type.getMethod("set", Set.class).invoke(null, callerSet);
            Object publicAlias = aliasType.getMethod("of", Set.class).invoke(null, callerSet);
            callerSet.add("added");
            assertThat(((Set<?>) unionValue(publicValue, sealed)).contains("added"))
                    .isEqualTo(!defensive);
            assertThat(((Set<?>) aliasType.getMethod("get").invoke(publicAlias)).contains("added"))
                    .isEqualTo(!defensive);
            if (defensive) {
                assertCustomSetOwnership(type, aliasType, sealed, nonNull);
                assertCustomElementOwnership(type, sealed);
                assertPolymorphicSetOwnership(type, aliasType, sealed);
            }
        }
    }

    private static void assertSetWireSemantics(
            ObjectMapper mapper, Class<?> type, boolean sealed, boolean defensive, boolean nonNull) throws Exception {
        for (String json : List.of(
                "{\"type\":\"set\",\"set\":[\"z\",\"a\",\"z\"]}", "{\"set\":[\"z\",\"a\",\"z\"],\"type\":\"set\"}")) {
            Object value = mapper.readValue(mapper.writeValueAsBytes(JSON_MAPPER.readTree(json)), type);
            Set<?> set = (Set<?>) unionValue(value, sealed);
            assertThat(new ArrayList<>(set)).isEqualTo(List.of("z", "a"));
            assertThat(wireTree(mapper, value))
                    .isEqualTo(JSON_MAPPER.readTree("{\"type\":\"set\",\"set\":[\"z\",\"a\"]}"));
            if (defensive) {
                assertThatThrownBy(set::clear).isInstanceOf(UnsupportedOperationException.class);
            }
        }
        assertEmptyVariants(mapper, type, "set", "setOptional", "numbers", "alias", "fromJson", "fromJson_");
        JsonNode optional = JSON_MAPPER.readTree("{\"type\":\"setOptional\",\"setOptional\":[null,\"a\"]}");
        assertThat(wireTree(mapper, mapper.readValue(mapper.writeValueAsBytes(optional), type)))
                .isEqualTo(optional);
        JsonNode nullable = JSON_MAPPER.readTree("{\"type\":\"set\",\"set\":[null]}");
        if (defensive && nonNull) {
            assertThatThrownBy(() -> mapper.readValue(mapper.writeValueAsBytes(nullable), type))
                    .isInstanceOf(IOException.class)
                    .hasMessageContaining("iterable cannot contain null elements");
        } else {
            assertThat(wireTree(mapper, mapper.readValue(mapper.writeValueAsBytes(nullable), type)))
                    .isEqualTo(nullable);
        }
    }

    private static JsonNode wireTree(ObjectMapper mapper, Object value) throws IOException {
        return mapper.readTree(mapper.writeValueAsBytes(value));
    }

    private static void assertEmptyVariants(ObjectMapper mapper, Class<?> type, String... fields) throws IOException {
        for (String field : fields) {
            for (String json :
                    List.of("{\"type\":\"" + field + "\"}", "{\"type\":\"" + field + "\",\"" + field + "\":null}")) {
                Object value = mapper.readValue(mapper.writeValueAsBytes(JSON_MAPPER.readTree(json)), type);
                assertThat(wireTree(mapper, value).get(field).isEmpty()).isTrue();
            }
        }
    }

    private static void assertAliasWireSemantics(ObjectMapper mapper, ClassLoader loader) throws Exception {
        Class<?> unionType = loader.loadClass("ownership.SetUnion");
        for (AliasCase alias : List.of(
                new AliasCase("SetAlias", Set.class, new LinkedHashSet<>(List.of("z", "a")), "[\"z\",\"a\"]"),
                new AliasCase("ListAlias", List.class, List.of("z", "a", "z"), "[\"z\",\"a\",\"z\"]"),
                new AliasCase(
                        "MapAlias", Map.class, new LinkedHashMap<>(Map.of("key", "value")), "{\"key\":\"value\"}"),
                new AliasCase(
                        "UnionAlias",
                        unionType,
                        unionType.getMethod("set", Set.class).invoke(null, new LinkedHashSet<>(List.of("z", "a"))),
                        "{\"type\":\"set\",\"set\":[\"z\",\"a\"]}"))) {
            Class<?> type = loader.loadClass("ownership." + alias.name());
            Object expected = type.getMethod("of", alias.parameter()).invoke(null, alias.input());
            JsonNode wireValue = JSON_MAPPER.readTree(alias.json());
            byte[] serialized = mapper.writerFor(type).writeValueAsBytes(expected);
            assertThat(mapper.readTree(serialized)).isEqualTo(wireValue);
            assertThat(mapper.readValue(mapper.writeValueAsBytes(wireValue), type))
                    .isEqualTo(expected);
            assertThat(mapper.readValue(serialized, type)).isEqualTo(expected);
            Map<String, ?> nested = mapper.readValue(
                    mapper.writeValueAsBytes(Map.of("key", expected)),
                    mapper.getTypeFactory().constructMapType(Map.class, String.class, type));
            assertThat(nested).isEqualTo(Map.of("key", expected));
            Object[] array = (Object[])
                    mapper.readValue(mapper.writeValueAsBytes(List.of(expected, expected)), type.arrayType());
            assertThat(array).containsExactly(expected, expected);
        }
    }

    private record AliasCase(String name, Class<?> parameter, Object input, String json) {}

    private static void assertCustomSetOwnership(Class<?> type, Class<?> aliasType, boolean sealed, boolean nonNull)
            throws Exception {
        Set<String> shared = new LinkedHashSet<>();
        for (ObjectMapper mapper : customMappers(LinkedHashSet.class, shared)) {
            for (String json :
                    List.of("{\"type\":\"set\",\"set\":[]}", "{\"type\":\"set\",\"set\":null}", "{\"type\":\"set\"}")) {
                shared.clear();
                shared.add("original");
                Object union = mapper.readValue(json, type);
                Object alias = mapper.readValue("[]", aliasType);
                shared.clear();
                assertThat(new ArrayList<>((Set<?>) unionValue(union, sealed)))
                        .describedAs("Union ownership for %s using %s", json, mapper.getRegisteredModuleIds())
                        .isEqualTo(List.of("original"));
                Set<?> aliasValue = (Set<?>) aliasType.getMethod("get").invoke(alias);
                assertThat(new ArrayList<>(aliasValue))
                        .describedAs("Alias ownership using %s", mapper.getRegisteredModuleIds())
                        .isEqualTo(List.of("original"));
                assertThatThrownBy(aliasValue::clear).isInstanceOf(UnsupportedOperationException.class);
            }
            shared.add(null);
            if (nonNull) {
                assertThatThrownBy(() -> mapper.readValue("{\"type\":\"set\",\"set\":[]}", type))
                        .hasMessageContaining("iterable cannot contain null elements");
                assertThatThrownBy(() -> mapper.readValue("[]", aliasType))
                        .hasMessageContaining("iterable cannot contain null elements");
            }
        }
        ObjectMapper recovering = ObjectMappers.newServerObjectMapper().addHandler(new DeserializationProblemHandler() {
            @Override
            public Object handleUnexpectedToken(
                    DeserializationContext context,
                    JavaType targetType,
                    JsonToken token,
                    JsonParser parser,
                    String message)
                    throws IOException {
                return Set.class.isAssignableFrom(targetType.getRawClass()) ? shared : NOT_HANDLED;
            }
        });
        shared.clear();
        shared.add("original");
        Object recovered = recovering.readValue("{\"type\":\"set\",\"set\":42}", type);
        shared.clear();
        assertThat(new ArrayList<>((Set<?>) unionValue(recovered, sealed))).isEqualTo(List.of("original"));
    }

    private static void assertCustomElementOwnership(Class<?> type, boolean sealed) throws Exception {
        AtomicReference<Set<?>> captured = new AtomicReference<>();
        ObjectMapper mapper = ObjectMappers.newServerObjectMapper()
                .registerModule(new SimpleModule().addDeserializer(Integer.class, new JsonDeserializer<>() {
                    @Override
                    public Integer deserialize(JsonParser parser, DeserializationContext _context) throws IOException {
                        captured.set((Set<?>) parser.currentValue());
                        return parser.getIntValue();
                    }
                }));
        Object union = mapper.readValue("{\"type\":\"numbers\",\"numbers\":[1,2]}", type);
        captured.get().clear();
        assertThat(new ArrayList<>((Set<?>) unionValue(union, sealed))).isEqualTo(List.of(1, 2));
    }

    private static void assertPolymorphicSetOwnership(Class<?> type, Class<?> aliasType, boolean sealed)
            throws Exception {
        SharedSet shared = new SharedSet();
        shared.add("original");
        SimpleModule module = sharedDeserializer(SharedSet.class, shared);
        module.registerSubtypes(new NamedType(SharedSet.class, "shared"));
        ObjectMapper mapper = ObjectMappers.newServerObjectMapper()
                .addMixIn(Set.class, TypedSet.class)
                .registerModule(module);
        Object union = mapper.readValue("{\"type\":\"set\",\"set\":[\"shared\",[]]}", type);
        Object alias = mapper.readValue("[\"shared\",[]]", aliasType);
        shared.clear();
        assertThat(new ArrayList<>((Set<?>) unionValue(union, sealed))).isEqualTo(List.of("original"));
        assertThat(new ArrayList<>((Set<?>) aliasType.getMethod("get").invoke(alias)))
                .isEqualTo(List.of("original"));
    }

    private static <T> SimpleModule sharedDeserializer(Class<?> type, T shared) {
        SimpleModule module = new SimpleModule("shared-deserializer");
        module.setDeserializers(new SimpleDeserializers(Map.of(type, new JsonDeserializer<T>() {
            @Override
            public T deserialize(JsonParser parser, DeserializationContext _context) throws IOException {
                parser.skipChildren();
                return shared;
            }

            @Override
            public T getEmptyValue(DeserializationContext _context) {
                return shared;
            }
        })));
        return module;
    }

    private static <T> List<ObjectMapper> customMappers(Class<?> binding, T shared) {
        SimpleModule constructor = new SimpleModule("shared-constructor")
                .addValueInstantiator(shared.getClass(), new ValueInstantiator.Base(shared.getClass()) {
                    @Override
                    public boolean canCreateUsingDefault() {
                        return true;
                    }

                    @Override
                    public Object createUsingDefault(DeserializationContext _context) {
                        return shared;
                    }
                });
        return List.of(
                ObjectMappers.newServerObjectMapper().registerModule(sharedDeserializer(binding, shared)),
                ObjectMappers.newServerObjectMapper().registerModule(constructor));
    }

    private URLClassLoader generateCollections(boolean sealed, boolean defensive, boolean nonNull) throws IOException {
        Type string = Type.primitive(PrimitiveType.STRING);
        Type map = Type.map(MapType.of(string, string));
        Type set = Type.set(SetType.of(string));
        ConjureDefinition.Builder definition = ConjureDefinition.builder().version(1);
        Map.ofEntries(
                        entry("MapAlias", map),
                        entry("SetAlias", set),
                        entry("ListAlias", Type.list(ListType.of(string))),
                        entry("UnionAlias", Type.reference(TypeName.of("SetUnion", "ownership"))))
                .forEach((name, type) -> definition.types(TypeDefinition.alias(AliasDefinition.builder()
                        .typeName(TypeName.of(name, "ownership"))
                        .alias(type)
                        .build())));
        definition.types(unionDefinition(
                "MapUnion",
                field("map", map),
                field("fromJson", map),
                field("fromJson_", map),
                field("mapOptional", Type.map(MapType.of(string, Type.optional(OptionalType.of(string))))),
                field("alias", Type.reference(TypeName.of("MapAlias", "ownership")))));
        definition.types(unionDefinition(
                "SetUnion",
                field("set", set),
                field("fromJson", set),
                field("fromJson_", set),
                field("setOptional", Type.set(SetType.of(Type.optional(OptionalType.of(string))))),
                field("numbers", Type.set(SetType.of(Type.primitive(PrimitiveType.INTEGER)))),
                field("alias", Type.reference(TypeName.of("SetAlias", "ownership")))));
        return generate(
                definition.build(),
                Options.builder()
                        .sealedUnions(sealed)
                        .defensiveCollections(defensive)
                        .nonNullCollections(nonNull)
                        .build());
    }

    private static TypeDefinition unionDefinition(String name, FieldDefinition... fields) {
        return TypeDefinition.union(UnionDefinition.builder()
                .typeName(TypeName.of(name, "ownership"))
                .union(List.of(fields))
                .build());
    }

    private static FieldDefinition field(String name, Type type) {
        return FieldDefinition.builder()
                .fieldName(FieldName.of(name))
                .type(type)
                .build();
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_ARRAY)
    private interface TypedSet {}

    @SuppressWarnings(
            "checkstyle:IllegalType") // Tests Jackson subtype handling for the generated LinkedHashSet binding.
    private static final class SharedSet extends LinkedHashSet<String> {
        private static final long serialVersionUID = 1L;
    }

    private URLClassLoader generate(ConjureDefinition definition, Options options) throws IOException {
        List<JavaFileObject> sources = new ObjectGenerator(options)
                .generate(definition)
                .map(JavaFile::toJavaFileObject)
                .toList();
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
            List<String> compilerOptions = new ArrayList<>(List.of("--release", "17", "-proc:none"));
            compilerOptions.addAll(
                    List.of("-d", output.toString(), "-classpath", String.join(File.pathSeparator, classpath)));
            boolean succeeded = compiler.getTask(null, fileManager, diagnostics, compilerOptions, null, sources)
                    .call();
            assertThat(succeeded)
                    .describedAs("Generated sources must compile: %s", diagnostics.getDiagnostics())
                    .isTrue();
        }
        return new URLClassLoader(new URL[] {output.toUri().toURL()}, getClass().getClassLoader());
    }
}
