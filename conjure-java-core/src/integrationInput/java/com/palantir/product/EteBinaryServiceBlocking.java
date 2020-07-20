package com.palantir.product;

import com.google.errorprone.annotations.MustBeClosed;
import com.palantir.dialogue.BinaryRequestBody;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.EndpointChannel;
import com.palantir.dialogue.EndpointChannelFactory;
import com.palantir.tokens.auth.AuthHeader;
import java.io.InputStream;
import java.lang.Override;
import java.lang.String;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
public interface EteBinaryServiceBlocking {
    /**
     * @apiNote {@code POST /binary}
     */
    InputStream postBinary(AuthHeader authHeader, BinaryRequestBody body);

    /**
     * @apiNote {@code GET /binary/optional/present}
     */
    Optional<InputStream> getOptionalBinaryPresent(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /binary/optional/empty}
     */
    Optional<InputStream> getOptionalBinaryEmpty(AuthHeader authHeader);

    /**
     * Throws an exception after partially writing a binary response.
     * @apiNote {@code GET /binary/failure}
     */
    InputStream getBinaryFailure(AuthHeader authHeader, int numBytes);

    /**
     * Creates a synchronous/blocking client for a EteBinaryService service.
     */
    static EteBinaryServiceBlocking of(EndpointChannelFactory _endpointChannelFactory, ConjureRuntime _runtime) {
        EteBinaryServiceAsync delegate = EteBinaryServiceAsync.of(_endpointChannelFactory, _runtime);
        return new EteBinaryServiceBlocking() {
            @Override
            @MustBeClosed
            public InputStream postBinary(AuthHeader authHeader, BinaryRequestBody body) {
                return _runtime.clients().block(delegate.postBinary(authHeader, body));
            }

            @Override
            public Optional<InputStream> getOptionalBinaryPresent(AuthHeader authHeader) {
                return _runtime.clients().block(delegate.getOptionalBinaryPresent(authHeader));
            }

            @Override
            public Optional<InputStream> getOptionalBinaryEmpty(AuthHeader authHeader) {
                return _runtime.clients().block(delegate.getOptionalBinaryEmpty(authHeader));
            }

            @Override
            @MustBeClosed
            public InputStream getBinaryFailure(AuthHeader authHeader, int numBytes) {
                return _runtime.clients().block(delegate.getBinaryFailure(authHeader, numBytes));
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
}
