package co.com.bancolombia.api.dto;

import co.com.bancolombia.api.common.SwaggerConstant;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public record LoanApplicationResponse(
        Double amount,
        Date timeLimit,
        String documentId,
        String email,
        @Schema(defaultValue = SwaggerConstant.LOAN_TYPE_DEFAULT, description = SwaggerConstant.LOAN_TYPE_DEFAULT_DESCRIPTION)
        String statusDescription,
        @Schema(defaultValue = SwaggerConstant.STATUS_DEFAULT, description = SwaggerConstant.STATUS_DEFAULT_DESCRIPTION)
        String loanTypeName) {

}
