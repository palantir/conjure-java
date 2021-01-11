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

import com.palantir.conjure.java.Options;
import com.palantir.logsafe.Preconditions;
import java.io.File;
import java.util.stream.Stream;
import org.immutables.value.Value;

@Value.Immutable
public interface CliConfiguration {
    File input();

    File outputDirectory();

    @Value.Default
    default boolean generateObjects() {
        return false;
    }

    @Value.Default
    default boolean generateJersey() {
        return false;
    }

    @Value.Default
    default boolean generateUndertow() {
        return false;
    }

    @Value.Default
    default boolean generateDialogue() {
        return false;
    }

    @Value.Default
    default Options options() {
        return Options.empty();
    }

    @Value.Check
    default void check() {
        Preconditions.checkArgument(input().isFile(), "Target must exist and be a file");
        Preconditions.checkArgument(outputDirectory().isDirectory(), "Output must exist and be a directory");
        long count = Stream.of(generateObjects(), generateJersey(), generateUndertow(), generateDialogue())
                .filter(b -> b)
                .count();
        Preconditions.checkArgument(count == 1, "Must specify exactly one project to generate");
    }

    class Builder extends ImmutableCliConfiguration.Builder {}

    static Builder builder() {
        return new Builder();
    }
}
