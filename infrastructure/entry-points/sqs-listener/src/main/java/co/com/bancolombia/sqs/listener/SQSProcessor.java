package co.com.bancolombia.sqs.listener;

import co.com.bancolombia.logconstants.LogConstants;
import co.com.bancolombia.sqs.listener.dto.UpdateLoanResponseDTO;
import co.com.bancolombia.usecase.update_loan_status_use_case.UpdateLoanStatusUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {

    private  final UpdateLoanStatusUseCase updateLoanStatusUseCase;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> apply(Message message) {

        log.info("=== SQS MESSAGE RECEIVED ===");
        log.info("Message ID: {}", message.messageId());
        log.info("Message Body: {}", message.body());
        log.info("Message Attributes: {}", message.messageAttributes());
        log.info("===============================");

        try {
            UpdateLoanResponseDTO dto = objectMapper.readValue(message.body(), UpdateLoanResponseDTO.class);
            log.info(LogConstants.RECEIVE_MESSAGE_SQS, dto);

            return updateLoanStatusUseCase.updateLambdaLoanStatus(dto.loanId(), dto.statusId())
                    .doOnSuccess(updated ->
                            log.info(LogConstants.SUCCESSFUL_UPDATE_LOAN, dto.loanId(), dto.statusId()))
                    .doOnError(error ->
                            log.error(LogConstants.ERROR_UPDATE_LOAN, dto.loanId(), error.getMessage()))
                    .then();
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

}
