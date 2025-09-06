package co.com.bancolombia.consumer;


import co.com.bancolombia.logconstants.LogConstants;
import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationMessages;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestConsumer implements UserRepository {
    private final WebClient client;

    private final Map<String, Mono<User>> cache = new ConcurrentHashMap<>();

    @Override
    public Mono<User> findByEmail(String email) {
        return cache.computeIfAbsent(email, e ->
                fetchUserFromApi(e)
                        .doFinally(signal -> cache.remove(e))
                        .onErrorMap(err -> new DomainException(LoanApplicationMessages.USER_NO_EXIST))
                        .cache());
    }

    private Mono<User> fetchUserFromApi(String email) {
        log.info(LogConstants.CONSUME_API_AUTHENTICATION, email);
        return client
                .get()
                .uri("/users/find?email={email}", email)
                .retrieve()
                .bodyToMono(User.class);
    }

}
