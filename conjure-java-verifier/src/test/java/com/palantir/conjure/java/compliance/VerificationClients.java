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

import com.palantir.conjure.verification.AutoDeserializeConfirmService;
import com.palantir.conjure.verification.AutoDeserializeService;
import com.palantir.remoting3.jaxrs.JaxRsClient;

public final class VerificationClients {
    private VerificationClients() {}

    public static AutoDeserializeService autoDeserializeService(VerificationServerRule server) {
        return JaxRsClient.create(
                AutoDeserializeService.class,
                server.getUserAgent(),
                server.getClientConfiguration());
    }

    public static AutoDeserializeConfirmService confirmService(VerificationServerRule server) {
        return JaxRsClient.create(
                AutoDeserializeConfirmService.class,
                server.getUserAgent(),
                server.getClientConfiguration());
    }
}
