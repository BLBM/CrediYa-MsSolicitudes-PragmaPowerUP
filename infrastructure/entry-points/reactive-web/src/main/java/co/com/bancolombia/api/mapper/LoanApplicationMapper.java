package co.com.bancolombia.api.mapper;


import co.com.bancolombia.api.dto.LoanApplicationRequest;
import co.com.bancolombia.api.dto.LoanApplicationResponse;
import co.com.bancolombia.model.loan_application.LoanApplication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LoanApplicationMapper {

    LoanApplicationMapper Instance = Mappers.getMapper(LoanApplicationMapper.class);

    @Mapping(target = "loanApplicationId", ignore = true)
    LoanApplication toDomain(LoanApplicationRequest loanApplicationRequest);

    @Mapping(source = "status.description", target = "statusDescription")
    @Mapping(source = "loanType.name", target = "loanTypeName")
    LoanApplicationResponse toResponse(LoanApplication loanApplication);
}
