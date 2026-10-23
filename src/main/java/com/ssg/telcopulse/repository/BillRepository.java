package com.ssg.telcopulse.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.ssg.telcopulse.model.Bill;

@Repository 
public interface BillRepository extends JpaRepository<Bill,Long>{

    @Query 
    public List<Bill> findBySubscriberId(Long subscriberId);

}
