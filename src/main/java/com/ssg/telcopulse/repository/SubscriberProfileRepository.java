package com.ssg.telcopulse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ssg.telcopulse.model.SubscriberProfile;


@Repository 
public interface SubscriberProfileRepository extends JpaRepository<SubscriberProfile, Long> {


}
