package co.com.bancolombia.model.loan_application_event.gateways;

import co.com.bancolombia.model.loan_application_event.LoanApplicationEvent;
import reactor.core.publisher.Mono;

public interface LoanApplicationEventRepository {
    Mono<Void> publish(LoanApplicationEvent event);
}
