package co.com.bancolombia.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public record LoanApplicationResponse(
        Integer loanApplicationId,
        Double amount,
        Date timeLimit,
        String email,
        @Schema(defaultValue = "1", description = "estado de revision")
        String status,
        @Schema(defaultValue = "2", description = "prestamo solicitado")
        String loanType) {

}
