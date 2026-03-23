package endpointerrors.com.palantir.product;

import com.palantir.dialogue.TypeMarker;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.ErrorTypeMarkersGenerator")
public final class TestErrorsTypeMarkers {
    public static final TypeMarker<TestErrors.ComplicatedParametersSerializableError>
            COMPLICATED_PARAMETERS_SERIALIZABLE_ERROR =
                    new TypeMarker<TestErrors.ComplicatedParametersSerializableError>() {};

    public static final TypeMarker<TestErrors.ComplicatedParametersException> COMPLICATED_PARAMETERS_EXCEPTION =
            new TypeMarker<TestErrors.ComplicatedParametersException>() {};

    public static final TypeMarker<TestErrors.InvalidArgumentSerializableError> INVALID_ARGUMENT_SERIALIZABLE_ERROR =
            new TypeMarker<TestErrors.InvalidArgumentSerializableError>() {};

    public static final TypeMarker<TestErrors.InvalidArgumentException> INVALID_ARGUMENT_EXCEPTION =
            new TypeMarker<TestErrors.InvalidArgumentException>() {};

    public static final TypeMarker<TestErrors.NotFoundSerializableError> NOT_FOUND_SERIALIZABLE_ERROR =
            new TypeMarker<TestErrors.NotFoundSerializableError>() {};

    public static final TypeMarker<TestErrors.NotFoundException> NOT_FOUND_EXCEPTION =
            new TypeMarker<TestErrors.NotFoundException>() {};

    private TestErrorsTypeMarkers() {}
}
