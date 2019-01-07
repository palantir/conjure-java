/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java;

import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.product.EteService;
import com.palantir.product.NestedStringAliasExample;
import com.palantir.product.SimpleEnum;
import com.palantir.product.StringAliasExample;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.StreamingOutput;

@SuppressWarnings("checkstyle:designforextension")
public class EteResource implements EteService {
    @Override
    public String string(AuthHeader authHeader) {
        return "Hello, world!";
    }

    @Override
    public int integer(AuthHeader authHeader) {
        return 1234;
    }

    @Override
    public double double_(AuthHeader authHeader) {
        return 1 / 3d;
    }

    @Override
    public boolean boolean_(AuthHeader authHeader) {
        return true;
    }

    @Override
    public SafeLong safelong(AuthHeader authHeader) {
        return SafeLong.of(12345L);
    }

    @Override
    public ResourceIdentifier rid(AuthHeader authHeader) {
        return ResourceIdentifier.of("ri.foundry.main.dataset.1234");
    }

    @Override
    public BearerToken bearertoken(AuthHeader authHeader) {
        return BearerToken.valueOf("fake");
    }

    @Override
    public Optional<String> optionalString(AuthHeader authHeader) {
        return Optional.of("foo");
    }

    @Override
    public Optional<String> optionalEmpty(AuthHeader authHeader) {
        return Optional.empty();
    }

    @Override
    public OffsetDateTime datetime(AuthHeader authHeader) {
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(1234L), ZoneId.from(ZoneOffset.UTC));
    }

    @Override
    public Streaming binary(AuthHeader authHeader) {
        return outputStream -> outputStream.write("Hello, world!".getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public StringAliasExample notNullBody(AuthHeader authHeader, StringAliasExample notNullBody) {
        return notNullBody;
    }

    @Override
    public StringAliasExample aliasOne(AuthHeader authHeader, StringAliasExample queryParamName) {
        return queryParamName;
    }

    @Override
    public StringAliasExample optionalAliasOne(@NotNull AuthHeader authHeader,
            Optional<StringAliasExample> queryParamName) {
        return queryParamName.orElse(StringAliasExample.of("foo"));
    }

    @Override
    public NestedStringAliasExample aliasTwo(@NotNull AuthHeader authHeader, NestedStringAliasExample queryParamName) {
        return queryParamName;
    }

    @Override
    public StringAliasExample notNullBodyExternalImport(AuthHeader authHeader, StringAliasExample notNullBody) {
        return notNullBody;
    }

    @Override
    public Optional<StringAliasExample> optionalBodyExternalImport(AuthHeader authHeader,
            Optional<StringAliasExample> body) {
        return body;
    }

    @Override
    public Optional<StringAliasExample> optionalQueryExternalImport(AuthHeader authHeader,
            Optional<StringAliasExample> query) {
        return query;
    }

    @Override
    public void noReturn(AuthHeader authHeader) {
        // nop
    }

    @Override
    public SimpleEnum enumQuery(AuthHeader authHeader, SimpleEnum value) {
        return value;
    }

    @Override
    public List<SimpleEnum> enumListQuery(AuthHeader authHeader, List<SimpleEnum> value) {
        return value;
    }

    @Override
    public Optional<SimpleEnum> optionalEnumQuery(AuthHeader authHeader, Optional<SimpleEnum> value) {
        return value;
    }

    interface Streaming extends StreamingOutput, BinaryResponseBody {}
}
