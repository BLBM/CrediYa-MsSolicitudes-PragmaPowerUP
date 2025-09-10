package co.com.bancolombia.sqs.listener.dto;

public record UpdateLoanResponseDTO(
        Integer loanId,
        Integer statusId
) {
}
