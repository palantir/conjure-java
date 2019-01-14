package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.tokens.auth.AuthHeader;
import javax.annotation.Generated;

/** SampleServiceDocs */
@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowDialogueSampleService {
    /** stringDocs */
    String string(AuthHeader authHeader);

    String stringEcho(AuthHeader authHeader, String string);

    int integer(AuthHeader authHeader);

    int integerEcho(AuthHeader authHeader, int integer);

    String queryEcho(AuthHeader authHeader, int integer);

    Complex complex(AuthHeader authHeader);

    Complex complexEcho(AuthHeader authHeader, Complex complex);

    BinaryResponseBody binaryEcho(AuthHeader authHeader, String string);
}
