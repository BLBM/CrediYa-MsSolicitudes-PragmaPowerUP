package co.com.bancolombia.api.dto;


import java.util.Date;

public record LoanApplicationRequest(
        Double amount,
        Date timeLimit,
        String email,
        Integer loanTypeId) {
}
