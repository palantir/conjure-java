package com.palantir.product;

import com.palantir.tokens.auth.AuthHeader;
import java.io.InputStream;
import javax.annotation.Generated;

/** SampleServiceDocs */
@Generated("com.palantir.conjure.java.services.dialogue.DialogueClientGenerator")
public interface BlockingDialogueSampleService {
    /** stringDocs */
    String string(AuthHeader authHeader);

    String stringEcho(AuthHeader authHeader, String string);

    int integer(AuthHeader authHeader);

    int integerEcho(AuthHeader authHeader, int integer);

    String queryEcho(AuthHeader authHeader, int integer);

    Complex complex(AuthHeader authHeader);

    Complex complexEcho(AuthHeader authHeader, Complex complex);

    InputStream binaryEcho(AuthHeader authHeader, String string);
}
