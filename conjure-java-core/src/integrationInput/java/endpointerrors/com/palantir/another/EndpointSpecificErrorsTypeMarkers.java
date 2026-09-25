package endpointerrors.com.palantir.another;

import com.palantir.dialogue.ExceptionDeserializerArgs;
import com.palantir.dialogue.TypeMarker;
import javax.annotation.processing.Generated;

/**
 * Internal utility class used by generated Dialogue interfaces. Not intended for external use. This class needs to be
 * public because errors from a certain namespace can be used in Dialogue interfaces defined in any namespace.
 */
@Generated("com.palantir.conjure.java.services.dialogue.ErrorTypeMarkersGenerator")
public final class EndpointSpecificErrorsTypeMarkers {
    private static final TypeMarker<EndpointSpecificErrors.DifferentPackageSerializableError>
            DIFFERENT_PACKAGE_SERIALIZABLE_ERROR =
                    TypeMarker.of(EndpointSpecificErrors.DifferentPackageSerializableError.class);

    private static final TypeMarker<EndpointSpecificErrors.DifferentPackageException> DIFFERENT_PACKAGE_EXCEPTION =
            TypeMarker.of(EndpointSpecificErrors.DifferentPackageException.class);

    private EndpointSpecificErrorsTypeMarkers() {}

    public static <T> void registerExceptions(ExceptionDeserializerArgs.Builder<T> builder) {
        builder.exception(
                EndpointSpecificErrors.DIFFERENT_PACKAGE.name(),
                DIFFERENT_PACKAGE_SERIALIZABLE_ERROR,
                DIFFERENT_PACKAGE_EXCEPTION);
    }
}
