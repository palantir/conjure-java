package strictfalse.com.palantir.strict;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.logsafe.Safe;

@Safe
@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
@ConjureGenerated("com.palantir.conjure.java.types.BeanGenerator")
public final class EmptyObjectNotStrict {
    private static final EmptyObjectNotStrict INSTANCE = new EmptyObjectNotStrict();

    private EmptyObjectNotStrict() {}

    @Override
    @Safe
    public String toString() {
        return "EmptyObjectNotStrict{}";
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public static EmptyObjectNotStrict of() {
        return INSTANCE;
    }
}
