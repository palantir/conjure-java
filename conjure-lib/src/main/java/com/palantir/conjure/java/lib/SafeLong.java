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

package com.palantir.conjure.java.lib;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Comparable;
import org.immutables.value.Value;

/**
 * A wrapper around a long which is safe for json-serialization as a number
 * without loss of precision.
 */
@Value.Immutable
public abstract class SafeLong implements Comparable<SafeLong> {

    private static final long MIN_SAFE_VALUE = -(1L << 53) + 1;
    private static final long MAX_SAFE_VALUE = (1L << 53) - 1;

    @JsonValue
    @Value.Parameter
    public abstract long longValue();

    @Value.Check
    protected final void check() {
        Preconditions.checkArgument(MIN_SAFE_VALUE <= longValue() && longValue() <= MAX_SAFE_VALUE,
                "number must be safely representable in javascript i.e. "
                        + "lie between -9007199254740991 and 9007199254740991");
    }

    public static SafeLong valueOf(String value) {
        return SafeLong.of(Long.parseLong(value));
    }

    @JsonCreator
    public static SafeLong of(long value) {
        return ImmutableSafeLong.of(value);
    }

    @Override
    public final String toString() {
        return Long.toString(longValue());
    }
    
    @Override
    public final int compareTo(SafeLong other) {
        return Long.compare(longValue(), other.longValue());
    }
}
