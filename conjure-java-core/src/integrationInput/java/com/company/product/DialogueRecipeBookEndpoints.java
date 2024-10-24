package com.company.product;

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
enum DialogueRecipeBookEndpoints implements Endpoint {
    getRecipe {
        private final PathTemplate pathTemplate =
                PathTemplate.builder().fixed("recipes").variable("recipeName").build();

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
            return "RecipeBookService";
        }

        @Override
        public String endpointName() {
            return "getRecipe";
        }

        @Override
        public String version() {
            return VERSION;
        }
    };

    private static final String VERSION = Optional.ofNullable(
                    DialogueRecipeBookEndpoints.class.getPackage().getImplementationVersion())
            .orElse("0.0.0");
}
