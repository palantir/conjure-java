package com.palantir.conjure.verification.server;

import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.verification.types.AliasString;
import com.palantir.conjure.verification.types.EnumExample;
import com.palantir.ri.ResourceIdentifier;
import java.time.OffsetDateTime;
import java.util.UUID;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowSinglePathParamService {
    void pathParamBoolean(int index, boolean param);

    void pathParamDatetime(int index, OffsetDateTime param);

    void pathParamDouble(int index, double param);

    void pathParamInteger(int index, int param);

    void pathParamRid(int index, ResourceIdentifier param);

    void pathParamSafelong(int index, SafeLong param);

    void pathParamString(int index, String param);

    void pathParamUuid(int index, UUID param);

    void pathParamAliasString(int index, AliasString param);

    void pathParamEnumExample(int index, EnumExample param);
}
