package com.palantir.product;

import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.RemoteExceptions;
import java.lang.Override;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
public interface EmptyPathServiceBlocking {
    boolean emptyPath();

    /** Creates a synchronous/blocking client for a EmptyPathService service. */
    static EmptyPathServiceBlocking of(Channel channel, ConjureRuntime runtime) {
        EmptyPathServiceAsync delegate = EmptyPathServiceAsync.of(channel, runtime);
        return new EmptyPathServiceBlocking() {
            @Override
            public boolean emptyPath() {
                return RemoteExceptions.getUnchecked(delegate.emptyPath());
            }
        };
    }
}
