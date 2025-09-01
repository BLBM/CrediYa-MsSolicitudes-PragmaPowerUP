package co.com.bancolombia.usecase.find_loans_by_status_use_case;


import co.com.bancolombia.model.loan_application.LoanApplication;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loan_application_summary.LoanApplicationSummary;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.find_loan_type_use_case.FindLoanTypeUseCase;
import co.com.bancolombia.usecase.find_status_use_case.FindStatusUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FindLoansByStatusUseCase {

    private final LoanApplicationRepository loanApplicationRepository;
    private final UserRepository userRepository;
    private final FindStatusUseCase findStatusUseCase;
    private final FindLoanTypeUseCase findLoanTypeUseCase;


    public Flux<LoanApplicationSummary> execute(int status, String email) {
        return loanApplicationRepository.findByStatusId(status)
                .flatMap(loan ->
                        userRepository.findByEmail(email)
                                .flatMap(user -> buildSummary(user, loan))
                );
    }

    private Mono<LoanApplicationSummary> buildSummary(User user, LoanApplication loanApplication) {
        return findLoanTypeUseCase.findLoanTypeById(loanApplication.getLoanType().getLoanTypeId())
                .flatMap(loanType ->
                        findStatusUseCase.findStatusById(loanApplication.getStatus().getStatusId())
                                .flatMap(status -> {
                                    loanApplication.setLoanType(loanType);
                                    loanApplication.setStatus(status);

                                    LoanApplicationSummary summary = LoanApplicationSummary.builder()
                                            .amount(loanApplication.getAmount())
                                            .timeLimit(loanApplication.getTimeLimit())
                                            .loanType(loanType.getName())
                                            .interestRate(loanType.getInterestRate())
                                            .status(status.getDescription())
                                            .name(user.getFirstName() + " " + user.getLastName())
                                            .baseSalary(user.getBaseSalary())
                                            .email(user.getEmail())
                                            .totalDebt(0.0)
                                            .build();

                                    return Mono.just(summary);
                                })
                );
    }



}
