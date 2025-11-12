package endpointerrors.com.palantir.product;

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
enum DialogueErrorEndpoints implements Endpoint {
    testBasicError {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("errors").fixed("basic").build();

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
            return "ErrorService";
        }

        @Override
        public String endpointName() {
            return "testBasicError";
        }

        @Override
        public String version() {
            return VERSION;
        }
    },

    testImportedError {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("errors").fixed("imported").build();

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
            return "ErrorService";
        }

        @Override
        public String endpointName() {
            return "testImportedError";
        }

        @Override
        public String version() {
            return VERSION;
        }
    },

    testMultipleErrorsAndPackages {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("errors").fixed("multiple").build();

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
            return "ErrorService";
        }

        @Override
        public String endpointName() {
            return "testMultipleErrorsAndPackages";
        }

        @Override
        public String version() {
            return VERSION;
        }
    },

    testEmptyBody {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("errors").fixed("empty").build();

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
            return "ErrorService";
        }

        @Override
        public String endpointName() {
            return "testEmptyBody";
        }

        @Override
        public String version() {
            return VERSION;
        }
    },

    testBinary {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("errors").fixed("binary").build();

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
            return "ErrorService";
        }

        @Override
        public String endpointName() {
            return "testBinary";
        }

        @Override
        public String version() {
            return VERSION;
        }
    },

    testOptionalBinary {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("errors").fixed("optional-binary").build();

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
            return "ErrorService";
        }

        @Override
        public String endpointName() {
            return "testOptionalBinary";
        }

        @Override
        public String version() {
            return VERSION;
        }
    };

    private static final String VERSION = Optional.ofNullable(
                    DialogueErrorEndpoints.class.getPackage().getImplementationVersion())
            .orElse("0.0.0");
}
