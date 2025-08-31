package co.com.bancolombia.jwtimplementation.manager;

import co.com.bancolombia.jwtimplementation.provider.JwtProvider;
import co.com.bancolombia.logconstants.LogConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtProvider jwtProvider;
    public JwtAuthenticationManager(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Mono<Authentication> authenticate (Authentication authentication) {
        log.info(LogConstants.START_JJWT_PROCESS);
        return Mono.just(authentication)
                .map(auth -> jwtProvider.getClaims((auth.getCredentials().toString())))
                .log()
                .onErrorResume(e-> Mono.error(new Throwable("bad token")))
                .map(claims -> {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> rawRoles = claims.get("roles", List.class);

                    List<SimpleGrantedAuthority> authorities = rawRoles.stream()
                            .map(role -> (String) role.get("name"))
                            .map(SimpleGrantedAuthority::new)
                            .toList();

                    return new UsernamePasswordAuthenticationToken(
                            claims.getSubject(),
                            null,
                            authorities
                    );
                });
    }
}
