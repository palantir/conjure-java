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

package com.palantir.conjure.java.verification.server.undertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.Reflection;
import com.palantir.conjure.java.com.palantir.conjure.verification.client.AutoDeserializeService;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.conjure.java.server.jersey.ConjureJerseyFeature;
import io.dropwizard.core.Application;
import io.dropwizard.core.Configuration;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.FuzzyEnumModule;

public final class JerseyServerUnderTestApplication extends Application<Configuration> {

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
        ObjectMapper remotingObjectMapper = ObjectMappers.newServerObjectMapper()
                // needs discoverable subtype resolver for DW polymorphic configuration mechanism
                .setSubtypeResolver(new DiscoverableSubtypeResolver())
                .registerModule(new FuzzyEnumModule());
        bootstrap.setObjectMapper(remotingObjectMapper);
    }

    @Override
    @SuppressWarnings("ProxyNonConstantType")
    public void run(Configuration _configuration, Environment environment) {
        environment
                .jersey()
                .register(Reflection.newProxy(AutoDeserializeService.class, new EchoResourceInvocationHandler()));

        // must register ConjureJerseyFeature to map conjure error types.
        environment.jersey().register(ConjureJerseyFeature.INSTANCE);
    }
}
