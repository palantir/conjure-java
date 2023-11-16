package test.prefix.com.palantir.product;

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
 * This enumerates the numbers 1:2 also 100.
 *
 * <p>This class is used instead of a native enum to support unknown values. Rather than throw an exception, the
 * {@link EnumExample#valueOf} method defaults to a new instantiation of {@link EnumExample} where
 * {@link EnumExample#get} will return {@link EnumExample.Value#UNKNOWN}.
 *
 * <p>For example, {@code EnumExample.valueOf("corrupted value").get()} will return {@link EnumExample.Value#UNKNOWN},
 * but {@link EnumExample#toString} will return "corrupted value".
 *
 * <p>There is no method to access all instantiations of this class, since they cannot be known at compile time.
 */
@Generated("com.palantir.conjure.java.types.EnumGenerator")
@Safe
@Immutable
public final class EnumExample {
    public static final EnumExample ONE = new EnumExample(Value.ONE, "ONE");

    /** @deprecated Prefer <code>ONE</code> where possible. */
    @Deprecated
    public static final EnumExample TWO = new EnumExample(Value.TWO, "TWO");

    /**
     * Value of 100.
     *
     * @deprecated One is easier to manage.
     */
    @Deprecated
    public static final EnumExample ONE_HUNDRED = new EnumExample(Value.ONE_HUNDRED, "ONE_HUNDRED");

    @SuppressWarnings("deprecation")
    private static final List<EnumExample> values = Collections.unmodifiableList(Arrays.asList(ONE, TWO, ONE_HUNDRED));

    private final Value value;

    private final String string;

    private EnumExample(Value value, String string) {
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
                        && other instanceof EnumExample
                        && this.string.equals(((EnumExample) other).string));
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    @SuppressWarnings("deprecation")
    public static EnumExample valueOf(@Nonnull @Safe String value) {
        Preconditions.checkNotNull(value, "value cannot be null");
        String upperCasedValue = value.toUpperCase(Locale.ROOT);
        switch (upperCasedValue) {
            case "ONE":
                return ONE;
            case "TWO":
                return TWO;
            case "ONE_HUNDRED":
                return ONE_HUNDRED;
            default:
                return new EnumExample(Value.UNKNOWN, upperCasedValue);
        }
    }

    @SuppressWarnings("deprecation")
    public <T> T accept(Visitor<T> visitor) {
        switch (value) {
            case ONE:
                return visitor.visitOne();
            case TWO:
                return visitor.visitTwo();
            case ONE_HUNDRED:
                return visitor.visitOneHundred();
            default:
                return visitor.visitUnknown(string);
        }
    }

    public static List<EnumExample> values() {
        return values;
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public enum Value {
        ONE,

        /** @deprecated Prefer <code>ONE</code> where possible. */
        @Deprecated
        TWO,

        /**
         * Value of 100.
         *
         * @deprecated One is easier to manage.
         */
        @Deprecated
        ONE_HUNDRED,

        UNKNOWN
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public interface Visitor<T> {
        T visitOne();

        /** @deprecated Prefer <code>ONE</code> where possible. */
        @Deprecated
        T visitTwo();

        /**
         * Value of 100.
         *
         * @deprecated One is easier to manage.
         */
        @Deprecated
        T visitOneHundred();

        T visitUnknown(String unknownValue);

        static <T> OneStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static final class VisitorBuilder<T>
            implements OneStageVisitorBuilder<T>,
                    TwoStageVisitorBuilder<T>,
                    OneHundredStageVisitorBuilder<T>,
                    UnknownStageVisitorBuilder<T>,
                    Completed_StageVisitorBuilder<T> {
        private Supplier<T> oneVisitor;

        private Supplier<T> twoVisitor;

        private Supplier<T> oneHundredVisitor;

        private Function<@Safe String, T> unknownVisitor;

        @Override
        public TwoStageVisitorBuilder<T> visitOne(@Nonnull Supplier<T> oneVisitor) {
            Preconditions.checkNotNull(oneVisitor, "oneVisitor cannot be null");
            this.oneVisitor = oneVisitor;
            return this;
        }

        @Override
        public OneHundredStageVisitorBuilder<T> visitTwo(@Nonnull Supplier<T> twoVisitor) {
            Preconditions.checkNotNull(twoVisitor, "twoVisitor cannot be null");
            this.twoVisitor = twoVisitor;
            return this;
        }

        @Override
        public UnknownStageVisitorBuilder<T> visitOneHundred(@Nonnull Supplier<T> oneHundredVisitor) {
            Preconditions.checkNotNull(oneHundredVisitor, "oneHundredVisitor cannot be null");
            this.oneHundredVisitor = oneHundredVisitor;
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
                        "Unknown variant of the 'EnumExample' union", SafeArg.of("unknownType", unknownType));
            };
            return this;
        }

        @Override
        public Visitor<T> build() {
            final Supplier<T> oneVisitor = this.oneVisitor;
            final Supplier<T> twoVisitor = this.twoVisitor;
            final Supplier<T> oneHundredVisitor = this.oneHundredVisitor;
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
                public T visitOneHundred() {
                    return oneHundredVisitor.get();
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
        OneHundredStageVisitorBuilder<T> visitTwo(@Nonnull Supplier<T> twoVisitor);
    }

    public interface OneHundredStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> visitOneHundred(@Nonnull Supplier<T> oneHundredVisitor);
    }

    public interface UnknownStageVisitorBuilder<T> {
        Completed_StageVisitorBuilder<T> visitUnknown(@Nonnull Function<@Safe String, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> throwOnUnknown();
    }

    public interface Completed_StageVisitorBuilder<T> {
        Visitor<T> build();
    }
}
