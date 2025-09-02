package co.com.bancolombia.config;

import co.com.bancolombia.model.loan_application.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.created_loan_application_use_case.CreatedLoanApplicationUseCase;
import co.com.bancolombia.usecase.find_loan_status_and_type.FindLoanTypeUseCase;
import co.com.bancolombia.usecase.find_loans_by_status_use_case.FindLoansByStatusUseCase;
import co.com.bancolombia.usecase.find_status_use_case.FindStatusUseCase;
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
            FindStatusUseCase findStatusUseCase,
            FindLoanTypeUseCase findLoanTypeUseCase,
            LoanApplicationValidator loanApplicationValidator,
            UserRepository userRepository

    ) {
        return new CreatedLoanApplicationUseCase(
                loanApplicationRepository,
                findLoanTypeUseCase,
                findStatusUseCase,
                loanApplicationValidator,
                userRepository
        );
    }

    @Bean
    public FindLoansByStatusUseCase findLoansByStatusUseCase(
            LoanApplicationRepository  loanApplicationRepository,
            UserRepository userRepository,
            FindLoanTypeUseCase findLoanTypeUseCase,
            FindStatusUseCase findStatusUseCase
    ){
        return new FindLoansByStatusUseCase(
                loanApplicationRepository,
                userRepository,
                findStatusUseCase,
                findLoanTypeUseCase
        );
    }

    @Bean
    public LoanApplicationValidator loanApplicationValidator() {
        return new LoanApplicationValidator();
    }


}
