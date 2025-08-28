package co.com.bancolombia.usecase.created_loan_application_use_case;

import co.com.bancolombia.model.loan_application.LoanApplication;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationConstants;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loan_type.LoanType;
import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.usecase.find_loan_type_use_case.FindLoanTypeUseCase;
import co.com.bancolombia.usecase.find_status_use_case.FindStatusUseCase;
import co.com.bancolombia.usecase.loan_application_validator_use_case.LoanApplicationValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreatedLoanApplicationUseCase {

    private final LoanApplicationRepository loanApplicationRepository;
    private final FindLoanTypeUseCase findLoanTypeUseCase;
    private final FindStatusUseCase findStatusUseCase;
    private final LoanApplicationValidator  loanApplicationValidator;

    public Mono<LoanApplication> execute(LoanApplication loanApplication) {
        return Mono.defer(() -> {
            loanApplicationValidator.validateLoanApplication(loanApplication);
            loanApplication.setStatus(new Status(LoanApplicationConstants.INITIAL_STATUS));

            return findLoanTypeUseCase.findLoanTypeById(loanApplication.getLoanType().getLoanTypeId())
                    .flatMap(loanType ->
                            findStatusUseCase.findStatusById(loanApplication.getStatus().getStatusId())
                                    .flatMap(status -> {
                                        loanApplication.setLoanType(loanType);
                                        loanApplication.setStatus(status);

                                        return loanApplicationRepository.save(loanApplication)
                                                .map(loanApplicationSaved -> {
                                                    loanApplicationSaved.setLoanType(loanType);
                                                    loanApplicationSaved.setStatus(status);
                                                    return loanApplicationSaved;
                                                });
                                    })
                    );
        });
    }

}
