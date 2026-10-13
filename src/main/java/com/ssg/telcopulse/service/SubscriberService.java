package com.ssg.telcopulse.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ssg.telcopulse.exception.SubscriberNotFoundException;
import com.ssg.telcopulse.model.Subscriber;
import com.ssg.telcopulse.model.SubscriberStatus;

@Service
public class SubscriberService {

    private final List<Subscriber> subscribers = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(100);

    private static final Logger log = LoggerFactory.getLogger(SubscriberService.class);

    public SubscriberService() {
        // Pre-populate seed data
        log.info("Initializing seed data subscribers.....");
        subscribers.add(new Subscriber(idCounter.incrementAndGet(), "John Doe", "+14045550199", "5G Unlimited",
                SubscriberStatus.ACTIVE));
        subscribers.add(new Subscriber(idCounter.incrementAndGet(), "Jane Smith", "+14045550288", "Prepaid Lite",
                SubscriberStatus.PENDING));
    }

    public List<Subscriber> getAllSubscribers() {
        log.info("fetching all subscribers. Total count {}", subscribers.size());
        return subscribers;
    }

    public Subscriber createSubscriber(Subscriber input) {
        Subscriber newSub = new Subscriber(idCounter.incrementAndGet(), input.name(), input.msisdn(), input.planName(),
                SubscriberStatus.PENDING);
        subscribers.add(newSub);
        log.info("New subscriber created successfully with id {} ", newSub.id());
        return newSub;
    }

    public Subscriber getSubscriberById(Long id) {
        log.info("Searching for subscriber with id {} ", id);
        return subscribers.stream().filter(subscriber -> subscriber.id().equals(id)).findFirst()
                .orElseThrow(() -> new SubscriberNotFoundException(id));
    }

    public void deleteSubscriberById(Long id) {
        boolean removed = subscribers.removeIf(subscriber -> subscriber.id().equals(id));
        if (removed) {
            log.info("Subscriber with id {} deleted successfully", id);
        } else {
            log.warn("Attempted to delete subscriber with id {}, but record was not found", id);
            throw new SubscriberNotFoundException(id);
        }
    }

    public Subscriber updateSubscriber(Subscriber givenSubscriber) {
        if (givenSubscriber == null || givenSubscriber.id() == null) {
            log.warn("Update failed: invalid or null subscriber input provided");
            throw new IllegalArgumentException("invalid or null subscriber input provided");
        }
        deleteSubscriberById(givenSubscriber.id());
        Subscriber updatedSubscriber = new Subscriber(givenSubscriber.id(), givenSubscriber.name(),
                givenSubscriber.msisdn(), givenSubscriber.planName(), givenSubscriber.status());
        subscribers.add(updatedSubscriber);
        log.info("Subscriber with id {} updated successfully", updatedSubscriber.id());
        log.debug("Updated subscriber details: {}", updatedSubscriber); // Passes record toString()
        return updatedSubscriber;
    }
}
