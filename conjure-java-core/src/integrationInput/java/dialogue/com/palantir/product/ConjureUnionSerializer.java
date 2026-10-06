package dialogue.com.palantir.product;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import java.io.IOException;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.JacksonSupportGenerator")
final class ConjureUnionSerializer extends JsonSerializer<Object> {
    @Override
    public void serialize(Object value, JsonGenerator generator, SerializerProvider serializers) throws IOException {
        serializers.findValueSerializer(value.getClass()).serialize(value, generator, serializers);
    }

    @Override
    public void serializeWithType(
            Object value, JsonGenerator generator, SerializerProvider serializers, TypeSerializer typeSerializer)
            throws IOException {
        serializers
                .findValueSerializer(value.getClass())
                .serializeWithType(value, generator, serializers, typeSerializer);
    }
}
