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

package com.palantir.conjure.java.compliance;

import com.palantir.conjure.java.client.jaxrs.JaxRsClient;
import com.palantir.conjure.java.com.palantir.conjure.verification.server.AutoDeserializeConfirmService;
import com.palantir.conjure.java.com.palantir.conjure.verification.server.AutoDeserializeService;
import com.palantir.conjure.java.com.palantir.conjure.verification.server.AutoDeserializeServiceBlocking;
import com.palantir.conjure.java.com.palantir.conjure.verification.server.SingleHeaderService;
import com.palantir.conjure.java.com.palantir.conjure.verification.server.SingleHeaderServiceBlocking;
import com.palantir.conjure.java.com.palantir.conjure.verification.server.SinglePathParamService;
import com.palantir.conjure.java.com.palantir.conjure.verification.server.SinglePathParamServiceBlocking;
import com.palantir.conjure.java.com.palantir.conjure.verification.server.SingleQueryParamService;
import com.palantir.conjure.java.com.palantir.conjure.verification.server.SingleQueryParamServiceBlocking;
import com.palantir.conjure.java.dialogue.serde.DefaultConjureRuntime;
import com.palantir.conjure.java.okhttp.HostMetricsRegistry;
import com.palantir.dialogue.JavaChannels;

public final class VerificationClients {
    private VerificationClients() {}

    private static final DefaultConjureRuntime DEFAULT_CONJURE_RUNTIME =
            DefaultConjureRuntime.builder().build();

    public static AutoDeserializeService autoDeserializeService(VerificationServerRule server) {
        return JaxRsClient.create(
                AutoDeserializeService.class,
                VerificationServerRule.userAgent,
                new HostMetricsRegistry(),
                server.getClientConfiguration());
    }

    public static AutoDeserializeServiceBlocking dialogueAutoDeserializeService(VerificationServerRule server) {
        return AutoDeserializeServiceBlocking.of(
                JavaChannels.create(server.getClientConfiguration()), DEFAULT_CONJURE_RUNTIME);
    }

    public static AutoDeserializeConfirmService confirmService(VerificationServerRule server) {
        return JaxRsClient.create(
                AutoDeserializeConfirmService.class,
                VerificationServerRule.userAgent,
                new HostMetricsRegistry(),
                server.getClientConfiguration());
    }

    public static SinglePathParamService singlePathParamService(VerificationServerRule server) {
        return JaxRsClient.create(
                SinglePathParamService.class,
                VerificationServerRule.userAgent,
                new HostMetricsRegistry(),
                server.getClientConfiguration());
    }

    public static SinglePathParamServiceBlocking dialogueSinglePathParamService(VerificationServerRule server) {
        return SinglePathParamServiceBlocking.of(
                JavaChannels.create(server.getClientConfiguration()), DEFAULT_CONJURE_RUNTIME);
    }

    public static SingleHeaderService singleHeaderService(VerificationServerRule server) {
        return JaxRsClient.create(
                SingleHeaderService.class,
                VerificationServerRule.userAgent,
                new HostMetricsRegistry(),
                server.getClientConfiguration());
    }

    public static SingleHeaderServiceBlocking dialogueSingleHeaderService(VerificationServerRule server) {
        return SingleHeaderServiceBlocking.of(
                JavaChannels.create(server.getClientConfiguration()), DEFAULT_CONJURE_RUNTIME);
    }

    public static SingleQueryParamService singleQueryParamService(VerificationServerRule server) {
        return JaxRsClient.create(
                SingleQueryParamService.class,
                VerificationServerRule.userAgent,
                new HostMetricsRegistry(),
                server.getClientConfiguration());
    }

    public static SingleQueryParamServiceBlocking dialogueSingleQueryParamService(VerificationServerRule server) {
        return SingleQueryParamServiceBlocking.of(
                JavaChannels.create(server.getClientConfiguration()), DEFAULT_CONJURE_RUNTIME);
    }
}
