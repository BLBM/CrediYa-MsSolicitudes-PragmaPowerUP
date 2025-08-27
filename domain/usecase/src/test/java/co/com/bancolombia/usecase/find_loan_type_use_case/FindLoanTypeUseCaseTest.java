package co.com.bancolombia.usecase.find_loan_type_use_case;

import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationMessages;
import co.com.bancolombia.model.loan_type.LoanType;
import co.com.bancolombia.model.loan_type.gateways.LoanTypeRepository;
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
class FindLoanTypeUseCaseTest {


    @Mock
    private LoanTypeRepository loanTypeRepository;

    private FindLoanTypeUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindLoanTypeUseCase(loanTypeRepository);
    }

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
