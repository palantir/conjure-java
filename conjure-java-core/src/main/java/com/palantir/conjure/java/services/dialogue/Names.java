/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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
