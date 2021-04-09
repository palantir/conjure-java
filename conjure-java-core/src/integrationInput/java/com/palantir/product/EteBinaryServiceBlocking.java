package com.palantir.product;

import com.google.errorprone.annotations.MustBeClosed;
import com.palantir.conjure.java.lib.internal.ConjureEndpoint;
import com.palantir.conjure.java.lib.internal.ConjureService;
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
import java.util.Optional;
import javax.annotation.Generated;

@DialogueService(EteBinaryServiceBlocking.Factory.class)
@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
@ConjureService(name = "EteBinaryService", package_ = "com.palantir.product")
public interface EteBinaryServiceBlocking {
    /**
     * @apiNote {@code POST /binary}
     */
    @ConjureEndpoint(path = "/binary", method = "POST")
    @MustBeClosed
    InputStream postBinary(AuthHeader authHeader, BinaryRequestBody body);

    /**
     * @apiNote {@code POST /binary/throws}
     */
    @ConjureEndpoint(path = "/binary/throws", method = "POST")
    @MustBeClosed
    InputStream postBinaryThrows(AuthHeader authHeader, int bytesToRead, BinaryRequestBody body);

    /**
     * @apiNote {@code GET /binary/optional/present}
     */
    @ConjureEndpoint(path = "/binary/optional/present", method = "GET")
    Optional<InputStream> getOptionalBinaryPresent(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /binary/optional/empty}
     */
    @ConjureEndpoint(path = "/binary/optional/empty", method = "GET")
    Optional<InputStream> getOptionalBinaryEmpty(AuthHeader authHeader);

    /**
     * Throws an exception after partially writing a binary response.
     * @apiNote {@code GET /binary/failure}
     */
    @ConjureEndpoint(path = "/binary/failure", method = "GET")
    @MustBeClosed
    InputStream getBinaryFailure(AuthHeader authHeader, int numBytes);

    /**
     * @apiNote {@code GET /binary/aliased}
     */
    @ConjureEndpoint(path = "/binary/aliased", method = "GET")
    Optional<InputStream> getAliased(AuthHeader authHeader);

    /**
     * Creates a synchronous/blocking client for a EteBinaryService service.
     */
    static EteBinaryServiceBlocking of(EndpointChannelFactory _endpointChannelFactory, ConjureRuntime _runtime) {
        return new EteBinaryServiceBlocking() {
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
            public InputStream postBinary(AuthHeader authHeader, BinaryRequestBody body) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(_runtime.bodySerDe().serialize(body));
                return _runtime.clients()
                        .callBlocking(
                                postBinaryChannel,
                                _request.build(),
                                _runtime.bodySerDe().inputStreamDeserializer());
            }

            @Override
            public InputStream postBinaryThrows(AuthHeader authHeader, int bytesToRead, BinaryRequestBody body) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(_runtime.bodySerDe().serialize(body));
                _request.putQueryParams("bytesToRead", _plainSerDe.serializeInteger(bytesToRead));
                return _runtime.clients()
                        .callBlocking(
                                postBinaryThrowsChannel,
                                _request.build(),
                                _runtime.bodySerDe().inputStreamDeserializer());
            }

            @Override
            public Optional<InputStream> getOptionalBinaryPresent(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients()
                        .callBlocking(
                                getOptionalBinaryPresentChannel,
                                _request.build(),
                                _runtime.bodySerDe().optionalInputStreamDeserializer());
            }

            @Override
            public Optional<InputStream> getOptionalBinaryEmpty(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients()
                        .callBlocking(
                                getOptionalBinaryEmptyChannel,
                                _request.build(),
                                _runtime.bodySerDe().optionalInputStreamDeserializer());
            }

            @Override
            public InputStream getBinaryFailure(AuthHeader authHeader, int numBytes) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.putQueryParams("numBytes", _plainSerDe.serializeInteger(numBytes));
                return _runtime.clients()
                        .callBlocking(
                                getBinaryFailureChannel,
                                _request.build(),
                                _runtime.bodySerDe().inputStreamDeserializer());
            }

            @Override
            public Optional<InputStream> getAliased(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients()
                        .callBlocking(
                                getAliasedChannel,
                                _request.build(),
                                _runtime.bodySerDe().optionalInputStreamDeserializer());
            }

            @Override
            public String toString() {
                return "EteBinaryServiceBlocking{_endpointChannelFactory=" + _endpointChannelFactory + ", runtime="
                        + _runtime + '}';
            }
        };
    }

    /**
     * Creates an asynchronous/non-blocking client for a EteBinaryService service.
     */
    static EteBinaryServiceBlocking of(Channel _channel, ConjureRuntime _runtime) {
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

    final class Factory implements DialogueServiceFactory<EteBinaryServiceBlocking> {
        @Override
        public EteBinaryServiceBlocking create(EndpointChannelFactory endpointChannelFactory, ConjureRuntime runtime) {
            return EteBinaryServiceBlocking.of(endpointChannelFactory, runtime);
        }
    }
}
