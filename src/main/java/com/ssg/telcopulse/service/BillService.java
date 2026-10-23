package com.ssg.telcopulse.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.ssg.telcopulse.dto.BillMapper;
import com.ssg.telcopulse.dto.BillRequestDto;
import com.ssg.telcopulse.dto.BillResponseDto;
import com.ssg.telcopulse.exception.SubscriberNotFoundException;
import com.ssg.telcopulse.model.Bill;
import com.ssg.telcopulse.model.Subscriber;
import com.ssg.telcopulse.repository.BillRepository;
import com.ssg.telcopulse.repository.SubscriberRepository;

@Service 
public class BillService {

    private final BillRepository billRepository;
    private final SubscriberRepository subscriberRepository;
    private static final Logger log = LoggerFactory.getLogger(BillService.class);

    public BillService(BillRepository billRepository,SubscriberRepository subscriberRepository) {
        this.billRepository=billRepository;
        this.subscriberRepository=subscriberRepository;
    }

    public BillResponseDto createBill(Long subscriberId,BillRequestDto requestBill)
    {
        log.info("Generating new bill for subscriber ID: {}", subscriberId);
        Subscriber subscriber= subscriberRepository.findById(subscriberId).orElseThrow(()->new SubscriberNotFoundException(subscriberId));
        Bill bill = BillMapper.toEntity(requestBill);
        subscriber.addBill(bill);
        Bill createdBill = billRepository.save(bill);
        log.debug("New bill created with values {} " , createdBill);
        return BillMapper.toDto(createdBill);
    }

    public List<BillResponseDto> getBillsForASubscriber(Long subscriberId)
    {
        log.info("Fetching all bills for subscriber ID: {}", subscriberId);
        return billRepository.findBySubscriberId(subscriberId).stream().map(BillMapper::toDto).toList();
    }


}
