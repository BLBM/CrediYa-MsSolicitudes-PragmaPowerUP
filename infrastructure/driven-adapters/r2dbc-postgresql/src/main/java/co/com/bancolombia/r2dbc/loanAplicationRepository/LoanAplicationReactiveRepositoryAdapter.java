package co.com.bancolombia.r2dbc.loanAplicationRepository;


import co.com.bancolombia.model.estado.Estado;
import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import co.com.bancolombia.model.tipoPrestamo.TipoPrestamo;
import co.com.bancolombia.r2dbc.config.TransactionConfig;
import co.com.bancolombia.r2dbc.entity.LoanAplicationEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class LoanAplicationReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Solicitud,
        LoanAplicationEntity,
        Integer,
        LoanAplicationReactiveRepository
        > implements SolicitudRepository {

    private final TransactionalOperator txOperator;

    public LoanAplicationReactiveRepositoryAdapter(LoanAplicationReactiveRepository repository, ObjectMapper mapper, TransactionalOperator txOperator) {
        super(repository, mapper, d -> mapper.map(d, Solicitud.class));
        this.txOperator = txOperator;
    }


    @Override
    protected LoanAplicationEntity toData(Solicitud solicitud) {
        return LoanAplicationEntity.builder()
                .idSolicitud(solicitud.getIdSolicitud())
                .monto(solicitud.getMonto())
                .plazo(solicitud.getPlazo())
                .email(solicitud.getEmail())
                .estado(solicitud.getEstado().getIdEstado())  // Solo el ID
                .tipoPrestamo(solicitud.getTipoPrestamo().getIdTipoPrestamo())  // Solo el ID
                .build();
    }

    @Override
    protected Solicitud toEntity(LoanAplicationEntity entity) {
        return Solicitud.builder()
                .idSolicitud(entity.getIdSolicitud())
                .monto(entity.getMonto())
                .plazo(entity.getPlazo())
                .email(entity.getEmail())
                .estado(new Estado(entity.getEstado(), null, null))  // Solo con ID
                .tipoPrestamo(new TipoPrestamo(entity.getTipoPrestamo(), null, null, null, null, null))  // Solo con ID
                .build();
    }

    @Override
    public Mono<Solicitud> save(Solicitud solicitud) {
        return repository.save(toData(solicitud))
                .map(this::toEntity)
                .as(txOperator::transactional)
                .doOnSuccess(saved -> log.info("Solicitud saved: {}", saved))
                .doOnError(e -> log.error("Error guardando usuario: {}", solicitud));
    }

    @Override
    public Mono<Solicitud> findById(Integer idSolicitud) {
        return repository.findById(idSolicitud)
                .map(this::toEntity);
    }

    @Override
    public Mono<Void> deleteById(Integer idSolicitud) {
        return repository.deleteById(idSolicitud);
    }

}
