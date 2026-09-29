package com.ssg.telcopulse.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssg.telcopulse.model.Subscriber;
import com.ssg.telcopulse.service.SubscriberService;


@RestController
@RequestMapping("/api/v1/subscribers")
public class SubscriberController {
    private final SubscriberService subscriberService;

    //Constructor Injection
    public SubscriberController(SubscriberService subscriberService)
    {
        this.subscriberService=subscriberService;
    }

    // GET /api/v1/subscribers
    @GetMapping
    public List<Subscriber> getAllSubscribers() {
        return subscriberService.getAllSubscribers();
    }
    
    // GET /api/v1/subscribers/{id}
    @GetMapping("/{id}")
    public Optional<Subscriber> getSubscriberById(@PathVariable Long id){
        return subscriberService.getSubscriberById(id);
    }

    // POST /api/v1/subscribers
    @PostMapping
    public Subscriber createSubscriber(@RequestBody Subscriber subscriber)
    {
        return subscriberService.createSubscriber(subscriber);
    }
    

}
