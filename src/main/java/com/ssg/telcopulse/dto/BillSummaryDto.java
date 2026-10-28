package com.ssg.telcopulse.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

//DTO Projections
public record BillSummaryDto(String msisdn,
        String name,
        String billNumber,
        BigDecimal amount,
        LocalDate dueDate
        ) {

}
