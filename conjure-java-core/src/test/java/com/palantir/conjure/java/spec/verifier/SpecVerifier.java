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

package com.palantir.conjure.java.spec.verifier;


/**
 * This is a stateful verifier which will validate a request and then validate the response;
 */
public final class SpecVerifier {


    public void verifyResponseJsonIsOk(String json) {
        // TODO send JSON to the server & check whether it is still alive?
        System.out.println("verifyResponseJsonIsOk:" + json);
    }

    public void notifyResponseDeserializedFailed() {
        // TODO send this back to the master spec verifier
    }
}
