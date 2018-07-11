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

import com.palantir.conjure.spec.BodyParameterType;
import com.palantir.conjure.spec.HeaderParameterType;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.ParameterType;
import com.palantir.conjure.spec.PathParameterType;
import com.palantir.conjure.spec.QueryParameterType;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.visitor.TypeVisitor;
import com.squareup.javapoet.CodeBlock;
import java.util.Collections;
import java.util.Optional;

public final class Parameters {

    private Parameters() {}

    /** Produces an ordering for ParamaterType of Header, Path, Query, Body. */
    public static final ParameterType.Visitor<Integer> PARAM_SORT_ORDER = new ParameterType.Visitor<Integer>() {
        @Override
        public Integer visitBody(BodyParameterType value) {
            return 30;
        }

        @Override
        public Integer visitHeader(HeaderParameterType value) {
            return 0;
        }

        @Override
        public Integer visitPath(PathParameterType value) {
            return 10;
        }

        @Override
        public Integer visitQuery(QueryParameterType value) {
            return 20;
        }

        @Override
        public Integer visitUnknown(String unknownType) {
            return -1;
        }
    };

    /**
     * Produces a type sort ordering for use with {@link #PARAM_SORT_ORDER} such that types with known defaults come
     * after types without known defaults.
     */
    public static final Type.Visitor<Integer> TYPE_SORT_ORDER = new TypeVisitor.Default<Integer>() {
        @Override
        public Integer visitOptional(OptionalType value) {
            return 1;
        }

        @Override
        public Integer visitList(ListType value) {
            return 1;
        }

        @Override
        public Integer visitSet(SetType value) {
            return 1;
        }

        @Override
        public Integer visitMap(MapType value) {
            return 1;
        }

        @Override
        public Integer visitDefault() {
            return 0;
        }
    };

    /** Indicates whether a particular type has a defaultable value. */
    public static final Type.Visitor<Boolean> TYPE_DEFAULTABLE_PREDICATE = new TypeVisitor.Default<Boolean>() {
        @Override
        public Boolean visitOptional(OptionalType value) {
            return true;
        }

        @Override
        public Boolean visitList(ListType value) {
            return true;
        }

        @Override
        public Boolean visitSet(SetType value) {
            return true;
        }

        @Override
        public Boolean visitMap(MapType value) {
            return true;
        }

        @Override
        public Boolean visitDefault() {
            return false;
        }
    };

    public static final Type.Visitor<CodeBlock> TYPE_DEFAULT_VALUE = new TypeVisitor.Default<CodeBlock>() {
        @Override
        public CodeBlock visitOptional(OptionalType value) {
            return CodeBlock.of("$T.empty()", Optional.class);
        }

        @Override
        public CodeBlock visitList(ListType value) {
            return CodeBlock.of("$T.emptyList()", Collections.class);
        }

        @Override
        public CodeBlock visitSet(SetType value) {
            return CodeBlock.of("$T.emptySet()", Collections.class);
        }

        @Override
        public CodeBlock visitMap(MapType value) {
            return CodeBlock.of("$T.emptyMap()", Collections.class);
        }

        @Override
        public CodeBlock visitDefault() {
            throw new IllegalArgumentException("Cannot backfill non-defaultable parameter type.");
        }
    };

    /**
     * Generates default values for types, where optional has a default value of null.
     * <p>
     * This is required by retrofit on service methods, for example.
     */
    public static final Type.Visitor<CodeBlock> TYPE_DEFAULT_VALUE_OPTIONALS_NULL
            = new TypeVisitor.Default<CodeBlock>() {
                @Override
                public CodeBlock visitOptional(OptionalType value) {
                    return CodeBlock.of("null", Optional.class);
                }

                @Override
                public CodeBlock visitList(ListType value) {
                    return CodeBlock.of("$T.emptyList()", Collections.class);
                }

                @Override
                public CodeBlock visitSet(SetType value) {
                    return CodeBlock.of("$T.emptySet()", Collections.class);
                }

                @Override
                public CodeBlock visitMap(MapType value) {
                    return CodeBlock.of("$T.emptyMap()", Collections.class);
                }

                @Override
                public CodeBlock visitDefault() {
                    throw new IllegalArgumentException("Cannot backfill non-defaultable parameter type.");
                }
            };
}
