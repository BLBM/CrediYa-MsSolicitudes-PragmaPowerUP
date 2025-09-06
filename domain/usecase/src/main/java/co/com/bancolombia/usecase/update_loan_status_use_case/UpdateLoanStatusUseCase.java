package co.com.bancolombia.usecase.update_loan_status_use_case;

import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.model.loan_application.LoanApplication;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationMessages;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationRepository;
import co.com.bancolombia.usecase.loan_type_status.LoanTypeStatus;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateLoanStatusUseCase {


    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanTypeStatus loanTypeStatus;


    public Mono<LoanApplication> updateLoanStatus(Integer loanApplicationId, Integer statusId){

        return loanApplicationRepository.findById(loanApplicationId)
                .switchIfEmpty(Mono.error(new DomainException(LoanApplicationMessages.LOAN_APPLICATION_NO_EXIST)))
                    .flatMap(loanApplication -> loanTypeStatus.findLoanTypeById(loanApplication.getLoanType().getLoanTypeId())
                        .flatMap(loanType->loanTypeStatus.findStatusById(statusId)
                            .flatMap(status -> {

                                loanApplication.setStatus(status);
                                loanApplication.setLoanType(loanType);

                                return loanApplicationRepository.update(loanApplication)
                                        .map(loanApplicationUpdated -> {
                                            loanApplicationUpdated.setLoanType(loanType);
                                            loanApplicationUpdated.setStatus(status);
                                            return loanApplicationUpdated;
                                                });
                            })
                        )
                    );

    }

}
