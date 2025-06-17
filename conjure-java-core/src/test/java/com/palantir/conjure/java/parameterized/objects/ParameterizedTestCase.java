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
import com.palantir.conjure.java.types.EndpointErrorGenerator;
import com.palantir.conjure.java.types.ErrorGenerator;
import com.palantir.conjure.java.types.ObjectGenerator;
import com.palantir.logsafe.Preconditions;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

public record ParameterizedTestCase(
        String name, String docs, List<Path> files, Options options, Set<GeneratorType> generatorTypes) {

    private static final Path TEST_RESOURCES_FOLDER = Path.of("src/test/resources");

    @Override
    public List<Path> files() {
        return files.stream()
                .map(filePath ->
                        filePath.startsWith(TEST_RESOURCES_FOLDER) ? filePath : TEST_RESOURCES_FOLDER.resolve(filePath))
                .toList();
    }

    @Override
    public Options options() {
        return Options.builder().from(options).packagePrefix(packagePrefix()).build();
    }

    public String packagePrefix() {
        return name.toLowerCase(Locale.ROOT).replaceAll("[^a-zA-Z]", "");
    }

    public Set<Generator> generators() {
        return generatorTypes.stream()
                .map(generatorType -> switch (generatorType) {
                    case OBJECT -> new ObjectGenerator(options());
                    case DIALOGUE -> new DialogueServiceGenerator(options());
                    case UNDERTOW -> new UndertowServiceGenerator(options());
                    case JERSEY -> new JerseyServiceGenerator(options());
                    case ERROR -> new ErrorGenerator(options());
                    case CHECKED_ERROR -> new EndpointErrorGenerator(options());
                })
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "ParameterizedTestCase{name: " + name + ", docs: " + docs + ", files: " + files + ", options: " + options
                + ", generatorTypes: " + generatorTypes + '}';
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

        private List<Path> files = ConjureCollections.newNonNullList();

        private Options options;

        private Set<GeneratorType> generatorTypes = ConjureCollections.newNonNullSet();

        private Builder() {}

        public Builder from(ParameterizedTestCase other) {
            checkNotBuilt();
            name(other.name);
            docs(other.docs);
            files(other.files);
            options(other.options);
            generatorTypes(other.generatorTypes);
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

        public Builder files(@Nonnull Iterable<Path> files) {
            checkNotBuilt();
            this.files = ConjureCollections.newNonNullList(Preconditions.checkNotNull(files, "files cannot be null"));
            return this;
        }

        public Builder files(Path files) {
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

        public Builder generatorTypes(@Nonnull Iterable<GeneratorType> generatorTypes) {
            checkNotBuilt();
            this.generatorTypes = ConjureCollections.newNonNullSet(
                    Preconditions.checkNotNull(generatorTypes, "generatorTypes cannot be null"));
            return this;
        }

        public Builder generatorTypes(GeneratorType generatorTypes) {
            checkNotBuilt();
            Preconditions.checkNotNull(generatorTypes, "generatorTypes cannot be null");
            this.generatorTypes.add(generatorTypes);
            return this;
        }

        @CheckReturnValue
        public ParameterizedTestCase build() {
            checkNotBuilt();
            this.buildInvoked = true;
            return new ParameterizedTestCase(name, docs, files, options, generatorTypes);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!buildInvoked, "Build has already been called");
        }
    }
}
