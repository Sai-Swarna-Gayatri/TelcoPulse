package com.ssg.telcopulse.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.ssg.telcopulse.model.BillStatus;

public record BillResponseDto(
        Long id,
        String billNumber,
        BigDecimal amount,
        LocalDate dueDate,
        BillStatus billStatus) {

}
