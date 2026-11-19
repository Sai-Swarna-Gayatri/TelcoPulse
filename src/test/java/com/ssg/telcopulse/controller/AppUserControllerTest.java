package com.ssg.telcopulse.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.ssg.telcopulse.dto.AppUserRequestDto;
import com.ssg.telcopulse.dto.AppUserResponseDto;
import com.ssg.telcopulse.exception.DuplicateUserException;
import com.ssg.telcopulse.model.UserRole;
import com.ssg.telcopulse.service.AppUserService;
import tools.jackson.databind.json.JsonMapper;

@WebMvcTest(AppUserController.class)
public class AppUserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired 
    JsonMapper jsonMapper;

    @MockitoBean
    AppUserService appUserService;
    
    @Test 
    void shouldCreateUser_success() throws Exception
    {
        Long id = 100L;
        AppUserRequestDto request = new AppUserRequestDto("TestUserA", "testPassw0rd@1",UserRole.USER);

        AppUserResponseDto response = new AppUserResponseDto(id, "TestUserA",UserRole.USER);

        when(appUserService.createUser(request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(jsonMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.userName").value("TestUserA"))
        .andExpect(jsonPath("$.role").value("USER"))
        .andExpect(jsonPath("$.id").value(100));

        verify(appUserService).createUser(request);

    }


    @Test 
    void shouldCreateUser_duplicateUser() throws Exception
    {
        Long id = 100L;
        AppUserRequestDto request = new AppUserRequestDto("TestUserA", "testPassw0rd@123",UserRole.USER);

        when(appUserService.createUser(request)).thenThrow(DuplicateUserException.class);

         mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(jsonMapper.writeValueAsString(request)))
        .andExpect(status().isConflict());
        
        verify(appUserService).createUser(request);

    }

}
