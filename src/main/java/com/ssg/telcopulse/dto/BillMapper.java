package com.ssg.telcopulse.dto;

import com.ssg.telcopulse.model.Bill;

public class BillMapper {

    public static BillResponseDto toDto(Bill bill)
    {
        return new BillResponseDto(bill.getId(),bill.getBillNumber(),bill.getAmount(),bill.getDueDate(),bill.getBillStatus());
    }

    public static Bill toEntity(BillRequestDto request)
    {
        return new Bill(request.billNumber(),request.amount(),request.dueDate(),request.billStatus());
    }

}
