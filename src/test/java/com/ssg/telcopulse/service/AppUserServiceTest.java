package com.ssg.telcopulse.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.ArgumentMatchers.argThat;
import com.ssg.telcopulse.dto.AppUserRequestDto;
import com.ssg.telcopulse.dto.AppUserResponseDto;
import com.ssg.telcopulse.exception.DuplicateUserException;
import com.ssg.telcopulse.model.AppUser;
import com.ssg.telcopulse.model.UserRole;
import com.ssg.telcopulse.repository.AppUserRepository;

@ExtendWith(MockitoExtension.class) 
public class AppUserServiceTest {

    @Mock 
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks 
    private AppUserService appUserService;


    @Test
    void shouldCreateUser_success()
    {
        Long id = 100L;
        AppUserRequestDto request = new AppUserRequestDto("TestUserA", "testPassw0rd@1",UserRole.USER);

        when(appUserRepository.existsByUserName(request.userName())).thenReturn(false);
        
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");

        when(appUserRepository.save(any(AppUser.class))).thenAnswer(invocation -> 
           { AppUser userToCreate = invocation.getArgument(0);
                userToCreate.setId(id);
                return userToCreate;      
         });
   
        AppUserResponseDto response = appUserService.createUser(request);
        assertEquals("TestUserA",response.userName());
        assertEquals(id,response.id());
        assertEquals(UserRole.USER,response.role());

        verify(appUserRepository).existsByUserName(request.userName());
        verify(appUserRepository).save(argThat(user->user.getPassword().equals("encodedPassword")));
        verify(appUserRepository).save(any(AppUser.class));
        
    }

    @Test
    void shouldCreateUser_duplicateUser()
    {
        AppUserRequestDto request = new AppUserRequestDto("TestUserA", "testPassw0rd@12",UserRole.USER);

        when(appUserRepository.existsByUserName(request.userName())).thenReturn(true);

        DuplicateUserException exception = assertThrows(DuplicateUserException.class,()-> appUserService.createUser(request));
        
        assertEquals("UserName "+ request.userName() +" already exists. Please select a different user name.",exception.getMessage());
       
        verify(appUserRepository).existsByUserName(request.userName());
        verify(passwordEncoder, never()).encode(anyString());
        verify(appUserRepository,never()).save(any(AppUser.class));
   
    }

}
