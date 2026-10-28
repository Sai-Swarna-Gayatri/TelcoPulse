package com.ssg.telcopulse.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ssg.telcopulse.dto.BillSummaryDto;
import com.ssg.telcopulse.model.Bill;

@Repository 
public interface BillRepository extends JpaRepository<Bill,Long>{

    @Query("SELECT new com.ssg.telcopulse.dto.BillSummaryDto(b.subscriber.msisdn,b.subscriber.name,b.billNumber,b.amount,b.dueDate) FROM Bill b where b.subscriber.id=:subscriberId ")
    public List<BillSummaryDto> findBySubscriberId(@Param("subscriberId") Long subscriberId);

}
