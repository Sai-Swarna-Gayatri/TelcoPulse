package com.ssg.telcopulse.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ssg.telcopulse.dto.SubscriberRequestDto;
import com.ssg.telcopulse.dto.SubscriberResponseDto;
import com.ssg.telcopulse.dto.SubscriberWithAddonPackageResponseDto;
import com.ssg.telcopulse.dto.SubscriberWithBillsResponseDto;
import com.ssg.telcopulse.dto.SubscriberMapper;
import com.ssg.telcopulse.exception.SubscriberNotFoundException;
import com.ssg.telcopulse.model.AddonPackage;
import com.ssg.telcopulse.model.Subscriber;
import com.ssg.telcopulse.repository.AddonPackageRepository;
import com.ssg.telcopulse.repository.SubscriberRepository;
import com.ssg.telcopulse.model.SubscriberStatus;
import com.ssg.telcopulse.repository.specification.SubscriberSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;

@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final AddonPackageRepository addonPackageRepository;

    private static final Logger log = LoggerFactory.getLogger(SubscriberService.class);

    public SubscriberService(SubscriberRepository subscriberRepository,AddonPackageRepository addonPackageRepository) {
        // constructor injection
        this.subscriberRepository = subscriberRepository;
        this.addonPackageRepository = addonPackageRepository;

        // Pre-populate seed data if table is empty
        if (subscriberRepository.count() == 0) {
            log.info("Initializing seed data subscribers to H2 database...");
            subscriberRepository
                    .save(new Subscriber("John Doe", "+14045550199", "5G Unlimited", SubscriberStatus.ACTIVE));
            subscriberRepository
                    .save(new Subscriber("Jane Smith", "+14045550288", "Prepaid Lite", SubscriberStatus.PENDING));
        }
    }

    // Paginated Data
    public Page<SubscriberResponseDto> getAllSubscribers(String name, SubscriberStatus status, String planName,
            String msisdn, Pageable pageable) {
        log.info("Fetching paginated subscribers - Page:{}, Size:{}, Sort:{}", pageable.getPageNumber(),
                pageable.getPageSize(), pageable.getSort());

        Specification<Subscriber> specification = Specification.where(SubscriberSpecification.hasName(name))
                .and(SubscriberSpecification.hasStatus(status)).and(SubscriberSpecification.hasPlanName(planName))
                .and(SubscriberSpecification.hasMsisdn(msisdn));
        return subscriberRepository.findAll(specification, pageable).map(SubscriberMapper::toDto);
    }

    /*
     * public Page<SubscriberResponseDto> getSubscribersByStatus(SubscriberStatus
     * status, Pageable pageable) {
     * log.info("fetching all subscribers with {} status", status); return
     * subscriberRepository.findByStatus(status,
     * pageable).map(SubscriberMapper::toDto); }
     */

    /*
     * public List<SubscriberResponseDto>
     * getSubscribersByStatusAndPlan(SubscriberStatus status, String plan) {
     * log.info("fetching all subscribers with {} status and {} plan", status,
     * plan); return subscriberRepository.findByStatusAndPlan(status,
     * plan).stream().map(SubscriberMapper::toDto).toList(); }
     */

    public SubscriberResponseDto createSubscriber(SubscriberRequestDto requestDto) {
        Subscriber input = SubscriberMapper.toEntity(requestDto);
        Subscriber created = subscriberRepository.save(input);
        log.info("Creating new subscriber with id {} ", created.getId());
        return SubscriberMapper.toDto(created);
    }

    public SubscriberResponseDto getSubscriberById(Long id) {
        log.info("Searching for subscriber with id {} ", id);
        Subscriber subscriber = subscriberRepository.findById(id)
                .orElseThrow(() -> new SubscriberNotFoundException(id));
        return SubscriberMapper.toDto(subscriber);
    }

    public void deleteSubscriberById(Long id) {
        Subscriber existingSubscriber = subscriberRepository.findById(id)
                .orElseThrow(() -> new SubscriberNotFoundException(id));
        subscriberRepository.delete(existingSubscriber);
        log.info("Deleted subscriber with id {}", id);
    }

    @Transactional 
    public SubscriberResponseDto updateSubscriber(Long id, SubscriberRequestDto givenSubscriber) {
        if (id == null || givenSubscriber == null) {
            log.warn("Update failed: invalid or null subscriber input provided");
            throw new IllegalArgumentException("invalid or null subscriber input provided");
        }
        Subscriber existing = subscriberRepository.findById(id).orElseThrow(() -> new SubscriberNotFoundException(id));
        existing.setName(givenSubscriber.name());
        existing.setMsisdn(givenSubscriber.msisdn());
        existing.setPlanName(givenSubscriber.planName());
        existing.setStatus(givenSubscriber.status());

        //Commenting out save due to Hibernate's dirty checking
       // Subscriber updatedSubscriber = subscriberRepository.save(existing);
        log.info("Subscriber with id {} updated successfully", existing.getId());
        log.debug("Updated subscriber details: {}", existing); // Passes record toString()
        return SubscriberMapper.toDto(existing);
    }

    public List<SubscriberWithBillsResponseDto> getSubscribersWithBills() {
        log.info("Fetching list of subscribers along with their bills to replicate N+1 problem");
       // return subscriberRepository.findAll().stream().map(SubscriberMapper::toBillsDto).toList();
        return subscriberRepository.findAllWithBills().stream().map(SubscriberMapper::toBillsDto).toList();
    }

    @Transactional 
    public SubscriberResponseDto assignAddonPackage(Long subscriberId,Long addonId)
    {
        log.info("Assigning addon package id {} to subscriber with id {} ", addonId,subscriberId);

        //fetching subscriber
        Subscriber subscriber = subscriberRepository.findById(subscriberId).orElseThrow(()-> new SubscriberNotFoundException(subscriberId));

        //fetching addon
        AddonPackage addon = addonPackageRepository.findById(addonId).orElseThrow(() -> new RuntimeException("Add-on package not found with ID: " + addonId));

        //check if subscriber already has this add on 
        if(subscriber.getAddon().contains(addon))
        {
            log.debug("Subscriber already opted for "+ addon.getPackageName() +" addon." );
            throw new IllegalArgumentException("Subscriber already opted for "+ addon.getPackageName() +" addon." );
        }

        //Calling helper method to syns both sides in memory
        subscriber.addAddOnPackage(addon);
        log.info("Addon "+ addon.getPackageName() +" saved for the Subscriber " );

        return SubscriberMapper.toDto(subscriber);

    }

    public List<SubscriberWithAddonPackageResponseDto> getSubscribersWithAddonPackages() {
              log.info("Fetching list of subscribers along with their Add-on packages....");
              List<SubscriberWithAddonPackageResponseDto>  subscriberWithAddon= subscriberRepository.findAllWithAddOn().stream().map(SubscriberMapper::toAddonDto).toList();
              return subscriberWithAddon;
    }
}
