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

import com.palantir.conjure.java.undertow.lib.Deserializer;
import com.palantir.logsafe.Preconditions;
import io.undertow.server.HttpServerExchange;
import java.util.Collections;
import java.util.Deque;

public final class QueryParamDeserializer<T> implements Deserializer<T> {

    private final String parameter;
    private final CollectionParamDecoder<? extends T> decoder;

    public QueryParamDeserializer(String parameter, CollectionParamDecoder<? extends T> decoder) {
        this.parameter = Preconditions.checkNotNull(parameter, "Query parameter name is required");
        this.decoder = Preconditions.checkNotNull(decoder, "Decoder is required");
    }

    @Override
    public T deserialize(HttpServerExchange exchange) {
        Deque<String> maybeValues = exchange.getQueryParameters().get(parameter);
        return Preconditions.checkNotNull(
                decoder.decode(
                        maybeValues == null
                                ? Collections.emptyList()
                                : Collections.unmodifiableCollection(maybeValues)),
                "Decoder produced a null value");
    }

    @Override
    public String toString() {
        return "QueryParamDeserializer{parameter='" + parameter + '\'' + ", decoder=" + decoder + '}';
    }
}
