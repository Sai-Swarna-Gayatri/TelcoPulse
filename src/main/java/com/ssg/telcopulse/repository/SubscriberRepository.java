package com.ssg.telcopulse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ssg.telcopulse.model.Subscriber;
import com.ssg.telcopulse.model.SubscriberStatus;
import java.util.List;

@Repository
//JpaRepository<Entity,PrimaryKey Type>
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

    //Derived Query Method
    public List<Subscriber> findByStatus(SubscriberStatus status);

    //Custom JPQL
    @Query("Select s from Subscriber s where s.status = :status and s.planName LIKE %:plan%")
    List<Subscriber> findByStatusAndPlan(@Param("status") SubscriberStatus status, @Param("plan") String plan);

    //Native Query
    @Query(value = "SELECT * FROM subscribers WHERE status = 'ACTIVE'", nativeQuery = true)
    List<Subscriber> findAllActiveSubscribersNative();


}
