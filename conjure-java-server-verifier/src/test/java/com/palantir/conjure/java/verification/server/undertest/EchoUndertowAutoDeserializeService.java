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

package com.palantir.conjure.java.verification.server.undertest;

import com.palantir.conjure.java.com.palantir.conjure.verification.client.UndertowAutoDeserializeService;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.AnyExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.BearerTokenAliasExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.BearerTokenExample;
import com.palantir.conjure.java.com.palantir.conjure.verification.types.BinaryAliasExample;
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

/**
 * Simple implementation of {@link UndertowAutoDeserializeService}, mirroring the behavior of a proxy using
 * {@link EchoResourceInvocationHandler}. This implementation is used instead of a proxy, due to difficulties calling
 * default methods on the non-private-accessible interface {@link UndertowAutoDeserializeService#endpoints}.
 */
final class EchoUndertowAutoDeserializeService implements UndertowAutoDeserializeService {
    @Override
    public BearerTokenExample getBearerTokenExample(BearerTokenExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public BinaryExample getBinaryExample(BinaryExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public BooleanExample getBooleanExample(BooleanExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public DateTimeExample getDateTimeExample(DateTimeExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public DoubleExample getDoubleExample(DoubleExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public IntegerExample getIntegerExample(IntegerExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public RidExample getRidExample(RidExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public SafeLongExample getSafeLongExample(SafeLongExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public StringExample getStringExample(StringExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public UuidExample getUuidExample(UuidExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public AnyExample getAnyExample(AnyExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public EnumExample getEnumExample(EnumExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public ListExample getListExample(ListExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public SetStringExample getSetStringExample(SetStringExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public SetDoubleExample getSetDoubleExample(SetDoubleExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public MapExample getMapExample(MapExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public OptionalExample getOptionalExample(OptionalExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public OptionalBooleanExample getOptionalBooleanExample(OptionalBooleanExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public OptionalIntegerExample getOptionalIntegerExample(OptionalIntegerExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public LongFieldNameOptionalExample getLongFieldNameOptionalExample(LongFieldNameOptionalExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public RawOptionalExample getRawOptionalExample(RawOptionalExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public StringAliasExample getStringAliasExample(StringAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public DoubleAliasExample getDoubleAliasExample(DoubleAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public IntegerAliasExample getIntegerAliasExample(IntegerAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public BooleanAliasExample getBooleanAliasExample(BooleanAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public SafeLongAliasExample getSafeLongAliasExample(SafeLongAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public RidAliasExample getRidAliasExample(RidAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public BearerTokenAliasExample getBearerTokenAliasExample(BearerTokenAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public UuidAliasExample getUuidAliasExample(UuidAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public ReferenceAliasExample getReferenceAliasExample(ReferenceAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public DateTimeAliasExample getDateTimeAliasExample(DateTimeAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public BinaryAliasExample getBinaryAliasExample(BinaryAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public KebabCaseObjectExample getKebabCaseObjectExample(KebabCaseObjectExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public SnakeCaseObjectExample getSnakeCaseObjectExample(SnakeCaseObjectExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public OptionalBearerTokenAliasExample getOptionalBearerTokenAliasExample(OptionalBearerTokenAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public OptionalBooleanAliasExample getOptionalBooleanAliasExample(OptionalBooleanAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public OptionalDateTimeAliasExample getOptionalDateTimeAliasExample(OptionalDateTimeAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public OptionalDoubleAliasExample getOptionalDoubleAliasExample(OptionalDoubleAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public OptionalIntegerAliasExample getOptionalIntegerAliasExample(OptionalIntegerAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public OptionalRidAliasExample getOptionalRidAliasExample(OptionalRidAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public OptionalSafeLongAliasExample getOptionalSafeLongAliasExample(OptionalSafeLongAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public OptionalStringAliasExample getOptionalStringAliasExample(OptionalStringAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public OptionalUuidAliasExample getOptionalUuidAliasExample(OptionalUuidAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public OptionalAnyAliasExample getOptionalAnyAliasExample(OptionalAnyAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public ListBearerTokenAliasExample getListBearerTokenAliasExample(ListBearerTokenAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public ListBinaryAliasExample getListBinaryAliasExample(ListBinaryAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public ListBooleanAliasExample getListBooleanAliasExample(ListBooleanAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public ListDateTimeAliasExample getListDateTimeAliasExample(ListDateTimeAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public ListDoubleAliasExample getListDoubleAliasExample(ListDoubleAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public ListIntegerAliasExample getListIntegerAliasExample(ListIntegerAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public ListRidAliasExample getListRidAliasExample(ListRidAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public ListSafeLongAliasExample getListSafeLongAliasExample(ListSafeLongAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public ListStringAliasExample getListStringAliasExample(ListStringAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public ListUuidAliasExample getListUuidAliasExample(ListUuidAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public ListAnyAliasExample getListAnyAliasExample(ListAnyAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public ListOptionalAnyAliasExample getListOptionalAnyAliasExample(ListOptionalAnyAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public SetBearerTokenAliasExample getSetBearerTokenAliasExample(SetBearerTokenAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public SetBinaryAliasExample getSetBinaryAliasExample(SetBinaryAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public SetBooleanAliasExample getSetBooleanAliasExample(SetBooleanAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public SetDateTimeAliasExample getSetDateTimeAliasExample(SetDateTimeAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public SetDoubleAliasExample getSetDoubleAliasExample(SetDoubleAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public SetIntegerAliasExample getSetIntegerAliasExample(SetIntegerAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public SetRidAliasExample getSetRidAliasExample(SetRidAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public SetSafeLongAliasExample getSetSafeLongAliasExample(SetSafeLongAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public SetStringAliasExample getSetStringAliasExample(SetStringAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public SetUuidAliasExample getSetUuidAliasExample(SetUuidAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public SetAnyAliasExample getSetAnyAliasExample(SetAnyAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public SetOptionalAnyAliasExample getSetOptionalAnyAliasExample(SetOptionalAnyAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public MapBearerTokenAliasExample getMapBearerTokenAliasExample(MapBearerTokenAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public MapBinaryAliasExample getMapBinaryAliasExample(MapBinaryAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public MapBooleanAliasExample getMapBooleanAliasExample(MapBooleanAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public MapDateTimeAliasExample getMapDateTimeAliasExample(MapDateTimeAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public MapDoubleAliasExample getMapDoubleAliasExample(MapDoubleAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public MapIntegerAliasExample getMapIntegerAliasExample(MapIntegerAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public MapRidAliasExample getMapRidAliasExample(MapRidAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public MapSafeLongAliasExample getMapSafeLongAliasExample(MapSafeLongAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public MapStringAliasExample getMapStringAliasExample(MapStringAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public MapUuidAliasExample getMapUuidAliasExample(MapUuidAliasExample body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }

    @Override
    public MapEnumExampleAlias getMapEnumExampleAlias(MapEnumExampleAlias body) {
        return com.palantir.logsafe.Preconditions.checkNotNull(body, "Null values are not allowed");
    }
}
