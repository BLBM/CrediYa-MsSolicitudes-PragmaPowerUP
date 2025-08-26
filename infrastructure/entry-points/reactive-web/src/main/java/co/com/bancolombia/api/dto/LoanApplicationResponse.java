package co.com.bancolombia.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public record LoanApplicationResponse(
        Double amount,
        Date timeLimit,
        String email,
        @Schema(defaultValue = "1", description = "estado de revision")
        String statusDescription,
        @Schema(defaultValue = "2", description = "prestamo solicitado")
        String loanTypeName) {

}
