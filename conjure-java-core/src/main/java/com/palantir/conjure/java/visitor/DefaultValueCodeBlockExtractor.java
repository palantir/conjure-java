/*
 * (c) Copyright 2024 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.visitor;

import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.spec.DefaultValue;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.visitor.TypeVisitor;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import com.palantir.ri.ResourceIdentifier;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import java.util.UUID;

public final class DefaultValueCodeBlockExtractor implements DefaultValue.Visitor<CodeBlock> {
    private final Type expectedType;
    private final TypeName expectedClass;

    public DefaultValueCodeBlockExtractor(Type expectedType, TypeName expectedClass) {
        this.expectedType = expectedType;
        this.expectedClass = expectedClass;
    }

    @Override
    public CodeBlock visitEnum(String value) {
        validateTypeSafety(expectedType.accept(TypeVisitor.IS_REFERENCE));
        return CodeBlock.of("$1T.valueOf($2S)", expectedClass, value);
    }

    @Override
    public CodeBlock visitString(String value) {
        validateTypeSafety(expectedType.accept(TypeVisitor.IS_PRIMITIVE)
                && expectedType.accept(TypeVisitor.PRIMITIVE).accept(PrimitiveTypeVisitor.IS_STRING));
        return CodeBlock.of("$S", value);
    }

    @Override
    public CodeBlock visitInteger(int value) {
        validateTypeSafety(expectedType.accept(TypeVisitor.IS_PRIMITIVE)
                && expectedType.accept(TypeVisitor.PRIMITIVE).accept(PrimitiveTypeVisitor.IS_INTEGER));
        return CodeBlock.of("$L", value);
    }

    @Override
    public CodeBlock visitDouble(double value) {
        validateTypeSafety(expectedType.accept(TypeVisitor.IS_PRIMITIVE)
                && expectedType.accept(TypeVisitor.PRIMITIVE).accept(PrimitiveTypeVisitor.IS_DOUBLE));
        return CodeBlock.of("$L", value);
    }

    @Override
    public CodeBlock visitSafelong(SafeLong value) {
        validateTypeSafety(expectedType.accept(TypeVisitor.IS_PRIMITIVE)
                && expectedType.accept(TypeVisitor.PRIMITIVE).accept(PrimitiveTypeVisitor.IS_SAFELONG));
        return CodeBlock.of("$LL", value.longValue());
    }

    @Override
    public CodeBlock visitBoolean(boolean value) {
        validateTypeSafety(expectedType.accept(TypeVisitor.IS_PRIMITIVE)
                && expectedType.accept(TypeVisitor.PRIMITIVE).accept(PrimitiveTypeVisitor.IS_BOOLEAN));
        return CodeBlock.of("$L", value);
    }

    @Override
    public CodeBlock visitUuid(UUID value) {
        validateTypeSafety(expectedType.accept(TypeVisitor.IS_PRIMITIVE)
                && expectedType.accept(TypeVisitor.PRIMITIVE).accept(PrimitiveTypeVisitor.IS_UUID));
        return CodeBlock.of("$1T.fromString($2S)", UUID.class, value.toString());
    }

    @Override
    public CodeBlock visitRid(ResourceIdentifier value) {
        validateTypeSafety(expectedType.accept(TypeVisitor.IS_PRIMITIVE)
                && expectedType.accept(TypeVisitor.PRIMITIVE).accept(PrimitiveTypeVisitor.IS_RID));
        return CodeBlock.of("$1T.of($2S)", ResourceIdentifier.class, value.toString());
    }

    @Override
    public CodeBlock visitUnknown(@Safe String unknownType) {
        throw new SafeIllegalStateException("Encountered unknown type", SafeArg.of("type", unknownType));
    }

    private static void validateTypeSafety(boolean typesMatch) {
        if (!typesMatch) {
            throw new SafeIllegalStateException("Encountered default value that does not match its field type");
        }
    }
}
