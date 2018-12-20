/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.undertow.lib;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.palantir.logsafe.Preconditions;

/**
 * {@link HandlerContext} provides state required by generated handlers.
 */
public final class HandlerContext {

    private final SerializerRegistry serializerRegistry;

    private HandlerContext(Builder builder) {
        this.serializerRegistry = Preconditions.checkNotNull(builder.serializerRegistry,
                "Missing required SerializerRegistry");
    }

    /**
     * {@link SerializerRegistry} for request and response body serialization.
     */
    public SerializerRegistry serializerRegistry() {
        return serializerRegistry;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private SerializerRegistry serializerRegistry;

        private Builder() {}

        @CanIgnoreReturnValue
        public Builder serializerRegistry(SerializerRegistry value) {
            this.serializerRegistry = Preconditions.checkNotNull(value, "Value is required");
            return this;
        }

        public HandlerContext build() {
            return new HandlerContext(this);
        }
    }
}
