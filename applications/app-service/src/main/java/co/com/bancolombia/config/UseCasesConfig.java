package co.com.bancolombia.config;

import co.com.bancolombia.model.loan_application.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loan_type.gateways.LoanTypeRepository;
import co.com.bancolombia.model.status.gateways.StatusRepository;
import co.com.bancolombia.usecase.created_loan_application_use_case.CreatedLoanApplicationUseCase;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "co.com.bancolombia.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

    @Bean
    @Primary
    public CreatedLoanApplicationUseCase createLoanApplicationUseCase(
            LoanApplicationRepository loanApplicationRepository,
            LoanTypeRepository loanTypeRepository,
            StatusRepository statusRepository
    ) {
        return new CreatedLoanApplicationUseCase(
                loanApplicationRepository,
                loanTypeRepository,
                statusRepository
        );
    }
}
