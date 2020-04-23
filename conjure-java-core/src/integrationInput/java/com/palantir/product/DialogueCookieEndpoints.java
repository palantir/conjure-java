package test.api;

import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.HttpMethod;
import com.palantir.dialogue.PathTemplate;
import com.palantir.dialogue.UrlBuilder;
import java.lang.Override;
import java.lang.String;
import java.util.Map;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueEndpointsGenerator")
enum DialogueCookieEndpoints implements Endpoint {
    eatCookies {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("cookies").build();

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
            return "CookieService";
        }

        @Override
        public String endpointName() {
            return "eatCookies";
        }

        @Override
        public String version() {
            return "1.2.3";
        }
    }
}
