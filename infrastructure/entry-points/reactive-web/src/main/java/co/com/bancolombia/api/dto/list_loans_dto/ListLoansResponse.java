package co.com.bancolombia.api.dto.list_loans_dto;

import java.util.Date;

public record ListLoansResponse(
        Double amount,
        Date timeLimit,
        String email,
        String name,
        String loanType,
        String status,
        Double baseSalary,
        Double totalDebt
) {
}
