package com.palantir.product;

import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.HttpMethod;
import com.palantir.dialogue.PathTemplate;
import com.palantir.dialogue.UrlBuilder;
import java.lang.Override;
import java.lang.String;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueEndpointsGenerator")
enum DialogueEmptyPathEndpoints implements Endpoint {
    emptyPath {
        private final PathTemplate pathTemplate = PathTemplate.builder().build();

        @Override
        public void renderPath(Map<String, String> params, UrlBuilder url) {
            pathTemplate.fill(params, url);
        }

        @Override
        public HttpMethod httpMethod() {
            return HttpMethod.GET;
        }

        @Override
        public String serviceName() {
            return "EmptyPathService";
        }

        @Override
        public String endpointName() {
            return "emptyPath";
        }

        @Override
        public String version() {
            return packageVersion;
        }
    };

    private static final String packageVersion = Optional.ofNullable(
                    DialogueEmptyPathEndpoints.class.getPackage().getImplementationVersion())
            .orElse("0.0.0");
    ;
}
