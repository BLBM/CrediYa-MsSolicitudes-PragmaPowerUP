package co.com.bancolombia.usecase.created_loan_application_use_case;

import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.model.loan_application.LoanApplication;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationConstants;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationMessages;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.find_loan_status_and_type.FindLoanTypeUseCase;
import co.com.bancolombia.usecase.find_status_use_case.FindStatusUseCase;
import co.com.bancolombia.usecase.util.LoanApplicationValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreatedLoanApplicationUseCase {

    private final LoanApplicationRepository loanApplicationRepository;
    private final FindLoanTypeUseCase findLoanTypeUseCase;
    private final FindStatusUseCase findStatusUseCase;
    private final LoanApplicationValidator  loanApplicationValidator;
    private final UserRepository userRepository;

    public Mono<LoanApplication> execute(LoanApplication loanApplication,String email) {
        return Mono.defer(() -> {
            loanApplicationValidator.validateLoanApplication(loanApplication);
            loanApplication.setStatus(new Status(LoanApplicationConstants.INITIAL_STATUS));

            return userRepository.findByEmail(email)
                    .switchIfEmpty(Mono.error(new DomainException(LoanApplicationMessages.USER_NO_EXIST)))
                    .flatMap(user -> {
                        loanApplication.setEmail(user.getEmail());
                        loanApplication.setDocumentId(user.getDocumentId());

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
        });
    }

}
