package co.com.bancolombia.model.loan_type;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

 class LoanTypeTest {

    @Test
    @DisplayName("create Loan type")
    void loanTypeTest(){
        LoanType loanType = LoanType.builder()
                .name("loan")
                .minAmount(1.0)
                .maxAmount(2000.0)
                .interestRate(1.0)
                .automaticValidation(true)
                .build();

        assertNotNull(loanType, "loan type is null");
        assertEquals("loan",loanType.getName());
        assertEquals(1.0,loanType.getMinAmount(),0);
        assertEquals(2000.0,loanType.getMaxAmount(),0);
        assertEquals(1.0,loanType.getInterestRate(),0);
        assertEquals(true,loanType.getAutomaticValidation());
    }


    @Test
    @DisplayName("loan_type builder id")
    void loanTypeBuilderTest(){

        int expectedId = 1;

        LoanType loanType = new LoanType(expectedId);

        assertNotNull(loanType, "loan type is null");
        assertEquals(expectedId,loanType.getLoanTypeId());
        assertNull(loanType.getMinAmount(),"min amount is null");
        assertNull(loanType.getMaxAmount(),"max amount is null");
        assertNull(loanType.getInterestRate(),"interest rate is null");
        assertNull(loanType.getAutomaticValidation(),"automatic validation is null");


    }
}
