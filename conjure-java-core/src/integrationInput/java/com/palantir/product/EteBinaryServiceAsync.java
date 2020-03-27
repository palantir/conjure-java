package com.palantir.product;

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.dialogue.BinaryRequestBody;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.PlainSerDe;
import com.palantir.dialogue.Request;
import com.palantir.tokens.auth.AuthHeader;
import java.io.InputStream;
import java.lang.Override;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
public interface EteBinaryServiceAsync {
    ListenableFuture<InputStream> postBinary(AuthHeader authHeader, BinaryRequestBody body);

    ListenableFuture<Optional<InputStream>> getOptionalBinaryPresent(AuthHeader authHeader);

    ListenableFuture<Optional<InputStream>> getOptionalBinaryEmpty(AuthHeader authHeader);

    /**
     * Throws an exception after partially writing a binary response.
     */
    ListenableFuture<InputStream> getBinaryFailure(AuthHeader authHeader, int numBytes);

    /**
     * Creates an asynchronous/non-blocking client for a EteBinaryService service.
     */
    static EteBinaryServiceAsync of(Channel channel, ConjureRuntime runtime) {
        return new EteBinaryServiceAsync() {
            private final PlainSerDe plainSerDe = runtime.plainSerDe();

            @Override
            public ListenableFuture<InputStream> postBinary(AuthHeader authHeader, BinaryRequestBody body) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.body(runtime.bodySerDe().serialize(body));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteBinaryEndpoints.postBinary,
                                _request.build(),
                                runtime.bodySerDe().inputStreamDeserializer());
            }

            @Override
            public ListenableFuture<Optional<InputStream>> getOptionalBinaryPresent(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteBinaryEndpoints.getOptionalBinaryPresent,
                                _request.build(),
                                runtime.bodySerDe().optionalInputStreamDeserializer());
            }

            @Override
            public ListenableFuture<Optional<InputStream>> getOptionalBinaryEmpty(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteBinaryEndpoints.getOptionalBinaryEmpty,
                                _request.build(),
                                runtime.bodySerDe().optionalInputStreamDeserializer());
            }

            @Override
            public ListenableFuture<InputStream> getBinaryFailure(AuthHeader authHeader, int numBytes) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.putQueryParams("numBytes", plainSerDe.serializeInteger(numBytes));
                return runtime.clients()
                        .call(
                                channel,
                                DialogueEteBinaryEndpoints.getBinaryFailure,
                                _request.build(),
                                runtime.bodySerDe().inputStreamDeserializer());
            }
        };
    }
}
