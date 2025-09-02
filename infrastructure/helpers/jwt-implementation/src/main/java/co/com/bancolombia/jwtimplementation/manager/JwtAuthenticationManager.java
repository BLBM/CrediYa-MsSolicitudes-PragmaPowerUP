package co.com.bancolombia.jwtimplementation.manager;

import co.com.bancolombia.jwt_common.JwtMessages;
import co.com.bancolombia.jwtimplementation.exception.JwtException;
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
        log.info(JwtMessages.START_JJWT_PROCESS_AUTHENTICATE_MANAGER);
        return Mono.just(authentication)
                .map(auth -> {
                    if (auth.getCredentials() == null) {
                            throw new JwtException(JwtMessages.TOKEN_NO_FOUNDS);
                        }
                            return jwtProvider.getClaims(auth.getCredentials().toString());
                })
                .log()
                .onErrorResume(e-> Mono.error(new JwtException(JwtMessages.TOKEN_NO_FOUNDS)))
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
