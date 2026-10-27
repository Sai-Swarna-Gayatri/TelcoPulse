package com.ssg.telcopulse.dto;

import com.ssg.telcopulse.model.AddonPackage;

public class AddonMapper {

     public static AddonPackageResponseDto toDto(AddonPackage addonPackage)
    {
       // Set<SubscriberResponseDto> subscriberDto = addonPackage.getSubscribers().stream().map(SubscriberMapper::toDto).collect(Collectors.toSet());
        
        return new AddonPackageResponseDto(addonPackage.getId(),addonPackage.getPackageName(),addonPackage.getPrice());
    }

}
