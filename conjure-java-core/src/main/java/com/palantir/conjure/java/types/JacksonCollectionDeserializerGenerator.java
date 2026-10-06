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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.impl.JDKValueInstantiators;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;
import com.palantir.javapoet.WildcardTypeName;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import javax.lang.model.element.Modifier;

final class JacksonCollectionDeserializerGenerator {
    private JacksonCollectionDeserializerGenerator() {}

    static TypeSpec mapDeserializer(ClassName className) {
        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.FINAL)
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(JacksonSupportGenerator.class))
                .superclass(
                        ParameterizedTypeName.get(ClassName.get(JsonDeserializer.class), ClassName.get(Object.class)))
                .addSuperinterface(ContextualDeserializer.class)
                .addMethod(MethodSpec.methodBuilder("createContextual")
                        .returns(ParameterizedTypeName.get(
                                ClassName.get(JsonDeserializer.class), WildcardTypeName.subtypeOf(Object.class)))
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(DeserializationContext.class, "context")
                        .addParameter(BeanProperty.class, "property")
                        .addException(JsonMappingException.class)
                        .addCode(
                                """
                                $T type = property.getType();
                                $T<?> delegate = context.findContextualValueDeserializer(type, property);
                                // Custom constructors and problem handlers can return shared maps.
                                if (delegate.getClass() == $T.class
                                        && context.getConfig().getProblemHandlers() == null
                                        && (($T) delegate).getValueInstantiator().getClass()
                                                == $T.findStdValueInstantiator(context.getConfig(), $T.class)
                                                        .getClass()
                                        && hasStandardEntries(($T) delegate, context, property)
                                        && !hasTypeDeserializer(context, type)
                                        && !hasTypeDeserializer(context, type.getKeyType())
                                        && !hasTypeDeserializer(context, type.getContentType())) {
                                    return delegate;
                                }
                                return new CopyingDeserializer(delegate);
                                """,
                                JavaType.class,
                                JsonDeserializer.class,
                                MapDeserializer.class,
                                MapDeserializer.class,
                                JDKValueInstantiators.class,
                                LinkedHashMap.class,
                                MapDeserializer.class)
                        .build())
                .addMethod(MethodSpec.methodBuilder("hasTypeDeserializer")
                        .returns(TypeName.BOOLEAN)
                        .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                        .addParameter(DeserializationContext.class, "context")
                        .addParameter(JavaType.class, "type")
                        .addException(JsonMappingException.class)
                        .addCode("""
                            return type.getTypeHandler() != null || context.getConfig().findTypeDeserializer(type) != null;
                            """)
                        .build())
                .addMethod(MethodSpec.methodBuilder("hasStandardEntries")
                        .returns(TypeName.BOOLEAN)
                        .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                        .addParameter(MapDeserializer.class, "deserializer")
                        .addParameter(DeserializationContext.class, "context")
                        .addParameter(BeanProperty.class, "property")
                        .addException(JsonMappingException.class)
                        .addCode(
                                """
                                // Custom key/value deserializers can retain the map exposed by JsonParser.currentValue().
                                $T<?> content = deserializer.getContentDeserializer();
                                $T keyType = property.getType().getKeyType();
                                return (content == null || (content instanceof $T<?> && $T.isJacksonStdImpl(content)))
                                        && keyType.getValueHandler() == null
                                        && $T.isJacksonStdImpl(context.findKeyDeserializer(keyType, property));
                                """,
                                JsonDeserializer.class,
                                JavaType.class,
                                StdScalarDeserializer.class,
                                ClassUtil.class,
                                ClassUtil.class)
                        .build())
                .addMethod(MethodSpec.methodBuilder("deserialize")
                        .returns(Object.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(JsonParser.class, "_parser")
                        .addParameter(DeserializationContext.class, "_context")
                        .addCode("""
                            throw new $T("Map deserializer must be contextualized");
                            """, SafeIllegalStateException.class)
                        .build())
                .addType(copyingDeserializer(false))
                .build();
    }

    static TypeSpec setDeserializer(ClassName className) {
        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.FINAL)
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(JacksonSupportGenerator.class))
                .superclass(
                        ParameterizedTypeName.get(ClassName.get(JsonDeserializer.class), ClassName.get(Object.class)))
                .addSuperinterface(ContextualDeserializer.class)
                .addMethod(MethodSpec.methodBuilder("createContextual")
                        .returns(ParameterizedTypeName.get(
                                ClassName.get(JsonDeserializer.class), WildcardTypeName.subtypeOf(Object.class)))
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(DeserializationContext.class, "context")
                        .addParameter(BeanProperty.class, "property")
                        .addException(JsonMappingException.class)
                        .addCode(
                                """
                                // Jackson skips `as` refinement on delegating creators that also specify `using`.
                                $T type = context.constructSpecializedType(property.getType(), $T.class);
                                $T<?> delegate = context.findContextualValueDeserializer(type, property);
                                if ((delegate.getClass() == $T.class
                                                || delegate.getClass() == $T.class)
                                        && context.getConfig().getProblemHandlers() == null
                                        && (($T<?>) delegate)
                                                        .getValueInstantiator()
                                                        .getClass()
                                                == $T.findStdValueInstantiator(context.getConfig(), $T.class)
                                                        .getClass()
                                        && hasStandardElements(($T<?>) delegate)
                                        && type.getTypeHandler() == null
                                        && context.getConfig().findTypeDeserializer(type) == null
                                        && type.getContentType().getTypeHandler() == null
                                        && context.getConfig().findTypeDeserializer(type.getContentType()) == null) {
                                    return delegate;
                                }
                                return new CopyingDeserializer(delegate);
                                """,
                                JavaType.class,
                                LinkedHashSet.class,
                                JsonDeserializer.class,
                                CollectionDeserializer.class,
                                StringCollectionDeserializer.class,
                                ContainerDeserializerBase.class,
                                JDKValueInstantiators.class,
                                LinkedHashSet.class,
                                ContainerDeserializerBase.class)
                        .build())
                .addMethod(MethodSpec.methodBuilder("hasStandardElements")
                        .returns(TypeName.BOOLEAN)
                        .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                        .addParameter(
                                ParameterizedTypeName.get(
                                        ClassName.get(ContainerDeserializerBase.class),
                                        WildcardTypeName.subtypeOf(Object.class)),
                                "deserializer")
                        .addCode("""
                            // Custom element deserializers can retain the set exposed by JsonParser.currentValue().
                            $T<?> content = deserializer.getContentDeserializer();
                            return content == null || (content instanceof $T<?> && $T.isJacksonStdImpl(content));
                            """, JsonDeserializer.class, StdScalarDeserializer.class, ClassUtil.class)
                        .build())
                .addMethod(MethodSpec.methodBuilder("deserialize")
                        .returns(Object.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(JsonParser.class, "_parser")
                        .addParameter(DeserializationContext.class, "_context")
                        .addCode("""
                            throw new $T("Set deserializer must be contextualized");
                            """, SafeIllegalStateException.class)
                        .build())
                .addType(copyingDeserializer(true))
                .build();
    }

    private static TypeSpec copyingDeserializer(boolean set) {
        return TypeSpec.classBuilder("CopyingDeserializer")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .superclass(DelegatingDeserializer.class)
                .addMethod(MethodSpec.constructorBuilder()
                        .addParameter(
                                ParameterizedTypeName.get(
                                        ClassName.get(JsonDeserializer.class),
                                        WildcardTypeName.subtypeOf(Object.class)),
                                "delegate")
                        .addCode("""
                            super(delegate);
                            """)
                        .build())
                .addMethod(MethodSpec.methodBuilder("newDelegatingInstance")
                        .returns(ParameterizedTypeName.get(
                                ClassName.get(JsonDeserializer.class), WildcardTypeName.subtypeOf(Object.class)))
                        .addModifiers(Modifier.PROTECTED)
                        .addAnnotation(Override.class)
                        .addParameter(
                                ParameterizedTypeName.get(
                                        ClassName.get(JsonDeserializer.class),
                                        WildcardTypeName.subtypeOf(Object.class)),
                                "delegate")
                        .addCode("""
                            return new CopyingDeserializer(delegate);
                            """)
                        .build())
                .addMethod(MethodSpec.methodBuilder("deserialize")
                        .returns(Object.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(JsonParser.class, "parser")
                        .addParameter(DeserializationContext.class, "context")
                        .addException(IOException.class)
                        .addCode("""
                            return copy(super.deserialize(parser, context));
                            """)
                        .build())
                .addMethod(MethodSpec.methodBuilder("deserializeWithType")
                        .returns(Object.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(JsonParser.class, "parser")
                        .addParameter(DeserializationContext.class, "context")
                        .addParameter(TypeDeserializer.class, "typeDeserializer")
                        .addException(IOException.class)
                        .addCode("""
                            return copy(super.deserializeWithType(parser, context, typeDeserializer));
                            """)
                        .build())
                .addMethod(MethodSpec.methodBuilder("getNullValue")
                        .returns(Object.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(DeserializationContext.class, "context")
                        .addException(JsonMappingException.class)
                        .addCode("""
                            return copy(super.getNullValue(context));
                            """)
                        .build())
                .addMethod(MethodSpec.methodBuilder("getEmptyValue")
                        .returns(Object.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(DeserializationContext.class, "context")
                        .addException(JsonMappingException.class)
                        .addCode("""
                            return copy(super.getEmptyValue(context));
                            """)
                        .build())
                .addMethod(MethodSpec.methodBuilder("getAbsentValue")
                        .returns(Object.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(DeserializationContext.class, "context")
                        .addException(JsonMappingException.class)
                        .addCode("""
                            return copy(super.getAbsentValue(context));
                            """)
                        .build())
                .addMethod(MethodSpec.methodBuilder("copy")
                        .returns(Object.class)
                        .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                        .addParameter(Object.class, "value")
                        .addStatement(
                                "return value == null ? null : new $T<>(($T) value)",
                                set ? LinkedHashSet.class : LinkedHashMap.class,
                                set
                                        ? ParameterizedTypeName.get(
                                                ClassName.get(Collection.class),
                                                WildcardTypeName.subtypeOf(Object.class))
                                        : ParameterizedTypeName.get(
                                                ClassName.get(Map.class),
                                                WildcardTypeName.subtypeOf(Object.class),
                                                WildcardTypeName.subtypeOf(Object.class)))
                        .build())
                .build();
    }
}
