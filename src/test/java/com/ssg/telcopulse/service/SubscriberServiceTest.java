package com.ssg.telcopulse.service;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import com.ssg.telcopulse.dto.SubscriberRequestDto;
import com.ssg.telcopulse.dto.SubscriberResponseDto;
import com.ssg.telcopulse.exception.DuplicateMsisdnException;
import com.ssg.telcopulse.exception.SubscriberNotFoundException;
import com.ssg.telcopulse.model.Subscriber;
import com.ssg.telcopulse.model.SubscriberStatus;
import com.ssg.telcopulse.repository.SubscriberRepository;

@ExtendWith(MockitoExtension.class)
public class SubscriberServiceTest {

      @Mock  
      private SubscriberRepository subscriberRepository;

      @InjectMocks 
      private SubscriberService subscriberService;



    @Test
      void shouldGetSubscriberById_Exists()
      {
            //Stubbing
            Long subscriberId = 1L;
            Subscriber subscriberMock = new Subscriber("Alice Bob","+1234567890","Premium",SubscriberStatus.ACTIVE);

            when(subscriberRepository.findById(subscriberId)).thenReturn(Optional.of(subscriberMock));

            SubscriberResponseDto response = subscriberService.getSubscriberById(subscriberId);

            //Assertions
            assertNotNull(response);
            assertEquals("Alice Bob",response.name());
            assertEquals("+1234567890",response.msisdn());

            //verifying the repository is called once
            verify(subscriberRepository, times(1)).findById(subscriberId);
      
      }

      @Test
      void shouldGetSubscriberById_DoesNotExists()
      {
            //Stubbing
            Long subscriberId = 10L;

            when(subscriberRepository.findById(subscriberId)).thenReturn(Optional.empty());

            //Assertion for expected exception when no subscriber found
            SubscriberNotFoundException exception=assertThrows(SubscriberNotFoundException.class,()-> subscriberService.getSubscriberById(subscriberId) );

            //verifying the repository is called once
            assertEquals("Subscriber with ID "+subscriberId+" not found", exception.getMessage());
            verify(subscriberRepository, times(1)).findById(subscriberId);
      
      }



      @Test 
      void shouldCreateSubscriber_Success()
      {
            //Stubbing
            SubscriberRequestDto requestDto = new SubscriberRequestDto("Rob James","+1122334455","Basic",SubscriberStatus.PENDING);

            Subscriber subscriberMock = new Subscriber(requestDto.name(),requestDto.msisdn(),requestDto.planName(),requestDto.status());
            subscriberMock.setId(100L);
            when(subscriberRepository.existsByMsisdn(requestDto.msisdn())).thenReturn(false);
  
            when(subscriberRepository.save(any(Subscriber.class))).thenReturn(subscriberMock);

            SubscriberResponseDto response = subscriberService.createSubscriber(requestDto);

            //Assertions
            assertNotNull(response);
            assertEquals("Rob James",response.name());
            assertEquals("+1122334455", response.msisdn());
            assertEquals(SubscriberStatus.PENDING, response.status());
            
            verify(subscriberRepository).existsByMsisdn(requestDto.msisdn());
            verify(subscriberRepository).save(any(Subscriber.class));
      }



      @Test 
      void shouldCreateSubscriber_DuplicateMsisdn()
      {
            //Stubbing
            SubscriberRequestDto requestDto = new SubscriberRequestDto("Rob James","+1122334455","Basic",SubscriberStatus.PENDING);

            when(subscriberRepository.existsByMsisdn(requestDto.msisdn())).thenReturn(true);
  
            DuplicateMsisdnException exception= assertThrows(DuplicateMsisdnException.class,()->subscriberService.createSubscriber(requestDto));

            assertEquals("Msisdn " + requestDto.msisdn() +" already exists for another subscriber. plz provide another msisdn",exception.getMessage());
            verify(subscriberRepository).existsByMsisdn(requestDto.msisdn());
            verify(subscriberRepository,never()).save(any(Subscriber.class));
      }

      @Test
      void shouldUpdateSubscriber_Success()
      {
            Long id=123L;
            SubscriberRequestDto request = new SubscriberRequestDto("Rob James","+1122334444","Premium",SubscriberStatus.ACTIVE);

            Subscriber subscriber = new Subscriber("Rob James","+1122334455","Premium",SubscriberStatus.ACTIVE);
            subscriber.setId(123L);

            when(subscriberRepository.findById(id)).thenReturn(Optional.of(subscriber));

            when(subscriberRepository.existsByMsisdn(request.msisdn())).thenReturn(false);


            SubscriberResponseDto response = subscriberService.updateSubscriber(id,request);

            assertEquals("Rob James",response.name());
            assertEquals("+1122334444",response.msisdn());
            assertEquals("Premium",response.planName());

            verify(subscriberRepository).findById(id);

            verify(subscriberRepository).existsByMsisdn(request.msisdn());

            verify(subscriberRepository,never()).save(any(Subscriber.class));

      }


      @Test
      void shouldUpdateSubscriber_subscriberNotFound()
      {
            Long id=123L;
            SubscriberRequestDto request = new SubscriberRequestDto("Rob James","+1122334444","Premium",SubscriberStatus.ACTIVE);

            when(subscriberRepository.findById(id)).thenReturn(Optional.empty());

            SubscriberNotFoundException exception = assertThrows(SubscriberNotFoundException.class,()->subscriberService.updateSubscriber(id,request));

            assertEquals("Subscriber with ID "+id+" not found", exception.getMessage());
            verify(subscriberRepository, times(1)).findById(id);

            verify(subscriberRepository,never()).existsByMsisdn(request.msisdn());

            verify(subscriberRepository,never()).save(any(Subscriber.class));

      }


      @Test
      void shouldUpdateSubscriber_duplicateMsisdn()
      {
            Long id=123L;
            SubscriberRequestDto request = new SubscriberRequestDto("Rob James","+1122334444","Premium",SubscriberStatus.ACTIVE);

            Subscriber subscriber = new Subscriber("Rob James","+1122334455","Premium",SubscriberStatus.ACTIVE);
            subscriber.setId(123L);

            when(subscriberRepository.findById(id)).thenReturn(Optional.of(subscriber));

            when(subscriberRepository.existsByMsisdn(request.msisdn())).thenReturn(true);

            DuplicateMsisdnException exception = assertThrows(DuplicateMsisdnException.class,()->subscriberService.updateSubscriber(id,request));

            assertEquals("Msisdn " + request.msisdn() +" already exists for another subscriber. plz provide another msisdn",exception.getMessage());

            verify(subscriberRepository).findById(id);

            verify(subscriberRepository).existsByMsisdn(request.msisdn());

            verify(subscriberRepository,never()).save(any(Subscriber.class));

      }


      @Test
      void shouldUpdateSubscriberSuccess_msisdnUnchanged()
      {
            Long id=123L;
            SubscriberRequestDto request = new SubscriberRequestDto("Rob James","+1122334444","Premium",SubscriberStatus.ACTIVE);

            Subscriber subscriber = new Subscriber("Rob James","+1122334444","Premium",SubscriberStatus.ACTIVE);
            subscriber.setId(123L);

            when(subscriberRepository.findById(id)).thenReturn(Optional.of(subscriber));

            SubscriberResponseDto response = subscriberService.updateSubscriber(id,request);

            assertEquals("Rob James",response.name());
            assertEquals("+1122334444",response.msisdn());
            assertEquals("Premium",response.planName());

            verify(subscriberRepository).findById(id);

            verify(subscriberRepository,never()).existsByMsisdn(request.msisdn());

            verify(subscriberRepository,never()).save(any(Subscriber.class));

      }



      @Test
      void shouldDeleteSubscriber_success()
      {
            Long id=123L;
            Subscriber subscriber = new Subscriber("Rob James","+1122334455","Premium",SubscriberStatus.ACTIVE);

            when(subscriberRepository.findById(id)).thenReturn(Optional.of(subscriber));

            subscriberService.deleteSubscriberById(id);

            verify(subscriberRepository).findById(id);
            verify(subscriberRepository).delete(subscriber);
      }

      @Test
      void shouldDeleteSubscriber_subscriberNotFound()
      {
            Long id=123L;
            when(subscriberRepository.findById(id)).thenReturn(Optional.empty());

            SubscriberNotFoundException exception = assertThrows(SubscriberNotFoundException.class,()->subscriberService.deleteSubscriberById(id));

            assertEquals("Subscriber with ID "+id+" not found", exception.getMessage());
            verify(subscriberRepository, times(1)).findById(id);


            verify(subscriberRepository,never()).delete(any(Subscriber.class));
      }



}
