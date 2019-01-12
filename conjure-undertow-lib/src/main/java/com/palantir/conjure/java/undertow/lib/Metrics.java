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

package com.palantir.conjure.java.undertow.lib;

import io.undertow.util.AttachmentKey;

public final class Metrics {

    private Metrics() {}

    public static final AttachmentKey<Long> DELEGATE_DURATION_KEY = AttachmentKey.create(Long.class);
    public static final AttachmentKey<String> RESOURCE_METHOD_NAME_KEY = AttachmentKey.create(String.class);
    public static final AttachmentKey<String> SERVICE_NAME_KEY = AttachmentKey.create(String.class);

}
