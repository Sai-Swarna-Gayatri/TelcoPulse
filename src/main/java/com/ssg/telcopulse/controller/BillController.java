package com.ssg.telcopulse.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ssg.telcopulse.dto.BillRequestDto;
import com.ssg.telcopulse.dto.BillResponseDto;
import com.ssg.telcopulse.service.BillService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/v1/subscribers/{subscriberId}/bill")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }


    @GetMapping 
    public ResponseEntity<List<BillResponseDto>> getBillsForASubscriber(@PathVariable Long subscriberId)
    {
        return ResponseEntity.ok(billService.getBillsForASubscriber(subscriberId));
        
    }

    @PostMapping 
    public ResponseEntity<BillResponseDto> createBill(@PathVariable Long subscriberId,@Valid @RequestBody BillRequestDto requestBill)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(billService.createBill(subscriberId,requestBill));

    }

}
