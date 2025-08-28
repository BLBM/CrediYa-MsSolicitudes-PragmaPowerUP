package co.com.bancolombia.api.mapper;


import co.com.bancolombia.api.dto.LoanApplicationRequest;
import co.com.bancolombia.api.dto.LoanApplicationResponse;
import co.com.bancolombia.model.loan_application.LoanApplication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LoanApplicationMapper {

    LoanApplicationMapper INSTANCE = Mappers.getMapper(LoanApplicationMapper.class);

    @Mapping(target = "loanApplicationId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "loanType", expression = "java(new LoanType(loanApplicationRequest.loanTypeId()))")
    LoanApplication toDomain(LoanApplicationRequest loanApplicationRequest);

    @Mapping(source = "status.description", target = "statusDescription")
    @Mapping(source = "loanType.name", target = "loanTypeName")
    LoanApplicationResponse toResponse(LoanApplication loanApplication);
}
