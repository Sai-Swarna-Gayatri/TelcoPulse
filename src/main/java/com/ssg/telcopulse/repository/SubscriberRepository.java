package com.ssg.telcopulse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ssg.telcopulse.model.Subscriber;
import com.ssg.telcopulse.model.SubscriberStatus;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
//JpaRepository<Entity,PrimaryKey Type>
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> ,JpaSpecificationExecutor<Subscriber> {

    //Derived Query Method
  //  public Page<Subscriber> findByStatus(SubscriberStatus status,Pageable pageable);

    //Custom JPQL
  //  @Query("Select s from Subscriber s where s.status = :status and s.planName LIKE %:plan%")
  //  List<Subscriber> findByStatusAndPlan(@Param("status") SubscriberStatus status, @Param("plan") String plan);

    //Native Query
   // @Query(value = "SELECT * FROM subscribers WHERE status = 'ACTIVE'", nativeQuery = true)
   // List<Subscriber> findAllActiveSubscribersNative();

   //Custom JPQL to resolve N+1 problem with JOIN FETCH
    @Query("select distinct sub from Subscriber sub LEFT JOIN FETCH sub.bills LEFT JOIN FETCH sub.profile")
    List<Subscriber> findAllWithBills();

    @Query("select distinct sub from Subscriber sub LEFT JOIN FETCH sub.addon")
    List<Subscriber> findAllWithAddOn();

}
