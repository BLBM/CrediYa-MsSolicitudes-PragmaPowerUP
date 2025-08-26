package co.com.bancolombia.usecase.created_loan_application_use_case;

import co.com.bancolombia.model.loan_application.LoanApplication;
import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loan_type.LoanType;
import co.com.bancolombia.model.loan_type.gateways.LoanTypeRepository;
import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.model.status.gateways.StatusRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreatedLoanApplicationUseCase {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final StatusRepository statusRepository;


    public Mono<LoanApplication> execute(LoanApplication loanApplication){

        Mono<LoanType> loanTypeMono = loanTypeRepository.findById(loanApplication.getLoanType().getLoanTypeId())
                .switchIfEmpty(Mono.error(new DomainException("el tipo prestamo no existe")));

        Mono<Status> statusMono = statusRepository.findById(loanApplication.getStatus().getStatusId())
                .switchIfEmpty(Mono.error(new IllegalStateException("No se encontró el estado inicial en BD. Verifique configuración.")));

        return Mono.zip(loanTypeMono,statusMono
        ).flatMap(loanTypeStatusTuple -> {
            LoanType loanTypeTuple = loanTypeStatusTuple.getT1();
            Status statusTuple = loanTypeStatusTuple.getT2();

            loanApplication.setLoanType(loanTypeTuple);
            loanApplication.setStatus(statusTuple);

            return loanApplicationRepository.save(loanApplication)
                    .map(loanApplicationSaved -> {
                        loanApplicationSaved.setLoanType(loanTypeTuple);
                        loanApplicationSaved.setStatus(statusTuple);
                        return loanApplicationSaved;
                    });
        });
    }
}
