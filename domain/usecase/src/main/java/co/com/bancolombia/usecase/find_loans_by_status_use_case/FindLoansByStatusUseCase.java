package co.com.bancolombia.usecase.find_loans_by_status_use_case;



import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.model.loan_application.LoanApplication;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationConstants;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationMessages;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loan_application_summary.LoanApplicationSummary;
import co.com.bancolombia.model.loan_type.LoanType;
import co.com.bancolombia.model.loanwithrate.LoanWithRate;
import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;

import co.com.bancolombia.usecase.loan_type_status.LoanTypeStatus;
import co.com.bancolombia.usecase.util.LoanCalculationService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class FindLoansByStatusUseCase {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanCalculationService loanCalculationService;
    private final UserRepository userRepository;
    private final LoanTypeStatus loanTypeStatus;


    public Flux<LoanApplicationSummary> execute(int status) {
        return loanApplicationRepository.findByStatusId(status)
                .flatMap(this::buildSummary, 10);
    }

    private Mono<LoanApplicationSummary> buildSummary(LoanApplication loanApplication) {
        Mono<User> userMono = userRepository.findByEmail(loanApplication.getEmail())
                .switchIfEmpty(Mono.error(new DomainException(LoanApplicationMessages.USER_NO_EXIST)));
        Mono<LoanType> loanTypeMono = loanTypeStatus.findLoanTypeById(
                loanApplication.getLoanType().getLoanTypeId());
        Mono<Status> statusMono = loanTypeStatus.findStatusById(
                loanApplication.getStatus().getStatusId());
        Flux<LoanWithRate> loanAppsApproved = loanApplicationRepository.findLoansWithRateByStatus(
                loanApplication.getEmail(), LoanApplicationConstants.APPROVED_STATUS);


        return Mono.zip(userMono, loanTypeMono, statusMono,loanAppsApproved.collectList())
                .map(tuple -> {
                    User user = tuple.getT1();
                    LoanType loanType = tuple.getT2();
                    Status status = tuple.getT3();
                    List<LoanWithRate> loansApproved = tuple.getT4();


                    double totalDebt = loansApproved.stream()
                            .mapToDouble(loan -> loanCalculationService.calculateApproximateMonthlyDebt(
                                    loan.getAmount(),
                                    loan.getInterestrate(),
                                    loan.getTimelimit()
                            ))
                            .sum();

                    totalDebt = Math.round(totalDebt * 100.0) / 100.0;

                    return LoanApplicationSummary.builder()
                            .amount(loanApplication.getAmount())
                            .timeLimit(loanApplication.getTimeLimit())
                            .loanType(loanType.getName())
                            .interestRate(loanType.getInterestRate())
                            .status(status.getDescription())
                            .name(user.getFirstName() + " " + user.getLastName())
                            .baseSalary(user.getBaseSalary())
                            .email(user.getEmail())
                            .totalDebt(totalDebt)
                            .build();
                });
    }
}