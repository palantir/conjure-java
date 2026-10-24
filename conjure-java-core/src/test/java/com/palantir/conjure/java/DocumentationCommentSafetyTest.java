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

package com.palantir.conjure.java;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.MoreExecutors;
import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.defs.ConjureArgs;
import com.palantir.conjure.defs.SafetyDeclarationRequirements;
import com.palantir.conjure.java.services.dialogue.DialogueServiceGenerator;
import com.palantir.conjure.java.types.ObjectGenerator;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.io.File;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public final class DocumentationCommentSafetyTest {

    @TempDir
    public File folder;

    @Test
    public void rejectsDocsThatBreakOutOfTheGeneratedJavadocComment() throws Exception {
        // example-docs.yml embeds "*/" in service and endpoint docs; without the guard this produced
        // attacker-controlled Java compiled into the generated sources.
        ConjureDefinition conjure = Conjure.parse(ConjureArgs.builder()
                .addDefinitions(new File("src/test/resources/example-docs.yml"))
                .safetyDeclarations(SafetyDeclarationRequirements.ALLOWED)
                .build());
        File src = Files.createDirectory(folder.toPath().resolve("out")).toFile();
        Options options = Options.empty();

        assertThatThrownBy(() -> new GenerationCoordinator(
                                MoreExecutors.directExecutor(),
                                ImmutableSet.of(new DialogueServiceGenerator(options), new ObjectGenerator(options)))
                        .emit(conjure, src))
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasMessageContaining("may not contain the block-comment terminator");
    }
}
