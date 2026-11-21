package com.ssg.telcopulse.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.ssg.telcopulse.dto.AppUserMapper;
import com.ssg.telcopulse.dto.AppUserRequestDto;
import com.ssg.telcopulse.dto.AppUserResponseDto;
import com.ssg.telcopulse.dto.LoginRequestDto;
import com.ssg.telcopulse.model.AppUser;
import com.ssg.telcopulse.repository.AppUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.ssg.telcopulse.exception.DuplicateUserException;

@Service
public class AppUserService {

    private final AppUserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private static final Logger log = LoggerFactory.getLogger(AppUserService.class);

    public AppUserService(AppUserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public AppUserResponseDto createUser(AppUserRequestDto request) {
        log.info("Creating a {} user", request.role());
        if (checkExistingUserName(request.userName())) {
            throw new DuplicateUserException(request.userName());
        }
        AppUser userToCreate = AppUserMapper.toEntity(request);
        userToCreate.setPassword(passwordEncoder.encode(userToCreate.getPassword()));

        AppUser createdUser = userRepository.save(userToCreate);
        log.debug("User created with id {} and role {}", createdUser.getId(), createdUser.getRole());
        return AppUserMapper.toDto(createdUser);
    }

    private boolean checkExistingUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    public void loginUser(LoginRequestDto request) {
      Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.userName(), request.password()));
    }
}
