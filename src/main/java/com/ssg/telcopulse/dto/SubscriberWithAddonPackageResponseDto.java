package com.ssg.telcopulse.dto;

import java.util.Set;
import com.ssg.telcopulse.model.SubscriberStatus;

public record SubscriberWithAddonPackageResponseDto(Long id,
    String name,
    String msisdn,
    String planName,
    SubscriberStatus status,
    Set <AddonPackageResponseDto> addOnPackages) {

}
