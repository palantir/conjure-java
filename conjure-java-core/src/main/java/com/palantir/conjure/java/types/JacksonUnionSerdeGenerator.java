/*
 * (c) Copyright 2026 Palantir Technologies Inc. All rights reserved.
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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.javapoet.ArrayTypeName;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;
import com.palantir.javapoet.TypeVariableName;
import com.palantir.javapoet.WildcardTypeName;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReferenceArray;
import javax.lang.model.element.Modifier;

final class JacksonUnionSerdeGenerator {
    private JacksonUnionSerdeGenerator() {}

    static TypeSpec unionSerializer(ClassName className) {
        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.FINAL)
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(JacksonSupportGenerator.class))
                .superclass(ParameterizedTypeName.get(ClassName.get(JsonSerializer.class), ClassName.get(Object.class)))
                .addMethod(MethodSpec.methodBuilder("serialize")
                        .returns(TypeName.VOID)
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(Object.class, "value")
                        .addParameter(JsonGenerator.class, "generator")
                        .addParameter(SerializerProvider.class, "serializers")
                        .addException(IOException.class)
                        .addCode("""
                            serializers.findValueSerializer(value.getClass()).serialize(value, generator, serializers);
                            """)
                        .build())
                .addMethod(MethodSpec.methodBuilder("serializeWithType")
                        .returns(TypeName.VOID)
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(Object.class, "value")
                        .addParameter(JsonGenerator.class, "generator")
                        .addParameter(SerializerProvider.class, "serializers")
                        .addParameter(TypeSerializer.class, "typeSerializer")
                        .addException(IOException.class)
                        .addCode("""
                            serializers
                                    .findValueSerializer(value.getClass())
                                    .serializeWithType(value, generator, serializers, typeSerializer);
                            """)
                        .build())
                .build();
    }

    static TypeSpec unionDeserializer(ClassName className) {
        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.ABSTRACT)
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(JacksonSupportGenerator.class))
                .superclass(ParameterizedTypeName.get(ClassName.get(JsonDeserializer.class), TypeVariableName.get("T")))
                .addTypeVariable(TypeVariableName.get("T"))
                .addField(FieldSpec.builder(
                                ParameterizedTypeName.get(ClassName.get(Class.class), TypeVariableName.get("T")),
                                "unionClass",
                                Modifier.PRIVATE,
                                Modifier.FINAL)
                        .build())
                .addField(FieldSpec.builder(
                                ArrayTypeName.of(ParameterizedTypeName.get(
                                        ClassName.get(Class.class), WildcardTypeName.subtypeOf(Object.class))),
                                "variantTypes",
                                Modifier.PRIVATE,
                                Modifier.FINAL)
                        .build())
                .addField(FieldSpec.builder(
                                ParameterizedTypeName.get(
                                        ClassName.get(AtomicReferenceArray.class),
                                        ParameterizedTypeName.get(
                                                ClassName.get(JsonDeserializer.class),
                                                WildcardTypeName.subtypeOf(Object.class))),
                                "variantDeserializers",
                                Modifier.PRIVATE,
                                Modifier.FINAL)
                        .build())
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PROTECTED)
                        .addParameter(
                                ParameterizedTypeName.get(ClassName.get(Class.class), TypeVariableName.get("T")),
                                "unionClass")
                        .addParameter(
                                ArrayTypeName.of(ParameterizedTypeName.get(
                                        ClassName.get(Class.class), WildcardTypeName.subtypeOf(Object.class))),
                                "variantTypes")
                        .addCode("""
                            this.unionClass = unionClass;
                            this.variantTypes = variantTypes;
                            this.variantDeserializers = new $T<>(variantTypes.length);
                            """, AtomicReferenceArray.class)
                        .build())
                .addMethod(MethodSpec.methodBuilder("isCachable")
                        .returns(TypeName.BOOLEAN)
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addAnnotation(Override.class)
                        .addCode("""
                            return true;
                            """)
                        .build())
                .addMethod(generateDeserialize())
                .addMethod(generateDeserializeBuffered())
                .addMethod(MethodSpec.methodBuilder("isTypeField")
                        .returns(TypeName.BOOLEAN)
                        .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                        .addParameter(String.class, "fieldName")
                        .addParameter(TypeName.BOOLEAN, "acceptCaseInsensitiveProperties")
                        .addCode("""
                            return "type".equals(fieldName) || (acceptCaseInsensitiveProperties && "type".equalsIgnoreCase(fieldName));
                            """)
                        .build())
                .addMethod(MethodSpec.methodBuilder("deserializeSelected")
                        .returns(TypeVariableName.get("T"))
                        .addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
                        .addParameter(JsonParser.class, "parser")
                        .addParameter(DeserializationContext.class, "context")
                        .addParameter(String.class, "type")
                        .addException(IOException.class)
                        .build())
                .addMethod(MethodSpec.methodBuilder("deserializeVariant")
                        .returns(Object.class)
                        .addModifiers(Modifier.PROTECTED, Modifier.FINAL)
                        .addParameter(JsonParser.class, "parser")
                        .addParameter(DeserializationContext.class, "context")
                        .addParameter(TypeName.INT, "variantIndex")
                        .addException(IOException.class)
                        .addCode("""
                            $T<?> deserializer = variantDeserializers.get(variantIndex);
                            if (deserializer == null) {
                                deserializer = resolveDeserializer(context, variantIndex);
                            }
                            return deserializer.deserialize(parser, context);
                            """, JsonDeserializer.class)
                        .build())
                .addMethod(MethodSpec.methodBuilder("resolveDeserializer")
                        .returns(ParameterizedTypeName.get(
                                ClassName.get(JsonDeserializer.class), WildcardTypeName.subtypeOf(Object.class)))
                        .addModifiers(Modifier.PRIVATE)
                        .addParameter(DeserializationContext.class, "context")
                        .addParameter(TypeName.INT, "variantIndex")
                        .addException(JsonMappingException.class)
                        .addCode("""
                            $T<?> deserializer =
                                    context.findContextualValueDeserializer(context.constructType(variantTypes[variantIndex]), null);
                            if (variantDeserializers.compareAndSet(variantIndex, null, deserializer)) {
                                return deserializer;
                            }
                            return variantDeserializers.get(variantIndex);
                            """, JsonDeserializer.class)
                        .build())
                .addMethod(generateDeserializeUnknown())
                .build();
    }

    private static MethodSpec generateDeserialize() {
        return MethodSpec.methodBuilder("deserialize")
                .returns(TypeVariableName.get("T"))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(Override.class)
                .addParameter(JsonParser.class, "parser")
                .addParameter(DeserializationContext.class, "context")
                .addException(IOException.class)
                .addCode(
                        """
                        // Delegating creators may consume START_OBJECT before invoking this deserializer.
                        $T firstToken = parser.currentToken();
                        if (parser.isExpectedStartObjectToken()) {
                            firstToken = parser.nextToken();
                        } else if (firstToken != $T.FIELD_NAME && firstToken != $T.END_OBJECT) {
                            return context.reportInputMismatch(unionClass, "Expected a JSON object for union deserialization");
                        }
                        boolean acceptCaseInsensitiveProperties = context.isEnabled($T.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
                        if (firstToken == $T.FIELD_NAME && isTypeField(parser.currentName(), acceptCaseInsensitiveProperties)) {
                            if (parser.nextToken() != $T.VALUE_STRING) {
                                return context.reportInputMismatch(unionClass, "Union discriminator 'type' must be a string");
                            }
                            $T type = parser.getText();
                            parser.nextToken();
                            return deserializeSelected(parser, context, type);
                        }
                        return deserializeBuffered(parser, context, acceptCaseInsensitiveProperties);
                        """,
                        JsonToken.class,
                        JsonToken.class,
                        JsonToken.class,
                        MapperFeature.class,
                        JsonToken.class,
                        JsonToken.class,
                        String.class)
                .build();
    }

    private static MethodSpec generateDeserializeBuffered() {
        return MethodSpec.methodBuilder("deserializeBuffered")
                .returns(TypeVariableName.get("T"))
                .addModifiers(Modifier.PRIVATE)
                .addParameter(JsonParser.class, "parser")
                .addParameter(DeserializationContext.class, "context")
                .addParameter(TypeName.BOOLEAN, "acceptCaseInsensitiveProperties")
                .addException(IOException.class)
                .addCode(
                        """
                        try ($T buffer = context.bufferForInputBuffering(parser)) {
                            buffer.writeStartObject();
                            $T token = parser.currentToken();
                            while (token == $T.FIELD_NAME) {
                                $T fieldName = parser.currentName();
                                $T valueToken = parser.nextToken();
                                if (isTypeField(fieldName, acceptCaseInsensitiveProperties)) {
                                    if (valueToken != $T.VALUE_STRING) {
                                        return context.reportInputMismatch(unionClass, "Union discriminator 'type' must be a string");
                                    }
                                    $T type = parser.getText();
                                    parser.nextToken();
                                    try ($T bufferedParser = buffer.asParser(parser)) {
                                        $T combinedParser = $T.createFlattened(true, bufferedParser, parser);
                                        combinedParser.nextToken();
                                        return deserializeSelected(combinedParser, context, type);
                                    }
                                }
                                buffer.writeFieldName(fieldName);
                                buffer.copyCurrentStructure(parser);
                                token = parser.nextToken();
                            }
                            if (token != $T.END_OBJECT) {
                                return context.reportInputMismatch(
                                        unionClass, "Expected the end of a JSON object while deserializing a union");
                            }
                        }
                        return context.reportInputMismatch(unionClass, "Union discriminator 'type' is required");
                        """,
                        TokenBuffer.class,
                        JsonToken.class,
                        JsonToken.class,
                        String.class,
                        JsonToken.class,
                        JsonToken.class,
                        String.class,
                        JsonParser.class,
                        JsonParser.class,
                        JsonParserSequence.class,
                        JsonToken.class)
                .build();
    }

    private static MethodSpec generateDeserializeUnknown() {
        return MethodSpec.methodBuilder("deserializeUnknown")
                .returns(ParameterizedTypeName.get(
                        ClassName.get(Map.class), ClassName.get(String.class), ClassName.get(Object.class)))
                .addModifiers(Modifier.PROTECTED, Modifier.FINAL)
                .addParameter(JsonParser.class, "parser")
                .addParameter(DeserializationContext.class, "context")
                .addException(IOException.class)
                .addCode(
                        """
                        $T<$T, $T> values = new $T<>();
                        $T<$T> valueDeserializer = null;
                        if (parser.currentToken() == $T.START_OBJECT) {
                            parser.nextToken();
                        }
                        while (parser.currentToken() == $T.FIELD_NAME) {
                            $T fieldName = parser.currentName();
                            parser.nextToken();
                            if (valueDeserializer == null) {
                                valueDeserializer = context.findRootValueDeserializer(context.constructType($T.class));
                            }
                            values.put(
                                    fieldName,
                                    parser.currentToken() == $T.VALUE_NULL
                                            ? valueDeserializer.getNullValue(context)
                                            : valueDeserializer.deserialize(parser, context));
                            parser.nextToken();
                        }
                        if (parser.currentToken() != $T.END_OBJECT) {
                            return context.reportInputMismatch(
                                    unionClass, "Expected the end of a JSON object while deserializing a union");
                        }
                        return values;
                        """,
                        Map.class,
                        String.class,
                        Object.class,
                        HashMap.class,
                        JsonDeserializer.class,
                        Object.class,
                        JsonToken.class,
                        JsonToken.class,
                        String.class,
                        Object.class,
                        JsonToken.class,
                        JsonToken.class)
                .build();
    }
}
