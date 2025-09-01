package co.com.bancolombia.consumer;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements UserRepository {
    private final WebClient client;


    @Override
    public Mono<User> findByEmail(String email) {
        return client
                .get()
                .uri("/users/find?email={email}", email)
                .retrieve()
                .bodyToMono(User.class);
    }

}
