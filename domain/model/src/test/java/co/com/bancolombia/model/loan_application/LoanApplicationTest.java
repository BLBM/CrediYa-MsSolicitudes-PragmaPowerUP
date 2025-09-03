package co.com.bancolombia.model.loan_application;

import co.com.bancolombia.model.loan_type.LoanType;
import co.com.bancolombia.model.status.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;


class LoanApplicationTest {

    @Test
    @DisplayName("Create Loan Application")
    void createLoanApplication() {
        LocalDate limitDate = LocalDate.now();
        Status status = Status.builder()
                .name("PENDING")
                .description("PENDING")
                .build();

        LoanType loanType = LoanType.builder()
                .name("loan")
                .minAmount(1.0)
                .maxAmount(1500000.0)
                .interestRate(1.0)
                .automaticValidation(true)
                .build();


        LoanApplication loanApplication = LoanApplication.builder()
                .amount(150000.0)
                .timeLimit(limitDate)
                .documentId("111111")
                .email("test@gmail.com")
                .status(status)
                .loanType(loanType)
                .build();

        assertNotNull(loanApplication,"Loan Application is null");
        assertEquals(150000.0, loanApplication.getAmount());
        assertEquals(limitDate,loanApplication.getTimeLimit());
        assertEquals("111111",loanApplication.getDocumentId(), "111111");
        assertEquals("test@gmail.com",loanApplication.getEmail());
        assertEquals(status,loanApplication.getStatus());
        assertEquals(loanType, loanApplication.getLoanType());
    }
}
