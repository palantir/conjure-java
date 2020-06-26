package com.palantir.product;

import com.google.errorprone.annotations.MustBeClosed;
import com.palantir.dialogue.BinaryRequestBody;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.tokens.auth.AuthHeader;
import java.io.InputStream;
import java.lang.Override;
import java.lang.String;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
public interface EteBinaryServiceBlocking {
    /**
     * POST /binary
     */
    InputStream postBinary(AuthHeader authHeader, BinaryRequestBody body);

    /**
     * GET /binary/optional/present
     */
    Optional<InputStream> getOptionalBinaryPresent(AuthHeader authHeader);

    /**
     * GET /binary/optional/empty
     */
    Optional<InputStream> getOptionalBinaryEmpty(AuthHeader authHeader);

    /**
     * Throws an exception after partially writing a binary response.
     * GET /binary/failure
     */
    InputStream getBinaryFailure(AuthHeader authHeader, int numBytes);

    /**
     * Creates a synchronous/blocking client for a EteBinaryService service.
     */
    static EteBinaryServiceBlocking of(Channel _channel, ConjureRuntime _runtime) {
        EteBinaryServiceAsync delegate = EteBinaryServiceAsync.of(_channel, _runtime);
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
                return "EteBinaryServiceBlocking{channel=" + _channel + ", runtime=" + _runtime + '}';
            }
        };
    }
}
