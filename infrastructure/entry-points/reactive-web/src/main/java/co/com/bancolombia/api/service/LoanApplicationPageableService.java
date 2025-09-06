package co.com.bancolombia.api.service;

import co.com.bancolombia.api.dto.list_loans_dto.LoanApplicationSummaryResponse;
import co.com.bancolombia.api.dto.list_loans_dto.PageResponse;
import co.com.bancolombia.api.mapper.LoanApplicationSummaryMapper;
import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.usecase.find_loans_by_status_use_case.FindLoansByStatusUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanApplicationPageableService {

    private final FindLoansByStatusUseCase findLoansByStatusUseCase;

    public Mono<PageResponse<LoanApplicationSummaryResponse>> findByStatus(int status, int page, int size) {
        return findLoansByStatusUseCase.execute(status)
                .collectList()
                .map(all -> {
                    int totalElements = all.size();
                    int totalPages = (int) Math.ceil((double) totalElements / size);
                    int currentPage = Math.clamp(page, 1, Math.max(totalPages, 1));

                    int fromIndex = Math.min((currentPage - 1) * size, totalElements);
                    int toIndex = Math.min(fromIndex + size, totalElements);

                    List<LoanApplicationSummaryResponse> content =
                            LoanApplicationSummaryMapper.INSTANCE.toDtoList(all.subList(fromIndex, toIndex));

                    boolean hasPrevious = currentPage > 1;
                    boolean hasNext = currentPage < totalPages;
                    boolean first = currentPage == 1;
                    boolean last = currentPage >= totalPages;

                    return PageResponse.<LoanApplicationSummaryResponse>builder()
                            .content(content)
                            .page(currentPage)
                            .size(size)
                            .totalElements(totalElements)
                            .totalPages(totalPages)
                            .hasNext(hasNext)
                            .hasPrevious(hasPrevious)
                            .first(first)
                            .last(last)
                            .build();
                })
                .onErrorResume(DomainException.class, ex ->
                        Mono.<PageResponse<LoanApplicationSummaryResponse>>error(
                                new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage())
                        )
                )
                .doOnError(ex -> log.warn("Handled error: {}", ex.getMessage()));
    }




}
