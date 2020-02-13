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

package com.palantir.conjure.java;

import static com.palantir.conjure.java.EteTestServer.clientConfiguration;
import static com.palantir.conjure.java.EteTestServer.clientUserAgent;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.java.dialogue.serde.DefaultConjureRuntime;
import com.palantir.conjure.java.services.dialogue.DialogueServiceGenerator;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.JavaChannels;
import com.palantir.product.BlockingEteBinaryService;
import com.palantir.product.DialogueEteBinaryService;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tritium.metrics.registry.DefaultTaggedMetricRegistry;
import io.dropwizard.Configuration;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.ResourceLock;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(DropwizardExtensionsSupport.class)
@ResourceLock("port:8080")
public final class DialogueClientEteTest extends TestBase {
    private static final DefaultConjureRuntime DEFAULT_CONJURE_RUNTIME =
            DefaultConjureRuntime.builder().build();

    // TODO(forozco): use dialogue
    @TempDir
    public static File folder;

    public static final DropwizardAppExtension<Configuration> RULE = new DropwizardAppExtension<>(EteTestServer.class);

    private final BlockingEteBinaryService binary;

    public DialogueClientEteTest() {
        Channel channel =
                JavaChannels.create(clientConfiguration(), clientUserAgent(), new DefaultTaggedMetricRegistry());
        binary = DialogueEteBinaryService.blocking(channel, DEFAULT_CONJURE_RUNTIME);
    }

    @SuppressWarnings("ByteBufferBackingArray")
    @Test
    @Disabled("Support optional binary in a sane way")
    public void test_optionalBinary_present() {
        Optional<ByteBuffer> response = binary.getOptionalBinaryPresent(AuthHeader.valueOf("authHeader"));
        assertThat(response).hasValueSatisfying(value -> {
            assertThat(new String(value.array(), StandardCharsets.UTF_8)).isEqualTo("Hello World!");
        });
    }

    @Test
    @Disabled("Support optional binary in a sane way")
    public void test_optionalBinary_empty() {
        Optional<ByteBuffer> response = binary.getOptionalBinaryEmpty(AuthHeader.valueOf("authHeader"));
        assertThat(response).isEmpty();
    }

    @BeforeAll
    public static void beforeAll() throws IOException {
        ConjureDefinition def = Conjure.parse(ImmutableList.of(
                // TODO(forozco): uncomment once we support everything
                // new File("src/test/resources/ete-service.yml"),
                new File("src/test/resources/ete-binary.yml")));
        List<Path> files = new DialogueServiceGenerator(Options.empty(), "").emit(def, folder);
        validateGeneratorOutput(files, Paths.get("src/integrationInput/java/com/palantir/product"));
    }
}
