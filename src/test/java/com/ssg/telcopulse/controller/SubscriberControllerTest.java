package com.ssg.telcopulse.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.ssg.telcopulse.dto.AddonPackageResponseDto;
import com.ssg.telcopulse.dto.AssignAddonsRequestDto;
import com.ssg.telcopulse.dto.SubscriberProfileResponseDto;
import com.ssg.telcopulse.dto.SubscriberRequestDto;
import com.ssg.telcopulse.dto.SubscriberResponseDto;
import com.ssg.telcopulse.dto.SubscriberWithAddonPackageResponseDto;
import com.ssg.telcopulse.exception.AddonPackageNotFoundException;
import com.ssg.telcopulse.exception.DuplicateMsisdnException;
import com.ssg.telcopulse.exception.SubscriberNotFoundException;
import com.ssg.telcopulse.model.SubscriberStatus;
import com.ssg.telcopulse.service.SubscriberService;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@WebMvcTest(SubscriberController.class)
public class SubscriberControllerTest {

    @MockitoBean 
    private SubscriberService subscriberService;

    @Autowired 
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;


    @Test 
    void shouldGetSubscriberById_success() throws Exception
    {
        Long id=100L;
        SubscriberProfileResponseDto profile=null;

        SubscriberResponseDto responseSubscriber = new SubscriberResponseDto(id,"Test Name","+1122334455","Basic Plan",SubscriberStatus.ACTIVE,profile);

        when(subscriberService.getSubscriberById(id)).thenReturn(responseSubscriber);

        mockMvc.perform(get("/api/v1/subscribers/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Test Name"))
                    .andExpect(jsonPath("$.msisdn").value("+1122334455"))
                    .andExpect(jsonPath("$.planName").value("Basic Plan"));

        verify(subscriberService).getSubscriberById(id);
    }


    @Test 
    void shouldGetSubscriberById_notFound() throws Exception
    {
        Long id=100L;

        when(subscriberService.getSubscriberById(id)).thenThrow(new SubscriberNotFoundException(id));

        mockMvc.perform(get("/api/v1/subscribers/{id}",id)).andExpect(status().isNotFound());

        verify(subscriberService).getSubscriberById(id);

    }

    @Test 
    void shouldDeleteSubscriberById_success() throws Exception
    {
        Long id=100L;

        doNothing().when(subscriberService).deleteSubscriberById(id);

        mockMvc.perform(delete("/api/v1/subscribers/{id}",id)).andExpect(status().isNoContent());

        verify(subscriberService).deleteSubscriberById(id);

    }

    @Test 
    void shouldDeleteSubscriberById_notFound() throws Exception
    {
        Long id=100L;

        doThrow(new SubscriberNotFoundException(id)).when(subscriberService).deleteSubscriberById(id);

        mockMvc.perform(delete("/api/v1/subscribers/{id}",id)).andExpect(status().isNotFound());

        verify(subscriberService).deleteSubscriberById(id);
    }


    @Test 
    void shouldAssignAddon_success() throws Exception
    {
        Long subscriberId=100L;
        Long addonId = 123L;

        AddonPackageResponseDto addOn = new AddonPackageResponseDto(addonId,"Extra Data",new BigDecimal("50.00"));

        Set<AddonPackageResponseDto> addonSet = Set.of(addOn);

        SubscriberWithAddonPackageResponseDto response = new SubscriberWithAddonPackageResponseDto(subscriberId,"Test Name","+1122334455","Basic Plan",SubscriberStatus.ACTIVE,addonSet);

        when(subscriberService.assignAddonPackage(subscriberId,addonId)).thenReturn(response);

        mockMvc.perform(post("/api/v1/subscribers/{subscriberId}/addons/{addonId}",subscriberId,addonId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Test Name"))
        .andExpect(jsonPath("$.msisdn").value("+1122334455"))
        .andExpect(jsonPath("$.addOnPackages[0].packageName").value("Extra Data"));

        verify(subscriberService).assignAddonPackage(subscriberId, addonId);
    }

    @Test 
    void shouldAssignAddon_subscriberAlreadyOptedForAddon() throws Exception
    {
        Long subscriberId=100L;
        Long addonId = 123L;

        when(subscriberService.assignAddonPackage(subscriberId, addonId)).thenThrow(new IllegalArgumentException("Subscriber already opted for Extra Data addon."));

        mockMvc.perform(post("/api/v1/subscribers/{subscriberId}/addons/{addonId}",subscriberId,addonId)).andExpect(status().isBadRequest());

        verify(subscriberService).assignAddonPackage(subscriberId, addonId);

    }

    @Test 
    void shouldAssignAddon_addonPackageNotFound() throws Exception
    {
        Long subscriberId=100L;
        Long addonId = 123L;

        when(subscriberService.assignAddonPackage(subscriberId, addonId)).thenThrow(new AddonPackageNotFoundException(addonId));

        mockMvc.perform(post("/api/v1/subscribers/{subscriberId}/addons/{addonId}",subscriberId,addonId)).andExpect(status().isNotFound());

        verify(subscriberService).assignAddonPackage(subscriberId, addonId);

    }

    @Test 
    void shouldAssignAddon_subscriberNotFound() throws Exception
    {
        Long subscriberId=100L;
        Long addonId = 123L;

        when(subscriberService.assignAddonPackage(subscriberId, addonId)).thenThrow(new SubscriberNotFoundException(subscriberId));

        mockMvc.perform(post("/api/v1/subscribers/{subscriberId}/addons/{addonId}",subscriberId,addonId)).andExpect(status().isNotFound());

        verify(subscriberService).assignAddonPackage(subscriberId, addonId);

    }


    @Test 
    void shouldAssignMultipleAddon_success() throws Exception
    {
        Long subscriberId=100L;

        List<Long> addonList = List.of(110L,111L,112L);
        AssignAddonsRequestDto addonIds=new AssignAddonsRequestDto(addonList);

        AddonPackageResponseDto addon1 = new AddonPackageResponseDto(addonList.get(0),"Extra Data",new BigDecimal("50.00"));
        AddonPackageResponseDto addon2 = new AddonPackageResponseDto(addonList.get(1),"Roaming",new BigDecimal("75.00"));
        AddonPackageResponseDto addon3 = new AddonPackageResponseDto(addonList.get(2),"5G Plus",new BigDecimal("69.00"));


        Set<AddonPackageResponseDto> addonSet = Set.of(addon1,addon2,addon3);

        SubscriberWithAddonPackageResponseDto response = new SubscriberWithAddonPackageResponseDto(subscriberId,"Test Name","+1122334455","Basic Plan",SubscriberStatus.ACTIVE,addonSet);

        when(subscriberService.assignMultipleAddons(subscriberId,addonIds)).thenReturn(response);

        mockMvc.perform(post("/api/v1/subscribers/{subscriberId}/addons",subscriberId).contentType(MediaType.APPLICATION_JSON).content(jsonMapper.writeValueAsString(addonIds)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Test Name"))
        .andExpect(jsonPath("$.msisdn").value("+1122334455"))
        .andExpect(jsonPath("$.addOnPackages[*].packageName",org.hamcrest.Matchers.containsInAnyOrder("Extra Data","Roaming","5G Plus")));

        verify(subscriberService).assignMultipleAddons(subscriberId, addonIds);
    }

    @Test 
    void shouldAssignMultipleAddon_subscriberNotFound() throws Exception
    {
        Long subscriberId=100L;
         List<Long> addonList = List.of(110L,111L,112L);
        AssignAddonsRequestDto addonIds=new AssignAddonsRequestDto(addonList);

        when(subscriberService.assignMultipleAddons(subscriberId, addonIds)).thenThrow(new SubscriberNotFoundException(subscriberId));

        mockMvc.perform(post("/api/v1/subscribers/{subscriberId}/addons",subscriberId).contentType(MediaType.APPLICATION_JSON).content(jsonMapper.writeValueAsString(addonIds)))
        .andExpect(status().isNotFound());

        verify(subscriberService).assignMultipleAddons(subscriberId, addonIds);

    }

    @Test 
    void shouldAssignMultipleAddon_oneOrMoreAddonNotFound() throws Exception
    {
        Long subscriberId=100L;
         List<Long> addonList = List.of(110L,111L,112L);
        AssignAddonsRequestDto addonIds=new AssignAddonsRequestDto(addonList);

        when(subscriberService.assignMultipleAddons(subscriberId, addonIds)).thenThrow( new AddonPackageNotFoundException("One or more invalid Add-on IDs were provided."));

        mockMvc.perform(post("/api/v1/subscribers/{subscriberId}/addons",subscriberId).contentType(MediaType.APPLICATION_JSON).content(jsonMapper.writeValueAsString(addonIds)))
        .andExpect(status().isNotFound());

        verify(subscriberService).assignMultipleAddons(subscriberId, addonIds);

    }

    @Test
    void shouldAssignMultipleAddon_addonListIsBlankBadRequest() throws Exception
    {
        Long subscriberId=100L;
         List<Long> addonList = new ArrayList<>();
        AssignAddonsRequestDto addonIds=new AssignAddonsRequestDto(addonList);

        mockMvc.perform(post("/api/v1/subscribers/{subscriberId}/addons",subscriberId).contentType(MediaType.APPLICATION_JSON).content(jsonMapper.writeValueAsString(addonIds)))
        .andExpect(status().isBadRequest());

        verify(subscriberService,never()).assignMultipleAddons(subscriberId, addonIds);

    }

    
    @Test 
    void shouldCreateSubscriber_success() throws Exception
    {
        Long subscriberId=100L;

        SubscriberResponseDto response = new SubscriberResponseDto(subscriberId,"Test Name","+1122334455","Test Plan",SubscriberStatus.PENDING,null);

        SubscriberRequestDto request = new SubscriberRequestDto("Test Name","+1122334455","Test Plan",SubscriberStatus.PENDING);
      
        when(subscriberService.createSubscriber(request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/subscribers").contentType(MediaType.APPLICATION_JSON).content(jsonMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(100))
        .andExpect(jsonPath("$.name").value("Test Name"))
        .andExpect(jsonPath("$.msisdn").value("+1122334455"))
        .andExpect(jsonPath("$.status").value("PENDING"));

        verify(subscriberService).createSubscriber(request);

    }


    @Test 
    void shouldCreateSubscriber_duplicateMsisdn() throws Exception
    {

        SubscriberRequestDto request = new SubscriberRequestDto("Test Name","+1122334455","Test Plan",SubscriberStatus.PENDING);
      
        when(subscriberService.createSubscriber(request)).thenThrow(new DuplicateMsisdnException(request.msisdn()));

        mockMvc.perform(post("/api/v1/subscribers").contentType(MediaType.APPLICATION_JSON).content(jsonMapper.writeValueAsString(request)))
        .andExpect(status().isConflict());

        verify(subscriberService).createSubscriber(request);

    }

    @Test 
    void shouldCreateSubscriber_nameIsBlankBadRequest() throws Exception
    {

        SubscriberRequestDto request = new SubscriberRequestDto("","+1122334455","Test Plan",SubscriberStatus.PENDING);
      
        mockMvc.perform(post("/api/v1/subscribers").contentType(MediaType.APPLICATION_JSON).content(jsonMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.name",org.hamcrest.Matchers.hasItem("Subscriber name is mandatory.")));

        verify(subscriberService,never()).createSubscriber(request);

    }


    @Test 
    void shouldUpdateSubscriber_success() throws Exception
    {
        Long subscriberId=100L;

        SubscriberResponseDto response = new SubscriberResponseDto(subscriberId,"Test Name","+1122334455","Test Plan",SubscriberStatus.PENDING,null);

        SubscriberRequestDto request = new SubscriberRequestDto("Test Name","+1122334455","Test Plan",SubscriberStatus.PENDING);
      
        when(subscriberService.updateSubscriber(subscriberId,request)).thenReturn(response);

        mockMvc.perform(put("/api/v1/subscribers/{id}",subscriberId).contentType(MediaType.APPLICATION_JSON).content(jsonMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(100))
        .andExpect(jsonPath("$.name").value("Test Name"))
        .andExpect(jsonPath("$.msisdn").value("+1122334455"))
        .andExpect(jsonPath("$.status").value("PENDING"));

        verify(subscriberService).updateSubscriber(subscriberId,request);

    }


}
