package com.palantir.product;

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.dialogue.BinaryRequestBody;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.EndpointChannel;
import com.palantir.dialogue.PlainSerDe;
import com.palantir.dialogue.Request;
import com.palantir.tokens.auth.AuthHeader;
import java.io.InputStream;
import java.lang.Override;
import java.lang.String;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
public interface EteBinaryServiceAsync {
    /**
     * @apiNote POST /binary
     */
    ListenableFuture<InputStream> postBinary(AuthHeader authHeader, BinaryRequestBody body);

    /**
     * @apiNote GET /binary/optional/present
     */
    ListenableFuture<Optional<InputStream>> getOptionalBinaryPresent(AuthHeader authHeader);

    /**
     * @apiNote GET /binary/optional/empty
     */
    ListenableFuture<Optional<InputStream>> getOptionalBinaryEmpty(AuthHeader authHeader);

    /**
     * Throws an exception after partially writing a binary response.
     * @apiNote GET /binary/failure
     */
    ListenableFuture<InputStream> getBinaryFailure(AuthHeader authHeader, int numBytes);

    /**
     * Creates an asynchronous/non-blocking client for a EteBinaryService service.
     */
    static EteBinaryServiceAsync of(Channel _channel, ConjureRuntime _runtime) {
        return new EteBinaryServiceAsync() {
            private final PlainSerDe _plainSerDe = _runtime.plainSerDe();

            private final EndpointChannel postBinaryChannel =
                    _runtime.clients().bind(_channel, DialogueEteBinaryEndpoints.postBinary);

            private final EndpointChannel getOptionalBinaryPresentChannel =
                    _runtime.clients().bind(_channel, DialogueEteBinaryEndpoints.getOptionalBinaryPresent);

            private final EndpointChannel getOptionalBinaryEmptyChannel =
                    _runtime.clients().bind(_channel, DialogueEteBinaryEndpoints.getOptionalBinaryEmpty);

            private final EndpointChannel getBinaryFailureChannel =
                    _runtime.clients().bind(_channel, DialogueEteBinaryEndpoints.getBinaryFailure);

            @Override
            public ListenableFuture<InputStream> postBinary(AuthHeader authHeader, BinaryRequestBody body) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(_runtime.bodySerDe().serialize(body));
                return _runtime.clients()
                        .call(
                                postBinaryChannel,
                                _request.build(),
                                _runtime.bodySerDe().inputStreamDeserializer());
            }

            @Override
            public ListenableFuture<Optional<InputStream>> getOptionalBinaryPresent(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients()
                        .call(
                                getOptionalBinaryPresentChannel,
                                _request.build(),
                                _runtime.bodySerDe().optionalInputStreamDeserializer());
            }

            @Override
            public ListenableFuture<Optional<InputStream>> getOptionalBinaryEmpty(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients()
                        .call(
                                getOptionalBinaryEmptyChannel,
                                _request.build(),
                                _runtime.bodySerDe().optionalInputStreamDeserializer());
            }

            @Override
            public ListenableFuture<InputStream> getBinaryFailure(AuthHeader authHeader, int numBytes) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putQueryParams("numBytes", _plainSerDe.serializeInteger(numBytes));
                return _runtime.clients()
                        .call(
                                getBinaryFailureChannel,
                                _request.build(),
                                _runtime.bodySerDe().inputStreamDeserializer());
            }

            @Override
            public String toString() {
                return "EteBinaryServiceBlocking{channel=" + _channel + ", runtime=" + _runtime + '}';
            }
        };
    }
}
