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

package com.palantir.conjure.java.verification.server.undertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.conjure.java.com.palantir.conjure.verification.client.AutoDeserializeService;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.AnyExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.BearerTokenAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.BearerTokenExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.BinaryExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.BooleanAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.BooleanExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.DateTimeAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.DateTimeExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.DoubleAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.DoubleExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.EnumExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.IntegerAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.IntegerExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.KebabCaseObjectExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.ListAnyAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.ListBearerTokenAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.ListBinaryAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.ListBooleanAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.ListDateTimeAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.ListDoubleAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.ListExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.ListIntegerAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.ListOptionalAnyAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.ListRidAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.ListSafeLongAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.ListStringAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.ListUuidAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.LongFieldNameOptionalExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.MapBearerTokenAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.MapBinaryAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.MapBooleanAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.MapDateTimeAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.MapDoubleAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.MapEnumExampleAlias;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.MapExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.MapIntegerAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.MapRidAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.MapSafeLongAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.MapStringAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.MapUuidAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.OptionalAnyAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.OptionalBearerTokenAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.OptionalBooleanAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.OptionalBooleanExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.OptionalDateTimeAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.OptionalDoubleAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.OptionalExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.OptionalIntegerAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.OptionalIntegerExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.OptionalRidAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.OptionalSafeLongAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.OptionalStringAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.OptionalUuidAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.RawOptionalExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.ReferenceAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.RidAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.RidExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.SafeLongAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.SafeLongExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.SetAnyAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.SetBearerTokenAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.SetBinaryAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.SetBooleanAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.SetDateTimeAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.SetDoubleAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.SetDoubleExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.SetIntegerAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.SetOptionalAnyAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.SetRidAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.SetSafeLongAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.SetStringAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.SetStringExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.SetUuidAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.SnakeCaseObjectExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.StringAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.StringExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.UuidAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.UuidExample;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.conjure.java.server.jersey.ConjureJerseyFeature;
import io.dropwizard.core.Application;
import io.dropwizard.core.Configuration;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.FuzzyEnumModule;
import java.io.InputStream;
import javax.ws.rs.core.StreamingOutput;

public final class JerseyServerUnderTestApplication extends Application<Configuration> {

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
        ObjectMapper remotingObjectMapper = ObjectMappers.newServerObjectMapper()
                // needs discoverable subtype resolver for DW polymorphic configuration mechanism
                .setSubtypeResolver(new DiscoverableSubtypeResolver())
                .registerModule(new FuzzyEnumModule());
        bootstrap.setObjectMapper(remotingObjectMapper);
    }

    @Override
    @SuppressWarnings("ProxyNonConstantType")
    public void run(Configuration _configuration, Environment environment) {
        environment.jersey().register(new AutoDeserializeResource());

        // must register ConjureJerseyFeature to map conjure error types.
        environment.jersey().register(ConjureJerseyFeature.INSTANCE);
    }

    private static class AutoDeserializeResource implements AutoDeserializeService {
        @Override
        public BearerTokenExample getBearerTokenExample(BearerTokenExample body) {
            return body;
        }

        @Override
        public BinaryExample getBinaryExample(BinaryExample body) {
            return body;
        }

        @Override
        public BooleanExample getBooleanExample(BooleanExample body) {
            return body;
        }

        @Override
        public DateTimeExample getDateTimeExample(DateTimeExample body) {
            return body;
        }

        @Override
        public DoubleExample getDoubleExample(DoubleExample body) {
            return body;
        }

        @Override
        public IntegerExample getIntegerExample(IntegerExample body) {
            return body;
        }

        @Override
        public RidExample getRidExample(RidExample body) {
            return body;
        }

        @Override
        public SafeLongExample getSafeLongExample(SafeLongExample body) {
            return body;
        }

        @Override
        public StringExample getStringExample(StringExample body) {
            return body;
        }

        @Override
        public UuidExample getUuidExample(UuidExample body) {
            return body;
        }

        @Override
        public AnyExample getAnyExample(AnyExample body) {
            return body;
        }

        @Override
        public EnumExample getEnumExample(EnumExample body) {
            return body;
        }

        @Override
        public ListExample getListExample(ListExample body) {
            return body;
        }

        @Override
        public SetStringExample getSetStringExample(SetStringExample body) {
            return body;
        }

        @Override
        public SetDoubleExample getSetDoubleExample(SetDoubleExample body) {
            return body;
        }

        @Override
        public MapExample getMapExample(MapExample body) {
            return body;
        }

        @Override
        public OptionalExample getOptionalExample(OptionalExample body) {
            return body;
        }

        @Override
        public OptionalBooleanExample getOptionalBooleanExample(OptionalBooleanExample body) {
            return body;
        }

        @Override
        public OptionalIntegerExample getOptionalIntegerExample(OptionalIntegerExample body) {
            return body;
        }

        @Override
        public LongFieldNameOptionalExample getLongFieldNameOptionalExample(LongFieldNameOptionalExample body) {
            return body;
        }

        @Override
        public RawOptionalExample getRawOptionalExample(RawOptionalExample body) {
            return body;
        }

        @Override
        public StringAliasExample getStringAliasExample(StringAliasExample body) {
            return body;
        }

        @Override
        public DoubleAliasExample getDoubleAliasExample(DoubleAliasExample body) {
            return body;
        }

        @Override
        public IntegerAliasExample getIntegerAliasExample(IntegerAliasExample body) {
            return body;
        }

        @Override
        public BooleanAliasExample getBooleanAliasExample(BooleanAliasExample body) {
            return body;
        }

        @Override
        public SafeLongAliasExample getSafeLongAliasExample(SafeLongAliasExample body) {
            return body;
        }

        @Override
        public RidAliasExample getRidAliasExample(RidAliasExample body) {
            return body;
        }

        @Override
        public BearerTokenAliasExample getBearerTokenAliasExample(BearerTokenAliasExample body) {
            return body;
        }

        @Override
        public UuidAliasExample getUuidAliasExample(UuidAliasExample body) {
            return body;
        }

        @Override
        public ReferenceAliasExample getReferenceAliasExample(ReferenceAliasExample body) {
            return body;
        }

        @Override
        public DateTimeAliasExample getDateTimeAliasExample(DateTimeAliasExample body) {
            return body;
        }

        @Override
        public StreamingOutput getBinaryAliasExample(InputStream body) {
            return body::transferTo;
        }

        @Override
        public KebabCaseObjectExample getKebabCaseObjectExample(KebabCaseObjectExample body) {
            return body;
        }

        @Override
        public SnakeCaseObjectExample getSnakeCaseObjectExample(SnakeCaseObjectExample body) {
            return body;
        }

        @Override
        public OptionalBearerTokenAliasExample getOptionalBearerTokenAliasExample(
                OptionalBearerTokenAliasExample body) {
            return body;
        }

        @Override
        public OptionalBooleanAliasExample getOptionalBooleanAliasExample(OptionalBooleanAliasExample body) {
            return body;
        }

        @Override
        public OptionalDateTimeAliasExample getOptionalDateTimeAliasExample(OptionalDateTimeAliasExample body) {
            return body;
        }

        @Override
        public OptionalDoubleAliasExample getOptionalDoubleAliasExample(OptionalDoubleAliasExample body) {
            return body;
        }

        @Override
        public OptionalIntegerAliasExample getOptionalIntegerAliasExample(OptionalIntegerAliasExample body) {
            return body;
        }

        @Override
        public OptionalRidAliasExample getOptionalRidAliasExample(OptionalRidAliasExample body) {
            return body;
        }

        @Override
        public OptionalSafeLongAliasExample getOptionalSafeLongAliasExample(OptionalSafeLongAliasExample body) {
            return body;
        }

        @Override
        public OptionalStringAliasExample getOptionalStringAliasExample(OptionalStringAliasExample body) {
            return body;
        }

        @Override
        public OptionalUuidAliasExample getOptionalUuidAliasExample(OptionalUuidAliasExample body) {
            return body;
        }

        @Override
        public OptionalAnyAliasExample getOptionalAnyAliasExample(OptionalAnyAliasExample body) {
            return body;
        }

        @Override
        public ListBearerTokenAliasExample getListBearerTokenAliasExample(ListBearerTokenAliasExample body) {
            return body;
        }

        @Override
        public ListBinaryAliasExample getListBinaryAliasExample(ListBinaryAliasExample body) {
            return body;
        }

        @Override
        public ListBooleanAliasExample getListBooleanAliasExample(ListBooleanAliasExample body) {
            return body;
        }

        @Override
        public ListDateTimeAliasExample getListDateTimeAliasExample(ListDateTimeAliasExample body) {
            return body;
        }

        @Override
        public ListDoubleAliasExample getListDoubleAliasExample(ListDoubleAliasExample body) {
            return body;
        }

        @Override
        public ListIntegerAliasExample getListIntegerAliasExample(ListIntegerAliasExample body) {
            return body;
        }

        @Override
        public ListRidAliasExample getListRidAliasExample(ListRidAliasExample body) {
            return body;
        }

        @Override
        public ListSafeLongAliasExample getListSafeLongAliasExample(ListSafeLongAliasExample body) {
            return body;
        }

        @Override
        public ListStringAliasExample getListStringAliasExample(ListStringAliasExample body) {
            return body;
        }

        @Override
        public ListUuidAliasExample getListUuidAliasExample(ListUuidAliasExample body) {
            return body;
        }

        @Override
        public ListAnyAliasExample getListAnyAliasExample(ListAnyAliasExample body) {
            return body;
        }

        @Override
        public ListOptionalAnyAliasExample getListOptionalAnyAliasExample(ListOptionalAnyAliasExample body) {
            return body;
        }

        @Override
        public SetBearerTokenAliasExample getSetBearerTokenAliasExample(SetBearerTokenAliasExample body) {
            return body;
        }

        @Override
        public SetBinaryAliasExample getSetBinaryAliasExample(SetBinaryAliasExample body) {
            return body;
        }

        @Override
        public SetBooleanAliasExample getSetBooleanAliasExample(SetBooleanAliasExample body) {
            return body;
        }

        @Override
        public SetDateTimeAliasExample getSetDateTimeAliasExample(SetDateTimeAliasExample body) {
            return body;
        }

        @Override
        public SetDoubleAliasExample getSetDoubleAliasExample(SetDoubleAliasExample body) {
            return body;
        }

        @Override
        public SetIntegerAliasExample getSetIntegerAliasExample(SetIntegerAliasExample body) {
            return body;
        }

        @Override
        public SetRidAliasExample getSetRidAliasExample(SetRidAliasExample body) {
            return body;
        }

        @Override
        public SetSafeLongAliasExample getSetSafeLongAliasExample(SetSafeLongAliasExample body) {
            return body;
        }

        @Override
        public SetStringAliasExample getSetStringAliasExample(SetStringAliasExample body) {
            return body;
        }

        @Override
        public SetUuidAliasExample getSetUuidAliasExample(SetUuidAliasExample body) {
            return body;
        }

        @Override
        public SetAnyAliasExample getSetAnyAliasExample(SetAnyAliasExample body) {
            return body;
        }

        @Override
        public SetOptionalAnyAliasExample getSetOptionalAnyAliasExample(SetOptionalAnyAliasExample body) {
            return body;
        }

        @Override
        public MapBearerTokenAliasExample getMapBearerTokenAliasExample(MapBearerTokenAliasExample body) {
            return body;
        }

        @Override
        public MapBinaryAliasExample getMapBinaryAliasExample(MapBinaryAliasExample body) {
            return body;
        }

        @Override
        public MapBooleanAliasExample getMapBooleanAliasExample(MapBooleanAliasExample body) {
            return body;
        }

        @Override
        public MapDateTimeAliasExample getMapDateTimeAliasExample(MapDateTimeAliasExample body) {
            return body;
        }

        @Override
        public MapDoubleAliasExample getMapDoubleAliasExample(MapDoubleAliasExample body) {
            return body;
        }

        @Override
        public MapIntegerAliasExample getMapIntegerAliasExample(MapIntegerAliasExample body) {
            return body;
        }

        @Override
        public MapRidAliasExample getMapRidAliasExample(MapRidAliasExample body) {
            return body;
        }

        @Override
        public MapSafeLongAliasExample getMapSafeLongAliasExample(MapSafeLongAliasExample body) {
            return body;
        }

        @Override
        public MapStringAliasExample getMapStringAliasExample(MapStringAliasExample body) {
            return body;
        }

        @Override
        public MapUuidAliasExample getMapUuidAliasExample(MapUuidAliasExample body) {
            return body;
        }

        @Override
        public MapEnumExampleAlias getMapEnumExampleAlias(MapEnumExampleAlias body) {
            return body;
        }
    }
}
