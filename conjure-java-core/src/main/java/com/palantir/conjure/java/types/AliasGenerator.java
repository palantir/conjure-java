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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.conjure.java.ConjureAnnotations;
import com.palantir.conjure.java.visitor.MoreVisitors;
import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.visitor.TypeDefinitionVisitor;
import com.palantir.conjure.visitor.TypeVisitor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.Objects;
import java.util.Optional;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

public final class AliasGenerator {

    private AliasGenerator() {}

    public static JavaFile generateAliasType(
            TypeMapper typeMapper,
            AliasDefinition typeDef) {

        TypeName aliasTypeName = typeMapper.getClassName(typeDef.getAlias());

        String typePackage = typeDef.getTypeName().getPackage();
        ClassName thisClass = ClassName.get(typePackage, typeDef.getTypeName().getName());

        TypeSpec.Builder spec = TypeSpec.classBuilder(typeDef.getTypeName().getName())
                .addAnnotation(ConjureAnnotations.getConjureGeneratedAnnotation(AliasGenerator.class))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(aliasTypeName, "value", Modifier.PRIVATE, Modifier.FINAL)
                .addMethod(createConstructor(aliasTypeName))
                .addMethod(MethodSpec.methodBuilder("get")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(JsonValue.class)
                        .returns(aliasTypeName)
                        .addStatement("return value")
                        .build())
                .addMethod(MethodSpec.methodBuilder("toString")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .returns(String.class)
                        .addCode(primitiveSafeToString(aliasTypeName))
                        .build())
                .addMethod(MethodSpec.methodBuilder("equals")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(TypeName.OBJECT, "other")
                        .returns(TypeName.BOOLEAN)
                        .addCode(primitiveSafeEquality(thisClass, aliasTypeName))
                        .build())
                .addMethod(MethodSpec.methodBuilder("hashCode")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .returns(TypeName.INT)
                        .addCode(primitiveSafeHashCode(aliasTypeName))
                        .build());

        Optional<CodeBlock> maybeValueOfFactoryMethod = valueOfFactoryMethod(
                typeDef.getAlias(), thisClass, aliasTypeName, typeMapper);
        if (maybeValueOfFactoryMethod.isPresent()) {
            spec.addMethod(MethodSpec.methodBuilder("valueOf")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(String.class, "value")
                    .returns(thisClass)
                    .addCode(maybeValueOfFactoryMethod.get())
                    .build());
        }

        spec.addMethod(MethodSpec.methodBuilder("of")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addAnnotation(JsonCreator.class)
                .addParameter(aliasTypeName, "value")
                .returns(thisClass)
                .addStatement("return new $T(value)", thisClass)
                .build());

        if (typeDef.getAlias().accept(TypeVisitor.IS_PRIMITIVE) && typeDef.getAlias().accept(
                TypeVisitor.PRIMITIVE).equals(PrimitiveType.DOUBLE)) {
            CodeBlock codeBlock = CodeBlock.builder()
                    .addStatement("return new $T((double) value)", thisClass)
                    .build();

            spec.addMethod(MethodSpec.methodBuilder("of")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addAnnotation(JsonCreator.class)
                    .addParameter(TypeName.INT, "value")
                    .returns(thisClass)
                    .addCode(codeBlock)
                    .build());

            CodeBlock doubleFromStringCodeBlock = CodeBlock.builder()
                    .beginControlFlow("switch (value)")
                    .add("case \"NaN\":\n")
                    .indent()
                    .addStatement("return $T.of($T.NaN)", thisClass, Double.class)
                    .unindent()
                    .add("case \"Infinity\":\n")
                    .indent()
                    .addStatement("return $T.of($T.POSITIVE_INFINITY)", thisClass, Double.class)
                    .unindent()
                    .add("case \"-Infinity\":\n")
                    .indent()
                    .addStatement("return $T.of($T.NEGATIVE_INFINITY)", thisClass, Double.class)
                    .unindent()
                    .add("default:\n")
                    .indent()
                    .addStatement(
                            "throw new $T(\"Cannot deserialize string into double: \" + value)",
                            IllegalArgumentException.class)
                    .unindent()
                    .endControlFlow()
                    .build();

            spec.addMethod(MethodSpec.methodBuilder("of")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addAnnotation(JsonCreator.class)
                    .addParameter(ClassName.get(String.class), "value")
                    .returns(thisClass)
                    .addCode(doubleFromStringCodeBlock)
                    .build());
        }

        typeDef.getDocs().ifPresent(docs -> spec.addJavadoc("$L", StringUtils.appendIfMissing(docs.get(), "\n")));

        return JavaFile.builder(typePackage, spec.build())
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }

    private static Optional<CodeBlock> valueOfFactoryMethod(
            Type conjureType,
            ClassName thisClass,
            TypeName aliasTypeName,
            TypeMapper typeMapper) {
        // doesn't support valueOf factories for ANY or BINARY types
        if (conjureType.accept(TypeVisitor.IS_PRIMITIVE)
                && !conjureType.accept(TypeVisitor.IS_ANY)
                && !conjureType.accept(TypeVisitor.IS_BINARY)) {
            return Optional.of(valueOfFactoryMethodForPrimitive(
                    conjureType.accept(TypeVisitor.PRIMITIVE), thisClass, aliasTypeName));
        } else if (conjureType.accept(MoreVisitors.IS_INTERNAL_REFERENCE)) {
            return typeMapper.getType(conjureType.accept(TypeVisitor.REFERENCE))
                    .filter(type -> type.accept(TypeDefinitionVisitor.IS_ALIAS))
                    .map(type -> type.accept(TypeDefinitionVisitor.ALIAS))
                    .flatMap(type -> valueOfFactoryMethod(type.getAlias(), thisClass, aliasTypeName, typeMapper)
                            .map(ignored -> {
                                ClassName className = ClassName.get(
                                        type.getTypeName().getPackage(), type.getTypeName().getName());
                                return CodeBlock.builder()
                                        .addStatement("return new $T($T.valueOf(value))", thisClass, className).build();
                            }));
        }
        return Optional.empty();
    }

    @SuppressWarnings("checkstyle:cyclomaticcomplexity")
    private static CodeBlock valueOfFactoryMethodForPrimitive(
            PrimitiveType primitiveType,
            ClassName thisClass,
            TypeName aliasTypeName) {
        switch (primitiveType.get()) {
            case STRING:
                return CodeBlock.builder().addStatement("return new $T(value)", thisClass).build();
            case DOUBLE:
                return CodeBlock.builder()
                        .addStatement("return new $T($T.parseDouble(value))", thisClass, aliasTypeName.box()).build();
            case INTEGER:
                return CodeBlock.builder()
                        .addStatement("return new $T($T.parseInt(value))", thisClass, aliasTypeName.box()).build();
            case BOOLEAN:
                return CodeBlock.builder()
                        .addStatement("return new $T($T.parseBoolean(value))", thisClass, aliasTypeName.box()).build();
            case SAFELONG:
            case RID:
            case BEARERTOKEN:
                return CodeBlock.builder()
                        .addStatement("return new $T($T.valueOf(value))", thisClass, aliasTypeName).build();
            case UUID:
                return CodeBlock.builder()
                        .addStatement("return new $T($T.fromString(value))", thisClass, aliasTypeName).build();
            case DATETIME:
                return CodeBlock.builder()
                        .addStatement("return new $T($T.parse(value))", thisClass, aliasTypeName).build();
            case BINARY:
            case ANY:
            case UNKNOWN:
        }
        throw new IllegalStateException(
                "Unsupported primitive type: " + primitiveType + "for `valueOf` method.");
    }

    private static MethodSpec createConstructor(TypeName aliasTypeName) {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(aliasTypeName, "value");
        if (!aliasTypeName.isPrimitive()) {
            builder.addStatement("$T.requireNonNull(value, \"value cannot be null\")", Objects.class);
        }
        return builder
                .addStatement("this.value = value")
                .build();
    }

    private static CodeBlock primitiveSafeEquality(ClassName thisClass, TypeName aliasTypeName) {
        if (aliasTypeName.isPrimitive()) {
            return CodeBlocks.statement(
                    "return this == other || (other instanceof $1T && this.value == (($1T) other).value)",
                    thisClass);
        }
        return CodeBlocks.statement(
                "return this == other || (other instanceof $1T && this.value.equals((($1T) other).value))",
                thisClass);
    }

    private static CodeBlock primitiveSafeToString(TypeName aliasTypeName) {
        if (aliasTypeName.isPrimitive()) {
            return CodeBlocks.statement("return $T.valueOf(value)", String.class);
        }
        return CodeBlocks.statement("return value.toString()");
    }

    private static CodeBlock primitiveSafeHashCode(TypeName aliasTypeName) {
        if (aliasTypeName.isPrimitive()) {
            return CodeBlocks.statement("return $T.hashCode(value)", aliasTypeName.box());
        }
        return CodeBlocks.statement("return value.hashCode()");
    }
}
