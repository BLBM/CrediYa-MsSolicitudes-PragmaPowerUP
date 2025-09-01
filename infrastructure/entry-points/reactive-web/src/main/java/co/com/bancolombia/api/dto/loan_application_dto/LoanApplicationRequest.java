package co.com.bancolombia.api.dto.loan_application_dto;


import co.com.bancolombia.api.common.SwaggerConstant;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public record LoanApplicationRequest(
        Double amount,
        Date timeLimit,
        @Schema(defaultValue = SwaggerConstant.LOAN_TYPE_REQUEST, description = SwaggerConstant.LOAN_TYPE_DEFAULT_DESCRIPTION)
        Integer loanTypeId) {
}
