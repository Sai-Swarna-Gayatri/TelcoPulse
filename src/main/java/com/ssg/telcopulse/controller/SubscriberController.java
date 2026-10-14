package com.ssg.telcopulse.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssg.telcopulse.model.Subscriber;
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
    @GetMapping
    public ResponseEntity<List<Subscriber>> getAllSubscribers() {
        return ResponseEntity.ok(subscriberService.getAllSubscribers());
    }

    // GET /api/v1/subscribers?status=
    @GetMapping(params = "status")
    public ResponseEntity<List<Subscriber>> getSubscribersByStatus(@RequestParam SubscriberStatus status) {
        return ResponseEntity.ok(subscriberService.getSubscribersByStatus(status));
    }

    // GET /api/v1/subscribers?status=SubscriberStatus&plan=planName
    @GetMapping(params = {"status","plan"})
    public ResponseEntity<List<Subscriber>> getSubscribersByStatusAndPlan(@RequestParam SubscriberStatus status, @RequestParam String plan) {
        return ResponseEntity.ok(subscriberService.getSubscribersByStatusAndPlan(status,plan));
    }

    // GET /api/v1/subscribers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Subscriber> getSubscriberById(@PathVariable Long id) {   
            return ResponseEntity.ok(subscriberService.getSubscriberById(id));
    }

    // POST /api/v1/subscribers
    @PostMapping
    public ResponseEntity<Subscriber> createSubscriber(@RequestBody Subscriber subscriber) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriberService.createSubscriber(subscriber));
    }

    // Delete /api/v1/subscribers/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscriberById(@PathVariable Long id) {
            subscriberService.deleteSubscriberById(id);
            return ResponseEntity.noContent().build();
    }

    // Update /api/v1/subscribers
    @PutMapping
    public ResponseEntity<Subscriber> updateSubscriber(@RequestBody Subscriber subscriber) {
             return ResponseEntity.ok(subscriberService.updateSubscriber(subscriber));
    }

}
