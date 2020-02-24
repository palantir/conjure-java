package com.palantir.product;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.palantir.dialogue.BinaryRequestBody;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.Deserializer;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.HttpMethod;
import com.palantir.dialogue.PathTemplate;
import com.palantir.dialogue.PlainSerDe;
import com.palantir.dialogue.RemoteExceptions;
import com.palantir.dialogue.Request;
import com.palantir.dialogue.TypeMarker;
import com.palantir.dialogue.UrlBuilder;
import com.palantir.tokens.auth.AuthHeader;
import java.io.InputStream;
import java.lang.Override;
import java.lang.String;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueServiceGenerator")
public final class DialogueEteBinaryService {
    private static final Endpoint postBinary =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder().fixed("binary").build();

                @Override
                public void renderPath(Map<String, String> params, UrlBuilder url) {
                    pathTemplate.fill(params, url);
                }

                @Override
                public HttpMethod httpMethod() {
                    return HttpMethod.POST;
                }

                @Override
                public String serviceName() {
                    return "EteBinaryService";
                }

                @Override
                public String endpointName() {
                    return "postBinary";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint getOptionalBinaryPresent =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder()
                                .fixed("binary")
                                .fixed("optional")
                                .fixed("present")
                                .build();

                @Override
                public void renderPath(Map<String, String> params, UrlBuilder url) {
                    pathTemplate.fill(params, url);
                }

                @Override
                public HttpMethod httpMethod() {
                    return HttpMethod.GET;
                }

                @Override
                public String serviceName() {
                    return "EteBinaryService";
                }

                @Override
                public String endpointName() {
                    return "getOptionalBinaryPresent";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint getOptionalBinaryEmpty =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder()
                                .fixed("binary")
                                .fixed("optional")
                                .fixed("empty")
                                .build();

                @Override
                public void renderPath(Map<String, String> params, UrlBuilder url) {
                    pathTemplate.fill(params, url);
                }

                @Override
                public HttpMethod httpMethod() {
                    return HttpMethod.GET;
                }

                @Override
                public String serviceName() {
                    return "EteBinaryService";
                }

                @Override
                public String endpointName() {
                    return "getOptionalBinaryEmpty";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint getBinaryFailure =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder().fixed("binary").fixed("failure").build();

                @Override
                public void renderPath(Map<String, String> params, UrlBuilder url) {
                    pathTemplate.fill(params, url);
                }

                @Override
                public HttpMethod httpMethod() {
                    return HttpMethod.GET;
                }

                @Override
                public String serviceName() {
                    return "EteBinaryService";
                }

                @Override
                public String endpointName() {
                    return "getBinaryFailure";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private DialogueEteBinaryService() {}

    /** Creates a synchronous/blocking client for a EteBinaryService service. */
    public static BlockingEteBinaryService blocking(Channel channel, ConjureRuntime runtime) {
        AsyncEteBinaryService delegate = async(channel, runtime);
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

    /** Creates an asynchronous/non-blocking client for a EteBinaryService service. */
    public static AsyncEteBinaryService async(Channel channel, ConjureRuntime runtime) {
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
