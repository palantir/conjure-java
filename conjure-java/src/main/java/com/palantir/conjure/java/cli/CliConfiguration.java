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

package com.palantir.conjure.java.cli;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.palantir.conjure.java.FeatureFlags;
import java.io.File;
import java.util.Set;
import org.immutables.value.Value;

@Value.Immutable
public abstract class CliConfiguration {
    abstract File input();

    abstract File outputDirectory();

    @Value.Default
    @SuppressWarnings("checkstyle:designforextension")
    boolean generateObjects() {
        return false;
    }

    @Value.Default
    @SuppressWarnings("checkstyle:designforextension")
    boolean generateJaxRsClient() {
        return false;
    }

    @Value.Default
    @SuppressWarnings("checkstyle:designforextension")
    boolean generateJersey() {
        return false;
    }

    @Value.Default
    @SuppressWarnings("checkstyle:designforextension")
    boolean generateRetrofit() {
        return false;
    }

    @Value.Default
    @SuppressWarnings("checkstyle:designforextension")
    boolean generateUndertow() {
        return false;
    }

    @Value.Default
    @SuppressWarnings("checkstyle:designforextension")
    Set<FeatureFlags> featureFlags() {
        return ImmutableSet.of();
    }


    @Value.Check
    final void check() {
        Preconditions.checkArgument(input().isFile(), "Target must exist and be a file");
        Preconditions.checkArgument(outputDirectory().isDirectory(), "Output must exist and be a directory");
        Preconditions.checkArgument(generateObjects() ^ generateJaxRsClient()
                        ^ generateJersey() ^ generateRetrofit() ^ generateUndertow(),
                "Must specify exactly one project to generate");
    }

    static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends ImmutableCliConfiguration.Builder {
        Builder retrofitCompletableFutures(boolean flag) {
            return flag ? addFeatureFlags(FeatureFlags.RetrofitCompletableFutures) : this;
        }

        Builder retrofitListenableFutures(boolean flag) {
            return flag ? addFeatureFlags(FeatureFlags.RetrofitListenableFutures) : this;
        }

        Builder jerseyBinaryAsResponse(boolean flag) {
            return flag ? addFeatureFlags(FeatureFlags.JerseyBinaryAsResponse) : this;
        }

        Builder notNullAuthAndBody(boolean flag) {
            return flag ? addFeatureFlags(FeatureFlags.RequireNotNullAuthAndBodyParams) : this;
        }

        Builder undertowServicePrefix(boolean flag) {
            return flag ? addFeatureFlags(FeatureFlags.UndertowServicePrefix) : this;
        }

        Builder useImmutableBytes(boolean flag) {
            return flag ? addFeatureFlags(FeatureFlags.UseImmutableBytes) : this;
        }
    }
}
