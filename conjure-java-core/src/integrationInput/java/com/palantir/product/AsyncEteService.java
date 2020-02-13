package com.palantir.product;

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import java.io.InputStream;
import java.lang.Boolean;
import java.lang.Double;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.lang.Void;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueServiceGenerator")
public interface AsyncEteService {
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
    ListenableFuture<String> string(AuthHeader authHeader);

    /** one <em>two</em> three. */
    ListenableFuture<Integer> integer(AuthHeader authHeader);

    ListenableFuture<Double> double_(AuthHeader authHeader);

    ListenableFuture<Boolean> boolean_(AuthHeader authHeader);

    ListenableFuture<SafeLong> safelong(AuthHeader authHeader);

    ListenableFuture<ResourceIdentifier> rid(AuthHeader authHeader);

    ListenableFuture<BearerToken> bearertoken(AuthHeader authHeader);

    ListenableFuture<Optional<String>> optionalString(AuthHeader authHeader);

    ListenableFuture<Optional<String>> optionalEmpty(AuthHeader authHeader);

    ListenableFuture<OffsetDateTime> datetime(AuthHeader authHeader);

    ListenableFuture<InputStream> binary(AuthHeader authHeader);

    /**
     * Path endpoint.
     *
     * @param param Documentation for <code>param</code>
     */
    ListenableFuture<String> path(AuthHeader authHeader, String param);

    ListenableFuture<Long> externalLongPath(AuthHeader authHeader, long param);

    ListenableFuture<Optional<Long>> optionalExternalLongQuery(
            AuthHeader authHeader, Optional<Long> param);

    ListenableFuture<StringAliasExample> notNullBody(
            AuthHeader authHeader, StringAliasExample notNullBody);

    ListenableFuture<StringAliasExample> aliasOne(
            AuthHeader authHeader, StringAliasExample queryParamName);

    ListenableFuture<StringAliasExample> optionalAliasOne(
            AuthHeader authHeader, Optional<StringAliasExample> queryParamName);

    ListenableFuture<NestedStringAliasExample> aliasTwo(
            AuthHeader authHeader, NestedStringAliasExample queryParamName);

    ListenableFuture<StringAliasExample> notNullBodyExternalImport(
            AuthHeader authHeader, StringAliasExample notNullBody);

    ListenableFuture<Optional<StringAliasExample>> optionalBodyExternalImport(
            AuthHeader authHeader, Optional<StringAliasExample> body);

    ListenableFuture<Optional<StringAliasExample>> optionalQueryExternalImport(
            AuthHeader authHeader, Optional<StringAliasExample> query);

    ListenableFuture<Void> noReturn(AuthHeader authHeader);

    ListenableFuture<SimpleEnum> enumQuery(AuthHeader authHeader, SimpleEnum queryParamName);

    ListenableFuture<List<SimpleEnum>> enumListQuery(
            AuthHeader authHeader, List<SimpleEnum> queryParamName);

    ListenableFuture<Optional<SimpleEnum>> optionalEnumQuery(
            AuthHeader authHeader, Optional<SimpleEnum> queryParamName);

    ListenableFuture<SimpleEnum> enumHeader(AuthHeader authHeader, SimpleEnum headerParameter);

    ListenableFuture<Optional<LongAlias>> aliasLongEndpoint(
            AuthHeader authHeader, Optional<LongAlias> input);

    ListenableFuture<Void> complexQueryParameters(
            AuthHeader authHeader,
            ResourceIdentifier datasetRid,
            Set<StringAliasExample> strings,
            Set<Long> longs,
            Set<Integer> ints);
}
