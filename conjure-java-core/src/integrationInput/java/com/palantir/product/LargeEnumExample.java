package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.errorprone.annotations.Immutable;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

/**
 * This class is used instead of a native enum to support unknown values. Rather than throw an exception, the
 * {@link LargeEnumExample#valueOf} method defaults to a new instantiation of {@link LargeEnumExample} where
 * {@link LargeEnumExample#get} will return {@link LargeEnumExample.Value#UNKNOWN}.
 *
 * <p>For example, {@code LargeEnumExample.valueOf("corrupted value").get()} will return
 * {@link LargeEnumExample.Value#UNKNOWN}, but {@link LargeEnumExample#toString} will return "corrupted value".
 *
 * <p>There is no method to access all instantiations of this class, since they cannot be known at compile time.
 */
@Generated("com.palantir.conjure.java.types.EnumGenerator")
@Safe
@Immutable
public final class LargeEnumExample {
    public static final LargeEnumExample E0 = new LargeEnumExample(Value.E0, "E0");

    public static final LargeEnumExample E1 = new LargeEnumExample(Value.E1, "E1");

    public static final LargeEnumExample E2 = new LargeEnumExample(Value.E2, "E2");

    public static final LargeEnumExample E3 = new LargeEnumExample(Value.E3, "E3");

    public static final LargeEnumExample E4 = new LargeEnumExample(Value.E4, "E4");

    public static final LargeEnumExample E5 = new LargeEnumExample(Value.E5, "E5");

    public static final LargeEnumExample E6 = new LargeEnumExample(Value.E6, "E6");

    public static final LargeEnumExample E7 = new LargeEnumExample(Value.E7, "E7");

    public static final LargeEnumExample E8 = new LargeEnumExample(Value.E8, "E8");

    public static final LargeEnumExample E9 = new LargeEnumExample(Value.E9, "E9");

    public static final LargeEnumExample E10 = new LargeEnumExample(Value.E10, "E10");

    public static final LargeEnumExample E11 = new LargeEnumExample(Value.E11, "E11");

    public static final LargeEnumExample E12 = new LargeEnumExample(Value.E12, "E12");

    public static final LargeEnumExample E13 = new LargeEnumExample(Value.E13, "E13");

    public static final LargeEnumExample E14 = new LargeEnumExample(Value.E14, "E14");

    public static final LargeEnumExample E15 = new LargeEnumExample(Value.E15, "E15");

    public static final LargeEnumExample E16 = new LargeEnumExample(Value.E16, "E16");

    public static final LargeEnumExample E17 = new LargeEnumExample(Value.E17, "E17");

    public static final LargeEnumExample E18 = new LargeEnumExample(Value.E18, "E18");

    public static final LargeEnumExample E19 = new LargeEnumExample(Value.E19, "E19");

    public static final LargeEnumExample E20 = new LargeEnumExample(Value.E20, "E20");

    public static final LargeEnumExample E21 = new LargeEnumExample(Value.E21, "E21");

    public static final LargeEnumExample E22 = new LargeEnumExample(Value.E22, "E22");

    public static final LargeEnumExample E23 = new LargeEnumExample(Value.E23, "E23");

    public static final LargeEnumExample E24 = new LargeEnumExample(Value.E24, "E24");

    public static final LargeEnumExample E25 = new LargeEnumExample(Value.E25, "E25");

    public static final LargeEnumExample E26 = new LargeEnumExample(Value.E26, "E26");

    public static final LargeEnumExample E27 = new LargeEnumExample(Value.E27, "E27");

    public static final LargeEnumExample E28 = new LargeEnumExample(Value.E28, "E28");

    public static final LargeEnumExample E29 = new LargeEnumExample(Value.E29, "E29");

    public static final LargeEnumExample E30 = new LargeEnumExample(Value.E30, "E30");

    public static final LargeEnumExample E31 = new LargeEnumExample(Value.E31, "E31");

    public static final LargeEnumExample E32 = new LargeEnumExample(Value.E32, "E32");

    public static final LargeEnumExample E33 = new LargeEnumExample(Value.E33, "E33");

    public static final LargeEnumExample E34 = new LargeEnumExample(Value.E34, "E34");

    public static final LargeEnumExample E35 = new LargeEnumExample(Value.E35, "E35");

    public static final LargeEnumExample E36 = new LargeEnumExample(Value.E36, "E36");

    public static final LargeEnumExample E37 = new LargeEnumExample(Value.E37, "E37");

    public static final LargeEnumExample E38 = new LargeEnumExample(Value.E38, "E38");

    public static final LargeEnumExample E39 = new LargeEnumExample(Value.E39, "E39");

    public static final LargeEnumExample E40 = new LargeEnumExample(Value.E40, "E40");

    public static final LargeEnumExample E41 = new LargeEnumExample(Value.E41, "E41");

    public static final LargeEnumExample E42 = new LargeEnumExample(Value.E42, "E42");

    public static final LargeEnumExample E43 = new LargeEnumExample(Value.E43, "E43");

    public static final LargeEnumExample E44 = new LargeEnumExample(Value.E44, "E44");

    public static final LargeEnumExample E45 = new LargeEnumExample(Value.E45, "E45");

    public static final LargeEnumExample E46 = new LargeEnumExample(Value.E46, "E46");

    public static final LargeEnumExample E47 = new LargeEnumExample(Value.E47, "E47");

    public static final LargeEnumExample E48 = new LargeEnumExample(Value.E48, "E48");

    public static final LargeEnumExample E49 = new LargeEnumExample(Value.E49, "E49");

    public static final LargeEnumExample E50 = new LargeEnumExample(Value.E50, "E50");

    public static final LargeEnumExample E51 = new LargeEnumExample(Value.E51, "E51");

    public static final LargeEnumExample E52 = new LargeEnumExample(Value.E52, "E52");

    public static final LargeEnumExample E53 = new LargeEnumExample(Value.E53, "E53");

    public static final LargeEnumExample E54 = new LargeEnumExample(Value.E54, "E54");

    public static final LargeEnumExample E55 = new LargeEnumExample(Value.E55, "E55");

    public static final LargeEnumExample E56 = new LargeEnumExample(Value.E56, "E56");

    public static final LargeEnumExample E57 = new LargeEnumExample(Value.E57, "E57");

    public static final LargeEnumExample E58 = new LargeEnumExample(Value.E58, "E58");

    public static final LargeEnumExample E59 = new LargeEnumExample(Value.E59, "E59");

    public static final LargeEnumExample E60 = new LargeEnumExample(Value.E60, "E60");

    public static final LargeEnumExample E61 = new LargeEnumExample(Value.E61, "E61");

    public static final LargeEnumExample E62 = new LargeEnumExample(Value.E62, "E62");

    public static final LargeEnumExample E63 = new LargeEnumExample(Value.E63, "E63");

    public static final LargeEnumExample E64 = new LargeEnumExample(Value.E64, "E64");

    public static final LargeEnumExample E65 = new LargeEnumExample(Value.E65, "E65");

    public static final LargeEnumExample E66 = new LargeEnumExample(Value.E66, "E66");

    public static final LargeEnumExample E67 = new LargeEnumExample(Value.E67, "E67");

    public static final LargeEnumExample E68 = new LargeEnumExample(Value.E68, "E68");

    public static final LargeEnumExample E69 = new LargeEnumExample(Value.E69, "E69");

    public static final LargeEnumExample E70 = new LargeEnumExample(Value.E70, "E70");

    public static final LargeEnumExample E71 = new LargeEnumExample(Value.E71, "E71");

    public static final LargeEnumExample E72 = new LargeEnumExample(Value.E72, "E72");

    public static final LargeEnumExample E73 = new LargeEnumExample(Value.E73, "E73");

    public static final LargeEnumExample E74 = new LargeEnumExample(Value.E74, "E74");

    public static final LargeEnumExample E75 = new LargeEnumExample(Value.E75, "E75");

    public static final LargeEnumExample E76 = new LargeEnumExample(Value.E76, "E76");

    public static final LargeEnumExample E77 = new LargeEnumExample(Value.E77, "E77");

    public static final LargeEnumExample E78 = new LargeEnumExample(Value.E78, "E78");

    public static final LargeEnumExample E79 = new LargeEnumExample(Value.E79, "E79");

    public static final LargeEnumExample E80 = new LargeEnumExample(Value.E80, "E80");

    public static final LargeEnumExample E81 = new LargeEnumExample(Value.E81, "E81");

    public static final LargeEnumExample E82 = new LargeEnumExample(Value.E82, "E82");

    public static final LargeEnumExample E83 = new LargeEnumExample(Value.E83, "E83");

    public static final LargeEnumExample E84 = new LargeEnumExample(Value.E84, "E84");

    public static final LargeEnumExample E85 = new LargeEnumExample(Value.E85, "E85");

    public static final LargeEnumExample E86 = new LargeEnumExample(Value.E86, "E86");

    public static final LargeEnumExample E87 = new LargeEnumExample(Value.E87, "E87");

    public static final LargeEnumExample E88 = new LargeEnumExample(Value.E88, "E88");

    public static final LargeEnumExample E89 = new LargeEnumExample(Value.E89, "E89");

    public static final LargeEnumExample E90 = new LargeEnumExample(Value.E90, "E90");

    public static final LargeEnumExample E91 = new LargeEnumExample(Value.E91, "E91");

    public static final LargeEnumExample E92 = new LargeEnumExample(Value.E92, "E92");

    public static final LargeEnumExample E93 = new LargeEnumExample(Value.E93, "E93");

    public static final LargeEnumExample E94 = new LargeEnumExample(Value.E94, "E94");

    public static final LargeEnumExample E95 = new LargeEnumExample(Value.E95, "E95");

    public static final LargeEnumExample E96 = new LargeEnumExample(Value.E96, "E96");

    public static final LargeEnumExample E97 = new LargeEnumExample(Value.E97, "E97");

    public static final LargeEnumExample E98 = new LargeEnumExample(Value.E98, "E98");

    public static final LargeEnumExample E99 = new LargeEnumExample(Value.E99, "E99");

    public static final LargeEnumExample E100 = new LargeEnumExample(Value.E100, "E100");

    private static final List<LargeEnumExample> values = Collections.unmodifiableList(Arrays.asList(
            E0, E1, E2, E3, E4, E5, E6, E7, E8, E9, E10, E11, E12, E13, E14, E15, E16, E17, E18, E19, E20, E21, E22,
            E23, E24, E25, E26, E27, E28, E29, E30, E31, E32, E33, E34, E35, E36, E37, E38, E39, E40, E41, E42, E43,
            E44, E45, E46, E47, E48, E49, E50, E51, E52, E53, E54, E55, E56, E57, E58, E59, E60, E61, E62, E63, E64,
            E65, E66, E67, E68, E69, E70, E71, E72, E73, E74, E75, E76, E77, E78, E79, E80, E81, E82, E83, E84, E85,
            E86, E87, E88, E89, E90, E91, E92, E93, E94, E95, E96, E97, E98, E99, E100));

    private final Value value;

    private final String string;

    private LargeEnumExample(Value value, String string) {
        this.value = value;
        this.string = string;
    }

    public Value get() {
        return this.value;
    }

    @Override
    @JsonValue
    public String toString() {
        return this.string;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return (this == other)
                || (this.value == Value.UNKNOWN
                        && other instanceof LargeEnumExample
                        && this.string.equals(((LargeEnumExample) other).string));
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static LargeEnumExample valueOf(@Nonnull @Safe String value) {
        Preconditions.checkNotNull(value, "value cannot be null");
        String upperCasedValue = value.toUpperCase(Locale.ROOT);
        switch (upperCasedValue) {
            case "E0":
                return E0;
            case "E1":
                return E1;
            case "E2":
                return E2;
            case "E3":
                return E3;
            case "E4":
                return E4;
            case "E5":
                return E5;
            case "E6":
                return E6;
            case "E7":
                return E7;
            case "E8":
                return E8;
            case "E9":
                return E9;
            case "E10":
                return E10;
            case "E11":
                return E11;
            case "E12":
                return E12;
            case "E13":
                return E13;
            case "E14":
                return E14;
            case "E15":
                return E15;
            case "E16":
                return E16;
            case "E17":
                return E17;
            case "E18":
                return E18;
            case "E19":
                return E19;
            case "E20":
                return E20;
            case "E21":
                return E21;
            case "E22":
                return E22;
            case "E23":
                return E23;
            case "E24":
                return E24;
            case "E25":
                return E25;
            case "E26":
                return E26;
            case "E27":
                return E27;
            case "E28":
                return E28;
            case "E29":
                return E29;
            case "E30":
                return E30;
            case "E31":
                return E31;
            case "E32":
                return E32;
            case "E33":
                return E33;
            case "E34":
                return E34;
            case "E35":
                return E35;
            case "E36":
                return E36;
            case "E37":
                return E37;
            case "E38":
                return E38;
            case "E39":
                return E39;
            case "E40":
                return E40;
            case "E41":
                return E41;
            case "E42":
                return E42;
            case "E43":
                return E43;
            case "E44":
                return E44;
            case "E45":
                return E45;
            case "E46":
                return E46;
            case "E47":
                return E47;
            case "E48":
                return E48;
            case "E49":
                return E49;
            case "E50":
                return E50;
            case "E51":
                return E51;
            case "E52":
                return E52;
            case "E53":
                return E53;
            case "E54":
                return E54;
            case "E55":
                return E55;
            case "E56":
                return E56;
            case "E57":
                return E57;
            case "E58":
                return E58;
            case "E59":
                return E59;
            case "E60":
                return E60;
            case "E61":
                return E61;
            case "E62":
                return E62;
            case "E63":
                return E63;
            case "E64":
                return E64;
            case "E65":
                return E65;
            case "E66":
                return E66;
            case "E67":
                return E67;
            case "E68":
                return E68;
            case "E69":
                return E69;
            case "E70":
                return E70;
            case "E71":
                return E71;
            case "E72":
                return E72;
            case "E73":
                return E73;
            case "E74":
                return E74;
            case "E75":
                return E75;
            case "E76":
                return E76;
            case "E77":
                return E77;
            case "E78":
                return E78;
            case "E79":
                return E79;
            case "E80":
                return E80;
            case "E81":
                return E81;
            case "E82":
                return E82;
            case "E83":
                return E83;
            case "E84":
                return E84;
            case "E85":
                return E85;
            case "E86":
                return E86;
            case "E87":
                return E87;
            case "E88":
                return E88;
            case "E89":
                return E89;
            case "E90":
                return E90;
            case "E91":
                return E91;
            case "E92":
                return E92;
            case "E93":
                return E93;
            case "E94":
                return E94;
            case "E95":
                return E95;
            case "E96":
                return E96;
            case "E97":
                return E97;
            case "E98":
                return E98;
            case "E99":
                return E99;
            case "E100":
                return E100;
            default:
                return new LargeEnumExample(Value.UNKNOWN, upperCasedValue);
        }
    }

    public <T> T accept(Visitor<T> visitor) {
        switch (value) {
            case E0:
                return visitor.visitE0();
            case E1:
                return visitor.visitE1();
            case E2:
                return visitor.visitE2();
            case E3:
                return visitor.visitE3();
            case E4:
                return visitor.visitE4();
            case E5:
                return visitor.visitE5();
            case E6:
                return visitor.visitE6();
            case E7:
                return visitor.visitE7();
            case E8:
                return visitor.visitE8();
            case E9:
                return visitor.visitE9();
            case E10:
                return visitor.visitE10();
            case E11:
                return visitor.visitE11();
            case E12:
                return visitor.visitE12();
            case E13:
                return visitor.visitE13();
            case E14:
                return visitor.visitE14();
            case E15:
                return visitor.visitE15();
            case E16:
                return visitor.visitE16();
            case E17:
                return visitor.visitE17();
            case E18:
                return visitor.visitE18();
            case E19:
                return visitor.visitE19();
            case E20:
                return visitor.visitE20();
            case E21:
                return visitor.visitE21();
            case E22:
                return visitor.visitE22();
            case E23:
                return visitor.visitE23();
            case E24:
                return visitor.visitE24();
            case E25:
                return visitor.visitE25();
            case E26:
                return visitor.visitE26();
            case E27:
                return visitor.visitE27();
            case E28:
                return visitor.visitE28();
            case E29:
                return visitor.visitE29();
            case E30:
                return visitor.visitE30();
            case E31:
                return visitor.visitE31();
            case E32:
                return visitor.visitE32();
            case E33:
                return visitor.visitE33();
            case E34:
                return visitor.visitE34();
            case E35:
                return visitor.visitE35();
            case E36:
                return visitor.visitE36();
            case E37:
                return visitor.visitE37();
            case E38:
                return visitor.visitE38();
            case E39:
                return visitor.visitE39();
            case E40:
                return visitor.visitE40();
            case E41:
                return visitor.visitE41();
            case E42:
                return visitor.visitE42();
            case E43:
                return visitor.visitE43();
            case E44:
                return visitor.visitE44();
            case E45:
                return visitor.visitE45();
            case E46:
                return visitor.visitE46();
            case E47:
                return visitor.visitE47();
            case E48:
                return visitor.visitE48();
            case E49:
                return visitor.visitE49();
            case E50:
                return visitor.visitE50();
            case E51:
                return visitor.visitE51();
            case E52:
                return visitor.visitE52();
            case E53:
                return visitor.visitE53();
            case E54:
                return visitor.visitE54();
            case E55:
                return visitor.visitE55();
            case E56:
                return visitor.visitE56();
            case E57:
                return visitor.visitE57();
            case E58:
                return visitor.visitE58();
            case E59:
                return visitor.visitE59();
            case E60:
                return visitor.visitE60();
            case E61:
                return visitor.visitE61();
            case E62:
                return visitor.visitE62();
            case E63:
                return visitor.visitE63();
            case E64:
                return visitor.visitE64();
            case E65:
                return visitor.visitE65();
            case E66:
                return visitor.visitE66();
            case E67:
                return visitor.visitE67();
            case E68:
                return visitor.visitE68();
            case E69:
                return visitor.visitE69();
            case E70:
                return visitor.visitE70();
            case E71:
                return visitor.visitE71();
            case E72:
                return visitor.visitE72();
            case E73:
                return visitor.visitE73();
            case E74:
                return visitor.visitE74();
            case E75:
                return visitor.visitE75();
            case E76:
                return visitor.visitE76();
            case E77:
                return visitor.visitE77();
            case E78:
                return visitor.visitE78();
            case E79:
                return visitor.visitE79();
            case E80:
                return visitor.visitE80();
            case E81:
                return visitor.visitE81();
            case E82:
                return visitor.visitE82();
            case E83:
                return visitor.visitE83();
            case E84:
                return visitor.visitE84();
            case E85:
                return visitor.visitE85();
            case E86:
                return visitor.visitE86();
            case E87:
                return visitor.visitE87();
            case E88:
                return visitor.visitE88();
            case E89:
                return visitor.visitE89();
            case E90:
                return visitor.visitE90();
            case E91:
                return visitor.visitE91();
            case E92:
                return visitor.visitE92();
            case E93:
                return visitor.visitE93();
            case E94:
                return visitor.visitE94();
            case E95:
                return visitor.visitE95();
            case E96:
                return visitor.visitE96();
            case E97:
                return visitor.visitE97();
            case E98:
                return visitor.visitE98();
            case E99:
                return visitor.visitE99();
            case E100:
                return visitor.visitE100();
            default:
                return visitor.visitUnknown(string);
        }
    }

    public static List<LargeEnumExample> values() {
        return values;
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public enum Value {
        E0,

        E1,

        E2,

        E3,

        E4,

        E5,

        E6,

        E7,

        E8,

        E9,

        E10,

        E11,

        E12,

        E13,

        E14,

        E15,

        E16,

        E17,

        E18,

        E19,

        E20,

        E21,

        E22,

        E23,

        E24,

        E25,

        E26,

        E27,

        E28,

        E29,

        E30,

        E31,

        E32,

        E33,

        E34,

        E35,

        E36,

        E37,

        E38,

        E39,

        E40,

        E41,

        E42,

        E43,

        E44,

        E45,

        E46,

        E47,

        E48,

        E49,

        E50,

        E51,

        E52,

        E53,

        E54,

        E55,

        E56,

        E57,

        E58,

        E59,

        E60,

        E61,

        E62,

        E63,

        E64,

        E65,

        E66,

        E67,

        E68,

        E69,

        E70,

        E71,

        E72,

        E73,

        E74,

        E75,

        E76,

        E77,

        E78,

        E79,

        E80,

        E81,

        E82,

        E83,

        E84,

        E85,

        E86,

        E87,

        E88,

        E89,

        E90,

        E91,

        E92,

        E93,

        E94,

        E95,

        E96,

        E97,

        E98,

        E99,

        E100,

        UNKNOWN
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public interface Visitor<T> {
        T visitE0();

        T visitE1();

        T visitE2();

        T visitE3();

        T visitE4();

        T visitE5();

        T visitE6();

        T visitE7();

        T visitE8();

        T visitE9();

        T visitE10();

        T visitE11();

        T visitE12();

        T visitE13();

        T visitE14();

        T visitE15();

        T visitE16();

        T visitE17();

        T visitE18();

        T visitE19();

        T visitE20();

        T visitE21();

        T visitE22();

        T visitE23();

        T visitE24();

        T visitE25();

        T visitE26();

        T visitE27();

        T visitE28();

        T visitE29();

        T visitE30();

        T visitE31();

        T visitE32();

        T visitE33();

        T visitE34();

        T visitE35();

        T visitE36();

        T visitE37();

        T visitE38();

        T visitE39();

        T visitE40();

        T visitE41();

        T visitE42();

        T visitE43();

        T visitE44();

        T visitE45();

        T visitE46();

        T visitE47();

        T visitE48();

        T visitE49();

        T visitE50();

        T visitE51();

        T visitE52();

        T visitE53();

        T visitE54();

        T visitE55();

        T visitE56();

        T visitE57();

        T visitE58();

        T visitE59();

        T visitE60();

        T visitE61();

        T visitE62();

        T visitE63();

        T visitE64();

        T visitE65();

        T visitE66();

        T visitE67();

        T visitE68();

        T visitE69();

        T visitE70();

        T visitE71();

        T visitE72();

        T visitE73();

        T visitE74();

        T visitE75();

        T visitE76();

        T visitE77();

        T visitE78();

        T visitE79();

        T visitE80();

        T visitE81();

        T visitE82();

        T visitE83();

        T visitE84();

        T visitE85();

        T visitE86();

        T visitE87();

        T visitE88();

        T visitE89();

        T visitE90();

        T visitE91();

        T visitE92();

        T visitE93();

        T visitE94();

        T visitE95();

        T visitE96();

        T visitE97();

        T visitE98();

        T visitE99();

        T visitE100();

        T visitUnknown(String unknownValue);
    }
}
