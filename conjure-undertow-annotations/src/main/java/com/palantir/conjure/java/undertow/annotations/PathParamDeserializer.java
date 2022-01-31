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
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.PathTemplateMatch;
import java.util.Map;

public final class PathParamDeserializer<T> implements Deserializer<T> {

    private final String pathParameterName;
    private final ParamDecoder<? extends T> decoder;

    public PathParamDeserializer(String pathParameterName, ParamDecoder<? extends T> decoder) {
        this.pathParameterName = Preconditions.checkNotNull(pathParameterName, "Path parameter name is required");
        this.decoder = Preconditions.checkNotNull(decoder, "Decoder is required");
    }

    @Override
    public T deserialize(HttpServerExchange exchange) {
        Map<String, String> pathParams =
                exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
        String value = pathParams.get(pathParameterName);
        if (value == null) {
            throw new SafeIllegalStateException(
                    "Failed to find path parameter", SafeArg.of("pathParameter", pathParameterName));
        }
        return Preconditions.checkNotNull(decoder.decode(value), "Decoder produced a null value");
    }
}
