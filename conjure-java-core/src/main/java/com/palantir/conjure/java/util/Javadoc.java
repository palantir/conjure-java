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

package com.palantir.conjure.java.util;

import com.palantir.conjure.spec.Documentation;
import org.apache.commons.lang3.StringUtils;

/** Provides utility functionality for rendering javadoc. */
public final class Javadoc {

    public static String render(Documentation documentation) {
        return StringUtils.isBlank(documentation.get())
                ? "" : StringUtils.appendIfMissing(documentation.get(), "\n");
    }

    private Javadoc() {}
}
