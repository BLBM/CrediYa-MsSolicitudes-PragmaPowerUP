package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.list_loans_dto.LoanApplicationSummaryResponse;
import co.com.bancolombia.model.loan_application_summary.LoanApplicationSummary;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface LoanApplicationSummaryMapper {

    LoanApplicationSummaryMapper INSTANCE = Mappers.getMapper(LoanApplicationSummaryMapper.class);

    LoanApplicationSummaryResponse toDto(LoanApplicationSummary loanApplicationSummary);

    List<LoanApplicationSummaryResponse> toDtoList(List<LoanApplicationSummary> domainList);
}
