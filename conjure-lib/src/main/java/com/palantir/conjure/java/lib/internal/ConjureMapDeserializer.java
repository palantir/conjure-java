/*
 * (c) Copyright 2026 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.lib.internal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.impl.JDKValueInstantiators;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/** Internal Jackson support for transferring ownership of maps to generated union values. */
public final class ConjureMapDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {
    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext context, BeanProperty property)
            throws JsonMappingException {
        JsonDeserializer<?> delegate = context.findContextualValueDeserializer(property.getType(), property);
        // Only Jackson's standard LinkedHashMap constructor guarantees a fresh, privately owned map.
        if (delegate.getClass() == MapDeserializer.class
                && ((MapDeserializer) delegate).getValueInstantiator().getClass()
                        == JDKValueInstantiators.findStdValueInstantiator(context.getConfig(), LinkedHashMap.class)
                                .getClass()
                && property.getType().getTypeHandler() == null
                && context.getConfig().findTypeDeserializer(property.getType()) == null) {
            return delegate;
        }
        return new CopyingDeserializer(delegate);
    }

    @Override
    public Object deserialize(JsonParser _parser, DeserializationContext _context) {
        throw new SafeIllegalStateException("Map deserializer must be contextualized");
    }

    private static final class CopyingDeserializer extends DelegatingDeserializer {
        CopyingDeserializer(JsonDeserializer<?> delegate) {
            super(delegate);
        }

        @Override
        protected JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> delegate) {
            return new CopyingDeserializer(delegate);
        }

        @Override
        public Object deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            return copy(super.deserialize(parser, context));
        }

        @Override
        public Object deserializeWithType(
                JsonParser parser, DeserializationContext context, TypeDeserializer typeDeserializer)
                throws IOException {
            return copy(super.deserializeWithType(parser, context, typeDeserializer));
        }

        @Override
        public Object getNullValue(DeserializationContext context) throws JsonMappingException {
            return copy(super.getNullValue(context));
        }

        @Override
        public Object getEmptyValue(DeserializationContext context) throws JsonMappingException {
            return copy(super.getEmptyValue(context));
        }

        @Override
        public Object getAbsentValue(DeserializationContext context) throws JsonMappingException {
            return copy(super.getAbsentValue(context));
        }

        private static Object copy(Object value) {
            return value == null ? null : new LinkedHashMap<>((Map<?, ?>) value);
        }
    }
}
