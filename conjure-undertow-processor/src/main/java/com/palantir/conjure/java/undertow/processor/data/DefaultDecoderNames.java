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
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import com.palantir.ri.ResourceIdentifier;
import java.time.OffsetDateTime;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Stream;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

final class DefaultDecoderNames {

    /**
     * Produces the corresponding method name of the default decoder in
     * {@link com.palantir.conjure.java.undertow.annotations.ParamDecoders}.
     */
    static String getDefaultDecoderMethodName(TypeMirror type, ContainerType inputType, ContainerType outType) {
        return getDefaultDecoderMethodName(getClassForType(type), inputType, outType);
    }

    @VisibleForTesting
    static String getDefaultDecoderMethodName(Class<?> clazz, ContainerType inputType, ContainerType outType) {
        Preconditions.checkState(
                Set.of(ContainerType.NONE, ContainerType.LIST).contains(inputType),
                "Input container type must be 'LIST' or 'NONE'");

        String type = getTypeName(clazz);
        String decoderSuffix = inputType.equals(ContainerType.LIST) ? "CollectionParamDecoder" : "ParamDecoder";
        String optionalPrefix = outType.equals(ContainerType.OPTIONAL) ? "optional" : "";
        String typeSuffix =
                outType.equals(ContainerType.LIST) || outType.equals(ContainerType.SET) ? outType.toString() : "";

        String[] segments = Stream.of(optionalPrefix, type, typeSuffix, decoderSuffix)
                .filter(segment -> !segment.isEmpty())
                .toArray(String[]::new);
        return InstanceVariables.joinCamelCase(segments);
    }

    private static String getTypeName(Class<?> clazz) {
        if (clazz.equals(ResourceIdentifier.class)) {
            return "rid";
        } else if (clazz.equals(OffsetDateTime.class)) {
            return "dateTime";
        } else if (clazz.equals(OptionalInt.class)) {
            return "optionalInteger";
        } else {
            return clazz.getSimpleName();
        }
    }

    // TODO(fwindheuser): There is probably a better way to get the simple class name from a type mirror.
    private static Class<?> getClassForType(TypeMirror typeMirror) {
        // Only need to support primitives that are also supported by {@link PlainSerDe}.
        switch (typeMirror.getKind()) {
            case INT:
                return Integer.class;
            case DOUBLE:
                return Double.class;
            case BOOLEAN:
                return Boolean.class;
            case DECLARED:
                Element typeElement = ((DeclaredType) typeMirror).asElement();
                String qualifiedName =
                        ((TypeElement) typeElement).getQualifiedName().toString();
                try {
                    return Class.forName(qualifiedName);
                } catch (ClassNotFoundException e) {
                    throw new SafeIllegalStateException(
                            "Could not determine declared class", e, SafeArg.of("type", typeMirror));
                }
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
