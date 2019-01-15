package com.palantir.conjure.verification.server;

import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.verification.types.AliasString;
import com.palantir.conjure.verification.types.EnumExample;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.BearerToken;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowSingleHeaderService {
    void headerBearertoken(BearerToken header, int index);

    void headerBoolean(boolean header, int index);

    void headerDatetime(OffsetDateTime header, int index);

    void headerDouble(double header, int index);

    void headerInteger(int header, int index);

    void headerRid(ResourceIdentifier header, int index);

    void headerSafelong(SafeLong header, int index);

    void headerString(String header, int index);

    void headerUuid(UUID header, int index);

    void headerOptionalOfString(Optional<String> header, int index);

    void headerAliasString(AliasString header, int index);

    void headerEnumExample(EnumExample header, int index);
}
