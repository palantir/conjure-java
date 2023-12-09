package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.errorprone.annotations.Immutable;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

/**
 * This class is used instead of a native enum to support unknown values. Rather than throw an exception, the
 * {@link EnumWithFiftyValues#valueOf} method defaults to a new instantiation of {@link EnumWithFiftyValues} where
 * {@link EnumWithFiftyValues#get} will return {@link EnumWithFiftyValues.Value#UNKNOWN}.
 *
 * <p>For example, {@code EnumWithFiftyValues.valueOf("corrupted value").get()} will return
 * {@link EnumWithFiftyValues.Value#UNKNOWN}, but {@link EnumWithFiftyValues#toString} will return "corrupted value".
 *
 * <p>There is no method to access all instantiations of this class, since they cannot be known at compile time.
 */
@Generated("com.palantir.conjure.java.types.EnumGenerator")
@Safe
@Immutable
public final class EnumWithFiftyValues {
    public static final EnumWithFiftyValues ONE = new EnumWithFiftyValues(Value.ONE, "ONE");

    public static final EnumWithFiftyValues TWO = new EnumWithFiftyValues(Value.TWO, "TWO");

    public static final EnumWithFiftyValues THREE = new EnumWithFiftyValues(Value.THREE, "THREE");

    public static final EnumWithFiftyValues FOUR = new EnumWithFiftyValues(Value.FOUR, "FOUR");

    public static final EnumWithFiftyValues FIVE = new EnumWithFiftyValues(Value.FIVE, "FIVE");

    public static final EnumWithFiftyValues SIX = new EnumWithFiftyValues(Value.SIX, "SIX");

    public static final EnumWithFiftyValues SEVEN = new EnumWithFiftyValues(Value.SEVEN, "SEVEN");

    public static final EnumWithFiftyValues EIGHT = new EnumWithFiftyValues(Value.EIGHT, "EIGHT");

    public static final EnumWithFiftyValues NINE = new EnumWithFiftyValues(Value.NINE, "NINE");

    public static final EnumWithFiftyValues TEN = new EnumWithFiftyValues(Value.TEN, "TEN");

    public static final EnumWithFiftyValues ELEVEN = new EnumWithFiftyValues(Value.ELEVEN, "ELEVEN");

    public static final EnumWithFiftyValues TWELVE = new EnumWithFiftyValues(Value.TWELVE, "TWELVE");

    public static final EnumWithFiftyValues THIRTEEN = new EnumWithFiftyValues(Value.THIRTEEN, "THIRTEEN");

    public static final EnumWithFiftyValues FOURTEEN = new EnumWithFiftyValues(Value.FOURTEEN, "FOURTEEN");

    public static final EnumWithFiftyValues FIFTEEN = new EnumWithFiftyValues(Value.FIFTEEN, "FIFTEEN");

    public static final EnumWithFiftyValues SIXTEEN = new EnumWithFiftyValues(Value.SIXTEEN, "SIXTEEN");

    public static final EnumWithFiftyValues SEVENTEEN = new EnumWithFiftyValues(Value.SEVENTEEN, "SEVENTEEN");

    public static final EnumWithFiftyValues EIGHTEEN = new EnumWithFiftyValues(Value.EIGHTEEN, "EIGHTEEN");

    public static final EnumWithFiftyValues NINETEEN = new EnumWithFiftyValues(Value.NINETEEN, "NINETEEN");

    public static final EnumWithFiftyValues TWENTY = new EnumWithFiftyValues(Value.TWENTY, "TWENTY");

    public static final EnumWithFiftyValues TWENTY_ONE = new EnumWithFiftyValues(Value.TWENTY_ONE, "TWENTY_ONE");

    public static final EnumWithFiftyValues TWENTY_TWO = new EnumWithFiftyValues(Value.TWENTY_TWO, "TWENTY_TWO");

    public static final EnumWithFiftyValues TWENTY_THREE = new EnumWithFiftyValues(Value.TWENTY_THREE, "TWENTY_THREE");

    public static final EnumWithFiftyValues TWENTY_FOUR = new EnumWithFiftyValues(Value.TWENTY_FOUR, "TWENTY_FOUR");

    public static final EnumWithFiftyValues TWENTY_FIVE = new EnumWithFiftyValues(Value.TWENTY_FIVE, "TWENTY_FIVE");

    public static final EnumWithFiftyValues TWENTY_SIX = new EnumWithFiftyValues(Value.TWENTY_SIX, "TWENTY_SIX");

    public static final EnumWithFiftyValues TWENTY_SEVEN = new EnumWithFiftyValues(Value.TWENTY_SEVEN, "TWENTY_SEVEN");

    public static final EnumWithFiftyValues TWENTY_EIGHT = new EnumWithFiftyValues(Value.TWENTY_EIGHT, "TWENTY_EIGHT");

    public static final EnumWithFiftyValues TWENTY_NINE = new EnumWithFiftyValues(Value.TWENTY_NINE, "TWENTY_NINE");

    public static final EnumWithFiftyValues THIRTY = new EnumWithFiftyValues(Value.THIRTY, "THIRTY");

    public static final EnumWithFiftyValues THIRTY_ONE = new EnumWithFiftyValues(Value.THIRTY_ONE, "THIRTY_ONE");

    public static final EnumWithFiftyValues THIRTY_TWO = new EnumWithFiftyValues(Value.THIRTY_TWO, "THIRTY_TWO");

    public static final EnumWithFiftyValues THIRTY_THREE = new EnumWithFiftyValues(Value.THIRTY_THREE, "THIRTY_THREE");

    public static final EnumWithFiftyValues THIRTY_FOUR = new EnumWithFiftyValues(Value.THIRTY_FOUR, "THIRTY_FOUR");

    public static final EnumWithFiftyValues THIRTY_FIVE = new EnumWithFiftyValues(Value.THIRTY_FIVE, "THIRTY_FIVE");

    public static final EnumWithFiftyValues THIRTY_SIX = new EnumWithFiftyValues(Value.THIRTY_SIX, "THIRTY_SIX");

    public static final EnumWithFiftyValues THIRTY_SEVEN = new EnumWithFiftyValues(Value.THIRTY_SEVEN, "THIRTY_SEVEN");

    public static final EnumWithFiftyValues THIRTY_EIGHT = new EnumWithFiftyValues(Value.THIRTY_EIGHT, "THIRTY_EIGHT");

    public static final EnumWithFiftyValues THIRTY_NINE = new EnumWithFiftyValues(Value.THIRTY_NINE, "THIRTY_NINE");

    public static final EnumWithFiftyValues FORTY = new EnumWithFiftyValues(Value.FORTY, "FORTY");

    public static final EnumWithFiftyValues FORTY_ONE = new EnumWithFiftyValues(Value.FORTY_ONE, "FORTY_ONE");

    public static final EnumWithFiftyValues FORTY_TWO = new EnumWithFiftyValues(Value.FORTY_TWO, "FORTY_TWO");

    public static final EnumWithFiftyValues FORTY_THREE = new EnumWithFiftyValues(Value.FORTY_THREE, "FORTY_THREE");

    public static final EnumWithFiftyValues FORTY_FOUR = new EnumWithFiftyValues(Value.FORTY_FOUR, "FORTY_FOUR");

    public static final EnumWithFiftyValues FORTY_FIVE = new EnumWithFiftyValues(Value.FORTY_FIVE, "FORTY_FIVE");

    public static final EnumWithFiftyValues FORTY_SIX = new EnumWithFiftyValues(Value.FORTY_SIX, "FORTY_SIX");

    public static final EnumWithFiftyValues FORTY_SEVEN = new EnumWithFiftyValues(Value.FORTY_SEVEN, "FORTY_SEVEN");

    public static final EnumWithFiftyValues FORTY_EIGHT = new EnumWithFiftyValues(Value.FORTY_EIGHT, "FORTY_EIGHT");

    public static final EnumWithFiftyValues FORTY_NINE = new EnumWithFiftyValues(Value.FORTY_NINE, "FORTY_NINE");

    public static final EnumWithFiftyValues FIFTY = new EnumWithFiftyValues(Value.FIFTY, "FIFTY");

    private static final List<EnumWithFiftyValues> values = Collections.unmodifiableList(Arrays.asList(
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
            FIFTY));

    private final Value value;

    private final String string;

    private EnumWithFiftyValues(Value value, String string) {
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
                        && other instanceof EnumWithFiftyValues
                        && this.string.equals(((EnumWithFiftyValues) other).string));
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static EnumWithFiftyValues valueOf(@Nonnull @Safe String value) {
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
            default:
                return new EnumWithFiftyValues(Value.UNKNOWN, upperCasedValue);
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
            default:
                return visitor.visitUnknown(string);
        }
    }

    public static List<EnumWithFiftyValues> values() {
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

        T visitUnknown(String unknownValue);

        static <T> OneStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static final class VisitorBuilder<T>
            implements OneStageVisitorBuilder<T>,
                    TwoStageVisitorBuilder<T>,
                    ThreeStageVisitorBuilder<T>,
                    FourStageVisitorBuilder<T>,
                    FiveStageVisitorBuilder<T>,
                    SixStageVisitorBuilder<T>,
                    SevenStageVisitorBuilder<T>,
                    EightStageVisitorBuilder<T>,
                    NineStageVisitorBuilder<T>,
                    TenStageVisitorBuilder<T>,
                    ElevenStageVisitorBuilder<T>,
                    TwelveStageVisitorBuilder<T>,
                    ThirteenStageVisitorBuilder<T>,
                    FourteenStageVisitorBuilder<T>,
                    FifteenStageVisitorBuilder<T>,
                    SixteenStageVisitorBuilder<T>,
                    SeventeenStageVisitorBuilder<T>,
                    EighteenStageVisitorBuilder<T>,
                    NineteenStageVisitorBuilder<T>,
                    TwentyStageVisitorBuilder<T>,
                    TwentyOneStageVisitorBuilder<T>,
                    TwentyTwoStageVisitorBuilder<T>,
                    TwentyThreeStageVisitorBuilder<T>,
                    TwentyFourStageVisitorBuilder<T>,
                    TwentyFiveStageVisitorBuilder<T>,
                    TwentySixStageVisitorBuilder<T>,
                    TwentySevenStageVisitorBuilder<T>,
                    TwentyEightStageVisitorBuilder<T>,
                    TwentyNineStageVisitorBuilder<T>,
                    ThirtyStageVisitorBuilder<T>,
                    ThirtyOneStageVisitorBuilder<T>,
                    ThirtyTwoStageVisitorBuilder<T>,
                    ThirtyThreeStageVisitorBuilder<T>,
                    ThirtyFourStageVisitorBuilder<T>,
                    ThirtyFiveStageVisitorBuilder<T>,
                    ThirtySixStageVisitorBuilder<T>,
                    ThirtySevenStageVisitorBuilder<T>,
                    ThirtyEightStageVisitorBuilder<T>,
                    ThirtyNineStageVisitorBuilder<T>,
                    FortyStageVisitorBuilder<T>,
                    FortyOneStageVisitorBuilder<T>,
                    FortyTwoStageVisitorBuilder<T>,
                    FortyThreeStageVisitorBuilder<T>,
                    FortyFourStageVisitorBuilder<T>,
                    FortyFiveStageVisitorBuilder<T>,
                    FortySixStageVisitorBuilder<T>,
                    FortySevenStageVisitorBuilder<T>,
                    FortyEightStageVisitorBuilder<T>,
                    FortyNineStageVisitorBuilder<T>,
                    FiftyStageVisitorBuilder<T>,
                    UnknownStageVisitorBuilder<T>,
                    Completed_StageVisitorBuilder<T> {
        private Supplier<T> oneVisitor;

        private Supplier<T> twoVisitor;

        private Supplier<T> threeVisitor;

        private Supplier<T> fourVisitor;

        private Supplier<T> fiveVisitor;

        private Supplier<T> sixVisitor;

        private Supplier<T> sevenVisitor;

        private Supplier<T> eightVisitor;

        private Supplier<T> nineVisitor;

        private Supplier<T> tenVisitor;

        private Supplier<T> elevenVisitor;

        private Supplier<T> twelveVisitor;

        private Supplier<T> thirteenVisitor;

        private Supplier<T> fourteenVisitor;

        private Supplier<T> fifteenVisitor;

        private Supplier<T> sixteenVisitor;

        private Supplier<T> seventeenVisitor;

        private Supplier<T> eighteenVisitor;

        private Supplier<T> nineteenVisitor;

        private Supplier<T> twentyVisitor;

        private Supplier<T> twentyOneVisitor;

        private Supplier<T> twentyTwoVisitor;

        private Supplier<T> twentyThreeVisitor;

        private Supplier<T> twentyFourVisitor;

        private Supplier<T> twentyFiveVisitor;

        private Supplier<T> twentySixVisitor;

        private Supplier<T> twentySevenVisitor;

        private Supplier<T> twentyEightVisitor;

        private Supplier<T> twentyNineVisitor;

        private Supplier<T> thirtyVisitor;

        private Supplier<T> thirtyOneVisitor;

        private Supplier<T> thirtyTwoVisitor;

        private Supplier<T> thirtyThreeVisitor;

        private Supplier<T> thirtyFourVisitor;

        private Supplier<T> thirtyFiveVisitor;

        private Supplier<T> thirtySixVisitor;

        private Supplier<T> thirtySevenVisitor;

        private Supplier<T> thirtyEightVisitor;

        private Supplier<T> thirtyNineVisitor;

        private Supplier<T> fortyVisitor;

        private Supplier<T> fortyOneVisitor;

        private Supplier<T> fortyTwoVisitor;

        private Supplier<T> fortyThreeVisitor;

        private Supplier<T> fortyFourVisitor;

        private Supplier<T> fortyFiveVisitor;

        private Supplier<T> fortySixVisitor;

        private Supplier<T> fortySevenVisitor;

        private Supplier<T> fortyEightVisitor;

        private Supplier<T> fortyNineVisitor;

        private Supplier<T> fiftyVisitor;

        private Function<@Safe String, T> unknownVisitor;

        @Override
        public TwoStageVisitorBuilder<T> visitOne(@Nonnull Supplier<T> oneVisitor) {
            Preconditions.checkNotNull(oneVisitor, "oneVisitor cannot be null");
            this.oneVisitor = oneVisitor;
            return this;
        }

        @Override
        public ThreeStageVisitorBuilder<T> visitTwo(@Nonnull Supplier<T> twoVisitor) {
            Preconditions.checkNotNull(twoVisitor, "twoVisitor cannot be null");
            this.twoVisitor = twoVisitor;
            return this;
        }

        @Override
        public FourStageVisitorBuilder<T> visitThree(@Nonnull Supplier<T> threeVisitor) {
            Preconditions.checkNotNull(threeVisitor, "threeVisitor cannot be null");
            this.threeVisitor = threeVisitor;
            return this;
        }

        @Override
        public FiveStageVisitorBuilder<T> visitFour(@Nonnull Supplier<T> fourVisitor) {
            Preconditions.checkNotNull(fourVisitor, "fourVisitor cannot be null");
            this.fourVisitor = fourVisitor;
            return this;
        }

        @Override
        public SixStageVisitorBuilder<T> visitFive(@Nonnull Supplier<T> fiveVisitor) {
            Preconditions.checkNotNull(fiveVisitor, "fiveVisitor cannot be null");
            this.fiveVisitor = fiveVisitor;
            return this;
        }

        @Override
        public SevenStageVisitorBuilder<T> visitSix(@Nonnull Supplier<T> sixVisitor) {
            Preconditions.checkNotNull(sixVisitor, "sixVisitor cannot be null");
            this.sixVisitor = sixVisitor;
            return this;
        }

        @Override
        public EightStageVisitorBuilder<T> visitSeven(@Nonnull Supplier<T> sevenVisitor) {
            Preconditions.checkNotNull(sevenVisitor, "sevenVisitor cannot be null");
            this.sevenVisitor = sevenVisitor;
            return this;
        }

        @Override
        public NineStageVisitorBuilder<T> visitEight(@Nonnull Supplier<T> eightVisitor) {
            Preconditions.checkNotNull(eightVisitor, "eightVisitor cannot be null");
            this.eightVisitor = eightVisitor;
            return this;
        }

        @Override
        public TenStageVisitorBuilder<T> visitNine(@Nonnull Supplier<T> nineVisitor) {
            Preconditions.checkNotNull(nineVisitor, "nineVisitor cannot be null");
            this.nineVisitor = nineVisitor;
            return this;
        }

        @Override
        public ElevenStageVisitorBuilder<T> visitTen(@Nonnull Supplier<T> tenVisitor) {
            Preconditions.checkNotNull(tenVisitor, "tenVisitor cannot be null");
            this.tenVisitor = tenVisitor;
            return this;
        }

        @Override
        public TwelveStageVisitorBuilder<T> visitEleven(@Nonnull Supplier<T> elevenVisitor) {
            Preconditions.checkNotNull(elevenVisitor, "elevenVisitor cannot be null");
            this.elevenVisitor = elevenVisitor;
            return this;
        }

        @Override
        public ThirteenStageVisitorBuilder<T> visitTwelve(@Nonnull Supplier<T> twelveVisitor) {
            Preconditions.checkNotNull(twelveVisitor, "twelveVisitor cannot be null");
            this.twelveVisitor = twelveVisitor;
            return this;
        }

        @Override
        public FourteenStageVisitorBuilder<T> visitThirteen(@Nonnull Supplier<T> thirteenVisitor) {
            Preconditions.checkNotNull(thirteenVisitor, "thirteenVisitor cannot be null");
            this.thirteenVisitor = thirteenVisitor;
            return this;
        }

        @Override
        public FifteenStageVisitorBuilder<T> visitFourteen(@Nonnull Supplier<T> fourteenVisitor) {
            Preconditions.checkNotNull(fourteenVisitor, "fourteenVisitor cannot be null");
            this.fourteenVisitor = fourteenVisitor;
            return this;
        }

        @Override
        public SixteenStageVisitorBuilder<T> visitFifteen(@Nonnull Supplier<T> fifteenVisitor) {
            Preconditions.checkNotNull(fifteenVisitor, "fifteenVisitor cannot be null");
            this.fifteenVisitor = fifteenVisitor;
            return this;
        }

        @Override
        public SeventeenStageVisitorBuilder<T> visitSixteen(@Nonnull Supplier<T> sixteenVisitor) {
            Preconditions.checkNotNull(sixteenVisitor, "sixteenVisitor cannot be null");
            this.sixteenVisitor = sixteenVisitor;
            return this;
        }

        @Override
        public EighteenStageVisitorBuilder<T> visitSeventeen(@Nonnull Supplier<T> seventeenVisitor) {
            Preconditions.checkNotNull(seventeenVisitor, "seventeenVisitor cannot be null");
            this.seventeenVisitor = seventeenVisitor;
            return this;
        }

        @Override
        public NineteenStageVisitorBuilder<T> visitEighteen(@Nonnull Supplier<T> eighteenVisitor) {
            Preconditions.checkNotNull(eighteenVisitor, "eighteenVisitor cannot be null");
            this.eighteenVisitor = eighteenVisitor;
            return this;
        }

        @Override
        public TwentyStageVisitorBuilder<T> visitNineteen(@Nonnull Supplier<T> nineteenVisitor) {
            Preconditions.checkNotNull(nineteenVisitor, "nineteenVisitor cannot be null");
            this.nineteenVisitor = nineteenVisitor;
            return this;
        }

        @Override
        public TwentyOneStageVisitorBuilder<T> visitTwenty(@Nonnull Supplier<T> twentyVisitor) {
            Preconditions.checkNotNull(twentyVisitor, "twentyVisitor cannot be null");
            this.twentyVisitor = twentyVisitor;
            return this;
        }

        @Override
        public TwentyTwoStageVisitorBuilder<T> visitTwentyOne(@Nonnull Supplier<T> twentyOneVisitor) {
            Preconditions.checkNotNull(twentyOneVisitor, "twentyOneVisitor cannot be null");
            this.twentyOneVisitor = twentyOneVisitor;
            return this;
        }

        @Override
        public TwentyThreeStageVisitorBuilder<T> visitTwentyTwo(@Nonnull Supplier<T> twentyTwoVisitor) {
            Preconditions.checkNotNull(twentyTwoVisitor, "twentyTwoVisitor cannot be null");
            this.twentyTwoVisitor = twentyTwoVisitor;
            return this;
        }

        @Override
        public TwentyFourStageVisitorBuilder<T> visitTwentyThree(@Nonnull Supplier<T> twentyThreeVisitor) {
            Preconditions.checkNotNull(twentyThreeVisitor, "twentyThreeVisitor cannot be null");
            this.twentyThreeVisitor = twentyThreeVisitor;
            return this;
        }

        @Override
        public TwentyFiveStageVisitorBuilder<T> visitTwentyFour(@Nonnull Supplier<T> twentyFourVisitor) {
            Preconditions.checkNotNull(twentyFourVisitor, "twentyFourVisitor cannot be null");
            this.twentyFourVisitor = twentyFourVisitor;
            return this;
        }

        @Override
        public TwentySixStageVisitorBuilder<T> visitTwentyFive(@Nonnull Supplier<T> twentyFiveVisitor) {
            Preconditions.checkNotNull(twentyFiveVisitor, "twentyFiveVisitor cannot be null");
            this.twentyFiveVisitor = twentyFiveVisitor;
            return this;
        }

        @Override
        public TwentySevenStageVisitorBuilder<T> visitTwentySix(@Nonnull Supplier<T> twentySixVisitor) {
            Preconditions.checkNotNull(twentySixVisitor, "twentySixVisitor cannot be null");
            this.twentySixVisitor = twentySixVisitor;
            return this;
        }

        @Override
        public TwentyEightStageVisitorBuilder<T> visitTwentySeven(@Nonnull Supplier<T> twentySevenVisitor) {
            Preconditions.checkNotNull(twentySevenVisitor, "twentySevenVisitor cannot be null");
            this.twentySevenVisitor = twentySevenVisitor;
            return this;
        }

        @Override
        public TwentyNineStageVisitorBuilder<T> visitTwentyEight(@Nonnull Supplier<T> twentyEightVisitor) {
            Preconditions.checkNotNull(twentyEightVisitor, "twentyEightVisitor cannot be null");
            this.twentyEightVisitor = twentyEightVisitor;
            return this;
        }

        @Override
        public ThirtyStageVisitorBuilder<T> visitTwentyNine(@Nonnull Supplier<T> twentyNineVisitor) {
            Preconditions.checkNotNull(twentyNineVisitor, "twentyNineVisitor cannot be null");
            this.twentyNineVisitor = twentyNineVisitor;
            return this;
        }

        @Override
        public ThirtyOneStageVisitorBuilder<T> visitThirty(@Nonnull Supplier<T> thirtyVisitor) {
            Preconditions.checkNotNull(thirtyVisitor, "thirtyVisitor cannot be null");
            this.thirtyVisitor = thirtyVisitor;
            return this;
        }

        @Override
        public ThirtyTwoStageVisitorBuilder<T> visitThirtyOne(@Nonnull Supplier<T> thirtyOneVisitor) {
            Preconditions.checkNotNull(thirtyOneVisitor, "thirtyOneVisitor cannot be null");
            this.thirtyOneVisitor = thirtyOneVisitor;
            return this;
        }

        @Override
        public ThirtyThreeStageVisitorBuilder<T> visitThirtyTwo(@Nonnull Supplier<T> thirtyTwoVisitor) {
            Preconditions.checkNotNull(thirtyTwoVisitor, "thirtyTwoVisitor cannot be null");
            this.thirtyTwoVisitor = thirtyTwoVisitor;
            return this;
        }

        @Override
        public ThirtyFourStageVisitorBuilder<T> visitThirtyThree(@Nonnull Supplier<T> thirtyThreeVisitor) {
            Preconditions.checkNotNull(thirtyThreeVisitor, "thirtyThreeVisitor cannot be null");
            this.thirtyThreeVisitor = thirtyThreeVisitor;
            return this;
        }

        @Override
        public ThirtyFiveStageVisitorBuilder<T> visitThirtyFour(@Nonnull Supplier<T> thirtyFourVisitor) {
            Preconditions.checkNotNull(thirtyFourVisitor, "thirtyFourVisitor cannot be null");
            this.thirtyFourVisitor = thirtyFourVisitor;
            return this;
        }

        @Override
        public ThirtySixStageVisitorBuilder<T> visitThirtyFive(@Nonnull Supplier<T> thirtyFiveVisitor) {
            Preconditions.checkNotNull(thirtyFiveVisitor, "thirtyFiveVisitor cannot be null");
            this.thirtyFiveVisitor = thirtyFiveVisitor;
            return this;
        }

        @Override
        public ThirtySevenStageVisitorBuilder<T> visitThirtySix(@Nonnull Supplier<T> thirtySixVisitor) {
            Preconditions.checkNotNull(thirtySixVisitor, "thirtySixVisitor cannot be null");
            this.thirtySixVisitor = thirtySixVisitor;
            return this;
        }

        @Override
        public ThirtyEightStageVisitorBuilder<T> visitThirtySeven(@Nonnull Supplier<T> thirtySevenVisitor) {
            Preconditions.checkNotNull(thirtySevenVisitor, "thirtySevenVisitor cannot be null");
            this.thirtySevenVisitor = thirtySevenVisitor;
            return this;
        }

        @Override
        public ThirtyNineStageVisitorBuilder<T> visitThirtyEight(@Nonnull Supplier<T> thirtyEightVisitor) {
            Preconditions.checkNotNull(thirtyEightVisitor, "thirtyEightVisitor cannot be null");
            this.thirtyEightVisitor = thirtyEightVisitor;
            return this;
        }

        @Override
        public FortyStageVisitorBuilder<T> visitThirtyNine(@Nonnull Supplier<T> thirtyNineVisitor) {
            Preconditions.checkNotNull(thirtyNineVisitor, "thirtyNineVisitor cannot be null");
            this.thirtyNineVisitor = thirtyNineVisitor;
            return this;
        }

        @Override
        public FortyOneStageVisitorBuilder<T> visitForty(@Nonnull Supplier<T> fortyVisitor) {
            Preconditions.checkNotNull(fortyVisitor, "fortyVisitor cannot be null");
            this.fortyVisitor = fortyVisitor;
            return this;
        }

        @Override
        public FortyTwoStageVisitorBuilder<T> visitFortyOne(@Nonnull Supplier<T> fortyOneVisitor) {
            Preconditions.checkNotNull(fortyOneVisitor, "fortyOneVisitor cannot be null");
            this.fortyOneVisitor = fortyOneVisitor;
            return this;
        }

        @Override
        public FortyThreeStageVisitorBuilder<T> visitFortyTwo(@Nonnull Supplier<T> fortyTwoVisitor) {
            Preconditions.checkNotNull(fortyTwoVisitor, "fortyTwoVisitor cannot be null");
            this.fortyTwoVisitor = fortyTwoVisitor;
            return this;
        }

        @Override
        public FortyFourStageVisitorBuilder<T> visitFortyThree(@Nonnull Supplier<T> fortyThreeVisitor) {
            Preconditions.checkNotNull(fortyThreeVisitor, "fortyThreeVisitor cannot be null");
            this.fortyThreeVisitor = fortyThreeVisitor;
            return this;
        }

        @Override
        public FortyFiveStageVisitorBuilder<T> visitFortyFour(@Nonnull Supplier<T> fortyFourVisitor) {
            Preconditions.checkNotNull(fortyFourVisitor, "fortyFourVisitor cannot be null");
            this.fortyFourVisitor = fortyFourVisitor;
            return this;
        }

        @Override
        public FortySixStageVisitorBuilder<T> visitFortyFive(@Nonnull Supplier<T> fortyFiveVisitor) {
            Preconditions.checkNotNull(fortyFiveVisitor, "fortyFiveVisitor cannot be null");
            this.fortyFiveVisitor = fortyFiveVisitor;
            return this;
        }

        @Override
        public FortySevenStageVisitorBuilder<T> visitFortySix(@Nonnull Supplier<T> fortySixVisitor) {
            Preconditions.checkNotNull(fortySixVisitor, "fortySixVisitor cannot be null");
            this.fortySixVisitor = fortySixVisitor;
            return this;
        }

        @Override
        public FortyEightStageVisitorBuilder<T> visitFortySeven(@Nonnull Supplier<T> fortySevenVisitor) {
            Preconditions.checkNotNull(fortySevenVisitor, "fortySevenVisitor cannot be null");
            this.fortySevenVisitor = fortySevenVisitor;
            return this;
        }

        @Override
        public FortyNineStageVisitorBuilder<T> visitFortyEight(@Nonnull Supplier<T> fortyEightVisitor) {
            Preconditions.checkNotNull(fortyEightVisitor, "fortyEightVisitor cannot be null");
            this.fortyEightVisitor = fortyEightVisitor;
            return this;
        }

        @Override
        public FiftyStageVisitorBuilder<T> visitFortyNine(@Nonnull Supplier<T> fortyNineVisitor) {
            Preconditions.checkNotNull(fortyNineVisitor, "fortyNineVisitor cannot be null");
            this.fortyNineVisitor = fortyNineVisitor;
            return this;
        }

        @Override
        public UnknownStageVisitorBuilder<T> visitFifty(@Nonnull Supplier<T> fiftyVisitor) {
            Preconditions.checkNotNull(fiftyVisitor, "fiftyVisitor cannot be null");
            this.fiftyVisitor = fiftyVisitor;
            return this;
        }

        @Override
        public Completed_StageVisitorBuilder<T> visitUnknown(@Nonnull Function<@Safe String, T> unknownVisitor) {
            Preconditions.checkNotNull(unknownVisitor, "unknownVisitor cannot be null");
            this.unknownVisitor = unknownType -> unknownVisitor.apply(unknownType);
            return this;
        }

        @Override
        public Completed_StageVisitorBuilder<T> throwOnUnknown() {
            this.unknownVisitor = unknownType -> {
                throw new SafeIllegalArgumentException(
                        "Unknown variant of the 'EnumWithFiftyValues' union", SafeArg.of("unknownType", unknownType));
            };
            return this;
        }

        @Override
        public Visitor<T> build() {
            final Supplier<T> oneVisitor = this.oneVisitor;
            final Supplier<T> twoVisitor = this.twoVisitor;
            final Supplier<T> threeVisitor = this.threeVisitor;
            final Supplier<T> fourVisitor = this.fourVisitor;
            final Supplier<T> fiveVisitor = this.fiveVisitor;
            final Supplier<T> sixVisitor = this.sixVisitor;
            final Supplier<T> sevenVisitor = this.sevenVisitor;
            final Supplier<T> eightVisitor = this.eightVisitor;
            final Supplier<T> nineVisitor = this.nineVisitor;
            final Supplier<T> tenVisitor = this.tenVisitor;
            final Supplier<T> elevenVisitor = this.elevenVisitor;
            final Supplier<T> twelveVisitor = this.twelveVisitor;
            final Supplier<T> thirteenVisitor = this.thirteenVisitor;
            final Supplier<T> fourteenVisitor = this.fourteenVisitor;
            final Supplier<T> fifteenVisitor = this.fifteenVisitor;
            final Supplier<T> sixteenVisitor = this.sixteenVisitor;
            final Supplier<T> seventeenVisitor = this.seventeenVisitor;
            final Supplier<T> eighteenVisitor = this.eighteenVisitor;
            final Supplier<T> nineteenVisitor = this.nineteenVisitor;
            final Supplier<T> twentyVisitor = this.twentyVisitor;
            final Supplier<T> twentyOneVisitor = this.twentyOneVisitor;
            final Supplier<T> twentyTwoVisitor = this.twentyTwoVisitor;
            final Supplier<T> twentyThreeVisitor = this.twentyThreeVisitor;
            final Supplier<T> twentyFourVisitor = this.twentyFourVisitor;
            final Supplier<T> twentyFiveVisitor = this.twentyFiveVisitor;
            final Supplier<T> twentySixVisitor = this.twentySixVisitor;
            final Supplier<T> twentySevenVisitor = this.twentySevenVisitor;
            final Supplier<T> twentyEightVisitor = this.twentyEightVisitor;
            final Supplier<T> twentyNineVisitor = this.twentyNineVisitor;
            final Supplier<T> thirtyVisitor = this.thirtyVisitor;
            final Supplier<T> thirtyOneVisitor = this.thirtyOneVisitor;
            final Supplier<T> thirtyTwoVisitor = this.thirtyTwoVisitor;
            final Supplier<T> thirtyThreeVisitor = this.thirtyThreeVisitor;
            final Supplier<T> thirtyFourVisitor = this.thirtyFourVisitor;
            final Supplier<T> thirtyFiveVisitor = this.thirtyFiveVisitor;
            final Supplier<T> thirtySixVisitor = this.thirtySixVisitor;
            final Supplier<T> thirtySevenVisitor = this.thirtySevenVisitor;
            final Supplier<T> thirtyEightVisitor = this.thirtyEightVisitor;
            final Supplier<T> thirtyNineVisitor = this.thirtyNineVisitor;
            final Supplier<T> fortyVisitor = this.fortyVisitor;
            final Supplier<T> fortyOneVisitor = this.fortyOneVisitor;
            final Supplier<T> fortyTwoVisitor = this.fortyTwoVisitor;
            final Supplier<T> fortyThreeVisitor = this.fortyThreeVisitor;
            final Supplier<T> fortyFourVisitor = this.fortyFourVisitor;
            final Supplier<T> fortyFiveVisitor = this.fortyFiveVisitor;
            final Supplier<T> fortySixVisitor = this.fortySixVisitor;
            final Supplier<T> fortySevenVisitor = this.fortySevenVisitor;
            final Supplier<T> fortyEightVisitor = this.fortyEightVisitor;
            final Supplier<T> fortyNineVisitor = this.fortyNineVisitor;
            final Supplier<T> fiftyVisitor = this.fiftyVisitor;
            final Function<@Safe String, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitOne() {
                    return oneVisitor.get();
                }

                @Override
                public T visitTwo() {
                    return twoVisitor.get();
                }

                @Override
                public T visitThree() {
                    return threeVisitor.get();
                }

                @Override
                public T visitFour() {
                    return fourVisitor.get();
                }

                @Override
                public T visitFive() {
                    return fiveVisitor.get();
                }

                @Override
                public T visitSix() {
                    return sixVisitor.get();
                }

                @Override
                public T visitSeven() {
                    return sevenVisitor.get();
                }

                @Override
                public T visitEight() {
                    return eightVisitor.get();
                }

                @Override
                public T visitNine() {
                    return nineVisitor.get();
                }

                @Override
                public T visitTen() {
                    return tenVisitor.get();
                }

                @Override
                public T visitEleven() {
                    return elevenVisitor.get();
                }

                @Override
                public T visitTwelve() {
                    return twelveVisitor.get();
                }

                @Override
                public T visitThirteen() {
                    return thirteenVisitor.get();
                }

                @Override
                public T visitFourteen() {
                    return fourteenVisitor.get();
                }

                @Override
                public T visitFifteen() {
                    return fifteenVisitor.get();
                }

                @Override
                public T visitSixteen() {
                    return sixteenVisitor.get();
                }

                @Override
                public T visitSeventeen() {
                    return seventeenVisitor.get();
                }

                @Override
                public T visitEighteen() {
                    return eighteenVisitor.get();
                }

                @Override
                public T visitNineteen() {
                    return nineteenVisitor.get();
                }

                @Override
                public T visitTwenty() {
                    return twentyVisitor.get();
                }

                @Override
                public T visitTwentyOne() {
                    return twentyOneVisitor.get();
                }

                @Override
                public T visitTwentyTwo() {
                    return twentyTwoVisitor.get();
                }

                @Override
                public T visitTwentyThree() {
                    return twentyThreeVisitor.get();
                }

                @Override
                public T visitTwentyFour() {
                    return twentyFourVisitor.get();
                }

                @Override
                public T visitTwentyFive() {
                    return twentyFiveVisitor.get();
                }

                @Override
                public T visitTwentySix() {
                    return twentySixVisitor.get();
                }

                @Override
                public T visitTwentySeven() {
                    return twentySevenVisitor.get();
                }

                @Override
                public T visitTwentyEight() {
                    return twentyEightVisitor.get();
                }

                @Override
                public T visitTwentyNine() {
                    return twentyNineVisitor.get();
                }

                @Override
                public T visitThirty() {
                    return thirtyVisitor.get();
                }

                @Override
                public T visitThirtyOne() {
                    return thirtyOneVisitor.get();
                }

                @Override
                public T visitThirtyTwo() {
                    return thirtyTwoVisitor.get();
                }

                @Override
                public T visitThirtyThree() {
                    return thirtyThreeVisitor.get();
                }

                @Override
                public T visitThirtyFour() {
                    return thirtyFourVisitor.get();
                }

                @Override
                public T visitThirtyFive() {
                    return thirtyFiveVisitor.get();
                }

                @Override
                public T visitThirtySix() {
                    return thirtySixVisitor.get();
                }

                @Override
                public T visitThirtySeven() {
                    return thirtySevenVisitor.get();
                }

                @Override
                public T visitThirtyEight() {
                    return thirtyEightVisitor.get();
                }

                @Override
                public T visitThirtyNine() {
                    return thirtyNineVisitor.get();
                }

                @Override
                public T visitForty() {
                    return fortyVisitor.get();
                }

                @Override
                public T visitFortyOne() {
                    return fortyOneVisitor.get();
                }

                @Override
                public T visitFortyTwo() {
                    return fortyTwoVisitor.get();
                }

                @Override
                public T visitFortyThree() {
                    return fortyThreeVisitor.get();
                }

                @Override
                public T visitFortyFour() {
                    return fortyFourVisitor.get();
                }

                @Override
                public T visitFortyFive() {
                    return fortyFiveVisitor.get();
                }

                @Override
                public T visitFortySix() {
                    return fortySixVisitor.get();
                }

                @Override
                public T visitFortySeven() {
                    return fortySevenVisitor.get();
                }

                @Override
                public T visitFortyEight() {
                    return fortyEightVisitor.get();
                }

                @Override
                public T visitFortyNine() {
                    return fortyNineVisitor.get();
                }

                @Override
                public T visitFifty() {
                    return fiftyVisitor.get();
                }

                @Override
                public T visitUnknown(String unknownType) {
                    return unknownVisitor.apply(unknownType);
                }
            };
        }
    }

    public interface OneStageVisitorBuilder<T> {
        TwoStageVisitorBuilder<T> visitOne(@Nonnull Supplier<T> oneVisitor);
    }

    public interface TwoStageVisitorBuilder<T> {
        ThreeStageVisitorBuilder<T> visitTwo(@Nonnull Supplier<T> twoVisitor);
    }

    public interface ThreeStageVisitorBuilder<T> {
        FourStageVisitorBuilder<T> visitThree(@Nonnull Supplier<T> threeVisitor);
    }

    public interface FourStageVisitorBuilder<T> {
        FiveStageVisitorBuilder<T> visitFour(@Nonnull Supplier<T> fourVisitor);
    }

    public interface FiveStageVisitorBuilder<T> {
        SixStageVisitorBuilder<T> visitFive(@Nonnull Supplier<T> fiveVisitor);
    }

    public interface SixStageVisitorBuilder<T> {
        SevenStageVisitorBuilder<T> visitSix(@Nonnull Supplier<T> sixVisitor);
    }

    public interface SevenStageVisitorBuilder<T> {
        EightStageVisitorBuilder<T> visitSeven(@Nonnull Supplier<T> sevenVisitor);
    }

    public interface EightStageVisitorBuilder<T> {
        NineStageVisitorBuilder<T> visitEight(@Nonnull Supplier<T> eightVisitor);
    }

    public interface NineStageVisitorBuilder<T> {
        TenStageVisitorBuilder<T> visitNine(@Nonnull Supplier<T> nineVisitor);
    }

    public interface TenStageVisitorBuilder<T> {
        ElevenStageVisitorBuilder<T> visitTen(@Nonnull Supplier<T> tenVisitor);
    }

    public interface ElevenStageVisitorBuilder<T> {
        TwelveStageVisitorBuilder<T> visitEleven(@Nonnull Supplier<T> elevenVisitor);
    }

    public interface TwelveStageVisitorBuilder<T> {
        ThirteenStageVisitorBuilder<T> visitTwelve(@Nonnull Supplier<T> twelveVisitor);
    }

    public interface ThirteenStageVisitorBuilder<T> {
        FourteenStageVisitorBuilder<T> visitThirteen(@Nonnull Supplier<T> thirteenVisitor);
    }

    public interface FourteenStageVisitorBuilder<T> {
        FifteenStageVisitorBuilder<T> visitFourteen(@Nonnull Supplier<T> fourteenVisitor);
    }

    public interface FifteenStageVisitorBuilder<T> {
        SixteenStageVisitorBuilder<T> visitFifteen(@Nonnull Supplier<T> fifteenVisitor);
    }

    public interface SixteenStageVisitorBuilder<T> {
        SeventeenStageVisitorBuilder<T> visitSixteen(@Nonnull Supplier<T> sixteenVisitor);
    }

    public interface SeventeenStageVisitorBuilder<T> {
        EighteenStageVisitorBuilder<T> visitSeventeen(@Nonnull Supplier<T> seventeenVisitor);
    }

    public interface EighteenStageVisitorBuilder<T> {
        NineteenStageVisitorBuilder<T> visitEighteen(@Nonnull Supplier<T> eighteenVisitor);
    }

    public interface NineteenStageVisitorBuilder<T> {
        TwentyStageVisitorBuilder<T> visitNineteen(@Nonnull Supplier<T> nineteenVisitor);
    }

    public interface TwentyStageVisitorBuilder<T> {
        TwentyOneStageVisitorBuilder<T> visitTwenty(@Nonnull Supplier<T> twentyVisitor);
    }

    public interface TwentyOneStageVisitorBuilder<T> {
        TwentyTwoStageVisitorBuilder<T> visitTwentyOne(@Nonnull Supplier<T> twentyOneVisitor);
    }

    public interface TwentyTwoStageVisitorBuilder<T> {
        TwentyThreeStageVisitorBuilder<T> visitTwentyTwo(@Nonnull Supplier<T> twentyTwoVisitor);
    }

    public interface TwentyThreeStageVisitorBuilder<T> {
        TwentyFourStageVisitorBuilder<T> visitTwentyThree(@Nonnull Supplier<T> twentyThreeVisitor);
    }

    public interface TwentyFourStageVisitorBuilder<T> {
        TwentyFiveStageVisitorBuilder<T> visitTwentyFour(@Nonnull Supplier<T> twentyFourVisitor);
    }

    public interface TwentyFiveStageVisitorBuilder<T> {
        TwentySixStageVisitorBuilder<T> visitTwentyFive(@Nonnull Supplier<T> twentyFiveVisitor);
    }

    public interface TwentySixStageVisitorBuilder<T> {
        TwentySevenStageVisitorBuilder<T> visitTwentySix(@Nonnull Supplier<T> twentySixVisitor);
    }

    public interface TwentySevenStageVisitorBuilder<T> {
        TwentyEightStageVisitorBuilder<T> visitTwentySeven(@Nonnull Supplier<T> twentySevenVisitor);
    }

    public interface TwentyEightStageVisitorBuilder<T> {
        TwentyNineStageVisitorBuilder<T> visitTwentyEight(@Nonnull Supplier<T> twentyEightVisitor);
    }

    public interface TwentyNineStageVisitorBuilder<T> {
        ThirtyStageVisitorBuilder<T> visitTwentyNine(@Nonnull Supplier<T> twentyNineVisitor);
    }

    public interface ThirtyStageVisitorBuilder<T> {
        ThirtyOneStageVisitorBuilder<T> visitThirty(@Nonnull Supplier<T> thirtyVisitor);
    }

    public interface ThirtyOneStageVisitorBuilder<T> {
        ThirtyTwoStageVisitorBuilder<T> visitThirtyOne(@Nonnull Supplier<T> thirtyOneVisitor);
    }

    public interface ThirtyTwoStageVisitorBuilder<T> {
        ThirtyThreeStageVisitorBuilder<T> visitThirtyTwo(@Nonnull Supplier<T> thirtyTwoVisitor);
    }

    public interface ThirtyThreeStageVisitorBuilder<T> {
        ThirtyFourStageVisitorBuilder<T> visitThirtyThree(@Nonnull Supplier<T> thirtyThreeVisitor);
    }

    public interface ThirtyFourStageVisitorBuilder<T> {
        ThirtyFiveStageVisitorBuilder<T> visitThirtyFour(@Nonnull Supplier<T> thirtyFourVisitor);
    }

    public interface ThirtyFiveStageVisitorBuilder<T> {
        ThirtySixStageVisitorBuilder<T> visitThirtyFive(@Nonnull Supplier<T> thirtyFiveVisitor);
    }

    public interface ThirtySixStageVisitorBuilder<T> {
        ThirtySevenStageVisitorBuilder<T> visitThirtySix(@Nonnull Supplier<T> thirtySixVisitor);
    }

    public interface ThirtySevenStageVisitorBuilder<T> {
        ThirtyEightStageVisitorBuilder<T> visitThirtySeven(@Nonnull Supplier<T> thirtySevenVisitor);
    }

    public interface ThirtyEightStageVisitorBuilder<T> {
        ThirtyNineStageVisitorBuilder<T> visitThirtyEight(@Nonnull Supplier<T> thirtyEightVisitor);
    }

    public interface ThirtyNineStageVisitorBuilder<T> {
        FortyStageVisitorBuilder<T> visitThirtyNine(@Nonnull Supplier<T> thirtyNineVisitor);
    }

    public interface FortyStageVisitorBuilder<T> {
        FortyOneStageVisitorBuilder<T> visitForty(@Nonnull Supplier<T> fortyVisitor);
    }

    public interface FortyOneStageVisitorBuilder<T> {
        FortyTwoStageVisitorBuilder<T> visitFortyOne(@Nonnull Supplier<T> fortyOneVisitor);
    }

    public interface FortyTwoStageVisitorBuilder<T> {
        FortyThreeStageVisitorBuilder<T> visitFortyTwo(@Nonnull Supplier<T> fortyTwoVisitor);
    }

    public interface FortyThreeStageVisitorBuilder<T> {
        FortyFourStageVisitorBuilder<T> visitFortyThree(@Nonnull Supplier<T> fortyThreeVisitor);
    }

    public interface FortyFourStageVisitorBuilder<T> {
        FortyFiveStageVisitorBuilder<T> visitFortyFour(@Nonnull Supplier<T> fortyFourVisitor);
    }

    public interface FortyFiveStageVisitorBuilder<T> {
        FortySixStageVisitorBuilder<T> visitFortyFive(@Nonnull Supplier<T> fortyFiveVisitor);
    }

    public interface FortySixStageVisitorBuilder<T> {
        FortySevenStageVisitorBuilder<T> visitFortySix(@Nonnull Supplier<T> fortySixVisitor);
    }

    public interface FortySevenStageVisitorBuilder<T> {
        FortyEightStageVisitorBuilder<T> visitFortySeven(@Nonnull Supplier<T> fortySevenVisitor);
    }

    public interface FortyEightStageVisitorBuilder<T> {
        FortyNineStageVisitorBuilder<T> visitFortyEight(@Nonnull Supplier<T> fortyEightVisitor);
    }

    public interface FortyNineStageVisitorBuilder<T> {
        FiftyStageVisitorBuilder<T> visitFortyNine(@Nonnull Supplier<T> fortyNineVisitor);
    }

    public interface FiftyStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> visitFifty(@Nonnull Supplier<T> fiftyVisitor);
    }

    public interface UnknownStageVisitorBuilder<T> {
        Completed_StageVisitorBuilder<T> visitUnknown(@Nonnull Function<@Safe String, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> throwOnUnknown();
    }

    public interface Completed_StageVisitorBuilder<T> {
        Visitor<T> build();
    }
}
