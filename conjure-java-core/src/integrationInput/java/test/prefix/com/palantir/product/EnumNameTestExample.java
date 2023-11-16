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
 * This class is used instead of a native enum to support unknown values. Rather than throw an exception, the
 * {@link EnumNameTestExample#valueOf} method defaults to a new instantiation of {@link EnumNameTestExample} where
 * {@link EnumNameTestExample#get} will return {@link EnumNameTestExample.Value#UNKNOWN}.
 *
 * <p>For example, {@code EnumNameTestExample.valueOf("corrupted value").get()} will return
 * {@link EnumNameTestExample.Value#UNKNOWN}, but {@link EnumNameTestExample#toString} will return "corrupted value".
 *
 * <p>There is no method to access all instantiations of this class, since they cannot be known at compile time.
 */
@Generated("com.palantir.conjure.java.types.EnumGenerator")
@Safe
@Immutable
public final class EnumNameTestExample {
    public static final EnumNameTestExample INCOMPLETE = new EnumNameTestExample(Value.INCOMPLETE, "INCOMPLETE");

    public static final EnumNameTestExample COMPLETED = new EnumNameTestExample(Value.COMPLETED, "COMPLETED");

    private static final List<EnumNameTestExample> values =
            Collections.unmodifiableList(Arrays.asList(INCOMPLETE, COMPLETED));

    private final Value value;

    private final String string;

    private EnumNameTestExample(Value value, String string) {
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
                        && other instanceof EnumNameTestExample
                        && this.string.equals(((EnumNameTestExample) other).string));
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static EnumNameTestExample valueOf(@Nonnull @Safe String value) {
        Preconditions.checkNotNull(value, "value cannot be null");
        String upperCasedValue = value.toUpperCase(Locale.ROOT);
        switch (upperCasedValue) {
            case "INCOMPLETE":
                return INCOMPLETE;
            case "COMPLETED":
                return COMPLETED;
            default:
                return new EnumNameTestExample(Value.UNKNOWN, upperCasedValue);
        }
    }

    public <T> T accept(Visitor<T> visitor) {
        switch (value) {
            case INCOMPLETE:
                return visitor.visitIncomplete();
            case COMPLETED:
                return visitor.visitCompleted();
            default:
                return visitor.visitUnknown(string);
        }
    }

    public static List<EnumNameTestExample> values() {
        return values;
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public enum Value {
        INCOMPLETE,

        COMPLETED,

        UNKNOWN
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public interface Visitor<T> {
        T visitIncomplete();

        T visitCompleted();

        T visitUnknown(String unknownValue);

        static <T> IncompleteStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static final class VisitorBuilder<T>
            implements IncompleteStageVisitorBuilder<T>,
                    CompletedStageVisitorBuilder<T>,
                    UnknownStageVisitorBuilder<T>,
                    Completed_StageVisitorBuilder<T> {
        private Supplier<T> incompleteVisitor;

        private Supplier<T> completedVisitor;

        private Function<@Safe String, T> unknownVisitor;

        @Override
        public CompletedStageVisitorBuilder<T> visitIncomplete(@Nonnull Supplier<T> incompleteVisitor) {
            Preconditions.checkNotNull(incompleteVisitor, "incompleteVisitor cannot be null");
            this.incompleteVisitor = incompleteVisitor;
            return this;
        }

        @Override
        public UnknownStageVisitorBuilder<T> visitCompleted(@Nonnull Supplier<T> completedVisitor) {
            Preconditions.checkNotNull(completedVisitor, "completedVisitor cannot be null");
            this.completedVisitor = completedVisitor;
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
                        "Unknown variant of the 'EnumNameTestExample' union", SafeArg.of("unknownType", unknownType));
            };
            return this;
        }

        @Override
        public Visitor<T> build() {
            final Supplier<T> incompleteVisitor = this.incompleteVisitor;
            final Supplier<T> completedVisitor = this.completedVisitor;
            final Function<@Safe String, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitIncomplete() {
                    return incompleteVisitor.get();
                }

                @Override
                public T visitCompleted() {
                    return completedVisitor.get();
                }

                @Override
                public T visitUnknown(String unknownType) {
                    return unknownVisitor.apply(unknownType);
                }
            };
        }
    }

    public interface IncompleteStageVisitorBuilder<T> {
        CompletedStageVisitorBuilder<T> visitIncomplete(@Nonnull Supplier<T> incompleteVisitor);
    }

    public interface CompletedStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> visitCompleted(@Nonnull Supplier<T> completedVisitor);
    }

    public interface UnknownStageVisitorBuilder<T> {
        Completed_StageVisitorBuilder<T> visitUnknown(@Nonnull Function<@Safe String, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> throwOnUnknown();
    }

    public interface Completed_StageVisitorBuilder<T> {
        Visitor<T> build();
    }
}
