package co.com.bancolombia.usecase.created_loan_application_use_case;

import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.model.loan_application.LoanApplication;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loan_type.LoanType;
import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.usecase.find_loan_type_use_case.FindLoanTypeUseCase;
import co.com.bancolombia.usecase.find_status_use_case.FindStatusUseCase;
import co.com.bancolombia.usecase.loan_application_validator_use_case.LoanApplicationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;
 class CreatedLoanAppUseCaseTest {

    @Mock
    private LoanApplicationRepository loanApplicationRepository;
    @Mock
    private FindLoanTypeUseCase findLoanTypeUseCase;
    @Mock
    private FindStatusUseCase findStatusUseCase;
    @Mock
    private LoanApplicationValidator loanApplicationValidator;

    @InjectMocks
    private CreatedLoanApplicationUseCase useCase;

    private LoanApplication baseLoan;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        baseLoan = LoanApplication.builder()
                .loanApplicationId(100)
                .loanType(LoanType.builder().loanTypeId(1).build())
                .status(Status.builder().statusId(10).build())
                .amount(1000.0)
                .timeLimit(new java.util.Date())
                .email("test@email.com")
                .documentId("123456")
                .build();
    }

    @Test
    void shouldValidateAndSaveSuccessfully() {
        // given
        LoanType loanType = LoanType.builder().loanTypeId(1).name("Personal").build();
        Status status = Status.builder().statusId(10).name("APPROVED").build();

        when(findLoanTypeUseCase.findLoanTypeById(1)).thenReturn(Mono.just(loanType));
        when(findStatusUseCase.findStatusById(10)).thenReturn(Mono.just(status));
        when(loanApplicationRepository.save(any(LoanApplication.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        // when
        Mono<LoanApplication> result = useCase.execute(baseLoan);

        // then
        StepVerifier.create(result)
                .expectNextMatches(saved ->
                        saved.getLoanType().equals(loanType) &&
                                saved.getStatus().equals(status) &&
                                saved.getAmount().equals(1000.0)
                )
                .verifyComplete();

        verify(loanApplicationValidator).validateLoanApplication(baseLoan);
        verify(loanApplicationRepository).save(any(LoanApplication.class));
    }

    @Test
    void shouldErrorWhenLoanTypeNotFound() {
        when(findLoanTypeUseCase.findLoanTypeById(1)).thenReturn(Mono.error(new DomainException("LoanType not found")));
        when(findStatusUseCase.findStatusById(10)).thenReturn(Mono.just(new Status(10)));

        Mono<LoanApplication> result = useCase.execute(baseLoan);

        StepVerifier.create(result)
                .expectErrorMatches(err -> err instanceof DomainException &&
                        err.getMessage().equals("LoanType not found"))
                .verify();

        verify(loanApplicationValidator).validateLoanApplication(baseLoan);
        verify(loanApplicationRepository, never()).save(any());
    }

    @Test
    void shouldErrorWhenStatusNotFound() {
        when(findLoanTypeUseCase.findLoanTypeById(1)).thenReturn(Mono.just(new LoanType(1)));
        when(findStatusUseCase.findStatusById(10)).thenReturn(Mono.error(new DomainException("Status not found")));

        Mono<LoanApplication> result = useCase.execute(baseLoan);

        StepVerifier.create(result)
                .expectErrorMatches(err -> err instanceof DomainException &&
                        err.getMessage().equals("Status not found"))
                .verify();

        verify(loanApplicationValidator).validateLoanApplication(baseLoan);
        verify(loanApplicationRepository, never()).save(any());
    }

}
