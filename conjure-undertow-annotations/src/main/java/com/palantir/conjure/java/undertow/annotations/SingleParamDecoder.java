/*
 * (c) Copyright 2021 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.undertow.annotations;

import com.google.common.collect.Iterables;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Collection;

final class SingleParamDecoder<T> implements CollectionParamDecoder<T> {

    private final ParamDecoder<? extends T> delegate;

    SingleParamDecoder(ParamDecoder<? extends T> delegate) {
        this.delegate = Preconditions.checkNotNull(delegate, "Decoder is required");
    }

    @Override
    public T decode(Collection<String> value) {
        int size = value.size();
        if (size != 1) {
            throw new SafeIllegalArgumentException("Expected exactly one parameter", SafeArg.of("size", size));
        }
        String singleValue = Preconditions.checkNotNull(Iterables.getOnlyElement(value), "Received a null value");
        return Preconditions.checkNotNull(delegate.decode(singleValue), "Decoder produced a null value");
    }

    @Override
    public String toString() {
        return "SingleParamDecoder{" + delegate + '}';
    }
}
