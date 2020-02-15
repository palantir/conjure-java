package com.palantir.product;

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.dialogue.BinaryRequestBody;
import com.palantir.tokens.auth.AuthHeader;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueServiceGenerator")
public interface AsyncEteBinaryService {
    ListenableFuture<InputStream> postBinary(AuthHeader authHeader, BinaryRequestBody body);

    ListenableFuture<Optional<ByteBuffer>> getOptionalBinaryPresent(AuthHeader authHeader);

    ListenableFuture<Optional<ByteBuffer>> getOptionalBinaryEmpty(AuthHeader authHeader);

    /** Throws an exception after partially writing a binary response. */
    ListenableFuture<InputStream> getBinaryFailure(AuthHeader authHeader, int numBytes);
}
