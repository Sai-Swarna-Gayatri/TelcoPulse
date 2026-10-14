package com.ssg.telcopulse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ssg.telcopulse.model.Subscriber;

@Repository
//JpaRepository<Entity,PrimaryKey Type>
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

}
