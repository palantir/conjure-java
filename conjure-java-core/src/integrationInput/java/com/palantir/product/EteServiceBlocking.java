package com.palantir.product;

import com.google.errorprone.annotations.MustBeClosed;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
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
     * GET /base/string
     */
    String string(AuthHeader authHeader);

    /**
     * one <em>two</em> three.
     * GET /base/integer
     */
    int integer(AuthHeader authHeader);

    /**
     * GET /base/double
     */
    double double_(AuthHeader authHeader);

    /**
     * GET /base/boolean
     */
    boolean boolean_(AuthHeader authHeader);

    /**
     * GET /base/safelong
     */
    SafeLong safelong(AuthHeader authHeader);

    /**
     * GET /base/rid
     */
    ResourceIdentifier rid(AuthHeader authHeader);

    /**
     * GET /base/bearertoken
     */
    BearerToken bearertoken(AuthHeader authHeader);

    /**
     * GET /base/optionalString
     */
    Optional<String> optionalString(AuthHeader authHeader);

    /**
     * GET /base/optionalEmpty
     */
    Optional<String> optionalEmpty(AuthHeader authHeader);

    /**
     * GET /base/datetime
     */
    OffsetDateTime datetime(AuthHeader authHeader);

    /**
     * GET /base/binary
     */
    InputStream binary(AuthHeader authHeader);

    /**
     * Path endpoint.
     * GET /base/path/{param}
     * @param param Documentation for <code>param</code>
     */
    String path(AuthHeader authHeader, String param);

    /**
     * GET /base/externalLong/{param}
     */
    long externalLongPath(AuthHeader authHeader, long param);

    /**
     * GET /base/optionalExternalLong
     */
    Optional<Long> optionalExternalLongQuery(AuthHeader authHeader, Optional<Long> param);

    /**
     * POST /base/notNullBody
     */
    StringAliasExample notNullBody(AuthHeader authHeader, StringAliasExample notNullBody);

    /**
     * GET /base/aliasOne
     */
    StringAliasExample aliasOne(AuthHeader authHeader, StringAliasExample queryParamName);

    /**
     * GET /base/optionalAliasOne
     */
    StringAliasExample optionalAliasOne(AuthHeader authHeader, Optional<StringAliasExample> queryParamName);

    /**
     * GET /base/aliasTwo
     */
    NestedStringAliasExample aliasTwo(AuthHeader authHeader, NestedStringAliasExample queryParamName);

    /**
     * POST /base/external/notNullBody
     */
    StringAliasExample notNullBodyExternalImport(AuthHeader authHeader, StringAliasExample notNullBody);

    /**
     * POST /base/external/optional-body
     */
    Optional<StringAliasExample> optionalBodyExternalImport(AuthHeader authHeader, Optional<StringAliasExample> body);

    /**
     * POST /base/external/optional-query
     */
    Optional<StringAliasExample> optionalQueryExternalImport(AuthHeader authHeader, Optional<StringAliasExample> query);

    /**
     * POST /base/no-return
     */
    void noReturn(AuthHeader authHeader);

    /**
     * GET /base/enum/query
     */
    SimpleEnum enumQuery(AuthHeader authHeader, SimpleEnum queryParamName);

    /**
     * GET /base/enum/list/query
     */
    List<SimpleEnum> enumListQuery(AuthHeader authHeader, List<SimpleEnum> queryParamName);

    /**
     * GET /base/enum/optional/query
     */
    Optional<SimpleEnum> optionalEnumQuery(AuthHeader authHeader, Optional<SimpleEnum> queryParamName);

    /**
     * GET /base/enum/header
     */
    SimpleEnum enumHeader(AuthHeader authHeader, SimpleEnum headerParameter);

    /**
     * GET /base/alias-long
     */
    Optional<LongAlias> aliasLongEndpoint(AuthHeader authHeader, Optional<LongAlias> input);

    /**
     * GET /base/datasets/{datasetRid}/strings
     */
    void complexQueryParameters(
            AuthHeader authHeader,
            ResourceIdentifier datasetRid,
            Set<StringAliasExample> strings,
            Set<Long> longs,
            Set<Integer> ints);

    /**
     * Creates a synchronous/blocking client for a EteService service.
     */
    static EteServiceBlocking of(Channel _channel, ConjureRuntime _runtime) {
        EteServiceAsync delegate = EteServiceAsync.of(_channel, _runtime);
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
            @MustBeClosed
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
            public String toString() {
                return "EteServiceBlocking{channel=" + _channel + ", runtime=" + _runtime + '}';
            }
        };
    }
}
