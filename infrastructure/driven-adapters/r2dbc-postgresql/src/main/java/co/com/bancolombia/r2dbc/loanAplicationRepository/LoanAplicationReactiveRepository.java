package co.com.bancolombia.r2dbc.loanAplicationRepository;

import co.com.bancolombia.r2dbc.entity.LoanAplicationEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface LoanAplicationReactiveRepository extends ReactiveCrudRepository<LoanAplicationEntity, Integer>, ReactiveQueryByExampleExecutor<LoanAplicationEntity> {

    Mono<LoanAplicationEntity> findByIdSolicitud(Integer idSolicitud);
    Mono<Void> deleteByIdSolicitud(Integer idSolicitud);
}
