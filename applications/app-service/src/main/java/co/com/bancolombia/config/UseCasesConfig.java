package co.com.bancolombia.config;

import co.com.bancolombia.model.loan_application.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loan_type.gateways.LoanTypeRepository;
import co.com.bancolombia.model.status.gateways.StatusRepository;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.created_loan_application_use_case.CreatedLoanApplicationUseCase;
import co.com.bancolombia.usecase.find_loans_by_status_use_case.FindLoansByStatusUseCase;
import co.com.bancolombia.usecase.loan_type_status.LoanTypeStatus;
import co.com.bancolombia.usecase.util.LoanApplicationValidator;
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
            LoanTypeStatus loanTypeStatus,
            LoanApplicationValidator loanApplicationValidator,
            UserRepository userRepository

    ) {
        return new CreatedLoanApplicationUseCase(
                loanApplicationRepository,
                loanTypeStatus,
                loanApplicationValidator,
                userRepository
        );
    }

    @Bean
    public FindLoansByStatusUseCase findLoansByStatusUseCase(
            LoanApplicationRepository  loanApplicationRepository,
            UserRepository userRepository,
            LoanTypeStatus loanTypeStatus
    ){
        return new FindLoansByStatusUseCase(
                loanApplicationRepository,
                userRepository,
                loanTypeStatus
        );
    }

    @Bean
    public LoanTypeStatus loanTypeStatus(LoanTypeRepository loanTypeRepository, StatusRepository statusRepository)
    {return new LoanTypeStatus(loanTypeRepository,statusRepository);}

    @Bean
    public LoanApplicationValidator loanApplicationValidator() {
        return new LoanApplicationValidator();
    }


}
