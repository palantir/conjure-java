/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.palantir.internal;

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
 * {@link GeneratorType#valueOf} method defaults to a new instantiation of {@link GeneratorType} where
 * {@link GeneratorType#get} will return {@link Value#UNKNOWN}.
 *
 * <p>For example, {@code GeneratorType.valueOf("corrupted value").get()} will return
 * {@link Value#UNKNOWN}, but {@link GeneratorType#toString} will return "corrupted value".
 *
 * <p>There is no method to access all instantiations of this class, since they cannot be known at compile time.
 */
@Generated("com.palantir.conjure.java.types.EnumGenerator")
@Safe
@Immutable
public final class GeneratorType {
    public static final GeneratorType OBJECT = new GeneratorType(Value.OBJECT, "OBJECT");

    public static final GeneratorType DIALOGUE = new GeneratorType(Value.DIALOGUE, "DIALOGUE");

    public static final GeneratorType UNDERTOW = new GeneratorType(Value.UNDERTOW, "UNDERTOW");

    public static final GeneratorType JERSEY = new GeneratorType(Value.JERSEY, "JERSEY");

    public static final GeneratorType ERROR = new GeneratorType(Value.ERROR, "ERROR");

    public static final GeneratorType CHECKED_ERROR = new GeneratorType(Value.CHECKED_ERROR, "CHECKED_ERROR");

    private static final List<GeneratorType> values =
            Collections.unmodifiableList(Arrays.asList(OBJECT, DIALOGUE, UNDERTOW, JERSEY, ERROR, CHECKED_ERROR));

    private final Value value;

    private final String string;

    private GeneratorType(Value value, String string) {
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
                        && other instanceof GeneratorType
                        && this.string.equals(((GeneratorType) other).string));
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static GeneratorType valueOf(@Nonnull @Safe String value) {
        Preconditions.checkNotNull(value, "value cannot be null");
        String upperCasedValue = value.toUpperCase(Locale.ROOT);
        switch (upperCasedValue) {
            case "OBJECT":
                return OBJECT;
            case "DIALOGUE":
                return DIALOGUE;
            case "UNDERTOW":
                return UNDERTOW;
            case "JERSEY":
                return JERSEY;
            case "ERROR":
                return ERROR;
            case "CHECKED_ERROR":
                return CHECKED_ERROR;
            default:
                return new GeneratorType(Value.UNKNOWN, upperCasedValue);
        }
    }

    public <T> T accept(Visitor<T> visitor) {
        switch (value) {
            case OBJECT:
                return visitor.visitObject();
            case DIALOGUE:
                return visitor.visitDialogue();
            case UNDERTOW:
                return visitor.visitUndertow();
            case JERSEY:
                return visitor.visitJersey();
            case ERROR:
                return visitor.visitError();
            case CHECKED_ERROR:
                return visitor.visitCheckedError();
            default:
                return visitor.visitUnknown(string);
        }
    }

    public static List<GeneratorType> values() {
        return values;
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public enum Value {
        OBJECT,

        DIALOGUE,

        UNDERTOW,

        JERSEY,

        ERROR,

        CHECKED_ERROR,

        UNKNOWN
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public interface Visitor<T> {
        T visitObject();

        T visitDialogue();

        T visitUndertow();

        T visitJersey();

        T visitError();

        T visitCheckedError();

        T visitUnknown(String unknownValue);

        static <T> ObjectStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static final class VisitorBuilder<T>
            implements ObjectStageVisitorBuilder<T>,
                    DialogueStageVisitorBuilder<T>,
                    UndertowStageVisitorBuilder<T>,
                    JerseyStageVisitorBuilder<T>,
                    ErrorStageVisitorBuilder<T>,
                    CheckedErrorStageVisitorBuilder<T>,
                    UnknownStageVisitorBuilder<T>,
                    Completed_StageVisitorBuilder<T> {
        private Supplier<T> objectVisitor;

        private Supplier<T> dialogueVisitor;

        private Supplier<T> undertowVisitor;

        private Supplier<T> jerseyVisitor;

        private Supplier<T> errorVisitor;

        private Supplier<T> checkedErrorVisitor;

        private Function<@Safe String, T> unknownVisitor;

        @Override
        public DialogueStageVisitorBuilder<T> visitObject(@Nonnull Supplier<T> objectVisitor) {
            Preconditions.checkNotNull(objectVisitor, "objectVisitor cannot be null");
            this.objectVisitor = objectVisitor;
            return this;
        }

        @Override
        public UndertowStageVisitorBuilder<T> visitDialogue(@Nonnull Supplier<T> dialogueVisitor) {
            Preconditions.checkNotNull(dialogueVisitor, "dialogueVisitor cannot be null");
            this.dialogueVisitor = dialogueVisitor;
            return this;
        }

        @Override
        public JerseyStageVisitorBuilder<T> visitUndertow(@Nonnull Supplier<T> undertowVisitor) {
            Preconditions.checkNotNull(undertowVisitor, "undertowVisitor cannot be null");
            this.undertowVisitor = undertowVisitor;
            return this;
        }

        @Override
        public ErrorStageVisitorBuilder<T> visitJersey(@Nonnull Supplier<T> jerseyVisitor) {
            Preconditions.checkNotNull(jerseyVisitor, "jerseyVisitor cannot be null");
            this.jerseyVisitor = jerseyVisitor;
            return this;
        }

        @Override
        public CheckedErrorStageVisitorBuilder<T> visitError(@Nonnull Supplier<T> errorVisitor) {
            Preconditions.checkNotNull(errorVisitor, "errorVisitor cannot be null");
            this.errorVisitor = errorVisitor;
            return this;
        }

        @Override
        public UnknownStageVisitorBuilder<T> visitCheckedError(@Nonnull Supplier<T> checkedErrorVisitor) {
            Preconditions.checkNotNull(checkedErrorVisitor, "checkedErrorVisitor cannot be null");
            this.checkedErrorVisitor = checkedErrorVisitor;
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
                        "Unknown variant of the 'GeneratorType' union", SafeArg.of("unknownType", unknownType));
            };
            return this;
        }

        @Override
        public Visitor<T> build() {
            final Supplier<T> objectVisitor = this.objectVisitor;
            final Supplier<T> dialogueVisitor = this.dialogueVisitor;
            final Supplier<T> undertowVisitor = this.undertowVisitor;
            final Supplier<T> jerseyVisitor = this.jerseyVisitor;
            final Supplier<T> errorVisitor = this.errorVisitor;
            final Supplier<T> checkedErrorVisitor = this.checkedErrorVisitor;
            final Function<@Safe String, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitObject() {
                    return objectVisitor.get();
                }

                @Override
                public T visitDialogue() {
                    return dialogueVisitor.get();
                }

                @Override
                public T visitUndertow() {
                    return undertowVisitor.get();
                }

                @Override
                public T visitJersey() {
                    return jerseyVisitor.get();
                }

                @Override
                public T visitError() {
                    return errorVisitor.get();
                }

                @Override
                public T visitCheckedError() {
                    return checkedErrorVisitor.get();
                }

                @Override
                public T visitUnknown(String unknownType) {
                    return unknownVisitor.apply(unknownType);
                }
            };
        }
    }

    public interface ObjectStageVisitorBuilder<T> {
        DialogueStageVisitorBuilder<T> visitObject(@Nonnull Supplier<T> objectVisitor);
    }

    public interface DialogueStageVisitorBuilder<T> {
        UndertowStageVisitorBuilder<T> visitDialogue(@Nonnull Supplier<T> dialogueVisitor);
    }

    public interface UndertowStageVisitorBuilder<T> {
        JerseyStageVisitorBuilder<T> visitUndertow(@Nonnull Supplier<T> undertowVisitor);
    }

    public interface JerseyStageVisitorBuilder<T> {
        ErrorStageVisitorBuilder<T> visitJersey(@Nonnull Supplier<T> jerseyVisitor);
    }

    public interface ErrorStageVisitorBuilder<T> {
        CheckedErrorStageVisitorBuilder<T> visitError(@Nonnull Supplier<T> errorVisitor);
    }

    public interface CheckedErrorStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> visitCheckedError(@Nonnull Supplier<T> checkedErrorVisitor);
    }

    public interface UnknownStageVisitorBuilder<T> {
        Completed_StageVisitorBuilder<T> visitUnknown(@Nonnull Function<@Safe String, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> throwOnUnknown();
    }

    public interface Completed_StageVisitorBuilder<T> {
        Visitor<T> build();
    }
}
