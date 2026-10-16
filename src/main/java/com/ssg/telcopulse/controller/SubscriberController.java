package com.ssg.telcopulse.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ssg.telcopulse.dto.SubscriberRequestDto;
import com.ssg.telcopulse.dto.SubscriberResponseDto;
import com.ssg.telcopulse.model.SubscriberStatus;
import com.ssg.telcopulse.service.SubscriberService;

@RestController
@RequestMapping("/api/v1/subscribers")
public class SubscriberController {
    private final SubscriberService subscriberService;

    // Constructor Injection
    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    // GET /api/v1/subscribers
    // GET /api/v1/subscribers?page=0&size=2&sort=name,asc
    //Adding Dynamic Filter
    @GetMapping
    public ResponseEntity<Page<SubscriberResponseDto>> getAllSubscribers(@RequestParam(required=false) String name,@RequestParam(required=false) SubscriberStatus status, @RequestParam(required=false) String planName,@RequestParam(required=false) String msisdn,Pageable pageable) {
        return ResponseEntity.ok(subscriberService.getAllSubscribers(name,status,planName,msisdn,pageable));
    }

    //Static Filter
    // GET /api/v1/subscribers?status=
  /*   @GetMapping(params = "status")
    public ResponseEntity<Page<SubscriberResponseDto>> getSubscribersByStatus(@RequestParam SubscriberStatus status,Pageable pageable) {
        return ResponseEntity.ok(subscriberService.getSubscribersByStatus(status,pageable));
    }

    // GET /api/v1/subscribers?status=SubscriberStatus&plan=planName
    @GetMapping(params = {"status","plan"})
    public ResponseEntity<List<SubscriberResponseDto>> getSubscribersByStatusAndPlan(@RequestParam SubscriberStatus status, @RequestParam String plan) {
        return ResponseEntity.ok(subscriberService.getSubscribersByStatusAndPlan(status,plan));
    } */

    // GET /api/v1/subscribers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<SubscriberResponseDto> getSubscriberById(@PathVariable Long id) {   
            return ResponseEntity.ok(subscriberService.getSubscriberById(id));
    }

    // POST /api/v1/subscribers
    @PostMapping
    public ResponseEntity<SubscriberResponseDto> createSubscriber(@Valid @RequestBody SubscriberRequestDto requestSubscriber) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriberService.createSubscriber(requestSubscriber));
    }

    // Delete /api/v1/subscribers/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscriberById(@PathVariable Long id) {
            subscriberService.deleteSubscriberById(id);
            return ResponseEntity.noContent().build();
    }

    // Update /api/v1/subscribers
    @PutMapping("/{id}")
    public ResponseEntity<SubscriberResponseDto> updateSubscriber(@PathVariable Long id,@Valid @RequestBody SubscriberRequestDto requestSubscriber) {
             return ResponseEntity.ok(subscriberService.updateSubscriber(id,requestSubscriber));
    }

}
