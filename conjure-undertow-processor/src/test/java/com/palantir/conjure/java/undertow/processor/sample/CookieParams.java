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

import com.palantir.conjure.java.undertow.annotations.Handle;
import com.palantir.conjure.java.undertow.annotations.HttpMethod;
import com.palantir.conjure.java.undertow.annotations.ParamDecoder;
import com.palantir.tokens.auth.BearerToken;
import java.util.Optional;
import java.util.OptionalInt;

public interface CookieParams {

    @Handle(method = HttpMethod.GET, path = "/cookies")
    String cookieParams(
            @Handle.Cookie(value = "stringCookie") String stringCookie,
            @Handle.Cookie(value = "optionalStringCookie") Optional<String> optionalStringCookie,
            @Handle.Cookie(value = "optionalIntCookie") OptionalInt optionalIntCookie,
            @Handle.Cookie(value = "decoderCookie", decoder = StringParamDecoder.class) String decoderCookie,
            @Handle.Cookie(value = "AUTH_TOKEN") BearerToken token);

    enum StringParamDecoder implements ParamDecoder<String> {
        INSTANCE;

        @Override
        public String decode(String value) {
            return value;
        }
    }
}
