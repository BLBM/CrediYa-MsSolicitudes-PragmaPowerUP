package co.com.bancolombia.r2dbc.loan_application_repository;


import co.com.bancolombia.logconstants.LogConstants;
import co.com.bancolombia.model.loan_application.LoanApplication;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loanwithrate.LoanWithRate;
import co.com.bancolombia.r2dbc.entity.LoanApplicationEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import co.com.bancolombia.r2dbc.mapper.LoanApplicationMapper;
import co.com.bancolombia.r2dbc.mapper.LoanWithRateMapper;
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
    private final LoanApplicationMapper loanApplicationMapper;
    private final LoanWithRateMapper loanWithRateMapper;

    public LoanAplicationReactiveRepositoryAdapter(
            LoanAplicationReactiveRepository repository,
            ObjectMapper mapper,
            TransactionalOperator txOperator,
            LoanApplicationMapper loanApplicationMapper,
            LoanWithRateMapper loanWithRateMapper
            ) {
        super(repository, mapper, loanApplicationMapper::toDomain);
        this.txOperator = txOperator;
        this.loanApplicationMapper = loanApplicationMapper;
        this.loanWithRateMapper = loanWithRateMapper;
    }

    @Override
    protected LoanApplicationEntity toData(LoanApplication loanApplication) {
        return loanApplicationMapper.toEntity(loanApplication);
    }

    @Override
    protected LoanApplication toEntity(LoanApplicationEntity entity) {
        return loanApplicationMapper.toDomain(entity);
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
    public Mono<LoanApplication> update(LoanApplication loanApplication) {
        log.debug(LogConstants.START_PROCESS_UPDATE,loanApplication.getLoanApplicationId());
        return repository.save(toData(loanApplication))
                .map(this::toEntity)
                .as(txOperator::transactional)
                .doOnSuccess(saved -> log.info(LogConstants.SUCCESSFUL_OPERATION_UPDATE, saved))
                .doOnError(e -> log.error(LogConstants.ERROR_OPERATION_UPDATE, loanApplication));
    }

    @Override
    public Flux<LoanApplication> findByStatusId(Integer statusId) {
        log.debug(LogConstants.START_PROCESS_FIND_BY_STATUS, statusId);
        return repository.findByStatusId(statusId)
                .map(this::toEntity)
                .doOnError(e -> log.error(LogConstants.ERROR_OPERATION_FIND_STATUS_BY_ID, statusId));
    }

    @Override
    public Flux<LoanApplication> findByStatusIdAndEmail(Integer statusId, String email) {
        return repository.findByStatusIdAndEmail(statusId,email)
                .map(this::toEntity)
                .doOnError(e -> log.error(LogConstants.ERROR_OPERATION_FIND_BY_STATUS_AND_DOCUMENT,statusId,email));
    }



    @Override
    public Mono<LoanApplication> findById(Integer loanApplicationId) {
        return repository.findById(loanApplicationId)
                .map(this::toEntity);
    }


    @Override
    public Flux<LoanWithRate> findLoansWithRateByStatus(String email, Integer statusId) {
        return repository.findLoansWithRateByStatusAndEmail(statusId, email)
                .map(loanWithRateMapper::toDomain);

    }


}
