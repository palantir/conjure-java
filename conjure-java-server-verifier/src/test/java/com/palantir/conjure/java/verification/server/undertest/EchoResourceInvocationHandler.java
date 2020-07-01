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

package com.palantir.conjure.java.verification.server.undertest;

import com.google.common.base.Preconditions;
import com.google.common.reflect.AbstractInvocationHandler;
import com.palantir.conjure.java.com.palantir.conjure.verification.client.AutoDeserializeService;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeRuntimeException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Simple {@link InvocationHandler} implementing all methods of {@link AutoDeserializeService} by returning the single
 * parameter.
 */
final class EchoResourceInvocationHandler extends AbstractInvocationHandler {

    @Override
    protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isDefault()) {
            return getMethodHandle(method).bindTo(proxy).invokeWithArguments(args);
        }
        Preconditions.checkArgument(args.length == 1, "Expected single argument. Method: %s", method);
        return com.palantir.logsafe.Preconditions.checkNotNull(args[0], "Null values are not allowed");
    }

    private static MethodHandle getMethodHandle(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        try {
            Constructor<MethodHandles.Lookup> constructor =
                    MethodHandles.Lookup.class.getDeclaredConstructor(Class.class);
            constructor.setAccessible(true);
            return constructor.newInstance(declaringClass).unreflectSpecial(method, declaringClass);
        } catch (ReflectiveOperationException e) {
            throw new SafeRuntimeException("Failed to find method handle", e, SafeArg.of("method", method));
        }
    }
}
