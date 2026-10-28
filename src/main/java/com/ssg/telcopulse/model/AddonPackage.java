package com.ssg.telcopulse.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter 
@Setter 
@NoArgsConstructor 
@Entity 
@Table(name="Addon_Package")
public class AddonPackage extends Auditable{

    @Id 
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Long id;

    @Column(name="package_name", nullable=false)
    private String packageName;

    @Column(nullable=false)
    private BigDecimal price;

    @ManyToMany(mappedBy = "addon" , fetch = FetchType.LAZY) 
    private Set<Subscriber> subscribers = new HashSet<>();

    public AddonPackage(Long id, String packageName, BigDecimal price, Set<Subscriber> subscribers) {
        this.id = id;
        this.packageName = packageName;
        this.price = price;
        this.subscribers = subscribers;
    }

    @Override
    public String toString() {
        return "AddonPackage [id=" + id + ", packageName=" + packageName + ", price=" + price + ", subscribers="
                + subscribers + "]";
    }

    

}
