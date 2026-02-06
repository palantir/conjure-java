package sealedunions.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.palantir.logsafe.Safe;
import javax.annotation.processing.Generated;

@Safe
@JsonSerialize
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class EmptyObject {
    private static final EmptyObject INSTANCE = new EmptyObject();

    private EmptyObject() {}

    @Override
    @Safe
    public String toString() {
        return "EmptyObject{}";
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public static EmptyObject of() {
        return INSTANCE;
    }
}
