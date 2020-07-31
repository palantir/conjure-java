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

package com.palantir.conjure.java.services;

import com.google.common.annotations.Beta;
import com.palantir.conjure.java.visitor.DefaultTypeVisitor;
import com.palantir.conjure.spec.ExternalReference;

@Beta
public final class IsUndertowAsyncMarkerVisitor extends DefaultTypeVisitor<Boolean> {
    public static final IsUndertowAsyncMarkerVisitor INSTANCE = new IsUndertowAsyncMarkerVisitor();

    @Override
    public Boolean visitExternal(ExternalReference value) {
        return "Async".equals(value.getExternalReference().getName());
    }

    @Override
    public Boolean visitDefault() {
        return false;
    }
}
