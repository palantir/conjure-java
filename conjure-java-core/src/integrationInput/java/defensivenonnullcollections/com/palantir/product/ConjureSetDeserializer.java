package defensivenonnullcollections.com.palantir.product;

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
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.JacksonSupportGenerator")
final class ConjureSetDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {
    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext context, BeanProperty property)
            throws JsonMappingException {
        // Jackson skips `as` refinement on delegating creators that also specify `using`.
        JavaType type = context.constructSpecializedType(property.getType(), LinkedHashSet.class);
        JsonDeserializer<?> delegate = context.findContextualValueDeserializer(type, property);
        if ((delegate.getClass() == CollectionDeserializer.class
                        || delegate.getClass() == StringCollectionDeserializer.class)
                && context.getConfig().getProblemHandlers() == null
                && ((ContainerDeserializerBase<?>) delegate)
                                .getValueInstantiator()
                                .getClass()
                        == JDKValueInstantiators.findStdValueInstantiator(context.getConfig(), LinkedHashSet.class)
                                .getClass()
                && hasStandardElements((ContainerDeserializerBase<?>) delegate)
                && type.getTypeHandler() == null
                && context.getConfig().findTypeDeserializer(type) == null
                && type.getContentType().getTypeHandler() == null
                && context.getConfig().findTypeDeserializer(type.getContentType()) == null) {
            return delegate;
        }
        return new CopyingDeserializer(delegate);
    }

    private static boolean hasStandardElements(ContainerDeserializerBase<?> deserializer) {
        // Custom element deserializers can retain the set exposed by JsonParser.currentValue().
        JsonDeserializer<?> content = deserializer.getContentDeserializer();
        return content == null || (content instanceof StdScalarDeserializer<?> && ClassUtil.isJacksonStdImpl(content));
    }

    @Override
    public Object deserialize(JsonParser _parser, DeserializationContext _context) {
        throw new SafeIllegalStateException("Set deserializer must be contextualized");
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
            return value == null ? null : new LinkedHashSet<>((Collection<?>) value);
        }
    }
}
