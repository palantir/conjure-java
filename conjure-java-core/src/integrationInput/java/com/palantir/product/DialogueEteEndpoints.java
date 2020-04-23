package com.palantir.product;

import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.HttpMethod;
import com.palantir.dialogue.PathTemplate;
import com.palantir.dialogue.UrlBuilder;
import java.lang.Override;
import java.lang.String;
import java.util.Map;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueEndpointsGenerator")
enum DialogueEteEndpoints implements Endpoint {
    /**
     * foo bar baz.
     * <h2>Very Important Documentation</h2>
     * <p>This documentation provides a <em>list</em>:</p>
     * <ul>
     * <li>Docs rule</li>
     * <li>Lists are wonderful</li>
     * </ul>
     */
    string {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("string").build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "string";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    /**
     * one <em>two</em> three.
     */
    integer {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("integer").build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "integer";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    double_ {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("double").build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "double_";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    boolean_ {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("boolean").build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "boolean_";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    safelong {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("safelong").build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "safelong";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    rid {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("rid").build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "rid";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    bearertoken {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("bearertoken").build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "bearertoken";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    optionalString {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("optionalString").build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "optionalString";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    optionalEmpty {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("optionalEmpty").build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "optionalEmpty";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    datetime {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("datetime").build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "datetime";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    binary {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("binary").build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "binary";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    /**
     * Path endpoint.
     */
    path {
        private final PathTemplate pathTemplate = PathTemplate.builder()
                .fixed("base")
                .fixed("path")
                .variable("param")
                .build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "path";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    externalLongPath {
        private final PathTemplate pathTemplate = PathTemplate.builder()
                .fixed("base")
                .fixed("externalLong")
                .variable("param")
                .build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "externalLongPath";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    optionalExternalLongQuery {
        private final PathTemplate pathTemplate = PathTemplate.builder()
                .fixed("base")
                .fixed("optionalExternalLong")
                .build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "optionalExternalLongQuery";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    notNullBody {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("notNullBody").build();

        @Override
        public void renderPath(Map<String, String> params, UrlBuilder url) {
            pathTemplate.fill(params, url);
        }

        @Override
        public HttpMethod httpMethod() {
            return HttpMethod.POST;
        }

        @Override
        public String serviceName() {
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "notNullBody";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    aliasOne {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("aliasOne").build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "aliasOne";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    optionalAliasOne {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("optionalAliasOne").build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "optionalAliasOne";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    aliasTwo {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("aliasTwo").build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "aliasTwo";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    notNullBodyExternalImport {
        private final PathTemplate pathTemplate = PathTemplate.builder()
                .fixed("base")
                .fixed("external")
                .fixed("notNullBody")
                .build();

        @Override
        public void renderPath(Map<String, String> params, UrlBuilder url) {
            pathTemplate.fill(params, url);
        }

        @Override
        public HttpMethod httpMethod() {
            return HttpMethod.POST;
        }

        @Override
        public String serviceName() {
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "notNullBodyExternalImport";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    optionalBodyExternalImport {
        private final PathTemplate pathTemplate = PathTemplate.builder()
                .fixed("base")
                .fixed("external")
                .fixed("optional-body")
                .build();

        @Override
        public void renderPath(Map<String, String> params, UrlBuilder url) {
            pathTemplate.fill(params, url);
        }

        @Override
        public HttpMethod httpMethod() {
            return HttpMethod.POST;
        }

        @Override
        public String serviceName() {
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "optionalBodyExternalImport";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    optionalQueryExternalImport {
        private final PathTemplate pathTemplate = PathTemplate.builder()
                .fixed("base")
                .fixed("external")
                .fixed("optional-query")
                .build();

        @Override
        public void renderPath(Map<String, String> params, UrlBuilder url) {
            pathTemplate.fill(params, url);
        }

        @Override
        public HttpMethod httpMethod() {
            return HttpMethod.POST;
        }

        @Override
        public String serviceName() {
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "optionalQueryExternalImport";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    noReturn {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("no-return").build();

        @Override
        public void renderPath(Map<String, String> params, UrlBuilder url) {
            pathTemplate.fill(params, url);
        }

        @Override
        public HttpMethod httpMethod() {
            return HttpMethod.POST;
        }

        @Override
        public String serviceName() {
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "noReturn";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    enumQuery {
        private final PathTemplate pathTemplate = PathTemplate.builder()
                .fixed("base")
                .fixed("enum")
                .fixed("query")
                .build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "enumQuery";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    enumListQuery {
        private final PathTemplate pathTemplate = PathTemplate.builder()
                .fixed("base")
                .fixed("enum")
                .fixed("list")
                .fixed("query")
                .build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "enumListQuery";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    optionalEnumQuery {
        private final PathTemplate pathTemplate = PathTemplate.builder()
                .fixed("base")
                .fixed("enum")
                .fixed("optional")
                .fixed("query")
                .build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "optionalEnumQuery";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    enumHeader {
        private final PathTemplate pathTemplate = PathTemplate.builder()
                .fixed("base")
                .fixed("enum")
                .fixed("header")
                .build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "enumHeader";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    aliasLongEndpoint {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("base").fixed("alias-long").build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "aliasLongEndpoint";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    },

    complexQueryParameters {
        private final PathTemplate pathTemplate = PathTemplate.builder()
                .fixed("base")
                .fixed("datasets")
                .variable("datasetRid")
                .fixed("strings")
                .build();

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
            return "EteService";
        }

        @Override
        public String endpointName() {
            return "complexQueryParameters";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    }
}
