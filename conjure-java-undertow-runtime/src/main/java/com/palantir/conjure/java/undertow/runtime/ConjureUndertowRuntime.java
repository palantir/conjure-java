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

package com.palantir.conjure.java.undertow.runtime;

import com.google.common.collect.Lists;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.palantir.conjure.java.undertow.lib.AuthorizationExtractor;
import com.palantir.conjure.java.undertow.lib.SerDe;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.logsafe.Preconditions;
import java.util.List;

/**
 * {@link ConjureUndertowRuntime} provides functionality required by generated handlers.
 */
public final class ConjureUndertowRuntime implements UndertowRuntime {

    private final SerDe serde;
    private final AuthorizationExtractor auth;

    private ConjureUndertowRuntime(Builder builder) {
        this.serde = new ConjureSerDe(builder.serializers.isEmpty()
                ? EncodingRegistry.getDefault() : new EncodingRegistry(builder.serializers));
        this.auth = new ConjureAuthorizationExtractor(serde);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public SerDe serde() {
        return serde;
    }

    @Override
    public AuthorizationExtractor auth() {
        return auth;
    }

    public static final class Builder {

        private final List<Encoding> serializers = Lists.newArrayList();

        private Builder() {}

        @CanIgnoreReturnValue
        public Builder serializer(Encoding value) {
            serializers.add(Preconditions.checkNotNull(value, "Value is required"));
            return this;
        }

        public ConjureUndertowRuntime build() {
            return new ConjureUndertowRuntime(this);
        }
    }
}
