package co.com.bancolombia.consumer;

import co.com.bancolombia.consumer.config.RestConsumeException;
import co.com.bancolombia.logconstants.LogConstants;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestConsumer implements UserRepository {
    private final WebClient client;


    @Override
    public Mono<User> findByEmail(String email) {
        log.info(LogConstants.CONSUME_API_AUTHENTICATION, email);
        return client
                .get()
                .uri("/users/find?email={email}", email)
                .retrieve()
                .bodyToMono(User.class);


    }

}
