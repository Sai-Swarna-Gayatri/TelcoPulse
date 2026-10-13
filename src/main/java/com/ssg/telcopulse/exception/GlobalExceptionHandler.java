package com.ssg.telcopulse.exception;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(SubscriberNotFoundException.class)
    public ResponseEntity<SubscriberErrorResponse> handleSubscriberNotFoundException(SubscriberNotFoundException ex)
    {
        log.warn("Resource not found {} " ,ex.getMessage());
        SubscriberErrorResponse error = new SubscriberErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<SubscriberErrorResponse> handleIllegalArgument(IllegalArgumentException ex)
    {
        log.warn("Invalid Input provided {} " ,ex.getMessage());
        SubscriberErrorResponse error = new SubscriberErrorResponse(ex.getMessage(),HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<SubscriberErrorResponse> handleGeneralExceptions(Exception ex)
    {
        log.error("An unexpected internal error occurred", ex);
        SubscriberErrorResponse error = new SubscriberErrorResponse("An unexpected error occurred.",HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
