package externalfallbacktypes.com.palantir.product.external;

import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.logsafe.Safe;
import java.util.List;
import java.util.Map;

@ConjureGenerated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface ServiceUsingExternalTypes {
    /** @apiNote {@code PUT /external/{path}} */
    Map<String, String> external(@Safe String path, @Safe List<String> body);
}
