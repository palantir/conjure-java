package com.palantir.product.external;

import com.google.common.collect.ListMultimap;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.HttpMethod;
import com.palantir.dialogue.PathTemplate;
import com.palantir.dialogue.UrlBuilder;
import java.lang.Override;
import java.lang.String;
import java.util.Optional;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueEndpointsGenerator")
enum DialogueServiceUsingExternalTypesEndpoints implements Endpoint {
    external {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("external").variable("path").build();

        @Override
        public void renderPath(ListMultimap<String, String> params, UrlBuilder url) {
            pathTemplate.fill(params, url);
        }

        @Override
        public HttpMethod httpMethod() {
            return HttpMethod.PUT;
        }

        @Override
        public String serviceName() {
            return "ServiceUsingExternalTypes";
        }

        @Override
        public String endpointName() {
            return "external";
        }

        @Override
        public String version() {
            return VERSION;
        }
    };

    private static final String VERSION = Optional.ofNullable(DialogueServiceUsingExternalTypesEndpoints.class
                    .getPackage()
                    .getImplementationVersion())
            .orElse("0.0.0");
}
