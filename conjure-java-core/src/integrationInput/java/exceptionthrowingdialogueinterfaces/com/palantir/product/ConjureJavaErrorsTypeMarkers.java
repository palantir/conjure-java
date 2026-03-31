package exceptionthrowingdialogueinterfaces.com.palantir.product;

import com.palantir.dialogue.ExceptionDeserializerArgs;
import com.palantir.dialogue.TypeMarker;
import javax.annotation.processing.Generated;

/** Internal utility class used by generated Dialogue interfaces. Not intended for external use. */
@Generated("com.palantir.conjure.java.services.dialogue.ErrorTypeMarkersGenerator")
public final class ConjureJavaErrorsTypeMarkers {
    private static final TypeMarker<ConjureJavaErrors.JavaCompilationFailedSerializableError>
            JAVA_COMPILATION_FAILED_SERIALIZABLE_ERROR =
                    new TypeMarker<ConjureJavaErrors.JavaCompilationFailedSerializableError>() {};

    private static final TypeMarker<ConjureJavaErrors.JavaCompilationFailedException>
            JAVA_COMPILATION_FAILED_EXCEPTION = new TypeMarker<ConjureJavaErrors.JavaCompilationFailedException>() {};

    private ConjureJavaErrorsTypeMarkers() {}

    public static <T> void registerExceptions(ExceptionDeserializerArgs.Builder<T> builder) {
        builder.exception(
                ConjureJavaErrors.JAVA_COMPILATION_FAILED.name(),
                JAVA_COMPILATION_FAILED_SERIALIZABLE_ERROR,
                JAVA_COMPILATION_FAILED_EXCEPTION);
    }
}
