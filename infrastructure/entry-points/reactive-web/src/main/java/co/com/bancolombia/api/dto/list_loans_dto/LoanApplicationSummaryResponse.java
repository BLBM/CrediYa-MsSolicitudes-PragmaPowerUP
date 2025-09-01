package co.com.bancolombia.api.dto.list_loans_dto;

import java.time.LocalDate;

public record LoanApplicationSummaryResponse(
        Double amount,
        LocalDate timeLimit,
        String email,
        String name,
        String loanType,
        String status,
        Double baseSalary,
        Double totalDebt
) {
}
