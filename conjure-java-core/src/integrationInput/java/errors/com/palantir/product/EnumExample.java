package errors.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.errorprone.annotations.Immutable;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
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

/**
 * This class is used instead of a native enum to support unknown values. Rather than throw an exception, the
 * {@link EnumExample#valueOf} method defaults to a new instantiation of {@link EnumExample} where
 * {@link EnumExample#get} will return {@link EnumExample.Value#UNKNOWN}.
 *
 * <p>For example, {@code EnumExample.valueOf("corrupted value").get()} will return {@link EnumExample.Value#UNKNOWN},
 * but {@link EnumExample#toString} will return "corrupted value".
 *
 * <p>There is no method to access all instantiations of this class, since they cannot be known at compile time.
 */
@ConjureGenerated("com.palantir.conjure.java.types.EnumGenerator")
@Safe
@Immutable
public final class EnumExample {
    public static final EnumExample A = new EnumExample(Value.A, "A");

    public static final EnumExample B = new EnumExample(Value.B, "B");

    private static final List<EnumExample> values = Collections.unmodifiableList(Arrays.asList(A, B));

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
    public static EnumExample valueOf(@Nonnull @Safe String value) {
        Preconditions.checkNotNull(value, "value cannot be null");
        String upperCasedValue = value.toUpperCase(Locale.ROOT);
        switch (upperCasedValue) {
            case "A":
                return A;
            case "B":
                return B;
            default:
                return new EnumExample(Value.UNKNOWN, upperCasedValue);
        }
    }

    public <T> T accept(Visitor<T> visitor) {
        switch (value) {
            case A:
                return visitor.visitA();
            case B:
                return visitor.visitB();
            default:
                return visitor.visitUnknown(string);
        }
    }

    public static List<EnumExample> values() {
        return values;
    }

    @ConjureGenerated("com.palantir.conjure.java.types.EnumGenerator")
    public enum Value {
        A,

        B,

        UNKNOWN
    }

    @ConjureGenerated("com.palantir.conjure.java.types.EnumGenerator")
    public interface Visitor<T> {
        T visitA();

        T visitB();

        T visitUnknown(String unknownValue);

        static <T> AStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static final class VisitorBuilder<T>
            implements AStageVisitorBuilder<T>,
                    BStageVisitorBuilder<T>,
                    UnknownStageVisitorBuilder<T>,
                    Completed_StageVisitorBuilder<T> {
        private Supplier<T> aVisitor;

        private Supplier<T> bVisitor;

        private Function<@Safe String, T> unknownVisitor;

        @Override
        public BStageVisitorBuilder<T> visitA(@Nonnull Supplier<T> aVisitor) {
            Preconditions.checkNotNull(aVisitor, "aVisitor cannot be null");
            this.aVisitor = aVisitor;
            return this;
        }

        @Override
        public UnknownStageVisitorBuilder<T> visitB(@Nonnull Supplier<T> bVisitor) {
            Preconditions.checkNotNull(bVisitor, "bVisitor cannot be null");
            this.bVisitor = bVisitor;
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
            final Supplier<T> aVisitor = this.aVisitor;
            final Supplier<T> bVisitor = this.bVisitor;
            final Function<@Safe String, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitA() {
                    return aVisitor.get();
                }

                @Override
                public T visitB() {
                    return bVisitor.get();
                }

                @Override
                public T visitUnknown(String unknownType) {
                    return unknownVisitor.apply(unknownType);
                }
            };
        }
    }

    public interface AStageVisitorBuilder<T> {
        BStageVisitorBuilder<T> visitA(@Nonnull Supplier<T> aVisitor);
    }

    public interface BStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> visitB(@Nonnull Supplier<T> bVisitor);
    }

    public interface UnknownStageVisitorBuilder<T> {
        Completed_StageVisitorBuilder<T> visitUnknown(@Nonnull Function<@Safe String, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> throwOnUnknown();
    }

    public interface Completed_StageVisitorBuilder<T> {
        Visitor<T> build();
    }
}
