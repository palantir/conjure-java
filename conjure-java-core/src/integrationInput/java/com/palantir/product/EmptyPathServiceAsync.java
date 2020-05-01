package com.palantir.product;

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.Deserializer;
import com.palantir.dialogue.PlainSerDe;
import com.palantir.dialogue.Request;
import com.palantir.dialogue.TypeMarker;
import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
public interface EmptyPathServiceAsync {
    ListenableFuture<Boolean> emptyPath();

    /**
     * Creates an asynchronous/non-blocking client for a EmptyPathService service.
     */
    static EmptyPathServiceAsync of(Channel _channel, ConjureRuntime _runtime) {
        return new EmptyPathServiceAsync() {
            private final PlainSerDe _plainSerDe = _runtime.plainSerDe();

            private final Deserializer<Boolean> emptyPathDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Boolean>() {});

            @Override
            public ListenableFuture<Boolean> emptyPath() {
                Request.Builder _request = Request.builder();
                return _runtime.clients()
                        .call(_channel, DialogueEmptyPathEndpoints.emptyPath, _request.build(), emptyPathDeserializer);
            }

            @Override
            public String toString() {
                return "EmptyPathService{channel=" + _channel + ", runtime=" + _runtime + '}';
            }
        };
    }
}
