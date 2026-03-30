package endpointerrors.com.palantir.product;

import com.palantir.dialogue.TypeMarker;
import javax.annotation.processing.Generated;

/** Internal utility class used by generated Dialogue interfaces. Not intended for external use. */
@Generated("com.palantir.conjure.java.services.dialogue.ErrorTypeMarkersGenerator")
public final class EndpointSpecificErrorsTypeMarkers {
    public static final TypeMarker<EndpointSpecificErrors.EndpointErrorSerializableError>
            ENDPOINT_ERROR_SERIALIZABLE_ERROR =
                    new TypeMarker<EndpointSpecificErrors.EndpointErrorSerializableError>() {};

    public static final TypeMarker<EndpointSpecificErrors.EndpointErrorException> ENDPOINT_ERROR_EXCEPTION =
            new TypeMarker<EndpointSpecificErrors.EndpointErrorException>() {};

    private EndpointSpecificErrorsTypeMarkers() {}
}
