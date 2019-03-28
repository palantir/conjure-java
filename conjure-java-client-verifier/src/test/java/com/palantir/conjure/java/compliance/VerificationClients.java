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

import com.palantir.conjure.java.api.config.service.UserAgent;
import com.palantir.conjure.java.client.jaxrs.JaxRsClient;
import com.palantir.conjure.java.okhttp.HostMetricsRegistry;
import com.palantir.conjure.verification.server.AutoDeserializeConfirmService;
import com.palantir.conjure.verification.server.AutoDeserializeConfirmServiceJaxRsClient;
import com.palantir.conjure.verification.server.AutoDeserializeService;
import com.palantir.conjure.verification.server.AutoDeserializeServiceJaxRsClient;
import com.palantir.conjure.verification.server.SingleHeaderService;
import com.palantir.conjure.verification.server.SingleHeaderServiceJaxRsClient;
import com.palantir.conjure.verification.server.SinglePathParamService;
import com.palantir.conjure.verification.server.SinglePathParamServiceJaxRsClient;
import com.palantir.conjure.verification.server.SingleQueryParamService;
import com.palantir.conjure.verification.server.SingleQueryParamServiceJaxRsClient;

public final class VerificationClients {
    private VerificationClients() {}

    public static AutoDeserializeService autoDeserializeService(VerificationServerRule server) {
        return createJaxRsClient(server, AutoDeserializeService.class);
    }

    public static AutoDeserializeConfirmService confirmService(VerificationServerRule server) {
        return createJaxRsClient(server, AutoDeserializeConfirmService.class);
    }

    public static AutoDeserializeServiceJaxRsClient autoDeserializeServiceJaxRsClient(VerificationServerRule server) {
        return createJaxRsClient(server, AutoDeserializeServiceJaxRsClient.class);
    }

    public static AutoDeserializeConfirmServiceJaxRsClient confirmServiceJaxRsClient(VerificationServerRule server) {
        return createJaxRsClient(server, AutoDeserializeConfirmServiceJaxRsClient.class);
    }

    public static SinglePathParamService singlePathParamService(VerificationServerRule server) {
        return createJaxRsClient(server, SinglePathParamService.class);
    }

    public static SingleHeaderService singleHeaderService(VerificationServerRule server) {
        return createJaxRsClient(server, SingleHeaderService.class);
    }

    public static SingleQueryParamService singleQueryParamService(VerificationServerRule server) {
        return createJaxRsClient(server, SingleQueryParamService.class);
    }

    public static SinglePathParamServiceJaxRsClient singlePathParamServiceJaxRs(VerificationServerRule server) {
        return createJaxRsClient(server, SinglePathParamServiceJaxRsClient .class);
    }

    public static SingleHeaderServiceJaxRsClient  singleHeaderServiceJaxRs(VerificationServerRule server) {
        return createJaxRsClient(server, SingleHeaderServiceJaxRsClient.class);
    }

    public static SingleQueryParamServiceJaxRsClient singleQueryParamServiceJaxRs(VerificationServerRule server) {
        return createJaxRsClient(server, SingleQueryParamServiceJaxRsClient .class);
    }

    private static <T> T createJaxRsClient(VerificationServerRule server, Class<T> clazz) {
        return JaxRsClient.create(clazz, getUserAgent(), new HostMetricsRegistry(), server.getClientConfiguration());
    }

    private static UserAgent getUserAgent() {
        return UserAgent.of(UserAgent.Agent.of("test", "develop"));
    }
}
