package exceptionthrowingdialogueinterfaces.com.palantir.another;

import com.palantir.dialogue.ExceptionDeserializerArgs;
import com.palantir.dialogue.TypeMarker;
import javax.annotation.processing.Generated;

/**
 * Internal utility class used by generated Dialogue interfaces. Not intended for external use. This class needs to be
 * public because errors from a certain namespace can be used in Dialogue interfaces defined in any namespace.
 */
@Generated("com.palantir.conjure.java.services.dialogue.ErrorTypeMarkersGenerator")
public final class ConjureErrorsTypeMarkers {
    private static final TypeMarker<ConjureErrors.DifferentPackageErrorSerializableError>
            DIFFERENT_PACKAGE_ERROR_SERIALIZABLE_ERROR =
                    TypeMarker.of(ConjureErrors.DifferentPackageErrorSerializableError.class);

    private static final TypeMarker<ConjureErrors.DifferentPackageErrorException> DIFFERENT_PACKAGE_ERROR_EXCEPTION =
            TypeMarker.of(ConjureErrors.DifferentPackageErrorException.class);

    private ConjureErrorsTypeMarkers() {}

    public static <T> void registerExceptions(ExceptionDeserializerArgs.Builder<T> builder) {
        builder.exception(
                ConjureErrors.DIFFERENT_PACKAGE_ERROR.name(),
                DIFFERENT_PACKAGE_ERROR_SERIALIZABLE_ERROR,
                DIFFERENT_PACKAGE_ERROR_EXCEPTION);
    }
}
