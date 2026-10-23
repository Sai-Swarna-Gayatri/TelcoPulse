package com.ssg.telcopulse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter 
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@Entity
@Table(name=  "subscriber_profile")
public class SubscriberProfile {

    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(length=50 ,nullable=false)
    private String email;

    @Column(name="kyc_id",length=20 ,nullable=false,unique=true)
    private String kycIdNumber;

    @Column(name="billing_address",length=100,nullable=false)
    private String billingAddress;


    //Subscriber profile existis only if there is a subscriber and one profile for one subscriber.foreign key is id from Subscriber table.
    @OneToOne 
    @JoinColumn(name="subscriber_id", referencedColumnName="id", nullable=false, unique=true)
    private Subscriber subscriber;


    public SubscriberProfile(String email, String kycIdNumber, String billingAddress) {
        System.out.println("In cinst of profile...."+ kycIdNumber);
        this.email = email;
        this.kycIdNumber = kycIdNumber;
        this.billingAddress = billingAddress;
        System.out.println("Const kyc  " + this.kycIdNumber);
    }


    @Override
    public String toString() {
        return "SubscriberProfile [id=" + id + ", email=" + email + ", KycIdNumber=" + kycIdNumber + ", billingAddress="
                + billingAddress + ", subscriber=" + subscriber + "]";
    }

    



}
