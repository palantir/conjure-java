package com.palantir.product;

import com.palantir.dialogue.BinaryRequestBody;
import com.palantir.tokens.auth.AuthHeader;
import java.io.InputStream;
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
}
