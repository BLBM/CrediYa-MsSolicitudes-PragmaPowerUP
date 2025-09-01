package co.com.bancolombia.r2dbc.loan_application_repository;


import co.com.bancolombia.logconstants.LogConstants;
import co.com.bancolombia.model.loan_application.LoanApplication;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loan_type.LoanType;
import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.r2dbc.entity.LoanApplicationEntity;
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
    protected LoanApplicationEntity toData(LoanApplication loanApplication) {
        return LoanApplicationEntity.builder()
                .loanApplicationId(loanApplication.getLoanApplicationId())
                .amount(loanApplication.getAmount())
                .timeLimit(loanApplication.getTimeLimit())
                .documentId(loanApplication.getDocumentId())
                .email(loanApplication.getEmail())
                .statusId(loanApplication.getStatus().getStatusId())
                .loanTypeId(loanApplication.getLoanType().getLoanTypeId())
                .build();
    }

    @Override
    protected LoanApplication toEntity(LoanApplicationEntity entity) {
        return LoanApplication.builder()
                .loanApplicationId(entity.getLoanApplicationId())
                .amount(entity.getAmount())
                .timeLimit(entity.getTimeLimit())
                .documentId(entity.getDocumentId())
                .email(entity.getEmail())
                .status(Status.builder()
                        .statusId(entity.getStatusId())
                        .build())
                .loanType(LoanType.builder()
                        .loanTypeId(entity.getLoanTypeId())
                        .build())
                .build();
    }


    @Override
    public Mono<LoanApplication> save(LoanApplication loanApplication) {
        log.debug(LogConstants.START_PROCESS);
        return repository.save(toData(loanApplication))
                .map(this::toEntity)
                .as(txOperator::transactional)
                .doOnSuccess(saved -> log.info(LogConstants.SUCCESSFUL_OPERATION, saved))
                .doOnError(e -> log.error(LogConstants.ERROR_OPERATION, loanApplication));
    }

    @Override
    public Flux<LoanApplication> findByStatusId(Integer statusId) {
        log.debug(LogConstants.START_PROCESS_FIND_BY_STATUS, statusId);
        return repository.findByStatusId(statusId)
                .map(this::toEntity)
                .doOnError(e -> log.error(LogConstants.ERROR_OPERATION_FIND_STATUS_BY_ID, statusId));
    }

    @Override
    public Mono<LoanApplication> findById(Integer loanApplicationId) {
        return repository.findById(loanApplicationId)
                .map(this::toEntity);
    }


}
