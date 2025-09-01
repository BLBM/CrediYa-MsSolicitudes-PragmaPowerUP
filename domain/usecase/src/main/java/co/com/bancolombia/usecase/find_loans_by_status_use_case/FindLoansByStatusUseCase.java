package co.com.bancolombia.usecase.find_loans_by_status_use_case;



import co.com.bancolombia.model.loan_application.LoanApplication;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loan_application_summary.LoanApplicationSummary;
import co.com.bancolombia.model.loan_type.LoanType;
import co.com.bancolombia.model.status.Status;
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

    public Flux<LoanApplicationSummary> execute(int status) {
        return loanApplicationRepository.findByStatusId(status)
                .flatMap(this::buildSummary, 10);
    }

    private Mono<LoanApplicationSummary> buildSummary(LoanApplication loanApplication) {
        Mono<User> userMono = userRepository.findByEmail(loanApplication.getEmail());
        Mono<LoanType> loanTypeMono = findLoanTypeUseCase.findLoanTypeById(
                loanApplication.getLoanType().getLoanTypeId());
        Mono<Status> statusMono = findStatusUseCase.findStatusById(
                loanApplication.getStatus().getStatusId());

        return Mono.zip(userMono, loanTypeMono, statusMono)
                .map(tuple -> {
                    User user = tuple.getT1();
                    LoanType loanType = tuple.getT2();
                    Status status = tuple.getT3();

                    return LoanApplicationSummary.builder()
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
                });
    }
}