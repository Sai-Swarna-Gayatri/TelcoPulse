package com.ssg.telcopulse.exception;

public class SubscriberNotFoundException extends RuntimeException{

    public SubscriberNotFoundException(Long id)
    {
        super("Subscriber with ID " + id +" not found");
    }

}
