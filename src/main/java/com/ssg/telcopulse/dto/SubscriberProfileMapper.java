package com.ssg.telcopulse.dto;

import com.ssg.telcopulse.model.SubscriberProfile;

public class SubscriberProfileMapper {

    public static SubscriberProfile toEntity(SubscriberProfileRequestDto request)
    {
        //Request DTO to Database Entity
        return new SubscriberProfile(request.email(),request.kycIdNumber(),request.billingAddress());
    }

    public static SubscriberProfileResponseDto toDto(SubscriberProfile profile)
    {
        //Database Entity to Response DTO
        if(profile==null)
        {
            return null;
        }
        return new SubscriberProfileResponseDto(profile.getId(),profile.getEmail(),profile.getKycIdNumber(),profile.getBillingAddress());
    }

}
