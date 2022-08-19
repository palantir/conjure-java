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

package com.palantir.conjure.java.undertow.annotations;

import com.google.common.collect.Collections2;
import com.palantir.conjure.java.undertow.lib.Deserializer;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData.FormValue;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormParserFactory;
import io.undertow.util.Headers;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;

public final class FormParamDeserializer<T> implements Deserializer<T> {

    private static final FormParserFactory FORM_PARSER_FACTORY =
            FormParserFactory.builder().build();

    private final String parameter;
    private final CollectionParamDecoder<? extends T> decoder;

    public FormParamDeserializer(String parameter, CollectionParamDecoder<? extends T> decoder) {
        this.parameter = Preconditions.checkNotNull(parameter, "Form parameter name is required");
        this.decoder = Preconditions.checkNotNull(decoder, "Decoder is required");
    }

    @Override
    public T deserialize(HttpServerExchange exchange) throws IOException {
        FormDataParser parser = FORM_PARSER_FACTORY.createParser(exchange);
        if (parser == null) {
            throw new SafeIllegalArgumentException(
                    "Failed to create form data parser",
                    SafeArg.of("contentType", exchange.getRequestHeaders().getFirst(Headers.CONTENT_TYPE)));
        }

        Deque<FormValue> maybeValues = parser.parseBlocking().get(parameter);
        return Preconditions.checkNotNull(decoder.decode(getValues(maybeValues)), "Decoder produced a null value");
    }

    private Collection<String> getValues(Deque<FormValue> maybeValues) {
        if (maybeValues == null) {
            return Collections.emptyList();
        }

        for (FormValue value : maybeValues) {
            Preconditions.checkArgument(!value.isFileItem(), "Form value was a file item");
        }

        return Collections.unmodifiableCollection(Collections2.transform(maybeValues, FormValue::getValue));
    }

    @Override
    public String toString() {
        return "QueryParamDeserializer{parameter='" + parameter + '\'' + ", decoder=" + decoder + '}';
    }
}
