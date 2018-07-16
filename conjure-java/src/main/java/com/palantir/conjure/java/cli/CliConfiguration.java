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
import org.apache.commons.cli.Option;
import org.immutables.value.Value;

@Value.Immutable
public abstract class CliConfiguration {
    public static final String OBJECTS_OPTION = "objects";
    public static final String JERSEY_OPTION = "jersey";
    public static final String RETROFIT_OPTION = "retrofit";
    public static final String RETROFIT_COMPLETABLE_FUTURES = "retrofitCompletableFutures";
    public static final String JERSEY_BINARY_AS_RESPONSE = "jerseyBinaryAsResponse";

    abstract File target();

    abstract File outputDirectory();

    @Value.Default
    @SuppressWarnings("checkstyle:designforextension")
    boolean generateObjects() {
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
    Set<FeatureFlags> featureFlags() {
        return ImmutableSet.of();
    }


    @Value.Check
    final void check() {
        Preconditions.checkArgument(target().isFile(), "Target must exist and be a file");
        Preconditions.checkArgument(outputDirectory().isDirectory(), "Output must exist and be a directory");
        Preconditions.checkArgument(generateObjects() ^ generateJersey() ^ generateRetrofit(),
                "Must specify exactly one project to generate");
    }

    static Builder builder() {
        return new Builder();
    }

    static CliConfiguration of(String target, String outputDirectory, Option[] options) {
        Builder builder = new Builder()
                .target(new File(target))
                .outputDirectory(new File(outputDirectory));
        ImmutableSet.Builder<FeatureFlags> flagsBuilder = ImmutableSet.builder();
        for (Option option : options) {
            switch (option.getOpt()) {
                case OBJECTS_OPTION:
                    builder.generateObjects(true);
                    break;
                case JERSEY_OPTION:
                    builder.generateJersey(true);
                    break;
                case RETROFIT_OPTION:
                    builder.generateRetrofit(true);
                    break;
                case RETROFIT_COMPLETABLE_FUTURES:
                    flagsBuilder.add(FeatureFlags.RetrofitCompletableFutures);
                    break;
                case JERSEY_BINARY_AS_RESPONSE:
                    flagsBuilder.add(FeatureFlags.JerseyBinaryAsResponse);
                    break;
                default:
                    break;
            }
        }

        builder.featureFlags(flagsBuilder.build());
        return builder.build();
    }

    public static final class Builder extends ImmutableCliConfiguration.Builder {}
}
