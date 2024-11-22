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

/** A wrapper around a long which is safe for json-serialization as a number without loss of precision. */
public final class SafeLong implements Comparable<SafeLong> {

    private static final long MIN_SAFE_VALUE = -(1L << 53) + 1;
    private static final long MAX_SAFE_VALUE = (1L << 53) - 1;

    public static final SafeLong MAX_VALUE = SafeLong.of(MAX_SAFE_VALUE);
    public static final SafeLong MIN_VALUE = SafeLong.of(MIN_SAFE_VALUE);

    private final long longValue;

    private SafeLong(long longValue) {
        this.longValue = check(longValue);
    }

    @JsonValue
    public long longValue() {
        return longValue;
    }

    private static long check(long value) {
        Preconditions.checkArgument(
                MIN_SAFE_VALUE <= value && value <= MAX_SAFE_VALUE,
                "number must be safely representable in javascript i.e. "
                        + "lie between -9007199254740991 and 9007199254740991");
        return value;
    }

    public static SafeLong valueOf(String value) {
        return of(Long.parseLong(value));
    }

    @JsonCreator
    public static SafeLong of(long value) {
        return new SafeLong(value);
    }

    @Override
    public String toString() {
        return Long.toString(longValue);
    }

    @Override
    public int compareTo(SafeLong other) {
        return Long.compare(longValue, other.longValue);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        SafeLong safeLong = (SafeLong) other;
        return longValue == safeLong.longValue;
    }

    @Override
    public int hashCode() {
        return 177573 + Long.hashCode(longValue);
    }
}
