package com.ssg.telcopulse.model;

public record Subscriber(Long id,
    String name,
    String msisdn, // Mobile Phone Number
    String planName,
    SubscriberStatus status) {

}
