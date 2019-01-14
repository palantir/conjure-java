/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.services.dialogue;

import com.google.common.base.Preconditions;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.product.Complex;
import com.palantir.product.UndertowDialogueSampleService;
import com.palantir.tokens.auth.AuthHeader;
import java.nio.charset.StandardCharsets;

public final class DialogueTestResource implements UndertowDialogueSampleService {
    public static final int INT = 42;
    public static final String STRING = "42";
    public static final AuthHeader EXPECTED_AUTH = AuthHeader.valueOf("foo");

    @Override
    public String string(AuthHeader authHeader) {
        Preconditions.checkArgument(authHeader.equals(EXPECTED_AUTH));
        return STRING;
    }

    @Override
    public String stringEcho(AuthHeader authHeader, String string) {
        Preconditions.checkArgument(authHeader.equals(EXPECTED_AUTH));
        return string;
    }

    @Override
    public int integer(AuthHeader authHeader) {
        Preconditions.checkArgument(authHeader.equals(EXPECTED_AUTH));
        return INT;
    }

    @Override
    public int integerEcho(AuthHeader authHeader, int integer) {
        Preconditions.checkArgument(authHeader.equals(EXPECTED_AUTH));
        return integer;
    }

    @Override
    public String queryEcho(AuthHeader authHeader, int integer) {
        return Integer.toString(integer);
    }

    @Override
    public Complex complex(AuthHeader authHeader) {
        Preconditions.checkArgument(authHeader.equals(EXPECTED_AUTH));
        return Complex.of(STRING, INT);
    }

    @Override
    public Complex complexEcho(AuthHeader authHeader, Complex complex) {
        Preconditions.checkArgument(authHeader.equals(EXPECTED_AUTH));
        return complex;
    }

    @Override
    public BinaryResponseBody binaryEcho(AuthHeader authHeader, String string) {
        return outputStream -> outputStream.write(string.getBytes(StandardCharsets.UTF_8));
    }
}
