package my.learn.mireaffjpractice4.exception.handler;


import lombok.extern.slf4j.Slf4j;
import my.learn.mireaffjpractice4.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleException(Exception e) {
        log.error("Unexpected error occurred: ", e);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(getResponseBody(e.getMessage(), status), status);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleAppException(AppException e) {
        log.warn("AppException error occurred: ", e);

        HttpStatus status = e.getStatus();
        String message = e.getMessage();
        return new ResponseEntity<>(getResponseBody(message, status), status);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Validation failed");
        body.put("errors", errors);
        body.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> getResponseBody(String message,
                                                HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", message);
        body.put("status", status.value());
        body.put("errorCode", status.getReasonPhrase());

        return body;
    }














}
