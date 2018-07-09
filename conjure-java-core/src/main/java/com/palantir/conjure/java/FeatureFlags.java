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

package com.palantir.conjure.java;

import com.palantir.conjure.java.services.JerseyServiceGenerator;
import com.palantir.conjure.java.services.Retrofit2ServiceGenerator;
import javax.validation.constraints.NotNull;

public enum FeatureFlags {
    /**
     * Instructs the {@link Retrofit2ServiceGenerator} to generate service
     * endpoints returning {@link java.util.concurrent.CompletableFuture} instead of {@code Call<>} objects.
     */
    RetrofitCompletableFutures,

    /**
     * Instructs the {@link JerseyServiceGenerator} to add {@link NotNull}
     * annotations to all auth parameters, as well as all non-optional body params on service endpoints.
     */
    RequireAuthParamsAndBodyParamsAreNotNull,
}
