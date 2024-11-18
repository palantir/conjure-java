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

import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.ServiceDefinition;
import com.squareup.javapoet.ClassName;

public final class Names {
    private Names() {}

    public static ClassName endpointsClassName(ServiceDefinition def, Options options) {
        String simpleName = "Dialogue" + def.getServiceName().getName().replaceAll("Service$", "") + "Endpoints";
        return maybeRelocate(def, options, simpleName);
    }

    public static ClassName blockingClassName(ServiceDefinition def, Options options) {
        String simpleName = def.getServiceName().getName() + "Blocking";
        return maybeRelocate(def, options, simpleName);
    }

    public static ClassName asyncClassName(ServiceDefinition def, Options options) {
        String simpleName = def.getServiceName().getName() + "Async";
        return maybeRelocate(def, options, simpleName);
    }

    static String endpointChannel(EndpointDefinition endpoint) {
        return endpoint.getEndpointName().get() + "Channel";
    }

    private static ClassName maybeRelocate(ServiceDefinition def, Options options, String simpleName) {
        return ClassName.get(
                Packages.getPrefixedPackage(def.getServiceName().getPackage(), options.packagePrefix()), simpleName);
    }
}
