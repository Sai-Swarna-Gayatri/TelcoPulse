package com.ssg.telcopulse.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssg.telcopulse.model.Subscriber;
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

    // GET /api/v1/subscribers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Subscriber>> getSubscriberById(@PathVariable Long id) {
        Optional<Subscriber> subscriberbyId = subscriberService.getSubscriberById(id);      
        if(subscriberbyId.isPresent())
            return ResponseEntity.ok(subscriberService.getSubscriberById(id));
        else
            return ResponseEntity.notFound().build();
    }

    // POST /api/v1/subscribers
    @PostMapping
    public ResponseEntity<Subscriber> createSubscriber(@RequestBody Subscriber subscriber) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriberService.createSubscriber(subscriber));
    }

    // Delete /api/v1/subscribers/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscriberById(@PathVariable Long id) {
        if (subscriberService.deleteSubscriberById(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update /api/v1/subscribers
    @PutMapping
    public ResponseEntity<Subscriber> updateSubscriber(@RequestBody Subscriber subscriber) {
        if(subscriberService.updateSubscriber(subscriber)!=null)
        {
             return ResponseEntity.ok(subscriberService.updateSubscriber(subscriber));
        }
        else
        {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }    
    }

}
