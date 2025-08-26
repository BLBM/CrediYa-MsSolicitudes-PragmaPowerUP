package co.com.bancolombia.model.loan_type.gateways;

import co.com.bancolombia.model.loan_type.LoanType;
import reactor.core.publisher.Mono;

public interface LoanTypeRepository {
    Mono<LoanType> findById(Integer id);
}
