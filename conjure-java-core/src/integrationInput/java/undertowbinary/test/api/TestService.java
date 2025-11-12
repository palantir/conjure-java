package undertowbinary.test.api;

import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;

@ConjureGenerated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface TestService {
    /** @apiNote {@code GET /} */
    BinaryResponseBody getBinary();
}
