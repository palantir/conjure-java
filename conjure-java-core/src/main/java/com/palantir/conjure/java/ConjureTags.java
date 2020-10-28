/*
 * (c) Copyright 2020 Palantir Technologies Inc. All rights reserved.
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

import com.google.common.collect.ImmutableList;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.Unsafe;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import com.squareup.javapoet.AnnotationSpec;
import java.util.Collection;

public final class ConjureTags {

    private static final String SAFE = "safe";
    private static final String UNSAFE = "unsafe";

    public static boolean isSafe(Collection<String> tags) {
        return tags.contains(SAFE);
    }

    public static boolean isUnsafe(Collection<String> tags) {
        return tags.contains(UNSAFE);
    }

    public static ImmutableList<AnnotationSpec> tagAnnotations(Collection<String> tags) {
        validateTags(tags);
        ImmutableList.Builder<AnnotationSpec> builder = ImmutableList.builderWithExpectedSize(1);
        if (isSafe(tags)) {
            builder.add(AnnotationSpec.builder(Safe.class).build());
        }
        if (isUnsafe(tags)) {
            builder.add(AnnotationSpec.builder(Unsafe.class).build());
        }
        return builder.build();
    }

    public static void validateTags(Collection<String> tags) {
        if (isSafe(tags) && isUnsafe(tags)) {
            throw new SafeIllegalStateException("Tags cannot include both safe and unsafe", SafeArg.of("tags", tags));
        }
        for (String tag : tags) {
            validateCase(UNSAFE, tag);
            validateCase(SAFE, tag);
        }
    }

    /** Fails if {@code toCheck} insensitively matches {@code name} but does not exactly match. */
    private static void validateCase(String name, String toCheck) {
        if (name.equalsIgnoreCase(toCheck) && !name.equals(toCheck)) {
            throw new SafeIllegalStateException(
                    "Unexpected capitalization", SafeArg.of("tag", name), SafeArg.of("unexpected", toCheck));
        }
    }

    private ConjureTags() {}
}
