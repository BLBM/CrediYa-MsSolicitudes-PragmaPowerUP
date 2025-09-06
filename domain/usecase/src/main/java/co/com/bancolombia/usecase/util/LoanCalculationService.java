package co.com.bancolombia.usecase.util;



import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
public class LoanCalculationService {

    public double calculateApproximateMonthlyDebt(double amount, double annualInterest, LocalDate timeLimit) {
        long remainingMonths = Math.max(1, ChronoUnit.MONTHS.between(LocalDate.now(), timeLimit));
        double monthlyInterest = (amount * (annualInterest / 100)) / 12;
        double amortization = amount / remainingMonths;
        return monthlyInterest + amortization;
    }

}
