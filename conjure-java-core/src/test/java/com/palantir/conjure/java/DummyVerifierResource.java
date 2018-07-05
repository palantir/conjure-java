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

import com.palantir.conjure.verifier.BinaryAlias;
import com.palantir.conjure.verifier.OptionalObject;
import com.palantir.conjure.verifier.PrimitivesObject;
import com.palantir.conjure.verifier.SimpleEnums;
import com.palantir.conjure.verifier.SimpleUnion;
import com.palantir.conjure.verifier.StringAlias;
import com.palantir.conjure.verifier.TestService;
import com.palantir.tokens.auth.AuthHeader;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.core.StreamingOutput;

public final class DummyVerifierResource implements TestService {

    @Override
    public String getString() {
        return null;
    }

    @Override
    public String getStringAuth(AuthHeader authHeader) {
        return null;
    }

    @Override
    public String echoHeaderParam(String someHeader) {
        return null;
    }

    @Override
    public String echoPathParam(String content) {
        return null;
    }

    @Override
    public Optional<String> echoQueryParam(String query) {
        return Optional.empty();
    }

    @Override
    public void echoOptionalQueryParam(Optional<String> query, Optional<String> returns) {

    }

    @Override
    public PrimitivesObject simpleBody(SimpleUnion body) {
        return null;
    }

    @Override
    public PrimitivesObject getPrimitivesObject() {
        return null;
    }

    @Override
    public OptionalObject getOptionalObject() {
        return null;
    }

    @Override
    public List<SimpleUnion> getUnions() {
        return null;
    }

    @Override
    public List<SimpleEnums> getEnums() {
        return null;
    }

    @Override
    public StringAlias getAlias() {
        return null;
    }

    @Override
    public StreamingOutput getBinary() {
        return null;
    }

    @Override
    public void sendBinary(InputStream body) {

    }

    @Override
    public PrimitivesObject getExtraFields() {
        return null;
    }

    @Override
    public BinaryAlias echoBinaryAlias(BinaryAlias body) {
        return null;
    }
}
