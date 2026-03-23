package endpointerrors.com.palantir.product;

import com.palantir.dialogue.TypeMarker;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.ErrorTypeMarkersGenerator")
public final class EndpointSpecificTwoErrorsTypeMarkers {
    public static final TypeMarker<EndpointSpecificTwoErrors.DifferentNamespaceSerializableError>
            DifferentNamespaceSerializableError =
                    new TypeMarker<EndpointSpecificTwoErrors.DifferentNamespaceSerializableError>() {};

    public static final TypeMarker<EndpointSpecificTwoErrors.DifferentNamespaceException> DifferentNamespaceException =
            new TypeMarker<EndpointSpecificTwoErrors.DifferentNamespaceException>() {};
}
