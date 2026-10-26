package com.ssg.telcopulse.dto;

import java.util.List;

import com.ssg.telcopulse.model.SubscriberStatus;

public record SubscriberWithBillsResponseDto(Long id,
    String name,
    String msisdn,
    String planName,
    SubscriberStatus status,
    SubscriberProfileResponseDto profile,
    List<BillResponseDto> bills
    )
{

}
