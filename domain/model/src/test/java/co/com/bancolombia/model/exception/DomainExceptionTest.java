package co.com.bancolombia.model.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DomainExceptionTest {

    @Test
    @DisplayName("Save Message")
    void saveMessage() {
        String message = "Domain Error";
        DomainException domainException = new DomainException(message);
        assertEquals(message,domainException.getMessage());
    }
}
