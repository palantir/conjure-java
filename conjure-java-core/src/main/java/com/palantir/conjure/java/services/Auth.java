/*
 * (c) Copyright 2020 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.services;

import com.palantir.conjure.spec.AuthType;
import com.palantir.conjure.spec.CookieAuthType;
import com.palantir.conjure.spec.HeaderAuthType;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import com.squareup.javapoet.ParameterSpec;

public final class Auth {

    public static final String AUTH_HEADER_NAME = "Authorization";
    public static final String AUTH_HEADER_PARAM_NAME = "authHeader";
    public static final String COOKIE_AUTH_PARAM_NAME = "token";

    private Auth() {}

    public static ParameterSpec authParam(AuthType auth) {
        return auth.accept(new AuthType.Visitor<ParameterSpec>() {
            @Override
            public ParameterSpec visitHeader(HeaderAuthType value) {
                return ParameterSpec.builder(AuthHeader.class, AUTH_HEADER_PARAM_NAME)
                        .build();
            }

            @Override
            public ParameterSpec visitCookie(CookieAuthType value) {
                return ParameterSpec.builder(BearerToken.class, COOKIE_AUTH_PARAM_NAME)
                        .build();
            }

            @Override
            public ParameterSpec visitUnknown(String unknownType) {
                throw new SafeIllegalStateException("unknown auth type", SafeArg.of("type", unknownType));
            }
        });
    }
}
