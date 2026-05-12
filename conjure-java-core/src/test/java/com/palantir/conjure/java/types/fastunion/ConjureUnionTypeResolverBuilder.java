/*
 * (c) Copyright 2026 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.types.fastunion;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import java.util.Collection;

/**
 * Resolver builder that swaps in {@link ConjureUnionTypeDeserializer} in place of Jackson's
 * stock {@code AsPropertyTypeDeserializer}. Plug in via {@code @JsonTypeResolver(...)} on the
 * generated union interface.
 */
public final class ConjureUnionTypeResolverBuilder extends StdTypeResolverBuilder {

    @Override
    public TypeDeserializer buildTypeDeserializer(
            DeserializationConfig config, JavaType baseType, Collection<NamedType> subtypes) {
        if (_idType == JsonTypeInfo.Id.NONE) {
            return null;
        }
        TypeIdResolver idRes = idResolver(
                config, baseType, subTypeValidator(config), subtypes, /* forSer */ false, /* forDeser */ true);
        JavaType defaultImpl = defineDefaultImpl(config, baseType);
        return new ConjureUnionTypeDeserializer(
                baseType, idRes, _typeProperty, /* typeIdVisible */ false, defaultImpl);
    }
}
