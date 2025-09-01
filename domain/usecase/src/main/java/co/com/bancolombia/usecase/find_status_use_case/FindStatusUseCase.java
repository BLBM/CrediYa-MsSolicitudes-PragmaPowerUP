package co.com.bancolombia.usecase.find_status_use_case;

import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationMessages;
import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.model.status.gateways.StatusRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class FindStatusUseCase {

    private final StatusRepository statusRepository;

    public Mono<Status> findStatusById(Integer statusId){
        return statusRepository.findById(statusId).switchIfEmpty(Mono.error(new DomainException(LoanApplicationMessages.STATUS_NO_VALID)));
    }


}
