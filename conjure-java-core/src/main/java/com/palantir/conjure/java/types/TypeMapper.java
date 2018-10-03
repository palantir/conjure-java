/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.types;

import com.palantir.conjure.java.types.ClassNameVisitor.Factory;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.squareup.javapoet.TypeName;
import java.util.List;

public final class TypeMapper {

    private final List<TypeDefinition> types;
    private final Factory classNameVisitorFactory;

    public TypeMapper(List<TypeDefinition> types) {
        this(types, DefaultClassNameVisitor::new);
    }

    public TypeMapper(List<TypeDefinition> types,
            Factory classNameVisitorFactory) {
        this.types = types;
        this.classNameVisitorFactory = classNameVisitorFactory;
    }

    public List<TypeDefinition> getTypes() {
        return types;
    }

    public TypeName getClassName(Type type) {
        return type.accept(classNameVisitorFactory.create(types));
    }
}
