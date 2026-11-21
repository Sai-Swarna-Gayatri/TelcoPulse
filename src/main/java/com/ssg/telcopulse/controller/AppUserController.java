package com.ssg.telcopulse.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ssg.telcopulse.dto.AppUserRequestDto;
import com.ssg.telcopulse.dto.AppUserResponseDto;
import com.ssg.telcopulse.dto.LoginRequestDto;
import com.ssg.telcopulse.service.AppUserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AppUserController {

    AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<AppUserResponseDto> createUser(@RequestBody @Valid AppUserRequestDto userDto)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(appUserService.createUser(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@RequestBody LoginRequestDto request)
    {
        appUserService.loginUser(request);
        return ResponseEntity.ok().build();
    }

}
