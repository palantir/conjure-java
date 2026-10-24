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

package com.palantir.conjure.java.lib.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonMerge;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

final class ConjureContainerOwnershipTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void copiesStandardScalarMergeInputs() throws IOException {
        for (boolean immutable : List.of(false, true)) {
            Map<String, Integer> map = immutable ? Map.of("original", 1) : new LinkedHashMap<>(Map.of("original", 1));
            Set<Integer> set = immutable ? Set.of(1) : new LinkedHashSet<>(Set.of(1));
            Merge holder = new Merge();
            holder.map = map;
            holder.set = set;
            mapper.readerForUpdating(holder).readValue("{\"map\":{\"added\":2},\"set\":[2]}");
            assertThat(holder.map).isEqualTo(Map.of("original", 1, "added", 2)).isNotSameAs(map);
            assertThat(holder.set).containsExactly(1, 2).isNotSameAs(set);
            assertThat(map).containsExactlyEntriesOf(Map.of("original", 1));
            assertThat(set).containsExactly(1);
        }
    }

    @Test
    void copiesStandardScalarInputsWithDefaultMerging() throws IOException {
        ObjectMapper merging = new ObjectMapper().setDefaultMergeable(true);
        DefaultMerge holder = new DefaultMerge();
        holder.map = Map.of("original", 1);
        holder.set = Set.of(1);
        merging.readerForUpdating(holder).readValue("{\"map\":{\"added\":2},\"set\":[2]}");
        assertThat(holder.map).isEqualTo(Map.of("original", 1, "added", 2));
        assertThat(holder.set).containsExactly(1, 2);
    }

    @Test
    void rejectsForwardReferencesInsteadOfDiscardingEntries() {
        for (boolean failOnUnresolved : List.of(false, true)) {
            ObjectMapper configured =
                    mapper.copy().configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, failOnUnresolved);
            for (String json : List.of(
                    "{\"map\":{\"first\":2},\"node\":{\"id\":2}}",
                    "{\"set\":[2],\"node\":{\"id\":2}}",
                    "{\"map\":{\"first\":2,\"second\":{\"id\":2}}}",
                    "{\"set\":[2,{\"id\":2}]}")) {
                assertThatThrownBy(() -> configured.readValue(json, References.class))
                        .isInstanceOf(JsonMappingException.class)
                        .hasMessageContaining("Forward object-id references are not supported in owned containers");
            }
        }
    }

    @Test
    void preservesResolvedReferences() throws IOException {
        References references =
                mapper.readValue("{\"node\":{\"id\":2},\"map\":{\"first\":2},\"set\":[2]}", References.class);
        assertThat(references.map().get("first")).isSameAs(references.node());
        assertThat(references.set()).containsExactly(references.node());
        References inMap = mapper.readValue("{\"map\":{\"first\":{\"id\":2},\"second\":2}}", References.class);
        assertThat(inMap.map().get("first")).isSameAs(inMap.map().get("second"));
        assertThat(mapper.readValue("{\"set\":[{\"id\":2},2]}", References.class)
                        .set())
                .hasSize(1);
    }

    @Test
    void handlesReferencesToPolymorphicEntries() throws IOException {
        ObjectMapper typed = mapper.copy().addMixIn(Node.class, TypedEntry.class);
        typed.registerSubtypes(new NamedType(Node.class, "node"));
        References resolved =
                typed.readValue("{\"node\":[\"node\",{\"id\":2}],\"map\":{\"first\":2},\"set\":[2]}", References.class);
        assertThat(resolved.map().get("first")).isSameAs(resolved.node());
        assertThat(resolved.set()).containsExactly(resolved.node());
        for (String json : List.of(
                "{\"map\":{\"first\":2},\"node\":[\"node\",{\"id\":2}]}",
                "{\"set\":[2],\"node\":[\"node\",{\"id\":2}]}")) {
            assertThatThrownBy(() -> typed.readValue(json, References.class))
                    .isInstanceOf(JsonMappingException.class)
                    .hasMessageContaining("Forward object-id references are not supported in owned containers");
        }
    }

    @Test
    void rejectsObjectIdEntriesInPolymorphicContainers() {
        ObjectMapper maps = mapper.copy().addMixIn(Map.class, TypedEntry.class);
        maps.registerSubtypes(new NamedType(LinkedHashMap.class, "map"));
        assertThatThrownBy(
                        () -> maps.readValue("{\"map\":[\"map\",{\"first\":2}],\"node\":{\"id\":2}}", References.class))
                .isInstanceOf(JsonMappingException.class)
                .hasMessageContaining("Object-id entries in polymorphic owned containers are not supported");
        ObjectMapper sets = mapper.copy().addMixIn(Set.class, TypedEntry.class);
        sets.registerSubtypes(new NamedType(LinkedHashSet.class, "set"));
        assertThatThrownBy(() -> sets.readValue("{\"set\":[\"set\",[2]],\"node\":{\"id\":2}}", References.class))
                .isInstanceOf(JsonMappingException.class)
                .hasMessageContaining("Object-id entries in polymorphic owned containers are not supported");
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_ARRAY)
    private interface TypedEntry {}

    static final class Merge {
        @JsonMerge
        @JsonDeserialize(using = ConjureJacksonSupport.ContainerDeserializer.class)
        public Map<String, Integer> map;

        @JsonMerge
        @JsonDeserialize(using = ConjureJacksonSupport.ContainerDeserializer.class)
        public Set<Integer> set;
    }

    static final class DefaultMerge {
        @JsonDeserialize(using = ConjureJacksonSupport.ContainerDeserializer.class)
        public Map<String, Integer> map;

        @JsonDeserialize(using = ConjureJacksonSupport.ContainerDeserializer.class)
        public Set<Integer> set;
    }

    record References(
            @JsonDeserialize(using = ConjureJacksonSupport.ContainerDeserializer.class)
            Map<String, Node> map,

            @JsonDeserialize(using = ConjureJacksonSupport.ContainerDeserializer.class)
            Set<Node> set,

            Node node) {}

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    static final class Node {
        public int id;
    }
}
