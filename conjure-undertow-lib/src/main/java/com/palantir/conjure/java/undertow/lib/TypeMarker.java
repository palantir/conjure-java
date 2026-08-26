/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.undertow.lib;

import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Captures generic type information.
 *
 * <p>Usage example:
 *
 * <pre>new TypeMarker&lt;List&lt;Integer&gt;() {}</pre>
 *
 * .
 */
@SuppressWarnings("unused") // Generic type exists for compile time safety but is not used internally.
public abstract class TypeMarker<T> {

    private final Type type;

    protected TypeMarker() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        Preconditions.checkArgument(
                genericSuperclass instanceof ParameterizedType,
                "Class is not parameterized",
                SafeArg.of("class", genericSuperclass));
        type = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        Preconditions.checkArgument(
                !(type instanceof TypeVariable),
                "TypeMarker does not support variable types",
                SafeArg.of("typeVariable", type));
    }

    private TypeMarker(Type type) {
        this.type = Preconditions.checkNotNull(type, "Type is required");
    }

    public final Type getType() {
        return type;
    }

    @Override
    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof TypeMarker) {
            TypeMarker<?> that = (TypeMarker<?>) other;
            return type.equals(that.type);
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return type.hashCode();
    }

    @Override
    public final String toString() {
        return "TypeMarker{type=" + type + '}';
    }

    /** Create a new {@link TypeMarker} instance wrapping the provided class. */
    public static <T> TypeMarker<T> of(Class<T> type) {
        return new WrappingTypeMarker<>(type);
    }

    /** Create a new {@link TypeMarker} instance wrapping the provided {@link Type}. */
    public static TypeMarker<?> of(Type type) {
        return new WrappingTypeMarker<>(type);
    }

    /**
     * Creates a type marker for a parameterized type. The returned type is inferred from the invocation context, so
     * callers must ensure it matches {@code rawType} and {@code typeArguments}.
     */
    @SafeVarargs
    public static <T> TypeMarker<T> ofGeneric(Class<?> rawType, TypeMarker<?>... typeArguments) {
        Preconditions.checkNotNull(rawType, "Raw type is required");
        TypeMarker<?>[] checkedTypeArguments = Preconditions.checkNotNull(typeArguments, "Type arguments are required");
        int expectedTypeArguments = rawType.getTypeParameters().length;
        Preconditions.checkArgument(
                expectedTypeArguments > 0, "Raw type is not generic", SafeArg.of("rawType", rawType));
        Preconditions.checkArgument(
                expectedTypeArguments == checkedTypeArguments.length,
                "Incorrect number of type arguments",
                SafeArg.of("rawType", rawType),
                SafeArg.of("expected", expectedTypeArguments),
                SafeArg.of("actual", checkedTypeArguments.length));
        Type[] types = new Type[checkedTypeArguments.length];
        for (int index = 0; index < checkedTypeArguments.length; index++) {
            types[index] = Preconditions.checkNotNull(checkedTypeArguments[index], "Type argument is required")
                    .getType();
        }
        return new WrappingTypeMarker<>(new ParameterizedTypeImpl(rawType, types));
    }

    private static final class WrappingTypeMarker<T> extends TypeMarker<T> {
        private WrappingTypeMarker(Type type) {
            super(type);
        }
    }

    private static final class ParameterizedTypeImpl implements ParameterizedType {
        private final Class<?> rawType;
        private final Type ownerType;
        private final Type[] typeArguments;

        private ParameterizedTypeImpl(Class<?> rawType, Type[] typeArguments) {
            this.rawType = rawType;
            this.ownerType = rawType.getDeclaringClass();
            this.typeArguments = typeArguments.clone();
        }

        @Override
        public Type[] getActualTypeArguments() {
            return typeArguments.clone();
        }

        @Override
        public Type getRawType() {
            return rawType;
        }

        @Override
        public Type getOwnerType() {
            return ownerType;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ParameterizedType that)) {
                return false;
            }
            return Objects.equals(ownerType, that.getOwnerType())
                    && Objects.equals(rawType, that.getRawType())
                    && Arrays.equals(typeArguments, that.getActualTypeArguments());
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(typeArguments) ^ Objects.hashCode(ownerType) ^ Objects.hashCode(rawType);
        }

        @Override
        public String getTypeName() {
            return toString();
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder(rawType.getTypeName());
            if (typeArguments.length > 0) {
                result.append('<');
                for (int index = 0; index < typeArguments.length; index++) {
                    if (index > 0) {
                        result.append(", ");
                    }
                    result.append(typeArguments[index].getTypeName());
                }
                result.append('>');
            }
            return result.toString();
        }
    }
}
