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
                    TypeMarker.of(TestErrors.ComplicatedParametersSerializableError.class);

    private static final TypeMarker<TestErrors.ComplicatedParametersException> COMPLICATED_PARAMETERS_EXCEPTION =
            TypeMarker.of(TestErrors.ComplicatedParametersException.class);

    private static final TypeMarker<TestErrors.InvalidArgumentSerializableError> INVALID_ARGUMENT_SERIALIZABLE_ERROR =
            TypeMarker.of(TestErrors.InvalidArgumentSerializableError.class);

    private static final TypeMarker<TestErrors.InvalidArgumentException> INVALID_ARGUMENT_EXCEPTION =
            TypeMarker.of(TestErrors.InvalidArgumentException.class);

    private static final TypeMarker<TestErrors.NotFoundSerializableError> NOT_FOUND_SERIALIZABLE_ERROR =
            TypeMarker.of(TestErrors.NotFoundSerializableError.class);

    private static final TypeMarker<TestErrors.NotFoundException> NOT_FOUND_EXCEPTION =
            TypeMarker.of(TestErrors.NotFoundException.class);

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
