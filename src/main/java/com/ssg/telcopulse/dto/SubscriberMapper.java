package com.ssg.telcopulse.dto;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.ssg.telcopulse.model.Bill;
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
                    subscriber.getStatus(), 
                    SubscriberProfileMapper.toDto(subscriber.getProfile()));
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


      public static SubscriberWithBillsResponseDto toBillsDto(Subscriber subscriber) {
        List<Bill> bill = subscriber.getBills();
        return new SubscriberWithBillsResponseDto(
                    subscriber.getId(),
                    subscriber.getName(), 
                    subscriber.getMsisdn(),
                    subscriber.getPlanName(), 
                    subscriber.getStatus(), 
                    SubscriberProfileMapper.toDto(subscriber.getProfile()),
                    bill.stream().map(BillMapper::toDto).toList());
    }

     public static SubscriberWithAddonPackageResponseDto toAddonDto(Subscriber subscriber) {
        Set<AddonPackageResponseDto> addOn = subscriber.getAddon().stream().map(AddonMapper::toDto).collect(Collectors.toSet());
        return new SubscriberWithAddonPackageResponseDto(
                    subscriber.getId(),
                    subscriber.getName(), 
                    subscriber.getMsisdn(),
                    subscriber.getPlanName(), 
                    subscriber.getStatus(), 
                    addOn);
    }
    
}
