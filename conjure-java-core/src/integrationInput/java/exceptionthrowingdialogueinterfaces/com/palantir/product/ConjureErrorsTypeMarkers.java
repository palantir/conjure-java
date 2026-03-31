package exceptionthrowingdialogueinterfaces.com.palantir.product;

import com.palantir.dialogue.ExceptionDeserializerArgs;
import com.palantir.dialogue.TypeMarker;
import javax.annotation.processing.Generated;

/** Internal utility class used by generated Dialogue interfaces. Not intended for external use. */
@Generated("com.palantir.conjure.java.services.dialogue.ErrorTypeMarkersGenerator")
public final class ConjureErrorsTypeMarkers {
    private static final TypeMarker<ConjureErrors.ConflictingCauseSafeArgSerializableError>
            CONFLICTING_CAUSE_SAFE_ARG_SERIALIZABLE_ERROR =
                    new TypeMarker<ConjureErrors.ConflictingCauseSafeArgSerializableError>() {};

    private static final TypeMarker<ConjureErrors.ConflictingCauseSafeArgException>
            CONFLICTING_CAUSE_SAFE_ARG_EXCEPTION = new TypeMarker<ConjureErrors.ConflictingCauseSafeArgException>() {};

    private static final TypeMarker<ConjureErrors.ConflictingCauseUnsafeArgSerializableError>
            CONFLICTING_CAUSE_UNSAFE_ARG_SERIALIZABLE_ERROR =
                    new TypeMarker<ConjureErrors.ConflictingCauseUnsafeArgSerializableError>() {};

    private static final TypeMarker<ConjureErrors.ConflictingCauseUnsafeArgException>
            CONFLICTING_CAUSE_UNSAFE_ARG_EXCEPTION =
                    new TypeMarker<ConjureErrors.ConflictingCauseUnsafeArgException>() {};

    private static final TypeMarker<ConjureErrors.ErrorWithComplexArgsSerializableError>
            ERROR_WITH_COMPLEX_ARGS_SERIALIZABLE_ERROR =
                    new TypeMarker<ConjureErrors.ErrorWithComplexArgsSerializableError>() {};

    private static final TypeMarker<ConjureErrors.ErrorWithComplexArgsException> ERROR_WITH_COMPLEX_ARGS_EXCEPTION =
            new TypeMarker<ConjureErrors.ErrorWithComplexArgsException>() {};

    private static final TypeMarker<ConjureErrors.InvalidServiceDefinitionSerializableError>
            INVALID_SERVICE_DEFINITION_SERIALIZABLE_ERROR =
                    new TypeMarker<ConjureErrors.InvalidServiceDefinitionSerializableError>() {};

    private static final TypeMarker<ConjureErrors.InvalidServiceDefinitionException>
            INVALID_SERVICE_DEFINITION_EXCEPTION = new TypeMarker<ConjureErrors.InvalidServiceDefinitionException>() {};

    private static final TypeMarker<ConjureErrors.InvalidTypeDefinitionSerializableError>
            INVALID_TYPE_DEFINITION_SERIALIZABLE_ERROR =
                    new TypeMarker<ConjureErrors.InvalidTypeDefinitionSerializableError>() {};

    private static final TypeMarker<ConjureErrors.InvalidTypeDefinitionException> INVALID_TYPE_DEFINITION_EXCEPTION =
            new TypeMarker<ConjureErrors.InvalidTypeDefinitionException>() {};

    private ConjureErrorsTypeMarkers() {}

    public static <T> void registerExceptions(ExceptionDeserializerArgs.Builder<T> builder) {
        builder.exception(
                ConjureErrors.CONFLICTING_CAUSE_SAFE_ARG.name(),
                CONFLICTING_CAUSE_SAFE_ARG_SERIALIZABLE_ERROR,
                CONFLICTING_CAUSE_SAFE_ARG_EXCEPTION);
        builder.exception(
                ConjureErrors.CONFLICTING_CAUSE_UNSAFE_ARG.name(),
                CONFLICTING_CAUSE_UNSAFE_ARG_SERIALIZABLE_ERROR,
                CONFLICTING_CAUSE_UNSAFE_ARG_EXCEPTION);
        builder.exception(
                ConjureErrors.ERROR_WITH_COMPLEX_ARGS.name(),
                ERROR_WITH_COMPLEX_ARGS_SERIALIZABLE_ERROR,
                ERROR_WITH_COMPLEX_ARGS_EXCEPTION);
        builder.exception(
                ConjureErrors.INVALID_SERVICE_DEFINITION.name(),
                INVALID_SERVICE_DEFINITION_SERIALIZABLE_ERROR,
                INVALID_SERVICE_DEFINITION_EXCEPTION);
        builder.exception(
                ConjureErrors.INVALID_TYPE_DEFINITION.name(),
                INVALID_TYPE_DEFINITION_SERIALIZABLE_ERROR,
                INVALID_TYPE_DEFINITION_EXCEPTION);
    }
}
