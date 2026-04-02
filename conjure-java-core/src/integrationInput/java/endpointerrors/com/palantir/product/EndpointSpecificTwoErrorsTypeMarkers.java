package endpointerrors.com.palantir.product;

import com.palantir.dialogue.ExceptionDeserializerArgs;
import com.palantir.dialogue.TypeMarker;
import javax.annotation.processing.Generated;

/**
 * Internal utility class used by generated Dialogue interfaces. Not intended for external use. This class needs to be
 * public because errors from a certain namespace can be used in Dialogue interfaces defined in any namespace.
 */
@Generated("com.palantir.conjure.java.services.dialogue.ErrorTypeMarkersGenerator")
public final class EndpointSpecificTwoErrorsTypeMarkers {
    private static final TypeMarker<EndpointSpecificTwoErrors.DifferentNamespaceSerializableError>
            DIFFERENT_NAMESPACE_SERIALIZABLE_ERROR =
                    new TypeMarker<EndpointSpecificTwoErrors.DifferentNamespaceSerializableError>() {};

    private static final TypeMarker<EndpointSpecificTwoErrors.DifferentNamespaceException>
            DIFFERENT_NAMESPACE_EXCEPTION = new TypeMarker<EndpointSpecificTwoErrors.DifferentNamespaceException>() {};

    private EndpointSpecificTwoErrorsTypeMarkers() {}

    public static <T> void registerExceptions(ExceptionDeserializerArgs.Builder<T> builder) {
        builder.exception(
                EndpointSpecificTwoErrors.DIFFERENT_NAMESPACE.name(),
                DIFFERENT_NAMESPACE_SERIALIZABLE_ERROR,
                DIFFERENT_NAMESPACE_EXCEPTION);
    }
}
