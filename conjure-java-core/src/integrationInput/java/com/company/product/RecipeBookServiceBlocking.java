package com.company.product;

import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.lib.internal.ClientEndpoint;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.Deserializer;
import com.palantir.dialogue.DialogueService;
import com.palantir.dialogue.DialogueServiceFactory;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.EndpointChannel;
import com.palantir.dialogue.EndpointChannelFactory;
import com.palantir.dialogue.PlainSerDe;
import com.palantir.dialogue.Request;
import com.palantir.dialogue.TypeMarker;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
@DialogueService(RecipeBookServiceBlocking.Factory.class)
public interface RecipeBookServiceBlocking {
    /** @apiNote {@code GET /recipes/{recipeName}} */
    @ClientEndpoint(method = "GET", path = "/recipes/{recipeName}")
    Recipe getRecipe(RecipeName recipeName);

    /** Creates a synchronous/blocking client for a RecipeBookService service. */
    static RecipeBookServiceBlocking of(EndpointChannelFactory _endpointChannelFactory, ConjureRuntime _runtime) {
        return new RecipeBookServiceBlocking() {
            private final PlainSerDe _plainSerDe = _runtime.plainSerDe();

            private final EndpointChannel getRecipeChannel =
                    _endpointChannelFactory.endpoint(DialogueRecipeBookEndpoints.getRecipe);

            private final Deserializer<Recipe> getRecipeDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Recipe>() {});

            @Override
            public Recipe getRecipe(RecipeName recipeName) {
                Request.Builder _request = Request.builder();
                _request.putPathParams("recipeName", _plainSerDe.serializeString(recipeName.get()));
                try {
                    return _runtime.clients().callBlocking(getRecipeChannel, _request.build(), getRecipeDeserializer);
                } catch (RemoteException remoteException) {
                    if (RecipesErrors.isNotFound(remoteException)) {
                        throw RecipesErrors.notFound(remoteException);
                    }
                }
            }

            @Override
            public String toString() {
                return "RecipeBookServiceBlocking{_endpointChannelFactory=" + _endpointChannelFactory + ", runtime="
                        + _runtime + '}';
            }
        };
    }

    /** Creates an asynchronous/non-blocking client for a RecipeBookService service. */
    static RecipeBookServiceBlocking of(Channel _channel, ConjureRuntime _runtime) {
        if (_channel instanceof EndpointChannelFactory) {
            return of((EndpointChannelFactory) _channel, _runtime);
        }
        return of(
                new EndpointChannelFactory() {
                    @Override
                    public EndpointChannel endpoint(Endpoint endpoint) {
                        return _runtime.clients().bind(_channel, endpoint);
                    }
                },
                _runtime);
    }

    final class Factory implements DialogueServiceFactory<RecipeBookServiceBlocking> {
        @Override
        public RecipeBookServiceBlocking create(EndpointChannelFactory endpointChannelFactory, ConjureRuntime runtime) {
            return RecipeBookServiceBlocking.of(endpointChannelFactory, runtime);
        }
    }
}
