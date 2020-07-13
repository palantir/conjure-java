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
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.util.Javadoc;
import com.palantir.conjure.java.util.Packages;
import com.palantir.conjure.java.visitor.MoreVisitors;
import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.visitor.TypeDefinitionVisitor;
import com.palantir.conjure.visitor.TypeVisitor;
import com.palantir.logsafe.Preconditions;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Optional;
import javax.lang.model.element.Modifier;

public final class AliasGenerator {

    private AliasGenerator() {}

    public static JavaFile generateAliasType(TypeMapper typeMapper, AliasDefinition typeDef, Options options) {
        com.palantir.conjure.spec.TypeName prefixedTypeName =
                Packages.getPrefixedName(typeDef.getTypeName(), options.packagePrefix());
        TypeName aliasTypeName = typeMapper.getClassName(typeDef.getAlias());

        ClassName thisClass = ClassName.get(prefixedTypeName.getPackage(), prefixedTypeName.getName());

        TypeSpec.Builder spec = TypeSpec.classBuilder(prefixedTypeName.getName())
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

        Optional<CodeBlock> maybeValueOfFactoryMethod =
                valueOfFactoryMethod(typeDef.getAlias(), aliasTypeName, typeMapper, options);
        if (maybeValueOfFactoryMethod.isPresent()) {
            spec.addMethod(MethodSpec.methodBuilder("valueOf")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(String.class, "value")
                    .returns(thisClass)
                    .addCode(maybeValueOfFactoryMethod.get())
                    .addAnnotations(
                            typeDef.getAlias().accept(MoreVisitors.IS_EXTERNAL)
                                    // JsonCreator behaves in unexpected ways:
                                    // https://github.com/FasterXML/jackson-databind/issues/2318
                                    // allow jackson to try all possible approaches to deserialize external type
                                    // imports.
                                    ? Collections.singleton(AnnotationSpec.builder(JsonCreator.class)
                                            .build())
                                    : Collections.emptySet())
                    .build());
        }

        spec.addMethod(MethodSpec.methodBuilder("of")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addAnnotation(JsonCreator.class)
                .addParameter(Parameters.nonnullParameter(aliasTypeName, "value"))
                .returns(thisClass)
                .addStatement("return new $T(value)", thisClass)
                .build());

        // Generate a default constructor so that Jackson can construct a default instance when coercing from null
        if (typeDef.getAlias().accept(TypeVisitor.IS_OPTIONAL)) {
            spec.addMethod(createDefaultConstructor(aliasTypeName));
        }

        if (typeDef.getAlias().accept(TypeVisitor.IS_PRIMITIVE)
                && typeDef.getAlias().accept(TypeVisitor.PRIMITIVE).equals(PrimitiveType.DOUBLE)) {
            CodeBlock longCastCodeBlock = CodeBlock.builder()
                    .addStatement("long safeValue = $T.of(value).longValue()", SafeLong.class)
                    .addStatement("return new $T((double) safeValue)", thisClass)
                    .build();

            CodeBlock intCastCodeBlock = CodeBlock.builder()
                    .addStatement("return new $T((double) value)", thisClass)
                    .build();

            spec.addMethod(MethodSpec.methodBuilder("of")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addAnnotation(JsonCreator.class)
                    .addParameter(TypeName.LONG, "value")
                    .returns(thisClass)
                    .addCode(longCastCodeBlock)
                    .build());

            spec.addMethod(MethodSpec.methodBuilder("of")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addAnnotation(JsonCreator.class)
                    .addParameter(TypeName.INT, "value")
                    .returns(thisClass)
                    .addCode(intCastCodeBlock)
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

        typeDef.getDocs().ifPresent(docs -> spec.addJavadoc("$L", Javadoc.render(docs)));

        return JavaFile.builder(prefixedTypeName.getPackage(), spec.build())
                .skipJavaLangImports(true)
                .indent("    ")
                .build();
    }

    private static MethodSpec createDefaultConstructor(TypeName aliasTypeName) {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addStatement(
                        "this($T.empty())",
                        aliasTypeName instanceof ParameterizedTypeName ? Optional.class : aliasTypeName)
                .build();
    }

    private static Optional<CodeBlock> valueOfFactoryMethod(
            Type conjureType, TypeName aliasTypeName, TypeMapper typeMapper, Options options) {
        // doesn't support valueOf factories for ANY or BINARY types
        if (conjureType.accept(TypeVisitor.IS_PRIMITIVE)
                && !conjureType.accept(TypeVisitor.IS_ANY)
                && !conjureType.accept(TypeVisitor.IS_BINARY)) {
            return Optional.of(
                    valueOfFactoryMethodForPrimitive(conjureType.accept(TypeVisitor.PRIMITIVE), aliasTypeName));
        } else if (conjureType.accept(MoreVisitors.IS_INTERNAL_REFERENCE)) {
            return typeMapper
                    .getType(conjureType.accept(TypeVisitor.REFERENCE))
                    .filter(type -> type.accept(TypeDefinitionVisitor.IS_ALIAS))
                    .map(type -> type.accept(TypeDefinitionVisitor.ALIAS))
                    .flatMap(type -> valueOfFactoryMethod(
                                    type.getAlias(), typeMapper.getClassName(type.getAlias()), typeMapper, options)
                            .map(ignored -> {
                                ClassName className = ClassName.get(
                                        Packages.getPrefixedPackage(
                                                type.getTypeName().getPackage(), options.packagePrefix()),
                                        type.getTypeName().getName());
                                return CodeBlock.builder()
                                        .addStatement("return of($T.valueOf(value))", className)
                                        .build();
                            }));
        } else if (conjureType.accept(MoreVisitors.IS_EXTERNAL)) {
            ExternalReference reference = conjureType.accept(MoreVisitors.EXTERNAL);
            // Only generate valueOf methods for external type imports if the fallback type is valid
            if (valueOfFactoryMethod(
                                    reference.getFallback(),
                                    typeMapper.getClassName(reference.getFallback()),
                                    typeMapper,
                                    options)
                            .isPresent()
                    && hasValueOfFactory(reference.getExternalReference())
                    && aliasTypeName.isPrimitive()) {
                return Optional.of(CodeBlock.builder()
                        .addStatement("return of($T.valueOf(value))", aliasTypeName.box())
                        .build());
            }
        }
        return Optional.empty();
    }

    @SuppressWarnings("checkstyle:cyclomaticcomplexity")
    private static CodeBlock valueOfFactoryMethodForPrimitive(PrimitiveType primitiveType, TypeName aliasTypeName) {
        switch (primitiveType.get()) {
            case STRING:
                return CodeBlock.builder().addStatement("return of(value)").build();
            case DOUBLE:
                return CodeBlock.builder()
                        .addStatement("return of($T.parseDouble(value))", aliasTypeName.box())
                        .build();
            case INTEGER:
                return CodeBlock.builder()
                        .addStatement("return of($T.parseInt(value))", aliasTypeName.box())
                        .build();
            case BOOLEAN:
                return CodeBlock.builder()
                        .addStatement("return of($T.parseBoolean(value))", aliasTypeName.box())
                        .build();
            case SAFELONG:
            case RID:
            case BEARERTOKEN:
                return CodeBlock.builder()
                        .addStatement("return of($T.valueOf(value))", aliasTypeName)
                        .build();
            case UUID:
                return CodeBlock.builder()
                        .addStatement("return of($T.fromString(value))", aliasTypeName)
                        .build();
            case DATETIME:
                return CodeBlock.builder()
                        .addStatement("return of($T.parse(value))", aliasTypeName)
                        .build();
            case BINARY:
            case ANY:
            case UNKNOWN:
        }
        throw new IllegalStateException("Unsupported primitive type: " + primitiveType + "for `valueOf` method.");
    }

    /**
     * Detects if the class is available in the generator classpath and has a public static <code>valueOf(String)
     * </code>.
     */
    private static boolean hasValueOfFactory(com.palantir.conjure.spec.TypeName className) {
        try {
            Class<?> clazz = Class.forName(className.getPackage() + '.' + className.getName());
            Method valueOf = clazz.getDeclaredMethod("valueOf", String.class);
            return java.lang.reflect.Modifier.isPublic(valueOf.getModifiers())
                    && java.lang.reflect.Modifier.isStatic(valueOf.getModifiers());
        } catch (ReflectiveOperationException e) {
            // Class doesn't exist on the code-gen classpath, or the type doesn't have a valueOf method
            return false;
        }
    }

    private static MethodSpec createConstructor(TypeName aliasTypeName) {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(Parameters.nonnullParameter(aliasTypeName, "value"));
        if (!aliasTypeName.isPrimitive()) {
            builder.addStatement("this.value = $T.checkNotNull(value, \"value cannot be null\")", Preconditions.class);
        } else {
            builder.addStatement("this.value = value");
        }
        return builder.build();
    }

    private static CodeBlock primitiveSafeEquality(ClassName thisClass, TypeName aliasTypeName) {
        if (aliasTypeName.isPrimitive()) {
            return CodeBlocks.statement(
                    "return this == other || (other instanceof $1T && this.value == (($1T) other).value)", thisClass);
        }
        return CodeBlocks.statement(
                "return this == other || (other instanceof $1T && this.value.equals((($1T) other).value))", thisClass);
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
