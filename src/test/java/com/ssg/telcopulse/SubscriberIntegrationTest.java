package com.ssg.telcopulse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.ssg.telcopulse.dto.SubscriberRequestDto;
import com.ssg.telcopulse.dto.SubscriberResponseDto;
import com.ssg.telcopulse.model.SubscriberStatus;
import com.ssg.telcopulse.repository.SubscriberRepository;
import jakarta.transaction.Transactional;
import tools.jackson.databind.json.JsonMapper;

@SpringBootTest 
@ActiveProfiles("test")
@AutoConfigureMockMvc 
@Transactional 
public class SubscriberIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @Autowired 
    private SubscriberRepository subscriberRepository;


    @Test 
    void shouldCreateSubscriber() throws Exception
    {
        SubscriberRequestDto request = new SubscriberRequestDto("Test Name", "+1122334455", "Test Plan",SubscriberStatus.PENDING);

        //real request that goes through service and repository
        MvcResult result=mockMvc.perform(post("/api/v1/subscribers").contentType(MediaType.APPLICATION_JSON).content(jsonMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("Test Name"))
        .andExpect(jsonPath("$.msisdn").value("+1122334455")).andReturn();

        //Retrieving response body and its contents and then getting subscriber id generated
        String responseBody=result.getResponse().getContentAsString();

        SubscriberResponseDto responseDto=jsonMapper.readValue(responseBody,SubscriberResponseDto.class);

        Long subscriberId = responseDto.id();
      

        //checking if the requested data actually persisted
        assertTrue(subscriberRepository.existsByMsisdn("+1122334455"));

        //Get Request to fetch the created subscriber details
        mockMvc.perform(get("/api/v1/subscribers/{id}",subscriberId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Test Name"))
        .andExpect(jsonPath("$.msisdn").value("+1122334455"))
        .andExpect(jsonPath("$.status").value("PENDING"))
        .andExpect(jsonPath("$.planName").value("Test Plan"));

    }


    @Test 
    void shouldUpdateExistingSubscriber() throws Exception
    {
        SubscriberRequestDto request = new SubscriberRequestDto("Test Namee", "+1234567890", "Test Plan",SubscriberStatus.PENDING);

        //Creating new Subscriber
        MvcResult result = mockMvc.perform(post("/api/v1/subscribers").contentType(MediaType.APPLICATION_JSON).content(jsonMapper.writeValueAsString(request)))
        .andExpect(status().isCreated()).andDo(print()).andReturn();

        String responseBody = result.getResponse().getContentAsString();

        SubscriberResponseDto response = jsonMapper.readValue(responseBody, SubscriberResponseDto.class);

        Long subscriberId = response.id();

        SubscriberRequestDto updatedSubscriber = new SubscriberRequestDto("New Name", "+1234567890", "New Plan", SubscriberStatus.PENDING);

        //Updating the subscriber
        mockMvc.perform(put("/api/v1/subscribers/{id}",subscriberId).contentType(MediaType.APPLICATION_JSON).content(jsonMapper.writeValueAsString(updatedSubscriber)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("New Name"))
        .andExpect(jsonPath("$.planName").value("New Plan"));

        //Fetching subscriber details to verify its actually updated
        mockMvc.perform(get("/api/v1/subscribers/{id}",subscriberId))
        .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("New Name"))
        .andExpect(jsonPath("$.planName").value("New Plan"));

        //verifying data is actually persisted in db
        assertTrue(subscriberRepository.existsByMsisdn(response.msisdn()));  
                
    }

}
