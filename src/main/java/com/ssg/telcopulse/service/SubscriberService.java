package com.ssg.telcopulse.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ssg.telcopulse.dto.SubscriberRequestDto;
import com.ssg.telcopulse.dto.SubscriberResponseDto;
import com.ssg.telcopulse.dto.SubscriberMapper;
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

    //Paginated Data
    public Page<SubscriberResponseDto> getAllSubscribers(Pageable pageable) {
        log.info("Fetching paginated subscribers - Page:{}, Size:{}, Sort:{}",
                                                    pageable.getPageNumber(),pageable.getPageSize(),pageable.getSort());
        return subscriberRepository.findAll(pageable).map(SubscriberMapper::toDto);
    }

    public Page<SubscriberResponseDto> getSubscribersByStatus(SubscriberStatus status,Pageable pageable) {
        log.info("fetching all subscribers with {} status", status);
        return subscriberRepository.findByStatus(status,pageable).map(SubscriberMapper::toDto);
    }

     public List<SubscriberResponseDto> getSubscribersByStatusAndPlan(SubscriberStatus status,String plan) {
        log.info("fetching all subscribers with {} status and {} plan", status,plan);
        return subscriberRepository.findByStatusAndPlan(status,plan).stream().map(SubscriberMapper::toDto).toList();
    }

    public SubscriberResponseDto createSubscriber(SubscriberRequestDto requestDto) {
        Subscriber input = SubscriberMapper.toEntity(requestDto);
        Subscriber created = subscriberRepository.save(input);
        log.info("Creating new subscriber with id {} " ,created.getId());
        return SubscriberMapper.toDto(created);
    }

    public SubscriberResponseDto getSubscriberById(Long id) {
        log.info("Searching for subscriber with id {} ", id);
        Subscriber subscriber = subscriberRepository.findById(id).orElseThrow(() -> new SubscriberNotFoundException(id));
        return SubscriberMapper.toDto(subscriber);
    }

    public void deleteSubscriberById(Long id) {
        Subscriber existingSubscriber = subscriberRepository.findById(id)
                .orElseThrow(() -> new SubscriberNotFoundException(id));
        subscriberRepository.delete(existingSubscriber);
        log.info("Deleted subscriber with id {}", id);
    }

    public SubscriberResponseDto updateSubscriber(Long id,SubscriberRequestDto givenSubscriber) {
        if (id == null || givenSubscriber == null) {
            log.warn("Update failed: invalid or null subscriber input provided");
            throw new IllegalArgumentException("invalid or null subscriber input provided");
        }
        Subscriber existing = subscriberRepository.findById(id)
                .orElseThrow(() -> new SubscriberNotFoundException(id));
        existing.setName(givenSubscriber.name());
        existing.setMsisdn(givenSubscriber.msisdn());
        existing.setPlanName(givenSubscriber.planName());
        existing.setStatus(givenSubscriber.status());

        Subscriber updatedSubscriber = subscriberRepository.save(existing);
        log.info("Subscriber with id {} updated successfully", updatedSubscriber.getId());
        log.debug("Updated subscriber details: {}", updatedSubscriber); // Passes record toString()
        return SubscriberMapper.toDto(updatedSubscriber);
    }
}
