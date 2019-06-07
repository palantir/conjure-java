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

package com.palantir.conjure.java.undertow.lib;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.palantir.logsafe.Preconditions;
import io.undertow.server.HttpHandler;
import io.undertow.util.HttpString;
import java.util.Optional;

/**
 * An {@link Endpoint} represents a single rpc method. End points provide a location, tuple of
 * {@link Endpoint#method()} and {@link Endpoint#template()}, as well as an implementation, the
 * {@link Endpoint#handler()}.
 */
public interface Endpoint {

    /** HTTP method which matches this {@link Endpoint}. See {@link io.undertow.util.Methods}. */
    HttpString method();

    /**
     * Conjure formatted http path template.
     * For example, this may take the form <pre>/ping</pre> or <pre>/object/{objectId}</pre>.
     * For more information, see the
     * <a href="https://palantir.github.io/conjure/#/docs/spec/conjure_definitions?id=pathstring">
     * specification for conjure path strings</a>.
     */
    String template();

    /** Undertow {@link HttpHandler} which provides the endpoint implementation. */
    HttpHandler handler();

    /** Simple name of the service which provides this endpoint. This data may be used for metric instrumentation. */
    String serviceName();

    /** Simple name of the endpoint method. This data may be used for metric instrumentation. */
    String name();

    /** Is present if the method is deprecated, and contains its corresponding deprecating documentation. */
    default Optional<String> deprecated() {
        return Optional.empty();
    }

    static Builder builder() {
        return new Builder();
    }

    final class Builder {

        private Builder() {}

        private HttpString method;
        private String template;
        private HttpHandler handler;
        private String serviceName;
        private String name;
        private Optional<String> deprecated = Optional.empty();

        @CanIgnoreReturnValue
        public Builder method(HttpString value) {
            method = Preconditions.checkNotNull(value, "method is required");
            return this;
        }

        @CanIgnoreReturnValue
        public Builder template(String value) {
            template = Preconditions.checkNotNull(value, "template is required");
            return this;
        }

        @CanIgnoreReturnValue
        public Builder handler(HttpHandler value) {
            handler = Preconditions.checkNotNull(value, "handler is required");
            return this;
        }

        @CanIgnoreReturnValue
        public Builder serviceName(String value) {
            serviceName = Preconditions.checkNotNull(value, "serviceName is required");
            return this;
        }

        @CanIgnoreReturnValue
        public Builder name(String value) {
            name = Preconditions.checkNotNull(value, "name is required");
            return this;
        }

        @CanIgnoreReturnValue
        public Builder deprecated(Optional<String> value) {
            deprecated = Preconditions.checkNotNull(value, "deprecated is required");
            return this;
        }

        @CanIgnoreReturnValue
        public Builder from(Endpoint endpoint) {
            method = endpoint.method();
            template = endpoint.template();
            handler = endpoint.handler();
            serviceName = endpoint.serviceName();
            name = endpoint.name();
            deprecated = endpoint.deprecated();
            return this;
        }

        public Endpoint build() {
            Preconditions.checkNotNull(method, "method is required");
            Preconditions.checkNotNull(template, "template is required");
            Preconditions.checkNotNull(handler, "handler is required");
            Preconditions.checkNotNull(serviceName, "serviceName is required");
            Preconditions.checkNotNull(name, "name is required");
            // no need to check for deprecated, it is always set.
            return new Endpoint() {
                @Override
                public HttpString method() {
                    return method;
                }

                @Override
                public String template() {
                    return template;
                }

                @Override
                public HttpHandler handler() {
                    return handler;
                }

                @Override
                public String serviceName() {
                    return serviceName;
                }

                @Override
                public String name() {
                    return name;
                }

                @Override
                public Optional<String> deprecated() {
                    return deprecated;
                }
            };
        }
    }
}
