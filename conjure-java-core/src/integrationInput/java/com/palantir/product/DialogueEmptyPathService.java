package com.palantir.product;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.Deserializer;
import com.palantir.dialogue.PlainSerDe;
import com.palantir.dialogue.RemoteExceptions;
import com.palantir.dialogue.Request;
import com.palantir.dialogue.TypeMarker;
import java.lang.Boolean;
import java.lang.Override;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueServiceGenerator")
public final class DialogueEmptyPathService {
    private DialogueEmptyPathService() {}

    /** Creates a synchronous/blocking client for a EmptyPathService service. */
    public static BlockingEmptyPathService blocking(Channel channel, ConjureRuntime runtime) {
        AsyncEmptyPathService delegate = async(channel, runtime);
        return new BlockingEmptyPathService() {
            @Override
            public boolean emptyPath() {
                return RemoteExceptions.getUnchecked(delegate.emptyPath());
            }
        };
    }

    /** Creates an asynchronous/non-blocking client for a EmptyPathService service. */
    public static AsyncEmptyPathService async(Channel channel, ConjureRuntime runtime) {
        return new AsyncEmptyPathService() {
            private final PlainSerDe plainSerDe = runtime.plainSerDe();

            private final Deserializer<Boolean> emptyPathDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<Boolean>() {});

            @Override
            public ListenableFuture<Boolean> emptyPath() {
                Request.Builder _request = Request.builder();
                return Futures.transform(
                        channel.execute(DialogueEmptyPathEndpoints.emptyPath, _request.build()),
                        emptyPathDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }
        };
    }
}
