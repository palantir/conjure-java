package com.palantir.product;

import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import java.lang.Override;
import java.lang.String;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
public interface EmptyPathServiceBlocking {
    boolean emptyPath();

    /**
     * Creates a synchronous/blocking client for a EmptyPathService service.
     */
    static EmptyPathServiceBlocking of(Channel _channel, ConjureRuntime _runtime) {
        EmptyPathServiceAsync delegate = EmptyPathServiceAsync.of(_channel, _runtime);
        return new EmptyPathServiceBlocking() {
            @Override
            public boolean emptyPath() {
                return _runtime.clients().block(delegate.emptyPath());
            }

            @Override
            public String toString() {
                return "EmptyPathService{channel=" + _channel + ", runtime=" + _runtime + '}';
            }
        };
    }
}
