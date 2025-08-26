package co.com.bancolombia.r2dbc.loan_application_repository;


import co.com.bancolombia.r2dbc.entity.LoanApplicationEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface LoanAplicationReactiveRepository extends ReactiveCrudRepository<LoanApplicationEntity, Integer>, ReactiveQueryByExampleExecutor<LoanApplicationEntity> {

}
