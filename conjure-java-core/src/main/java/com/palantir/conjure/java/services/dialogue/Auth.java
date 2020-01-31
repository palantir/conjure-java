/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.services.dialogue;

import com.palantir.conjure.spec.AuthType;
import com.palantir.conjure.visitor.AuthTypeVisitor;
import com.palantir.tokens.auth.AuthHeader;
import com.squareup.javapoet.ParameterSpec;

public final class Auth {
    public static final String AUTH_HEADER_NAME = "Authorization";
    public static final String AUTH_HEADER_PARAM_NAME = "authHeader";

    private Auth() {}

    public static void verifyAuthTypeIsHeader(AuthType authType) {
        if (!authType.accept(AuthTypeVisitor.IS_HEADER)) {
            throw new UnsupportedOperationException("AuthType not supported by conjure-dialogue: " + authType);
        }
    }

    public static ParameterSpec authParam(AuthType auth) {
        verifyAuthTypeIsHeader(auth);
        return ParameterSpec.builder(AuthHeader.class, AUTH_HEADER_PARAM_NAME).build();
    }
}
