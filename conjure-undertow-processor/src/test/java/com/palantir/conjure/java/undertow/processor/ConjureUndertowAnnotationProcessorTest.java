/*
 * (c) Copyright 2021 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.undertow.processor;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.io.ByteStreams;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import com.palantir.conjure.java.undertow.processor.sample.PrimitiveQueryParams;
import com.palantir.conjure.java.undertow.processor.sample.PrivateMethodAnnotatedResource;
import com.palantir.conjure.java.undertow.processor.sample.ProtectedMethodAnnotatedResource;
import com.palantir.conjure.java.undertow.processor.sample.SimpleInterface;
import com.palantir.conjure.java.undertow.processor.sample.StaticMethodAnnotatedResource;
import com.palantir.logsafe.exceptions.SafeRuntimeException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import org.junit.jupiter.api.Test;

public class ConjureUndertowAnnotationProcessorTest {

    private static final boolean DEV_MODE = Boolean.valueOf(System.getProperty("recreate", "false"));
    private static final Path TEST_CLASSES_BASE_DIR = Paths.get("src", "test", "java");
    private static final Path RESOURCES_BASE_DIR = Paths.get("src", "test", "resources");

    @Test
    public void testExampleFileCompiles() {
        assertTestFileCompileAndMatches(TEST_CLASSES_BASE_DIR, SimpleInterface.class);
    }

    @Test
    public void testPrimitiveQueryParams() {
        assertTestFileCompileAndMatches(TEST_CLASSES_BASE_DIR, PrimitiveQueryParams.class);
    }

    @Test
    public void testConcreteClassWithPrivateAnnotatedMethod() {
        assertThat(compileTestClass(TEST_CLASSES_BASE_DIR, PrivateMethodAnnotatedResource.class))
                .hadErrorContaining("must be accessible to classes in the same package");
    }

    @Test
    public void testConcreteClassWithProtectedAnnotatedMethod() {
        assertThat(compileTestClass(TEST_CLASSES_BASE_DIR, ProtectedMethodAnnotatedResource.class))
                .hadErrorContaining("must be accessible to classes in the same package");
    }

    @Test
    public void testStaticAnnotatedMethod() {
        assertThat(compileTestClass(TEST_CLASSES_BASE_DIR, StaticMethodAnnotatedResource.class))
                .hadErrorContaining("The '@Handle' annotation does not support static methods");
    }

    private void assertTestFileCompileAndMatches(Path basePath, Class<?> clazz) {
        Compilation compilation = compileTestClass(basePath, clazz);
        assertThat(compilation).succeededWithoutWarnings();
        String generatedClassName = clazz.getSimpleName() + "Endpoints";
        String generatedFqnClassName = clazz.getPackage().getName() + "." + generatedClassName;
        String generatedClassFileRelativePath = generatedFqnClassName.replaceAll("\\.", "/") + ".java";
        assertThat(compilation.generatedFile(StandardLocation.SOURCE_OUTPUT, generatedClassFileRelativePath))
                .hasValueSatisfying(
                        javaFileObject -> assertContentsMatch(javaFileObject, generatedClassFileRelativePath));
    }

    private Compilation compileTestClass(Path basePath, Class<?> clazz) {
        Path clazzPath = basePath.resolve(Paths.get(
                Joiner.on("/").join(Splitter.on(".").split(clazz.getPackage().getName())),
                clazz.getSimpleName() + ".java"));
        try {
            return Compiler.javac()
                    // This is required because this tool does not know about our gradle setting.
                    .withOptions("-source", "11")
                    .withProcessors(new ConjureUndertowAnnotationProcessor())
                    .compile(JavaFileObjects.forResource(clazzPath.toUri().toURL()));
        } catch (MalformedURLException e) {
            throw new SafeRuntimeException(e);
        }
    }

    private void assertContentsMatch(JavaFileObject javaFileObject, String generatedClassFile) {
        try {
            Path output = RESOURCES_BASE_DIR.resolve(generatedClassFile + ".generated");
            String generatedContents = readJavaFileObject(javaFileObject);
            if (DEV_MODE) {
                Files.deleteIfExists(output);
                Files.createDirectories(output.getParent());
                Files.write(output, generatedContents.getBytes(StandardCharsets.UTF_8));
            }
            assertThat(generatedContents).isEqualTo(Files.readString(output));
        } catch (IOException e) {
            throw new SafeRuntimeException(e);
        }
    }

    private String readJavaFileObject(JavaFileObject javaFileObject) throws IOException {
        try (InputStream stream = javaFileObject.openInputStream()) {
            return new String(ByteStreams.toByteArray(stream), StandardCharsets.UTF_8);
        }
    }
}
