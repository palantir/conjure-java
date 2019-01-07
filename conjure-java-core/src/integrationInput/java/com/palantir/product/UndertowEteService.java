package com.palantir.product;

import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowEteService {
    String string(AuthHeader authHeader);

    int integer(AuthHeader authHeader);

    double double_(AuthHeader authHeader);

    boolean boolean_(AuthHeader authHeader);

    SafeLong safelong(AuthHeader authHeader);

    ResourceIdentifier rid(AuthHeader authHeader);

    BearerToken bearertoken(AuthHeader authHeader);

    Optional<String> optionalString(AuthHeader authHeader);

    Optional<String> optionalEmpty(AuthHeader authHeader);

    OffsetDateTime datetime(AuthHeader authHeader);

    BinaryResponseBody binary(AuthHeader authHeader);

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
}
