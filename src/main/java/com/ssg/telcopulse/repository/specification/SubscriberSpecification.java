package com.ssg.telcopulse.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import com.ssg.telcopulse.model.Subscriber;
import com.ssg.telcopulse.model.SubscriberStatus;

public class SubscriberSpecification {

    //filter by case insensitive name
    public static Specification<Subscriber> hasName(String name)
    {
        return (root,query,cb) ->
         (name == null || name.isBlank() ? null 
                : cb.like(cb.lower(root.get("name")),"%" + name.trim().toLowerCase()+"%"));
    }

    //Filter by exact status
    public static Specification<Subscriber> hasStatus(SubscriberStatus status)
    {
        return (root,query,cb) -> (status == null ? null : cb.equal(root.get("status"),status));
    }

    //Filter by case insenstive plan name
    public static Specification<Subscriber> hasPlanName(String planName)
    {
        return (root,query,cb) -> (planName == null || planName.isBlank() ? null 
        : cb.like(cb.lower(root.get("planName")),"%" + planName.trim().toLowerCase() +"%"));   
    }

    //Filter by msisdn number
    public static Specification<Subscriber> hasMsisdn(String msisdn)
    {
        return (root,query,cb) -> (msisdn==null || msisdn.isBlank() ? null : cb.equal(root.get("msisdn"),msisdn.trim()));
    }

}
