package co.com.bancolombia.api.controller;


import co.com.bancolombia.api.common.RequestMappingConstant;
import co.com.bancolombia.api.common.SwaggerConstant;
import co.com.bancolombia.api.dto.LoanApplicationRequest;
import co.com.bancolombia.api.dto.LoanApplicationResponse;
import co.com.bancolombia.api.mapper.LoanApplicationMapper;
import co.com.bancolombia.logconstants.LogConstants;
import co.com.bancolombia.usecase.created_loan_application_use_case.CreatedLoanApplicationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(RequestMappingConstant.URL_LOAN_APPLICATION)
public class LoanApplicationController {

    private final CreatedLoanApplicationUseCase createdLoanApplicationUseCase;

    @PostMapping
    @Operation(summary = SwaggerConstant.SUMMARY_LOAN_APPLICATION)
    @PreAuthorize("hasAnyRole('USER')")
    public Mono<LoanApplicationResponse> createLoanApplication(@RequestBody LoanApplicationRequest loanApplicationRequest){
        log.info(LogConstants.REQUEST_RECEIVED,loanApplicationRequest);

        return createdLoanApplicationUseCase.execute(LoanApplicationMapper.INSTANCE.toDomain(loanApplicationRequest))
                .doOnSuccess(loanApp -> log.info(LogConstants.SUCCESSFUL_APPLICATION, loanApp))
                .doOnError(e-> log.error(LogConstants.ERROR_PROCESS))
                .map(LoanApplicationMapper.INSTANCE::toResponse);
    }
}
