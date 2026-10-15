package com.ssg.telcopulse.dto;
import com.ssg.telcopulse.model.Subscriber;
import com.ssg.telcopulse.model.SubscriberStatus;

public class SubscriberMapper {

    //Database Entity to Response DTO
    public static SubscriberResponseDto toDto(Subscriber subscriber) {
        return new SubscriberResponseDto(
                    subscriber.getId(),
                    subscriber.getName(), 
                    subscriber.getMsisdn(),
                    subscriber.getPlanName(), 
                    subscriber.getStatus());
    }

    //Request DTO to Database Entity
    public static Subscriber toEntity(SubscriberRequestDto requestDto)
    {
        return new Subscriber(
            requestDto.name(),
            requestDto.msisdn(),
            requestDto.planName(),
            requestDto.status() != null ? requestDto.status() : SubscriberStatus.PENDING
        );
    }
}
