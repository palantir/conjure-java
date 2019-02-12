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

package com.palantir.conjure.java.undertow.lib;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.palantir.logsafe.Preconditions;

/**
 * {@link ServiceContext} provides state required by generated handlers.
 */
public final class ServiceContext {

    private final SerializerRegistry serializerRegistry;
    private final ServiceInstrumenter serviceInstrumenter;

    private ServiceContext(Builder builder) {
        this.serializerRegistry = Preconditions.checkNotNull(builder.serializerRegistry,
                "Missing required SerializerRegistry");
        this.serviceInstrumenter = Preconditions.checkNotNull(builder.serviceInstrumenter,
                "Missing required ServiceInstrumenter");
    }

    /**
     * {@link SerializerRegistry} for request and response body serialization.
     */
    public SerializerRegistry serializerRegistry() {
        return serializerRegistry;
    }

    /**
     * {@link ServiceInstrumenter} to apply metric instrumentation to exposed services.
     */
    public ServiceInstrumenter serviceInstrumenter() {
        return serviceInstrumenter;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private SerializerRegistry serializerRegistry;
        private ServiceInstrumenter serviceInstrumenter = new ServiceInstrumenter() {
            @Override
            public <T> T instrument(T serviceImplementation, Class<T> serviceInterface) {
                return serviceImplementation;
            }
        };

        private Builder() {}

        @CanIgnoreReturnValue
        public Builder serializerRegistry(SerializerRegistry value) {
            this.serializerRegistry = Preconditions.checkNotNull(value, "Value is required");
            return this;
        }

        @CanIgnoreReturnValue
        public Builder serviceInstrumenter(ServiceInstrumenter value) {
            this.serviceInstrumenter = Preconditions.checkNotNull(value, "Value is required");
            return this;
        }

        public ServiceContext build() {
            return new ServiceContext(this);
        }
    }
}
