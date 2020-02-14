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

import com.palantir.conjure.spec.ServiceDefinition;
import com.squareup.javapoet.ClassName;

public final class Names {
    private Names() {}

    public static ClassName publicClassName(ServiceDefinition def) {
        return serviceClassName("Dialogue", def);
    }

    public static ClassName blockingClassName(ServiceDefinition def) {
        return serviceClassName("Blocking", def);
    }

    public static ClassName asyncClassName(ServiceDefinition def) {
        return serviceClassName("Async", def);
    }

    private static ClassName serviceClassName(String prefix, ServiceDefinition def) {
        String simpleName = prefix + def.getServiceName().getName();
        return ClassName.get(def.getServiceName().getPackage(), simpleName);
    }
}
