package com.palantir.product;

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
     *
     * <h2>Very Important Documentation</h2>
     *
     * <p>This documentation provides a <em>list</em>:
     *
     * <ul>
     *   <li>Docs rule
     *   <li>Lists are wonderful
     * </ul>
     */
    String string(AuthHeader authHeader);

    /** one <em>two</em> three. */
    int integer(AuthHeader authHeader);

    double double_(AuthHeader authHeader);

    boolean boolean_(AuthHeader authHeader);

    SafeLong safelong(AuthHeader authHeader);

    ResourceIdentifier rid(AuthHeader authHeader);

    BearerToken bearertoken(AuthHeader authHeader);

    Optional<String> optionalString(AuthHeader authHeader);

    Optional<String> optionalEmpty(AuthHeader authHeader);

    OffsetDateTime datetime(AuthHeader authHeader);

    InputStream binary(AuthHeader authHeader);

    /**
     * Path endpoint.
     *
     * @param param Documentation for <code>param</code>
     */
    String path(AuthHeader authHeader, String param);

    long externalLongPath(AuthHeader authHeader, long param);

    Optional<Long> optionalExternalLongQuery(AuthHeader authHeader, Optional<Long> param);

    StringAliasExample notNullBody(AuthHeader authHeader, StringAliasExample notNullBody);

    StringAliasExample aliasOne(AuthHeader authHeader, StringAliasExample queryParamName);

    StringAliasExample optionalAliasOne(
            AuthHeader authHeader, Optional<StringAliasExample> queryParamName);

    NestedStringAliasExample aliasTwo(
            AuthHeader authHeader, NestedStringAliasExample queryParamName);

    StringAliasExample notNullBodyExternalImport(
            AuthHeader authHeader, StringAliasExample notNullBody);

    Optional<StringAliasExample> optionalBodyExternalImport(
            AuthHeader authHeader, Optional<StringAliasExample> body);

    Optional<StringAliasExample> optionalQueryExternalImport(
            AuthHeader authHeader, Optional<StringAliasExample> query);

    void noReturn(AuthHeader authHeader);

    SimpleEnum enumQuery(AuthHeader authHeader, SimpleEnum queryParamName);

    List<SimpleEnum> enumListQuery(AuthHeader authHeader, List<SimpleEnum> queryParamName);

    Optional<SimpleEnum> optionalEnumQuery(
            AuthHeader authHeader, Optional<SimpleEnum> queryParamName);

    SimpleEnum enumHeader(AuthHeader authHeader, SimpleEnum headerParameter);

    Optional<LongAlias> aliasLongEndpoint(AuthHeader authHeader, Optional<LongAlias> input);

    void complexQueryParameters(
            AuthHeader authHeader,
            ResourceIdentifier datasetRid,
            Set<StringAliasExample> strings,
            Set<Long> longs,
            Set<Integer> ints);

    /** Creates a synchronous/blocking client for a EteService service. */
    static EteServiceBlocking of(Channel channel, ConjureRuntime runtime) {
        EteServiceAsync delegate = EteServiceAsync.of(channel, runtime);
        return new EteServiceBlocking() {
            @Override
            public String string(AuthHeader authHeader) {
                return runtime.clients().block(delegate.string(authHeader));
            }

            @Override
            public int integer(AuthHeader authHeader) {
                return runtime.clients().block(delegate.integer(authHeader));
            }

            @Override
            public double double_(AuthHeader authHeader) {
                return runtime.clients().block(delegate.double_(authHeader));
            }

            @Override
            public boolean boolean_(AuthHeader authHeader) {
                return runtime.clients().block(delegate.boolean_(authHeader));
            }

            @Override
            public SafeLong safelong(AuthHeader authHeader) {
                return runtime.clients().block(delegate.safelong(authHeader));
            }

            @Override
            public ResourceIdentifier rid(AuthHeader authHeader) {
                return runtime.clients().block(delegate.rid(authHeader));
            }

            @Override
            public BearerToken bearertoken(AuthHeader authHeader) {
                return runtime.clients().block(delegate.bearertoken(authHeader));
            }

            @Override
            public Optional<String> optionalString(AuthHeader authHeader) {
                return runtime.clients().block(delegate.optionalString(authHeader));
            }

            @Override
            public Optional<String> optionalEmpty(AuthHeader authHeader) {
                return runtime.clients().block(delegate.optionalEmpty(authHeader));
            }

            @Override
            public OffsetDateTime datetime(AuthHeader authHeader) {
                return runtime.clients().block(delegate.datetime(authHeader));
            }

            @Override
            public InputStream binary(AuthHeader authHeader) {
                return runtime.clients().block(delegate.binary(authHeader));
            }

            @Override
            public String path(AuthHeader authHeader, String param) {
                return runtime.clients().block(delegate.path(authHeader, param));
            }

            @Override
            public long externalLongPath(AuthHeader authHeader, long param) {
                return runtime.clients().block(delegate.externalLongPath(authHeader, param));
            }

            @Override
            public Optional<Long> optionalExternalLongQuery(
                    AuthHeader authHeader, Optional<Long> param) {
                return runtime.clients()
                        .block(delegate.optionalExternalLongQuery(authHeader, param));
            }

            @Override
            public StringAliasExample notNullBody(
                    AuthHeader authHeader, StringAliasExample notNullBody) {
                return runtime.clients().block(delegate.notNullBody(authHeader, notNullBody));
            }

            @Override
            public StringAliasExample aliasOne(
                    AuthHeader authHeader, StringAliasExample queryParamName) {
                return runtime.clients().block(delegate.aliasOne(authHeader, queryParamName));
            }

            @Override
            public StringAliasExample optionalAliasOne(
                    AuthHeader authHeader, Optional<StringAliasExample> queryParamName) {
                return runtime.clients()
                        .block(delegate.optionalAliasOne(authHeader, queryParamName));
            }

            @Override
            public NestedStringAliasExample aliasTwo(
                    AuthHeader authHeader, NestedStringAliasExample queryParamName) {
                return runtime.clients().block(delegate.aliasTwo(authHeader, queryParamName));
            }

            @Override
            public StringAliasExample notNullBodyExternalImport(
                    AuthHeader authHeader, StringAliasExample notNullBody) {
                return runtime.clients()
                        .block(delegate.notNullBodyExternalImport(authHeader, notNullBody));
            }

            @Override
            public Optional<StringAliasExample> optionalBodyExternalImport(
                    AuthHeader authHeader, Optional<StringAliasExample> body) {
                return runtime.clients()
                        .block(delegate.optionalBodyExternalImport(authHeader, body));
            }

            @Override
            public Optional<StringAliasExample> optionalQueryExternalImport(
                    AuthHeader authHeader, Optional<StringAliasExample> query) {
                return runtime.clients()
                        .block(delegate.optionalQueryExternalImport(authHeader, query));
            }

            @Override
            public void noReturn(AuthHeader authHeader) {
                runtime.clients().block(delegate.noReturn(authHeader));
            }

            @Override
            public SimpleEnum enumQuery(AuthHeader authHeader, SimpleEnum queryParamName) {
                return runtime.clients().block(delegate.enumQuery(authHeader, queryParamName));
            }

            @Override
            public List<SimpleEnum> enumListQuery(
                    AuthHeader authHeader, List<SimpleEnum> queryParamName) {
                return runtime.clients().block(delegate.enumListQuery(authHeader, queryParamName));
            }

            @Override
            public Optional<SimpleEnum> optionalEnumQuery(
                    AuthHeader authHeader, Optional<SimpleEnum> queryParamName) {
                return runtime.clients()
                        .block(delegate.optionalEnumQuery(authHeader, queryParamName));
            }

            @Override
            public SimpleEnum enumHeader(AuthHeader authHeader, SimpleEnum headerParameter) {
                return runtime.clients().block(delegate.enumHeader(authHeader, headerParameter));
            }

            @Override
            public Optional<LongAlias> aliasLongEndpoint(
                    AuthHeader authHeader, Optional<LongAlias> input) {
                return runtime.clients().block(delegate.aliasLongEndpoint(authHeader, input));
            }

            @Override
            public void complexQueryParameters(
                    AuthHeader authHeader,
                    ResourceIdentifier datasetRid,
                    Set<StringAliasExample> strings,
                    Set<Long> longs,
                    Set<Integer> ints) {
                runtime.clients()
                        .block(
                                delegate.complexQueryParameters(
                                        authHeader, datasetRid, strings, longs, ints));
            }
        };
    }
}
