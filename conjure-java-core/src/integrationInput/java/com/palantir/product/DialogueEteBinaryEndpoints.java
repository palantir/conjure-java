package com.palantir.product;

import com.google.common.collect.ListMultimap;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.HttpMethod;
import com.palantir.dialogue.PathTemplate;
import com.palantir.dialogue.UrlBuilder;
import java.lang.Override;
import java.lang.String;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueEndpointsGenerator")
enum DialogueEteBinaryEndpoints implements Endpoint {
    postBinary {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("binary").build();

        @Override
        public void renderPath(ListMultimap<String, String> params, UrlBuilder url) {
            pathTemplate.fill(params, url);
        }

        @Override
        public HttpMethod httpMethod() {
            return HttpMethod.POST;
        }

        @Override
        public String serviceName() {
            return "EteBinaryService";
        }

        @Override
        public String endpointName() {
            return "postBinary";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    postBinaryThrows {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("binary").fixed("throws").build();

        @Override
        public void renderPath(ListMultimap<String, String> params, UrlBuilder url) {
            pathTemplate.fill(params, url);
        }

        @Override
        public HttpMethod httpMethod() {
            return HttpMethod.POST;
        }

        @Override
        public String serviceName() {
            return "EteBinaryService";
        }

        @Override
        public String endpointName() {
            return "postBinaryThrows";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    getOptionalBinaryPresent {
        private final PathTemplate pathTemplate = PathTemplate.builder()
                .fixed("binary")
                .fixed("optional")
                .fixed("present")
                .build();

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
            return "EteBinaryService";
        }

        @Override
        public String endpointName() {
            return "getOptionalBinaryPresent";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    getOptionalBinaryEmpty {
        private final PathTemplate pathTemplate = PathTemplate.builder()
                .fixed("binary")
                .fixed("optional")
                .fixed("empty")
                .build();

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
            return "EteBinaryService";
        }

        @Override
        public String endpointName() {
            return "getOptionalBinaryEmpty";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    /**
     * Throws an exception after partially writing a binary response.
     */
    getBinaryFailure {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("binary").fixed("failure").build();

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
            return "EteBinaryService";
        }

        @Override
        public String endpointName() {
            return "getBinaryFailure";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    getAliased {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("binary").fixed("aliased").build();

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
            return "EteBinaryService";
        }

        @Override
        public String endpointName() {
            return "getAliased";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    }
}
