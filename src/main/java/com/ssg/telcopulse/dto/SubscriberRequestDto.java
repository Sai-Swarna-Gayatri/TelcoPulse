package com.ssg.telcopulse.dto;

import com.ssg.telcopulse.model.SubscriberStatus;
import jakarta.validation.constraints.*;

public record SubscriberRequestDto(
    @NotBlank(message="Subscriber name is mandatory.")
    @Size(max=25,message="Name can not exceed 25 characters.")
    @Pattern(
    regexp = "^[\\p{L}]+(?:\\s[\\p{L}]+)*$",
    message = "Name can contain letters, spaces, hyphens, and apostrophes only")
    String name,
    @NotBlank(message="Subscriber number is mandatory.")
    @Pattern(regexp="^\\+\\d{10,15}$", message="MSISDN must start with '+' and followed by country code and 10 digit number.")
    String msisdn,
    @NotBlank(message = "Plan name is mandatory.")
    String planName,
    @NotNull(message = "Subscriber status must be provided (e.g., ACTIVE or PENDING).")
    SubscriberStatus status) {}
