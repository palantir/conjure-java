package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * A type which can either be a StringExample, a set of strings, or an integer.
 */
@JsonSubTypes({
    @JsonSubTypes.Type(StringExampleWrapper.class),
    @JsonSubTypes.Type(ThisFieldIsAnIntegerWrapper.class),
    @JsonSubTypes.Type(AlsoAnIntegerWrapper.class),
    @JsonSubTypes.Type(IfWrapper.class),
    @JsonSubTypes.Type(NewWrapper.class),
    @JsonSubTypes.Type(InterfaceWrapper.class),
    @JsonSubTypes.Type(CompletedWrapper.class),
    @JsonSubTypes.Type(Unknown_Wrapper.class),
    @JsonSubTypes.Type(OptionalWrapper.class),
    @JsonSubTypes.Type(ListWrapper.class),
    @JsonSubTypes.Type(SetWrapper.class),
    @JsonSubTypes.Type(MapWrapper.class),
    @JsonSubTypes.Type(OptionalAliasWrapper.class),
    @JsonSubTypes.Type(ListAliasWrapper.class),
    @JsonSubTypes.Type(SetAliasWrapper.class),
    @JsonSubTypes.Type(MapAliasWrapper.class)
})
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed interface UnionTypeExample {
    /**
     * Docs for when UnionTypeExample is of type StringExample.
     */
    static UnionTypeExample stringExample(StringExample value) {
        return new UnionTypeExample.StringExampleWrapper(value);
    }

    static UnionTypeExample thisFieldIsAnInteger(int value) {
        return new UnionTypeExample.ThisFieldIsAnIntegerWrapper(value);
    }

    static UnionTypeExample alsoAnInteger(int value) {
        return new UnionTypeExample.AlsoAnIntegerWrapper(value);
    }

    static UnionTypeExample if_(int value) {
        return new UnionTypeExample.IfWrapper(value);
    }

    static UnionTypeExample new_(int value) {
        return new UnionTypeExample.NewWrapper(value);
    }

    static UnionTypeExample interface_(int value) {
        return new UnionTypeExample.InterfaceWrapper(value);
    }

    static UnionTypeExample completed(int value) {
        return new UnionTypeExample.CompletedWrapper(value);
    }

    static UnionTypeExample unknown_(int value) {
        return new UnionTypeExample.Unknown_Wrapper(value);
    }

    static UnionTypeExample optional(Optional<String> value) {
        return new UnionTypeExample.OptionalWrapper(value);
    }

    static UnionTypeExample list(List<String> value) {
        return new UnionTypeExample.ListWrapper(value);
    }

    static UnionTypeExample set(Set<String> value) {
        return new UnionTypeExample.SetWrapper(value);
    }

    static UnionTypeExample map(Map<String, String> value) {
        return new UnionTypeExample.MapWrapper(value);
    }

    static UnionTypeExample optionalAlias(OptionalAlias value) {
        return new UnionTypeExample.OptionalAliasWrapper(value);
    }

    static UnionTypeExample listAlias(ListAlias value) {
        return new UnionTypeExample.ListAliasWrapper(value);
    }

    static UnionTypeExample setAlias(SetAlias value) {
        return new UnionTypeExample.SetAliasWrapper(value);
    }

    static UnionTypeExample mapAlias(MapAliasExample value) {
        return new UnionTypeExample.MapAliasWrapper(value);
    }

    static UnionTypeExample unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "stringExample":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: stringExample");
            case "thisFieldIsAnInteger":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: thisFieldIsAnInteger");
            case "alsoAnInteger":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: alsoAnInteger");
            case "if":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: if");
            case "new":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: new");
            case "interface":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: interface");
            case "completed":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: completed");
            case "unknown":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: unknown");
            case "optional":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: optional");
            case "list":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: list");
            case "set":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: set");
            case "map":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: map");
            case "optionalAlias":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: optionalAlias");
            case "listAlias":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: listAlias");
            case "setAlias":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: setAlias");
            case "mapAlias":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: mapAlias");
            default:
                return new UnionTypeExample(
                        new UnionTypeExample.UnknownWrapper(type, Collections.singletonMap(type, value)));
        }
    }

    <T> void accept(Visitor<T> visitor);

    interface Visitor<T> {
        /**
         * Docs for when UnionTypeExample is of type StringExample.
         */
        T visitStringExample(StringExample value);

        T visitThisFieldIsAnInteger(int value);

        T visitAlsoAnInteger(int value);

        T visitIf(int value);

        T visitNew(int value);

        T visitInterface(int value);

        T visitCompleted(int value);

        T visitUnknown_(int value);

        T visitOptional(Optional<String> value);

        T visitList(List<String> value);

        T visitSet(Set<String> value);

        T visitMap(Map<String, String> value);

        T visitOptionalAlias(OptionalAlias value);

        T visitListAlias(ListAlias value);

        T visitSetAlias(SetAlias value);

        T visitMapAlias(MapAliasExample value);

        T visitUnknown(@Safe String unknownType, Object unknownValue);

        static <T> UnionTypeExample.AlsoAnIntegerStageVisitorBuilder<T> builder() {
            return new UnionTypeExample.VisitorBuilder<T>();
        }
    }
}
