package co.com.bancolombia.api.controller;


import co.com.bancolombia.api.common.RequestMappingConstant;
import co.com.bancolombia.api.common.SwaggerConstant;
import co.com.bancolombia.api.dto.loan_application_dto.LoanApplicationRequest;
import co.com.bancolombia.api.dto.loan_application_dto.LoanApplicationResponse;
import co.com.bancolombia.api.mapper.LoanApplicationMapper;
import co.com.bancolombia.logconstants.LogConstants;
import co.com.bancolombia.usecase.created_loan_application_use_case.CreatedLoanApplicationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(RequestMappingConstant.GLOBAL_URL)
public class LoanApplicationController {

    private final CreatedLoanApplicationUseCase createdLoanApplicationUseCase;

    @PostMapping(path =  RequestMappingConstant.LOAN_APPLICATION_URL)
    @Operation(summary = SwaggerConstant.SUMMARY_LOAN_APPLICATION)
    @PreAuthorize("hasAnyRole('USER')")
    public Mono<LoanApplicationResponse> createLoanApplication(@RequestBody LoanApplicationRequest loanApplicationRequest, Authentication authentication){
        log.info(LogConstants.REQUEST_RECEIVED,loanApplicationRequest);

        String email = authentication.getName();

        return createdLoanApplicationUseCase.execute(LoanApplicationMapper.INSTANCE.toDomain(loanApplicationRequest),email)
                .doOnSuccess(loanApp -> log.info(LogConstants.SUCCESSFUL_APPLICATION, loanApp))
                .doOnError(e-> log.error(LogConstants.ERROR_PROCESS))
                .map(LoanApplicationMapper.INSTANCE::toResponse);
    }

    @PostMapping(path = RequestMappingConstant.LOAN_APPLICATION_LIST_URL)
    @Operation(summary = SwaggerConstant.SUMMARY_LOAN_APPLICATION_LIST)
    @PreAuthorize("hasAnyRole('ADVISER')")
    public Mono<LoanApplicationResponse> findLoanApplicationByStatus(@RequestBody LoanApplicationRequest loanApplicationRequest, Authentication authentication) {
        return ;
    }
}
