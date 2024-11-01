/*
 * (c) Copyright 2024 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.types;

import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.spec.ErrorNamespace;
import com.palantir.conjure.spec.TypeName;
import com.squareup.javapoet.ClassName;
import java.util.Map;

public final class EndpointErrorMapper {
    private final Map<TypeName, ErrorNamespace> errorNameToNamespace;
    private final Options options;

    public EndpointErrorMapper(Map<TypeName, ErrorNamespace> errorNameToNamespace, Options options) {
        this.options = options;
        this.errorNameToNamespace = errorNameToNamespace;
    }

    public com.squareup.javapoet.TypeName getClassName(TypeName errorName) {
        if (errorNameToNamespace.containsKey(errorName)) {
            return ClassName.get(
                    Packages.getPrefixedPackage(errorName.getPackage(), options.packagePrefix()),
                    getErrorsClassName(errorNameToNamespace.get(errorName).get()),
                    errorName.getName());
        } else {
            throw new IllegalStateException("Unknown error: " + errorName);
        }
    }

    // TODO(pm): move this to a utils class.
    private static String getErrorsClassName(String namespace) {
        return "Server" + namespace + "Errors";
    }
}
