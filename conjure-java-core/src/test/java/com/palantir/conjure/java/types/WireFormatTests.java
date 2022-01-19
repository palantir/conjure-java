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

package com.palantir.conjure.java.types;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.palantir.conjure.java.lib.Bytes;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.product.BinaryAliasExample;
import com.palantir.product.BinaryExample;
import com.palantir.product.DateTimeExample;
import com.palantir.product.DoubleAliasExample;
import com.palantir.product.DoubleExample;
import com.palantir.product.EmptyObjectExample;
import com.palantir.product.EnumExample;
import com.palantir.product.ExternalLongAliasExample;
import com.palantir.product.ExternalStringAliasExample;
import com.palantir.product.IntegerAliasExample;
import com.palantir.product.ListAlias;
import com.palantir.product.ListExample;
import com.palantir.product.MapAliasExample;
import com.palantir.product.MapExample;
import com.palantir.product.OptionalAlias;
import com.palantir.product.OptionalAliasExample;
import com.palantir.product.OptionalExample;
import com.palantir.product.OptionalListAliasExample;
import com.palantir.product.OptionalMapAliasExample;
import com.palantir.product.OptionalSetAliasExample;
import com.palantir.product.PrimitiveOptionalsExample;
import com.palantir.product.SetAlias;
import com.palantir.product.SetExample;
import com.palantir.product.StringAliasExample;
import com.palantir.product.StringAliasOne;
import com.palantir.product.StringAliasTwo;
import com.palantir.product.StringExample;
import com.palantir.product.UnionTypeExample;
import com.palantir.product.UuidExample;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
public final class WireFormatTests {
    private final ObjectMapper mapper = ObjectMappers.newServerObjectMapper();

    @Test
    public void testPresentCollectionFieldsDeserializeWithElements() throws Exception {
        assertThat(mapper.readValue("{\"items\": [\"a\", \"b\"]}", SetExample.class)
                        .getItems())
                .contains("a", "b");
        assertThat(mapper.readValue("{\"items\": [\"a\", \"b\"]}", ListExample.class)
                        .getItems())
                .contains("a", "b");
        assertThat(mapper.readValue("{\"items\": {\"a\": \"b\"}}", MapExample.class)
                        .getItems())
                .containsEntry("a", "b");
    }

    @Test
    public void double_alias_should_deserialize_nan() throws IOException {
        assertThat(mapper.readValue("\"NaN\"", DoubleAliasExample.class).get()).isNaN();
    }

    @Test
    public void double_alias_should_deserialize_negative_zero() throws IOException {
        DoubleAliasExample conjureType = mapper.readValue("-0.0", DoubleAliasExample.class);
        assertThat(conjureType).hasToString("-0.0");
        assertThat(conjureType).isEqualTo(DoubleAliasExample.of(-0.0d));
        assertThat(conjureType)
                .describedAs("Java is weird: == gets this wrong, but .equals gets it right")
                .isNotEqualTo(DoubleAliasExample.of(0.0d));
    }

    @Test
    public void double_alias_should_deserialize_infinity() throws IOException {
        assertThat(mapper.readValue("\"Infinity\"", DoubleAliasExample.class).get())
                .isEqualTo(Double.POSITIVE_INFINITY);

        assertThat(mapper.readValue("\"-Infinity\"", DoubleAliasExample.class).get())
                .isEqualTo(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testPresentOptionalDeserializesWithElement() throws Exception {
        assertThat(mapper.readValue("{\"item\": \"a\"}", OptionalExample.class).getItem())
                .contains("a");
    }

    @Test
    public void testOptionalSerialization() throws Exception {
        assertThat(mapper.writeValueAsString(OptionalExample.of("a"))).isEqualTo("{\"item\":\"a\"}");
    }

    @Test
    public void double_nan_fields_should_be_serialized_as_a_string() throws Exception {
        assertThat(mapper.writeValueAsString(DoubleExample.of(Double.NaN))).isEqualTo("{\"doubleValue\":\"NaN\"}");
    }

    @Test
    public void double_nan_fields_should_deserialize_from_a_string() throws Exception {
        DoubleExample deserialized = mapper.readValue("{\"doubleValue\":\"NaN\"}", DoubleExample.class);
        assertThat(deserialized.getDoubleValue()).isNaN();
    }

    @Test
    public void testBinaryFieldsDeserializeFromBase64() throws Exception {
        assertThat(mapper.readValue("{\"binary\": \"AAEC\"}", BinaryExample.class)
                        .getBinary())
                .isEqualTo(Bytes.from(new byte[] {0, 1, 2}));
    }

    @Test
    public void testBinaryFieldsDeserializeFromBase64_legacy() throws Exception {
        assertThat(mapper.readValue("{\"binary\": \"AAEC\"}", com.palantir.binary.BinaryExample.class)
                        .getBinary())
                .isEqualTo(ByteBuffer.wrap(new byte[] {0, 1, 2}));
    }

    @Test
    public void testBinaryFieldsSerializeToBase64() throws Exception {
        assertThat(mapper.writeValueAsString(BinaryExample.builder()
                        .binary(Bytes.from(new byte[] {0, 1, 2}))
                        .build()))
                .isEqualTo("{\"binary\":\"AAEC\"}");
    }

    @Test
    public void testBinaryFieldsSerializeToBase64_legacy() throws Exception {
        assertThat(mapper.writeValueAsString(com.palantir.binary.BinaryExample.builder()
                        .binary(ByteBuffer.wrap(new byte[] {0, 1, 2}))
                        .build()))
                .isEqualTo("{\"binary\":\"AAEC\"}");
    }

    @Test
    public void testBinaryAliasFieldsDeserializeFromBase64() throws Exception {
        assertThat(mapper.readValue("\"AAEC\"", BinaryAliasExample.class).get())
                .isEqualTo(Bytes.from(new byte[] {0, 1, 2}));
    }

    @Test
    public void testBinaryAliasFieldsDeserializeFromBase64_legacy() throws Exception {
        assertThat(mapper.readValue("\"AAEC\"", com.palantir.binary.BinaryAliasExample.class)
                        .get())
                .isEqualTo(ByteBuffer.wrap(new byte[] {0, 1, 2}));
    }

    @Test
    public void testBinaryAliasFieldsSerializeToBase64() throws Exception {
        assertThat(mapper.writeValueAsString(BinaryAliasExample.of(Bytes.from(new byte[] {0, 1, 2}))))
                .isEqualTo("\"AAEC\"");
    }

    @Test
    public void testBinaryAliasFieldsSerializeToBase64_legacy() throws Exception {
        assertThat(mapper.writeValueAsString(
                        com.palantir.binary.BinaryAliasExample.of(ByteBuffer.wrap(new byte[] {0, 1, 2}))))
                .isEqualTo("\"AAEC\"");
    }

    @Test
    public void testObjectsThatImplementEqualityImplementDeepEquality() throws Exception {
        assertThat(SetExample.builder().items("a").items("b").build())
                .isEqualTo(SetExample.builder().items("b").items("a").build());
        assertThat(SetExample.builder().items("a").items("c").build())
                .isNotEqualTo(SetExample.builder().items("b").items("a").build());

        assertThat(OptionalExample.builder().item("a").build())
                .isEqualTo(OptionalExample.builder().item("a").build());
        assertThat(OptionalExample.builder().build())
                .isEqualTo(OptionalExample.builder().build());
        assertThat(OptionalExample.builder().item("a").build())
                .isNotEqualTo(OptionalExample.builder().item("b").build());
    }

    @Test
    public void testObjectsThatImplementHashCodeImplementDeepHashCode() {
        assertThat(BinaryExample.of(Bytes.from(new byte[] {0, 1, 2})).hashCode())
                .isEqualTo(BinaryExample.of(Bytes.from(new byte[] {0, 1, 2})).hashCode());

        assertThat(OptionalExample.builder().item("a").build().hashCode())
                .isEqualTo(OptionalExample.builder().item("a").build().hashCode());

        assertThat(SetExample.builder().items("a").items("b").build().hashCode())
                .isEqualTo(SetExample.builder().items("b").items("a").build().hashCode());
    }

    @Test
    public void testEmptyObjectsSerialize() throws Exception {
        assertThat(mapper.writeValueAsString(EmptyObjectExample.of())).isEqualTo("{}");
    }

    @Test
    public void testEmptyObjectsDeserialize() throws Exception {
        assertThat(mapper.readValue("{}", EmptyObjectExample.class)).isEqualTo(EmptyObjectExample.of());
    }

    @Test
    public void enumSerializationDoesntPreserveCase() throws Exception {
        assertThat(mapper.writeValueAsString(mapper.readValue("\"one\"", EnumExample.class)))
                .isEqualTo("\"ONE\"");
        assertThat(mapper.writeValueAsString(mapper.readValue("\"ONE\"", EnumExample.class)))
                .isEqualTo("\"ONE\"");
        assertThat(mapper.writeValueAsString(mapper.readValue("\"onE\"", EnumExample.class)))
                .isEqualTo("\"ONE\"");
    }

    @Test
    public void testEnumCasingDeserializationInvariantToInputCase() throws Exception {
        assertThat(mapper.readValue("\"ONE\"", EnumExample.class)).isEqualTo(EnumExample.ONE);
        assertThat(mapper.readValue("\"one\"", EnumExample.class)).isEqualTo(EnumExample.ONE);
        assertThat(mapper.readValue("\"onE\"", EnumExample.class)).isEqualTo(EnumExample.ONE);
        assertThat(mapper.readValue("\"oNE\"", EnumExample.class)).isEqualTo(EnumExample.ONE);
    }

    @Test
    public void testIgnoreUnknownValuesDuringDeserialization() throws Exception {
        assertThat(ObjectMappers.newClientObjectMapper().readValue("{\"fake\": \"fake\"}", OptionalExample.class))
                .isEqualTo(OptionalExample.builder().build());
    }

    @Test
    public void testEnumRoundTripsUnknownValue() throws Exception {
        assertThat(mapper.writeValueAsString(mapper.readValue("\"FAKE_FAKE_FAKE\"", EnumExample.class)))
                .isEqualTo("\"FAKE_FAKE_FAKE\"");
        // nb: we upper-case incoming values to sanitize, so fake_FAKE_fake would fail here
    }

    @Test
    public void testAliasTypesEqualWhenInnerTypeEqual() throws Exception {
        assertThat(StringAliasExample.of("a")).isEqualTo(StringAliasExample.of("a"));
        assertThat(IntegerAliasExample.of(103)).isEqualTo(IntegerAliasExample.of(103));
        assertThat(DoubleAliasExample.of(10.3)).isEqualTo(DoubleAliasExample.of(10.3));

        Bytes bytes = Bytes.from(new byte[] {0, 1, 2});
        assertThat(BinaryAliasExample.of(bytes)).isEqualTo(BinaryAliasExample.of(bytes));
    }

    @Test
    public void testAliasTypesEqualWhenInnerTypeEqual_legacyBinary() throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[] {0, 1, 2});
        assertThat(com.palantir.binary.BinaryAliasExample.of(byteBuffer))
                .isEqualTo(com.palantir.binary.BinaryAliasExample.of(byteBuffer));
    }

    @Test
    public void testAliasTypesHashCodeEqualWhenInnerTypeEqual() throws Exception {
        assertThat(StringAliasExample.of("a").hashCode())
                .isEqualTo(StringAliasExample.of("a").hashCode());
        assertThat(IntegerAliasExample.of(103).hashCode())
                .isEqualTo(IntegerAliasExample.of(103).hashCode());
        assertThat(DoubleAliasExample.of(10.3).hashCode())
                .isEqualTo(DoubleAliasExample.of(10.3).hashCode());
        Bytes bytes = Bytes.from(new byte[] {0, 1, 2});
        assertThat(BinaryAliasExample.of(bytes).hashCode())
                .isEqualTo(BinaryAliasExample.of(bytes).hashCode());
    }

    @Test
    public void testAliasTypesHashCodeEqualWhenInnerTypeEqual_legacyBinary() throws Exception {
        assertThat(StringAliasExample.of("a").hashCode())
                .isEqualTo(StringAliasExample.of("a").hashCode());
        assertThat(IntegerAliasExample.of(103).hashCode())
                .isEqualTo(IntegerAliasExample.of(103).hashCode());
        assertThat(DoubleAliasExample.of(10.3).hashCode())
                .isEqualTo(DoubleAliasExample.of(10.3).hashCode());
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[] {0, 1, 2});
        assertThat(com.palantir.binary.BinaryAliasExample.of(byteBuffer).hashCode())
                .isEqualTo(com.palantir.binary.BinaryAliasExample.of(byteBuffer).hashCode());
    }

    @Test
    public void testUnionType() throws Exception {
        StringExample stringExample = StringExample.of("foo");
        UnionTypeExample unionTypeStringExample = UnionTypeExample.stringExample(stringExample);
        UnionTypeExample unionTypeSet = UnionTypeExample.set(ImmutableSet.of("item"));
        UnionTypeExample unionTypeInt = UnionTypeExample.thisFieldIsAnInteger(5);
        String serializedUnionTypeStringExample = "{\"type\":\"stringExample\",\"stringExample\":{\"string\":\"foo\"}}";
        String serializedUnionTypeSet = "{\"type\":\"set\",\"set\":[\"item\"]}";
        String serializedUnionTypeInt = "{\"type\":\"thisFieldIsAnInteger\",\"thisFieldIsAnInteger\":5}";

        // serialization
        assertThat(mapper.writeValueAsString(unionTypeStringExample)).isEqualTo(serializedUnionTypeStringExample);
        assertThat(mapper.writeValueAsString(unionTypeSet)).isEqualTo(serializedUnionTypeSet);
        assertThat(mapper.writeValueAsString(unionTypeInt)).isEqualTo(serializedUnionTypeInt);
        // serialization of member type is unchanged
        assertThat(mapper.writeValueAsString(stringExample)).isEqualTo("{\"string\":\"foo\"}");

        // deserialization and equals()
        assertThat(mapper.readValue(serializedUnionTypeStringExample, UnionTypeExample.class))
                .isEqualTo(unionTypeStringExample);
        assertThat(mapper.readValue(serializedUnionTypeSet, UnionTypeExample.class))
                .isEqualTo(unionTypeSet);
        assertThat(mapper.readValue(serializedUnionTypeInt, UnionTypeExample.class))
                .isEqualTo(unionTypeInt);

        // visitor
        UnionTypeExample.Visitor<Integer> visitor = new TestVisitor();
        assertThat(unionTypeStringExample.accept(visitor)).isEqualTo("foo".length());
        assertThat(unionTypeInt.accept(visitor)).isEqualTo(5);
        assertThat(unionTypeSet.accept(visitor)).isEqualTo(1);
    }

    @Test
    public void testUnionType_unknownType() throws Exception {
        String serializedUnionTypeUnknown = "{\"type\":\"notknown\",\"notknown\":5}";
        UnionTypeExample unionTypeUnknown = mapper.readValue(serializedUnionTypeUnknown, UnionTypeExample.class);
        assertThat(mapper.writeValueAsString(unionTypeUnknown)).isEqualTo(serializedUnionTypeUnknown);
        assertThat(unionTypeUnknown.accept(new TestVisitor())).isZero();
    }

    @Test
    public void testUnionType_knownUnknown() throws Exception {
        String serializedUnionTypeUnknown = "{\"type\":\"unknown\",\"unknown\":5}";
        UnionTypeExample unionTypeUnknown = mapper.readValue(serializedUnionTypeUnknown, UnionTypeExample.class);
        assertThat(mapper.writeValueAsString(unionTypeUnknown)).isEqualTo(serializedUnionTypeUnknown);
        assertThat(unionTypeUnknown.accept(new TestVisitor())).isEqualTo(5);
    }

    @Test
    public void testUnionType_noType() {
        assertThatThrownBy(() -> mapper.readValue("{\"typ\":\"unknown\",\"value\":5}", UnionTypeExample.class))
                .isInstanceOf(JsonMappingException.class);
    }

    @Test
    public void testUnionType_excludedOptionalValue() throws IOException {
        assertThat(mapper.readValue("{\"type\":\"optional\"}", UnionTypeExample.class))
                .isEqualTo(UnionTypeExample.optional(Optional.empty()));
    }

    @Test
    public void testUnionType_nullOptionalValue() throws IOException {
        assertThat(mapper.readValue("{\"type\":\"optional\",\"optional\":null}", UnionTypeExample.class))
                .isEqualTo(UnionTypeExample.optional(Optional.empty()));
    }

    @Test
    public void testUnionType_excludedOptionalAliasValue() throws IOException {
        assertThat(mapper.readValue("{\"type\":\"optionalAlias\"}", UnionTypeExample.class))
                .isEqualTo(UnionTypeExample.optionalAlias(OptionalAlias.of(Optional.empty())));
    }

    @Test
    public void testUnionType_nullOptionalAliasValue() throws IOException {
        assertThat(mapper.readValue("{\"type\":\"optionalAlias\",\"optionalAlias\":null}", UnionTypeExample.class))
                .isEqualTo(UnionTypeExample.optionalAlias(OptionalAlias.of(Optional.empty())));
    }

    @Test
    public void testUnionType_excludedSetValue() throws IOException {
        assertThat(mapper.readValue("{\"type\":\"set\"}", UnionTypeExample.class))
                .isEqualTo(UnionTypeExample.set(ImmutableSet.of()));
    }

    @Test
    public void testUnionType_nullSetValue() throws IOException {
        assertThat(mapper.readValue("{\"type\":\"set\",\"set\":null}", UnionTypeExample.class))
                .isEqualTo(UnionTypeExample.set(ImmutableSet.of()));
    }

    @Test
    public void testUnionType_excludedSetAliasValue() throws IOException {
        assertThat(mapper.readValue("{\"type\":\"setAlias\"}", UnionTypeExample.class))
                .isEqualTo(UnionTypeExample.setAlias(SetAlias.of(ImmutableSet.of())));
    }

    @Test
    public void testUnionType_nullSetAliasValue() throws IOException {
        assertThat(mapper.readValue("{\"type\":\"setAlias\",\"setAlias\":null}", UnionTypeExample.class))
                .isEqualTo(UnionTypeExample.setAlias(SetAlias.of(ImmutableSet.of())));
    }

    @Test
    public void testUnionType_excludedListValue() throws IOException {
        assertThat(mapper.readValue("{\"type\":\"list\"}", UnionTypeExample.class))
                .isEqualTo(UnionTypeExample.list(ImmutableList.of()));
    }

    @Test
    public void testUnionType_nullListValue() throws IOException {
        assertThat(mapper.readValue("{\"type\":\"list\",\"list\":null}", UnionTypeExample.class))
                .isEqualTo(UnionTypeExample.list(ImmutableList.of()));
    }

    @Test
    public void testUnionType_excludedListAliasValue() throws IOException {
        assertThat(mapper.readValue("{\"type\":\"listAlias\"}", UnionTypeExample.class))
                .isEqualTo(UnionTypeExample.listAlias(ListAlias.of(ImmutableList.of())));
    }

    @Test
    public void testUnionType_nullListAliasValue() throws IOException {
        assertThat(mapper.readValue("{\"type\":\"listAlias\",\"listAlias\":null}", UnionTypeExample.class))
                .isEqualTo(UnionTypeExample.listAlias(ListAlias.of(ImmutableList.of())));
    }

    @Test
    public void testUnionType_excludedMapValue() throws IOException {
        assertThat(mapper.readValue("{\"type\":\"map\"}", UnionTypeExample.class))
                .isEqualTo(UnionTypeExample.map(ImmutableMap.of()));
    }

    @Test
    public void testUnionType_nullMapValue() throws IOException {
        assertThat(mapper.readValue("{\"type\":\"map\",\"map\":null}", UnionTypeExample.class))
                .isEqualTo(UnionTypeExample.map(ImmutableMap.of()));
    }

    @Test
    public void testUnionType_excludedMapAliasValue() throws IOException {
        assertThat(mapper.readValue("{\"type\":\"mapAlias\"}", UnionTypeExample.class))
                .isEqualTo(UnionTypeExample.mapAlias(MapAliasExample.of(ImmutableMap.of())));
    }

    @Test
    public void testUnionType_nullMapAliasValue() throws IOException {
        assertThat(mapper.readValue("{\"type\":\"mapAlias\",\"mapAlias\":null}", UnionTypeExample.class))
                .isEqualTo(UnionTypeExample.mapAlias(MapAliasExample.of(ImmutableMap.of())));
    }

    @Test
    public void testDateTime_roundTrip() throws Exception {
        String serialized = "{\"datetime\":\"2017-01-02T03:04:05.000000006Z\"}";
        DateTimeExample deserialized =
                DateTimeExample.of(OffsetDateTime.of(2017, 1, 2, 3, 4, 5, 6, ZoneOffset.of("Z")));
        assertThat(mapper.writeValueAsString(deserialized)).isEqualTo(serialized);
        assertThat(mapper.readValue(serialized, DateTimeExample.class)).isEqualTo(deserialized);
    }

    @Test
    public void testDateTime_with_explicit_zoneId_is_iso_compliant() throws Exception {
        DateTimeExample deserialized =
                DateTimeExample.of(OffsetDateTime.of(2017, 1, 2, 3, 4, 5, 0, ZoneOffset.of("+1")));
        assertThat(mapper.writeValueAsString(deserialized)).isEqualTo("{\"datetime\":\"2017-01-02T03:04:05+01:00\"}");
    }

    @Test
    public void testDateTimeType_acceptFormats() throws Exception {
        DateTimeExample reference = DateTimeExample.of(OffsetDateTime.parse("2017-01-02T03:04:05.000000006Z"));

        assertThat(mapper.readValue("{\"datetime\":\"2017-01-02T03:04:05.000000006Z\"}", DateTimeExample.class))
                .isEqualTo(reference);

        assertThat(mapper.readValue("{\"datetime\":\"2017-01-02T03:04:05.000000006+00:00\"}", DateTimeExample.class))
                .isEqualTo(reference);

        assertThat(mapper.readValue("{\"datetime\":\"2017-01-02T04:04:05.000000006+01:00\"}", DateTimeExample.class))
                .isEqualTo(reference);

        DateTimeExample secondsOnly = DateTimeExample.of(OffsetDateTime.parse("2017-01-02T03:04:05.000000000Z"));

        // seconds
        assertThat(mapper.readValue("{\"datetime\":\"2017-01-02T03:04:05Z\"}", DateTimeExample.class))
                .isEqualTo(secondsOnly);

        // milli
        assertThat(mapper.readValue("{\"datetime\":\"2017-01-02T03:04:05.000Z\"}", DateTimeExample.class))
                .isEqualTo(secondsOnly);

        // micro
        assertThat(mapper.readValue("{\"datetime\":\"2017-01-02T03:04:05.000000Z\"}", DateTimeExample.class))
                .isEqualTo(secondsOnly);
    }

    @Test
    public void testDateTimeType_equality() throws Exception {
        OffsetDateTime aa = OffsetDateTime.parse("2017-01-02T03:04:05.000000006Z");
        OffsetDateTime bb = OffsetDateTime.parse("2017-01-02T03:04:05.000000006+00:00");
        OffsetDateTime cc = OffsetDateTime.parse("2017-01-02T04:04:05.000000006+01:00");

        assertThat(aa.isEqual(bb)).isTrue();
        assertThat(aa.isEqual(cc)).isTrue();
        assertThat(bb.isEqual(cc)).isTrue();

        DateTimeExample one = DateTimeExample.of(OffsetDateTime.parse("2017-01-02T03:04:05.000000006Z"));
        DateTimeExample two = DateTimeExample.of(OffsetDateTime.parse("2017-01-02T04:04:05.000000006+01:00"));

        assertThat(one).isEqualTo(two);
        assertThat(one.hashCode()).isEqualTo(two.hashCode());
    }

    @Test
    public void double_alias_should_serialize_with_decimal_point() throws Exception {
        assertThat(mapper.writeValueAsString(DoubleAliasExample.of(100L))).isEqualTo("100.0");
    }

    @Test
    public void double_alias_should_deserialize_without_decimal_point() throws Exception {
        // frontends can send numbers like this!
        assertThat(mapper.readValue("100", DoubleAliasExample.class)).isEqualTo(DoubleAliasExample.of(100L));
    }

    @Test
    public void testUuidType_roundTrip() throws Exception {
        String serialized = "{\"uuid\":\"0db30881-8f3e-46f4-a8bb-df5883bf7eb8\"}";
        UuidExample deserialized = UuidExample.of(UUID.fromString("0db30881-8f3e-46f4-a8bb-df5883bf7eb8"));

        assertThat(mapper.writeValueAsString(deserialized)).isEqualTo(serialized);
        assertThat(mapper.readValue(serialized, UuidExample.class)).isEqualTo(deserialized);
    }

    @Test
    public void test_long_alias_numeric() throws IOException {
        assertThat(mapper.readValue("123", ExternalLongAliasExample.class))
                .isEqualTo(ExternalLongAliasExample.of(123L));
    }

    @Test
    public void test_long_alias_string() throws IOException {
        assertThat(mapper.readValue("\"123\"", ExternalLongAliasExample.class))
                .isEqualTo(ExternalLongAliasExample.of(123L));
    }

    @Test
    public void test_external_string_alias() throws IOException {
        assertThat(mapper.readValue("\"Hello, World!\"", ExternalStringAliasExample.class))
                .isEqualTo(ExternalStringAliasExample.of("Hello, World!"));
    }

    @Test
    public void testUuidType_equality() throws Exception {
        UUID randomUuid = UUID.randomUUID();
        UuidExample uuidA = UuidExample.of(randomUuid);
        UuidExample uuidB = UuidExample.of(UUID.randomUUID());
        UuidExample uuidC = UuidExample.builder().from(uuidA).build();

        assertThat(uuidA).isEqualTo(uuidC);
        assertThat(uuidA).isNotEqualTo(uuidB);

        assertThat(uuidA.hashCode()).isEqualTo(uuidC.hashCode());
        assertThat(uuidA.hashCode()).isNotEqualTo(uuidB.hashCode());
    }

    @Test
    public void testOptionalAliasExample() throws IOException {
        assertThat(mapper.readValue("{\"optionalAlias\":null}", OptionalAliasExample.class))
                .isEqualTo(OptionalAliasExample.of(OptionalAlias.of(Optional.empty())));
    }

    @Test
    public void testEmptyOptionalFieldSerialization() throws IOException {
        assertThat(mapper.writeValueAsString(PrimitiveOptionalsExample.builder()
                        .aliasTwo(StringAliasTwo.of(Optional.empty()))
                        .aliasOptional(OptionalAlias.of(Optional.empty()))
                        .aliasOptionalMap(OptionalMapAliasExample.of(Optional.empty()))
                        .aliasOptionalList(OptionalListAliasExample.of(Optional.empty()))
                        .aliasOptionalSet(OptionalSetAliasExample.of(Optional.empty()))
                        .build()))
                .isEqualTo("{}");
    }

    @Test
    public void testEmptyOptionalFieldSerialization_nonEmptyValues() throws IOException {
        PrimitiveOptionalsExample original = PrimitiveOptionalsExample.builder()
                .num(OptionalDouble.of(0D))
                .bool(Optional.of(Boolean.FALSE))
                .integer(OptionalInt.of(0))
                .map(ImmutableMap.of())
                .list(ImmutableList.of())
                .set(ImmutableSet.of())
                .aliasOne(StringAliasOne.of(""))
                .aliasTwo(StringAliasTwo.of(Optional.of(StringAliasOne.of(""))))
                .aliasOptional(OptionalAlias.of(Optional.of("")))
                .aliasOptionalMap(OptionalMapAliasExample.of(Optional.of(ImmutableMap.of())))
                .aliasOptionalList(OptionalListAliasExample.of(Optional.of(ImmutableList.of())))
                .aliasOptionalSet(OptionalSetAliasExample.of(Optional.of(ImmutableSet.of())))
                .aliasMap(MapAliasExample.of(ImmutableMap.of()))
                .build();
        String json = mapper.writeValueAsString(original);
        assertThat(mapper.readValue(json, new TypeReference<Map<String, Object>>() {}))
                .as("Unexpected number of fields in '%s'", json)
                .hasSize(13);
        PrimitiveOptionalsExample recreated = mapper.readValue(json, PrimitiveOptionalsExample.class);
        assertThat(recreated)
                .as("Failed to recreate the original object using '%s'", json)
                .isEqualTo(original);
    }

    @Test
    public void testEmptyOptionalFieldSerializationOfAlias() throws IOException {
        assertThat(mapper.writeValueAsString(OptionalAliasExample.builder()
                        .optionalAlias(OptionalAlias.of(Optional.empty()))
                        .build()))
                .isEqualTo("{}");
    }

    @Test
    public void testEmptyOptionalFieldSerializationOfAlias_emptyString() throws IOException {
        assertThat(mapper.writeValueAsString(OptionalAliasExample.builder()
                        .optionalAlias(OptionalAlias.of(Optional.of("")))
                        .build()))
                .isEqualTo("{\"optionalAlias\":\"\"}");
    }

    private static final class TestVisitor implements UnionTypeExample.Visitor<Integer> {
        @Override
        public Integer visitStringExample(StringExample stringExampleValue) {
            return stringExampleValue.getString().length();
        }

        @Override
        public Integer visitSet(Set<String> setStringValue) {
            return setStringValue.size();
        }

        @Override
        public Integer visitAlsoAnInteger(int value) {
            return value;
        }

        @Override
        public Integer visitThisFieldIsAnInteger(int value) {
            return value;
        }

        @Override
        public Integer visitUnknown(String _unknownType, Map<String, Object> _unknownValue) {
            return 0;
        }

        @Override
        public Integer visitNew(int value) {
            return value;
        }

        @Override
        public Integer visitInterface(int value) {
            return value;
        }

        @Override
        public Integer visitCompleted(int value) {
            return value;
        }

        @Override
        public Integer visitUnknown_(int value) {
            return value;
        }

        @Override
        public Integer visitOptional(Optional<String> value) {
            return value.map(String::length).orElse(-1);
        }

        @Override
        public Integer visitList(List<String> value) {
            return value.size();
        }

        @Override
        public Integer visitMap(Map<String, String> value) {
            return value.size();
        }

        @Override
        public Integer visitOptionalAlias(OptionalAlias _value) {
            return -1;
        }

        @Override
        public Integer visitListAlias(ListAlias value) {
            return value.get().size();
        }

        @Override
        public Integer visitSetAlias(SetAlias value) {
            return value.get().size();
        }

        @Override
        public Integer visitMapAlias(MapAliasExample value) {
            return value.get().size();
        }

        @Override
        public Integer visitIf(int value) {
            return value;
        }
    }
}
