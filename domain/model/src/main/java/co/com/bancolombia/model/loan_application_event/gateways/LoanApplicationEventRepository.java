package co.com.bancolombia.model.loan_application_event.gateways;

import co.com.bancolombia.model.loan_application_event.LoanApplicationEvent;
import co.com.bancolombia.model.loan_validation_message.LoanValidationMessage;
import reactor.core.publisher.Mono;


public interface LoanApplicationEventRepository {
    Mono<Void> notify(LoanApplicationEvent event);
    Mono<Void> validate(LoanValidationMessage event);
}
