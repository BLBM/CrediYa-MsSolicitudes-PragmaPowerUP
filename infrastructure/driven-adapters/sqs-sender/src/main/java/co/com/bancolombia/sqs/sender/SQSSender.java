package co.com.bancolombia.sqs.sender;

import co.com.bancolombia.logconstants.LogConstants;
import co.com.bancolombia.model.loan_application_event.LoanApplicationEvent;
import co.com.bancolombia.model.loan_application_event.gateways.LoanApplicationEventRepository;
import co.com.bancolombia.model.loan_validation_message.LoanValidationMessage;
import co.com.bancolombia.sqs.sender.common.QueueAliasConstants;
import co.com.bancolombia.sqs.sender.config.SQSSenderProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.time.LocalDate;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements LoanApplicationEventRepository {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;
    private final ObjectMapper objectMapper;

    public Mono<String> send(String message, String queueAlias) {
        return Mono.fromCallable(() -> buildRequest(message, queueAlias))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug(LogConstants.SUCCESSFUL_SEND_EVENT, queueAlias, response.messageId()))
                .map(SendMessageResponse::messageId);
    }

    private SendMessageRequest buildRequest(String message, String queueAlias) {
        String queueUrl = properties.getQueueUrl(queueAlias);
        if (queueUrl == null) {
            throw new IllegalArgumentException("No queue configured for alias: " + queueAlias);
        }
        return SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message)
                .build();
    }

    @Override
    public Mono<Void> notify(LoanApplicationEvent event) {
        String queueAlias = QueueAliasConstants.LOAN_APPLICATION_EVENT.getMessage();

        return Mono.fromCallable(() -> objectMapper.writeValueAsString(event))
                .map(body -> buildRequest(body, queueAlias))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.info(LogConstants.SUCCESSFUL_LOAN_APPLICATION_EVENT, queueAlias, response.messageId()))
                .then();
    }

    @Override
    public Mono<Void> validate(LoanValidationMessage event) {
        log.info("Validating loan application event: {}", event);
        String queueValidateAlias = QueueAliasConstants.AUTOMATIC_VALIDATE_EVENT.getMessage();

        return Mono.fromCallable(()-> objectMapper.writeValueAsString(event))
                .map(body -> buildRequest(body,queueValidateAlias))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.info(LogConstants.SUCCESSFUL_VALIDATE_EVENT, queueValidateAlias, response.messageId()))
                .then();
    }
}
