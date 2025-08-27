package co.com.bancolombia.usecase.find_status_use_case;

import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationMessages;
import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.model.status.gateways.StatusRepository;
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
class FindStatusUseCaseTest {

    @Mock
    private StatusRepository statusRepository;

    private FindStatusUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindStatusUseCase(statusRepository);
    }

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

}
