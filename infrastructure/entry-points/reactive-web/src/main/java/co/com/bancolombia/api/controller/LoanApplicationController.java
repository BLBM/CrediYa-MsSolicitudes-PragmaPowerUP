package co.com.bancolombia.api.controller;


import co.com.bancolombia.api.dto.LoanApplicationRequest;
import co.com.bancolombia.api.dto.LoanApplicationResponse;

import co.com.bancolombia.api.mapper.LoanApplicationMapper;
import co.com.bancolombia.usecase.created_loan_application_use_case.CreatedLoanApplicationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/solicitud")
public class LoanApplicationController {

    private final CreatedLoanApplicationUseCase createdLoanApplicationUseCase;

    @PostMapping
    public Mono<LoanApplicationResponse> createLoanApplication(@RequestBody LoanApplicationRequest loanApplicationRequest){
        log.info("ok {}",loanApplicationRequest);

        return createdLoanApplicationUseCase.execute(LoanApplicationMapper.Instance.toDomain(loanApplicationRequest))
                .doOnSuccess(loanApp -> log.info("ok {}", loanApp))
                .doOnError(e-> log.error("not ok"))
                .map(LoanApplicationMapper.Instance::toResponse);
    }
}
