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

package com.palantir.conjure.java.services.dialogue;

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.ConjureTags;
import com.palantir.conjure.java.services.Auth;
import com.palantir.conjure.java.types.SafetyEvaluator;
import com.palantir.conjure.java.types.TypeMapper;
import com.palantir.conjure.java.visitor.DefaultTypeVisitor;
import com.palantir.conjure.spec.ArgumentDefinition;
import com.palantir.conjure.spec.BodyParameterType;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.HeaderParameterType;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.LogSafety;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.ParameterType;
import com.palantir.conjure.spec.PathParameterType;
import com.palantir.conjure.spec.QueryParameterType;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.spec.Type;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class ParameterTypeMapper {

    private final TypeMapper parameterTypes;
    private final SafetyEvaluator safetyEvaluator;

    public ParameterTypeMapper(TypeMapper parameterTypes, SafetyEvaluator safetyEvaluator) {
        this.parameterTypes = parameterTypes;
        this.safetyEvaluator = safetyEvaluator;
    }

    public TypeName baseType(Type type) {
        return parameterTypes.getClassName(type);
    }

    public List<ParameterSpec> interfaceMethodParams(EndpointDefinition endpointDef) {
        return methodParams(endpointDef, true);
    }

    public List<ParameterSpec> implementationMethodParams(EndpointDefinition endpointDef) {
        return methodParams(endpointDef, false);
    }

    private List<ParameterSpec> methodParams(EndpointDefinition endpointDef, boolean includeSafetyAnnotations) {
        ImmutableList.Builder<ParameterSpec> paramSpecBuilder = ImmutableList.builder();
        endpointDef.getAuth().ifPresent(auth -> paramSpecBuilder.add(Auth.authParam(auth)));
        endpointDef.getArgs().stream()
                .sorted(Comparator.comparing(o -> o.getParamType().accept(PARAM_SORT_VISITOR)
                        + o.getType().accept(TYPE_SORT_VISITOR)))
                .forEach(def -> paramSpecBuilder.add(param(def, includeSafetyAnnotations)));

        return paramSpecBuilder.build();
    }

    private ParameterSpec param(ArgumentDefinition def, boolean includeSafetyAnnotations) {
        Optional<LogSafety> safety = ConjureTags.validateArgument(def, safetyEvaluator);
        ParameterSpec.Builder param = ParameterSpec.builder(
                parameterTypes.getClassName(def.getType()), def.getArgName().get());
        if (includeSafetyAnnotations) {
            // Safety annotations are helpful to inform developers of safety guarantees, and to
            // reinforce static analysis tooling which validates arguments to annotated methods.
            param.addAnnotations(ConjureAnnotations.safety(safety));
        }
        return param.build();
    }

    /** Produces an ordering for ParamaterType of Header, Path, Query, Body. */
    private static final ParameterType.Visitor<Integer> PARAM_SORT_VISITOR = new ParameterType.Visitor<Integer>() {
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
        public Integer visitBody(BodyParameterType value) {
            return 30;
        }

        @Override
        public Integer visitUnknown(String unknownType) {
            return -1;
        }
    };

    /**
     * Produces a type sort ordering for use with {@link #PARAM_SORT_VISITOR} such that types with known defaults come
     * after types without known defaults.
     */
    private static final Type.Visitor<Integer> TYPE_SORT_VISITOR = new DefaultTypeVisitor<Integer>() {
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
}
