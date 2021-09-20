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

package com.palantir.conjure.java.undertow.processor.data;

import com.google.auto.common.MoreTypes;
import com.google.common.collect.MoreCollectors;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

final class Instantiables {

    private Instantiables() {}

    /**
     * Produces a {@link CodeBlock} of instantiation code for the provided type.
     * This supports both no-arg class constructors and single-value enum singletons.
     */
    static CodeBlock instantiate(TypeMirror mirror) {
        DeclaredType declaredType = MoreTypes.asDeclared(mirror);
        if (declaredType == null) {
            throw new SafeIllegalStateException("TypeMirror is not a DeclaredType", SafeArg.of("type", mirror));
        }
        return instantiate(declaredType);
    }

    static CodeBlock instantiate(DeclaredType declaredType) {
        TypeElement typeElement = (TypeElement) declaredType.asElement();
        if (typeElement.getKind() == ElementKind.ENUM) {
            Element onlyEnumConstant = typeElement.getEnclosedElements().stream()
                    .filter(elem -> elem.getKind() == ElementKind.ENUM_CONSTANT)
                    .collect(MoreCollectors.onlyElement());
            return CodeBlock.of(
                    "$T.$N",
                    TypeName.get(declaredType),
                    onlyEnumConstant.getSimpleName().toString());
        } else {
            return CodeBlock.of("new $T()", TypeName.get(declaredType));
        }
    }
}
