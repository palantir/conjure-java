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

import com.google.common.collect.ImmutableSet;
import com.palantir.conjure.java.types.SafetyEvaluator;
import com.palantir.conjure.spec.ArgumentDefinition;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.LogSafety;
import com.palantir.logsafe.DoNotLog;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.Unsafe;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public final class ConjureTags {

    private static final String SAFE = "safe";
    private static final String UNSAFE = "unsafe";

    public static final String SERVER_REQUEST_CONTEXT_TAG = "server-request-context";
    public static final String SERVER_REQUEST_CONTEXT_NAME = "requestContext";
    public static final String SERVER_SAFE_LOGGING_DISABLED = "server-squelch";

    /** Values that are consumed by the conjure generator, and needn't be listed for use at runtime. */
    public static final ImmutableSet<String> HANDLED_ARGUMENT_TAGS =
            ImmutableSet.of(SERVER_SAFE_LOGGING_DISABLED, SAFE, UNSAFE);

    public static boolean hasServerRequestContext(EndpointDefinition endpointDefinition) {
        return endpointDefinition.getTags().contains(SERVER_REQUEST_CONTEXT_TAG);
    }

    public static boolean isServerSafeLoggingAllowed(ArgumentDefinition arg) {
        return !arg.getTags().contains(SERVER_SAFE_LOGGING_DISABLED);
    }

    public static boolean isSafe(Collection<String> tags) {
        return tags.contains(SAFE);
    }

    public static boolean isUnsafe(Collection<String> tags) {
        return tags.contains(UNSAFE);
    }

    /**
     * Validates that safety declared using tags or markers agrees with the safety of the argument
     * type, and returns the declared safety.
     */
    public static Optional<LogSafety> validateArgument(ArgumentDefinition argument, SafetyEvaluator safetyEvaluator) {
        Optional<LogSafety> tagSafety = safety(argument, safetyEvaluator);
        if (tagSafety.isPresent()) {
            Optional<LogSafety> typeSafety = safetyEvaluator.evaluate(argument.getType());
            if (!SafetyEvaluator.allows(tagSafety, typeSafety)) {
                throw new SafeIllegalStateException(
                        "Declared argument safety is incompatible with the provided type",
                        SafeArg.of("name", argument.getArgName()),
                        SafeArg.of("declaredSafety", tagSafety.get()),
                        SafeArg.of("argumentTypeSafety", typeSafety));
            }
        }
        return tagSafety;
    }

    public static Optional<LogSafety> safety(ArgumentDefinition argument, SafetyEvaluator safetyEvaluator) {
        validateTags(argument, safetyEvaluator);
        Optional<LogSafety> argumentSafety = safetyEvaluator.getUsageTimeSafety(argument);
        if (argumentSafety.isPresent()) {
            return argumentSafety;
        }
        Set<String> tags = argument.getTags();
        if (isSafe(tags)) {
            return Optional.of(LogSafety.SAFE);
        }
        if (isUnsafe(tags)) {
            return Optional.of(LogSafety.UNSAFE);
        }
        return ConjureMarkers.markerSafety(argument);
    }

    public static void validateTags(ArgumentDefinition argument, SafetyEvaluator safetyEvaluator) {
        Set<String> tags = argument.getTags();
        validateTags(tags);
        Optional<LogSafety> markerSafety = ConjureMarkers.markerSafety(argument);
        if (markerSafety.isPresent() && (isSafe(tags) || isUnsafe(tags))) {
            throw new IllegalStateException(String.format(
                    "Unexpected %s marker in addition to a '%s' tag on argument '%s'",
                    markerSafety.get().accept(MarkerNameLogSafetyVisitor.INSTANCE),
                    isSafe(tags) ? "safe" : "unsafe",
                    argument.getArgName()));
        }
        Optional<LogSafety> argumentSafety = safetyEvaluator.getUsageTimeSafety(argument);
        if (argumentSafety.isPresent()) {
            if (markerSafety.isPresent()) {
                throw new IllegalStateException(String.format(
                        "Unexpected 'safety: %s' value in addition to a '%s' marker on argument '%s'",
                        argumentSafety.get().accept(DefFormatSafetyVisitor.INSTANCE),
                        markerSafety.get().accept(MarkerNameLogSafetyVisitor.INSTANCE),
                        argument.getArgName()));
            }
            if (isSafe(tags) || isUnsafe(tags)) {
                throw new IllegalStateException(String.format(
                        "Unexpected 'safety: %s' value in addition to a '%s' tag on argument '%s'",
                        argumentSafety.get().accept(DefFormatSafetyVisitor.INSTANCE),
                        isSafe(tags) ? "safe" : "unsafe",
                        argument.getArgName()));
            }
        }
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

    private enum MarkerNameLogSafetyVisitor implements LogSafety.Visitor<String> {
        INSTANCE;

        @Override
        public String visitSafe() {
            return Safe.class.getName();
        }

        @Override
        public String visitUnsafe() {
            return Unsafe.class.getName();
        }

        @Override
        public String visitDoNotLog() {
            return DoNotLog.class.getName();
        }

        @Override
        public String visitUnknown(String unknownValue) {
            throw new IllegalStateException("Unknown value: " + unknownValue);
        }
    }

    /** Visitor returns a value formatted like the yaml input rather than the IR. */
    private enum DefFormatSafetyVisitor implements LogSafety.Visitor<String> {
        INSTANCE;

        @Override
        public String visitSafe() {
            return "safe";
        }

        @Override
        public String visitUnsafe() {
            return "unsafe";
        }

        @Override
        public String visitDoNotLog() {
            return "do-not-log";
        }

        @Override
        public String visitUnknown(String unknownValue) {
            throw new IllegalStateException("Unknown value: " + unknownValue);
        }
    }
}
