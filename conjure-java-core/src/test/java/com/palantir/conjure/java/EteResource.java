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
import com.palantir.product.LongAlias;
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
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.StreamingOutput;

@SuppressWarnings("checkstyle:designforextension")
public class EteResource implements EteService {
    @Override
    public String string(AuthHeader _authHeader) {
        return "Hello, world!";
    }

    @Override
    public int integer(AuthHeader _authHeader) {
        return 1234;
    }

    @Override
    public double double_(AuthHeader _authHeader) {
        return 1 / 3d;
    }

    @Override
    public boolean boolean_(AuthHeader _authHeader) {
        return true;
    }

    @Override
    public SafeLong safelong(AuthHeader _authHeader) {
        return SafeLong.of(12345L);
    }

    @Override
    public ResourceIdentifier rid(AuthHeader _authHeader) {
        return ResourceIdentifier.of("ri.foundry.main.dataset.1234");
    }

    @Override
    public BearerToken bearertoken(AuthHeader _authHeader) {
        return BearerToken.valueOf("fake");
    }

    @Override
    public Optional<String> optionalString(AuthHeader _authHeader) {
        return Optional.of("foo");
    }

    @Override
    public Optional<String> optionalEmpty(AuthHeader _authHeader) {
        return Optional.empty();
    }

    @Override
    public OffsetDateTime datetime(AuthHeader _authHeader) {
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(1234L), ZoneId.from(ZoneOffset.UTC));
    }

    @Override
    public Streaming binary(AuthHeader _authHeader) {
        return outputStream -> outputStream.write("Hello, world!".getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String path(AuthHeader _authHeader, String param) {
        return param;
    }

    @Override
    public long externalLongPath(AuthHeader _authHeader, long param) {
        return param;
    }

    @Override
    public Optional<Long> optionalExternalLongQuery(AuthHeader _authHeader, Optional<Long> param) {
        return param;
    }

    @Override
    public StringAliasExample notNullBody(AuthHeader _authHeader, StringAliasExample notNullBody) {
        return notNullBody;
    }

    @Override
    public StringAliasExample aliasOne(AuthHeader _authHeader, StringAliasExample queryParamName) {
        return queryParamName;
    }

    @Override
    public StringAliasExample optionalAliasOne(
            @NotNull AuthHeader _authHeader, Optional<StringAliasExample> queryParamName) {
        return queryParamName.orElseGet(() -> StringAliasExample.of("foo"));
    }

    @Override
    public NestedStringAliasExample aliasTwo(@NotNull AuthHeader _authHeader, NestedStringAliasExample queryParamName) {
        return queryParamName;
    }

    @Override
    public StringAliasExample notNullBodyExternalImport(AuthHeader _authHeader, StringAliasExample notNullBody) {
        return notNullBody;
    }

    @Override
    public Optional<StringAliasExample> optionalBodyExternalImport(
            AuthHeader _authHeader, Optional<StringAliasExample> body) {
        return body;
    }

    @Override
    public Optional<StringAliasExample> optionalQueryExternalImport(
            AuthHeader _authHeader, Optional<StringAliasExample> query) {
        return query;
    }

    @Override
    public void noReturn(AuthHeader _authHeader) {
        // nop
    }

    @Override
    public SimpleEnum enumQuery(AuthHeader _authHeader, SimpleEnum value) {
        return value;
    }

    @Override
    public List<SimpleEnum> enumListQuery(AuthHeader _authHeader, List<SimpleEnum> value) {
        return value;
    }

    @Override
    public Optional<SimpleEnum> optionalEnumQuery(AuthHeader _authHeader, Optional<SimpleEnum> value) {
        return value;
    }

    @Override
    public SimpleEnum enumHeader(AuthHeader _authHeader, SimpleEnum headerParameter) {
        return headerParameter;
    }

    @Override
    public Optional<LongAlias> aliasLongEndpoint(AuthHeader _authHeader, Optional<LongAlias> input) {
        return input;
    }

    @Override
    public void complexQueryParameters(
            AuthHeader _authHeader,
            ResourceIdentifier _datasetRid,
            Set<StringAliasExample> _strings,
            Set<Long> _longs,
            Set<Integer> _ints) {
        // nop
    }

    @Override
    public void receiveListOfOptionals(AuthHeader _authHeader, List<Optional<String>> _value) {}

    @Override
    public void receiveSetOfOptionals(AuthHeader _authHeader, Set<Optional<String>> _value) {}

    @Override
    public void throwsCheckedException(@NotNull AuthHeader _authHeader) {
        // nop
    }

    interface Streaming extends StreamingOutput, BinaryResponseBody {}
}
