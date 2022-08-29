/*
 * (c) Copyright 2022 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.undertow.processor.sample;

import com.google.common.collect.Iterables;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.undertow.annotations.CollectionParamDecoder;
import com.palantir.conjure.java.undertow.annotations.Handle;
import com.palantir.conjure.java.undertow.annotations.HttpMethod;
import com.palantir.conjure.java.undertow.annotations.ParamDecoder;
import com.palantir.ri.ResourceIdentifier;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("TooManyArguments")
public interface DefaultDecoderService {

    @Handle(method = HttpMethod.GET, path = "/queryParam")
    String queryParam(
            @Handle.QueryParam("stringParam") String stringParam,
            @Handle.QueryParam("booleanParam") Boolean booleanParam,
            @Handle.QueryParam("stringSetParam") Set<String> stringSetParam,
            @Handle.QueryParam("stringListParam") List<String> stringListParam,
            @Handle.QueryParam("optionalStringParam") Optional<String> optionalStringParam,
            @Handle.QueryParam("floatBoxed") Float floatBoxed,
            @Handle.QueryParam("floatUnboxed") float floatUnboxed,
            @Handle.QueryParam("optionalInt") OptionalInt optionalIntParam,
            @Handle.QueryParam("dateTime") OffsetDateTime dateTimeParam,
            @Handle.QueryParam("ridSetParam") Set<ResourceIdentifier> ridSetParam,
            @Handle.QueryParam("optionalSafeLongParam") Optional<SafeLong> optionalSafeLongParam,
            @Handle.QueryParam("uuidParam") UUID uuidParam,
            @Handle.QueryParam(value = "decoderParam", decoder = StringCollectionDecoder.class) String decoderParam,
            @Handle.QueryParam("constructor") Constructor constructor,
            @Handle.QueryParam("ofFactory") OfFactory ofFactory,
            @Handle.QueryParam("valueOfFactory") ValueOfFactory valueOfFactory,
            @Handle.QueryParam("fromStringFactory") FromStringFactory fromStringFactory,
            @Handle.QueryParam("createFactory") CreateFactory createFactory);

    @Handle(method = HttpMethod.POST, path = "/formParam")
    String formParam(
            @Handle.FormParam("stringParam") String stringParam,
            @Handle.FormParam("booleanParam") Boolean booleanParam,
            @Handle.FormParam("stringSetParam") Set<String> stringSetParam,
            @Handle.FormParam("stringListParam") List<String> stringListParam,
            @Handle.FormParam("optionalStringParam") Optional<String> optionalStringParam,
            @Handle.FormParam("floatBoxed") Float floatBoxed,
            @Handle.FormParam("floatUnboxed") float floatUnboxed,
            @Handle.FormParam("optionalInt") OptionalInt optionalIntParam,
            @Handle.FormParam("dateTime") OffsetDateTime dateTimeParam,
            @Handle.FormParam("ridSetParam") Set<ResourceIdentifier> ridSetParam,
            @Handle.FormParam("optionalSafeLongParam") Optional<SafeLong> optionalSafeLongParam,
            @Handle.FormParam("uuidParam") UUID uuidParam,
            @Handle.FormParam(value = "decoderParam", decoder = StringCollectionDecoder.class) String decoderParam,
            @Handle.FormParam("constructor") Constructor constructor,
            @Handle.FormParam("ofFactory") OfFactory ofFactory,
            @Handle.FormParam("valueOfFactory") ValueOfFactory valueOfFactory,
            @Handle.FormParam("fromStringFactory") FromStringFactory fromStringFactory,
            @Handle.FormParam("createFactory") CreateFactory createFactory);

    @Handle(method = HttpMethod.GET, path = "/headers")
    String headers(
            @Handle.Header("stringParam") String stringParam,
            @Handle.Header("booleanParam") Boolean booleanParam,
            @Handle.Header("stringSetParam") Set<String> stringSetParam,
            @Handle.Header("stringListParam") List<String> stringListParam,
            @Handle.Header("optionalStringParam") Optional<String> optionalStringParam,
            @Handle.Header("floatBoxed") Float floatBoxed,
            @Handle.Header("floatUnboxed") float floatUnboxed,
            @Handle.Header(value = "decoderParam", decoder = StringCollectionDecoder.class) String decoderParam,
            @Handle.Header("constructor") Constructor constructor,
            @Handle.Header("ofFactory") OfFactory ofFactory,
            @Handle.Header("valueOfFactory") ValueOfFactory valueOfFactory,
            @Handle.Header("fromStringFactory") FromStringFactory fromStringFactory,
            @Handle.Header("createFactory") CreateFactory createFactory);

    @Handle(
            method = HttpMethod.GET,
            path = "/pathParam/{stringParam}/{booleanParam}/{decoderParam}/{floatBoxed}/{floatUnboxed}/{bigInt}"
                    + "/{constructor}/{ofFactory}/{valueOfFactory}")
    String pathParam(
            @Handle.PathParam String stringParam,
            @Handle.PathParam Boolean booleanParam,
            @Handle.PathParam(decoder = StringDecoder.class) String decoderParam,
            @Handle.PathParam Float floatBoxed,
            @Handle.PathParam float floatUnboxed,
            @Handle.PathParam Constructor constructor,
            @Handle.PathParam OfFactory ofFactory,
            @Handle.PathParam ValueOfFactory valueOfFactory,
            @Handle.PathParam FromStringFactory fromStringFactory,
            @Handle.PathParam CreateFactory createFactory);

    enum StringCollectionDecoder implements CollectionParamDecoder<String> {
        INSTANCE;

        @Override
        public String decode(Collection<String> value) {
            return Iterables.getOnlyElement(value);
        }
    }

    enum StringDecoder implements ParamDecoder<String> {
        INSTANCE;

        @Override
        public String decode(String value) {
            return value;
        }
    }

    final class Constructor {

        public Constructor(String _value) {}
    }

    final class OfFactory {

        private OfFactory() {}

        public static OfFactory of(String _value) {
            return new OfFactory();
        }
    }

    final class ValueOfFactory {

        private ValueOfFactory() {}

        public static ValueOfFactory valueOf(String _value) {
            return new ValueOfFactory();
        }
    }

    final class FromStringFactory {

        private FromStringFactory() {}

        public static FromStringFactory fromString(String _value) {
            return new FromStringFactory();
        }
    }

    final class CreateFactory {

        private CreateFactory() {}

        public static CreateFactory create(String _value) {
            return new CreateFactory();
        }
    }
}
