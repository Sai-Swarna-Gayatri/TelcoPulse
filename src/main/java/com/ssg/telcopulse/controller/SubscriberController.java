package com.ssg.telcopulse.controller;

import java.util.List;
import java.util.Optional;

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
    public List<Subscriber> getAllSubscribers() {
        return subscriberService.getAllSubscribers();
    }

    // GET /api/v1/subscribers/{id}
    @GetMapping("/{id}")
    public Optional<Subscriber> getSubscriberById(@PathVariable Long id) {
        return subscriberService.getSubscriberById(id);
    }

    // POST /api/v1/subscribers
    @PostMapping
    public Subscriber createSubscriber(@RequestBody Subscriber subscriber) {
        return subscriberService.createSubscriber(subscriber);
    }

    // Delete /api/v1/subscribers/{id}
    @DeleteMapping("/{id}")
    public String deleteSubscriberById(@PathVariable Long id) {
        if (subscriberService.deleteSubscriberById(id)) {
            return "Removed";
        } else {
            return "Subscriber not found";
        }
    }

    // Update /api/v1/subscribers
    @PutMapping
    public Subscriber updateSubscriber(@RequestBody Subscriber subscriber) {
        return subscriberService.updateSubscriber(subscriber);
    }

}
