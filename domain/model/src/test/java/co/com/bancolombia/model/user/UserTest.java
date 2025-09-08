package co.com.bancolombia.model.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void createUser() {
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .documentId("123456")
                .baseSalary(3000.0)
                .build();

        assertNotNull(user, "User is null");
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("123456", user.getDocumentId());
        assertEquals(3000.0, user.getBaseSalary());
    }
}
