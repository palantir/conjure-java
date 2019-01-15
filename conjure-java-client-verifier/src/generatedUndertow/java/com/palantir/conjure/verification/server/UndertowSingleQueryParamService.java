package com.palantir.conjure.verification.server;

import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.verification.types.AliasString;
import com.palantir.conjure.verification.types.EnumExample;
import com.palantir.ri.ResourceIdentifier;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowSingleQueryParamService {
    void queryParamBoolean(int index, boolean someQuery);

    void queryParamDouble(int index, double someQuery);

    void queryParamInteger(int index, int someQuery);

    void queryParamRid(int index, ResourceIdentifier someQuery);

    void queryParamSafelong(int index, SafeLong someQuery);

    void queryParamString(int index, String someQuery);

    void queryParamUuid(int index, UUID someQuery);

    void queryParamOptionalOfString(int index, Optional<String> someQuery);

    void queryParamAliasString(int index, AliasString someQuery);

    void queryParamEnumExample(int index, EnumExample someQuery);
}
