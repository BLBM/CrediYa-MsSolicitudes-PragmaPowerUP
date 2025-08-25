package co.com.bancolombia.r2dbc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.reactive.TransactionalOperator;

@Configuration
@EnableTransactionManagement
public class TransactionConfig {

    @Bean
    public TransactionalOperator transactionalOperator(ReactiveTransactionManager txManager){
        return  TransactionalOperator.create(txManager);
    }
}
