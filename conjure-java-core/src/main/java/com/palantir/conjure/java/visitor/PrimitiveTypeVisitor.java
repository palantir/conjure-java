/*
 * (c) Copyright 2024 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.visitor;

public final class PrimitiveTypeVisitor {
    private PrimitiveTypeVisitor() {}

    public static final IsStringVisitor IS_STRING = new IsStringVisitor();
    public static final IsIntegerVisitor IS_INTEGER = new IsIntegerVisitor();
    public static final IsDoubleVisitor IS_DOUBLE = new IsDoubleVisitor();
    public static final IsSafeLongVisitor IS_SAFELONG = new IsSafeLongVisitor();
    public static final IsBooleanVisitor IS_BOOLEAN = new IsBooleanVisitor();
    public static final IsUuidVisitor IS_UUID = new IsUuidVisitor();
    public static final IsRidVisitor IS_RID = new IsRidVisitor();

    private static final class IsStringVisitor extends IsPrimitiveTypeVisitor {
        @Override
        public Boolean visitString() {
            return true;
        }
    }

    private static final class IsIntegerVisitor extends IsPrimitiveTypeVisitor {
        @Override
        public Boolean visitInteger() {
            return true;
        }
    }

    private static final class IsDoubleVisitor extends IsPrimitiveTypeVisitor {
        @Override
        public Boolean visitDouble() {
            return true;
        }
    }

    private static final class IsSafeLongVisitor extends IsPrimitiveTypeVisitor {
        @Override
        public Boolean visitSafelong() {
            return true;
        }
    }

    private static final class IsBooleanVisitor extends IsPrimitiveTypeVisitor {
        @Override
        public Boolean visitBoolean() {
            return true;
        }
    }

    private static final class IsUuidVisitor extends IsPrimitiveTypeVisitor {
        @Override
        public Boolean visitUuid() {
            return true;
        }
    }

    private static final class IsRidVisitor extends IsPrimitiveTypeVisitor {
        @Override
        public Boolean visitRid() {
            return true;
        }
    }

    private static class IsPrimitiveTypeVisitor extends DefaultPrimitiveTypeVisitor<Boolean> {
        @Override
        public Boolean visitDefault() {
            return false;
        }
    }
}
