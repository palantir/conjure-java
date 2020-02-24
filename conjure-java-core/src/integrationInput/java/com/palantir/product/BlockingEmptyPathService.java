package com.palantir.product;

import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.RemoteExceptions;
import java.lang.Override;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
public interface BlockingEmptyPathService {
    boolean emptyPath();

    /** Creates a synchronous/blocking client for a EmptyPathService service. */
    static BlockingEmptyPathService of(Channel channel, ConjureRuntime runtime) {
        AsyncEmptyPathService delegate = AsyncEmptyPathService.of(channel, runtime);
        return new BlockingEmptyPathService() {
            @Override
            public boolean emptyPath() {
                return RemoteExceptions.getUnchecked(delegate.emptyPath());
            }
        };
    }
}
