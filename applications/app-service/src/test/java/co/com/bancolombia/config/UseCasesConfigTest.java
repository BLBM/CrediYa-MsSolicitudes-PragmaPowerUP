package co.com.bancolombia.config;

import co.com.bancolombia.model.loan_application.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loan_type.gateways.LoanTypeRepository;
import co.com.bancolombia.model.status.gateways.StatusRepository;
import co.com.bancolombia.usecase.created_loan_application_use_case.CreatedLoanApplicationUseCase;
import co.com.bancolombia.usecase.find_loan_status_and_type.FindLoanTypeUseCase;
import co.com.bancolombia.usecase.find_status_use_case.FindStatusUseCase;
import co.com.bancolombia.usecase.util.LoanApplicationValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'Use Case' were found");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        public MyUseCase myUseCase() {
            return new MyUseCase();
        }

        @Bean
        public StatusRepository statusRepository() {return  Mockito.mock(StatusRepository.class);}

        @Bean
        public LoanTypeRepository loanTypeRepository() {return  Mockito.mock(LoanTypeRepository.class);}

        @Bean
        public LoanApplicationRepository loanApplicationRepository() {return  Mockito.mock(LoanApplicationRepository.class);}

        @Bean
        public CreatedLoanApplicationUseCase createdLoanApplicationUseCase() {return  Mockito.mock(CreatedLoanApplicationUseCase.class);}

        @Bean
        public FindLoanTypeUseCase findLoanTypeUseCase(){return  Mockito.mock(FindLoanTypeUseCase.class);}

        @Bean
        public FindStatusUseCase findStatusUseCase(){return  Mockito.mock(FindStatusUseCase.class);}

        @Bean
        public LoanApplicationValidator findLoanApplicationValidator(){return  Mockito.mock(LoanApplicationValidator.class);}
    }

    static class MyUseCase {
        public String execute() {
            return "MyUseCase Test";
        }
    }
}