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
     * @apiNote GET /base/string
     */
    String string(AuthHeader authHeader);

    /**
     * one <em>two</em> three.
     * @apiNote GET /base/integer
     */
    int integer(AuthHeader authHeader);

    /**
     * @apiNote GET /base/double
     */
    double double_(AuthHeader authHeader);

    /**
     * @apiNote GET /base/boolean
     */
    boolean boolean_(AuthHeader authHeader);

    /**
     * @apiNote GET /base/safelong
     */
    SafeLong safelong(AuthHeader authHeader);

    /**
     * @apiNote GET /base/rid
     */
    ResourceIdentifier rid(AuthHeader authHeader);

    /**
     * @apiNote GET /base/bearertoken
     */
    BearerToken bearertoken(AuthHeader authHeader);

    /**
     * @apiNote GET /base/optionalString
     */
    Optional<String> optionalString(AuthHeader authHeader);

    /**
     * @apiNote GET /base/optionalEmpty
     */
    Optional<String> optionalEmpty(AuthHeader authHeader);

    /**
     * @apiNote GET /base/datetime
     */
    OffsetDateTime datetime(AuthHeader authHeader);

    /**
     * @apiNote GET /base/binary
     */
    BinaryResponseBody binary(AuthHeader authHeader);

    /**
     * Path endpoint.
     * @apiNote GET /base/path/{param}
     * @param param Documentation for <code>param</code>
     */
    String path(AuthHeader authHeader, String param);

    /**
     * @apiNote GET /base/externalLong/{param}
     */
    long externalLongPath(AuthHeader authHeader, long param);

    /**
     * @apiNote GET /base/optionalExternalLong
     */
    Optional<Long> optionalExternalLongQuery(AuthHeader authHeader, Optional<Long> param);

    /**
     * @apiNote POST /base/notNullBody
     */
    StringAliasExample notNullBody(AuthHeader authHeader, StringAliasExample notNullBody);

    /**
     * @apiNote GET /base/aliasOne
     */
    StringAliasExample aliasOne(AuthHeader authHeader, StringAliasExample queryParamName);

    /**
     * @apiNote GET /base/optionalAliasOne
     */
    StringAliasExample optionalAliasOne(AuthHeader authHeader, Optional<StringAliasExample> queryParamName);

    /**
     * @apiNote GET /base/aliasTwo
     */
    NestedStringAliasExample aliasTwo(AuthHeader authHeader, NestedStringAliasExample queryParamName);

    /**
     * @apiNote POST /base/external/notNullBody
     */
    StringAliasExample notNullBodyExternalImport(AuthHeader authHeader, StringAliasExample notNullBody);

    /**
     * @apiNote POST /base/external/optional-body
     */
    Optional<StringAliasExample> optionalBodyExternalImport(AuthHeader authHeader, Optional<StringAliasExample> body);

    /**
     * @apiNote POST /base/external/optional-query
     */
    Optional<StringAliasExample> optionalQueryExternalImport(AuthHeader authHeader, Optional<StringAliasExample> query);

    /**
     * @apiNote POST /base/no-return
     */
    void noReturn(AuthHeader authHeader);

    /**
     * @apiNote GET /base/enum/query
     */
    SimpleEnum enumQuery(AuthHeader authHeader, SimpleEnum queryParamName);

    /**
     * @apiNote GET /base/enum/list/query
     */
    List<SimpleEnum> enumListQuery(AuthHeader authHeader, List<SimpleEnum> queryParamName);

    /**
     * @apiNote GET /base/enum/optional/query
     */
    Optional<SimpleEnum> optionalEnumQuery(AuthHeader authHeader, Optional<SimpleEnum> queryParamName);

    /**
     * @apiNote GET /base/enum/header
     */
    SimpleEnum enumHeader(AuthHeader authHeader, SimpleEnum headerParameter);

    /**
     * @apiNote GET /base/alias-long
     */
    Optional<LongAlias> aliasLongEndpoint(AuthHeader authHeader, Optional<LongAlias> input);

    /**
     * @apiNote GET /base/datasets/{datasetRid}/strings
     */
    void complexQueryParameters(
            AuthHeader authHeader,
            ResourceIdentifier datasetRid,
            Set<StringAliasExample> strings,
            Set<Long> longs,
            Set<Integer> ints);
}
