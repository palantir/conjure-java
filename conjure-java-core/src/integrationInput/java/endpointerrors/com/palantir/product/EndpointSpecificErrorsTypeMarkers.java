package endpointerrors.com.palantir.product;

import com.palantir.dialogue.ExceptionDeserializerArgs;
import com.palantir.dialogue.TypeMarker;
import javax.annotation.processing.Generated;

/**
 * Internal utility class used by generated Dialogue interfaces. Not intended for external use. This class needs to be
 * public because errors from a certain namespace can be used in Dialogue interfaces defined in any namespace.
 */
@Generated("com.palantir.conjure.java.services.dialogue.ErrorTypeMarkersGenerator")
public final class EndpointSpecificErrorsTypeMarkers {
    private static final TypeMarker<EndpointSpecificErrors.EndpointErrorSerializableError>
            ENDPOINT_ERROR_SERIALIZABLE_ERROR =
                    new TypeMarker<EndpointSpecificErrors.EndpointErrorSerializableError>() {};

    private static final TypeMarker<EndpointSpecificErrors.EndpointErrorException> ENDPOINT_ERROR_EXCEPTION =
            new TypeMarker<EndpointSpecificErrors.EndpointErrorException>() {};

    private EndpointSpecificErrorsTypeMarkers() {}

    public static <T> void registerExceptions(ExceptionDeserializerArgs.Builder<T> builder) {
        builder.exception(
                EndpointSpecificErrors.ENDPOINT_ERROR.name(),
                ENDPOINT_ERROR_SERIALIZABLE_ERROR,
                ENDPOINT_ERROR_EXCEPTION);
    }
}
