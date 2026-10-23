package com.ssg.telcopulse.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.ssg.telcopulse.model.BillStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BillRequestDto(
        @NotBlank(message = "Bill number can not be blank") 
        @Size(min = 5, max = 5, message = "Bill Number can only be  5 characters") 
        String billNumber,
        @NotNull 
        @DecimalMin("0.00") 
        @Digits(message = "Amount must be in x.xx format", fraction = 2, integer = 5) 
        BigDecimal amount,
        @NotNull(message = "Please provide a valid due date") 
        LocalDate dueDate,
        @NotNull(message = "Bill Status can not be null") 
        BillStatus billStatus) {

}
