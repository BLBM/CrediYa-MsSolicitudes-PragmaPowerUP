package co.com.bancolombia.usecase.created_loan_application_use_case;

import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.model.loan_application.LoanApplication;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationConstants;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationMessages;
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

import static org.junit.jupiter.api.Assertions.*;
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



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

     @Test
     void shouldCreateLoanApplicationSuccessfully() {
         // Arrange
         LoanApplication loanApplication = new LoanApplication();
         loanApplication.setLoanType(new LoanType(1));
         loanApplication.setStatus(new Status(LoanApplicationConstants.INITIAL_STATUS));

         LoanType loanType = new LoanType(1);
         Status status = new Status(LoanApplicationConstants.INITIAL_STATUS);

         when(findLoanTypeUseCase.findLoanTypeById(1)).thenReturn(Mono.just(loanType));
         when(findStatusUseCase.findStatusById(1)).thenReturn(Mono.just(status));
         when(loanApplicationRepository.save(any(LoanApplication.class)))
                 .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

         // Act & Assert
         StepVerifier.create(useCase.execute(loanApplication))
                 .assertNext(saved -> {
                     assertEquals(loanType, saved.getLoanType());
                     assertEquals(status, saved.getStatus());
                 })
                 .verifyComplete();

         // Verify
         verify(loanApplicationValidator, times(1)).validateLoanApplication(any());
         verify(findLoanTypeUseCase, times(1)).findLoanTypeById(1);
         verify(findStatusUseCase, times(1)).findStatusById(1);
         verify(loanApplicationRepository, times(1)).save(any(LoanApplication.class));
     }

     @Test
     void shouldFailWhenLoanTypeDoesNotExist() {
         LoanApplication loanApplication = new LoanApplication();
         loanApplication.setLoanType(new LoanType(99));
         loanApplication.setStatus(new Status(LoanApplicationConstants.INITIAL_STATUS));

         when(findLoanTypeUseCase.findLoanTypeById(99))
                 .thenReturn(Mono.error(new DomainException(LoanApplicationMessages.LOAN_TYPE_NO_EXIST)));

         StepVerifier.create(useCase.execute(loanApplication))
                 .expectErrorSatisfies(error -> {
                     assertTrue(error instanceof DomainException);
                     assertNotNull(error.getMessage());
                 })
                 .verify();

         verify(loanApplicationValidator, times(1)).validateLoanApplication(any());
         verify(findLoanTypeUseCase, times(1)).findLoanTypeById(99);
         verify(findStatusUseCase, never()).findStatusById(any());
         verify(loanApplicationRepository, never()).save(any());
     }


     @Test
     void shouldFailWhenStatusDoesNotExist() {
         LoanApplication loanApplication = new LoanApplication();
         loanApplication.setLoanType(new LoanType(1));
         loanApplication.setStatus(new Status(LoanApplicationConstants.INITIAL_STATUS));

         LoanType loanType = new LoanType(1);

         when(findLoanTypeUseCase.findLoanTypeById(1)).thenReturn(Mono.just(loanType));
         when(findStatusUseCase.findStatusById(LoanApplicationConstants.INITIAL_STATUS))
                 .thenReturn(Mono.error(new DomainException(LoanApplicationMessages.STATUS_NO_VALID)));

         StepVerifier.create(useCase.execute(loanApplication))
                 .expectError(DomainException.class)
                 .verify();

         verify(findLoanTypeUseCase, times(1)).findLoanTypeById(1);
         verify(findStatusUseCase, times(1)).findStatusById(LoanApplicationConstants.INITIAL_STATUS);
         verify(loanApplicationRepository, never()).save(any());
     }


}
