/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.parameterized.objects;

import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.conjure.java.Generator;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.conjure.java.services.JerseyServiceGenerator;
import com.palantir.conjure.java.services.UndertowServiceGenerator;
import com.palantir.conjure.java.services.dialogue.DialogueServiceGenerator;
import com.palantir.conjure.java.types.CheckedErrorGenerator;
import com.palantir.conjure.java.types.ErrorGenerator;
import com.palantir.conjure.java.types.ObjectGenerator;
import com.palantir.logsafe.Preconditions;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

public record ParameterizedTestCase(
        String name, String docs, List<FilePath> files, Options options, Set<GeneratorType> generators) {

    @Override
    public Options options() {
        return Options.builder().from(options).packagePrefix(getPackageName()).build();
    }

    private String getPackageName() {
        return name.toLowerCase(Locale.ROOT).replaceAll("-", "");
    }

    public Set<Generator> getGenerators() {
        return generators.stream()
                .map(generatorType -> generatorType.accept(GeneratorType.Visitor.<Generator>builder()
                        .visitObject(() -> new ObjectGenerator(options()))
                        .visitDialogue(() -> new DialogueServiceGenerator(options()))
                        .visitUndertow(() -> new UndertowServiceGenerator(options()))
                        .visitJersey(() -> new JerseyServiceGenerator(options()))
                        .visitError(() -> new ErrorGenerator(options()))
                        .visitCheckedError(() -> new CheckedErrorGenerator(options()))
                        .throwOnUnknown()
                        .build()))
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "ParameterizedTestCase{name: " + name + ", docs: " + docs + ", files: " + files + ", options: " + options
                + ", generators: " + generators + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    // Hand-rolled the Builder definition to improve the creation pattern
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {
        boolean buildInvoked;

        private String name;

        private String docs;

        private List<FilePath> files = ConjureCollections.newNonNullList();

        private Options options;

        private Set<GeneratorType> generators = ConjureCollections.newNonNullSet();

        private Builder() {}

        public Builder from(ParameterizedTestCase other) {
            checkNotBuilt();
            name(other.name);
            docs(other.docs);
            files(other.files);
            options(other.options);
            generators(other.generators);
            return this;
        }

        public Builder name(@Nonnull String name) {
            checkNotBuilt();
            this.name = Preconditions.checkNotNull(name, "name cannot be null");
            return this;
        }

        public Builder docs(@Nonnull String docs) {
            checkNotBuilt();
            this.docs = Preconditions.checkNotNull(docs, "docs cannot be null");
            return this;
        }

        public Builder files(@Nonnull Iterable<FilePath> files) {
            checkNotBuilt();
            this.files = ConjureCollections.newNonNullList(Preconditions.checkNotNull(files, "files cannot be null"));
            return this;
        }

        public Builder files(FilePath files) {
            checkNotBuilt();
            Preconditions.checkNotNull(files, "files cannot be null");
            this.files.add(files);
            return this;
        }

        public Builder options(@Nonnull Options options) {
            checkNotBuilt();
            this.options = Preconditions.checkNotNull(options, "options cannot be null");
            return this;
        }

        public Builder generators(@Nonnull Iterable<GeneratorType> generators) {
            checkNotBuilt();
            this.generators = ConjureCollections.newNonNullSet(
                    Preconditions.checkNotNull(generators, "generators cannot be null"));
            return this;
        }

        public Builder generators(GeneratorType generators) {
            checkNotBuilt();
            Preconditions.checkNotNull(generators, "generators cannot be null");
            this.generators.add(generators);
            return this;
        }

        @CheckReturnValue
        public ParameterizedTestCase build() {
            checkNotBuilt();
            this.buildInvoked = true;
            return new ParameterizedTestCase(name, docs, files, options, generators);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!buildInvoked, "Build has already been called");
        }
    }
}
