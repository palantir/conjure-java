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
 * {@link EnumWithFiftyOneValues#valueOf} method defaults to a new instantiation of {@link EnumWithFiftyOneValues} where
 * {@link EnumWithFiftyOneValues#get} will return {@link EnumWithFiftyOneValues.Value#UNKNOWN}.
 *
 * <p>For example, {@code EnumWithFiftyOneValues.valueOf("corrupted value").get()} will return
 * {@link EnumWithFiftyOneValues.Value#UNKNOWN}, but {@link EnumWithFiftyOneValues#toString} will return "corrupted
 * value".
 *
 * <p>There is no method to access all instantiations of this class, since they cannot be known at compile time.
 */
@Generated("com.palantir.conjure.java.types.EnumGenerator")
@Safe
@Immutable
public final class EnumWithFiftyOneValues {
    public static final EnumWithFiftyOneValues ONE = new EnumWithFiftyOneValues(Value.ONE, "ONE");

    public static final EnumWithFiftyOneValues TWO = new EnumWithFiftyOneValues(Value.TWO, "TWO");

    public static final EnumWithFiftyOneValues THREE = new EnumWithFiftyOneValues(Value.THREE, "THREE");

    public static final EnumWithFiftyOneValues FOUR = new EnumWithFiftyOneValues(Value.FOUR, "FOUR");

    public static final EnumWithFiftyOneValues FIVE = new EnumWithFiftyOneValues(Value.FIVE, "FIVE");

    public static final EnumWithFiftyOneValues SIX = new EnumWithFiftyOneValues(Value.SIX, "SIX");

    public static final EnumWithFiftyOneValues SEVEN = new EnumWithFiftyOneValues(Value.SEVEN, "SEVEN");

    public static final EnumWithFiftyOneValues EIGHT = new EnumWithFiftyOneValues(Value.EIGHT, "EIGHT");

    public static final EnumWithFiftyOneValues NINE = new EnumWithFiftyOneValues(Value.NINE, "NINE");

    public static final EnumWithFiftyOneValues TEN = new EnumWithFiftyOneValues(Value.TEN, "TEN");

    public static final EnumWithFiftyOneValues ELEVEN = new EnumWithFiftyOneValues(Value.ELEVEN, "ELEVEN");

    public static final EnumWithFiftyOneValues TWELVE = new EnumWithFiftyOneValues(Value.TWELVE, "TWELVE");

    public static final EnumWithFiftyOneValues THIRTEEN = new EnumWithFiftyOneValues(Value.THIRTEEN, "THIRTEEN");

    public static final EnumWithFiftyOneValues FOURTEEN = new EnumWithFiftyOneValues(Value.FOURTEEN, "FOURTEEN");

    public static final EnumWithFiftyOneValues FIFTEEN = new EnumWithFiftyOneValues(Value.FIFTEEN, "FIFTEEN");

    public static final EnumWithFiftyOneValues SIXTEEN = new EnumWithFiftyOneValues(Value.SIXTEEN, "SIXTEEN");

    public static final EnumWithFiftyOneValues SEVENTEEN = new EnumWithFiftyOneValues(Value.SEVENTEEN, "SEVENTEEN");

    public static final EnumWithFiftyOneValues EIGHTEEN = new EnumWithFiftyOneValues(Value.EIGHTEEN, "EIGHTEEN");

    public static final EnumWithFiftyOneValues NINETEEN = new EnumWithFiftyOneValues(Value.NINETEEN, "NINETEEN");

    public static final EnumWithFiftyOneValues TWENTY = new EnumWithFiftyOneValues(Value.TWENTY, "TWENTY");

    public static final EnumWithFiftyOneValues TWENTY_ONE = new EnumWithFiftyOneValues(Value.TWENTY_ONE, "TWENTY_ONE");

    public static final EnumWithFiftyOneValues TWENTY_TWO = new EnumWithFiftyOneValues(Value.TWENTY_TWO, "TWENTY_TWO");

    public static final EnumWithFiftyOneValues TWENTY_THREE =
            new EnumWithFiftyOneValues(Value.TWENTY_THREE, "TWENTY_THREE");

    public static final EnumWithFiftyOneValues TWENTY_FOUR =
            new EnumWithFiftyOneValues(Value.TWENTY_FOUR, "TWENTY_FOUR");

    public static final EnumWithFiftyOneValues TWENTY_FIVE =
            new EnumWithFiftyOneValues(Value.TWENTY_FIVE, "TWENTY_FIVE");

    public static final EnumWithFiftyOneValues TWENTY_SIX = new EnumWithFiftyOneValues(Value.TWENTY_SIX, "TWENTY_SIX");

    public static final EnumWithFiftyOneValues TWENTY_SEVEN =
            new EnumWithFiftyOneValues(Value.TWENTY_SEVEN, "TWENTY_SEVEN");

    public static final EnumWithFiftyOneValues TWENTY_EIGHT =
            new EnumWithFiftyOneValues(Value.TWENTY_EIGHT, "TWENTY_EIGHT");

    public static final EnumWithFiftyOneValues TWENTY_NINE =
            new EnumWithFiftyOneValues(Value.TWENTY_NINE, "TWENTY_NINE");

    public static final EnumWithFiftyOneValues THIRTY = new EnumWithFiftyOneValues(Value.THIRTY, "THIRTY");

    public static final EnumWithFiftyOneValues THIRTY_ONE = new EnumWithFiftyOneValues(Value.THIRTY_ONE, "THIRTY_ONE");

    public static final EnumWithFiftyOneValues THIRTY_TWO = new EnumWithFiftyOneValues(Value.THIRTY_TWO, "THIRTY_TWO");

    public static final EnumWithFiftyOneValues THIRTY_THREE =
            new EnumWithFiftyOneValues(Value.THIRTY_THREE, "THIRTY_THREE");

    public static final EnumWithFiftyOneValues THIRTY_FOUR =
            new EnumWithFiftyOneValues(Value.THIRTY_FOUR, "THIRTY_FOUR");

    public static final EnumWithFiftyOneValues THIRTY_FIVE =
            new EnumWithFiftyOneValues(Value.THIRTY_FIVE, "THIRTY_FIVE");

    public static final EnumWithFiftyOneValues THIRTY_SIX = new EnumWithFiftyOneValues(Value.THIRTY_SIX, "THIRTY_SIX");

    public static final EnumWithFiftyOneValues THIRTY_SEVEN =
            new EnumWithFiftyOneValues(Value.THIRTY_SEVEN, "THIRTY_SEVEN");

    public static final EnumWithFiftyOneValues THIRTY_EIGHT =
            new EnumWithFiftyOneValues(Value.THIRTY_EIGHT, "THIRTY_EIGHT");

    public static final EnumWithFiftyOneValues THIRTY_NINE =
            new EnumWithFiftyOneValues(Value.THIRTY_NINE, "THIRTY_NINE");

    public static final EnumWithFiftyOneValues FORTY = new EnumWithFiftyOneValues(Value.FORTY, "FORTY");

    public static final EnumWithFiftyOneValues FORTY_ONE = new EnumWithFiftyOneValues(Value.FORTY_ONE, "FORTY_ONE");

    public static final EnumWithFiftyOneValues FORTY_TWO = new EnumWithFiftyOneValues(Value.FORTY_TWO, "FORTY_TWO");

    public static final EnumWithFiftyOneValues FORTY_THREE =
            new EnumWithFiftyOneValues(Value.FORTY_THREE, "FORTY_THREE");

    public static final EnumWithFiftyOneValues FORTY_FOUR = new EnumWithFiftyOneValues(Value.FORTY_FOUR, "FORTY_FOUR");

    public static final EnumWithFiftyOneValues FORTY_FIVE = new EnumWithFiftyOneValues(Value.FORTY_FIVE, "FORTY_FIVE");

    public static final EnumWithFiftyOneValues FORTY_SIX = new EnumWithFiftyOneValues(Value.FORTY_SIX, "FORTY_SIX");

    public static final EnumWithFiftyOneValues FORTY_SEVEN =
            new EnumWithFiftyOneValues(Value.FORTY_SEVEN, "FORTY_SEVEN");

    public static final EnumWithFiftyOneValues FORTY_EIGHT =
            new EnumWithFiftyOneValues(Value.FORTY_EIGHT, "FORTY_EIGHT");

    public static final EnumWithFiftyOneValues FORTY_NINE = new EnumWithFiftyOneValues(Value.FORTY_NINE, "FORTY_NINE");

    public static final EnumWithFiftyOneValues FIFTY = new EnumWithFiftyOneValues(Value.FIFTY, "FIFTY");

    public static final EnumWithFiftyOneValues FIFTY_ONE = new EnumWithFiftyOneValues(Value.FIFTY_ONE, "FIFTY_ONE");

    private static final List<EnumWithFiftyOneValues> values = Collections.unmodifiableList(Arrays.asList(
            ONE,
            TWO,
            THREE,
            FOUR,
            FIVE,
            SIX,
            SEVEN,
            EIGHT,
            NINE,
            TEN,
            ELEVEN,
            TWELVE,
            THIRTEEN,
            FOURTEEN,
            FIFTEEN,
            SIXTEEN,
            SEVENTEEN,
            EIGHTEEN,
            NINETEEN,
            TWENTY,
            TWENTY_ONE,
            TWENTY_TWO,
            TWENTY_THREE,
            TWENTY_FOUR,
            TWENTY_FIVE,
            TWENTY_SIX,
            TWENTY_SEVEN,
            TWENTY_EIGHT,
            TWENTY_NINE,
            THIRTY,
            THIRTY_ONE,
            THIRTY_TWO,
            THIRTY_THREE,
            THIRTY_FOUR,
            THIRTY_FIVE,
            THIRTY_SIX,
            THIRTY_SEVEN,
            THIRTY_EIGHT,
            THIRTY_NINE,
            FORTY,
            FORTY_ONE,
            FORTY_TWO,
            FORTY_THREE,
            FORTY_FOUR,
            FORTY_FIVE,
            FORTY_SIX,
            FORTY_SEVEN,
            FORTY_EIGHT,
            FORTY_NINE,
            FIFTY,
            FIFTY_ONE));

    private final Value value;

    private final String string;

    private EnumWithFiftyOneValues(Value value, String string) {
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
                        && other instanceof EnumWithFiftyOneValues
                        && this.string.equals(((EnumWithFiftyOneValues) other).string));
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static EnumWithFiftyOneValues valueOf(@Nonnull @Safe String value) {
        Preconditions.checkNotNull(value, "value cannot be null");
        String upperCasedValue = value.toUpperCase(Locale.ROOT);
        switch (upperCasedValue) {
            case "ONE":
                return ONE;
            case "TWO":
                return TWO;
            case "THREE":
                return THREE;
            case "FOUR":
                return FOUR;
            case "FIVE":
                return FIVE;
            case "SIX":
                return SIX;
            case "SEVEN":
                return SEVEN;
            case "EIGHT":
                return EIGHT;
            case "NINE":
                return NINE;
            case "TEN":
                return TEN;
            case "ELEVEN":
                return ELEVEN;
            case "TWELVE":
                return TWELVE;
            case "THIRTEEN":
                return THIRTEEN;
            case "FOURTEEN":
                return FOURTEEN;
            case "FIFTEEN":
                return FIFTEEN;
            case "SIXTEEN":
                return SIXTEEN;
            case "SEVENTEEN":
                return SEVENTEEN;
            case "EIGHTEEN":
                return EIGHTEEN;
            case "NINETEEN":
                return NINETEEN;
            case "TWENTY":
                return TWENTY;
            case "TWENTY_ONE":
                return TWENTY_ONE;
            case "TWENTY_TWO":
                return TWENTY_TWO;
            case "TWENTY_THREE":
                return TWENTY_THREE;
            case "TWENTY_FOUR":
                return TWENTY_FOUR;
            case "TWENTY_FIVE":
                return TWENTY_FIVE;
            case "TWENTY_SIX":
                return TWENTY_SIX;
            case "TWENTY_SEVEN":
                return TWENTY_SEVEN;
            case "TWENTY_EIGHT":
                return TWENTY_EIGHT;
            case "TWENTY_NINE":
                return TWENTY_NINE;
            case "THIRTY":
                return THIRTY;
            case "THIRTY_ONE":
                return THIRTY_ONE;
            case "THIRTY_TWO":
                return THIRTY_TWO;
            case "THIRTY_THREE":
                return THIRTY_THREE;
            case "THIRTY_FOUR":
                return THIRTY_FOUR;
            case "THIRTY_FIVE":
                return THIRTY_FIVE;
            case "THIRTY_SIX":
                return THIRTY_SIX;
            case "THIRTY_SEVEN":
                return THIRTY_SEVEN;
            case "THIRTY_EIGHT":
                return THIRTY_EIGHT;
            case "THIRTY_NINE":
                return THIRTY_NINE;
            case "FORTY":
                return FORTY;
            case "FORTY_ONE":
                return FORTY_ONE;
            case "FORTY_TWO":
                return FORTY_TWO;
            case "FORTY_THREE":
                return FORTY_THREE;
            case "FORTY_FOUR":
                return FORTY_FOUR;
            case "FORTY_FIVE":
                return FORTY_FIVE;
            case "FORTY_SIX":
                return FORTY_SIX;
            case "FORTY_SEVEN":
                return FORTY_SEVEN;
            case "FORTY_EIGHT":
                return FORTY_EIGHT;
            case "FORTY_NINE":
                return FORTY_NINE;
            case "FIFTY":
                return FIFTY;
            case "FIFTY_ONE":
                return FIFTY_ONE;
            default:
                return new EnumWithFiftyOneValues(Value.UNKNOWN, upperCasedValue);
        }
    }

    public <T> T accept(Visitor<T> visitor) {
        switch (value) {
            case ONE:
                return visitor.visitOne();
            case TWO:
                return visitor.visitTwo();
            case THREE:
                return visitor.visitThree();
            case FOUR:
                return visitor.visitFour();
            case FIVE:
                return visitor.visitFive();
            case SIX:
                return visitor.visitSix();
            case SEVEN:
                return visitor.visitSeven();
            case EIGHT:
                return visitor.visitEight();
            case NINE:
                return visitor.visitNine();
            case TEN:
                return visitor.visitTen();
            case ELEVEN:
                return visitor.visitEleven();
            case TWELVE:
                return visitor.visitTwelve();
            case THIRTEEN:
                return visitor.visitThirteen();
            case FOURTEEN:
                return visitor.visitFourteen();
            case FIFTEEN:
                return visitor.visitFifteen();
            case SIXTEEN:
                return visitor.visitSixteen();
            case SEVENTEEN:
                return visitor.visitSeventeen();
            case EIGHTEEN:
                return visitor.visitEighteen();
            case NINETEEN:
                return visitor.visitNineteen();
            case TWENTY:
                return visitor.visitTwenty();
            case TWENTY_ONE:
                return visitor.visitTwentyOne();
            case TWENTY_TWO:
                return visitor.visitTwentyTwo();
            case TWENTY_THREE:
                return visitor.visitTwentyThree();
            case TWENTY_FOUR:
                return visitor.visitTwentyFour();
            case TWENTY_FIVE:
                return visitor.visitTwentyFive();
            case TWENTY_SIX:
                return visitor.visitTwentySix();
            case TWENTY_SEVEN:
                return visitor.visitTwentySeven();
            case TWENTY_EIGHT:
                return visitor.visitTwentyEight();
            case TWENTY_NINE:
                return visitor.visitTwentyNine();
            case THIRTY:
                return visitor.visitThirty();
            case THIRTY_ONE:
                return visitor.visitThirtyOne();
            case THIRTY_TWO:
                return visitor.visitThirtyTwo();
            case THIRTY_THREE:
                return visitor.visitThirtyThree();
            case THIRTY_FOUR:
                return visitor.visitThirtyFour();
            case THIRTY_FIVE:
                return visitor.visitThirtyFive();
            case THIRTY_SIX:
                return visitor.visitThirtySix();
            case THIRTY_SEVEN:
                return visitor.visitThirtySeven();
            case THIRTY_EIGHT:
                return visitor.visitThirtyEight();
            case THIRTY_NINE:
                return visitor.visitThirtyNine();
            case FORTY:
                return visitor.visitForty();
            case FORTY_ONE:
                return visitor.visitFortyOne();
            case FORTY_TWO:
                return visitor.visitFortyTwo();
            case FORTY_THREE:
                return visitor.visitFortyThree();
            case FORTY_FOUR:
                return visitor.visitFortyFour();
            case FORTY_FIVE:
                return visitor.visitFortyFive();
            case FORTY_SIX:
                return visitor.visitFortySix();
            case FORTY_SEVEN:
                return visitor.visitFortySeven();
            case FORTY_EIGHT:
                return visitor.visitFortyEight();
            case FORTY_NINE:
                return visitor.visitFortyNine();
            case FIFTY:
                return visitor.visitFifty();
            case FIFTY_ONE:
                return visitor.visitFiftyOne();
            default:
                return visitor.visitUnknown(string);
        }
    }

    public static List<EnumWithFiftyOneValues> values() {
        return values;
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public enum Value {
        ONE,

        TWO,

        THREE,

        FOUR,

        FIVE,

        SIX,

        SEVEN,

        EIGHT,

        NINE,

        TEN,

        ELEVEN,

        TWELVE,

        THIRTEEN,

        FOURTEEN,

        FIFTEEN,

        SIXTEEN,

        SEVENTEEN,

        EIGHTEEN,

        NINETEEN,

        TWENTY,

        TWENTY_ONE,

        TWENTY_TWO,

        TWENTY_THREE,

        TWENTY_FOUR,

        TWENTY_FIVE,

        TWENTY_SIX,

        TWENTY_SEVEN,

        TWENTY_EIGHT,

        TWENTY_NINE,

        THIRTY,

        THIRTY_ONE,

        THIRTY_TWO,

        THIRTY_THREE,

        THIRTY_FOUR,

        THIRTY_FIVE,

        THIRTY_SIX,

        THIRTY_SEVEN,

        THIRTY_EIGHT,

        THIRTY_NINE,

        FORTY,

        FORTY_ONE,

        FORTY_TWO,

        FORTY_THREE,

        FORTY_FOUR,

        FORTY_FIVE,

        FORTY_SIX,

        FORTY_SEVEN,

        FORTY_EIGHT,

        FORTY_NINE,

        FIFTY,

        FIFTY_ONE,

        UNKNOWN
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public interface Visitor<T> {
        T visitOne();

        T visitTwo();

        T visitThree();

        T visitFour();

        T visitFive();

        T visitSix();

        T visitSeven();

        T visitEight();

        T visitNine();

        T visitTen();

        T visitEleven();

        T visitTwelve();

        T visitThirteen();

        T visitFourteen();

        T visitFifteen();

        T visitSixteen();

        T visitSeventeen();

        T visitEighteen();

        T visitNineteen();

        T visitTwenty();

        T visitTwentyOne();

        T visitTwentyTwo();

        T visitTwentyThree();

        T visitTwentyFour();

        T visitTwentyFive();

        T visitTwentySix();

        T visitTwentySeven();

        T visitTwentyEight();

        T visitTwentyNine();

        T visitThirty();

        T visitThirtyOne();

        T visitThirtyTwo();

        T visitThirtyThree();

        T visitThirtyFour();

        T visitThirtyFive();

        T visitThirtySix();

        T visitThirtySeven();

        T visitThirtyEight();

        T visitThirtyNine();

        T visitForty();

        T visitFortyOne();

        T visitFortyTwo();

        T visitFortyThree();

        T visitFortyFour();

        T visitFortyFive();

        T visitFortySix();

        T visitFortySeven();

        T visitFortyEight();

        T visitFortyNine();

        T visitFifty();

        T visitFiftyOne();

        T visitUnknown(String unknownValue);
    }
}
