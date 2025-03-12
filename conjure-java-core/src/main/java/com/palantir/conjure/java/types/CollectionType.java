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

package com.palantir.conjure.java.types;

import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.visitor.DefaultPrimitiveTypeVisitor;
import com.palantir.conjure.java.visitor.DefaultTypeVisitor;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.spec.Type;

final class CollectionType {
    private final ConjureCollectionType conjureCollectionType;

    private final ConjureCollectionNullHandlingMode nullHandlingMode;

    CollectionType(ConjureCollectionType conjureCollectionType, ConjureCollectionNullHandlingMode nullHandlingMode) {
        this.conjureCollectionType = conjureCollectionType;
        this.nullHandlingMode = nullHandlingMode;
    }

    ConjureCollectionType getConjureCollectionType() {
        return conjureCollectionType;
    }

    boolean useNonNullFactory() {
        return nullHandlingMode.shouldUseNonNullFactory();
    }

    String getConjureCollectionStaticFactoryMethod() {
        // Primitive optimized lists can only be used with the non-null flag enabled
        if (nullHandlingMode.shouldUseNonNullFactory()) {
            return "newNonNull" + conjureCollectionType.getCollectionName();
        }
        return "new" + conjureCollectionType.getCollectionName();
    }

    enum ConjureCollectionType {
        LIST("List", false),
        DOUBLE_LIST("DoubleList", true),
        INTEGER_LIST("IntegerList", true),
        // Eclipse has a BooleanList type, but this use case implies
        // bit mask and it doesn't serialize efficiently as a collection
        // so let's just use the "naive" boxed collection
        BOOLEAN_LIST("List", false),
        // SafeLong is unique in this list. While it is technically backed with a long
        // its logical limitations are captured in the boxed type SafeLong. Meaning,
        // you must either expose this "implementation detail" on the public API or
        // accept that you cannot optimize away the boxing. For now, given the focus
        // on doubles, let's delay this optimization and have it as a separate discussion.
        // Technically this type could be optimized at rest, but that would require a more
        // complex enum to represent this trinary. So for now this is disabled.
        SAFE_LONG_LIST("SafeLongList", false),
        SET("Set", false);

        private final String collectionName;
        private final Boolean primitiveCollection;

        ConjureCollectionType(String collectionName, boolean primitiveCollection) {
            this.collectionName = collectionName;
            this.primitiveCollection = primitiveCollection;
        }

        String getCollectionName() {
            return collectionName;
        }

        Boolean isPrimitiveCollection() {
            return primitiveCollection;
        }
    }

    enum ConjureCollectionNullHandlingMode {
        NON_NULL_COLLECTION_FACTORY(true),
        NULLABLE_COLLECTION_FACTORY(false);

        private final boolean useNonNullFactory;

        ConjureCollectionNullHandlingMode(boolean useNonNullFactory) {
            this.useNonNullFactory = useNonNullFactory;
        }

        boolean shouldUseNonNullFactory() {
            return useNonNullFactory;
        }
    }

    /**
     * Creates an instance of CollectionType.
     * @param type must be either a LIST or SET, other types are unsupported within CollectionType.
     */
    static CollectionType from(Type type, Options options) {
        return type.accept(new DefaultTypeVisitor<>() {
            @Override
            public CollectionType visitList(ListType value) {
                if (!options.nonNullCollections()) {
                    return new CollectionType(
                            ConjureCollectionType.LIST, ConjureCollectionNullHandlingMode.NULLABLE_COLLECTION_FACTORY);
                }

                return value.getItemType().accept(new DefaultTypeVisitor<>() {
                    @Override
                    public CollectionType visitDefault() {
                        return new CollectionType(
                                ConjureCollectionType.LIST,
                                ConjureCollectionNullHandlingMode.NON_NULL_COLLECTION_FACTORY);
                    }

                    @Override
                    public CollectionType visitPrimitive(PrimitiveType primitiveType) {
                        return primitiveType.accept(new DefaultPrimitiveTypeVisitor<>() {

                            @Override
                            public CollectionType visitDefault() {
                                return new CollectionType(
                                        ConjureCollectionType.LIST,
                                        ConjureCollectionNullHandlingMode.NON_NULL_COLLECTION_FACTORY);
                            }

                            @Override
                            public CollectionType visitDouble() {
                                return new CollectionType(
                                        ConjureCollectionType.DOUBLE_LIST,
                                        ConjureCollectionNullHandlingMode.NON_NULL_COLLECTION_FACTORY);
                            }

                            @Override
                            public CollectionType visitInteger() {
                                return new CollectionType(
                                        ConjureCollectionType.INTEGER_LIST,
                                        ConjureCollectionNullHandlingMode.NON_NULL_COLLECTION_FACTORY);
                            }

                            @Override
                            public CollectionType visitBoolean() {
                                return new CollectionType(
                                        ConjureCollectionType.BOOLEAN_LIST,
                                        ConjureCollectionNullHandlingMode.NON_NULL_COLLECTION_FACTORY);
                            }

                            @Override
                            public CollectionType visitSafelong() {
                                return new CollectionType(
                                        ConjureCollectionType.SAFE_LONG_LIST,
                                        ConjureCollectionNullHandlingMode.NON_NULL_COLLECTION_FACTORY);
                            }
                        });
                    }
                });
            }

            @Override
            public CollectionType visitSet(SetType _value) {
                if (options.nonNullCollections()) {
                    return new CollectionType(
                            ConjureCollectionType.SET, ConjureCollectionNullHandlingMode.NON_NULL_COLLECTION_FACTORY);
                } else {
                    return new CollectionType(
                            ConjureCollectionType.SET, ConjureCollectionNullHandlingMode.NULLABLE_COLLECTION_FACTORY);
                }
            }
        });
    }
}
