/*
 * (c) Copyright 2022 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.undertow.processor.data;

import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

public enum Visibility {
    PUBLIC() {
        @Override
        public Modifier[] decorate(Modifier... modifiers) {
            Set<Modifier> results = new LinkedHashSet<>();
            results.add(Modifier.PUBLIC);
            results.addAll(Arrays.asList(modifiers));
            return results.toArray(new Modifier[0]);
        }
    },
    PACKAGE_PRIVATE() {
        @Override
        public Modifier[] decorate(Modifier... modifiers) {
            Set<Modifier> results = new LinkedHashSet<>(Arrays.asList(modifiers));
            results.remove(Modifier.PUBLIC);
            return results.toArray(new Modifier[0]);
        }
    };

    public abstract Modifier[] decorate(Modifier... modifiers);

    public static Visibility of(Element element) {
        Set<Modifier> elementModifiers = element.getModifiers();
        if (elementModifiers.contains(Modifier.PRIVATE)) {
            throw new SafeIllegalArgumentException("Element is private", SafeArg.of("element", element));
        }
        return elementModifiers.contains(Modifier.PUBLIC) ? PUBLIC : PACKAGE_PRIVATE;
    }
}
