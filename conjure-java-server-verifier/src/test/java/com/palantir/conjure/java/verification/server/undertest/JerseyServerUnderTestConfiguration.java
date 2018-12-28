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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.palantir.websecurity.WebSecurityConfigurable;
import com.palantir.websecurity.WebSecurityConfiguration;
import io.dropwizard.Configuration;
import javax.validation.Valid;

public final class JerseyServerUnderTestConfiguration extends Configuration implements WebSecurityConfigurable {

    @JsonProperty("webSecurity")
    @Valid
    private final WebSecurityConfiguration webSecurity = WebSecurityConfiguration.DEFAULT;

    @Override
    public WebSecurityConfiguration getWebSecurityConfiguration() {
        return webSecurity;
    }
}
