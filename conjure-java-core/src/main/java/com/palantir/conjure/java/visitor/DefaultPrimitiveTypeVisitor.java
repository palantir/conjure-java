/*
 * (c) Copyright 2024 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.visitor;

import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;

@SuppressWarnings("checkstyle:DesignForExtension")
public abstract class DefaultPrimitiveTypeVisitor<T> implements PrimitiveType.Visitor<T> {

    public T visitDefault() {
        throw new SafeIllegalStateException("Unexpected primitive type");
    }

    @Override
    public T visitString() {
        return visitDefault();
    }

    @Override
    public T visitDatetime() {
        return visitDefault();
    }

    @Override
    public T visitInteger() {
        return visitDefault();
    }

    @Override
    public T visitDouble() {
        return visitDefault();
    }

    @Override
    public T visitSafelong() {
        return visitDefault();
    }

    @Override
    public T visitBinary() {
        return visitDefault();
    }

    @Override
    public T visitAny() {
        return visitDefault();
    }

    @Override
    public T visitBoolean() {
        return visitDefault();
    }

    @Override
    public T visitUuid() {
        return visitDefault();
    }

    @Override
    public T visitRid() {
        return visitDefault();
    }

    @Override
    public T visitBearertoken() {
        return visitDefault();
    }

    @Override
    public T visitUnknown(String unknownValue) {
        return visitDefault();
    }
}
