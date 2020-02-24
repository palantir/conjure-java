package com.palantir.product;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.Deserializer;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.HttpMethod;
import com.palantir.dialogue.PathTemplate;
import com.palantir.dialogue.PlainSerDe;
import com.palantir.dialogue.RemoteExceptions;
import com.palantir.dialogue.Request;
import com.palantir.dialogue.Serializer;
import com.palantir.dialogue.TypeMarker;
import com.palantir.dialogue.UrlBuilder;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import java.io.InputStream;
import java.lang.Boolean;
import java.lang.Double;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.Void;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueServiceGenerator")
public final class DialogueEteService {
    private static final Endpoint string =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder().fixed("base").fixed("string").build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "string";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint integer =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder().fixed("base").fixed("integer").build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "integer";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint double_ =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder().fixed("base").fixed("double").build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "double_";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint boolean_ =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder().fixed("base").fixed("boolean").build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "boolean_";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint safelong =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder().fixed("base").fixed("safelong").build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "safelong";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint rid =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder().fixed("base").fixed("rid").build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "rid";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint bearertoken =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder().fixed("base").fixed("bearertoken").build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "bearertoken";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint optionalString =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder().fixed("base").fixed("optionalString").build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "optionalString";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint optionalEmpty =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder().fixed("base").fixed("optionalEmpty").build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "optionalEmpty";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint datetime =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder().fixed("base").fixed("datetime").build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "datetime";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint binary =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder().fixed("base").fixed("binary").build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "binary";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint path =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder()
                                .fixed("base")
                                .fixed("path")
                                .variable("param")
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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "path";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint externalLongPath =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder()
                                .fixed("base")
                                .fixed("externalLong")
                                .variable("param")
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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "externalLongPath";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint optionalExternalLongQuery =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder().fixed("base").fixed("optionalExternalLong").build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "optionalExternalLongQuery";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint notNullBody =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder().fixed("base").fixed("notNullBody").build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "notNullBody";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint aliasOne =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder().fixed("base").fixed("aliasOne").build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "aliasOne";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint optionalAliasOne =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder().fixed("base").fixed("optionalAliasOne").build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "optionalAliasOne";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint aliasTwo =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder().fixed("base").fixed("aliasTwo").build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "aliasTwo";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint notNullBodyExternalImport =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder()
                                .fixed("base")
                                .fixed("external")
                                .fixed("notNullBody")
                                .build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "notNullBodyExternalImport";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint optionalBodyExternalImport =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder()
                                .fixed("base")
                                .fixed("external")
                                .fixed("optional-body")
                                .build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "optionalBodyExternalImport";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint optionalQueryExternalImport =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder()
                                .fixed("base")
                                .fixed("external")
                                .fixed("optional-query")
                                .build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "optionalQueryExternalImport";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint noReturn =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder().fixed("base").fixed("no-return").build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "noReturn";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint enumQuery =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder().fixed("base").fixed("enum").fixed("query").build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "enumQuery";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint enumListQuery =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder()
                                .fixed("base")
                                .fixed("enum")
                                .fixed("list")
                                .fixed("query")
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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "enumListQuery";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint optionalEnumQuery =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder()
                                .fixed("base")
                                .fixed("enum")
                                .fixed("optional")
                                .fixed("query")
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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "optionalEnumQuery";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint enumHeader =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder().fixed("base").fixed("enum").fixed("header").build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "enumHeader";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint aliasLongEndpoint =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder().fixed("base").fixed("alias-long").build();

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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "aliasLongEndpoint";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private static final Endpoint complexQueryParameters =
            new Endpoint() {
                private final PathTemplate pathTemplate =
                        PathTemplate.builder()
                                .fixed("base")
                                .fixed("datasets")
                                .variable("datasetRid")
                                .fixed("strings")
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
                    return "EteService";
                }

                @Override
                public String endpointName() {
                    return "complexQueryParameters";
                }

                @Override
                public String version() {
                    return "";
                }
            };

    private DialogueEteService() {}

    /** Creates a synchronous/blocking client for a EteService service. */
    public static BlockingEteService blocking(Channel channel, ConjureRuntime runtime) {
        AsyncEteService delegate = async(channel, runtime);
        return new BlockingEteService() {
            @Override
            public String string(AuthHeader authHeader) {
                return RemoteExceptions.getUnchecked(delegate.string(authHeader));
            }

            @Override
            public int integer(AuthHeader authHeader) {
                return RemoteExceptions.getUnchecked(delegate.integer(authHeader));
            }

            @Override
            public double double_(AuthHeader authHeader) {
                return RemoteExceptions.getUnchecked(delegate.double_(authHeader));
            }

            @Override
            public boolean boolean_(AuthHeader authHeader) {
                return RemoteExceptions.getUnchecked(delegate.boolean_(authHeader));
            }

            @Override
            public SafeLong safelong(AuthHeader authHeader) {
                return RemoteExceptions.getUnchecked(delegate.safelong(authHeader));
            }

            @Override
            public ResourceIdentifier rid(AuthHeader authHeader) {
                return RemoteExceptions.getUnchecked(delegate.rid(authHeader));
            }

            @Override
            public BearerToken bearertoken(AuthHeader authHeader) {
                return RemoteExceptions.getUnchecked(delegate.bearertoken(authHeader));
            }

            @Override
            public Optional<String> optionalString(AuthHeader authHeader) {
                return RemoteExceptions.getUnchecked(delegate.optionalString(authHeader));
            }

            @Override
            public Optional<String> optionalEmpty(AuthHeader authHeader) {
                return RemoteExceptions.getUnchecked(delegate.optionalEmpty(authHeader));
            }

            @Override
            public OffsetDateTime datetime(AuthHeader authHeader) {
                return RemoteExceptions.getUnchecked(delegate.datetime(authHeader));
            }

            @Override
            public InputStream binary(AuthHeader authHeader) {
                return RemoteExceptions.getUnchecked(delegate.binary(authHeader));
            }

            @Override
            public String path(AuthHeader authHeader, String param) {
                return RemoteExceptions.getUnchecked(delegate.path(authHeader, param));
            }

            @Override
            public long externalLongPath(AuthHeader authHeader, long param) {
                return RemoteExceptions.getUnchecked(delegate.externalLongPath(authHeader, param));
            }

            @Override
            public Optional<Long> optionalExternalLongQuery(
                    AuthHeader authHeader, Optional<Long> param) {
                return RemoteExceptions.getUnchecked(
                        delegate.optionalExternalLongQuery(authHeader, param));
            }

            @Override
            public StringAliasExample notNullBody(
                    AuthHeader authHeader, StringAliasExample notNullBody) {
                return RemoteExceptions.getUnchecked(delegate.notNullBody(authHeader, notNullBody));
            }

            @Override
            public StringAliasExample aliasOne(
                    AuthHeader authHeader, StringAliasExample queryParamName) {
                return RemoteExceptions.getUnchecked(delegate.aliasOne(authHeader, queryParamName));
            }

            @Override
            public StringAliasExample optionalAliasOne(
                    AuthHeader authHeader, Optional<StringAliasExample> queryParamName) {
                return RemoteExceptions.getUnchecked(
                        delegate.optionalAliasOne(authHeader, queryParamName));
            }

            @Override
            public NestedStringAliasExample aliasTwo(
                    AuthHeader authHeader, NestedStringAliasExample queryParamName) {
                return RemoteExceptions.getUnchecked(delegate.aliasTwo(authHeader, queryParamName));
            }

            @Override
            public StringAliasExample notNullBodyExternalImport(
                    AuthHeader authHeader, StringAliasExample notNullBody) {
                return RemoteExceptions.getUnchecked(
                        delegate.notNullBodyExternalImport(authHeader, notNullBody));
            }

            @Override
            public Optional<StringAliasExample> optionalBodyExternalImport(
                    AuthHeader authHeader, Optional<StringAliasExample> body) {
                return RemoteExceptions.getUnchecked(
                        delegate.optionalBodyExternalImport(authHeader, body));
            }

            @Override
            public Optional<StringAliasExample> optionalQueryExternalImport(
                    AuthHeader authHeader, Optional<StringAliasExample> query) {
                return RemoteExceptions.getUnchecked(
                        delegate.optionalQueryExternalImport(authHeader, query));
            }

            @Override
            public void noReturn(AuthHeader authHeader) {
                RemoteExceptions.getUnchecked(delegate.noReturn(authHeader));
            }

            @Override
            public SimpleEnum enumQuery(AuthHeader authHeader, SimpleEnum queryParamName) {
                return RemoteExceptions.getUnchecked(
                        delegate.enumQuery(authHeader, queryParamName));
            }

            @Override
            public List<SimpleEnum> enumListQuery(
                    AuthHeader authHeader, List<SimpleEnum> queryParamName) {
                return RemoteExceptions.getUnchecked(
                        delegate.enumListQuery(authHeader, queryParamName));
            }

            @Override
            public Optional<SimpleEnum> optionalEnumQuery(
                    AuthHeader authHeader, Optional<SimpleEnum> queryParamName) {
                return RemoteExceptions.getUnchecked(
                        delegate.optionalEnumQuery(authHeader, queryParamName));
            }

            @Override
            public SimpleEnum enumHeader(AuthHeader authHeader, SimpleEnum headerParameter) {
                return RemoteExceptions.getUnchecked(
                        delegate.enumHeader(authHeader, headerParameter));
            }

            @Override
            public Optional<LongAlias> aliasLongEndpoint(
                    AuthHeader authHeader, Optional<LongAlias> input) {
                return RemoteExceptions.getUnchecked(delegate.aliasLongEndpoint(authHeader, input));
            }

            @Override
            public void complexQueryParameters(
                    AuthHeader authHeader,
                    ResourceIdentifier datasetRid,
                    Set<StringAliasExample> strings,
                    Set<Long> longs,
                    Set<Integer> ints) {
                RemoteExceptions.getUnchecked(
                        delegate.complexQueryParameters(
                                authHeader, datasetRid, strings, longs, ints));
            }
        };
    }

    /** Creates an asynchronous/non-blocking client for a EteService service. */
    public static AsyncEteService async(Channel channel, ConjureRuntime runtime) {
        return new AsyncEteService() {
            private final PlainSerDe plainSerDe = runtime.plainSerDe();

            private final Deserializer<String> stringDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<String>() {});

            private final Deserializer<Integer> integerDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<Integer>() {});

            private final Deserializer<Double> double_Deserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<Double>() {});

            private final Deserializer<Boolean> boolean_Deserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<Boolean>() {});

            private final Deserializer<SafeLong> safelongDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<SafeLong>() {});

            private final Deserializer<ResourceIdentifier> ridDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<ResourceIdentifier>() {});

            private final Deserializer<BearerToken> bearertokenDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<BearerToken>() {});

            private final Deserializer<Optional<String>> optionalStringDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<Optional<String>>() {});

            private final Deserializer<Optional<String>> optionalEmptyDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<Optional<String>>() {});

            private final Deserializer<OffsetDateTime> datetimeDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<OffsetDateTime>() {});

            private final Deserializer<String> pathDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<String>() {});

            private final Deserializer<Long> externalLongPathDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<Long>() {});

            private final Deserializer<Optional<Long>> optionalExternalLongQueryDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<Optional<Long>>() {});

            private final Serializer<StringAliasExample> notNullBodySerializer =
                    runtime.bodySerDe().serializer(new TypeMarker<StringAliasExample>() {});

            private final Deserializer<StringAliasExample> notNullBodyDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<StringAliasExample>() {});

            private final Deserializer<StringAliasExample> aliasOneDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<StringAliasExample>() {});

            private final Deserializer<StringAliasExample> optionalAliasOneDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<StringAliasExample>() {});

            private final Deserializer<NestedStringAliasExample> aliasTwoDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<NestedStringAliasExample>() {});

            private final Serializer<StringAliasExample> notNullBodyExternalImportSerializer =
                    runtime.bodySerDe().serializer(new TypeMarker<StringAliasExample>() {});

            private final Deserializer<StringAliasExample> notNullBodyExternalImportDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<StringAliasExample>() {});

            private final Serializer<Optional<StringAliasExample>>
                    optionalBodyExternalImportSerializer =
                            runtime.bodySerDe()
                                    .serializer(new TypeMarker<Optional<StringAliasExample>>() {});

            private final Deserializer<Optional<StringAliasExample>>
                    optionalBodyExternalImportDeserializer =
                            runtime.bodySerDe()
                                    .deserializer(
                                            new TypeMarker<Optional<StringAliasExample>>() {});

            private final Deserializer<Optional<StringAliasExample>>
                    optionalQueryExternalImportDeserializer =
                            runtime.bodySerDe()
                                    .deserializer(
                                            new TypeMarker<Optional<StringAliasExample>>() {});

            private final Deserializer<Void> noReturnDeserializer =
                    runtime.bodySerDe().emptyBodyDeserializer();

            private final Deserializer<SimpleEnum> enumQueryDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<SimpleEnum>() {});

            private final Deserializer<List<SimpleEnum>> enumListQueryDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<List<SimpleEnum>>() {});

            private final Deserializer<Optional<SimpleEnum>> optionalEnumQueryDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<Optional<SimpleEnum>>() {});

            private final Deserializer<SimpleEnum> enumHeaderDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<SimpleEnum>() {});

            private final Deserializer<Optional<LongAlias>> aliasLongEndpointDeserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<Optional<LongAlias>>() {});

            private final Deserializer<Void> complexQueryParametersDeserializer =
                    runtime.bodySerDe().emptyBodyDeserializer();

            @Override
            public ListenableFuture<String> string(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.string, _request.build()),
                        stringDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<Integer> integer(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.integer, _request.build()),
                        integerDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<Double> double_(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.double_, _request.build()),
                        double_Deserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<Boolean> boolean_(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.boolean_, _request.build()),
                        boolean_Deserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<SafeLong> safelong(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.safelong, _request.build()),
                        safelongDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<ResourceIdentifier> rid(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.rid, _request.build()),
                        ridDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<BearerToken> bearertoken(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.bearertoken, _request.build()),
                        bearertokenDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<Optional<String>> optionalString(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.optionalString, _request.build()),
                        optionalStringDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<Optional<String>> optionalEmpty(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.optionalEmpty, _request.build()),
                        optionalEmptyDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<OffsetDateTime> datetime(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.datetime, _request.build()),
                        datetimeDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<InputStream> binary(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.binary, _request.build()),
                        runtime.bodySerDe()::deserializeInputStream,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<String> path(AuthHeader authHeader, String param) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.putPathParams("param", plainSerDe.serializeString(param));
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.path, _request.build()),
                        pathDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<Long> externalLongPath(AuthHeader authHeader, long param) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.putPathParams("param", Objects.toString(param));
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.externalLongPath, _request.build()),
                        externalLongPathDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<Optional<Long>> optionalExternalLongQuery(
                    AuthHeader authHeader, Optional<Long> param) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                if (param.isPresent()) {
                    _request.putQueryParams("param", Objects.toString(param.get()));
                }
                return Futures.transform(
                        channel.execute(
                                DialogueEteEndpoints.optionalExternalLongQuery, _request.build()),
                        optionalExternalLongQueryDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<StringAliasExample> notNullBody(
                    AuthHeader authHeader, StringAliasExample notNullBody) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.body(notNullBodySerializer.serialize(notNullBody));
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.notNullBody, _request.build()),
                        notNullBodyDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<StringAliasExample> aliasOne(
                    AuthHeader authHeader, StringAliasExample queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.putQueryParams(
                        "queryParamName", plainSerDe.serializeString(queryParamName.get()));
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.aliasOne, _request.build()),
                        aliasOneDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<StringAliasExample> optionalAliasOne(
                    AuthHeader authHeader, Optional<StringAliasExample> queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                if (queryParamName.isPresent()) {
                    _request.putQueryParams(
                            "queryParamName",
                            plainSerDe.serializeString(queryParamName.get().get()));
                }
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.optionalAliasOne, _request.build()),
                        optionalAliasOneDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<NestedStringAliasExample> aliasTwo(
                    AuthHeader authHeader, NestedStringAliasExample queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.putQueryParams(
                        "queryParamName", plainSerDe.serializeString(queryParamName.get().get()));
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.aliasTwo, _request.build()),
                        aliasTwoDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<StringAliasExample> notNullBodyExternalImport(
                    AuthHeader authHeader, StringAliasExample notNullBody) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.body(notNullBodyExternalImportSerializer.serialize(notNullBody));
                return Futures.transform(
                        channel.execute(
                                DialogueEteEndpoints.notNullBodyExternalImport, _request.build()),
                        notNullBodyExternalImportDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<Optional<StringAliasExample>> optionalBodyExternalImport(
                    AuthHeader authHeader, Optional<StringAliasExample> body) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.body(optionalBodyExternalImportSerializer.serialize(body));
                return Futures.transform(
                        channel.execute(
                                DialogueEteEndpoints.optionalBodyExternalImport, _request.build()),
                        optionalBodyExternalImportDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<Optional<StringAliasExample>> optionalQueryExternalImport(
                    AuthHeader authHeader, Optional<StringAliasExample> query) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                if (query.isPresent()) {
                    _request.putQueryParams("query", Objects.toString(query.get()));
                }
                return Futures.transform(
                        channel.execute(
                                DialogueEteEndpoints.optionalQueryExternalImport, _request.build()),
                        optionalQueryExternalImportDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<Void> noReturn(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.noReturn, _request.build()),
                        noReturnDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<SimpleEnum> enumQuery(
                    AuthHeader authHeader, SimpleEnum queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.putQueryParams("queryParamName", Objects.toString(queryParamName));
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.enumQuery, _request.build()),
                        enumQueryDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<List<SimpleEnum>> enumListQuery(
                    AuthHeader authHeader, List<SimpleEnum> queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                for (SimpleEnum queryParamNameElement : queryParamName) {
                    _request.putQueryParams(
                            "queryParamName", Objects.toString(queryParamNameElement));
                }
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.enumListQuery, _request.build()),
                        enumListQueryDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<Optional<SimpleEnum>> optionalEnumQuery(
                    AuthHeader authHeader, Optional<SimpleEnum> queryParamName) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                if (queryParamName.isPresent()) {
                    _request.putQueryParams(
                            "queryParamName", Objects.toString(queryParamName.get()));
                }
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.optionalEnumQuery, _request.build()),
                        optionalEnumQueryDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<SimpleEnum> enumHeader(
                    AuthHeader authHeader, SimpleEnum headerParameter) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.putHeaderParams("Custom-Header", Objects.toString(headerParameter));
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.enumHeader, _request.build()),
                        enumHeaderDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<Optional<LongAlias>> aliasLongEndpoint(
                    AuthHeader authHeader, Optional<LongAlias> input) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                if (input.isPresent()) {
                    _request.putQueryParams("input", Objects.toString(input.get().get()));
                }
                return Futures.transform(
                        channel.execute(DialogueEteEndpoints.aliasLongEndpoint, _request.build()),
                        aliasLongEndpointDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }

            @Override
            public ListenableFuture<Void> complexQueryParameters(
                    AuthHeader authHeader,
                    ResourceIdentifier datasetRid,
                    Set<StringAliasExample> strings,
                    Set<Long> longs,
                    Set<Integer> ints) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams(
                        "Authorization",
                        plainSerDe.serializeBearerToken(authHeader.getBearerToken()));
                _request.putPathParams("datasetRid", plainSerDe.serializeRid(datasetRid));
                for (StringAliasExample stringsElement : strings) {
                    _request.putQueryParams(
                            "strings", plainSerDe.serializeString(stringsElement.get()));
                }
                for (long longsElement : longs) {
                    _request.putQueryParams("longs", Objects.toString(longsElement));
                }
                for (int intsElement : ints) {
                    _request.putQueryParams("ints", plainSerDe.serializeInteger(intsElement));
                }
                return Futures.transform(
                        channel.execute(
                                DialogueEteEndpoints.complexQueryParameters, _request.build()),
                        complexQueryParametersDeserializer::deserialize,
                        MoreExecutors.directExecutor());
            }
        };
    }
}
