package co.com.bancolombia.r2dbc.loan_application_repository;


import co.com.bancolombia.model.loan_application.LoanApplication;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationRepository;
import co.com.bancolombia.r2dbc.entity.LoanApplicationEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class LoanAplicationReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        LoanApplication,
        LoanApplicationEntity,
        Integer,
        LoanAplicationReactiveRepository
        > implements LoanApplicationRepository {

    private final TransactionalOperator txOperator;

    public LoanAplicationReactiveRepositoryAdapter(LoanAplicationReactiveRepository repository, ObjectMapper mapper, TransactionalOperator txOperator) {
        super(repository, mapper, d -> mapper.map(d, LoanApplication.class));
        this.txOperator = txOperator;
    }


    @Override
    public Mono<LoanApplication> save(LoanApplication loanApplication) {
        return repository.save(toData(loanApplication))
                .map(this::toEntity)
                .as(txOperator::transactional)
                .doOnSuccess(saved -> log.info("Solicitud saved: {}", saved))
                .doOnError(e -> log.error("Error guardando usuario: {}", loanApplication));
    }

    @Override
    public Mono<LoanApplication> findById(Integer loanApplicationId) {
        return repository.findById(loanApplicationId)
                .map(this::toEntity);
    }


}
