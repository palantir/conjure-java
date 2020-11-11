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

package com.palantir.conjure.java.services;

import com.google.common.base.Strings;
import com.google.common.collect.Streams;
import com.palantir.conjure.java.util.Javadoc;
import com.palantir.conjure.spec.EndpointDefinition;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ServiceGenerators {

    public static Optional<String> getJavaDoc(EndpointDefinition endpointDef) {
        return getJavaDocInternal(endpointDef, false);
    }

    public static String getJavaDocWithRequestLine(EndpointDefinition endpointDef) {
        return getJavaDocInternal(endpointDef, true).get();
    }

    private static Optional<String> getJavaDocInternal(EndpointDefinition endpointDef, boolean includeRequestLine) {
        Optional<String> depr = endpointDef.getDeprecated().map(Javadoc::getDeprecatedJavadoc);

        Optional<String> docs = endpointDef.getDocs().map(Javadoc::render);

        Optional<String> requestLine = Optional.empty();

        if (includeRequestLine) {
            requestLine = Optional.of(Javadoc.getRequestLine(endpointDef.getHttpMethod(), endpointDef.getHttpPath()));
        }

        Optional<String> params = Optional.ofNullable(Strings.emptyToNull(endpointDef.getArgs().stream()
                .flatMap(argument -> Streams.stream(Javadoc.getParameterJavadoc(argument, endpointDef)))
                .collect(Collectors.joining("\n"))));

        StringBuilder sb = new StringBuilder();
        docs.ifPresent(sb::append);
        requestLine.ifPresent(sb::append);
        params.ifPresent(sb::append);
        depr.ifPresent(sb::append);
        return sb.length() > 0 ? Optional.of(sb.toString()) : Optional.empty();
    }

    private ServiceGenerators() {}
}
