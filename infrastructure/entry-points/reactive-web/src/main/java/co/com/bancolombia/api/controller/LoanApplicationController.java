package co.com.bancolombia.api.controller;


import co.com.bancolombia.api.common.RequestMappingConstant;
import co.com.bancolombia.api.common.SwaggerConstant;
import co.com.bancolombia.api.dto.list_loans_dto.LoanApplicationSummaryResponse;
import co.com.bancolombia.api.dto.loan_application_dto.LoanApplicationRequest;
import co.com.bancolombia.api.dto.loan_application_dto.LoanApplicationResponse;
import co.com.bancolombia.api.mapper.LoanApplicationMapper;
import co.com.bancolombia.api.mapper.LoanApplicationSummaryMapper;
import co.com.bancolombia.logconstants.LogConstants;
import co.com.bancolombia.usecase.created_loan_application_use_case.CreatedLoanApplicationUseCase;
import co.com.bancolombia.usecase.find_loans_by_status_use_case.FindLoansByStatusUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(RequestMappingConstant.GLOBAL_URL)
public class LoanApplicationController {

    private final CreatedLoanApplicationUseCase createdLoanApplicationUseCase;
    private final FindLoansByStatusUseCase findLoansByStatusUseCase;

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
    public Flux<List<LoanApplicationSummaryResponse>> findLoanApplicationByStatus(@RequestParam("batchSize") int batchSize, @RequestParam("status") Integer status) {

        log.info(LogConstants.REQUEST_RECEIVED_LOANS_BY_STATUS, status);
        return findLoansByStatusUseCase.execute(status)
                .map(summary -> {
                        LoanApplicationSummaryResponse response = LoanApplicationSummaryMapper.INSTANCE.toDto(summary);
                        log.info(LogConstants.RESPONSE_MAPPED, response);
                        return response;
                })
                .buffer(batchSize)
                .delayElements(Duration.ofSeconds(1))
                .doOnComplete(() -> log.info(LogConstants.FLOW_COMPLETED_LOANS_BY_STATUS, status));
    }
}
