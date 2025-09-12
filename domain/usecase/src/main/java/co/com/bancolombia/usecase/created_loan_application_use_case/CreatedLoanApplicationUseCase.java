package co.com.bancolombia.usecase.created_loan_application_use_case;

import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.model.loan_application.LoanApplication;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationConstants;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationMessages;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loan_application_event.gateways.LoanApplicationEventRepository;
import co.com.bancolombia.model.loan_type.LoanType;
import co.com.bancolombia.model.loan_validation_message.LoanValidationMessage;
import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.loan_type_status.LoanTypeStatus;
import co.com.bancolombia.usecase.util.LoanApplicationValidator;
import co.com.bancolombia.usecase.util.LoanCalculationService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RequiredArgsConstructor
public class CreatedLoanApplicationUseCase {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanTypeStatus loanTypeStatus;
    private final LoanApplicationValidator  loanApplicationValidator;
    private final UserRepository userRepository;
    private final LoanCalculationService loanCalculationService;
    private final LoanApplicationEventRepository loanApplicationEventRepository;

    public Mono<LoanApplication> execute(LoanApplication loanApplication, String email) {
        return Mono.defer(() -> {
            loanApplicationValidator.validateLoanApplication(loanApplication);
            loanApplication.setStatus(new Status(LoanApplicationConstants.INITIAL_STATUS));

            Mono<User> userMono = userRepository.findByEmail(email)
                    .switchIfEmpty(Mono.error(new DomainException(LoanApplicationMessages.USER_NO_EXIST)));

            Mono<LoanType> loanTypeMono = loanTypeStatus.findLoanTypeById(loanApplication.getLoanType().getLoanTypeId());
            Mono<Status> statusMono = loanTypeStatus.findStatusById(loanApplication.getStatus().getStatusId());

            return Mono.zip(userMono, loanTypeMono, statusMono)
                    .flatMap(tuple -> {
                        User user = tuple.getT1();
                        LoanType loanType = tuple.getT2();
                        Status status = tuple.getT3();

                        loanApplication.setEmail(user.getEmail());
                        loanApplication.setDocumentId(user.getDocumentId());
                        loanApplication.setLoanType(loanType);
                        loanApplication.setStatus(status);

                        return loanApplicationRepository.save(loanApplication)
                                .flatMap(saved -> {
                                    saved.setLoanType(loanType);
                                    saved.setStatus(status);

                                    if (Boolean.TRUE.equals(loanType.getAutomaticValidation())) {
                                        return handleAutomaticValidation(saved, user, loanType, status)
                                                .thenReturn(saved);
                                    }

                                    return Mono.just(saved);
                                });
                    });
        });
    }


      Mono<Double> calculateTotalDebt(String email) {
        return loanApplicationRepository.findLoansWithRateByStatus(
                        email,
                        LoanApplicationConstants.APPROVED_STATUS
                )
                .map(loan -> loanCalculationService.calculateApproximateMonthlyDebt(
                        loan.getAmount(),
                        loan.getInterestrate(),
                        loan.getTimelimit()
                ))
                .reduce(0.0, Double::sum)
                .map(total -> Math.round(total * 100.0) / 100.0);
    }


      Mono<Void> handleAutomaticValidation(LoanApplication loanApplication,
                                                 User user,
                                                 LoanType loanType,
                                                 Status status) {

        Integer timeLimitMonths =  loanCalculationService.calculateMonthsBetween(LocalDate.now(), loanApplication.getTimeLimit());
        return calculateTotalDebt(user.getEmail())
                .flatMap(totalDebt -> {
                    LoanValidationMessage event = LoanValidationMessage.builder()
                            .loanId(loanApplication.getLoanApplicationId())
                            .amount(loanApplication.getAmount())
                            .timeLimit(timeLimitMonths)
                            .loanType(loanType.getName())
                            .status(status.getDescription())
                            .interestRate(loanType.getInterestRate())
                            .email(user.getEmail())
                            .baseSalary(user.getBaseSalary())
                            .totalDebt(totalDebt)
                            .build();

                    return loanApplicationEventRepository.validate(event);
                });
    }





}
