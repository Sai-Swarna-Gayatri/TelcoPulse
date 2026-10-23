package com.ssg.telcopulse.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ssg.telcopulse.dto.SubscriberProfileRequestDto;
import com.ssg.telcopulse.dto.SubscriberProfileResponseDto;
import com.ssg.telcopulse.service.SubscriberProfileService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping ("/api/v1/subscribers")
public class SubscriberProfileController {

    private final SubscriberProfileService subscriberProfileService;

    public SubscriberProfileController(SubscriberProfileService subscriberProfileService) {
        this.subscriberProfileService = subscriberProfileService;
    }

    @GetMapping("/profile") 
    public ResponseEntity<Page<SubscriberProfileResponseDto>> getSubscriberProfiles(Pageable pageable) {
        // Implement logic to fetch subscriber profiles with pagination
        return ResponseEntity.ok(subscriberProfileService.getAllSubscriberProfiles(pageable));     
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<SubscriberProfileResponseDto> getSubscriberProfileById(@PathVariable Long id)
    {
            return ResponseEntity.ok(subscriberProfileService.getSubscriberProfileById(id));
    }

    @PostMapping("/{subscriberId}/profile")
    public ResponseEntity<SubscriberProfileResponseDto> saveSusbcriberProfile(@Valid @RequestBody SubscriberProfileRequestDto profile, @PathVariable Long subscriberId)
    {
            return ResponseEntity.ok(subscriberProfileService.saveSubscriberProfile(profile,subscriberId));
    }

    @DeleteMapping("/{id}/profile")
    public  ResponseEntity<Void> deleteSubscriberProfile(@PathVariable Long id)
    {
            subscriberProfileService.deleteSubscriberProfile(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
   

    }
