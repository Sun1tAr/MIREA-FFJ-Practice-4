package my.learn.mireaffjpractice4.exception;

import org.springframework.http.HttpStatus;

public class InvalidArgumentException extends AppException{
    public InvalidArgumentException(String message, HttpStatus status, String errorCode) {
        super(message, status, errorCode);
    }
}
