package co.com.bancolombia.usecase.eliminarsolicitud;


import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class EliminarSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;

    public Mono<Void> execute(Integer idSolicitud){
        return solicitudRepository.deleteById(idSolicitud);
    }
}
