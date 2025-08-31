package co.com.bancolombia.jwtimplementation.exception;

public class JwtException extends RuntimeException {
    public JwtException(String message) {
        super(message);
    }
}
