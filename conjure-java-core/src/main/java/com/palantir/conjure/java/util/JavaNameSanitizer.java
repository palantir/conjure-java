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

package com.palantir.conjure.java.util;

import com.google.common.collect.ImmutableSet;
import com.palantir.conjure.CaseConverter;
import com.palantir.conjure.spec.AuthType;
import com.palantir.conjure.spec.CookieAuthType;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.FieldName;
import com.palantir.conjure.spec.HeaderAuthType;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Optional;
import javax.lang.model.SourceVersion;

public final class JavaNameSanitizer {

    private static final ImmutableSet<String> RESERVED_FIELD_NAMES = ImmutableSet.of("memoizedHashCode");

    /**
     * Sanitizes the given {@link FieldName} for use as a java specifier.
     */
    public static String sanitize(FieldName fieldName) {
        String identifier = CaseConverter.toCase(fieldName.get(), CaseConverter.Case.LOWER_CAMEL_CASE);
        return sanitizeFieldName(identifier);
    }

    /** Returns a valid java name based on the input. */
    public static String sanitize(String name) {
        return SourceVersion.isName(name) ? name : escape(name);
    }

    /** Sanitizes parameter names taking into account default auth type parameter names. */
    public static String sanitizeParameterName(String input, EndpointDefinition endpoint) {
        String value = sanitize(input);
        Optional<String> maybeAuthParamName = endpoint.getAuth()
                .map(authType -> authType.accept(new AuthType.Visitor<String>() {
                    @Override
                    public String visitHeader(HeaderAuthType header) {
                        return "authHeader";
                    }

                    @Override
                    public String visitCookie(CookieAuthType cookie) {
                        return "cookieToken";
                    }

                    @Override
                    public String visitUnknown(String unknownType) {
                        throw new SafeIllegalArgumentException(
                                "Unknown auth type",
                                SafeArg.of("authType", unknownType));
                    }
                }));
        if (maybeAuthParamName.isPresent() && maybeAuthParamName.get().equals(value)) {
            return sanitizeParameterName(escape(value), endpoint);
        }
        return value;
    }

    private static String sanitizeFieldName(String name) {
        return SourceVersion.isName(name) && !RESERVED_FIELD_NAMES.contains(name) ? name : escape(name);
    }

    private static String escape(String input) {
        return input + '_';
    }

    private JavaNameSanitizer() {}
}
