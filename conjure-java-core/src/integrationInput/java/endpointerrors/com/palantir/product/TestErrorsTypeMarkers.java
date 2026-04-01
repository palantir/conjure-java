package endpointerrors.com.palantir.product;

import com.palantir.dialogue.ExceptionDeserializerArgs;
import com.palantir.dialogue.TypeMarker;
import javax.annotation.processing.Generated;

/**
 * Internal utility class used by generated Dialogue interfaces. Not intended for external use. This class needs to be
 * public because errors from a certain namespace can be used in Dialogue interfaces defined in any namespace.
 */
@Generated("com.palantir.conjure.java.services.dialogue.ErrorTypeMarkersGenerator")
public final class TestErrorsTypeMarkers {
    private static final TypeMarker<TestErrors.ComplicatedParametersSerializableError>
            COMPLICATED_PARAMETERS_SERIALIZABLE_ERROR =
                    new TypeMarker<TestErrors.ComplicatedParametersSerializableError>() {};

    private static final TypeMarker<TestErrors.ComplicatedParametersException> COMPLICATED_PARAMETERS_EXCEPTION =
            new TypeMarker<TestErrors.ComplicatedParametersException>() {};

    private static final TypeMarker<TestErrors.InvalidArgumentSerializableError> INVALID_ARGUMENT_SERIALIZABLE_ERROR =
            new TypeMarker<TestErrors.InvalidArgumentSerializableError>() {};

    private static final TypeMarker<TestErrors.InvalidArgumentException> INVALID_ARGUMENT_EXCEPTION =
            new TypeMarker<TestErrors.InvalidArgumentException>() {};

    private static final TypeMarker<TestErrors.NotFoundSerializableError> NOT_FOUND_SERIALIZABLE_ERROR =
            new TypeMarker<TestErrors.NotFoundSerializableError>() {};

    private static final TypeMarker<TestErrors.NotFoundException> NOT_FOUND_EXCEPTION =
            new TypeMarker<TestErrors.NotFoundException>() {};

    private TestErrorsTypeMarkers() {}

    public static <T> void registerExceptions(ExceptionDeserializerArgs.Builder<T> builder) {
        builder.exception(
                TestErrors.COMPLICATED_PARAMETERS.name(),
                COMPLICATED_PARAMETERS_SERIALIZABLE_ERROR,
                COMPLICATED_PARAMETERS_EXCEPTION);
        builder.exception(
                TestErrors.INVALID_ARGUMENT.name(), INVALID_ARGUMENT_SERIALIZABLE_ERROR, INVALID_ARGUMENT_EXCEPTION);
        builder.exception(TestErrors.NOT_FOUND.name(), NOT_FOUND_SERIALIZABLE_ERROR, NOT_FOUND_EXCEPTION);
    }
}
