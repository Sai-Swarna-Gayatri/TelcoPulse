package com.ssg.telcopulse.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public record SubscriberErrorResponse(String message,HttpStatus status,LocalDateTime timestamp) {

    public SubscriberErrorResponse(String message, HttpStatus status)
    {
            this(message,status,LocalDateTime.now());
    }

}
