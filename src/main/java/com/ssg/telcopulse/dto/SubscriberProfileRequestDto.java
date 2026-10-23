package com.ssg.telcopulse.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SubscriberProfileRequestDto(
    @Email(message="Please provide a valid email address")
    String email, 
    @NotBlank(message="KYC ID number is mandatory.")
    @Pattern(
    regexp = "KYC\\d{4}",
    message = "KYC ID must start with KYC followed by exactly 4 digits.")
    String kycIdNumber,
    @NotBlank(message="Please provide billing Address")
    String billingAddress) {

}
