package com.ssg.telcopulse.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.mockito.ArgumentCaptor;
import com.ssg.telcopulse.dto.SubscriberWithAddonPackageResponseDto;
import com.ssg.telcopulse.dto.AddonPackageResponseDto;
import com.ssg.telcopulse.dto.SubscriberRequestDto;
import com.ssg.telcopulse.dto.SubscriberResponseDto;
import com.ssg.telcopulse.exception.AddonPackageNotFoundException;
import com.ssg.telcopulse.exception.DuplicateMsisdnException;
import com.ssg.telcopulse.exception.SubscriberNotFoundException;
import com.ssg.telcopulse.model.AddonPackage;
import com.ssg.telcopulse.model.Subscriber;
import com.ssg.telcopulse.model.SubscriberStatus;
import com.ssg.telcopulse.repository.AddonPackageRepository;
import com.ssg.telcopulse.repository.SubscriberRepository;

@ExtendWith(MockitoExtension.class)
public class SubscriberServiceTest {

      @Mock  
      private SubscriberRepository subscriberRepository;

      @Mock
      private AddonPackageRepository addonPackageRepository;

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

           // Subscriber subscriberMock = new Subscriber(requestDto.name(),requestDto.msisdn(),requestDto.planName(),requestDto.status());
            //subscriberMock.setId(100L);
            when(subscriberRepository.existsByMsisdn(requestDto.msisdn())).thenReturn(false);
  
            when(subscriberRepository.save(any(Subscriber.class))).thenAnswer(invocation-> 
            {Subscriber subscriberToSave = invocation.getArgument(0);
            subscriberToSave.setId(1L);
            return subscriberToSave;
            });

            SubscriberResponseDto response = subscriberService.createSubscriber(requestDto);

            //Assertions
            assertNotNull(response);
            assertEquals(1L,response.id());
            assertEquals("Rob James",response.name());
            assertEquals("+1122334455", response.msisdn());
            assertEquals(SubscriberStatus.PENDING, response.status());
            
            verify(subscriberRepository).existsByMsisdn(requestDto.msisdn());
            verify(subscriberRepository).save(any(Subscriber.class));
      }


      @Test 
      void shouldCreateSubscriber_mapDtoFieldsCorrectly()
      {
            //Stubbing
            SubscriberRequestDto requestDto = new SubscriberRequestDto("Rob James","+1122334455","Basic",SubscriberStatus.PENDING);

            ArgumentCaptor<Subscriber> subscriberCaptor = ArgumentCaptor.forClass(Subscriber.class) ;

            when(subscriberRepository.existsByMsisdn(requestDto.msisdn())).thenReturn(false);
  
            when(subscriberRepository.save(any(Subscriber.class))).thenAnswer(invocation-> 
            {Subscriber subscriberToSave = invocation.getArgument(0);
            subscriberToSave.setId(1L);
            return subscriberToSave;
            });

            subscriberService.createSubscriber(requestDto);

            verify(subscriberRepository).existsByMsisdn(requestDto.msisdn());
            verify(subscriberRepository).save(subscriberCaptor.capture());

            Subscriber captured = subscriberCaptor.getValue();

            //Assertions
            assertNotNull(captured);
            assertEquals(1L,captured.getId());
            assertEquals("Rob James",captured.getName());
            assertEquals("+1122334455", captured.getMsisdn());
            assertEquals(SubscriberStatus.PENDING, captured.getStatus());
                  
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


      @Test
      void shouldAssignAddonPackage_Success()
      {
            Long subscriberId= 1L;
            Long addonId = 101L;


            Subscriber subscriber = new Subscriber("Alice Bob", "+1234567890", "Premium", SubscriberStatus.ACTIVE);
            subscriber.setId(subscriberId);

            Set<Subscriber> subscriberSet = new HashSet<>();

            AddonPackage addOn = new AddonPackage(101L, "Unlimited SMS", new BigDecimal("120.00"), subscriberSet);

            when(subscriberRepository.findById(subscriberId)).thenReturn(Optional.of(subscriber));

            when(addonPackageRepository.findById(addonId)).thenReturn(Optional.of(addOn));
            
            
            SubscriberWithAddonPackageResponseDto response= subscriberService.assignAddonPackage(subscriberId,addonId);

            assertEquals("Alice Bob",response.name());

            assertEquals(1,response.addOnPackages().size());

            AddonPackageResponseDto addonDto =
            response.addOnPackages().iterator().next();

            assertEquals(101L, addonDto.id());
            assertEquals("Unlimited SMS", addonDto.packageName());
            assertEquals(0, new BigDecimal("120.00").compareTo(addonDto.price()));

            assertTrue(subscriber.getAddon().contains(addOn));
            assertTrue(addOn.getSubscribers().contains(subscriber));

            
            verify(subscriberRepository).findById(subscriberId);

            verify(addonPackageRepository).findById(addonId);

            verify(subscriberRepository,never()).save(any(Subscriber.class));

      }


      @Test
      void shouldAssignAddonPackage_subscriberNotFound()
      {
            Long subscriberId= 100L;
            Long addonId=101L;

            when(subscriberRepository.findById(subscriberId)).thenReturn(Optional.empty());

            SubscriberNotFoundException exception =assertThrows(SubscriberNotFoundException.class,()->subscriberService.assignAddonPackage(subscriberId,addonId));

            assertEquals("Subscriber with ID "+subscriberId+" not found",exception.getMessage());

            verify(subscriberRepository).findById(subscriberId);

            verify(addonPackageRepository,never()).findById(addonId);

            verify(subscriberRepository,never()).save(any(Subscriber.class));

      }



       @Test
      void shouldAssignAddonPackage_addonNotFound()
      {
            Long subscriberId= 100L;
            Long addonId=101L;

            Subscriber subscriber = new Subscriber("Alice Bob", "+1234567890", "Premium", SubscriberStatus.ACTIVE);
            subscriber.setId(subscriberId);

            when(subscriberRepository.findById(subscriberId)).thenReturn(Optional.of(subscriber));

            when(addonPackageRepository.findById(addonId)).thenReturn(Optional.empty());

            AddonPackageNotFoundException exception =assertThrows(AddonPackageNotFoundException.class,()->subscriberService.assignAddonPackage(subscriberId,addonId));

            assertEquals("Add-on package not found with ID: " + addonId,exception.getMessage());

            verify(subscriberRepository).findById(subscriberId);

            verify(addonPackageRepository).findById(addonId);

            verify(subscriberRepository,never()).save(any(Subscriber.class));

      }

      @Test
      void shouldAssignAddonPackage_subscriberAlreadyHasAddon()
      {
            Long subscriberId= 1L;
            Long addonId = 101L;

            Set<Subscriber> subscriberSet = new HashSet<>();
            AddonPackage addon = new AddonPackage(101L, "Unlimited SMS", new BigDecimal("120.00"), subscriberSet);

            Subscriber subscriber = new Subscriber("Alice Bob", "+1234567890", "Premium", SubscriberStatus.ACTIVE);
            subscriber.setId(subscriberId);
            subscriber.addAddOnPackage(addon);

            when(subscriberRepository.findById(subscriberId)).thenReturn(Optional.of(subscriber));

            when(addonPackageRepository.findById(addonId)).thenReturn(Optional.of(addon));
            
            IllegalArgumentException exception= assertThrows(IllegalArgumentException.class,()->subscriberService.assignAddonPackage(subscriberId,addonId));

            assertEquals("Subscriber already opted for " + addon.getPackageName() + " addon.",exception.getMessage());

            //checking existing relation is not broken
            assertEquals(1, subscriber.getAddon().size());
            assertTrue(subscriber.getAddon().contains(addon));

            verify(subscriberRepository).findById(subscriberId);

            verify(addonPackageRepository).findById(addonId);

            verify(subscriberRepository,never()).save(any(Subscriber.class));

      }










}
