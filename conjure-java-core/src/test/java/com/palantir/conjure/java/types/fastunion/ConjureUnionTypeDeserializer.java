/*
 * (c) Copyright 2026 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.types.fastunion;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import java.io.IOException;

/**
 * Polymorphic deserializer that only weaves the discriminator into a TokenBuffer when
 * dispatching to the Unknown default-impl variant. Known variants stream directly without
 * buffering, eliminating the {@code visible=true} overhead in the common case while still
 * preserving the wire-spec round-trip behavior for unknown variants.
 */
public final class ConjureUnionTypeDeserializer extends AsPropertyTypeDeserializer {

    @SuppressWarnings("deprecation")
    public ConjureUnionTypeDeserializer(
            JavaType bt,
            TypeIdResolver idRes,
            String typePropertyName,
            boolean typeIdVisible,
            JavaType defaultImpl) {
        // Force typeIdVisible=false in the parent; this class handles selective weaving.
        super(bt, idRes, typePropertyName, false, defaultImpl);
    }

    private ConjureUnionTypeDeserializer(ConjureUnionTypeDeserializer src, BeanProperty property) {
        super(src, property);
    }

    @Override
    public TypeDeserializer forProperty(BeanProperty prop) {
        return (prop == _property) ? this : new ConjureUnionTypeDeserializer(this, prop);
    }

    @Override
    @SuppressWarnings("checkstyle:HiddenField")
    protected Object _deserializeTypedForId(
            JsonParser p, DeserializationContext ctxt, TokenBuffer tb, String typeId) throws IOException {
        JsonDeserializer<Object> deser = _findDeserializer(ctxt, typeId);
        if (deser == _defaultImplDeserializer) {
            if (tb == null) {
                tb = ctxt.bufferForInputBuffering(p);
            }
            tb.writeFieldName(p.currentName());
            tb.writeString(typeId);
        }
        if (tb != null) {
            p.clearCurrentToken();
            p = JsonParserSequence.createFlattened(false, tb.asParser(p), p);
        }
        if (p.currentToken() != JsonToken.END_OBJECT) {
            p.nextToken();
        }
        return deser.deserialize(p, ctxt);
    }
}
