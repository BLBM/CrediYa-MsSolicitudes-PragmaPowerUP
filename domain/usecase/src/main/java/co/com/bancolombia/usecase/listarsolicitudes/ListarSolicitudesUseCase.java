package co.com.bancolombia.usecase.listarsolicitudes;

import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class ListarSolicitudesUseCase {

    private final SolicitudRepository solicitudRepository;

    public Flux<Solicitud> execute(){
       return solicitudRepository.findAll();
    }
}
