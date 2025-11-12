package undertowasyncdisabled.test.api;

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;

@ConjureGenerated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface AsyncMarkers {
    /** @apiNote {@code GET /async/marker} */
    String asyncMarker();

    /** @apiNote {@code GET /async/tag} */
    ListenableFuture<String> asyncTag();

    /** @apiNote {@code GET /sync} */
    String sync();
}
