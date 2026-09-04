package externalfallbacktypes.com.palantir.product.external;

import com.palantir.conjure.java.lib.internal.ClientEndpoint;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.Deserializer;
import com.palantir.dialogue.DialogueService;
import com.palantir.dialogue.DialogueServiceFactory;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.EndpointChannel;
import com.palantir.dialogue.EndpointChannelFactory;
import com.palantir.dialogue.PlainSerDe;
import com.palantir.dialogue.Request;
import com.palantir.dialogue.Serializer;
import com.palantir.dialogue.TypeMarker;
import com.palantir.logsafe.Safe;
import java.lang.Override;
import java.lang.String;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
@DialogueService(ServiceUsingExternalTypesBlocking.Factory.class)
public interface ServiceUsingExternalTypesBlocking {
    /** @apiNote {@code PUT /external/{path}} */
    @ClientEndpoint(method = "PUT", path = "/external/{path}")
    Map<String, String> external(@Safe String path, @Safe List<String> body);

    /** Creates a synchronous/blocking client for a ServiceUsingExternalTypes service. */
    static ServiceUsingExternalTypesBlocking of(
            EndpointChannelFactory _endpointChannelFactory, ConjureRuntime _runtime) {
        return new ServiceUsingExternalTypesBlocking() {
            private static final TypeMarker<Map<String, String>> mapStringStringTypeMarker =
                    new TypeMarker<Map<String, String>>() {};

            private static final TypeMarker<List<String>> listStringTypeMarker = new TypeMarker<List<String>>() {};

            private final PlainSerDe _plainSerDe = _runtime.plainSerDe();

            private final Deserializer<Map<String, String>> mapStringStringDeserializer =
                    _runtime.bodySerDe().deserializer(mapStringStringTypeMarker);

            private final Serializer<List<String>> listStringSerializer =
                    _runtime.bodySerDe().serializer(listStringTypeMarker);

            private final EndpointChannel externalChannel =
                    _endpointChannelFactory.endpoint(DialogueServiceUsingExternalTypesEndpoints.external);

            @Override
            public Map<String, String> external(String path, List<String> body) {
                Request.Builder _request = Request.builder();
                _request.putPathParams("path", _plainSerDe.serializeString(path));
                _request.body(listStringSerializer.serialize(body));
                return _runtime.clients().callBlocking(externalChannel, _request.build(), mapStringStringDeserializer);
            }

            @Override
            public String toString() {
                return "ServiceUsingExternalTypesBlocking{_endpointChannelFactory=" + _endpointChannelFactory
                        + ", runtime=" + _runtime + '}';
            }
        };
    }

    /** Creates an asynchronous/non-blocking client for a ServiceUsingExternalTypes service. */
    static ServiceUsingExternalTypesBlocking of(Channel _channel, ConjureRuntime _runtime) {
        if (_channel instanceof EndpointChannelFactory) {
            return of((EndpointChannelFactory) _channel, _runtime);
        }
        return of(
                new EndpointChannelFactory() {
                    @Override
                    public EndpointChannel endpoint(Endpoint endpoint) {
                        return _runtime.clients().bind(_channel, endpoint);
                    }
                },
                _runtime);
    }

    final class Factory implements DialogueServiceFactory<ServiceUsingExternalTypesBlocking> {
        @Override
        public ServiceUsingExternalTypesBlocking create(
                EndpointChannelFactory endpointChannelFactory, ConjureRuntime runtime) {
            return ServiceUsingExternalTypesBlocking.of(endpointChannelFactory, runtime);
        }
    }
}
