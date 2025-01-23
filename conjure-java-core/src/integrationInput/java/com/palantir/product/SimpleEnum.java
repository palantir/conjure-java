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
 * {@link SimpleEnum#valueOf} method defaults to a new instantiation of {@link SimpleEnum} where {@link SimpleEnum#get}
 * will return {@link SimpleEnum.Value#UNKNOWN}.
 *
 * <p>For example, {@code SimpleEnum.valueOf("corrupted value").get()} will return {@link SimpleEnum.Value#UNKNOWN}, but
 * {@link SimpleEnum#toString} will return "corrupted value".
 *
 * <p>There is no method to access all instantiations of this class, since they cannot be known at compile time.
 */
@Generated("com.palantir.conjure.java.types.EnumGenerator")
@Safe
@Immutable
public final class SimpleEnum {
    public static final SimpleEnum VALUE = new SimpleEnum(Value.VALUE, "VALUE");

    private static final List<SimpleEnum> values = Collections.unmodifiableList(Arrays.asList(VALUE));

    private final Value value;

    private final String string;

    private SimpleEnum(Value value, String string) {
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
                        && other instanceof SimpleEnum
                        && this.string.equals(((SimpleEnum) other).string));
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SimpleEnum valueOf(@Nonnull @Safe String value) {
        Preconditions.checkNotNull(value, "value cannot be null");
        String upperCasedValue = value.toUpperCase(Locale.ROOT);
        switch (upperCasedValue) {
            case "VALUE":
                return VALUE;
            default:
                return new SimpleEnum(Value.UNKNOWN, upperCasedValue);
        }
    }

    public <T> T accept(Visitor<T> visitor) {
        switch (value) {
            case VALUE:
                return visitor.visitValue();
            default:
                return visitor.visitUnknown(string);
        }
    }

    public static List<SimpleEnum> values() {
        return values;
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public enum Value {
        VALUE,

        UNKNOWN
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public interface Visitor<T> {
        T visitValue();

        T visitUnknown(String unknownValue);

        static <T> ValueStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static final class VisitorBuilder<T>
            implements ValueStageVisitorBuilder<T>, UnknownStageVisitorBuilder<T>, Completed_StageVisitorBuilder<T> {
        private Supplier<T> valueVisitor;

        private Function<@Safe String, T> unknownVisitor;

        @Override
        public UnknownStageVisitorBuilder<T> visitValue(@Nonnull Supplier<T> valueVisitor) {
            Preconditions.checkNotNull(valueVisitor, "valueVisitor cannot be null");
            this.valueVisitor = valueVisitor;
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
                        "Unknown variant of the 'SimpleEnum' union", SafeArg.of("unknownType", unknownType));
            };
            return this;
        }

        @Override
        public Visitor<T> build() {
            final Supplier<T> valueVisitor = this.valueVisitor;
            final Function<@Safe String, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitValue() {
                    return valueVisitor.get();
                }

                @Override
                public T visitUnknown(String unknownType) {
                    return unknownVisitor.apply(unknownType);
                }
            };
        }
    }

    public interface ValueStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> visitValue(@Nonnull Supplier<T> valueVisitor);
    }

    public interface UnknownStageVisitorBuilder<T> {
        Completed_StageVisitorBuilder<T> visitUnknown(@Nonnull Function<@Safe String, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> throwOnUnknown();
    }

    public interface Completed_StageVisitorBuilder<T> {
        Visitor<T> build();
    }
}
