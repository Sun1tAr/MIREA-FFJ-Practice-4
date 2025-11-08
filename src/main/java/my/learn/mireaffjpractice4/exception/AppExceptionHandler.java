package my.learn.mireaffjpractice4.exception;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    private Map<String, Object> getResponseBody(String message,
                                                HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", message);
        body.put("status", status.value());
        body.put("errorCode", status.getReasonPhrase());

        return body;
    }














}
