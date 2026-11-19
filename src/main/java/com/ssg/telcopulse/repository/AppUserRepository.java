package com.ssg.telcopulse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ssg.telcopulse.model.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser,Long>{

    public boolean existsByUserName(String userName);

}
