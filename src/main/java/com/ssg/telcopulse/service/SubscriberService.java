package com.ssg.telcopulse.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.ssg.telcopulse.model.Subscriber;
import com.ssg.telcopulse.model.SubscriberStatus;

@Service
public class SubscriberService {

    private final List<Subscriber> subscribers = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(100);

    public SubscriberService() {
        // Pre-populate seed data
        subscribers.add(new Subscriber(idCounter.incrementAndGet(), "John Doe", "+14045550199", "5G Unlimited",
                SubscriberStatus.ACTIVE));
        subscribers.add(new Subscriber(idCounter.incrementAndGet(), "Jane Smith", "+14045550288", "Prepaid Lite",
                SubscriberStatus.PENDING));
    }

    public List<Subscriber> getAllSubscribers() {
        return subscribers;
    }

    public Subscriber createSubscriber(Subscriber input) {
        Subscriber newSub = new Subscriber(idCounter.incrementAndGet(), input.name(), input.msisdn(), input.planName(),
                SubscriberStatus.PENDING);
        subscribers.add(newSub);
        return newSub;
    }

    public Optional<Subscriber> getSubscriberById(Long id) {
        return subscribers.stream().filter(subscriber -> subscriber.id().equals(id)).findFirst();
    }

}
