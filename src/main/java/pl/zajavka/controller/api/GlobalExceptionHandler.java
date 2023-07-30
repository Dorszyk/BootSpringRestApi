package pl.zajavka.controller.api;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.zajavka.controller.dto.ExceptionMessage;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Map<Class<?>, HttpStatus> EXCEPTION_STATUS = Map.of(
            ConstraintViolationException.class, HttpStatus.BAD_REQUEST,
            EntityNotFoundException.class, HttpStatus.NOT_FOUND
    );


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception ex,
            Object body,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode,
            @NonNull WebRequest request) {
        final String errorId = UUID.randomUUID().toString();
        log.error("Exception: ID ={}, HttpStatus={}", errorId, statusCode, ex);

        return super.handleExceptionInternal(
                ex,
                body,
                headers,
                statusCode,
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle(Exception exception) {
        return doHandle(exception, getHttpStatusFromException(exception.getClass()));
    }

    private ResponseEntity<?> doHandle(Exception exception, HttpStatus status) {
        final String errorId = UUID.randomUUID().toString();
        log.error("Handled exception: {}", exception.getMessage());
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ExceptionMessage.of(errorId));
    }

    private HttpStatus getHttpStatusFromException(final Class<?> exceptionClass) {
        return EXCEPTION_STATUS.getOrDefault(exceptionClass, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

