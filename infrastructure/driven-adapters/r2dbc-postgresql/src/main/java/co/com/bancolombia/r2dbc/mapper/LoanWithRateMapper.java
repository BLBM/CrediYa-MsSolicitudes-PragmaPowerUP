package co.com.bancolombia.r2dbc.mapper;


import co.com.bancolombia.model.loanwithrate.LoanWithRate;
import co.com.bancolombia.r2dbc.dto.LoanWithRateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LoanWithRateMapper {

    LoanWithRateMapper INSTANCE = Mappers.getMapper(LoanWithRateMapper.class);

    @Mapping(target = "loanid", source = "loanid")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "timelimit", source = "timelimit")
    @Mapping(target = "interestrate", source = "interestrate")
    LoanWithRate toDomain(LoanWithRateDTO dto);
}