package co.com.bancolombia.model.loan_application.gateways;

import co.com.bancolombia.model.loan_application.LoanApplication;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



public interface LoanApplicationRepository {
    Mono<LoanApplication> save(LoanApplication loanApplication);
    Flux<LoanApplication> findByStatusId(Integer statusId);
}
