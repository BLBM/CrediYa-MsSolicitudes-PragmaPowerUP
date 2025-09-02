package co.com.bancolombia.usecase.util;

import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.model.loan_application.LoanApplication;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationMessages;
import co.com.bancolombia.model.loan_type.LoanType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LoanAppValidatorUseCaseTest {

    private LoanApplicationValidator validator;

    @BeforeEach
    void setUp() {
        validator = new LoanApplicationValidator();
    }

    private LoanApplication baseValidLoanApplication() {
        LocalDate date = LocalDate.now();

        LoanType loanType = LoanType.builder()
                .loanTypeId(1)
                .build();

        return LoanApplication.builder()
                .loanApplicationId(100)
                .loanType(loanType)
                .amount(1000.0)
                .timeLimit(date)
                .email("valid@email.com")
                .documentId("12345")
                .build();
    }

    @Test
    void shouldThrowWhenLoanTypeIsNull() {
        LoanApplication loan = baseValidLoanApplication();
        loan.setLoanType(new LoanType(null));

        DomainException ex = assertThrows(DomainException.class,
                () -> validator.validateLoanApplication(loan));

        assertEquals(LoanApplicationMessages.LOAN_TYPE_NO_VALID, ex.getMessage());
    }

    @Test
    void shouldThrowWhenAmountIsNullOrLessThanMin() {
        LoanApplication loanNullAmount = baseValidLoanApplication();
        loanNullAmount.setAmount(null);

        DomainException ex1 = assertThrows(DomainException.class,
                () -> validator.validateLoanApplication(loanNullAmount));
        assertEquals(LoanApplicationMessages.AMOUNT_NO_VALID, ex1.getMessage());

        LoanApplication loanLowAmount = baseValidLoanApplication();
        loanLowAmount.setAmount(0.5);

        DomainException ex2 = assertThrows(DomainException.class,
                () -> validator.validateLoanApplication(loanLowAmount));
        assertEquals(LoanApplicationMessages.AMOUNT_NO_VALID, ex2.getMessage());
    }

    @Test
    void shouldThrowWhenTimeLimitIsNull() {
        LoanApplication loan = baseValidLoanApplication();
        loan.setTimeLimit(null);

        DomainException ex = assertThrows(DomainException.class,
                () -> validator.validateLoanApplication(loan));

        assertEquals(LoanApplicationMessages.TIME_LIMIT_NO_VALID, ex.getMessage());
    }

    @Test
    void shouldThrowWhenEmailIsInvalid() {
        LoanApplication loan = baseValidLoanApplication();
        loan.setEmail("invalid-email");

        DomainException ex = assertThrows(DomainException.class,
                () -> validator.validateLoanApplication(loan));

        assertEquals(LoanApplicationMessages.ERROR_EMAIL_NO_VALID, ex.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    void shouldThrowWhenDocumentIdIsInvalid(String invalidDocId) {
        LoanApplication loan = baseValidLoanApplication();
        loan.setDocumentId(invalidDocId);

        DomainException ex = assertThrows(DomainException.class,
                () -> validator.validateLoanApplication(loan));

        assertEquals(LoanApplicationMessages.DOCUMENT_ID_NO_VALID, ex.getMessage());
    }

    @Test
    void shouldPassWhenLoanApplicationIsValid() {
        LoanApplication loan = baseValidLoanApplication();

        assertDoesNotThrow(() -> validator.validateLoanApplication(loan));
    }
}
