package com.palantir.product;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.palantir.dialogue.BinaryRequestBody;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.Deserializer;
import com.palantir.dialogue.PlainSerDe;
import com.palantir.dialogue.Request;
import com.palantir.dialogue.TypeMarker;
import com.palantir.tokens.auth.AuthHeader;
import java.io.InputStream;
import java.lang.Override;
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

    /** Creates an asynchronous/non-blocking client for a EteBinaryService service. */
    static AsyncEteBinaryService of(Channel channel, ConjureRuntime runtime) {
        return new AsyncEteBinaryService() {
            private final PlainSerDe plainSerDe = runtime.plainSerDe();

            private final Deserializer<Optional<ByteBuffer>> getOptionalBinaryPresentDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<Optional<ByteBuffer>>() {});

            private final Deserializer<Optional<ByteBuffer>> getOptionalBinaryEmptyDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<Optional<ByteBuffer>>() {});

            @Override
            public ListenableFuture<InputStream> postBinary(
                    AuthHeader authHeader, BinaryRequestBody body) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.body(runtime.bodySerDe().serialize(body));
                return Futures.transform(
                        channel.execute(DialogueEteBinaryEndpoints.postBinary, _request.build()),
                        runtime.bodySerDe()::deserializeInputStream,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<Optional<ByteBuffer>> getOptionalBinaryPresent(
                    AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return Futures.transform(
                        channel.execute(
                                DialogueEteBinaryEndpoints.getOptionalBinaryPresent,
                                _request.build()),
                        getOptionalBinaryPresentDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<Optional<ByteBuffer>> getOptionalBinaryEmpty(
                    AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return Futures.transform(
                        channel.execute(
                                DialogueEteBinaryEndpoints.getOptionalBinaryEmpty,
                                _request.build()),
                        getOptionalBinaryEmptyDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<InputStream> getBinaryFailure(
                    AuthHeader authHeader, int numBytes) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.putQueryParams("numBytes", plainSerDe.serializeInteger(numBytes));
                return Futures.transform(
                        channel.execute(
                                DialogueEteBinaryEndpoints.getBinaryFailure, _request.build()),
                        runtime.bodySerDe()::deserializeInputStream,
                        MoreExecutors.directExecutor());
            }
        };
    }
}
