package co.com.bancolombia.model.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

 class StatusTest
{
    @Test
    @DisplayName("Created status")
    void StatusCreatedTest()
    {
        Status status = Status.builder()
                .name("Pending")
                .description("Pending")
                .build();

        assertNotNull(status,"status null");
        assertEquals("Pending",status.getName());
        assertEquals("Pending",status.getDescription());
    }


    @Test
    @DisplayName("Created buildeid")
    void StatusCreateBuildeidTest()
    {
        int expectedId = 1;

        Status status = new Status(expectedId);

        assertNotNull(status,"status null");
        assertEquals(expectedId,status.getStatusId());
        assertNull(status.getName(),"status name null");
        assertNull(status.getDescription(),"status description null");

    }
}
