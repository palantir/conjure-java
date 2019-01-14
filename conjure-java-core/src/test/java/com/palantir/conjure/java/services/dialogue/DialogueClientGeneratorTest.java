/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.services.dialogue;

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.java.TestBase;
import com.palantir.conjure.spec.ConjureDefinition;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public final class DialogueClientGeneratorTest extends TestBase {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testDialogueSampleServiceCodeGen() throws IOException {
        assertGeneratedOutputIsAsExpected("dialogue-sample-service");
    }

    private void assertGeneratedOutputIsAsExpected(String conjureFile) throws IOException {
        ConjureDefinition def = Conjure.parse(
                ImmutableList.of(new File("src/test/resources/" + conjureFile + ".yml")));
        List<Path> files = new DialogueClientGenerator().emit(def, folder.getRoot());
        validateGeneratorOutput(files, Paths.get("src/test/resources/test/api"), ".dialogue");
    }
}