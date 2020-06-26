package com.palantir.product;

import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowEteService {
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
    BinaryResponseBody binary(AuthHeader authHeader);

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
}
