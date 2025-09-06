package co.com.bancolombia.usecase.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoanCalculationsServiceTest {

    private LoanCalculationService loanCalculationService;

    @BeforeEach
    void setUp() {
        loanCalculationService = new LoanCalculationService();
    }

    @Test
    void shouldCalculateApproximateMonthlyDebtCorrectly() {

        double amount = 1200.0;
        double annualInterest = 12.0;
        LocalDate timeLimit = LocalDate.now().plusMonths(12);


        double result = loanCalculationService.calculateApproximateMonthlyDebt(amount, annualInterest, timeLimit);

        long remainingMonths = 12;
        double monthlyInterest = (amount * (annualInterest / 100)) / 12;
        double amortization = amount / remainingMonths;
        double expected = monthlyInterest + amortization;


        assertEquals(expected, result, 0.001);
    }

    @Test
    void shouldReturnAmountIfTimeLimitIsNowOrPast() {

        double amount = 500.0;
        double annualInterest = 10.0;
        LocalDate timeLimit = LocalDate.now().minusDays(1);

        double result = loanCalculationService.calculateApproximateMonthlyDebt(amount, annualInterest, timeLimit);


        double monthlyInterest = (amount * (annualInterest / 100)) / 12;
        double amortization = amount / 1;
        double expected = monthlyInterest + amortization;


        assertEquals(expected, result, 0.001);
    }
}
