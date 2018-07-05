package com.palantir.conjure.verifier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class TestCasesYml {
    private final Map<EndpointName, List<ServiceTestStructure>> value;

    private TestCasesYml(Map<EndpointName, List<ServiceTestStructure>> value) {
        Objects.requireNonNull(value, "value cannot be null");
        this.value = value;
    }

    @JsonValue
    public Map<EndpointName, List<ServiceTestStructure>> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof TestCasesYml
                        && this.value.equals(((TestCasesYml) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator
    public static TestCasesYml of(Map<EndpointName, List<ServiceTestStructure>> value) {
        return new TestCasesYml(value);
    }
}
