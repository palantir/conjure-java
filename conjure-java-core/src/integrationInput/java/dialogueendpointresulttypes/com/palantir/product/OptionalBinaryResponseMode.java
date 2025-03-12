package dialogueendpointresulttypes.com.palantir.product;

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
 * {@link OptionalBinaryResponseMode#valueOf} method defaults to a new instantiation of
 * {@link OptionalBinaryResponseMode} where {@link OptionalBinaryResponseMode#get} will return
 * {@link OptionalBinaryResponseMode.Value#UNKNOWN}.
 *
 * <p>For example, {@code OptionalBinaryResponseMode.valueOf("corrupted value").get()} will return
 * {@link OptionalBinaryResponseMode.Value#UNKNOWN}, but {@link OptionalBinaryResponseMode#toString} will return
 * "corrupted value".
 *
 * <p>There is no method to access all instantiations of this class, since they cannot be known at compile time.
 */
@Generated("com.palantir.conjure.java.types.EnumGenerator")
@Safe
@Immutable
public final class OptionalBinaryResponseMode {
    public static final OptionalBinaryResponseMode PRESENT = new OptionalBinaryResponseMode(Value.PRESENT, "PRESENT");

    public static final OptionalBinaryResponseMode ABSENT = new OptionalBinaryResponseMode(Value.ABSENT, "ABSENT");

    public static final OptionalBinaryResponseMode ERROR = new OptionalBinaryResponseMode(Value.ERROR, "ERROR");

    private static final List<OptionalBinaryResponseMode> values =
            Collections.unmodifiableList(Arrays.asList(PRESENT, ABSENT, ERROR));

    private final Value value;

    private final String string;

    private OptionalBinaryResponseMode(Value value, String string) {
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
                        && other instanceof OptionalBinaryResponseMode
                        && this.string.equals(((OptionalBinaryResponseMode) other).string));
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static OptionalBinaryResponseMode valueOf(@Nonnull @Safe String value) {
        Preconditions.checkNotNull(value, "value cannot be null");
        String upperCasedValue = value.toUpperCase(Locale.ROOT);
        switch (upperCasedValue) {
            case "PRESENT":
                return PRESENT;
            case "ABSENT":
                return ABSENT;
            case "ERROR":
                return ERROR;
            default:
                return new OptionalBinaryResponseMode(Value.UNKNOWN, upperCasedValue);
        }
    }

    public <T> T accept(Visitor<T> visitor) {
        switch (value) {
            case PRESENT:
                return visitor.visitPresent();
            case ABSENT:
                return visitor.visitAbsent();
            case ERROR:
                return visitor.visitError();
            default:
                return visitor.visitUnknown(string);
        }
    }

    public static List<OptionalBinaryResponseMode> values() {
        return values;
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public enum Value {
        PRESENT,

        ABSENT,

        ERROR,

        UNKNOWN
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public interface Visitor<T> {
        T visitPresent();

        T visitAbsent();

        T visitError();

        T visitUnknown(String unknownValue);

        static <T> PresentStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static final class VisitorBuilder<T>
            implements PresentStageVisitorBuilder<T>,
                    AbsentStageVisitorBuilder<T>,
                    ErrorStageVisitorBuilder<T>,
                    UnknownStageVisitorBuilder<T>,
                    Completed_StageVisitorBuilder<T> {
        private Supplier<T> presentVisitor;

        private Supplier<T> absentVisitor;

        private Supplier<T> errorVisitor;

        private Function<@Safe String, T> unknownVisitor;

        @Override
        public AbsentStageVisitorBuilder<T> visitPresent(@Nonnull Supplier<T> presentVisitor) {
            Preconditions.checkNotNull(presentVisitor, "presentVisitor cannot be null");
            this.presentVisitor = presentVisitor;
            return this;
        }

        @Override
        public ErrorStageVisitorBuilder<T> visitAbsent(@Nonnull Supplier<T> absentVisitor) {
            Preconditions.checkNotNull(absentVisitor, "absentVisitor cannot be null");
            this.absentVisitor = absentVisitor;
            return this;
        }

        @Override
        public UnknownStageVisitorBuilder<T> visitError(@Nonnull Supplier<T> errorVisitor) {
            Preconditions.checkNotNull(errorVisitor, "errorVisitor cannot be null");
            this.errorVisitor = errorVisitor;
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
                        "Unknown variant of the 'OptionalBinaryResponseMode' union",
                        SafeArg.of("unknownType", unknownType));
            };
            return this;
        }

        @Override
        public Visitor<T> build() {
            final Supplier<T> presentVisitor = this.presentVisitor;
            final Supplier<T> absentVisitor = this.absentVisitor;
            final Supplier<T> errorVisitor = this.errorVisitor;
            final Function<@Safe String, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitPresent() {
                    return presentVisitor.get();
                }

                @Override
                public T visitAbsent() {
                    return absentVisitor.get();
                }

                @Override
                public T visitError() {
                    return errorVisitor.get();
                }

                @Override
                public T visitUnknown(String unknownType) {
                    return unknownVisitor.apply(unknownType);
                }
            };
        }
    }

    public interface PresentStageVisitorBuilder<T> {
        AbsentStageVisitorBuilder<T> visitPresent(@Nonnull Supplier<T> presentVisitor);
    }

    public interface AbsentStageVisitorBuilder<T> {
        ErrorStageVisitorBuilder<T> visitAbsent(@Nonnull Supplier<T> absentVisitor);
    }

    public interface ErrorStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> visitError(@Nonnull Supplier<T> errorVisitor);
    }

    public interface UnknownStageVisitorBuilder<T> {
        Completed_StageVisitorBuilder<T> visitUnknown(@Nonnull Function<@Safe String, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> throwOnUnknown();
    }

    public interface Completed_StageVisitorBuilder<T> {
        Visitor<T> build();
    }
}
