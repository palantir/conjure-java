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

package com.palantir.conjure.java.undertow.runtime;

import com.google.common.base.Strings;
import com.palantir.deadlines.DeadlinesHttpHeaders;
import com.palantir.tracing.TagTranslator;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HttpString;

/** A {@link TagTranslator} which populates metadata from deadlines headers. */
enum DeadlineTagTranslator implements TagTranslator<HttpServerExchange> {
    INSTANCE;

    private static final HttpString EXPECT_WITHIN = HttpString.tryFromString(DeadlinesHttpHeaders.EXPECT_WITHIN);
    private static final String EXPECT_WITHIN_TAG = "deadlines.expect_within";
    private static final HttpString EXPECT_WITHIN_ENFORCED =
            HttpString.tryFromString(DeadlinesHttpHeaders.EXPECT_WITHIN_ENFORCED);
    private static final String EXPECT_WITHIN_ENFORCED_TAG = "deadlines.expect_within_enforced";
    private static final HttpString DEADLINE_EXPIRED_REASON =
            HttpString.tryFromString(DeadlinesHttpHeaders.DEADLINE_EXPIRED_REASON);
    private static final String DEADLINE_EXPIRED_REASON_TAG = "deadlines.deadline_expired_reason";

    @Override
    public <T> void translate(TagAdapter<T> adapter, T target, HttpServerExchange data) {
        HeaderMap headers = data.getRequestHeaders();
        String expectWithin = headers.getFirst(EXPECT_WITHIN);
        if (!Strings.isNullOrEmpty(expectWithin)) {
            adapter.tag(target, EXPECT_WITHIN_TAG, expectWithin);
        }
        String expectWithinEnforced = headers.getFirst(EXPECT_WITHIN_ENFORCED);
        if (!Strings.isNullOrEmpty(expectWithinEnforced)) {
            adapter.tag(target, EXPECT_WITHIN_ENFORCED_TAG, expectWithinEnforced);
        }
        String deadlineExpiredReason = headers.getFirst(DEADLINE_EXPIRED_REASON);
        if (!Strings.isNullOrEmpty(deadlineExpiredReason)) {
            adapter.tag(target, DEADLINE_EXPIRED_REASON_TAG, deadlineExpiredReason);
        }
    }
}
