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
    static EteBinaryServiceAsync of(Channel _channel, ConjureRuntime _runtime) {
        return new EteBinaryServiceAsync() {
            private final PlainSerDe _plainSerDe = _runtime.plainSerDe();

            @Override
            public ListenableFuture<InputStream> postBinary(AuthHeader authHeader, BinaryRequestBody body) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization", _plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.body(_runtime.bodySerDe().serialize(body));
                return _runtime.clients()
                        .call(
                                _channel,
                                DialogueEteBinaryEndpoints.postBinary,
                                _request.build(),
                                _runtime.bodySerDe().inputStreamDeserializer());
            }

            @Override
            public ListenableFuture<Optional<InputStream>> getOptionalBinaryPresent(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization", _plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return _runtime.clients()
                        .call(
                                _channel,
                                DialogueEteBinaryEndpoints.getOptionalBinaryPresent,
                                _request.build(),
                                _runtime.bodySerDe().optionalInputStreamDeserializer());
            }

            @Override
            public ListenableFuture<Optional<InputStream>> getOptionalBinaryEmpty(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization", _plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return _runtime.clients()
                        .call(
                                _channel,
                                DialogueEteBinaryEndpoints.getOptionalBinaryEmpty,
                                _request.build(),
                                _runtime.bodySerDe().optionalInputStreamDeserializer());
            }

            @Override
            public ListenableFuture<InputStream> getBinaryFailure(AuthHeader authHeader, int numBytes) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization", _plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.putQueryParams("numBytes", _plainSerDe.serializeInteger(numBytes));
                return _runtime.clients()
                        .call(
                                _channel,
                                DialogueEteBinaryEndpoints.getBinaryFailure,
                                _request.build(),
                                _runtime.bodySerDe().inputStreamDeserializer());
            }
        };
    }
}
