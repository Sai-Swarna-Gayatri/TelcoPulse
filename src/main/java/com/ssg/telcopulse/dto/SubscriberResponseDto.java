package com.ssg.telcopulse.dto;

import com.ssg.telcopulse.model.SubscriberProfile;
import com.ssg.telcopulse.model.SubscriberStatus;

public record SubscriberResponseDto(Long id,
    String name,
    String msisdn,
    String planName,
    SubscriberStatus status,
    SubscriberProfile profile) {
    
}
