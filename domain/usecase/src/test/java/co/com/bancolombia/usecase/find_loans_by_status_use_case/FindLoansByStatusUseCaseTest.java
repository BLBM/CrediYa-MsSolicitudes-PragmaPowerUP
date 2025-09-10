package co.com.bancolombia.usecase.find_loans_by_status_use_case;

import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.model.loan_application.LoanApplication;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationConstants;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationMessages;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loan_type.LoanType;
import co.com.bancolombia.model.loanwithrate.LoanWithRate;
import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.loan_type_status.LoanTypeStatus;
import co.com.bancolombia.usecase.util.LoanCalculationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FindLoansByStatusUseCaseTest {

    @Mock
    private LoanApplicationRepository loanApplicationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoanTypeStatus loanTypeStatus;

    @Mock
    private LoanCalculationService loanCalculationService;

    @InjectMocks
    private FindLoansByStatusUseCase useCase;


    @Test
    void shouldReturnLoanApplicationSummaryWithTotalDebt() {
        LocalDate currentDate = LocalDate.now();

        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setEmail("test@email.com");
        loanApplication.setAmount(5000.0);
        loanApplication.setTimeLimit(currentDate);
        loanApplication.setLoanType(new LoanType(1));
        loanApplication.setStatus(new Status(1));

        User user = new User();
        user.setEmail("test@email.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBaseSalary(2000.0);

        LoanType loanType = new LoanType(1);
        loanType.setName("Personal Loan");
        loanType.setInterestRate(5.5);

        Status status = new Status(1);
        status.setDescription("APPROVED");

        LoanWithRate loan1 = new LoanWithRate(1, 1000.0, currentDate.plusMonths(12), 10.0);
        LoanWithRate loan2 = new LoanWithRate(2, 2000.0, currentDate.plusMonths(24), 8.0);

        when(userRepository.findByEmail("test@email.com")).thenReturn(Mono.just(user));
        when(loanTypeStatus.findLoanTypeById(1)).thenReturn(Mono.just(loanType));
        when(loanTypeStatus.findStatusById(1)).thenReturn(Mono.just(status));
        when(loanApplicationRepository.findLoansWithRateByStatus("test@email.com",
                LoanApplicationConstants.APPROVED_STATUS)).thenReturn(Flux.just(loan1, loan2));
        when(loanCalculationService.calculateApproximateMonthlyDebt(1000.0, 10.0, loan1.getTimelimit())).thenReturn(100.0);
        when(loanCalculationService.calculateApproximateMonthlyDebt(2000.0, 8.0, loan2.getTimelimit())).thenReturn(200.0);

        StepVerifier.create(useCase.buildSummary(loanApplication))
                .assertNext(summary -> {
                    assertEquals(5000.0, summary.getAmount());
                    assertEquals(currentDate, summary.getTimeLimit());
                    assertEquals("Personal Loan", summary.getLoanType());
                    assertEquals(5.5, summary.getInterestRate());
                    assertEquals("APPROVED", summary.getStatus());
                    assertEquals("John Doe", summary.getName());
                    assertEquals(2000.0, summary.getBaseSalary());
                    assertEquals("test@email.com", summary.getEmail());
                    assertEquals(300.0, summary.getTotalDebt());
                })
                .verifyComplete();

        verify(loanCalculationService).calculateApproximateMonthlyDebt(1000.0, 10.0, loan1.getTimelimit());
        verify(loanCalculationService).calculateApproximateMonthlyDebt(2000.0, 8.0, loan2.getTimelimit());
    }

    @Test
    void shouldFailBuildSummaryWhenUserNotFound() {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setEmail("ghost@email.com");
        loanApplication.setLoanType(new LoanType(1));
        loanApplication.setStatus(new Status(1));

        when(userRepository.findByEmail("ghost@email.com")).thenReturn(Mono.empty());
        when(loanTypeStatus.findLoanTypeById(1)).thenReturn(Mono.just(new LoanType(1)));
        when(loanTypeStatus.findStatusById(1)).thenReturn(Mono.just(new Status(1)));
        when(loanApplicationRepository.findLoansWithRateByStatus(anyString(), anyInt())).thenReturn(Flux.empty());

        StepVerifier.create(useCase.buildSummary(loanApplication))
                .expectErrorSatisfies(error -> {
                    assertTrue(error instanceof DomainException);
                    assertEquals(LoanApplicationMessages.USER_NO_EXIST, error.getMessage());
                })
                .verify();
    }

    @Test
    void shouldFailBuildSummaryWhenLoanTypeNotFound() {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setEmail("loan@email.com");
        loanApplication.setLoanType(new LoanType(99));
        loanApplication.setStatus(new Status(1));

        User user = new User();
        user.setEmail("loan@email.com");

        when(userRepository.findByEmail("loan@email.com")).thenReturn(Mono.just(user));
        when(loanTypeStatus.findLoanTypeById(99)).thenReturn(Mono.error(new DomainException(LoanApplicationMessages.LOAN_TYPE_NO_EXIST)));
        when(loanTypeStatus.findStatusById(any())).thenReturn(Mono.just(new Status(1)));
        when(loanApplicationRepository.findLoansWithRateByStatus(anyString(), anyInt())).thenReturn(Flux.empty());

        StepVerifier.create(useCase.buildSummary(loanApplication))
                .expectError(DomainException.class)
                .verify();
    }

    @Test
    void shouldFailBuildSummaryWhenStatusNotFound() {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setEmail("loan@email.com");
        loanApplication.setLoanType(new LoanType(1));
        loanApplication.setStatus(new Status(99));

        User user = new User();
        user.setEmail("loan@email.com");

        LoanType loanType = new LoanType(1);

        when(userRepository.findByEmail("loan@email.com")).thenReturn(Mono.just(user));
        when(loanTypeStatus.findLoanTypeById(1)).thenReturn(Mono.just(loanType));
        when(loanTypeStatus.findStatusById(99)).thenReturn(Mono.error(new DomainException(LoanApplicationMessages.STATUS_NO_VALID)));
        when(loanApplicationRepository.findLoansWithRateByStatus(anyString(), anyInt())).thenReturn(Flux.empty());

        StepVerifier.create(useCase.buildSummary(loanApplication))
                .expectError(DomainException.class)
                .verify();
    }


}
