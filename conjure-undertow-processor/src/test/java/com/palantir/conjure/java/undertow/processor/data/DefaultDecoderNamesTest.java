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

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.undertow.annotations.ParamDecoders;
import com.palantir.conjure.java.undertow.processor.data.DefaultDecoderNames.ContainerType;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class DefaultDecoderNamesTest {

    @Test
    void creates_default_string_decoder_method_names() {
        assertThat(DefaultDecoderNames.getDefaultDecoderMethodName(
                        String.class, ContainerType.NONE, ContainerType.NONE))
                .isEqualTo("stringParamDecoder");

        assertThat(DefaultDecoderNames.getDefaultDecoderMethodName(
                        String.class, ContainerType.NONE, ContainerType.OPTIONAL))
                .isEqualTo("optionalStringParamDecoder");

        assertThat(DefaultDecoderNames.getDefaultDecoderMethodName(
                        String.class, ContainerType.LIST, ContainerType.NONE))
                .isEqualTo("stringCollectionParamDecoder");

        assertThat(DefaultDecoderNames.getDefaultDecoderMethodName(
                        String.class, ContainerType.LIST, ContainerType.OPTIONAL))
                .isEqualTo("optionalStringCollectionParamDecoder");

        assertThat(DefaultDecoderNames.getDefaultDecoderMethodName(
                        String.class, ContainerType.LIST, ContainerType.LIST))
                .isEqualTo("stringListCollectionParamDecoder");

        assertThat(DefaultDecoderNames.getDefaultDecoderMethodName(String.class, ContainerType.LIST, ContainerType.SET))
                .isEqualTo("stringSetCollectionParamDecoder");
    }

    @ParameterizedTest
    @MethodSource("getParamDecoderArguments")
    void all_param_decoder_types_have_respective_method(
            Class<?> clazz, ContainerType inputType, ContainerType outputType) {
        Set<String> declaredMethodNames = Arrays.stream(ParamDecoders.class.getDeclaredMethods())
                .map(Method::getName)
                .collect(Collectors.toSet());

        String methodName = DefaultDecoderNames.getDefaultDecoderMethodName(clazz, inputType, outputType);

        assertThat(declaredMethodNames).contains(methodName);
    }

    private static List<Arguments> getParamDecoderArguments() {
        ImmutableList.Builder<Arguments> arguments = ImmutableList.builder();

        // TODO(fwindheuser): Add all PlainSerdeClasses
        List<Class<?>> supportedClasses = ImmutableList.of(String.class, Boolean.class);

        // For 'ParamDecoder', we only support mapping to Optional as output container.
        for (ContainerType outputContainer : ImmutableList.of(ContainerType.NONE, ContainerType.OPTIONAL)) {
            for (Class<?> clazz : supportedClasses) {
                arguments.add(Arguments.of(clazz, ContainerType.NONE, outputContainer));
            }
        }

        // For 'CollectionParamDecoder', we support mapping to Optional, List, and Set as output container.
        for (ContainerType outputContainer : ContainerType.values()) {
            for (Class<?> clazz : supportedClasses) {
                arguments.add(Arguments.of(clazz, ContainerType.LIST, outputContainer));
            }
        }

        return arguments.build();
    }
}
