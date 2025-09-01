package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.list_loans_dto.LoanApplicationSummaryResponse;
import co.com.bancolombia.model.loan_application_summary.LoanApplicationSummary;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LoanApplicationSummaryMapper {

    LoanApplicationSummaryMapper INSTANCE = Mappers.getMapper(LoanApplicationSummaryMapper.class);

    LoanApplicationSummaryResponse toDto(LoanApplicationSummary loanApplicationSummary);
}
