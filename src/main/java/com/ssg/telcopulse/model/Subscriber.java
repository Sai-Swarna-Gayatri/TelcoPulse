package com.ssg.telcopulse.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;



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

    @JsonManagedReference 
    @OneToOne(mappedBy="subscriber", optional=true, cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    private SubscriberProfile profile;

    public Subscriber(String name, String msisdn, String planName, SubscriberStatus status) {
        this.name = name;
        this.msisdn = msisdn;
        this.planName = planName;
        this.status = status;
    }

    //Subscriber can have 0 or more bills
    @OneToMany(mappedBy="subscriber",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Bill> bills = new ArrayList<>();

    //Helper method to add bills
    public void addBill(Bill bill)
    {
        bills.add(bill);
        bill.setSubscriber(this);
    }

    //Helper method to remove bills
    public void removeBill(Bill bill)
    {
        bills.remove(bill);
        bill.setSubscriber(null);
    }


    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE} , fetch=FetchType.LAZY)
    @JoinTable(name="subscriber_addons",joinColumns =@JoinColumn(name="subscriber_id") ,inverseJoinColumns = @JoinColumn(name="addon_id")) 
    private Set<AddonPackage> addon = new HashSet<>();

    //Helper method to add addon packages
    public void addAddOnPackage(AddonPackage addonPackage)
    {
        this.addon.add(addonPackage);
        addonPackage.getSubscribers().add(this);
    }

    //Helper method to remove addOn packages
    public void removeAddOnPackage(AddonPackage addonPackage)
    {
        this.addon.remove(addonPackage);
        addonPackage.getSubscribers().remove(this);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Subscriber{");
        sb.append("id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", msisdn=").append(msisdn);
        sb.append(", planName=").append(planName);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}

