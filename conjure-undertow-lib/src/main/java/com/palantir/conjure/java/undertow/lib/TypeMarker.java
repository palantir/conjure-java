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
    public final String toString() {
        return "TypeMarker{type=" + type + '}';
    }

    /** Create a new {@link TypeMarker} instance wrapping the provided {@link Type}. */
    public static TypeMarker<?> of(Type type) {
        return new WrappingTypeMarker(type);
    }

    private static final class WrappingTypeMarker extends TypeMarker<Object> {
        private WrappingTypeMarker(Type type) {
            super(type);
        }

        @Override
        public int hashCode() {
            return getType().hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof WrappingTypeMarker) {
                WrappingTypeMarker otherMarker = (WrappingTypeMarker) other;
                return Objects.equals(getType(), otherMarker.getType());
            }
            return false;
        }
    }
}
