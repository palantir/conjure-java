package com.company.product;

import com.company.product.RecipesErrors.NotFoundException;
import com.company.product.RecipesErrors.RecipeNotGoodException;
import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.Serializer;
import com.palantir.conjure.java.undertow.lib.TypeMarker;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import io.undertow.util.PathTemplateMatch;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class RecipeBookServiceEndpoints implements UndertowService {
    private final RecipeBookService delegate;

    private RecipeBookServiceEndpoints(RecipeBookService delegate) {
        this.delegate = delegate;
    }

    public static UndertowService of(RecipeBookService delegate) {
        return new RecipeBookServiceEndpoints(delegate);
    }

    @Override
    public List<Endpoint> endpoints(UndertowRuntime runtime) {
        return ImmutableList.of(new GetRecipeEndpoint(runtime, delegate));
    }

    private static final class GetRecipeEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final RecipeBookService delegate;

        private final Serializer<Recipe> serializer;

        GetRecipeEndpoint(UndertowRuntime runtime, RecipeBookService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<Recipe>() {}, this);
        }

        @Override
        public void handleRequest(HttpServerExchange exchange)
                throws IOException, NotFoundException, RecipeNotGoodException {
            Map<String, String> pathParams =
                    exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
            String recipeNameRaw = runtime.plainSerDe().deserializeString(pathParams.get("recipeName"));
            RecipeName recipeName = RecipeName.of(recipeNameRaw);
            Recipe result = delegate.getRecipe(recipeName);
            serializer.serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/recipes/{recipeName}";
        }

        @Override
        public String serviceName() {
            return "RecipeBookService";
        }

        @Override
        public String name() {
            return "getRecipe";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }
}
