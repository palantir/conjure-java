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

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.SetMultimap;
import com.palantir.conjure.java.undertow.annotations.Handle;
import com.palantir.conjure.java.undertow.annotations.HttpMethod;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CollectionBodyParam {

    @Handle(method = HttpMethod.POST, path = "/collection")
    void collection(@Handle.Body Collection<String> values);

    @Handle(method = HttpMethod.POST, path = "/list")
    void list(@Handle.Body List<String> values);

    @Handle(method = HttpMethod.POST, path = "/set")
    void set(@Handle.Body Set<String> values);

    @Handle(method = HttpMethod.POST, path = "/map")
    void map(@Handle.Body Map<String, String> values);

    @Handle(method = HttpMethod.POST, path = "/multiset")
    void multiset(@Handle.Body Multiset<String> values);

    @Handle(method = HttpMethod.POST, path = "/multimap")
    void multimap(@Handle.Body Multimap<String, String> values);

    @Handle(method = HttpMethod.POST, path = "/listMultimap")
    void listMultimap(@Handle.Body ListMultimap<String, String> values);

    @Handle(method = HttpMethod.POST, path = "/setMultimap")
    void setMultimap(@Handle.Body SetMultimap<String, String> values);
}
