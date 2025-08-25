package co.com.bancolombia.usecase.listarsolicitud;

import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ListarSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;

    public Mono<Solicitud> execute(Integer idSolicitud) {
        return solicitudRepository.findById(idSolicitud);
    }
}
