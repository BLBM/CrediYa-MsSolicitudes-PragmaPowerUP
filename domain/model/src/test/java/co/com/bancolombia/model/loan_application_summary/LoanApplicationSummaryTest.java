package co.com.bancolombia.model.loan_application_summary;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LoanApplicationSummaryTest {

    @Test
    void createLoanApplicationSummary() {
        LocalDate limitDate = LocalDate.now();

        LoanApplicationSummary summary = LoanApplicationSummary.builder()
                .amount(50000.0)
                .timeLimit(limitDate)
                .loanType("Personal")
                .status("APPROVED")
                .interestRate(1.5)
                .name("John Doe")
                .email("john.doe@example.com")
                .baseSalary(3000.0)
                .totalDebt(1000.0)
                .build();

        assertNotNull(summary, "LoanApplicationSummary is null");
        assertEquals(50000.0, summary.getAmount());
        assertEquals(limitDate, summary.getTimeLimit());
        assertEquals("Personal", summary.getLoanType());
        assertEquals("APPROVED", summary.getStatus());
        assertEquals(1.5, summary.getInterestRate());
        assertEquals("John Doe", summary.getName());
        assertEquals("john.doe@example.com", summary.getEmail());
        assertEquals(3000.0, summary.getBaseSalary());
        assertEquals(1000.0, summary.getTotalDebt());
    }
}
