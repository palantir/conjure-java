package exceptionthrowingdialogueinterfaces.com.palantir.product;

import com.palantir.dialogue.TypeMarker;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.ErrorTypeMarkersGenerator")
public final class ConjureErrorsTypeMarkers {
    public static final TypeMarker<ConjureErrors.ConflictingCauseSafeArgSerializableError>
            CONFLICTING_CAUSE_SAFE_ARG_SERIALIZABLE_ERROR =
                    new TypeMarker<ConjureErrors.ConflictingCauseSafeArgSerializableError>() {};

    public static final TypeMarker<ConjureErrors.ConflictingCauseSafeArgException>
            CONFLICTING_CAUSE_SAFE_ARG_EXCEPTION = new TypeMarker<ConjureErrors.ConflictingCauseSafeArgException>() {};

    public static final TypeMarker<ConjureErrors.ConflictingCauseUnsafeArgSerializableError>
            CONFLICTING_CAUSE_UNSAFE_ARG_SERIALIZABLE_ERROR =
                    new TypeMarker<ConjureErrors.ConflictingCauseUnsafeArgSerializableError>() {};

    public static final TypeMarker<ConjureErrors.ConflictingCauseUnsafeArgException>
            CONFLICTING_CAUSE_UNSAFE_ARG_EXCEPTION =
                    new TypeMarker<ConjureErrors.ConflictingCauseUnsafeArgException>() {};

    public static final TypeMarker<ConjureErrors.ErrorWithComplexArgsSerializableError>
            ERROR_WITH_COMPLEX_ARGS_SERIALIZABLE_ERROR =
                    new TypeMarker<ConjureErrors.ErrorWithComplexArgsSerializableError>() {};

    public static final TypeMarker<ConjureErrors.ErrorWithComplexArgsException> ERROR_WITH_COMPLEX_ARGS_EXCEPTION =
            new TypeMarker<ConjureErrors.ErrorWithComplexArgsException>() {};

    public static final TypeMarker<ConjureErrors.InvalidServiceDefinitionSerializableError>
            INVALID_SERVICE_DEFINITION_SERIALIZABLE_ERROR =
                    new TypeMarker<ConjureErrors.InvalidServiceDefinitionSerializableError>() {};

    public static final TypeMarker<ConjureErrors.InvalidServiceDefinitionException>
            INVALID_SERVICE_DEFINITION_EXCEPTION = new TypeMarker<ConjureErrors.InvalidServiceDefinitionException>() {};

    public static final TypeMarker<ConjureErrors.InvalidTypeDefinitionSerializableError>
            INVALID_TYPE_DEFINITION_SERIALIZABLE_ERROR =
                    new TypeMarker<ConjureErrors.InvalidTypeDefinitionSerializableError>() {};

    public static final TypeMarker<ConjureErrors.InvalidTypeDefinitionException> INVALID_TYPE_DEFINITION_EXCEPTION =
            new TypeMarker<ConjureErrors.InvalidTypeDefinitionException>() {};

    private ConjureErrorsTypeMarkers() {}
}
