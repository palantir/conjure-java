package com.palantir.product;

import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.lib.internal.ConjureEndpoint;
import com.palantir.conjure.java.lib.internal.ConjureService;
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
@ConjureService(name = "EteService", package_ = "com.palantir.product")
public interface UndertowEteService {
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
    @ConjureEndpoint(path = "/base/string", method = "GET")
    String string(AuthHeader authHeader);

    /**
     * one <em>two</em> three.
     * @apiNote {@code GET /base/integer}
     */
    @ConjureEndpoint(path = "/base/integer", method = "GET")
    int integer(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/double}
     */
    @ConjureEndpoint(path = "/base/double", method = "GET")
    double double_(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/boolean}
     */
    @ConjureEndpoint(path = "/base/boolean", method = "GET")
    boolean boolean_(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/safelong}
     */
    @ConjureEndpoint(path = "/base/safelong", method = "GET")
    SafeLong safelong(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/rid}
     */
    @ConjureEndpoint(path = "/base/rid", method = "GET")
    ResourceIdentifier rid(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/bearertoken}
     */
    @ConjureEndpoint(path = "/base/bearertoken", method = "GET")
    BearerToken bearertoken(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/optionalString}
     */
    @ConjureEndpoint(path = "/base/optionalString", method = "GET")
    Optional<String> optionalString(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/optionalEmpty}
     */
    @ConjureEndpoint(path = "/base/optionalEmpty", method = "GET")
    Optional<String> optionalEmpty(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/datetime}
     */
    @ConjureEndpoint(path = "/base/datetime", method = "GET")
    OffsetDateTime datetime(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/binary}
     */
    @ConjureEndpoint(path = "/base/binary", method = "GET")
    BinaryResponseBody binary(AuthHeader authHeader);

    /**
     * Path endpoint.
     * @apiNote {@code GET /base/path/{param}}
     * @param param Documentation for <code>param</code>
     */
    @ConjureEndpoint(path = "/base/path/{param}", method = "GET")
    String path(AuthHeader authHeader, String param);

    /**
     * @apiNote {@code GET /base/externalLong/{param}}
     */
    @ConjureEndpoint(path = "/base/externalLong/{param}", method = "GET")
    long externalLongPath(AuthHeader authHeader, long param);

    /**
     * @apiNote {@code GET /base/optionalExternalLong}
     */
    @ConjureEndpoint(path = "/base/optionalExternalLong", method = "GET")
    Optional<Long> optionalExternalLongQuery(AuthHeader authHeader, Optional<Long> param);

    /**
     * @apiNote {@code POST /base/notNullBody}
     */
    @ConjureEndpoint(path = "/base/notNullBody", method = "POST")
    StringAliasExample notNullBody(AuthHeader authHeader, StringAliasExample notNullBody);

    /**
     * @apiNote {@code GET /base/aliasOne}
     */
    @ConjureEndpoint(path = "/base/aliasOne", method = "GET")
    StringAliasExample aliasOne(AuthHeader authHeader, StringAliasExample queryParamName);

    /**
     * @apiNote {@code GET /base/optionalAliasOne}
     */
    @ConjureEndpoint(path = "/base/optionalAliasOne", method = "GET")
    StringAliasExample optionalAliasOne(AuthHeader authHeader, Optional<StringAliasExample> queryParamName);

    /**
     * @apiNote {@code GET /base/aliasTwo}
     */
    @ConjureEndpoint(path = "/base/aliasTwo", method = "GET")
    NestedStringAliasExample aliasTwo(AuthHeader authHeader, NestedStringAliasExample queryParamName);

    /**
     * @apiNote {@code POST /base/external/notNullBody}
     */
    @ConjureEndpoint(path = "/base/external/notNullBody", method = "POST")
    StringAliasExample notNullBodyExternalImport(AuthHeader authHeader, StringAliasExample notNullBody);

    /**
     * @apiNote {@code POST /base/external/optional-body}
     */
    @ConjureEndpoint(path = "/base/external/optional-body", method = "POST")
    Optional<StringAliasExample> optionalBodyExternalImport(AuthHeader authHeader, Optional<StringAliasExample> body);

    /**
     * @apiNote {@code POST /base/external/optional-query}
     */
    @ConjureEndpoint(path = "/base/external/optional-query", method = "POST")
    Optional<StringAliasExample> optionalQueryExternalImport(AuthHeader authHeader, Optional<StringAliasExample> query);

    /**
     * @apiNote {@code POST /base/no-return}
     */
    @ConjureEndpoint(path = "/base/no-return", method = "POST")
    void noReturn(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /base/enum/query}
     */
    @ConjureEndpoint(path = "/base/enum/query", method = "GET")
    SimpleEnum enumQuery(AuthHeader authHeader, SimpleEnum queryParamName);

    /**
     * @apiNote {@code GET /base/enum/list/query}
     */
    @ConjureEndpoint(path = "/base/enum/list/query", method = "GET")
    List<SimpleEnum> enumListQuery(AuthHeader authHeader, List<SimpleEnum> queryParamName);

    /**
     * @apiNote {@code GET /base/enum/optional/query}
     */
    @ConjureEndpoint(path = "/base/enum/optional/query", method = "GET")
    Optional<SimpleEnum> optionalEnumQuery(AuthHeader authHeader, Optional<SimpleEnum> queryParamName);

    /**
     * @apiNote {@code GET /base/enum/header}
     */
    @ConjureEndpoint(path = "/base/enum/header", method = "GET")
    SimpleEnum enumHeader(AuthHeader authHeader, SimpleEnum headerParameter);

    /**
     * @apiNote {@code GET /base/alias-long}
     */
    @ConjureEndpoint(path = "/base/alias-long", method = "GET")
    Optional<LongAlias> aliasLongEndpoint(AuthHeader authHeader, Optional<LongAlias> input);

    /**
     * @apiNote {@code GET /base/datasets/{datasetRid}/strings}
     */
    @ConjureEndpoint(path = "/base/datasets/{datasetRid}/strings", method = "GET")
    void complexQueryParameters(
            AuthHeader authHeader,
            ResourceIdentifier datasetRid,
            Set<StringAliasExample> strings,
            Set<Long> longs,
            Set<Integer> ints);

    /**
     * @apiNote {@code PUT /base/list/optionals}
     */
    @ConjureEndpoint(path = "/base/list/optionals", method = "PUT")
    void receiveListOfOptionals(AuthHeader authHeader, List<Optional<String>> value);

    /**
     * @apiNote {@code PUT /base/set/optionals}
     */
    @ConjureEndpoint(path = "/base/set/optionals", method = "PUT")
    void receiveSetOfOptionals(AuthHeader authHeader, Set<Optional<String>> value);
}
