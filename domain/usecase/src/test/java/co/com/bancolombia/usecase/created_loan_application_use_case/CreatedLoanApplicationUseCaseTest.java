package co.com.bancolombia.usecase.created_loan_application_use_case;

import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.model.loan_application.LoanApplication;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationConstants;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationMessages;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loan_application_event.gateways.LoanApplicationEventRepository;
import co.com.bancolombia.model.loan_type.LoanType;
import co.com.bancolombia.model.loanwithrate.LoanWithRate;
import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.loan_type_status.LoanTypeStatus;
import co.com.bancolombia.usecase.util.LoanApplicationValidator;
import co.com.bancolombia.usecase.util.LoanCalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 class CreatedLoanApplicationUseCaseTest {

    @Mock
    private LoanApplicationRepository loanApplicationRepository;
    @Mock
    private LoanCalculationService loanCalculationService;

    @Mock
    private LoanApplicationEventRepository loanApplicationEventRepository;
    @Mock
    private LoanTypeStatus loanTypeStatus;
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
         when(loanTypeStatus.findLoanTypeById(1)).thenReturn(Mono.just(loanType));
         when(loanTypeStatus.findStatusById(LoanApplicationConstants.INITIAL_STATUS))
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
         verify(loanTypeStatus).findLoanTypeById(1);
         verify(loanTypeStatus).findStatusById(LoanApplicationConstants.INITIAL_STATUS);
         verify(loanApplicationRepository).save(any());
     }

     @Test
     void shouldTriggerAutomaticValidationWhenEnabled() {
         LoanApplication loanApplication = new LoanApplication();
         loanApplication.setLoanType(new LoanType(1, "Personal",1000.00, 100000.00, 12.0 ,true ));
         loanApplication.setStatus(new Status(LoanApplicationConstants.INITIAL_STATUS));
         String email = "test@email.com";

         User user = new User();
         user.setEmail(email);
         user.setDocumentId("12345");

         LoanType loanType = new LoanType(1, "Personal",1000.00, 100000.00, 12.0 ,true);
         Status status = new Status(LoanApplicationConstants.INITIAL_STATUS);

         when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));
         when(loanTypeStatus.findLoanTypeById(1)).thenReturn(Mono.just(loanType));
         when(loanTypeStatus.findStatusById(LoanApplicationConstants.INITIAL_STATUS))
                 .thenReturn(Mono.just(status));

         when(loanApplicationRepository.save(any(LoanApplication.class)))
                 .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

         when(loanApplicationRepository.findLoansWithRateByStatus(email, LoanApplicationConstants.APPROVED_STATUS))
                 .thenReturn(Flux.just(new LoanWithRate(1, 1000.0, LocalDate.now().plusMonths(12), 10.0)));

         when(loanCalculationService.calculateApproximateMonthlyDebt(anyDouble(), anyDouble(), any()))
                 .thenReturn(100.0);
         when(loanApplicationEventRepository.validate(any())).thenReturn(Mono.empty());

         StepVerifier.create(useCase.execute(loanApplication, email))
                 .assertNext(saved -> {
                     assertEquals(loanType, saved.getLoanType());
                     assertEquals(status, saved.getStatus());
                     assertEquals(email, saved.getEmail());
                 })
                 .verifyComplete();

         verify(loanApplicationEventRepository).validate(any());
     }


     @Test
     void shouldFailWhenUserNotFound() {
         LoanApplication loanApplication = new LoanApplication();
         loanApplication.setLoanType(new LoanType(1));
         loanApplication.setStatus(new Status(LoanApplicationConstants.INITIAL_STATUS));
         String email = "notfound@email.com";


         doNothing().when(loanApplicationValidator).validateLoanApplication(any());
         when(userRepository.findByEmail(email)).thenReturn(Mono.empty());
         when(loanTypeStatus.findLoanTypeById(any())).thenReturn(Mono.just(new LoanType(1)));
         when(loanTypeStatus.findStatusById(any())).thenReturn(Mono.just(new Status(LoanApplicationConstants.INITIAL_STATUS)));

         StepVerifier.create(useCase.execute(loanApplication, email))
                 .expectErrorSatisfies(error -> {
                     assertTrue(error instanceof DomainException);
                     assertEquals(LoanApplicationMessages.USER_NO_EXIST, error.getMessage());
                 })
                 .verify();

         verify(loanApplicationRepository, never()).save(any());
         verify(loanApplicationValidator).validateLoanApplication(any());
         verify(userRepository).findByEmail(email);
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
         when(loanTypeStatus.findLoanTypeById(99))
                 .thenReturn(Mono.error(new DomainException(LoanApplicationMessages.LOAN_TYPE_NO_EXIST)));
         when(loanTypeStatus.findStatusById(anyInt()))
                 .thenReturn(Mono.just(new Status(LoanApplicationConstants.INITIAL_STATUS)));

         StepVerifier.create(useCase.execute(loanApplication, email))
                 .expectErrorSatisfies(error -> {
                     assertTrue(error instanceof DomainException);
                     assertEquals(LoanApplicationMessages.LOAN_TYPE_NO_EXIST, error.getMessage());
                 })
                 .verify();

         verify(userRepository).findByEmail(email);
         verify(loanApplicationValidator).validateLoanApplication(any());
         verify(loanTypeStatus).findLoanTypeById(99);
         verify(loanTypeStatus).findStatusById(anyInt());
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
         when(loanTypeStatus.findLoanTypeById(1)).thenReturn(Mono.just(loanType));
         when(loanTypeStatus.findStatusById(LoanApplicationConstants.INITIAL_STATUS))
                 .thenReturn(Mono.error(new DomainException(LoanApplicationMessages.STATUS_NO_VALID)));

         StepVerifier.create(useCase.execute(loanApplication, email))
                 .expectError(DomainException.class)
                 .verify();

         verify(userRepository).findByEmail(email);
         verify(loanTypeStatus).findLoanTypeById(1);
         verify(loanTypeStatus).findStatusById(LoanApplicationConstants.INITIAL_STATUS);
         verify(loanApplicationRepository, never()).save(any());
     }

     @Test
     void calculateTotalDebt_whenLoansExist_shouldReturnSum() {
         String email = "test@mail.com";
         LoanWithRate loan1 = new LoanWithRate(1, 1000.0, LocalDate.now().plusMonths(12), 10.0);
         LoanWithRate loan2 = new LoanWithRate(2, 2000.0, LocalDate.now().plusMonths(24), 8.0);

         when(loanApplicationRepository.findLoansWithRateByStatus(email, LoanApplicationConstants.APPROVED_STATUS))
                 .thenReturn(Flux.just(loan1, loan2));

         when(loanCalculationService.calculateApproximateMonthlyDebt(1000.0, 10.0, loan1.getTimelimit()))
                 .thenReturn(100.0);
         when(loanCalculationService.calculateApproximateMonthlyDebt(2000.0, 8.0, loan2.getTimelimit()))
                 .thenReturn(200.0);

         Mono<Double> result = useCase.calculateTotalDebt(email);

         StepVerifier.create(result)
                 .expectNext(300.0)
                 .verifyComplete();
     }

     @Test
     void calculateTotalDebt_whenNoLoans_shouldReturnZero() {
         String email = "empty@mail.com";
         when(loanApplicationRepository.findLoansWithRateByStatus(email, LoanApplicationConstants.APPROVED_STATUS))
                 .thenReturn(Flux.empty());

         Mono<Double> result = useCase.calculateTotalDebt(email);

         StepVerifier.create(result)
                 .expectNext(0.0)
                 .verifyComplete();
     }


}
