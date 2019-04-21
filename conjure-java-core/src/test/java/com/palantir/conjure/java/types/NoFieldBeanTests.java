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

package com.palantir.conjure.java.types;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.product.EmptyObjectExample;
import java.io.IOException;
import org.junit.Test;

public class NoFieldBeanTests {

    private final ObjectMapper mapper = ObjectMappers.newServerObjectMapper();

    @Test
    public void testSingletonInstance() {
        assertThat(EmptyObjectExample.of()).isSameAs(EmptyObjectExample.of());
    }

    @Test
    public void testDeserializeUsesSingleton() throws IOException {
        assertThat(mapper.readValue("{}", EmptyObjectExample.class)).isSameAs(EmptyObjectExample.of());
    }

    @Test
    public void testConstantToString() {
        EmptyObjectExample value = EmptyObjectExample.of();
        assertThat(value.toString()).isSameAs(value.toString());
    }

    @Test
    public void testSerializedForm() throws IOException {
        assertThat(mapper.writeValueAsString(EmptyObjectExample.of())).isEqualTo("{}");
    }
}
