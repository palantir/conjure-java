package com.palantir.product;

import com.palantir.dialogue.BinaryRequestBody;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.RemoteExceptions;
import com.palantir.tokens.auth.AuthHeader;
import java.io.InputStream;
import java.lang.Override;
import java.nio.ByteBuffer;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueServiceGenerator")
public interface BlockingEteBinaryService {
    InputStream postBinary(AuthHeader authHeader, BinaryRequestBody body);

    Optional<ByteBuffer> getOptionalBinaryPresent(AuthHeader authHeader);

    Optional<ByteBuffer> getOptionalBinaryEmpty(AuthHeader authHeader);

    /** Throws an exception after partially writing a binary response. */
    InputStream getBinaryFailure(AuthHeader authHeader, int numBytes);

    /** Creates a synchronous/blocking client for a EteBinaryService service. */
    static BlockingEteBinaryService of(Channel channel, ConjureRuntime runtime) {
        AsyncEteBinaryService delegate = AsyncEteBinaryService.of(channel, runtime);
        return new BlockingEteBinaryService() {
            @Override
            public InputStream postBinary(AuthHeader authHeader, BinaryRequestBody body) {
                return RemoteExceptions.getUnchecked(delegate.postBinary(authHeader, body));
            }

            @Override
            public Optional<ByteBuffer> getOptionalBinaryPresent(AuthHeader authHeader) {
                return RemoteExceptions.getUnchecked(delegate.getOptionalBinaryPresent(authHeader));
            }

            @Override
            public Optional<ByteBuffer> getOptionalBinaryEmpty(AuthHeader authHeader) {
                return RemoteExceptions.getUnchecked(delegate.getOptionalBinaryEmpty(authHeader));
            }

            @Override
            public InputStream getBinaryFailure(AuthHeader authHeader, int numBytes) {
                return RemoteExceptions.getUnchecked(
                        delegate.getBinaryFailure(authHeader, numBytes));
            }
        };
    }
}
