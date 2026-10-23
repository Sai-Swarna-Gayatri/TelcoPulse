package com.ssg.telcopulse.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ssg.telcopulse.dto.SubscriberProfileMapper;
import com.ssg.telcopulse.dto.SubscriberProfileRequestDto;
import com.ssg.telcopulse.dto.SubscriberProfileResponseDto;
import com.ssg.telcopulse.exception.SubscriberNotFoundException;
import com.ssg.telcopulse.model.Subscriber;
import com.ssg.telcopulse.model.SubscriberProfile;
import com.ssg.telcopulse.repository.SubscriberProfileRepository;
import com.ssg.telcopulse.repository.SubscriberRepository;

import jakarta.transaction.Transactional;

@Service
public class SubscriberProfileService {

    private final SubscriberProfileRepository subscriberProfileRepository;
    private final SubscriberRepository subscriberRepository;
    private static final Logger log = LoggerFactory.getLogger(SubscriberProfileService.class);

    public SubscriberProfileService(SubscriberProfileRepository subscriberProfileRepository,
            SubscriberRepository subscriberRepository) {
        this.subscriberProfileRepository = subscriberProfileRepository;
        this.subscriberRepository = subscriberRepository;
    }

    public Page<SubscriberProfileResponseDto> getAllSubscriberProfiles(Pageable pageable) {
        log.info("Getting all Subscriber profile details ");
        return subscriberProfileRepository.findAll(pageable).map(SubscriberProfileMapper::toDto);
    }

    public SubscriberProfileResponseDto getSubscriberProfileById(Long id) {
        SubscriberProfile profile = subscriberProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscriber profile not found with id: " + id));
        log.info("Subscriber profile for {} is  {}", id, profile);
        return SubscriberProfileMapper.toDto(profile);
    }

    public SubscriberProfileResponseDto saveSubscriberProfile(SubscriberProfileRequestDto request, Long subscriberId) {

        Subscriber subscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new SubscriberNotFoundException(subscriberId));
        log.debug("Creating profile for subscriber id {} ", subscriber.getId());
        SubscriberProfile profile = SubscriberProfileMapper.toEntity(request);
        profile.setSubscriber(subscriber);
        return SubscriberProfileMapper.toDto(subscriberProfileRepository.save(profile));
    }

    @Transactional
    public void deleteSubscriberProfile(Long id) {
        log.info("Deleting profile with ID: {}", id);

        SubscriberProfile existingProfile = subscriberProfileRepository.findById(id)
                .orElseThrow(() -> new SubscriberNotFoundException(id));

        Subscriber subscriber = existingProfile.getSubscriber();
        if (subscriber != null) {
            subscriber.setProfile(null);
        }

        // Delete only the profile
        subscriberProfileRepository.delete(existingProfile);
        log.info("Profile deleted successfully.");
    }

}
