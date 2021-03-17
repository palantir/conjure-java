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
    BinaryResponseBody binary(AuthHeader authHeader);

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
     * @apiNote {@code PUT /base/errors}
     */
    void throwsCheckedException(AuthHeader authHeader) throws ExampleErrors.ExampleErrorException;
}
