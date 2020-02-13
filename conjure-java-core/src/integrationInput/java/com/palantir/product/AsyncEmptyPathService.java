package com.palantir.product;

import com.google.common.util.concurrent.ListenableFuture;
import java.lang.Boolean;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueServiceGenerator")
public interface AsyncEmptyPathService {
    ListenableFuture<Boolean> emptyPath();
}
