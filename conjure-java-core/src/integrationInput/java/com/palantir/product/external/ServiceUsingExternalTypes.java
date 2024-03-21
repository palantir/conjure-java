package com.palantir.product.external;

import com.palantir.logsafe.Safe;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface ServiceUsingExternalTypes {
    /** @apiNote {@code PUT /external/{path}} */
    Map<String, String> external(@Safe String path, @Safe List<String> body);
}
