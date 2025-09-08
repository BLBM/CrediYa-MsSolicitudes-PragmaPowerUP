package co.com.bancolombia.model.loan_application_event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoanApplicationEventTest {

    @Test
    void createLoanApplicationEvent() {
        LoanApplicationEvent event = LoanApplicationEvent.builder()
                .loanApplicationId(1)
                .documentId("12345")
                .email("test@example.com")
                .statusDescription("APPROVED")
                .loanTypeName("Personal")
                .amount(1000.0)
                .build();

        assertNotNull(event, "LoanApplicationEvent is null");
        assertEquals(1, event.getLoanApplicationId());
        assertEquals("12345", event.getDocumentId());
        assertEquals("test@example.com", event.getEmail());
        assertEquals("APPROVED", event.getStatusDescription());
        assertEquals("Personal", event.getLoanTypeName());
        assertEquals(1000.0, event.getAmount());
    }
}
