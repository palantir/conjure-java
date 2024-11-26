package com.palantir.product;

import java.lang.String;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueEndpointResultTypeGenerator")
public final class DialogueErrorResultTypes {
    public sealed interface TestBasicErrorResult
            permits TestBasicErrorResult.Success, TestBasicErrorResult.Test_InvalidArgument {
        record Success(String value) implements TestBasicErrorResult {}

        record Test_InvalidArgument() implements TestBasicErrorResult {}
    }

    public sealed interface TestImportedErrorResult
            permits TestImportedErrorResult.Success, TestImportedErrorResult.EndpointSpecific_EndpointError {
        record Success(String value) implements TestImportedErrorResult {}

        record EndpointSpecific_EndpointError() implements TestImportedErrorResult {}
    }

    public sealed interface TestMultipleErrorsAndPackagesResult
            permits TestMultipleErrorsAndPackagesResult.Success,
                    TestMultipleErrorsAndPackagesResult.Test_InvalidArgument,
                    TestMultipleErrorsAndPackagesResult.Test_NotFound,
                    TestMultipleErrorsAndPackagesResult.EndpointSpecificTwo_DifferentNamespace,
                    TestMultipleErrorsAndPackagesResult.EndpointSpecific_DifferentPackage {
        record Success(String value) implements TestMultipleErrorsAndPackagesResult {}

        record Test_InvalidArgument() implements TestMultipleErrorsAndPackagesResult {}

        record Test_NotFound() implements TestMultipleErrorsAndPackagesResult {}

        record EndpointSpecificTwo_DifferentNamespace() implements TestMultipleErrorsAndPackagesResult {}

        record EndpointSpecific_DifferentPackage() implements TestMultipleErrorsAndPackagesResult {}
    }
}
