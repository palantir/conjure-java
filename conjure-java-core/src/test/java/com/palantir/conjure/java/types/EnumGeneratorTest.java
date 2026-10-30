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

import static org.assertj.core.api.Assertions.assertThat;

import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.defs.ConjureArgs;
import com.palantir.conjure.defs.SafetyDeclarationRequirements;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.EnumDefinition;
import com.palantir.conjure.spec.EnumValueDefinition;
import com.palantir.conjure.spec.TypeName;
import com.palantir.conjure.visitor.TypeDefinitionVisitor;
import com.palantir.javapoet.JavaFile;
import java.io.File;
import java.util.List;
import org.junit.jupiter.api.Test;

public class EnumGeneratorTest {

    private static final TypeName TEST_ENUM = TypeName.of("TestEnumWithUnknownMember", "com.test");

    @Test
    void visitorBuilderUsesUnderscoreForUnknownHandlerStage() {
        String source = generateSource(parseEnum("src/test/resources/enum-one-value.yml"));

        assertThat(source).contains("Unknown_StageVisitorBuilder");
        assertThat(source).doesNotContain("interface UnknownStageVisitorBuilder");
    }

    @Test
    void visitorBuilderAvoidsUnknownStageNameCollisionWhenEnumHasUnknownMember() {
        EnumDefinition definition = EnumDefinition.builder()
                .typeName(TEST_ENUM)
                .values(EnumValueDefinition.builder().value("INCOMPLETE").build())
                .values(EnumValueDefinition.builder().value("UNKNOWN").build())
                .build();

        String source = generateSource(definition);

        assertThat(source).contains("interface UnknownStageVisitorBuilder");
        assertThat(source).contains("interface Unknown_StageVisitorBuilder");
        assertThat(source.split("interface UnknownStageVisitorBuilder", -1)).hasSize(2);
        assertThat(source.split("interface Unknown_StageVisitorBuilder", -1)).hasSize(2);
    }

    private static EnumDefinition parseEnum(String yamlPath) {
        ConjureDefinition conjureDef = Conjure.parse(ConjureArgs.builder()
                .definitions(List.of(new File(yamlPath)))
                .safetyDeclarations(SafetyDeclarationRequirements.ALLOWED)
                .build());
        return conjureDef.getTypes().stream()
                .filter(typeDef -> typeDef.accept(TypeDefinitionVisitor.IS_ENUM))
                .map(typeDef -> typeDef.accept(TypeDefinitionVisitor.ENUM))
                .findFirst()
                .orElseThrow();
    }

    private static String generateSource(EnumDefinition definition) {
        JavaFile file = EnumGenerator.generateEnumType(definition, Options.builder().build());
        return file.toString();
    }
}
