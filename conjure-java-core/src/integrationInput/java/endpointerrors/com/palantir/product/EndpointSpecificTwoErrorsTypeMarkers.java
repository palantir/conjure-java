package endpointerrors.com.palantir.product;

import com.palantir.dialogue.TypeMarker;
import javax.annotation.processing.Generated;

/** Internal utility class used by generated Dialogue interfaces. Not intended for external use. */
@Generated("com.palantir.conjure.java.services.dialogue.ErrorTypeMarkersGenerator")
public final class EndpointSpecificTwoErrorsTypeMarkers {
    public static final TypeMarker<EndpointSpecificTwoErrors.DifferentNamespaceSerializableError>
            DIFFERENT_NAMESPACE_SERIALIZABLE_ERROR =
                    new TypeMarker<EndpointSpecificTwoErrors.DifferentNamespaceSerializableError>() {};

    public static final TypeMarker<EndpointSpecificTwoErrors.DifferentNamespaceException>
            DIFFERENT_NAMESPACE_EXCEPTION = new TypeMarker<EndpointSpecificTwoErrors.DifferentNamespaceException>() {};

    private EndpointSpecificTwoErrorsTypeMarkers() {}
}
