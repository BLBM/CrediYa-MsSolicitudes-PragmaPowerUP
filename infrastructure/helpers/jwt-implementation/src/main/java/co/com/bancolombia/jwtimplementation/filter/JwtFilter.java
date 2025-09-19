package co.com.bancolombia.jwtimplementation.filter;

import co.com.bancolombia.jwt_common.JwtMessages;
import co.com.bancolombia.logconstants.LogConstants;
import co.com.bancolombia.jwtimplementation.exception.JwtException;
import co.com.bancolombia.jwtimplementation.provider.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@Component
public class JwtFilter implements WebFilter {

    private final JwtProvider jwtProvider;

    public JwtFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info(LogConstants.START_JJWT_PROCESS);

        String path = exchange.getRequest().getPath().value();
        ServerHttpRequest request = exchange.getRequest();

        InetSocketAddress remoteAddress = request.getRemoteAddress();
        String ip = (remoteAddress != null) ? remoteAddress.getAddress().getHostAddress() : "unknown";
        String userAgent = request.getHeaders().getFirst(HttpHeaders.USER_AGENT);
        String forwardedFor = request.getHeaders().getFirst("X-Forwarded-For");


        if (PublicPaths.isPublic(path)) {
            log.info("Skipping JWT filter for public path: {}", path);
            return chain.filter(exchange);
        }

        log.info("Incoming request -> path: {}, method: {}, ip: {}, X-Forwarded-For: {}, User-Agent: {}",
                request.getPath().value(),
                request.getMethod(),
                ip,
                forwardedFor,
                userAgent
        );

        String auth = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (auth == null) {
            log.warn(JwtMessages.JJWT_ERROR_PROCESS, JwtMessages.TOKEN_NO_FOUNDS);
            return this.writeError(exchange, HttpStatus.UNAUTHORIZED, JwtMessages.TOKEN_NO_FOUNDS);
        }

        if (!auth.startsWith("Bearer ")) {
            log.warn(LogConstants.ERROR_PROCESS, JwtMessages.TOKEN_INVALID);
            return this.writeError(exchange, HttpStatus.BAD_REQUEST, JwtMessages.TOKEN_INVALID);
        }
        String token = auth.replace("Bearer ", "");

        if (!jwtProvider.validate(token)) {
            log.warn(LogConstants.ERROR_PROCESS, JwtMessages.TOKEN_INVALID);
            return Mono.error(new JwtException(JwtMessages.TOKEN_INVALID_IN_VALIDATE));
        }


        Authentication authentication = jwtProvider.getAuthentication(token);


        if (authentication.getCredentials() == null) {
            return Mono.error(new BadCredentialsException("Missing JWT token"));
        }

        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
    }


    private Mono<Void> writeError(ServerWebExchange exchange, HttpStatus status, String message) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = String.format(
                JwtMessages.COMPLETE_ERROR_MESSAGES,
                LocalDateTime.now(), status.value(), status.getReasonPhrase(), message
        );

        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}