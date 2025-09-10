package co.com.bancolombia.config;

import co.com.bancolombia.model.loan_application.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loan_application_event.gateways.LoanApplicationEventRepository;
import co.com.bancolombia.model.loan_type.gateways.LoanTypeRepository;
import co.com.bancolombia.model.status.gateways.StatusRepository;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.created_loan_application_use_case.CreatedLoanApplicationUseCase;
import co.com.bancolombia.usecase.find_loans_by_status_use_case.FindLoansByStatusUseCase;
import co.com.bancolombia.usecase.loan_type_status.LoanTypeStatus;
import co.com.bancolombia.usecase.update_loan_status_use_case.UpdateLoanStatusUseCase;
import co.com.bancolombia.usecase.util.LoanApplicationValidator;
import co.com.bancolombia.usecase.util.LoanCalculationService;
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
            UserRepository userRepository,
            LoanApplicationEventRepository loanApplicationEventRepository,
            LoanCalculationService loanCalculationService

    ) {
        return new CreatedLoanApplicationUseCase(
                loanApplicationRepository,
                loanTypeStatus,
                loanApplicationValidator,
                userRepository,
                loanCalculationService,
                loanApplicationEventRepository
        );
    }

    @Bean
    public FindLoansByStatusUseCase findLoansByStatusUseCase(
            LoanApplicationRepository  loanApplicationRepository,
            LoanCalculationService loanCalculationService,
            UserRepository userRepository,
            LoanTypeStatus loanTypeStatus
    ){
        return new FindLoansByStatusUseCase(
                loanApplicationRepository,
                loanCalculationService,
                userRepository,
                loanTypeStatus
        );
    }

    @Bean
    public UpdateLoanStatusUseCase  updateLoanStatusUseCase(LoanApplicationRepository loanApplicationRepository,
                                                            LoanTypeStatus loanTypeStatus,
                                                            LoanApplicationEventRepository  loanApplicationEventRepository) {
        return new UpdateLoanStatusUseCase(loanApplicationRepository, loanTypeStatus, loanApplicationEventRepository);}

    @Bean
    public LoanTypeStatus loanTypeStatus(LoanTypeRepository loanTypeRepository, StatusRepository statusRepository)
    {return new LoanTypeStatus(loanTypeRepository,statusRepository);}

    @Bean
    public LoanApplicationValidator loanApplicationValidator() {
        return new LoanApplicationValidator();
    }

    @Bean
    public LoanCalculationService loanCalculationService(){
        return new LoanCalculationService();
    }


}
