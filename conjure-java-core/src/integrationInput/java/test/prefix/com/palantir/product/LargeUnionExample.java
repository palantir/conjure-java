package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
public final class LargeUnionExample {
    private final Base value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    private LargeUnionExample(Base value) {
        this.value = value;
    }

    @JsonValue
    private Base getValue() {
        return value;
    }

    public static LargeUnionExample u0(String value) {
        return new LargeUnionExample(new U0Wrapper(value));
    }

    public static LargeUnionExample u1(String value) {
        return new LargeUnionExample(new U1Wrapper(value));
    }

    public static LargeUnionExample u2(String value) {
        return new LargeUnionExample(new U2Wrapper(value));
    }

    public static LargeUnionExample u3(String value) {
        return new LargeUnionExample(new U3Wrapper(value));
    }

    public static LargeUnionExample u4(String value) {
        return new LargeUnionExample(new U4Wrapper(value));
    }

    public static LargeUnionExample u5(String value) {
        return new LargeUnionExample(new U5Wrapper(value));
    }

    public static LargeUnionExample u6(String value) {
        return new LargeUnionExample(new U6Wrapper(value));
    }

    public static LargeUnionExample u7(String value) {
        return new LargeUnionExample(new U7Wrapper(value));
    }

    public static LargeUnionExample u8(String value) {
        return new LargeUnionExample(new U8Wrapper(value));
    }

    public static LargeUnionExample u9(String value) {
        return new LargeUnionExample(new U9Wrapper(value));
    }

    public static LargeUnionExample u10(String value) {
        return new LargeUnionExample(new U10Wrapper(value));
    }

    public static LargeUnionExample u11(String value) {
        return new LargeUnionExample(new U11Wrapper(value));
    }

    public static LargeUnionExample u12(String value) {
        return new LargeUnionExample(new U12Wrapper(value));
    }

    public static LargeUnionExample u13(String value) {
        return new LargeUnionExample(new U13Wrapper(value));
    }

    public static LargeUnionExample u14(String value) {
        return new LargeUnionExample(new U14Wrapper(value));
    }

    public static LargeUnionExample u15(String value) {
        return new LargeUnionExample(new U15Wrapper(value));
    }

    public static LargeUnionExample u16(String value) {
        return new LargeUnionExample(new U16Wrapper(value));
    }

    public static LargeUnionExample u17(String value) {
        return new LargeUnionExample(new U17Wrapper(value));
    }

    public static LargeUnionExample u18(String value) {
        return new LargeUnionExample(new U18Wrapper(value));
    }

    public static LargeUnionExample u19(String value) {
        return new LargeUnionExample(new U19Wrapper(value));
    }

    public static LargeUnionExample u20(String value) {
        return new LargeUnionExample(new U20Wrapper(value));
    }

    public static LargeUnionExample u21(String value) {
        return new LargeUnionExample(new U21Wrapper(value));
    }

    public static LargeUnionExample u22(String value) {
        return new LargeUnionExample(new U22Wrapper(value));
    }

    public static LargeUnionExample u23(String value) {
        return new LargeUnionExample(new U23Wrapper(value));
    }

    public static LargeUnionExample u24(String value) {
        return new LargeUnionExample(new U24Wrapper(value));
    }

    public static LargeUnionExample u25(String value) {
        return new LargeUnionExample(new U25Wrapper(value));
    }

    public static LargeUnionExample u26(String value) {
        return new LargeUnionExample(new U26Wrapper(value));
    }

    public static LargeUnionExample u27(String value) {
        return new LargeUnionExample(new U27Wrapper(value));
    }

    public static LargeUnionExample u28(String value) {
        return new LargeUnionExample(new U28Wrapper(value));
    }

    public static LargeUnionExample u29(String value) {
        return new LargeUnionExample(new U29Wrapper(value));
    }

    public static LargeUnionExample u30(String value) {
        return new LargeUnionExample(new U30Wrapper(value));
    }

    public static LargeUnionExample u31(String value) {
        return new LargeUnionExample(new U31Wrapper(value));
    }

    public static LargeUnionExample u32(String value) {
        return new LargeUnionExample(new U32Wrapper(value));
    }

    public static LargeUnionExample u33(String value) {
        return new LargeUnionExample(new U33Wrapper(value));
    }

    public static LargeUnionExample u34(String value) {
        return new LargeUnionExample(new U34Wrapper(value));
    }

    public static LargeUnionExample u35(String value) {
        return new LargeUnionExample(new U35Wrapper(value));
    }

    public static LargeUnionExample u36(String value) {
        return new LargeUnionExample(new U36Wrapper(value));
    }

    public static LargeUnionExample u37(String value) {
        return new LargeUnionExample(new U37Wrapper(value));
    }

    public static LargeUnionExample u38(String value) {
        return new LargeUnionExample(new U38Wrapper(value));
    }

    public static LargeUnionExample u39(String value) {
        return new LargeUnionExample(new U39Wrapper(value));
    }

    public static LargeUnionExample u40(String value) {
        return new LargeUnionExample(new U40Wrapper(value));
    }

    public static LargeUnionExample u41(String value) {
        return new LargeUnionExample(new U41Wrapper(value));
    }

    public static LargeUnionExample u42(String value) {
        return new LargeUnionExample(new U42Wrapper(value));
    }

    public static LargeUnionExample u43(String value) {
        return new LargeUnionExample(new U43Wrapper(value));
    }

    public static LargeUnionExample u44(String value) {
        return new LargeUnionExample(new U44Wrapper(value));
    }

    public static LargeUnionExample u45(String value) {
        return new LargeUnionExample(new U45Wrapper(value));
    }

    public static LargeUnionExample u46(String value) {
        return new LargeUnionExample(new U46Wrapper(value));
    }

    public static LargeUnionExample u47(String value) {
        return new LargeUnionExample(new U47Wrapper(value));
    }

    public static LargeUnionExample u48(String value) {
        return new LargeUnionExample(new U48Wrapper(value));
    }

    public static LargeUnionExample u49(String value) {
        return new LargeUnionExample(new U49Wrapper(value));
    }

    public static LargeUnionExample u50(String value) {
        return new LargeUnionExample(new U50Wrapper(value));
    }

    public static LargeUnionExample u51(String value) {
        return new LargeUnionExample(new U51Wrapper(value));
    }

    public static LargeUnionExample u52(String value) {
        return new LargeUnionExample(new U52Wrapper(value));
    }

    public static LargeUnionExample u53(String value) {
        return new LargeUnionExample(new U53Wrapper(value));
    }

    public static LargeUnionExample u54(String value) {
        return new LargeUnionExample(new U54Wrapper(value));
    }

    public static LargeUnionExample u55(String value) {
        return new LargeUnionExample(new U55Wrapper(value));
    }

    public static LargeUnionExample u56(String value) {
        return new LargeUnionExample(new U56Wrapper(value));
    }

    public static LargeUnionExample u57(String value) {
        return new LargeUnionExample(new U57Wrapper(value));
    }

    public static LargeUnionExample u58(String value) {
        return new LargeUnionExample(new U58Wrapper(value));
    }

    public static LargeUnionExample u59(String value) {
        return new LargeUnionExample(new U59Wrapper(value));
    }

    public static LargeUnionExample u60(String value) {
        return new LargeUnionExample(new U60Wrapper(value));
    }

    public static LargeUnionExample u61(String value) {
        return new LargeUnionExample(new U61Wrapper(value));
    }

    public static LargeUnionExample u62(String value) {
        return new LargeUnionExample(new U62Wrapper(value));
    }

    public static LargeUnionExample u63(String value) {
        return new LargeUnionExample(new U63Wrapper(value));
    }

    public static LargeUnionExample u64(String value) {
        return new LargeUnionExample(new U64Wrapper(value));
    }

    public static LargeUnionExample u65(String value) {
        return new LargeUnionExample(new U65Wrapper(value));
    }

    public static LargeUnionExample u66(String value) {
        return new LargeUnionExample(new U66Wrapper(value));
    }

    public static LargeUnionExample u67(String value) {
        return new LargeUnionExample(new U67Wrapper(value));
    }

    public static LargeUnionExample u68(String value) {
        return new LargeUnionExample(new U68Wrapper(value));
    }

    public static LargeUnionExample u69(String value) {
        return new LargeUnionExample(new U69Wrapper(value));
    }

    public static LargeUnionExample u70(String value) {
        return new LargeUnionExample(new U70Wrapper(value));
    }

    public static LargeUnionExample u71(String value) {
        return new LargeUnionExample(new U71Wrapper(value));
    }

    public static LargeUnionExample u72(String value) {
        return new LargeUnionExample(new U72Wrapper(value));
    }

    public static LargeUnionExample u73(String value) {
        return new LargeUnionExample(new U73Wrapper(value));
    }

    public static LargeUnionExample u74(String value) {
        return new LargeUnionExample(new U74Wrapper(value));
    }

    public static LargeUnionExample u75(String value) {
        return new LargeUnionExample(new U75Wrapper(value));
    }

    public static LargeUnionExample u76(String value) {
        return new LargeUnionExample(new U76Wrapper(value));
    }

    public static LargeUnionExample u77(String value) {
        return new LargeUnionExample(new U77Wrapper(value));
    }

    public static LargeUnionExample u78(String value) {
        return new LargeUnionExample(new U78Wrapper(value));
    }

    public static LargeUnionExample u79(String value) {
        return new LargeUnionExample(new U79Wrapper(value));
    }

    public static LargeUnionExample u80(String value) {
        return new LargeUnionExample(new U80Wrapper(value));
    }

    public static LargeUnionExample u81(String value) {
        return new LargeUnionExample(new U81Wrapper(value));
    }

    public static LargeUnionExample u82(String value) {
        return new LargeUnionExample(new U82Wrapper(value));
    }

    public static LargeUnionExample u83(String value) {
        return new LargeUnionExample(new U83Wrapper(value));
    }

    public static LargeUnionExample u84(String value) {
        return new LargeUnionExample(new U84Wrapper(value));
    }

    public static LargeUnionExample u85(String value) {
        return new LargeUnionExample(new U85Wrapper(value));
    }

    public static LargeUnionExample u86(String value) {
        return new LargeUnionExample(new U86Wrapper(value));
    }

    public static LargeUnionExample u87(String value) {
        return new LargeUnionExample(new U87Wrapper(value));
    }

    public static LargeUnionExample u88(String value) {
        return new LargeUnionExample(new U88Wrapper(value));
    }

    public static LargeUnionExample u89(String value) {
        return new LargeUnionExample(new U89Wrapper(value));
    }

    public static LargeUnionExample u90(String value) {
        return new LargeUnionExample(new U90Wrapper(value));
    }

    public static LargeUnionExample u91(String value) {
        return new LargeUnionExample(new U91Wrapper(value));
    }

    public static LargeUnionExample u92(String value) {
        return new LargeUnionExample(new U92Wrapper(value));
    }

    public static LargeUnionExample u93(String value) {
        return new LargeUnionExample(new U93Wrapper(value));
    }

    public static LargeUnionExample u94(String value) {
        return new LargeUnionExample(new U94Wrapper(value));
    }

    public static LargeUnionExample u95(String value) {
        return new LargeUnionExample(new U95Wrapper(value));
    }

    public static LargeUnionExample u96(String value) {
        return new LargeUnionExample(new U96Wrapper(value));
    }

    public static LargeUnionExample u97(String value) {
        return new LargeUnionExample(new U97Wrapper(value));
    }

    public static LargeUnionExample u98(String value) {
        return new LargeUnionExample(new U98Wrapper(value));
    }

    public static LargeUnionExample u99(String value) {
        return new LargeUnionExample(new U99Wrapper(value));
    }

    public static LargeUnionExample u100(String value) {
        return new LargeUnionExample(new U100Wrapper(value));
    }

    public static LargeUnionExample unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "u0":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u0");
            case "u1":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u1");
            case "u2":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u2");
            case "u3":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u3");
            case "u4":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u4");
            case "u5":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u5");
            case "u6":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u6");
            case "u7":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u7");
            case "u8":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u8");
            case "u9":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u9");
            case "u10":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u10");
            case "u11":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u11");
            case "u12":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u12");
            case "u13":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u13");
            case "u14":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u14");
            case "u15":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u15");
            case "u16":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u16");
            case "u17":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u17");
            case "u18":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u18");
            case "u19":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u19");
            case "u20":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u20");
            case "u21":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u21");
            case "u22":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u22");
            case "u23":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u23");
            case "u24":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u24");
            case "u25":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u25");
            case "u26":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u26");
            case "u27":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u27");
            case "u28":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u28");
            case "u29":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u29");
            case "u30":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u30");
            case "u31":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u31");
            case "u32":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u32");
            case "u33":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u33");
            case "u34":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u34");
            case "u35":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u35");
            case "u36":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u36");
            case "u37":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u37");
            case "u38":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u38");
            case "u39":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u39");
            case "u40":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u40");
            case "u41":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u41");
            case "u42":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u42");
            case "u43":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u43");
            case "u44":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u44");
            case "u45":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u45");
            case "u46":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u46");
            case "u47":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u47");
            case "u48":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u48");
            case "u49":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u49");
            case "u50":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u50");
            case "u51":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u51");
            case "u52":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u52");
            case "u53":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u53");
            case "u54":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u54");
            case "u55":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u55");
            case "u56":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u56");
            case "u57":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u57");
            case "u58":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u58");
            case "u59":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u59");
            case "u60":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u60");
            case "u61":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u61");
            case "u62":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u62");
            case "u63":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u63");
            case "u64":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u64");
            case "u65":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u65");
            case "u66":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u66");
            case "u67":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u67");
            case "u68":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u68");
            case "u69":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u69");
            case "u70":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u70");
            case "u71":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u71");
            case "u72":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u72");
            case "u73":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u73");
            case "u74":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u74");
            case "u75":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u75");
            case "u76":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u76");
            case "u77":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u77");
            case "u78":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u78");
            case "u79":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u79");
            case "u80":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u80");
            case "u81":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u81");
            case "u82":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u82");
            case "u83":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u83");
            case "u84":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u84");
            case "u85":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u85");
            case "u86":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u86");
            case "u87":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u87");
            case "u88":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u88");
            case "u89":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u89");
            case "u90":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u90");
            case "u91":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u91");
            case "u92":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u92");
            case "u93":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u93");
            case "u94":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u94");
            case "u95":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u95");
            case "u96":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u96");
            case "u97":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u97");
            case "u98":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u98");
            case "u99":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u99");
            case "u100":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: u100");
            default:
                return new LargeUnionExample(new UnknownWrapper(type, Collections.singletonMap(type, value)));
        }
    }

    public <T> T accept(Visitor<T> visitor) {
        return value.accept(visitor);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof LargeUnionExample && equalTo((LargeUnionExample) other));
    }

    private boolean equalTo(LargeUnionExample other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public String toString() {
        return "LargeUnionExample{value: " + value + '}';
    }

    public interface Visitor<T> {
        T visitU0(String value);

        T visitU1(String value);

        T visitU2(String value);

        T visitU3(String value);

        T visitU4(String value);

        T visitU5(String value);

        T visitU6(String value);

        T visitU7(String value);

        T visitU8(String value);

        T visitU9(String value);

        T visitU10(String value);

        T visitU11(String value);

        T visitU12(String value);

        T visitU13(String value);

        T visitU14(String value);

        T visitU15(String value);

        T visitU16(String value);

        T visitU17(String value);

        T visitU18(String value);

        T visitU19(String value);

        T visitU20(String value);

        T visitU21(String value);

        T visitU22(String value);

        T visitU23(String value);

        T visitU24(String value);

        T visitU25(String value);

        T visitU26(String value);

        T visitU27(String value);

        T visitU28(String value);

        T visitU29(String value);

        T visitU30(String value);

        T visitU31(String value);

        T visitU32(String value);

        T visitU33(String value);

        T visitU34(String value);

        T visitU35(String value);

        T visitU36(String value);

        T visitU37(String value);

        T visitU38(String value);

        T visitU39(String value);

        T visitU40(String value);

        T visitU41(String value);

        T visitU42(String value);

        T visitU43(String value);

        T visitU44(String value);

        T visitU45(String value);

        T visitU46(String value);

        T visitU47(String value);

        T visitU48(String value);

        T visitU49(String value);

        T visitU50(String value);

        T visitU51(String value);

        T visitU52(String value);

        T visitU53(String value);

        T visitU54(String value);

        T visitU55(String value);

        T visitU56(String value);

        T visitU57(String value);

        T visitU58(String value);

        T visitU59(String value);

        T visitU60(String value);

        T visitU61(String value);

        T visitU62(String value);

        T visitU63(String value);

        T visitU64(String value);

        T visitU65(String value);

        T visitU66(String value);

        T visitU67(String value);

        T visitU68(String value);

        T visitU69(String value);

        T visitU70(String value);

        T visitU71(String value);

        T visitU72(String value);

        T visitU73(String value);

        T visitU74(String value);

        T visitU75(String value);

        T visitU76(String value);

        T visitU77(String value);

        T visitU78(String value);

        T visitU79(String value);

        T visitU80(String value);

        T visitU81(String value);

        T visitU82(String value);

        T visitU83(String value);

        T visitU84(String value);

        T visitU85(String value);

        T visitU86(String value);

        T visitU87(String value);

        T visitU88(String value);

        T visitU89(String value);

        T visitU90(String value);

        T visitU91(String value);

        T visitU92(String value);

        T visitU93(String value);

        T visitU94(String value);

        T visitU95(String value);

        T visitU96(String value);

        T visitU97(String value);

        T visitU98(String value);

        T visitU99(String value);

        T visitU100(String value);

        T visitUnknown(@Safe String unknownType);
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXISTING_PROPERTY,
            property = "type",
            visible = true,
            defaultImpl = UnknownWrapper.class)
    @JsonSubTypes({
        @JsonSubTypes.Type(U0Wrapper.class),
        @JsonSubTypes.Type(U1Wrapper.class),
        @JsonSubTypes.Type(U2Wrapper.class),
        @JsonSubTypes.Type(U3Wrapper.class),
        @JsonSubTypes.Type(U4Wrapper.class),
        @JsonSubTypes.Type(U5Wrapper.class),
        @JsonSubTypes.Type(U6Wrapper.class),
        @JsonSubTypes.Type(U7Wrapper.class),
        @JsonSubTypes.Type(U8Wrapper.class),
        @JsonSubTypes.Type(U9Wrapper.class),
        @JsonSubTypes.Type(U10Wrapper.class),
        @JsonSubTypes.Type(U11Wrapper.class),
        @JsonSubTypes.Type(U12Wrapper.class),
        @JsonSubTypes.Type(U13Wrapper.class),
        @JsonSubTypes.Type(U14Wrapper.class),
        @JsonSubTypes.Type(U15Wrapper.class),
        @JsonSubTypes.Type(U16Wrapper.class),
        @JsonSubTypes.Type(U17Wrapper.class),
        @JsonSubTypes.Type(U18Wrapper.class),
        @JsonSubTypes.Type(U19Wrapper.class),
        @JsonSubTypes.Type(U20Wrapper.class),
        @JsonSubTypes.Type(U21Wrapper.class),
        @JsonSubTypes.Type(U22Wrapper.class),
        @JsonSubTypes.Type(U23Wrapper.class),
        @JsonSubTypes.Type(U24Wrapper.class),
        @JsonSubTypes.Type(U25Wrapper.class),
        @JsonSubTypes.Type(U26Wrapper.class),
        @JsonSubTypes.Type(U27Wrapper.class),
        @JsonSubTypes.Type(U28Wrapper.class),
        @JsonSubTypes.Type(U29Wrapper.class),
        @JsonSubTypes.Type(U30Wrapper.class),
        @JsonSubTypes.Type(U31Wrapper.class),
        @JsonSubTypes.Type(U32Wrapper.class),
        @JsonSubTypes.Type(U33Wrapper.class),
        @JsonSubTypes.Type(U34Wrapper.class),
        @JsonSubTypes.Type(U35Wrapper.class),
        @JsonSubTypes.Type(U36Wrapper.class),
        @JsonSubTypes.Type(U37Wrapper.class),
        @JsonSubTypes.Type(U38Wrapper.class),
        @JsonSubTypes.Type(U39Wrapper.class),
        @JsonSubTypes.Type(U40Wrapper.class),
        @JsonSubTypes.Type(U41Wrapper.class),
        @JsonSubTypes.Type(U42Wrapper.class),
        @JsonSubTypes.Type(U43Wrapper.class),
        @JsonSubTypes.Type(U44Wrapper.class),
        @JsonSubTypes.Type(U45Wrapper.class),
        @JsonSubTypes.Type(U46Wrapper.class),
        @JsonSubTypes.Type(U47Wrapper.class),
        @JsonSubTypes.Type(U48Wrapper.class),
        @JsonSubTypes.Type(U49Wrapper.class),
        @JsonSubTypes.Type(U50Wrapper.class),
        @JsonSubTypes.Type(U51Wrapper.class),
        @JsonSubTypes.Type(U52Wrapper.class),
        @JsonSubTypes.Type(U53Wrapper.class),
        @JsonSubTypes.Type(U54Wrapper.class),
        @JsonSubTypes.Type(U55Wrapper.class),
        @JsonSubTypes.Type(U56Wrapper.class),
        @JsonSubTypes.Type(U57Wrapper.class),
        @JsonSubTypes.Type(U58Wrapper.class),
        @JsonSubTypes.Type(U59Wrapper.class),
        @JsonSubTypes.Type(U60Wrapper.class),
        @JsonSubTypes.Type(U61Wrapper.class),
        @JsonSubTypes.Type(U62Wrapper.class),
        @JsonSubTypes.Type(U63Wrapper.class),
        @JsonSubTypes.Type(U64Wrapper.class),
        @JsonSubTypes.Type(U65Wrapper.class),
        @JsonSubTypes.Type(U66Wrapper.class),
        @JsonSubTypes.Type(U67Wrapper.class),
        @JsonSubTypes.Type(U68Wrapper.class),
        @JsonSubTypes.Type(U69Wrapper.class),
        @JsonSubTypes.Type(U70Wrapper.class),
        @JsonSubTypes.Type(U71Wrapper.class),
        @JsonSubTypes.Type(U72Wrapper.class),
        @JsonSubTypes.Type(U73Wrapper.class),
        @JsonSubTypes.Type(U74Wrapper.class),
        @JsonSubTypes.Type(U75Wrapper.class),
        @JsonSubTypes.Type(U76Wrapper.class),
        @JsonSubTypes.Type(U77Wrapper.class),
        @JsonSubTypes.Type(U78Wrapper.class),
        @JsonSubTypes.Type(U79Wrapper.class),
        @JsonSubTypes.Type(U80Wrapper.class),
        @JsonSubTypes.Type(U81Wrapper.class),
        @JsonSubTypes.Type(U82Wrapper.class),
        @JsonSubTypes.Type(U83Wrapper.class),
        @JsonSubTypes.Type(U84Wrapper.class),
        @JsonSubTypes.Type(U85Wrapper.class),
        @JsonSubTypes.Type(U86Wrapper.class),
        @JsonSubTypes.Type(U87Wrapper.class),
        @JsonSubTypes.Type(U88Wrapper.class),
        @JsonSubTypes.Type(U89Wrapper.class),
        @JsonSubTypes.Type(U90Wrapper.class),
        @JsonSubTypes.Type(U91Wrapper.class),
        @JsonSubTypes.Type(U92Wrapper.class),
        @JsonSubTypes.Type(U93Wrapper.class),
        @JsonSubTypes.Type(U94Wrapper.class),
        @JsonSubTypes.Type(U95Wrapper.class),
        @JsonSubTypes.Type(U96Wrapper.class),
        @JsonSubTypes.Type(U97Wrapper.class),
        @JsonSubTypes.Type(U98Wrapper.class),
        @JsonSubTypes.Type(U99Wrapper.class),
        @JsonSubTypes.Type(U100Wrapper.class)
    })
    @JsonIgnoreProperties(ignoreUnknown = true)
    private interface Base {
        <T> T accept(Visitor<T> visitor);
    }

    @JsonTypeName("u0")
    private static final class U0Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U0Wrapper(@JsonSetter("u0") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u0 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u0";
        }

        @JsonProperty("u0")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU0(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U0Wrapper && equalTo((U0Wrapper) other));
        }

        private boolean equalTo(U0Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U0Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u1")
    private static final class U1Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U1Wrapper(@JsonSetter("u1") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u1 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u1";
        }

        @JsonProperty("u1")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU1(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U1Wrapper && equalTo((U1Wrapper) other));
        }

        private boolean equalTo(U1Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U1Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u2")
    private static final class U2Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U2Wrapper(@JsonSetter("u2") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u2 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u2";
        }

        @JsonProperty("u2")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU2(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U2Wrapper && equalTo((U2Wrapper) other));
        }

        private boolean equalTo(U2Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U2Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u3")
    private static final class U3Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U3Wrapper(@JsonSetter("u3") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u3 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u3";
        }

        @JsonProperty("u3")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU3(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U3Wrapper && equalTo((U3Wrapper) other));
        }

        private boolean equalTo(U3Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U3Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u4")
    private static final class U4Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U4Wrapper(@JsonSetter("u4") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u4 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u4";
        }

        @JsonProperty("u4")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU4(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U4Wrapper && equalTo((U4Wrapper) other));
        }

        private boolean equalTo(U4Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U4Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u5")
    private static final class U5Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U5Wrapper(@JsonSetter("u5") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u5 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u5";
        }

        @JsonProperty("u5")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU5(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U5Wrapper && equalTo((U5Wrapper) other));
        }

        private boolean equalTo(U5Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U5Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u6")
    private static final class U6Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U6Wrapper(@JsonSetter("u6") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u6 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u6";
        }

        @JsonProperty("u6")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU6(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U6Wrapper && equalTo((U6Wrapper) other));
        }

        private boolean equalTo(U6Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U6Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u7")
    private static final class U7Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U7Wrapper(@JsonSetter("u7") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u7 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u7";
        }

        @JsonProperty("u7")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU7(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U7Wrapper && equalTo((U7Wrapper) other));
        }

        private boolean equalTo(U7Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U7Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u8")
    private static final class U8Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U8Wrapper(@JsonSetter("u8") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u8 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u8";
        }

        @JsonProperty("u8")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU8(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U8Wrapper && equalTo((U8Wrapper) other));
        }

        private boolean equalTo(U8Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U8Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u9")
    private static final class U9Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U9Wrapper(@JsonSetter("u9") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u9 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u9";
        }

        @JsonProperty("u9")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU9(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U9Wrapper && equalTo((U9Wrapper) other));
        }

        private boolean equalTo(U9Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U9Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u10")
    private static final class U10Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U10Wrapper(@JsonSetter("u10") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u10 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u10";
        }

        @JsonProperty("u10")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU10(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U10Wrapper && equalTo((U10Wrapper) other));
        }

        private boolean equalTo(U10Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U10Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u11")
    private static final class U11Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U11Wrapper(@JsonSetter("u11") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u11 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u11";
        }

        @JsonProperty("u11")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU11(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U11Wrapper && equalTo((U11Wrapper) other));
        }

        private boolean equalTo(U11Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U11Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u12")
    private static final class U12Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U12Wrapper(@JsonSetter("u12") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u12 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u12";
        }

        @JsonProperty("u12")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU12(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U12Wrapper && equalTo((U12Wrapper) other));
        }

        private boolean equalTo(U12Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U12Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u13")
    private static final class U13Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U13Wrapper(@JsonSetter("u13") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u13 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u13";
        }

        @JsonProperty("u13")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU13(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U13Wrapper && equalTo((U13Wrapper) other));
        }

        private boolean equalTo(U13Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U13Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u14")
    private static final class U14Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U14Wrapper(@JsonSetter("u14") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u14 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u14";
        }

        @JsonProperty("u14")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU14(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U14Wrapper && equalTo((U14Wrapper) other));
        }

        private boolean equalTo(U14Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U14Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u15")
    private static final class U15Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U15Wrapper(@JsonSetter("u15") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u15 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u15";
        }

        @JsonProperty("u15")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU15(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U15Wrapper && equalTo((U15Wrapper) other));
        }

        private boolean equalTo(U15Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U15Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u16")
    private static final class U16Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U16Wrapper(@JsonSetter("u16") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u16 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u16";
        }

        @JsonProperty("u16")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU16(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U16Wrapper && equalTo((U16Wrapper) other));
        }

        private boolean equalTo(U16Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U16Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u17")
    private static final class U17Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U17Wrapper(@JsonSetter("u17") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u17 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u17";
        }

        @JsonProperty("u17")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU17(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U17Wrapper && equalTo((U17Wrapper) other));
        }

        private boolean equalTo(U17Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U17Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u18")
    private static final class U18Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U18Wrapper(@JsonSetter("u18") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u18 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u18";
        }

        @JsonProperty("u18")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU18(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U18Wrapper && equalTo((U18Wrapper) other));
        }

        private boolean equalTo(U18Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U18Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u19")
    private static final class U19Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U19Wrapper(@JsonSetter("u19") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u19 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u19";
        }

        @JsonProperty("u19")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU19(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U19Wrapper && equalTo((U19Wrapper) other));
        }

        private boolean equalTo(U19Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U19Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u20")
    private static final class U20Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U20Wrapper(@JsonSetter("u20") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u20 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u20";
        }

        @JsonProperty("u20")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU20(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U20Wrapper && equalTo((U20Wrapper) other));
        }

        private boolean equalTo(U20Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U20Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u21")
    private static final class U21Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U21Wrapper(@JsonSetter("u21") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u21 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u21";
        }

        @JsonProperty("u21")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU21(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U21Wrapper && equalTo((U21Wrapper) other));
        }

        private boolean equalTo(U21Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U21Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u22")
    private static final class U22Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U22Wrapper(@JsonSetter("u22") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u22 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u22";
        }

        @JsonProperty("u22")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU22(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U22Wrapper && equalTo((U22Wrapper) other));
        }

        private boolean equalTo(U22Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U22Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u23")
    private static final class U23Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U23Wrapper(@JsonSetter("u23") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u23 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u23";
        }

        @JsonProperty("u23")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU23(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U23Wrapper && equalTo((U23Wrapper) other));
        }

        private boolean equalTo(U23Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U23Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u24")
    private static final class U24Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U24Wrapper(@JsonSetter("u24") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u24 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u24";
        }

        @JsonProperty("u24")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU24(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U24Wrapper && equalTo((U24Wrapper) other));
        }

        private boolean equalTo(U24Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U24Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u25")
    private static final class U25Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U25Wrapper(@JsonSetter("u25") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u25 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u25";
        }

        @JsonProperty("u25")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU25(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U25Wrapper && equalTo((U25Wrapper) other));
        }

        private boolean equalTo(U25Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U25Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u26")
    private static final class U26Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U26Wrapper(@JsonSetter("u26") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u26 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u26";
        }

        @JsonProperty("u26")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU26(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U26Wrapper && equalTo((U26Wrapper) other));
        }

        private boolean equalTo(U26Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U26Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u27")
    private static final class U27Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U27Wrapper(@JsonSetter("u27") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u27 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u27";
        }

        @JsonProperty("u27")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU27(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U27Wrapper && equalTo((U27Wrapper) other));
        }

        private boolean equalTo(U27Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U27Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u28")
    private static final class U28Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U28Wrapper(@JsonSetter("u28") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u28 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u28";
        }

        @JsonProperty("u28")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU28(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U28Wrapper && equalTo((U28Wrapper) other));
        }

        private boolean equalTo(U28Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U28Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u29")
    private static final class U29Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U29Wrapper(@JsonSetter("u29") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u29 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u29";
        }

        @JsonProperty("u29")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU29(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U29Wrapper && equalTo((U29Wrapper) other));
        }

        private boolean equalTo(U29Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U29Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u30")
    private static final class U30Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U30Wrapper(@JsonSetter("u30") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u30 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u30";
        }

        @JsonProperty("u30")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU30(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U30Wrapper && equalTo((U30Wrapper) other));
        }

        private boolean equalTo(U30Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U30Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u31")
    private static final class U31Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U31Wrapper(@JsonSetter("u31") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u31 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u31";
        }

        @JsonProperty("u31")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU31(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U31Wrapper && equalTo((U31Wrapper) other));
        }

        private boolean equalTo(U31Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U31Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u32")
    private static final class U32Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U32Wrapper(@JsonSetter("u32") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u32 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u32";
        }

        @JsonProperty("u32")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU32(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U32Wrapper && equalTo((U32Wrapper) other));
        }

        private boolean equalTo(U32Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U32Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u33")
    private static final class U33Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U33Wrapper(@JsonSetter("u33") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u33 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u33";
        }

        @JsonProperty("u33")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU33(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U33Wrapper && equalTo((U33Wrapper) other));
        }

        private boolean equalTo(U33Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U33Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u34")
    private static final class U34Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U34Wrapper(@JsonSetter("u34") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u34 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u34";
        }

        @JsonProperty("u34")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU34(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U34Wrapper && equalTo((U34Wrapper) other));
        }

        private boolean equalTo(U34Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U34Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u35")
    private static final class U35Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U35Wrapper(@JsonSetter("u35") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u35 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u35";
        }

        @JsonProperty("u35")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU35(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U35Wrapper && equalTo((U35Wrapper) other));
        }

        private boolean equalTo(U35Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U35Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u36")
    private static final class U36Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U36Wrapper(@JsonSetter("u36") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u36 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u36";
        }

        @JsonProperty("u36")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU36(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U36Wrapper && equalTo((U36Wrapper) other));
        }

        private boolean equalTo(U36Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U36Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u37")
    private static final class U37Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U37Wrapper(@JsonSetter("u37") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u37 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u37";
        }

        @JsonProperty("u37")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU37(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U37Wrapper && equalTo((U37Wrapper) other));
        }

        private boolean equalTo(U37Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U37Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u38")
    private static final class U38Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U38Wrapper(@JsonSetter("u38") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u38 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u38";
        }

        @JsonProperty("u38")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU38(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U38Wrapper && equalTo((U38Wrapper) other));
        }

        private boolean equalTo(U38Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U38Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u39")
    private static final class U39Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U39Wrapper(@JsonSetter("u39") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u39 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u39";
        }

        @JsonProperty("u39")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU39(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U39Wrapper && equalTo((U39Wrapper) other));
        }

        private boolean equalTo(U39Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U39Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u40")
    private static final class U40Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U40Wrapper(@JsonSetter("u40") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u40 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u40";
        }

        @JsonProperty("u40")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU40(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U40Wrapper && equalTo((U40Wrapper) other));
        }

        private boolean equalTo(U40Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U40Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u41")
    private static final class U41Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U41Wrapper(@JsonSetter("u41") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u41 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u41";
        }

        @JsonProperty("u41")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU41(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U41Wrapper && equalTo((U41Wrapper) other));
        }

        private boolean equalTo(U41Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U41Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u42")
    private static final class U42Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U42Wrapper(@JsonSetter("u42") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u42 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u42";
        }

        @JsonProperty("u42")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU42(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U42Wrapper && equalTo((U42Wrapper) other));
        }

        private boolean equalTo(U42Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U42Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u43")
    private static final class U43Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U43Wrapper(@JsonSetter("u43") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u43 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u43";
        }

        @JsonProperty("u43")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU43(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U43Wrapper && equalTo((U43Wrapper) other));
        }

        private boolean equalTo(U43Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U43Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u44")
    private static final class U44Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U44Wrapper(@JsonSetter("u44") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u44 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u44";
        }

        @JsonProperty("u44")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU44(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U44Wrapper && equalTo((U44Wrapper) other));
        }

        private boolean equalTo(U44Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U44Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u45")
    private static final class U45Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U45Wrapper(@JsonSetter("u45") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u45 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u45";
        }

        @JsonProperty("u45")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU45(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U45Wrapper && equalTo((U45Wrapper) other));
        }

        private boolean equalTo(U45Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U45Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u46")
    private static final class U46Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U46Wrapper(@JsonSetter("u46") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u46 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u46";
        }

        @JsonProperty("u46")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU46(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U46Wrapper && equalTo((U46Wrapper) other));
        }

        private boolean equalTo(U46Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U46Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u47")
    private static final class U47Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U47Wrapper(@JsonSetter("u47") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u47 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u47";
        }

        @JsonProperty("u47")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU47(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U47Wrapper && equalTo((U47Wrapper) other));
        }

        private boolean equalTo(U47Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U47Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u48")
    private static final class U48Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U48Wrapper(@JsonSetter("u48") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u48 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u48";
        }

        @JsonProperty("u48")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU48(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U48Wrapper && equalTo((U48Wrapper) other));
        }

        private boolean equalTo(U48Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U48Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u49")
    private static final class U49Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U49Wrapper(@JsonSetter("u49") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u49 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u49";
        }

        @JsonProperty("u49")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU49(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U49Wrapper && equalTo((U49Wrapper) other));
        }

        private boolean equalTo(U49Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U49Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u50")
    private static final class U50Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U50Wrapper(@JsonSetter("u50") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u50 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u50";
        }

        @JsonProperty("u50")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU50(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U50Wrapper && equalTo((U50Wrapper) other));
        }

        private boolean equalTo(U50Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U50Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u51")
    private static final class U51Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U51Wrapper(@JsonSetter("u51") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u51 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u51";
        }

        @JsonProperty("u51")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU51(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U51Wrapper && equalTo((U51Wrapper) other));
        }

        private boolean equalTo(U51Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U51Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u52")
    private static final class U52Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U52Wrapper(@JsonSetter("u52") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u52 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u52";
        }

        @JsonProperty("u52")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU52(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U52Wrapper && equalTo((U52Wrapper) other));
        }

        private boolean equalTo(U52Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U52Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u53")
    private static final class U53Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U53Wrapper(@JsonSetter("u53") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u53 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u53";
        }

        @JsonProperty("u53")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU53(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U53Wrapper && equalTo((U53Wrapper) other));
        }

        private boolean equalTo(U53Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U53Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u54")
    private static final class U54Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U54Wrapper(@JsonSetter("u54") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u54 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u54";
        }

        @JsonProperty("u54")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU54(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U54Wrapper && equalTo((U54Wrapper) other));
        }

        private boolean equalTo(U54Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U54Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u55")
    private static final class U55Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U55Wrapper(@JsonSetter("u55") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u55 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u55";
        }

        @JsonProperty("u55")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU55(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U55Wrapper && equalTo((U55Wrapper) other));
        }

        private boolean equalTo(U55Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U55Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u56")
    private static final class U56Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U56Wrapper(@JsonSetter("u56") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u56 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u56";
        }

        @JsonProperty("u56")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU56(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U56Wrapper && equalTo((U56Wrapper) other));
        }

        private boolean equalTo(U56Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U56Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u57")
    private static final class U57Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U57Wrapper(@JsonSetter("u57") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u57 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u57";
        }

        @JsonProperty("u57")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU57(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U57Wrapper && equalTo((U57Wrapper) other));
        }

        private boolean equalTo(U57Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U57Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u58")
    private static final class U58Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U58Wrapper(@JsonSetter("u58") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u58 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u58";
        }

        @JsonProperty("u58")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU58(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U58Wrapper && equalTo((U58Wrapper) other));
        }

        private boolean equalTo(U58Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U58Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u59")
    private static final class U59Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U59Wrapper(@JsonSetter("u59") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u59 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u59";
        }

        @JsonProperty("u59")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU59(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U59Wrapper && equalTo((U59Wrapper) other));
        }

        private boolean equalTo(U59Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U59Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u60")
    private static final class U60Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U60Wrapper(@JsonSetter("u60") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u60 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u60";
        }

        @JsonProperty("u60")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU60(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U60Wrapper && equalTo((U60Wrapper) other));
        }

        private boolean equalTo(U60Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U60Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u61")
    private static final class U61Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U61Wrapper(@JsonSetter("u61") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u61 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u61";
        }

        @JsonProperty("u61")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU61(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U61Wrapper && equalTo((U61Wrapper) other));
        }

        private boolean equalTo(U61Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U61Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u62")
    private static final class U62Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U62Wrapper(@JsonSetter("u62") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u62 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u62";
        }

        @JsonProperty("u62")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU62(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U62Wrapper && equalTo((U62Wrapper) other));
        }

        private boolean equalTo(U62Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U62Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u63")
    private static final class U63Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U63Wrapper(@JsonSetter("u63") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u63 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u63";
        }

        @JsonProperty("u63")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU63(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U63Wrapper && equalTo((U63Wrapper) other));
        }

        private boolean equalTo(U63Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U63Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u64")
    private static final class U64Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U64Wrapper(@JsonSetter("u64") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u64 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u64";
        }

        @JsonProperty("u64")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU64(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U64Wrapper && equalTo((U64Wrapper) other));
        }

        private boolean equalTo(U64Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U64Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u65")
    private static final class U65Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U65Wrapper(@JsonSetter("u65") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u65 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u65";
        }

        @JsonProperty("u65")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU65(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U65Wrapper && equalTo((U65Wrapper) other));
        }

        private boolean equalTo(U65Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U65Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u66")
    private static final class U66Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U66Wrapper(@JsonSetter("u66") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u66 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u66";
        }

        @JsonProperty("u66")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU66(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U66Wrapper && equalTo((U66Wrapper) other));
        }

        private boolean equalTo(U66Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U66Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u67")
    private static final class U67Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U67Wrapper(@JsonSetter("u67") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u67 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u67";
        }

        @JsonProperty("u67")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU67(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U67Wrapper && equalTo((U67Wrapper) other));
        }

        private boolean equalTo(U67Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U67Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u68")
    private static final class U68Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U68Wrapper(@JsonSetter("u68") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u68 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u68";
        }

        @JsonProperty("u68")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU68(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U68Wrapper && equalTo((U68Wrapper) other));
        }

        private boolean equalTo(U68Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U68Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u69")
    private static final class U69Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U69Wrapper(@JsonSetter("u69") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u69 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u69";
        }

        @JsonProperty("u69")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU69(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U69Wrapper && equalTo((U69Wrapper) other));
        }

        private boolean equalTo(U69Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U69Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u70")
    private static final class U70Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U70Wrapper(@JsonSetter("u70") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u70 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u70";
        }

        @JsonProperty("u70")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU70(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U70Wrapper && equalTo((U70Wrapper) other));
        }

        private boolean equalTo(U70Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U70Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u71")
    private static final class U71Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U71Wrapper(@JsonSetter("u71") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u71 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u71";
        }

        @JsonProperty("u71")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU71(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U71Wrapper && equalTo((U71Wrapper) other));
        }

        private boolean equalTo(U71Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U71Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u72")
    private static final class U72Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U72Wrapper(@JsonSetter("u72") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u72 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u72";
        }

        @JsonProperty("u72")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU72(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U72Wrapper && equalTo((U72Wrapper) other));
        }

        private boolean equalTo(U72Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U72Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u73")
    private static final class U73Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U73Wrapper(@JsonSetter("u73") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u73 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u73";
        }

        @JsonProperty("u73")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU73(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U73Wrapper && equalTo((U73Wrapper) other));
        }

        private boolean equalTo(U73Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U73Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u74")
    private static final class U74Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U74Wrapper(@JsonSetter("u74") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u74 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u74";
        }

        @JsonProperty("u74")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU74(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U74Wrapper && equalTo((U74Wrapper) other));
        }

        private boolean equalTo(U74Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U74Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u75")
    private static final class U75Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U75Wrapper(@JsonSetter("u75") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u75 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u75";
        }

        @JsonProperty("u75")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU75(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U75Wrapper && equalTo((U75Wrapper) other));
        }

        private boolean equalTo(U75Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U75Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u76")
    private static final class U76Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U76Wrapper(@JsonSetter("u76") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u76 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u76";
        }

        @JsonProperty("u76")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU76(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U76Wrapper && equalTo((U76Wrapper) other));
        }

        private boolean equalTo(U76Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U76Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u77")
    private static final class U77Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U77Wrapper(@JsonSetter("u77") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u77 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u77";
        }

        @JsonProperty("u77")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU77(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U77Wrapper && equalTo((U77Wrapper) other));
        }

        private boolean equalTo(U77Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U77Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u78")
    private static final class U78Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U78Wrapper(@JsonSetter("u78") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u78 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u78";
        }

        @JsonProperty("u78")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU78(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U78Wrapper && equalTo((U78Wrapper) other));
        }

        private boolean equalTo(U78Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U78Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u79")
    private static final class U79Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U79Wrapper(@JsonSetter("u79") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u79 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u79";
        }

        @JsonProperty("u79")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU79(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U79Wrapper && equalTo((U79Wrapper) other));
        }

        private boolean equalTo(U79Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U79Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u80")
    private static final class U80Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U80Wrapper(@JsonSetter("u80") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u80 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u80";
        }

        @JsonProperty("u80")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU80(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U80Wrapper && equalTo((U80Wrapper) other));
        }

        private boolean equalTo(U80Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U80Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u81")
    private static final class U81Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U81Wrapper(@JsonSetter("u81") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u81 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u81";
        }

        @JsonProperty("u81")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU81(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U81Wrapper && equalTo((U81Wrapper) other));
        }

        private boolean equalTo(U81Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U81Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u82")
    private static final class U82Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U82Wrapper(@JsonSetter("u82") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u82 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u82";
        }

        @JsonProperty("u82")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU82(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U82Wrapper && equalTo((U82Wrapper) other));
        }

        private boolean equalTo(U82Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U82Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u83")
    private static final class U83Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U83Wrapper(@JsonSetter("u83") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u83 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u83";
        }

        @JsonProperty("u83")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU83(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U83Wrapper && equalTo((U83Wrapper) other));
        }

        private boolean equalTo(U83Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U83Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u84")
    private static final class U84Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U84Wrapper(@JsonSetter("u84") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u84 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u84";
        }

        @JsonProperty("u84")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU84(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U84Wrapper && equalTo((U84Wrapper) other));
        }

        private boolean equalTo(U84Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U84Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u85")
    private static final class U85Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U85Wrapper(@JsonSetter("u85") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u85 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u85";
        }

        @JsonProperty("u85")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU85(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U85Wrapper && equalTo((U85Wrapper) other));
        }

        private boolean equalTo(U85Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U85Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u86")
    private static final class U86Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U86Wrapper(@JsonSetter("u86") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u86 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u86";
        }

        @JsonProperty("u86")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU86(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U86Wrapper && equalTo((U86Wrapper) other));
        }

        private boolean equalTo(U86Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U86Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u87")
    private static final class U87Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U87Wrapper(@JsonSetter("u87") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u87 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u87";
        }

        @JsonProperty("u87")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU87(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U87Wrapper && equalTo((U87Wrapper) other));
        }

        private boolean equalTo(U87Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U87Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u88")
    private static final class U88Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U88Wrapper(@JsonSetter("u88") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u88 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u88";
        }

        @JsonProperty("u88")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU88(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U88Wrapper && equalTo((U88Wrapper) other));
        }

        private boolean equalTo(U88Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U88Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u89")
    private static final class U89Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U89Wrapper(@JsonSetter("u89") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u89 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u89";
        }

        @JsonProperty("u89")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU89(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U89Wrapper && equalTo((U89Wrapper) other));
        }

        private boolean equalTo(U89Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U89Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u90")
    private static final class U90Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U90Wrapper(@JsonSetter("u90") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u90 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u90";
        }

        @JsonProperty("u90")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU90(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U90Wrapper && equalTo((U90Wrapper) other));
        }

        private boolean equalTo(U90Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U90Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u91")
    private static final class U91Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U91Wrapper(@JsonSetter("u91") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u91 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u91";
        }

        @JsonProperty("u91")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU91(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U91Wrapper && equalTo((U91Wrapper) other));
        }

        private boolean equalTo(U91Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U91Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u92")
    private static final class U92Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U92Wrapper(@JsonSetter("u92") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u92 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u92";
        }

        @JsonProperty("u92")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU92(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U92Wrapper && equalTo((U92Wrapper) other));
        }

        private boolean equalTo(U92Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U92Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u93")
    private static final class U93Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U93Wrapper(@JsonSetter("u93") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u93 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u93";
        }

        @JsonProperty("u93")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU93(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U93Wrapper && equalTo((U93Wrapper) other));
        }

        private boolean equalTo(U93Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U93Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u94")
    private static final class U94Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U94Wrapper(@JsonSetter("u94") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u94 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u94";
        }

        @JsonProperty("u94")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU94(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U94Wrapper && equalTo((U94Wrapper) other));
        }

        private boolean equalTo(U94Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U94Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u95")
    private static final class U95Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U95Wrapper(@JsonSetter("u95") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u95 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u95";
        }

        @JsonProperty("u95")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU95(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U95Wrapper && equalTo((U95Wrapper) other));
        }

        private boolean equalTo(U95Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U95Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u96")
    private static final class U96Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U96Wrapper(@JsonSetter("u96") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u96 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u96";
        }

        @JsonProperty("u96")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU96(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U96Wrapper && equalTo((U96Wrapper) other));
        }

        private boolean equalTo(U96Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U96Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u97")
    private static final class U97Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U97Wrapper(@JsonSetter("u97") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u97 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u97";
        }

        @JsonProperty("u97")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU97(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U97Wrapper && equalTo((U97Wrapper) other));
        }

        private boolean equalTo(U97Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U97Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u98")
    private static final class U98Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U98Wrapper(@JsonSetter("u98") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u98 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u98";
        }

        @JsonProperty("u98")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU98(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U98Wrapper && equalTo((U98Wrapper) other));
        }

        private boolean equalTo(U98Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U98Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u99")
    private static final class U99Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U99Wrapper(@JsonSetter("u99") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u99 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u99";
        }

        @JsonProperty("u99")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU99(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U99Wrapper && equalTo((U99Wrapper) other));
        }

        private boolean equalTo(U99Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U99Wrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("u100")
    private static final class U100Wrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private U100Wrapper(@JsonSetter("u100") @Nonnull String value) {
            Preconditions.checkNotNull(value, "u100 cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "u100";
        }

        @JsonProperty("u100")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitU100(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof U100Wrapper && equalTo((U100Wrapper) other));
        }

        private boolean equalTo(U100Wrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "U100Wrapper{value: " + value + '}';
        }
    }

    private static final class UnknownWrapper implements Base {
        private final String type;

        private final Map<String, Object> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private UnknownWrapper(@JsonProperty("type") String type) {
            this(type, new HashMap<String, Object>());
        }

        private UnknownWrapper(@Nonnull String type, @Nonnull Map<String, Object> value) {
            Preconditions.checkNotNull(type, "type cannot be null");
            Preconditions.checkNotNull(value, "value cannot be null");
            this.type = type;
            this.value = value;
        }

        @JsonProperty
        private String getType() {
            return type;
        }

        @JsonAnyGetter
        private Map<String, Object> getValue() {
            return value;
        }

        @JsonAnySetter
        private void put(String key, Object val) {
            value.put(key, val);
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitUnknown(type);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof UnknownWrapper && equalTo((UnknownWrapper) other));
        }

        private boolean equalTo(UnknownWrapper other) {
            return this.type.equals(other.type) && this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            int hash = 1;
            hash = 31 * hash + this.type.hashCode();
            hash = 31 * hash + this.value.hashCode();
            return hash;
        }

        @Override
        public String toString() {
            return "UnknownWrapper{type: " + type + ", value: " + value + '}';
        }
    }
}
