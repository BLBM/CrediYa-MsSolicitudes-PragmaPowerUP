package co.com.bancolombia.model.loan_type.gateways;

import co.com.bancolombia.model.loan_type.LoanType;
import co.com.bancolombia.model.loan_type.TipoPrestamo;
import reactor.core.publisher.Mono;

public interface LoanTypeRepository {
    Mono<LoanType> findById(Integer id);
}
