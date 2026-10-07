package bothstrictstagedbuilder.com.palantir.product;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.impl.JDKValueInstantiators;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.JacksonSupportGenerator")
final class ConjureJacksonSupport {
    private ConjureJacksonSupport() {}

    static final class ContainerDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {
        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext context, BeanProperty property)
                throws JsonMappingException {
            JavaType type = property.getType();
            boolean map = type.isMapLikeType();
            if (!map) {
                // Jackson skips `as` refinement on delegating creators that also specify `using`.
                type = context.constructSpecializedType(type, LinkedHashSet.class);
            }
            JsonDeserializer<?> delegate = context.findContextualValueDeserializer(type, property);
            // Custom constructors, entries, and problem handlers can expose shared containers.
            if (context.getConfig().getProblemHandlers() == null
                    && !hasTypeDeserializer(context, type)
                    && !hasTypeDeserializer(context, type.getContentType())
                    && (map ? isStandardMap(delegate, context, property) : isStandardSet(delegate, context))) {
                return delegate;
            }
            return new CopyingDeserializer(delegate, map);
        }

        private static boolean hasTypeDeserializer(DeserializationContext context, JavaType type)
                throws JsonMappingException {
            return type.getTypeHandler() != null || context.getConfig().findTypeDeserializer(type) != null;
        }

        private static boolean isStandardMap(
                JsonDeserializer<?> delegate, DeserializationContext context, BeanProperty property)
                throws JsonMappingException {
            if (delegate.getClass() != MapDeserializer.class) {
                return false;
            }
            MapDeserializer deserializer = (MapDeserializer) delegate;
            JavaType keyType = property.getType().getKeyType();
            return deserializer.getValueInstantiator().getClass()
                            == JDKValueInstantiators.findStdValueInstantiator(context.getConfig(), LinkedHashMap.class)
                                    .getClass()
                    && hasStandardElements(deserializer)
                    && keyType.getValueHandler() == null
                    && ClassUtil.isJacksonStdImpl(context.findKeyDeserializer(keyType, property))
                    && !hasTypeDeserializer(context, keyType);
        }

        private static boolean isStandardSet(JsonDeserializer<?> delegate, DeserializationContext context) {
            if (delegate.getClass() != CollectionDeserializer.class
                    && delegate.getClass() != StringCollectionDeserializer.class) {
                return false;
            }
            ContainerDeserializerBase<?> deserializer = (ContainerDeserializerBase<?>) delegate;
            return deserializer.getValueInstantiator().getClass()
                            == JDKValueInstantiators.findStdValueInstantiator(context.getConfig(), LinkedHashSet.class)
                                    .getClass()
                    && hasStandardElements(deserializer);
        }

        private static boolean hasStandardElements(ContainerDeserializerBase<?> deserializer) {
            // Custom element deserializers can retain the container exposed by JsonParser.currentValue().
            JsonDeserializer<?> content = deserializer.getContentDeserializer();
            return content == null
                    || (content instanceof StdScalarDeserializer<?> && ClassUtil.isJacksonStdImpl(content));
        }

        @Override
        public Object deserialize(JsonParser _parser, DeserializationContext _context) {
            throw new SafeIllegalStateException("Container deserializer must be contextualized");
        }

        private static final class CopyingDeserializer extends DelegatingDeserializer {
            private final boolean map;

            CopyingDeserializer(JsonDeserializer<?> delegate, boolean map) {
                super(delegate);
                this.map = map;
            }

            @Override
            protected JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> delegate) {
                return new CopyingDeserializer(delegate, map);
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

            private Object copy(Object value) {
                if (value == null) {
                    return null;
                }
                return map ? new LinkedHashMap<>((Map<?, ?>) value) : new LinkedHashSet<>((Collection<?>) value);
            }
        }
    }
}
