/*
 * (c) Copyright 2021 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.undertow.annotations;

import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.BodySerDe;
import com.palantir.conjure.java.undertow.lib.Deserializer;
import com.palantir.conjure.java.undertow.lib.Serializer;
import com.palantir.conjure.java.undertow.lib.TypeMarker;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

/** Default serializer and deserializer factory which produces behavior equivalent to conjure. */
public enum DefaultSerDe implements SerializerFactory<Object>, DeserializerFactory<Object> {
    INSTANCE;

    private static final TypeMarker<InputStream> INPUT_STREAM = new TypeMarker<>() {};

    @Override
    @SuppressWarnings("unchecked")
    public <T> Deserializer<T> deserializer(TypeMarker<T> marker, UndertowRuntime runtime) {
        if (INPUT_STREAM.getType().equals(marker.getType())) {
            return (Deserializer<T>) new BinaryDeserializer(runtime.bodySerDe());
        }
        return runtime.bodySerDe().deserializer(marker);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Serializer<T> serializer(TypeMarker<T> marker, UndertowRuntime runtime) {
        Type type = marker.getType();
        Serializer<T> maybeSpecialSerializer = (Serializer<T>) maybeSpecialSerializer(type, runtime);
        if (maybeSpecialSerializer != null) {
            return maybeSpecialSerializer;
        }
        Serializer<T> delegateSerializer = runtime.bodySerDe().serializer(marker);
        return isOptional(marker)
                ? (Serializer<T>) new OptionalDelegatingSerializer<>((Serializer<Optional<Object>>) delegateSerializer)
                : delegateSerializer;
    }

    private Serializer<?> maybeSpecialSerializer(Type type, UndertowRuntime runtime) {
        if (Void.class.equals(type)) {
            return VoidSerializer.INSTANCE;
        }
        if (type instanceof Class) {
            Class<?> clazz = (Class<?>) type;
            if (BinaryResponseBody.class.isAssignableFrom(clazz)) {
                return new BinarySerializer(runtime.bodySerDe());
            }
        }
        Type optionalValueType = unwrapOptional(type);
        if (optionalValueType != null) {
            Serializer<?> maybeDelegate = maybeSpecialSerializer(optionalValueType, runtime);
            if (maybeDelegate != null) {
                return new OptionalValueDelegatingSerializer<>(maybeDelegate);
            }
        }
        return null;
    }

    private static boolean isOptional(TypeMarker<?> marker) {
        return unwrapOptional(marker.getType()) != null;
    }

    private static Type unwrapOptional(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if (parameterizedType.getRawType().equals(Optional.class)) {
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                if (typeArguments.length != 1) {
                    throw new SafeIllegalArgumentException(
                            "Expected Optional to have exactly one type argument",
                            SafeArg.of("typeArguments", typeArguments));
                }
                return typeArguments[0];
            }
        }
        return null;
    }

    private enum VoidSerializer implements Serializer<Void> {
        INSTANCE;

        @Override
        public void serialize(Void _value, HttpServerExchange exchange) {
            exchange.setStatusCode(StatusCodes.NO_CONTENT);
        }
    }

    private static final class BinaryDeserializer implements Deserializer<InputStream> {
        private final BodySerDe bodySerDe;

        BinaryDeserializer(BodySerDe bodySerDe) {
            this.bodySerDe = bodySerDe;
        }

        @Override
        public InputStream deserialize(HttpServerExchange exchange) {
            return bodySerDe.deserializeInputStream(exchange);
        }
    }

    private static final class BinarySerializer implements Serializer<BinaryResponseBody> {
        private final BodySerDe bodySerDe;

        BinarySerializer(BodySerDe bodySerDe) {
            this.bodySerDe = bodySerDe;
        }

        @Override
        public void serialize(BinaryResponseBody value, HttpServerExchange exchange) throws IOException {
            bodySerDe.serialize(value, exchange);
        }
    }

    private static final class OptionalDelegatingSerializer<T> implements Serializer<Optional<T>> {
        private final Serializer<Optional<T>> delegate;

        OptionalDelegatingSerializer(Serializer<Optional<T>> delegate) {
            this.delegate = delegate;
        }

        @Override
        public void serialize(Optional<T> value, HttpServerExchange exchange) throws IOException {
            if (value.isPresent()) {
                delegate.serialize(value, exchange);
            } else {
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }
    }

    private static final class OptionalValueDelegatingSerializer<T> implements Serializer<Optional<T>> {
        private final Serializer<T> delegate;

        OptionalValueDelegatingSerializer(Serializer<T> delegate) {
            this.delegate = delegate;
        }

        @Override
        public void serialize(Optional<T> value, HttpServerExchange exchange) throws IOException {
            if (value.isPresent()) {
                delegate.serialize(value.get(), exchange);
            } else {
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }
    }
}
