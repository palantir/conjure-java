package endpointerrors.com.palantir.product;

import com.palantir.dialogue.TypeMarker;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.ErrorTypeMarkersGenerator")
public final class TestErrorsTypeMarkers {
    public static final TypeMarker<TestErrors.ComplicatedParametersSerializableError>
            ComplicatedParametersSerializableError =
                    new TypeMarker<TestErrors.ComplicatedParametersSerializableError>() {};

    public static final TypeMarker<TestErrors.ComplicatedParametersException> ComplicatedParametersException =
            new TypeMarker<TestErrors.ComplicatedParametersException>() {};

    public static final TypeMarker<TestErrors.InvalidArgumentSerializableError> InvalidArgumentSerializableError =
            new TypeMarker<TestErrors.InvalidArgumentSerializableError>() {};

    public static final TypeMarker<TestErrors.InvalidArgumentException> InvalidArgumentException =
            new TypeMarker<TestErrors.InvalidArgumentException>() {};

    public static final TypeMarker<TestErrors.NotFoundSerializableError> NotFoundSerializableError =
            new TypeMarker<TestErrors.NotFoundSerializableError>() {};

    public static final TypeMarker<TestErrors.NotFoundException> NotFoundException =
            new TypeMarker<TestErrors.NotFoundException>() {};
}
