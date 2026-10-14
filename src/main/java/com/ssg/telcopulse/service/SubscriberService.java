package com.ssg.telcopulse.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ssg.telcopulse.exception.SubscriberNotFoundException;
import com.ssg.telcopulse.model.Subscriber;
import com.ssg.telcopulse.repository.SubscriberRepository;
import com.ssg.telcopulse.model.SubscriberStatus;

@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;

    private static final Logger log = LoggerFactory.getLogger(SubscriberService.class);

    public SubscriberService(SubscriberRepository subscriberRepository) {
        // constructor injection
        this.subscriberRepository = subscriberRepository;

        // Pre-populate seed data if table is empty
        if (subscriberRepository.count() == 0) {
            log.info("Initializing seed data subscribers to H2 database...");
            subscriberRepository
                    .save(new Subscriber("John Doe", "+14045550199", "5G Unlimited", SubscriberStatus.ACTIVE));
            subscriberRepository
                    .save(new Subscriber("Jane Smith", "+14045550288", "Prepaid Lite", SubscriberStatus.PENDING));
        }
    }

    public List<Subscriber> getAllSubscribers() {
        log.info("fetching all subscribers. Total count {}", subscriberRepository.count());
        return subscriberRepository.findAll();
    }

    public Subscriber createSubscriber(Subscriber input) {
        log.info("Creating new subscribers ");
        return subscriberRepository.save(input);
    }

    public Subscriber getSubscriberById(Long id) {
        log.info("Searching for subscriber with id {} ", id);
        return subscriberRepository.findById(id).orElseThrow(() -> new SubscriberNotFoundException(id));
    }

    public void deleteSubscriberById(Long id) {
        Subscriber existingSubscriber = getSubscriberById(id);
        subscriberRepository.delete(existingSubscriber);
    }

    public Subscriber updateSubscriber(Subscriber givenSubscriber) {
        if (givenSubscriber == null || givenSubscriber.getId() == null) {
            log.warn("Update failed: invalid or null subscriber input provided");
            throw new IllegalArgumentException("invalid or null subscriber input provided");
        }
        Subscriber existing = getSubscriberById(givenSubscriber.getId());
        existing.setName(givenSubscriber.getName());
        existing.setMsisdn(givenSubscriber.getMsisdn());
        existing.setPlanName(givenSubscriber.getPlanName());
        existing.setStatus(givenSubscriber.getStatus());

        Subscriber updatedSubscriber = subscriberRepository.save(existing);
        log.info("Subscriber with id {} updated successfully", updatedSubscriber.getId());
        log.debug("Updated subscriber details: {}", updatedSubscriber); // Passes record toString()
        return updatedSubscriber;
    }
}
