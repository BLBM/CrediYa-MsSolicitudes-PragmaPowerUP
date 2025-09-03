package co.com.bancolombia.usecase.loan_type_status;

import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationMessages;
import co.com.bancolombia.model.loan_type.LoanType;
import co.com.bancolombia.model.loan_type.gateways.LoanTypeRepository;
import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.model.status.gateways.StatusRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanTypeStatus {

    private final LoanTypeRepository loanTypeRepository;

    private final StatusRepository statusRepository;

    public Mono<LoanType> findLoanTypeById(Integer loanTypeId) {
        return loanTypeRepository.findById(loanTypeId).switchIfEmpty(Mono.error(new DomainException(LoanApplicationMessages.LOAN_TYPE_NO_EXIST)));
    }

    public Mono<Status> findStatusById(Integer statusId){
        return statusRepository.findById(statusId).switchIfEmpty(Mono.error(new DomainException(LoanApplicationMessages.STATUS_NO_VALID)));
    }



}
