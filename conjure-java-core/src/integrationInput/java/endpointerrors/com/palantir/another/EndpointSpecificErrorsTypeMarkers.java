package endpointerrors.com.palantir.another;

import com.palantir.dialogue.TypeMarker;
import javax.annotation.processing.Generated;

/** Internal utility class used by generated Dialogue interfaces. Not intended for external use. */
@Generated("com.palantir.conjure.java.services.dialogue.ErrorTypeMarkersGenerator")
public final class EndpointSpecificErrorsTypeMarkers {
    public static final TypeMarker<EndpointSpecificErrors.DifferentPackageSerializableError>
            DIFFERENT_PACKAGE_SERIALIZABLE_ERROR =
                    new TypeMarker<EndpointSpecificErrors.DifferentPackageSerializableError>() {};

    public static final TypeMarker<EndpointSpecificErrors.DifferentPackageException> DIFFERENT_PACKAGE_EXCEPTION =
            new TypeMarker<EndpointSpecificErrors.DifferentPackageException>() {};

    private EndpointSpecificErrorsTypeMarkers() {}
}
