/*
 * (c) Copyright 2026 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.lib.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Demonstrates which {@link Stream} operations preserve the {@link Spliterator#SIZED} characteristic.
 * This is relevant to <a href="https://github.com/palantir/conjure-java/issues/519">#519</a>:
 * if we add {@code Stream}-accepting overloads to generated builders, we can use
 * {@link Spliterator#getExactSizeIfKnown()} to pre-size the internal collection for pipelines
 * that preserve SIZED (e.g. {@code .map()}).
 */
public class StreamSpliteratorSizingTest {

    private static final List<Integer> SOURCE = List.of(1, 2, 3, 4, 5);

    @Test
    public void source_stream_is_sized() {
        assertSized(SOURCE.stream(), 5);
    }

    @Test
    public void map_preserves_sized() {
        assertSized(SOURCE.stream().map(x -> x * 2), 5);
    }

    @Test
    public void filter_loses_sized() {
        assertNotSized(SOURCE.stream().filter(x -> x > 2));
    }

    @Test
    public void flatMap_loses_sized() {
        assertNotSized(SOURCE.stream().flatMap(x -> Stream.of(x, x)));
    }

    @Test
    public void distinct_loses_sized() {
        assertNotSized(SOURCE.stream().distinct());
    }

    @Test
    public void sorted_preserves_sized() {
        assertSized(SOURCE.stream().sorted(), 5);
    }

    @Test
    public void peek_preserves_sized() {
        assertSized(SOURCE.stream().peek(_x -> {}), 5);
    }

    @Test
    public void limit_preserves_sized() {
        assertSized(SOURCE.stream().limit(3), 3);
    }

    @Test
    public void skip_preserves_sized() {
        assertSized(SOURCE.stream().skip(2), 3);
    }

    @Test
    public void mapMulti_loses_sized() {
        assertNotSized(SOURCE.stream().mapMulti((x, consumer) -> consumer.accept(x)));
    }

    @Test
    public void stream_of_is_sized() {
        assertSized(Stream.of(1, 2, 3), 3);
    }

    @Test
    public void stream_generate_is_not_sized() {
        assertNotSized(Stream.generate(() -> 1));
    }

    @Test
    public void stream_iterate_is_not_sized() {
        assertNotSized(Stream.iterate(0, x -> x + 1));
    }

    @Test
    public void stream_concat_preserves_sized() {
        assertSized(Stream.concat(SOURCE.stream(), SOURCE.stream()), 10);
    }

    @Test
    public void map_then_filter_loses_sized() {
        assertNotSized(SOURCE.stream().map(x -> x * 2).filter(x -> x > 4));
    }

    @Test
    public void filter_then_map_loses_sized() {
        assertNotSized(SOURCE.stream().filter(x -> x > 2).map(x -> x * 2));
    }

    private static void assertSized(Stream<?> stream, long expectedSize) {
        Spliterator<?> spliterator = stream.spliterator();
        assertThat(spliterator.hasCharacteristics(Spliterator.SIZED))
                .as("expected SIZED characteristic")
                .isTrue();
        assertThat(spliterator.getExactSizeIfKnown()).as("exact size").isEqualTo(expectedSize);
    }

    private static void assertNotSized(Stream<?> stream) {
        Spliterator<?> spliterator = stream.spliterator();
        assertThat(spliterator.hasCharacteristics(Spliterator.SIZED))
                .as("expected no SIZED characteristic")
                .isFalse();
        assertThat(spliterator.getExactSizeIfKnown())
                .as("exact size should be unknown")
                .isEqualTo(-1);
    }
}
