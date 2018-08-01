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

package com.palantir.conjure.java;

import static org.assertj.core.api.Assertions.assertThat;

import com.palantir.product.ListExample;
import com.palantir.product.MapExample;
import com.palantir.product.SetExample;
import org.junit.Test;

public final class ImmutableCollectionsTest {

    @Test
    public void ImmutableSetExample() {
        SetExample.Builder builder = SetExample.builder();
        SetExample setExample = builder.build();
        builder.items("new item to the underlying set");

        assertThat(setExample.getItems()).isEmpty();
    }

    @Test
    public void ImmutableListExample() {
        ListExample.Builder builder = ListExample.builder();
        ListExample listExample = builder.build();
        builder.items("new item to the underlying list");

        assertThat(listExample.getItems()).isEmpty();
    }

    @Test
    public void ImmutableMapExample() {
        MapExample.Builder builder = MapExample.builder();
        MapExample mapExample = builder.build();
        builder.items("new key", "new value");

        assertThat(mapExample.getItems()).isEmpty();
    }
}
