package com.palantir.product;

import com.google.common.collect.ListMultimap;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.HttpMethod;
import com.palantir.dialogue.PathTemplate;
import com.palantir.dialogue.UrlBuilder;
import java.lang.Override;
import java.lang.String;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueEndpointsGenerator")
enum DialogueErrorEndpoints implements Endpoint {
    testBasicError {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("basic").build();

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
            return "ErrorService";
        }

        @Override
        public String endpointName() {
            return "testBasicError";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    testImportedError {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("imported").build();

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
            return "ErrorService";
        }

        @Override
        public String endpointName() {
            return "testImportedError";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    testMultipleErrorsAndPackages {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("multiple").build();

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
            return "ErrorService";
        }

        @Override
        public String endpointName() {
            return "testMultipleErrorsAndPackages";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    testEmptyBody {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("empty").build();

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
            return "ErrorService";
        }

        @Override
        public String endpointName() {
            return "testEmptyBody";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    testBinary {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("binary").build();

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
            return "ErrorService";
        }

        @Override
        public String endpointName() {
            return "testBinary";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    testOptionalBinary {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("optional-binary").build();

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
            return "ErrorService";
        }

        @Override
        public String endpointName() {
            return "testOptionalBinary";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    }
}
