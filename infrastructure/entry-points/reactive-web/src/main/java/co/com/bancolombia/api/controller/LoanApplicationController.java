package co.com.bancolombia.api.controller;


import co.com.bancolombia.api.common.RequestMappingConstant;
import co.com.bancolombia.api.common.SwaggerConstant;
import co.com.bancolombia.api.dto.list_loans_dto.LoanApplicationSummaryResponse;
import co.com.bancolombia.api.dto.list_loans_dto.PageResponse;
import co.com.bancolombia.api.dto.loan_application_dto.LoanApplicationRequest;
import co.com.bancolombia.api.dto.loan_application_dto.LoanApplicationResponse;
import co.com.bancolombia.api.mapper.LoanApplicationMapper;
import co.com.bancolombia.api.service.LoanApplicationPageableService;
import co.com.bancolombia.logconstants.LogConstants;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationConstants;
import co.com.bancolombia.usecase.created_loan_application_use_case.CreatedLoanApplicationUseCase;
import co.com.bancolombia.usecase.update_loan_status_use_case.UpdateLoanStatusUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(RequestMappingConstant.GLOBAL_URL)
public class LoanApplicationController {

    private final CreatedLoanApplicationUseCase createdLoanApplicationUseCase;
    private final LoanApplicationPageableService pageableService;
    private final UpdateLoanStatusUseCase updateLoanStatusUseCase;


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

    @GetMapping(path = RequestMappingConstant.LOAN_APPLICATION_LIST_URL, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = SwaggerConstant.SUMMARY_LOAN_APPLICATION_LIST)
    @PreAuthorize("hasAnyRole('ADVISER')")
    public Mono<PageResponse<LoanApplicationSummaryResponse>> findLoanApplicationByStatus(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "3") int size,
            @RequestParam(name = "status", defaultValue = "1") Integer status) {

        log.info(LogConstants.REQUEST_RECEIVED_LOANS_BY_STATUS, status);
        return pageableService.findByStatus(status, page, size);
    }

    @PutMapping(path = RequestMappingConstant.LOAN_APPLICATION_UPDATE_URL)
    @Operation(summary = SwaggerConstant.SUMMARY_LOAN_APPLICATION_UPDATE)
    @PreAuthorize("hasAnyRole('ADVISER')")
    public Mono<LoanApplicationResponse> updateLoanApplicationStatus(
            @RequestParam(name = "loanApplicationId") int loanApplicationId,
            @RequestParam(name = "status") int status){

        log.info(LogConstants.REQUEST_RECEIVED_UPDATE,loanApplicationId);
        return updateLoanStatusUseCase.updateLoanStatus(loanApplicationId, status)
                .doOnSuccess(loanApp -> log.info(LogConstants.SUCCESSFUL_APPLICATION_UPDATE, loanApplicationId))
                .doOnError(e-> log.error(LogConstants.ERROR_PROCESS_UPDATE,loanApplicationId))
                .map(LoanApplicationMapper.INSTANCE::toResponse);
    }
}
