package com.ssg.telcopulse.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="User_Details")
public class AppUser extends Auditable {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    Long id;

    @Column(name="user_name", nullable=false,unique=true)
    String userName;

    @Column(nullable=false)
    String password;
     
    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    UserRole role;

    public AppUser(String userName, String password, UserRole role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    

}
