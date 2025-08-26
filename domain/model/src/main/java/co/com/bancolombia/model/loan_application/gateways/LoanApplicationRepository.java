package co.com.bancolombia.model.loan_application.gateways;

import co.com.bancolombia.model.loan_application.LoanApplication;

import reactor.core.publisher.Mono;

public interface LoanApplicationRepository {
    Mono<LoanApplication> save(LoanApplication loanApplication);

}
