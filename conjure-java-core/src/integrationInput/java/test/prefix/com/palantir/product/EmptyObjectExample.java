package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.palantir.logsafe.Safe;
import javax.annotation.Generated;

@Safe
@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class EmptyObjectExample {
    private static final EmptyObjectExample INSTANCE = new EmptyObjectExample();

    private EmptyObjectExample() {}

    @Override
    public String toString() {
        return "EmptyObjectExample{}";
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static EmptyObjectExample of() {
        return INSTANCE;
    }
}
