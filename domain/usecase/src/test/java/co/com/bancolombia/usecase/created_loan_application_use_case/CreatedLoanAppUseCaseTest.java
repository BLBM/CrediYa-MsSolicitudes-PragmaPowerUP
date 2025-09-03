package co.com.bancolombia.usecase.created_loan_application_use_case;

import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.model.loan_application.LoanApplication;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationConstants;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationMessages;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loan_type.LoanType;
import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.loan_type_status.LoanTypeStatus;
import co.com.bancolombia.usecase.util.LoanApplicationValidator;
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
    private LoanTypeStatus findLoanTypeStatusUseCase;
    @Mock
    private LoanApplicationValidator loanApplicationValidator;
     @Mock
     private UserRepository userRepository;


     @InjectMocks
    private CreatedLoanApplicationUseCase useCase;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

     @Test
     void shouldCreateLoanApplicationSuccessfully() {
         LoanApplication loanApplication = new LoanApplication();
         loanApplication.setLoanType(new LoanType(1));
         loanApplication.setStatus(new Status(LoanApplicationConstants.INITIAL_STATUS));
         String email = "test@email.com";

         LoanType loanType = new LoanType(1);
         Status status = new Status(LoanApplicationConstants.INITIAL_STATUS);
         User user = new User();
         user.setEmail(email);
         user.setDocumentId("12345");

         when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));
         when(findLoanTypeStatusUseCase.findLoanTypeById(1)).thenReturn(Mono.just(loanType));
         when(findLoanTypeStatusUseCase.findStatusById(LoanApplicationConstants.INITIAL_STATUS))
                 .thenReturn(Mono.just(status));
         when(loanApplicationRepository.save(any(LoanApplication.class)))
                 .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

         StepVerifier.create(useCase.execute(loanApplication, email))
                 .assertNext(saved -> {
                     assertEquals(loanType, saved.getLoanType());
                     assertEquals(status, saved.getStatus());
                     assertEquals(email, saved.getEmail());
                     assertEquals("12345", saved.getDocumentId());
                 })
                 .verifyComplete();

         verify(userRepository).findByEmail(email);
         verify(loanApplicationValidator).validateLoanApplication(any());
         verify(findLoanTypeStatusUseCase).findLoanTypeById(1);
         verify(findLoanTypeStatusUseCase).findStatusById(LoanApplicationConstants.INITIAL_STATUS);
         verify(loanApplicationRepository).save(any());
     }


     @Test
     void shouldFailWhenUserNotFound() {
         LoanApplication loanApplication = new LoanApplication();
         loanApplication.setLoanType(new LoanType(1));
         loanApplication.setStatus(new Status(LoanApplicationConstants.INITIAL_STATUS));
         String email = "notfound@email.com";

         when(userRepository.findByEmail(email)).thenReturn(Mono.empty());

         StepVerifier.create(useCase.execute(loanApplication, email))
                 .expectErrorSatisfies(error -> {
                     assertTrue(error instanceof DomainException);
                     assertEquals(LoanApplicationMessages.USER_NO_EXIST, error.getMessage());
                 })
                 .verify();

         verify(userRepository).findByEmail(email);
         verify(loanApplicationValidator).validateLoanApplication(any());
         verify(findLoanTypeStatusUseCase, never()).findLoanTypeById(any());
         verify(findLoanTypeStatusUseCase, never()).findStatusById(any());
         verify(loanApplicationRepository, never()).save(any());
     }


     @Test
     void shouldFailWhenLoanTypeDoesNotExist() {
         LoanApplication loanApplication = new LoanApplication();
         loanApplication.setLoanType(new LoanType(99));
         loanApplication.setStatus(new Status(LoanApplicationConstants.INITIAL_STATUS));
         String email = "test@email.com";
         User user = new User();
         user.setEmail(email);
         user.setDocumentId("12345");

         when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));
         when(findLoanTypeStatusUseCase.findLoanTypeById(99))
                 .thenReturn(Mono.error(new DomainException(LoanApplicationMessages.LOAN_TYPE_NO_EXIST)));

         StepVerifier.create(useCase.execute(loanApplication, email))
                 .expectError(DomainException.class)
                 .verify();

         verify(userRepository).findByEmail(email);
         verify(loanApplicationValidator).validateLoanApplication(any());
         verify(findLoanTypeStatusUseCase).findLoanTypeById(99);
         verify(findLoanTypeStatusUseCase, never()).findStatusById(any());
         verify(loanApplicationRepository, never()).save(any());
     }


     @Test
     void shouldFailWhenStatusDoesNotExist() {
         LoanApplication loanApplication = new LoanApplication();
         loanApplication.setLoanType(new LoanType(1));
         loanApplication.setStatus(new Status(LoanApplicationConstants.INITIAL_STATUS));
         String email = "test@email.com";
         User user = new User();
         user.setEmail(email);
         user.setDocumentId("12345");

         LoanType loanType = new LoanType(1);

         when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));
         when(findLoanTypeStatusUseCase.findLoanTypeById(1)).thenReturn(Mono.just(loanType));
         when(findLoanTypeStatusUseCase.findStatusById(LoanApplicationConstants.INITIAL_STATUS))
                 .thenReturn(Mono.error(new DomainException(LoanApplicationMessages.STATUS_NO_VALID)));

         StepVerifier.create(useCase.execute(loanApplication, email))
                 .expectError(DomainException.class)
                 .verify();

         verify(userRepository).findByEmail(email);
         verify(findLoanTypeStatusUseCase).findLoanTypeById(1);
         verify(findLoanTypeStatusUseCase).findStatusById(LoanApplicationConstants.INITIAL_STATUS);
         verify(loanApplicationRepository, never()).save(any());
     }


}
