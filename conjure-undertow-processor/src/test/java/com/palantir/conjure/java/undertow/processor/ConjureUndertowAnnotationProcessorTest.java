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
import com.palantir.conjure.java.undertow.processor.sample.CookieParams;
import com.palantir.conjure.java.undertow.processor.sample.DefaultDecoderService;
import com.palantir.conjure.java.undertow.processor.sample.DeprecatedEndpointResource;
import com.palantir.conjure.java.undertow.processor.sample.DeprecatedResource;
import com.palantir.conjure.java.undertow.processor.sample.MultipleBodyInterface;
import com.palantir.conjure.java.undertow.processor.sample.NameClashContextParam;
import com.palantir.conjure.java.undertow.processor.sample.NameClashExchangeParam;
import com.palantir.conjure.java.undertow.processor.sample.OfFactory;
import com.palantir.conjure.java.undertow.processor.sample.OptionalPrimitives;
import com.palantir.conjure.java.undertow.processor.sample.OverloadedResource;
import com.palantir.conjure.java.undertow.processor.sample.ParameterNotAnnotated;
import com.palantir.conjure.java.undertow.processor.sample.PrimitiveBodyParam;
import com.palantir.conjure.java.undertow.processor.sample.PrimitiveQueryParams;
import com.palantir.conjure.java.undertow.processor.sample.PrivateMethodAnnotatedResource;
import com.palantir.conjure.java.undertow.processor.sample.ProtectedMethodAnnotatedResource;
import com.palantir.conjure.java.undertow.processor.sample.SafeLoggableAuthCookieParam;
import com.palantir.conjure.java.undertow.processor.sample.SafeLoggableAuthHeaderParam;
import com.palantir.conjure.java.undertow.processor.sample.SafeLoggableParams;
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
    public void testPrimitiveBodyParam() {
        assertTestFileCompileAndMatches(TEST_CLASSES_BASE_DIR, PrimitiveBodyParam.class);
    }

    @Test
    public void testMultipleBodies() {
        assertTestFileCompileAndMatches(TEST_CLASSES_BASE_DIR, MultipleBodyInterface.class);
    }

    @Test
    public void testOfFactoryMethod() {
        assertTestFileCompileAndMatches(TEST_CLASSES_BASE_DIR, OfFactory.class);
    }

    @Test
    public void testPrimitiveQueryParams() {
        assertTestFileCompileAndMatches(TEST_CLASSES_BASE_DIR, PrimitiveQueryParams.class);
    }

    @Test
    public void testDefaultDecoders() {
        assertTestFileCompileAndMatches(TEST_CLASSES_BASE_DIR, DefaultDecoderService.class);
    }

    @Test
    public void testCookieDecoders() {
        assertTestFileCompileAndMatches(TEST_CLASSES_BASE_DIR, CookieParams.class);
    }

    @Test
    public void testSafeLoggableParams() {
        assertTestFileCompileAndMatches(TEST_CLASSES_BASE_DIR, SafeLoggableParams.class);
    }

    @Test
    public void testDeprecatedEndpoint() {
        assertTestFileCompileAndMatches(TEST_CLASSES_BASE_DIR, DeprecatedEndpointResource.class);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testDeprecatedResource() {
        assertTestFileCompileAndMatches(TEST_CLASSES_BASE_DIR, DeprecatedResource.class);
    }

    @Test
    public void testOverloadedEndpoint() {
        assertTestFileCompileAndMatches(TEST_CLASSES_BASE_DIR, OverloadedResource.class);
    }

    @Test
    public void testQueryOptionals() {
        assertTestFileCompileAndMatches(TEST_CLASSES_BASE_DIR, OptionalPrimitives.class);
    }

    @Test
    public void testPackagePrivateInterface() {
        assertTestFileCompileAndMatches(TEST_CLASSES_BASE_DIR, PackagePrivateInterface.class);
    }

    @Test
    public void testSafeLoggingAuthCookie() {
        assertThat(compileTestClass(TEST_CLASSES_BASE_DIR, SafeLoggableAuthCookieParam.class))
                .hadErrorContaining("BearerToken parameter cannot be annotated with safe logging annotations");
    }

    @Test
    public void testSafeLoggingAuthHeader() {
        assertThat(compileTestClass(TEST_CLASSES_BASE_DIR, SafeLoggableAuthHeaderParam.class))
                .hadErrorContaining("Parameter type cannot be annotated with safe logging annotations");
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

    @Test
    public void testContextNameClash() {
        assertThat(compileTestClass(TEST_CLASSES_BASE_DIR, NameClashContextParam.class))
                .hadErrorContaining("incompatible types: java.lang.String cannot be converted to "
                        + "com.palantir.conjure.java.undertow.lib.RequestContext");
    }

    @Test
    public void testExchangeNameClash() {
        assertThat(compileTestClass(TEST_CLASSES_BASE_DIR, NameClashExchangeParam.class))
                .hadErrorContaining("variable exchange is already defined in method handleRequest");
    }

    @Test
    public void testBodyIsNotAnnotated() {
        assertThat(compileTestClass(TEST_CLASSES_BASE_DIR, ParameterNotAnnotated.class))
                .hadErrorContaining("At least one annotation should be present");
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
                    .withOptions("-source", "11", "-Werror", "-Xlint:deprecation")
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
