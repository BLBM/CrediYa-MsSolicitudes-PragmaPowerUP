package co.com.bancolombia.usecase.guardarsolicitud;

import co.com.bancolombia.model.estado.Estado;
import co.com.bancolombia.model.estado.gateways.EstadoRepository;
import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import co.com.bancolombia.model.tipoPrestamo.TipoPrestamo;
import co.com.bancolombia.model.tipoPrestamo.gateways.TipoPrestamoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GuardarSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final EstadoRepository estadoRepository;


    public Mono<Solicitud> execute(Solicitud solicitud){

        Mono<TipoPrestamo> tipoPrestamoMono = tipoPrestamoRepository.findById(solicitud.getTipoPrestamo().getIdTipoPrestamo())
                .switchIfEmpty(Mono.error(new DomainException("el tipo prestamo no existe")));

        Mono<Estado> estadoMono = estadoRepository.findById(solicitud.getEstado().getIdEstado())
                .switchIfEmpty(Mono.error(new IllegalStateException("No se encontró el estado inicial en BD. Verifique configuración.")));

        return Mono.zip(tipoPrestamoMono,estadoMono
        ).flatMap(tipoPrestamoEstado -> {
            TipoPrestamo tipoPrestamoCompleto = tipoPrestamoEstado.getT1();
            Estado estadoCompleto = tipoPrestamoEstado.getT2();

            solicitud.setTipoPrestamo(tipoPrestamoCompleto);
            solicitud.setEstado(estadoCompleto);

            return solicitudRepository.save(solicitud)
                    .map(solicitudGuardada -> {
                        solicitudGuardada.setTipoPrestamo(tipoPrestamoCompleto);
                        solicitudGuardada.setEstado(estadoCompleto);
                        return solicitudGuardada;
                    });
        });
    }
}
