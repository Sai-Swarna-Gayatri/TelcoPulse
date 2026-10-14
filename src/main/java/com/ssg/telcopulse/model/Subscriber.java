package com.ssg.telcopulse.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;



@Getter
@Setter
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@Entity
@Table(name="subscribers")
public class Subscriber {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="subscriber_name", nullable=false, length=25)
    private String name;

    @Column(nullable=false, unique=true, length=12)
    private String msisdn;// Mobile Phone Number

    @Column(name="plan_type", nullable=false)
    private String planName;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private SubscriberStatus status;

    public Subscriber(String name, String msisdn, String planName, SubscriberStatus status) {
        this.name = name;
        this.msisdn = msisdn;
        this.planName = planName;
        this.status = status;
    }
}

