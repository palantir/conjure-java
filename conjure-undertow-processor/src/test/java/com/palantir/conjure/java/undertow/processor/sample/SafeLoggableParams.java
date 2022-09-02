/*
 * (c) Copyright 2022 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.undertow.processor.sample;

import com.palantir.conjure.java.undertow.annotations.Handle;
import com.palantir.conjure.java.undertow.annotations.HttpMethod;
import com.palantir.conjure.java.undertow.lib.RequestContext;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.Unsafe;

public interface SafeLoggableParams {

    @Handle(
            method = HttpMethod.GET,
            path = "/pathParams/{noAnnotationParam}/{safeParam}/{unsafeParam}/{safeMultiParam}")
    void pathParams(
            @Handle.PathParam String noAnnotationParam,
            @Safe @Handle.PathParam String safeParam,
            @Unsafe @Handle.PathParam String unsafeParam,
            @Safe @Handle.PathMultiParam String safeMultiParam);

    @Handle(method = HttpMethod.GET, path = "/queryParams")
    void queryParams(
            @Handle.QueryParam("noAnnotation") String noAnnotationParam,
            @Safe @Handle.QueryParam("safeParam") String safeParam,
            @Unsafe @Handle.QueryParam("unsafeParam") String unsafeParam);

    @Handle(method = HttpMethod.POST, path = "/formParams")
    void formParams(
            @Handle.FormParam("noAnnotation") String noAnnotationParam,
            @Safe @Handle.FormParam("safeParam") String safeParam,
            @Unsafe @Handle.FormParam("unsafeParam") String unsafeParam);

    @Handle(method = HttpMethod.GET, path = "/headerParams")
    void headerParams(
            @Handle.Header("noAnnotation") String noAnnotationParam,
            @Safe @Handle.Header("safeParam") String safeParam,
            @Unsafe @Handle.Header("unsafeParam") String unsafeParam);

    @Handle(method = HttpMethod.GET, path = "/cookieParams")
    void cookieParams(
            @Handle.Cookie("noAnnotation") String noAnnotationParam,
            @Safe @Handle.Cookie("safeParam") String safeParam,
            @Unsafe @Handle.Cookie("unsafeParam") String unsafeParam);

    @Handle(method = HttpMethod.GET, path = "/safeLoggingReusesContext")
    String safeLoggingReusesContext(
            @Safe @Handle.QueryParam("safeParam") String safeParam,
            @Safe @Handle.QueryParam("context") String context,
            RequestContext requestContext);

    @Handle(method = HttpMethod.GET, path = "/safeLoggingBody")
    String bodyParam(@Safe @Handle.Body String body);
}
