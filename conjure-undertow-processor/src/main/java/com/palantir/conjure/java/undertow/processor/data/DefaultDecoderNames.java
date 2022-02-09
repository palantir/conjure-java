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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.undertow.annotations.ParamDecoders;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.BearerToken;
import java.time.OffsetDateTime;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.Stream;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

final class DefaultDecoderNames {
    /** Mirrored from {@link ParamDecoders}. */
    @VisibleForTesting
    static final ImmutableList<String> SUPPORTED_CLASSES = ImmutableList.of(
            String.class.getName(),
            Boolean.class.getName(),
            BearerToken.class.getName(),
            OffsetDateTime.class.getName(),
            Double.class.getName(),
            OptionalDouble.class.getName(),
            Integer.class.getName(),
            OptionalInt.class.getName(),
            ResourceIdentifier.class.getName(),
            SafeLong.class.getName(),
            UUID.class.getName());

    private static final ImmutableSet<ContainerType> INPUT_TYPES =
            ImmutableSet.of(ContainerType.NONE, ContainerType.LIST);

    /**
     * Generates the corresponding method name of the default decoder as declared in
     * {@link com.palantir.conjure.java.undertow.annotations.ParamDecoders}.
     *
     * E.g. calling {@code getDefaultDecoderMethodName(String, LIST, OPTIONAL} will return
     * 'optionalStringCollectionParamDecoder' as the respective factory method name for
     * {@code CollectionParamDecoder<Optional<String>>}.
     *
     * @param type the type of the encoder, e.g. 'string' or 'rid'.
     * @param inputType the container type used as input for the encoder, 'LIST' or 'NONE'.
     * @param outType the container type used as output for the encoder, 'LIST', 'SET', 'OPTIONAL', or 'NONE'.
     */
    static String getDefaultDecoderMethodName(TypeMirror type, ContainerType inputType, ContainerType outType) {
        return getDefaultDecoderMethodName(getClassNameForTypeMirror(type), inputType, outType);
    }

    @VisibleForTesting
    static String getDefaultDecoderMethodName(String className, ContainerType inputType, ContainerType outType) {
        Preconditions.checkState(
                SUPPORTED_CLASSES.contains(className),
                "Default decoder not supported for this class. Please provide your own decoder implementation",
                SafeArg.of("class", className));
        Preconditions.checkState(
                INPUT_TYPES.contains(inputType),
                "Only list is allowed as container for encoders",
                SafeArg.of("type", inputType));

        String optionalPrefix = outType.equals(ContainerType.OPTIONAL) ? "optional" : "";
        String type = getTypeName(className);
        String decoderSuffix = inputType.equals(ContainerType.LIST) ? "CollectionParamDecoder" : "ParamDecoder";
        String typeSuffix =
                outType.equals(ContainerType.LIST) || outType.equals(ContainerType.SET) ? outType.toString() : "";

        String[] segments = Stream.of(optionalPrefix, type, typeSuffix, decoderSuffix)
                .filter(segment -> !segment.isEmpty())
                .toArray(String[]::new);
        return InstanceVariables.joinCamelCase(segments);
    }

    private static String getTypeName(String className) {
        // We have a few special cases, where we don't want to use the full class name.
        if (className.equals(ResourceIdentifier.class.getName())) {
            return "rid";
        } else if (className.equals(OffsetDateTime.class.getName())) {
            return "dateTime";
        } else if (className.equals(OptionalInt.class.getName())) {
            return "optionalInteger";
        } else {
            int lastDelimiterIndex = className.lastIndexOf('.');
            return lastDelimiterIndex < 0 ? className : className.substring(lastDelimiterIndex + 1);
        }
    }

    private static String getClassNameForTypeMirror(TypeMirror typeMirror) {
        // Only need to support primitives that are also supported by {@link PlainSerDe}.
        switch (typeMirror.getKind()) {
            case INT:
                return Integer.class.getName();
            case DOUBLE:
                return Double.class.getName();
            case BOOLEAN:
                return Boolean.class.getName();
            case DECLARED:
                DeclaredType declaredType = (DeclaredType) typeMirror;
                TypeElement typeElement = (TypeElement) declaredType.asElement();
                return typeElement.getQualifiedName().toString();
            default:
                throw new SafeIllegalStateException("Unsupported type", SafeArg.of("type", typeMirror));
        }
    }

    enum ContainerType {
        NONE(""),
        OPTIONAL("optional"),
        LIST("list"),
        SET("set");

        private final String value;

        ContainerType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private DefaultDecoderNames() {}
}
