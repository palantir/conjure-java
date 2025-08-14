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

package nongenerated.com.palantir.product.api;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.palantir.logsafe.Preconditions;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import nongenerated.com.palantir.product.api.ExampleSealedInterfaceUnion.Bar;
import nongenerated.com.palantir.product.api.ExampleSealedInterfaceUnion.Baz;
import nongenerated.com.palantir.product.api.ExampleSealedInterfaceUnion.Foo;
import nongenerated.com.palantir.product.api.ExampleSealedInterfaceUnion.UnknownVariant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = UnknownVariant.class)
@JsonSubTypes({@JsonSubTypes.Type(Foo.class), @JsonSubTypes.Type(Bar.class), @JsonSubTypes.Type(Baz.class)})
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed interface ExampleSealedInterfaceUnion {

    sealed interface Known extends ExampleSealedInterfaceUnion permits Foo, Bar, Baz {}

    @JsonTypeName("foo")
    record Foo(String value) implements Known {

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Foo(@JsonSetter("foo") String value) {
            Preconditions.checkNotNull(value, "foo cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "foo";
        }

        private String getValue() {
            return value;
        }
    }

    @JsonAppend(attrs = @JsonAppend.Attr(propName = "type", value = "bar"), prepend = true)
    record Bar(@JsonProperty("bar") int value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Bar {
            Preconditions.checkNotNull(value, "bar cannot be null");
        }
    }

    @JsonAppend(attrs = @JsonAppend.Attr(propName = "type", value = "baz"), prepend = true)
    record Baz(@JsonProperty("baz") long value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Baz {
            Preconditions.checkNotNull(value, "baz cannot be null");
        }
    }

    // TODO(kkak): Clean up
    final class UnknownVariant implements ExampleSealedInterfaceUnion {
        private final String type;

        private final Map<String, Object> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private UnknownVariant(@JsonProperty("type") String type) {
            this(type, new HashMap<String, Object>());
        }

        private UnknownVariant(@Nonnull String type, @Nonnull Map<String, Object> value) {
            Preconditions.checkNotNull(type, "type cannot be null");
            Preconditions.checkNotNull(value, "value cannot be null");
            this.type = type;
            this.value = value;
        }

        @JsonProperty
        @SuppressWarnings("UnusedMethod")
        private String getType() {
            return type;
        }

        @JsonAnyGetter
        public Map<String, Object> getValue() {
            return value;
        }

        @SuppressWarnings("UnusedMethod")
        @JsonAnySetter
        private void put(String key, Object val) {
            value.put(key, val);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof UnknownVariant && equalTo((UnknownVariant) other));
        }

        private boolean equalTo(UnknownVariant other) {
            return this.type.equals(other.type) && this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            int hash = 1;
            hash = 31 * hash + this.type.hashCode();
            hash = 31 * hash + this.value.hashCode();
            return hash;
        }

        @Override
        public String toString() {
            return "UnknownWrapper{type: " + type + ", value: " + value + '}';
        }
    }
}
