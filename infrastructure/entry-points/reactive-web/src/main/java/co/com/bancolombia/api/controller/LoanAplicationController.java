package co.com.bancolombia.api.controller;


import co.com.bancolombia.api.dto.LoanAplicationRequest;
import co.com.bancolombia.api.dto.LoanAplicationResponse;
import co.com.bancolombia.model.estado.Estado;
import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.tipoPrestamo.TipoPrestamo;
import co.com.bancolombia.usecase.guardarsolicitud.GuardarSolicitudUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/solicitud")
public class LoanAplicationController {

    private final GuardarSolicitudUseCase guardarSolicitudUseCase;

    @PostMapping
    public Mono<LoanAplicationResponse> create(@RequestBody LoanAplicationRequest loanAplicationRequest) {
        Solicitud solicitud = Solicitud.builder()
                        .monto(loanAplicationRequest.monto())
                        .plazo(loanAplicationRequest.plazo())
                        .email(loanAplicationRequest.email())
                        .estado(new Estado(1,null,null))
                        .tipoPrestamo(new TipoPrestamo(loanAplicationRequest.idTipoPrestamo(),null,null,null,null,null))
                        .build();

        return guardarSolicitudUseCase.execute(solicitud)
                .map(s-> new LoanAplicationResponse(
                        s.getIdSolicitud(),
                        s.getMonto(),
                        s.getPlazo(),
                        s.getEmail(),
                        s.getEstado().getDescripcion(),
                        s.getTipoPrestamo().getNombre()
                ));
    }
}
