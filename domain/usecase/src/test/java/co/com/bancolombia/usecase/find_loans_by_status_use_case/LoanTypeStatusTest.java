package co.com.bancolombia.usecase.find_loans_by_status_use_case;

import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationMessages;
import co.com.bancolombia.model.loan_type.LoanType;
import co.com.bancolombia.model.loan_type.gateways.LoanTypeRepository;
import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.model.status.gateways.StatusRepository;
import co.com.bancolombia.usecase.loan_type_status.LoanTypeStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanTypeStatusTest {

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private LoanTypeRepository loanTypeRepository;

    private LoanTypeStatus useCase;

    @BeforeEach
    void setUp() {
        useCase = new LoanTypeStatus(loanTypeRepository, statusRepository);
    }

    // -------- TESTS STATUS --------
    @Test
    void shouldReturnStatusWhenFound() {
        Status status = new Status(1);
        when(statusRepository.findById(1)).thenReturn(Mono.just(status));

        StepVerifier.create(useCase.findStatusById(1))
                .expectNext(status)
                .verifyComplete();

        verify(statusRepository).findById(1);
    }

    @Test
    void shouldThrowExceptionWhenStatusNotFound() {
        when(statusRepository.findById(99)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.findStatusById(99))
                .expectErrorMatches(err -> err instanceof DomainException &&
                        err.getMessage().equals(LoanApplicationMessages.STATUS_NO_VALID))
                .verify();

        verify(statusRepository).findById(99);
    }

    // -------- TESTS LOAN TYPE --------
    @Test
    void shouldReturnLoanTypeWhenFound() {
        LoanType loanType = new LoanType(1);
        when(loanTypeRepository.findById(1)).thenReturn(Mono.just(loanType));

        StepVerifier.create(useCase.findLoanTypeById(1))
                .expectNext(loanType)
                .verifyComplete();

        verify(loanTypeRepository).findById(1);
    }

    @Test
    void shouldThrowExceptionWhenLoanTypeNotFound() {
        when(loanTypeRepository.findById(99)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.findLoanTypeById(99))
                .expectErrorMatches(err -> err instanceof DomainException &&
                        err.getMessage().equals(LoanApplicationMessages.LOAN_TYPE_NO_EXIST))
                .verify();

        verify(loanTypeRepository).findById(99);
    }
}