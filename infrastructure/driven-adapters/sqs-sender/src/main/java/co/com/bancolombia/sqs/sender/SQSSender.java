package co.com.bancolombia.sqs.sender;

import co.com.bancolombia.model.loan_application_event.LoanApplicationEvent;
import co.com.bancolombia.model.loan_application_event.gateways.LoanApplicationEventRepository;
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
                .doOnNext(response -> log.debug("Message sent to {} with id={}", queueAlias, response.messageId()))
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
    public Mono<Void> publish(LoanApplicationEvent event) {
        String queueAlias = QueueAliasConstants.LOAN_APPLICATION_EVENT.getMessage();

        return Mono.fromCallable(() -> objectMapper.writeValueAsString(event))
                .map(body -> buildRequest(body, queueAlias))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.info("Event published to {} with id {}", queueAlias, response.messageId()))
                .then();
    }
}
