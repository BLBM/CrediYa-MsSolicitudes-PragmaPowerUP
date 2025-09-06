package co.com.bancolombia.model.loan_application.gateways;

import co.com.bancolombia.model.loan_application.LoanApplication;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



public interface LoanApplicationRepository {
    Mono<LoanApplication> save(LoanApplication loanApplication);
    Flux<LoanApplication> findByStatusId(Integer statusId);
    Flux<LoanApplication> findByStatusIdAndEmail(Integer statusId, String email);
    Mono<LoanApplication> update(LoanApplication loanApplication);
    Mono<LoanApplication> findById(Integer id);
}
