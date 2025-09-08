package co.com.bancolombia.usecase.update_loan_status_use_case;

import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.model.loan_application.LoanApplication;
import co.com.bancolombia.model.loan_type.LoanType;
import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationMessages;
import co.com.bancolombia.model.loan_application_event.gateways.LoanApplicationEventRepository;
import co.com.bancolombia.usecase.loan_type_status.LoanTypeStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateLoanStatusUseCaseTest {

    private LoanApplicationRepository loanApplicationRepository;
    private LoanTypeStatus loanTypeStatus;
    private LoanApplicationEventRepository loanApplicationEventRepository;

    private UpdateLoanStatusUseCase useCase;

    @BeforeEach
    void setup() {
        loanApplicationRepository = mock(LoanApplicationRepository.class);
        loanTypeStatus = mock(LoanTypeStatus.class);
        loanApplicationEventRepository = mock(LoanApplicationEventRepository.class);

        useCase = new UpdateLoanStatusUseCase(
                loanApplicationRepository,
                loanTypeStatus,
                loanApplicationEventRepository
        );
    }

    @Test
    void updateLoanStatus_success() {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setLoanApplicationId(1);
        loanApplication.setLoanType(new LoanType(10));
        loanApplication.setStatus(new Status(1));

        LoanType newLoanType = new LoanType(20);
        Status newStatus = new Status(2);

        when(loanApplicationRepository.findById(1)).thenReturn(Mono.just(loanApplication));
        when(loanTypeStatus.findLoanTypeById(10)).thenReturn(Mono.just(newLoanType));
        when(loanTypeStatus.findStatusById(2)).thenReturn(Mono.just(newStatus));
        when(loanApplicationRepository.update(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        when(loanApplicationEventRepository.publish(any())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateLoanStatus(1, 2))
                .expectNextMatches(updated ->
                        updated.getLoanType().equals(newLoanType) &&
                                updated.getStatus().equals(newStatus)
                )
                .verifyComplete();

        verify(loanApplicationRepository).findById(1);
        verify(loanTypeStatus).findLoanTypeById(10);
        verify(loanTypeStatus).findStatusById(2);
        verify(loanApplicationRepository).update(any());
        verify(loanApplicationEventRepository).publish(any());
    }

    @Test
    void updateLoanStatus_loanNotFound() {
        when(loanApplicationRepository.findById(1))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateLoanStatus(1, 2))
                .expectErrorMatches(throwable ->
                        throwable instanceof DomainException &&
                                throwable.getMessage().equals(LoanApplicationMessages.LOAN_APPLICATION_NO_EXIST)
                )
                .verify();

        verify(loanApplicationRepository).findById(1);
        verifyNoMoreInteractions(loanTypeStatus, loanApplicationRepository, loanApplicationEventRepository);
    }
}
