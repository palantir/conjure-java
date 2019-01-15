package com.palantir.conjure.verification.server;

import com.palantir.conjure.verification.types.AnyExample;
import com.palantir.conjure.verification.types.BearerTokenAliasExample;
import com.palantir.conjure.verification.types.BearerTokenExample;
import com.palantir.conjure.verification.types.BooleanAliasExample;
import com.palantir.conjure.verification.types.BooleanExample;
import com.palantir.conjure.verification.types.DateTimeAliasExample;
import com.palantir.conjure.verification.types.DateTimeExample;
import com.palantir.conjure.verification.types.DoubleAliasExample;
import com.palantir.conjure.verification.types.DoubleExample;
import com.palantir.conjure.verification.types.EnumExample;
import com.palantir.conjure.verification.types.IntegerAliasExample;
import com.palantir.conjure.verification.types.IntegerExample;
import com.palantir.conjure.verification.types.KebabCaseObjectExample;
import com.palantir.conjure.verification.types.ListAnyAliasExample;
import com.palantir.conjure.verification.types.ListBearerTokenAliasExample;
import com.palantir.conjure.verification.types.ListBinaryAliasExample;
import com.palantir.conjure.verification.types.ListBooleanAliasExample;
import com.palantir.conjure.verification.types.ListDateTimeAliasExample;
import com.palantir.conjure.verification.types.ListDoubleAliasExample;
import com.palantir.conjure.verification.types.ListExample;
import com.palantir.conjure.verification.types.ListIntegerAliasExample;
import com.palantir.conjure.verification.types.ListRidAliasExample;
import com.palantir.conjure.verification.types.ListSafeLongAliasExample;
import com.palantir.conjure.verification.types.ListStringAliasExample;
import com.palantir.conjure.verification.types.ListUuidAliasExample;
import com.palantir.conjure.verification.types.LongFieldNameOptionalExample;
import com.palantir.conjure.verification.types.MapBearerTokenAliasExample;
import com.palantir.conjure.verification.types.MapBinaryAliasExample;
import com.palantir.conjure.verification.types.MapBooleanAliasExample;
import com.palantir.conjure.verification.types.MapDateTimeAliasExample;
import com.palantir.conjure.verification.types.MapDoubleAliasExample;
import com.palantir.conjure.verification.types.MapEnumExampleAlias;
import com.palantir.conjure.verification.types.MapExample;
import com.palantir.conjure.verification.types.MapIntegerAliasExample;
import com.palantir.conjure.verification.types.MapRidAliasExample;
import com.palantir.conjure.verification.types.MapSafeLongAliasExample;
import com.palantir.conjure.verification.types.MapStringAliasExample;
import com.palantir.conjure.verification.types.MapUuidAliasExample;
import com.palantir.conjure.verification.types.OptionalAnyAliasExample;
import com.palantir.conjure.verification.types.OptionalBearerTokenAliasExample;
import com.palantir.conjure.verification.types.OptionalBinaryAliasExample;
import com.palantir.conjure.verification.types.OptionalBooleanAliasExample;
import com.palantir.conjure.verification.types.OptionalBooleanExample;
import com.palantir.conjure.verification.types.OptionalDateTimeAliasExample;
import com.palantir.conjure.verification.types.OptionalDoubleAliasExample;
import com.palantir.conjure.verification.types.OptionalExample;
import com.palantir.conjure.verification.types.OptionalIntegerAliasExample;
import com.palantir.conjure.verification.types.OptionalIntegerExample;
import com.palantir.conjure.verification.types.OptionalRidAliasExample;
import com.palantir.conjure.verification.types.OptionalSafeLongAliasExample;
import com.palantir.conjure.verification.types.OptionalStringAliasExample;
import com.palantir.conjure.verification.types.OptionalUuidAliasExample;
import com.palantir.conjure.verification.types.RawOptionalExample;
import com.palantir.conjure.verification.types.ReferenceAliasExample;
import com.palantir.conjure.verification.types.RidAliasExample;
import com.palantir.conjure.verification.types.RidExample;
import com.palantir.conjure.verification.types.SafeLongAliasExample;
import com.palantir.conjure.verification.types.SafeLongExample;
import com.palantir.conjure.verification.types.SetAnyAliasExample;
import com.palantir.conjure.verification.types.SetBearerTokenAliasExample;
import com.palantir.conjure.verification.types.SetBinaryAliasExample;
import com.palantir.conjure.verification.types.SetBooleanAliasExample;
import com.palantir.conjure.verification.types.SetDateTimeAliasExample;
import com.palantir.conjure.verification.types.SetDoubleAliasExample;
import com.palantir.conjure.verification.types.SetDoubleExample;
import com.palantir.conjure.verification.types.SetIntegerAliasExample;
import com.palantir.conjure.verification.types.SetRidAliasExample;
import com.palantir.conjure.verification.types.SetSafeLongAliasExample;
import com.palantir.conjure.verification.types.SetStringAliasExample;
import com.palantir.conjure.verification.types.SetStringExample;
import com.palantir.conjure.verification.types.SetUuidAliasExample;
import com.palantir.conjure.verification.types.SnakeCaseObjectExample;
import com.palantir.conjure.verification.types.StringAliasExample;
import com.palantir.conjure.verification.types.StringExample;
import com.palantir.conjure.verification.types.UuidAliasExample;
import com.palantir.conjure.verification.types.UuidExample;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowAutoDeserializeService {
    BearerTokenExample receiveBearerTokenExample(int index);

    BooleanExample receiveBooleanExample(int index);

    DateTimeExample receiveDateTimeExample(int index);

    DoubleExample receiveDoubleExample(int index);

    IntegerExample receiveIntegerExample(int index);

    RidExample receiveRidExample(int index);

    SafeLongExample receiveSafeLongExample(int index);

    StringExample receiveStringExample(int index);

    UuidExample receiveUuidExample(int index);

    AnyExample receiveAnyExample(int index);

    EnumExample receiveEnumExample(int index);

    ListExample receiveListExample(int index);

    SetStringExample receiveSetStringExample(int index);

    SetDoubleExample receiveSetDoubleExample(int index);

    MapExample receiveMapExample(int index);

    OptionalExample receiveOptionalExample(int index);

    OptionalBooleanExample receiveOptionalBooleanExample(int index);

    OptionalIntegerExample receiveOptionalIntegerExample(int index);

    LongFieldNameOptionalExample receiveLongFieldNameOptionalExample(int index);

    RawOptionalExample receiveRawOptionalExample(int index);

    StringAliasExample receiveStringAliasExample(int index);

    DoubleAliasExample receiveDoubleAliasExample(int index);

    IntegerAliasExample receiveIntegerAliasExample(int index);

    BooleanAliasExample receiveBooleanAliasExample(int index);

    SafeLongAliasExample receiveSafeLongAliasExample(int index);

    RidAliasExample receiveRidAliasExample(int index);

    BearerTokenAliasExample receiveBearerTokenAliasExample(int index);

    UuidAliasExample receiveUuidAliasExample(int index);

    ReferenceAliasExample receiveReferenceAliasExample(int index);

    DateTimeAliasExample receiveDateTimeAliasExample(int index);

    KebabCaseObjectExample receiveKebabCaseObjectExample(int index);

    SnakeCaseObjectExample receiveSnakeCaseObjectExample(int index);

    OptionalBearerTokenAliasExample receiveOptionalBearerTokenAliasExample(int index);

    OptionalBinaryAliasExample receiveOptionalBinaryAliasExample(int index);

    OptionalBooleanAliasExample receiveOptionalBooleanAliasExample(int index);

    OptionalDateTimeAliasExample receiveOptionalDateTimeAliasExample(int index);

    OptionalDoubleAliasExample receiveOptionalDoubleAliasExample(int index);

    OptionalIntegerAliasExample receiveOptionalIntegerAliasExample(int index);

    OptionalRidAliasExample receiveOptionalRidAliasExample(int index);

    OptionalSafeLongAliasExample receiveOptionalSafeLongAliasExample(int index);

    OptionalStringAliasExample receiveOptionalStringAliasExample(int index);

    OptionalUuidAliasExample receiveOptionalUuidAliasExample(int index);

    OptionalAnyAliasExample receiveOptionalAnyAliasExample(int index);

    ListBearerTokenAliasExample receiveListBearerTokenAliasExample(int index);

    ListBinaryAliasExample receiveListBinaryAliasExample(int index);

    ListBooleanAliasExample receiveListBooleanAliasExample(int index);

    ListDateTimeAliasExample receiveListDateTimeAliasExample(int index);

    ListDoubleAliasExample receiveListDoubleAliasExample(int index);

    ListIntegerAliasExample receiveListIntegerAliasExample(int index);

    ListRidAliasExample receiveListRidAliasExample(int index);

    ListSafeLongAliasExample receiveListSafeLongAliasExample(int index);

    ListStringAliasExample receiveListStringAliasExample(int index);

    ListUuidAliasExample receiveListUuidAliasExample(int index);

    ListAnyAliasExample receiveListAnyAliasExample(int index);

    SetBearerTokenAliasExample receiveSetBearerTokenAliasExample(int index);

    SetBinaryAliasExample receiveSetBinaryAliasExample(int index);

    SetBooleanAliasExample receiveSetBooleanAliasExample(int index);

    SetDateTimeAliasExample receiveSetDateTimeAliasExample(int index);

    SetDoubleAliasExample receiveSetDoubleAliasExample(int index);

    SetIntegerAliasExample receiveSetIntegerAliasExample(int index);

    SetRidAliasExample receiveSetRidAliasExample(int index);

    SetSafeLongAliasExample receiveSetSafeLongAliasExample(int index);

    SetStringAliasExample receiveSetStringAliasExample(int index);

    SetUuidAliasExample receiveSetUuidAliasExample(int index);

    SetAnyAliasExample receiveSetAnyAliasExample(int index);

    MapBearerTokenAliasExample receiveMapBearerTokenAliasExample(int index);

    MapBinaryAliasExample receiveMapBinaryAliasExample(int index);

    MapBooleanAliasExample receiveMapBooleanAliasExample(int index);

    MapDateTimeAliasExample receiveMapDateTimeAliasExample(int index);

    MapDoubleAliasExample receiveMapDoubleAliasExample(int index);

    MapIntegerAliasExample receiveMapIntegerAliasExample(int index);

    MapRidAliasExample receiveMapRidAliasExample(int index);

    MapSafeLongAliasExample receiveMapSafeLongAliasExample(int index);

    MapStringAliasExample receiveMapStringAliasExample(int index);

    MapUuidAliasExample receiveMapUuidAliasExample(int index);

    MapEnumExampleAlias receiveMapEnumExampleAlias(int index);
}
