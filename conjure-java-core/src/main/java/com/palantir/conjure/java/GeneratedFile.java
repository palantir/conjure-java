/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
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

import com.palantir.conjure.java.GeneratedFile.GeneratedJavaFile;
import com.palantir.conjure.java.GeneratedFile.GeneratedReachabilityMetadataFile;
import com.palantir.conjure.java.types.ReachabilityMetadata;
import com.palantir.javapoet.JavaFile;

public sealed interface GeneratedFile permits GeneratedJavaFile, GeneratedReachabilityMetadataFile {
    record GeneratedJavaFile(JavaFile javaFile) implements GeneratedFile {
        public static GeneratedJavaFile of(JavaFile javaFile) {
            return new GeneratedJavaFile(javaFile);
        }
    }

    record GeneratedReachabilityMetadataFile(String packageName, ReachabilityMetadata reachabilityMetadata)
            implements GeneratedFile {}
}
