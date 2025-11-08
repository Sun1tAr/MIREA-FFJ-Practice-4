package my.learn.mireaffjpractice4.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends AppException{

    public NotFoundException() {
        super("Entity was not found", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase());
    }
}
