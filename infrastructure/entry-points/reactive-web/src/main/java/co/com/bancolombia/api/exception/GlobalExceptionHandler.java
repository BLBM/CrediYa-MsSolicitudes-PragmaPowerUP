package co.com.bancolombia.api.exception;

import co.com.bancolombia.logconstants.LogConstants;
import co.com.bancolombia.model.exception.DomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Map<String, Object>> handleDomainException(DomainException ex) {

        log.error(LogConstants.DOMAIN_ERROR,ex.getMessage());

        Map<String, Object> body = new HashMap<>();
        body.put(LogConstants.TIMESTAMP_ERROR,LocalDateTime.now());
        body.put(LogConstants.DOMAIN_ERROR, LogConstants.DOMAIN_ERROR_MESSAGE);
        body.put(LogConstants.MESSAGE_ERROR, ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
        log.error(LogConstants.SERVER_ERROR,ex.getMessage());

        Map<String, Object> body = new HashMap<>();
        body.put(LogConstants.TIMESTAMP_ERROR,LocalDateTime.now());
        body.put(LogConstants.APPLICATION_ERROR, LogConstants.SERVER_ERROR_MESSAGE);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }



}
