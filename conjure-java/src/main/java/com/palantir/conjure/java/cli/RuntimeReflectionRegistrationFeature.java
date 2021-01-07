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
package com.palantir.conjure.java.cli;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.graalvm.nativeimage.Platform;
import org.graalvm.nativeimage.Platforms;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;

@SuppressWarnings({"CatchAndPrintStackTrace", "JdkObsolete"})
@Platforms(Platform.HOSTED_ONLY.class)
final class RuntimeReflectionRegistrationFeature implements Feature {

    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        // Common standard library classes
        registerClassForReflection(String.class);
        registerClassForReflection(HashSet.class);
        registerClassForReflection(LinkedHashSet.class);
        registerClassForReflection(HashMap.class);
        registerClassForReflection(LinkedHashMap.class);
        registerClassForReflection(ArrayList.class);
        registerClassForReflection(LinkedList.class);
        registerClassForReflection(ConcurrentHashMap.class);
        try {
            registerClassForReflection(com.fasterxml.jackson.databind.ext.Java7Handlers.class);
            registerClassForReflection(com.fasterxml.jackson.databind.ext.Java7HandlersImpl.class);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        for (Path jarPath : access.getApplicationClassPath()) {
            String jarFileName = jarPath.getFileName().toString();
            if (!jarFileName.endsWith(".jar")) {
                // Only scan jars
                continue;
            }
            try (JarFile jar = new JarFile(jarPath.toFile())) {
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (name.endsWith(".class")
                            // Exclude multirelease files, the default jvm version class name is sufficient.
                            && !name.startsWith("META-INF")
                            // Exclude graal components
                            && !name.contains("com/oracle/svm")
                            && !name.contains("$")) {
                        String className = name.replace("/", ".").substring(0, name.length() - 6);
                        try {
                            maybeRegisterClassForReflection(access.findClassByName(className));
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            } catch (IOException | RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    private static void maybeRegisterClassForReflection(Class<?> clazz) {
        if (clazz != null) {
            try {
                if (isAnyElementAnnotated(
                        clazz, GraalAnnotationFilter.INSTANCE.negate().and(RuntimeAnnotationFilter.INSTANCE), false)) {
                    registerClassForReflection(clazz);
                }
            } catch (NoClassDefFoundError e) {
                System.err.printf("NoClassDefFoundError: %s While inspecting %s\n", e.getMessage(), clazz.getName());
            } catch (Throwable t) {
                t.printStackTrace();
                // ignored
            }
        }
    }

    private static void registerClassForReflection(Class<?> clazz) {
        try {
            Method[] declaredMethods = clazz.getDeclaredMethods();
            Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
            Field[] declaredFields = clazz.getDeclaredFields();
            Class<?>[] declaredClasses = clazz.getDeclaredClasses();
            RuntimeReflection.register(clazz);
            RuntimeReflection.register(declaredMethods);
            RuntimeReflection.register(declaredConstructors);
            RuntimeReflection.register(declaredFields);
            for (Class<?> declaredClass : declaredClasses) {
                registerClassForReflection(declaredClass);
            }
        } catch (NoClassDefFoundError e) {
            System.err.printf("NoClassDefFoundError: %s While registering %s\n", e.getMessage(), clazz.getName());
        } catch (Throwable t) {
            t.printStackTrace();
            // ignored
        }
    }

    private static boolean isAnyElementAnnotated(Class<?> clazz, Predicate<Annotation> filter, boolean onFailure) {
        try {
            boolean result = isAnnotated(clazz, filter)
                    || isAnyAnnotated(clazz.getDeclaredMethods(), filter)
                    || isAnyAnnotated(clazz.getDeclaredConstructors(), filter)
                    || isAnyAnnotated(clazz.getDeclaredFields(), filter);
            if (!result) {
                for (Class<?> enclosed : clazz.getDeclaredClasses()) {
                    if (isAnyElementAnnotated(enclosed, filter, onFailure)) {
                        return true;
                    }
                }
            }
            return result;
        } catch (Throwable t) {
            return onFailure;
        }
    }

    private static boolean isAnyAnnotated(AnnotatedElement[] elements, Predicate<Annotation> filter) {
        for (AnnotatedElement element : elements) {
            if (isAnnotated(element, filter)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAnnotated(AnnotatedElement element, Predicate<Annotation> filter) {
        for (Annotation annotation : element.getAnnotations()) {
            if (filter.test(annotation)) {
                return true;
            }
        }
        return false;
    }

    private enum RuntimeAnnotationFilter implements Predicate<Annotation> {
        INSTANCE;

        @Override
        public boolean test(Annotation annotation) {
            // javax nullness. Are these used for validation at runtime?
            if (annotation instanceof Nullable
                    || annotation instanceof ParametersAreNonnullByDefault
                    || annotation instanceof Nonnull
                    || annotation instanceof CheckReturnValue) {
                // Deprecation annotations are common and provide little signal.
                return false;
            }
            String name = annotation.annotationType().getName();
            // Errorprone annotations are used at compile time, not runtime
            boolean excluded = name.startsWith("com.google.")
                    || name.startsWith("org.checkerframework.")
                    // Deprecated, Documented, Target, Retention, FunctionalInterface,
                    // SafeVarargs, Inherited, Repeatable
                    || name.startsWith("java.lang.")
                    || name.startsWith("org.openjdk.tools.")
                    || name.startsWith("org.openjdk.javax.annotation.processing.");
            return !excluded;
        }
    }

    private enum GraalAnnotationFilter implements Predicate<Annotation> {
        INSTANCE;

        @Override
        public boolean test(Annotation annotation) {
            String annotationName = annotation.annotationType().getName();
            return annotationName.startsWith("com.oracle.svm.") || annotationName.startsWith("org.graalvm.");
        }
    }
}
