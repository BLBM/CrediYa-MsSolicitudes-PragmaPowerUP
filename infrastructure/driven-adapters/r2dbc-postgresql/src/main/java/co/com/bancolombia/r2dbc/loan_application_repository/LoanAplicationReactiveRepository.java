package co.com.bancolombia.r2dbc.loan_application_repository;



import co.com.bancolombia.r2dbc.dto.LoanWithRateDTO;
import co.com.bancolombia.r2dbc.entity.LoanApplicationEntity;
import co.com.bancolombia.r2dbc.query.LoanQueries;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;


public interface LoanAplicationReactiveRepository extends ReactiveCrudRepository<LoanApplicationEntity, Integer>, ReactiveQueryByExampleExecutor<LoanApplicationEntity> {

    Flux<LoanApplicationEntity> findByStatusId(Integer statusId);
    Flux<LoanApplicationEntity> findByStatusIdAndEmail(Integer statusId, String email);


    @Query(LoanQueries.FIND_LOANS_WITH_RATE_BY_STATUS_AND_EMAIL)
    Flux<LoanWithRateDTO> findLoansWithRateByStatusAndEmail(Integer statusId, String email);

}
