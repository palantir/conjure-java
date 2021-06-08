package com.palantir.product;

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.conjure.java.lib.internal.ClientEndpoint;
import com.palantir.dialogue.BinaryRequestBody;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.DialogueService;
import com.palantir.dialogue.DialogueServiceFactory;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.EndpointChannel;
import com.palantir.dialogue.EndpointChannelFactory;
import com.palantir.dialogue.PlainSerDe;
import com.palantir.dialogue.Request;
import com.palantir.tokens.auth.AuthHeader;
import java.io.InputStream;
import java.lang.Override;
import java.lang.String;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
@DialogueService(EteBinaryServiceAsync.Factory.class)
public interface EteBinaryServiceAsync {
    /**
     * @apiNote {@code POST /binary}
     */
    @ClientEndpoint(method = "POST", path = "/binary")
    ListenableFuture<InputStream> postBinary(AuthHeader authHeader, BinaryRequestBody body);

    /**
     * @apiNote {@code POST /binary/throws}
     */
    @ClientEndpoint(method = "POST", path = "/binary/throws")
    ListenableFuture<InputStream> postBinaryThrows(AuthHeader authHeader, int bytesToRead, BinaryRequestBody body);

    /**
     * @apiNote {@code GET /binary/optional/present}
     */
    @ClientEndpoint(method = "GET", path = "/binary/optional/present")
    ListenableFuture<Optional<InputStream>> getOptionalBinaryPresent(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /binary/optional/empty}
     */
    @ClientEndpoint(method = "GET", path = "/binary/optional/empty")
    ListenableFuture<Optional<InputStream>> getOptionalBinaryEmpty(AuthHeader authHeader);

    /**
     * Throws an exception after partially writing a binary response.
     * @apiNote {@code GET /binary/failure}
     */
    @ClientEndpoint(method = "GET", path = "/binary/failure")
    ListenableFuture<InputStream> getBinaryFailure(AuthHeader authHeader, int numBytes);

    /**
     * @apiNote {@code GET /binary/aliased}
     */
    @ClientEndpoint(method = "GET", path = "/binary/aliased")
    ListenableFuture<Optional<InputStream>> getAliased(AuthHeader authHeader);

    /**
     * Creates an asynchronous/non-blocking client for a EteBinaryService service.
     */
    static EteBinaryServiceAsync of(EndpointChannelFactory _endpointChannelFactory, ConjureRuntime _runtime) {
        return new EteBinaryServiceAsync() {
            private final PlainSerDe _plainSerDe = _runtime.plainSerDe();

            private final EndpointChannel postBinaryChannel =
                    _endpointChannelFactory.endpoint(DialogueEteBinaryEndpoints.postBinary);

            private final EndpointChannel postBinaryThrowsChannel =
                    _endpointChannelFactory.endpoint(DialogueEteBinaryEndpoints.postBinaryThrows);

            private final EndpointChannel getOptionalBinaryPresentChannel =
                    _endpointChannelFactory.endpoint(DialogueEteBinaryEndpoints.getOptionalBinaryPresent);

            private final EndpointChannel getOptionalBinaryEmptyChannel =
                    _endpointChannelFactory.endpoint(DialogueEteBinaryEndpoints.getOptionalBinaryEmpty);

            private final EndpointChannel getBinaryFailureChannel =
                    _endpointChannelFactory.endpoint(DialogueEteBinaryEndpoints.getBinaryFailure);

            private final EndpointChannel getAliasedChannel =
                    _endpointChannelFactory.endpoint(DialogueEteBinaryEndpoints.getAliased);

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
            public ListenableFuture<InputStream> postBinaryThrows(
                    AuthHeader authHeader, int bytesToRead, BinaryRequestBody body) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(_runtime.bodySerDe().serialize(body));
                _request.putQueryParams("bytesToRead", _plainSerDe.serializeInteger(bytesToRead));
                return _runtime.clients()
                        .call(
                                postBinaryThrowsChannel,
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
            public ListenableFuture<Optional<InputStream>> getAliased(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients()
                        .call(
                                getAliasedChannel,
                                _request.build(),
                                _runtime.bodySerDe().optionalInputStreamDeserializer());
            }

            @Override
            public String toString() {
                return "EteBinaryServiceAsync{_endpointChannelFactory=" + _endpointChannelFactory + ", runtime="
                        + _runtime + '}';
            }
        };
    }

    /**
     * Creates an asynchronous/non-blocking client for a EteBinaryService service.
     */
    static EteBinaryServiceAsync of(Channel _channel, ConjureRuntime _runtime) {
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

    final class Factory implements DialogueServiceFactory<EteBinaryServiceAsync> {
        @Override
        public EteBinaryServiceAsync create(EndpointChannelFactory endpointChannelFactory, ConjureRuntime runtime) {
            return EteBinaryServiceAsync.of(endpointChannelFactory, runtime);
        }
    }
}
