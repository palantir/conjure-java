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
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.conjure.java.server.jersey.ConjureJerseyFeature;
import com.palantir.conjure.verification.client.AutoDeserializeService;
import com.palantir.websecurity.WebSecurityBundle;
import io.dropwizard.Application;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.FuzzyEnumModule;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public final class JerseyServerUnderTestApplication extends Application<JerseyServerUnderTestConfiguration> {

    @Override
    public void initialize(Bootstrap<JerseyServerUnderTestConfiguration> bootstrap) {
        ObjectMapper remotingObjectMapper = ObjectMappers.newServerObjectMapper()
                // needs discoverable subtype resolver for DW polymorphic configuration mechanism
                .setSubtypeResolver(new DiscoverableSubtypeResolver())
                .registerModule(new FuzzyEnumModule());
        bootstrap.setObjectMapper(remotingObjectMapper);
        bootstrap.addBundle(new WebSecurityBundle());
    }

    @Override
    public void run(JerseyServerUnderTestConfiguration configuration, Environment environment) {
        environment.jersey().register(
                Reflection.newProxy(AutoDeserializeService.class, new EchoResourceInvocationHandler()));

        // must add ConjureJerseyFeature to map conjure error types.
        environment.jersey().register(ConjureJerseyFeature.INSTANCE);
    }
}
