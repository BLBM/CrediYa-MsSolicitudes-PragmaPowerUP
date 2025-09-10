package co.com.bancolombia.usecase.update_loan_status_use_case;

import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.model.loan_application.LoanApplication;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationMessages;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loan_application_event.LoanApplicationEvent;
import co.com.bancolombia.model.loan_application_event.gateways.LoanApplicationEventRepository;
import co.com.bancolombia.usecase.loan_type_status.LoanTypeStatus;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateLoanStatusUseCase {


    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanTypeStatus loanTypeStatus;
    private final LoanApplicationEventRepository loanApplicationEventRepository;


    public Mono<LoanApplication> updateLoanStatus(Integer loanApplicationId, Integer statusId) {
        return loanApplicationRepository.findById(loanApplicationId)
                .switchIfEmpty(Mono.error(new DomainException(LoanApplicationMessages.LOAN_APPLICATION_NO_EXIST)))
                .flatMap(loanApplication ->
                        loanTypeStatus.findLoanTypeById(loanApplication.getLoanType().getLoanTypeId())
                                .zipWith(loanTypeStatus.findStatusById(statusId))
                                .flatMap(tuple -> {
                                    var loanType = tuple.getT1();
                                    var status = tuple.getT2();

                                    loanApplication.setLoanType(loanType);
                                    loanApplication.setStatus(status);

                                    return loanApplicationRepository.update(loanApplication)
                                            .flatMap(updated -> {
                                                updated.setLoanType(loanType);
                                                updated.setStatus(status);

                                                LoanApplicationEvent event = new LoanApplicationEvent(
                                                        updated.getLoanApplicationId(),
                                                        updated.getDocumentId(),
                                                        updated.getEmail(),
                                                        updated.getStatus().getDescription(),
                                                        updated.getLoanType().getName(),
                                                        updated.getAmount()
                                                );

                                                return loanApplicationEventRepository.notify(event)
                                                        .thenReturn(updated);
                                            });
                                })
                );
    }

    public Mono<LoanApplication> updateLambdaLoanStatus(Integer loanApplicationId, Integer statusId) {
        return loanApplicationRepository.findById(loanApplicationId)
                .switchIfEmpty(Mono.error(new DomainException(LoanApplicationMessages.LOAN_APPLICATION_NO_EXIST)))
                .flatMap(loanApplication -> loanTypeStatus.findStatusById(statusId)
                        .flatMap(status -> {
                            loanApplication.setStatus(status);
                            return loanApplicationRepository.update(loanApplication);
                        })
                );
    }


}
