package com.palantir.product;

import com.google.common.collect.ListMultimap;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.HttpMethod;
import com.palantir.dialogue.PathTemplate;
import com.palantir.dialogue.UrlBuilder;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueEndpointsGenerator")
enum DialogueEmptyPathEndpoints implements Endpoint {
    emptyPath {
        private final PathTemplate pathTemplate = PathTemplate.builder().build();

        @Override
        public void renderPath(ListMultimap<String, String> params, UrlBuilder url) {
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
            return "1.2.3";
        }
    }
}
