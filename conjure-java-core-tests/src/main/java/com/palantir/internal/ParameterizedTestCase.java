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
package com.palantir.internal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@JsonDeserialize(builder = ParameterizedTestCase.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class ParameterizedTestCase {
    private final Optional<String> docs;

    private final String name;

    private final List<FilePath> files;

    private final Options options;

    private final Set<GeneratorType> generators;

    private int memoizedHashCode;

    private ParameterizedTestCase(
            Optional<String> docs, String name, List<FilePath> files, Options options, Set<GeneratorType> generators) {
        validateFields(docs, name, files, options, generators);
        this.docs = docs;
        this.name = name;
        this.files = ConjureCollections.unmodifiableList(files);
        this.options = options;
        this.generators = Collections.unmodifiableSet(generators);
    }

    @JsonProperty("docs")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<String> getDocs() {
        return this.docs;
    }

    @JsonProperty("name")
    public String getName() {
        return this.name;
    }

    @JsonProperty("files")
    public List<FilePath> getFiles() {
        return this.files;
    }

    @JsonProperty("options")
    public Options getOptions() {
        return this.options;
    }

    @JsonProperty("generators")
    public Set<GeneratorType> getGenerators() {
        return this.generators;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof ParameterizedTestCase && equalTo((ParameterizedTestCase) other));
    }

    private boolean equalTo(ParameterizedTestCase other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.docs.equals(other.docs)
                && this.name.equals(other.name)
                && this.files.equals(other.files)
                && this.options.equals(other.options)
                && this.generators.equals(other.generators);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.docs.hashCode();
            hash = 31 * hash + this.name.hashCode();
            hash = 31 * hash + this.files.hashCode();
            hash = 31 * hash + this.options.hashCode();
            hash = 31 * hash + this.generators.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "ParameterizedTestCase{docs: " + docs + ", name: " + name + ", files: " + files + ", options: " + options
                + ", generators: " + generators + '}';
    }

    private static void validateFields(
            Optional<String> docs, String name, List<FilePath> files, Options options, Set<GeneratorType> generators) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, docs, "docs");
        missingFields = addFieldIfMissing(missingFields, name, "name");
        missingFields = addFieldIfMissing(missingFields, files, "files");
        missingFields = addFieldIfMissing(missingFields, options, "options");
        missingFields = addFieldIfMissing(missingFields, generators, "generators");
        if (missingFields != null) {
            throw new SafeIllegalArgumentException(
                    "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(5);
            }
            missingFields.add(fieldName);
        }
        return missingFields;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    public static final class Builder {
        boolean _buildInvoked;

        private Optional<String> docs = Optional.empty();

        private String name;

        private List<FilePath> files = ConjureCollections.newNonNullList();

        private Options options;

        private Set<GeneratorType> generators = ConjureCollections.newNonNullSet();

        private Builder() {}

        public Builder from(ParameterizedTestCase other) {
            checkNotBuilt();
            docs(other.getDocs());
            name(other.getName());
            files(other.getFiles());
            options(other.getOptions());
            generators(other.getGenerators());
            return this;
        }

        @JsonSetter(value = "docs", nulls = Nulls.SKIP)
        public Builder docs(@Nonnull Optional<String> docs) {
            checkNotBuilt();
            this.docs = Preconditions.checkNotNull(docs, "docs cannot be null");
            return this;
        }

        public Builder docs(@Nonnull String docs) {
            checkNotBuilt();
            this.docs = Optional.of(Preconditions.checkNotNull(docs, "docs cannot be null"));
            return this;
        }

        @JsonSetter("name")
        public Builder name(@Nonnull String name) {
            checkNotBuilt();
            this.name = Preconditions.checkNotNull(name, "name cannot be null");
            return this;
        }

        @JsonSetter(value = "files", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder files(@Nonnull Iterable<FilePath> files) {
            checkNotBuilt();
            this.files = ConjureCollections.newNonNullList(Preconditions.checkNotNull(files, "files cannot be null"));
            return this;
        }

        public Builder addAllFiles(@Nonnull Iterable<FilePath> files) {
            checkNotBuilt();
            ConjureCollections.addAllAndCheckNonNull(
                    this.files, Preconditions.checkNotNull(files, "files cannot be null"));
            return this;
        }

        public Builder files(FilePath files) {
            checkNotBuilt();
            Preconditions.checkNotNull(files, "files cannot be null");
            this.files.add(files);
            return this;
        }

        @JsonSetter("options")
        public Builder options(@Nonnull Options options) {
            checkNotBuilt();
            this.options = Preconditions.checkNotNull(options, "options cannot be null");
            return this;
        }

        @JsonSetter(value = "generators", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder generators(@Nonnull Iterable<GeneratorType> generators) {
            checkNotBuilt();
            this.generators = ConjureCollections.newNonNullSet(
                    Preconditions.checkNotNull(generators, "generators cannot be null"));
            return this;
        }

        public Builder addAllGenerators(@Nonnull Iterable<GeneratorType> generators) {
            checkNotBuilt();
            ConjureCollections.addAllAndCheckNonNull(
                    this.generators, Preconditions.checkNotNull(generators, "generators cannot be null"));
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
            this._buildInvoked = true;
            return new ParameterizedTestCase(docs, name, files, options, generators);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
