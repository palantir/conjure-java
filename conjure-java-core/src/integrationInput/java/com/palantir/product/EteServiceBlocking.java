package com.palantir.product;

import com.google.errorprone.annotations.MustBeClosed;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.EndpointChannel;
import com.palantir.dialogue.EndpointChannelFactory;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import java.io.InputStream;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
public interface EteServiceBlocking {
    /**
     * foo bar baz.
     * <h2>Very Important Documentation</h2>
     * <p>This documentation provides a <em>list</em>:</p>
     * <ul>
     * <li>Docs rule</li>
     * <li>Lists are wonderful</li>
     * </ul>
     * @apiNote {@code GET /base/string}
     */
    String string(AuthHeader authHeader);

    /**
     * one <em>two</em> three.
     * @apiNote {@code GET /base/integer}
     */
    int integer(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/double}
     */
    double double_(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/boolean}
     */
    boolean boolean_(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/safelong}
     */
    SafeLong safelong(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/rid}
     */
    ResourceIdentifier rid(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/bearertoken}
     */
    BearerToken bearertoken(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/optionalString}
     */
    Optional<String> optionalString(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/optionalEmpty}
     */
    Optional<String> optionalEmpty(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/datetime}
     */
    OffsetDateTime datetime(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/binary}
     */
    @MustBeClosed
    InputStream binary(AuthHeader authHeader);

    /**
     * Path endpoint.
     * @apiNote {@code GET /base/path/{param}}
     * @param param Documentation for <code>param</code>
     */
    String path(AuthHeader authHeader, String param);

    /**
     * @apiNote {@code GET /base/externalLong/{param}}
     */
    long externalLongPath(AuthHeader authHeader, long param);

    /**
     * @apiNote {@code GET /base/optionalExternalLong}
     */
    Optional<Long> optionalExternalLongQuery(AuthHeader authHeader, Optional<Long> param);

    /**
     * @apiNote {@code POST /base/notNullBody}
     */
    StringAliasExample notNullBody(AuthHeader authHeader, StringAliasExample notNullBody);

    /**
     * @apiNote {@code GET /base/aliasOne}
     */
    StringAliasExample aliasOne(AuthHeader authHeader, StringAliasExample queryParamName);

    /**
     * @apiNote {@code GET /base/optionalAliasOne}
     */
    StringAliasExample optionalAliasOne(AuthHeader authHeader, Optional<StringAliasExample> queryParamName);

    /**
     * @apiNote {@code GET /base/aliasTwo}
     */
    NestedStringAliasExample aliasTwo(AuthHeader authHeader, NestedStringAliasExample queryParamName);

    /**
     * @apiNote {@code POST /base/external/notNullBody}
     */
    StringAliasExample notNullBodyExternalImport(AuthHeader authHeader, StringAliasExample notNullBody);

    /**
     * @apiNote {@code POST /base/external/optional-body}
     */
    Optional<StringAliasExample> optionalBodyExternalImport(AuthHeader authHeader, Optional<StringAliasExample> body);

    /**
     * @apiNote {@code POST /base/external/optional-query}
     */
    Optional<StringAliasExample> optionalQueryExternalImport(AuthHeader authHeader, Optional<StringAliasExample> query);

    /**
     * @apiNote {@code POST /base/no-return}
     */
    void noReturn(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/enum/query}
     */
    SimpleEnum enumQuery(AuthHeader authHeader, SimpleEnum queryParamName);

    /**
     * @apiNote {@code GET /base/enum/list/query}
     */
    List<SimpleEnum> enumListQuery(AuthHeader authHeader, List<SimpleEnum> queryParamName);

    /**
     * @apiNote {@code GET /base/enum/optional/query}
     */
    Optional<SimpleEnum> optionalEnumQuery(AuthHeader authHeader, Optional<SimpleEnum> queryParamName);

    /**
     * @apiNote {@code GET /base/enum/header}
     */
    SimpleEnum enumHeader(AuthHeader authHeader, SimpleEnum headerParameter);

    /**
     * @apiNote {@code GET /base/alias-long}
     */
    Optional<LongAlias> aliasLongEndpoint(AuthHeader authHeader, Optional<LongAlias> input);

    /**
     * @apiNote {@code GET /base/datasets/{datasetRid}/strings}
     */
    void complexQueryParameters(
            AuthHeader authHeader,
            ResourceIdentifier datasetRid,
            Set<StringAliasExample> strings,
            Set<Long> longs,
            Set<Integer> ints);

    /**
     * @apiNote {@code PUT /base/list/optionals}
     */
    void receiveListOfOptionals(AuthHeader authHeader, List<Optional<String>> value);

    /**
     * @apiNote {@code PUT /base/set/optionals}
     */
    void receiveSetOfOptionals(AuthHeader authHeader, Set<Optional<String>> value);

    /**
     * Creates a synchronous/blocking client for a EteService service.
     */
    static EteServiceBlocking of(EndpointChannelFactory _endpointChannelFactory, ConjureRuntime _runtime) {
        EteServiceAsync delegate = EteServiceAsync.of(_endpointChannelFactory, _runtime);
        return new EteServiceBlocking() {
            @Override
            public String string(AuthHeader authHeader) {
                return _runtime.clients().block(delegate.string(authHeader));
            }

            @Override
            public int integer(AuthHeader authHeader) {
                return _runtime.clients().block(delegate.integer(authHeader));
            }

            @Override
            public double double_(AuthHeader authHeader) {
                return _runtime.clients().block(delegate.double_(authHeader));
            }

            @Override
            public boolean boolean_(AuthHeader authHeader) {
                return _runtime.clients().block(delegate.boolean_(authHeader));
            }

            @Override
            public SafeLong safelong(AuthHeader authHeader) {
                return _runtime.clients().block(delegate.safelong(authHeader));
            }

            @Override
            public ResourceIdentifier rid(AuthHeader authHeader) {
                return _runtime.clients().block(delegate.rid(authHeader));
            }

            @Override
            public BearerToken bearertoken(AuthHeader authHeader) {
                return _runtime.clients().block(delegate.bearertoken(authHeader));
            }

            @Override
            public Optional<String> optionalString(AuthHeader authHeader) {
                return _runtime.clients().block(delegate.optionalString(authHeader));
            }

            @Override
            public Optional<String> optionalEmpty(AuthHeader authHeader) {
                return _runtime.clients().block(delegate.optionalEmpty(authHeader));
            }

            @Override
            public OffsetDateTime datetime(AuthHeader authHeader) {
                return _runtime.clients().block(delegate.datetime(authHeader));
            }

            @Override
            public InputStream binary(AuthHeader authHeader) {
                return _runtime.clients().block(delegate.binary(authHeader));
            }

            @Override
            public String path(AuthHeader authHeader, String param) {
                return _runtime.clients().block(delegate.path(authHeader, param));
            }

            @Override
            public long externalLongPath(AuthHeader authHeader, long param) {
                return _runtime.clients().block(delegate.externalLongPath(authHeader, param));
            }

            @Override
            public Optional<Long> optionalExternalLongQuery(AuthHeader authHeader, Optional<Long> param) {
                return _runtime.clients().block(delegate.optionalExternalLongQuery(authHeader, param));
            }

            @Override
            public StringAliasExample notNullBody(AuthHeader authHeader, StringAliasExample notNullBody) {
                return _runtime.clients().block(delegate.notNullBody(authHeader, notNullBody));
            }

            @Override
            public StringAliasExample aliasOne(AuthHeader authHeader, StringAliasExample queryParamName) {
                return _runtime.clients().block(delegate.aliasOne(authHeader, queryParamName));
            }

            @Override
            public StringAliasExample optionalAliasOne(
                    AuthHeader authHeader, Optional<StringAliasExample> queryParamName) {
                return _runtime.clients().block(delegate.optionalAliasOne(authHeader, queryParamName));
            }

            @Override
            public NestedStringAliasExample aliasTwo(AuthHeader authHeader, NestedStringAliasExample queryParamName) {
                return _runtime.clients().block(delegate.aliasTwo(authHeader, queryParamName));
            }

            @Override
            public StringAliasExample notNullBodyExternalImport(AuthHeader authHeader, StringAliasExample notNullBody) {
                return _runtime.clients().block(delegate.notNullBodyExternalImport(authHeader, notNullBody));
            }

            @Override
            public Optional<StringAliasExample> optionalBodyExternalImport(
                    AuthHeader authHeader, Optional<StringAliasExample> body) {
                return _runtime.clients().block(delegate.optionalBodyExternalImport(authHeader, body));
            }

            @Override
            public Optional<StringAliasExample> optionalQueryExternalImport(
                    AuthHeader authHeader, Optional<StringAliasExample> query) {
                return _runtime.clients().block(delegate.optionalQueryExternalImport(authHeader, query));
            }

            @Override
            public void noReturn(AuthHeader authHeader) {
                _runtime.clients().block(delegate.noReturn(authHeader));
            }

            @Override
            public SimpleEnum enumQuery(AuthHeader authHeader, SimpleEnum queryParamName) {
                return _runtime.clients().block(delegate.enumQuery(authHeader, queryParamName));
            }

            @Override
            public List<SimpleEnum> enumListQuery(AuthHeader authHeader, List<SimpleEnum> queryParamName) {
                return _runtime.clients().block(delegate.enumListQuery(authHeader, queryParamName));
            }

            @Override
            public Optional<SimpleEnum> optionalEnumQuery(AuthHeader authHeader, Optional<SimpleEnum> queryParamName) {
                return _runtime.clients().block(delegate.optionalEnumQuery(authHeader, queryParamName));
            }

            @Override
            public SimpleEnum enumHeader(AuthHeader authHeader, SimpleEnum headerParameter) {
                return _runtime.clients().block(delegate.enumHeader(authHeader, headerParameter));
            }

            @Override
            public Optional<LongAlias> aliasLongEndpoint(AuthHeader authHeader, Optional<LongAlias> input) {
                return _runtime.clients().block(delegate.aliasLongEndpoint(authHeader, input));
            }

            @Override
            public void complexQueryParameters(
                    AuthHeader authHeader,
                    ResourceIdentifier datasetRid,
                    Set<StringAliasExample> strings,
                    Set<Long> longs,
                    Set<Integer> ints) {
                _runtime.clients().block(delegate.complexQueryParameters(authHeader, datasetRid, strings, longs, ints));
            }

            @Override
            public void receiveListOfOptionals(AuthHeader authHeader, List<Optional<String>> value) {
                _runtime.clients().block(delegate.receiveListOfOptionals(authHeader, value));
            }

            @Override
            public void receiveSetOfOptionals(AuthHeader authHeader, Set<Optional<String>> value) {
                _runtime.clients().block(delegate.receiveSetOfOptionals(authHeader, value));
            }

            @Override
            public String toString() {
                return "EteServiceBlocking{_endpointChannelFactory=" + _endpointChannelFactory + ", runtime=" + _runtime
                        + '}';
            }
        };
    }

    /**
     * Creates an asynchronous/non-blocking client for a EteService service.
     */
    static EteServiceBlocking of(Channel _channel, ConjureRuntime _runtime) {
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
