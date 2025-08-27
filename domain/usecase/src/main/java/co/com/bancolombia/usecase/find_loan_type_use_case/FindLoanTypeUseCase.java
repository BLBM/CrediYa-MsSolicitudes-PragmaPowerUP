package co.com.bancolombia.usecase.find_loan_type_use_case;

import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationMessages;
import co.com.bancolombia.model.loan_type.LoanType;
import co.com.bancolombia.model.loan_type.gateways.LoanTypeRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FindLoanTypeUseCase {

    private final LoanTypeRepository loanTypeRepository;


    public Mono<LoanType> findLoanTypeById(Integer loanTypeId) {
        return loanTypeRepository.findById(loanTypeId).switchIfEmpty(Mono.error(new DomainException(LoanApplicationMessages.LOAN_TYPE_NO_EXIST)));
    }


}
