package dialogue.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.logsafe.Safe;

@Safe
@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
@ConjureGenerated("com.palantir.conjure.java.types.BeanGenerator")
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
